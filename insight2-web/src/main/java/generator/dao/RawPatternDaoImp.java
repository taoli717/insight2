package generator.dao;

import com.mongodb.DBCursor;
import generator.model.RawPatternModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 11/29/2014.
 */
@Service
public class RawPatternDaoImp implements RawPatternDao {

    private static DBCursor cursor;
    static long seqIndex = 1;

    @Autowired
    private MongoOperations mongoOperation;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean save(RawPatternModel rpm) {
        try {
            long i = getNextSequenceId();
            rpm.setSeq(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mongoOperation.save(rpm);
        return true;
    }

    @Override
    public long getNextSequenceId() throws Exception {
        //get sequence id
        Query query = new Query();
        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);
        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        //this is the magic happened.
        Long total = mongoOperation.count(null,RawPatternModel.class);
       // RawPatternModel sm =  mongoOperation.findAndModify(query, update, options, RawPatternModel.class);
        //if no id, throws
        //optional, just a way to tell user when the sequence id is failed to generate.
        return total;
    }
    @Override
    public Object loadNext(int seqIndex) {
        Query query = new Query();
        query.addCriteria(Criteria.where("seq").is(seqIndex));

        RawPatternModel dbSm = mongoOperation.findOne(query, RawPatternModel.class);
        return dbSm;
    }
}
