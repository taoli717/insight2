package generator.dao;

import com.mongodb.DBCursor;
import generator.model.RawPatternModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by PC on 11/29/2014.
 */
@Service
public class RawPatternDaoImp implements RawPatternDao {

    private static final Logger logger = Logger.getLogger(RawPatternDaoImp.class);
    private static DBCursor cursor;
    static long seqIndex = 1;

    @Autowired
    private MongoOperations mongoOperation;

    @Autowired
    private MongoTemplate mongoTemplate;

    private boolean localCaced;

    public boolean save(RawPatternModel rpm){
        boolean isSaved = true;
        try{
            //TODO remove if block eventually
            if(!localCaced){
                Query query = new Query();
                query.addCriteria(Criteria.where("seq").is(1));
                RawPatternModel dbSm = null;
                try{
                    dbSm = mongoOperation.findOne(query, RawPatternModel.class);
                }catch (Exception e){
                    logger.error(e);
                }
                if(dbSm==null){
                    rpm.setSeq(1);
                    mongoOperation.save(rpm);
                }else{
                    localCaced = true;
                }
            }
            //TODO make sure stock code is unique
            long i = getNextSequenceId(rpm.getStockName());
            rpm.setSeq(i);
            mongoOperation.save(rpm, rpm.getStockName());
        }catch (Exception e){
            logger.error(e);
            isSaved = false;
        }
        return isSaved;
    }

    @Override
    public long getNextSequenceId(String tableName) throws Exception {
        //get sequence id
        Query query = new Query();
        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);
        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        //this is the magic happened.
        Long total = mongoOperation.count(null,tableName);
       // RawPatternModel sm =  mongoOperation.findAndModify(query, update, options, RawPatternModel.class);
        //if no id, throws
        //optional, just a way to tell user when the sequence id is failed to generate.
        return total;
    }

    //TODO need to rewrite it to loop all tables
    @Override
    public Object load(int seqIndex, String tableName) {
        if(tableName == null || StringUtils.isEmpty(tableName)){
            tableName = "B";
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("seq").is(seqIndex));
        RawPatternModel dbSm = (RawPatternModel) mongoOperation.find(query, RawPatternModel.class, tableName).get(0);
        return dbSm;
    }
}
