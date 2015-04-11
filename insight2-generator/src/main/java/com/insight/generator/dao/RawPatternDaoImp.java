package com.insight.generator.dao;

import com.insight.model.Constants;
import com.insight.model.RawPatternModel;
import com.mongodb.DBCursor;
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

import java.util.List;

/**
 * Created by PC on 11/29/2014.
 */
@Service
public class RawPatternDaoImp implements RawPatternDao {

    private static final Logger logger = Logger.getLogger(RawPatternDaoImp.class);

    public static final String TEST_STOCK_NAME = "B";
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
/*            //TODO remove if block eventually
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
                    //rpm.setSeq(1l);
                    mongoOperation.save(rpm);
                }else{
                    localCaced = true;
                }
            }
            //TODO make sure stock code is unique
            long i = getNextSequenceId(rpm.getStockName());*/
            //rpm.setSeq(i);
            mongoOperation.save(rpm);
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
    public RawPatternModel load(long seqIndex, String tableName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(tableName + Constants.DELIMITER + seqIndex));
        List<RawPatternModel> list =  mongoOperation.find(query, RawPatternModel.class);
        RawPatternModel dbSm = null;
        if(list!=null && list.size()!= 0){
            dbSm = list.get(0);
        }
        return dbSm;
    }
}
