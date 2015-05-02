package com.insight.generator.dao;

import com.insight.model.PatternPrototype;
import com.mongodb.DBCursor;
import com.insight.model.StockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 11/7/2014.
 */
@Service
public class StockDaoImp implements StockDao{

    private static DBCursor cursor;
    long seqIndex = 1;

    @Autowired
    private MongoOperations mongoOperation;

    @Autowired
    private MongoTemplate mongoTemplate;

    CloseableIterator<StockModel> stockModelCloseableIterator;

    @Override
    public boolean save(StockModel sm) {
        mongoOperation.save(sm);
        return true;
/*        Query query = new Query();
        query.addCriteria(Criteria.where("stockName").is(sm.getStockName()));
        StockModel dbSm = mongoOperation.findOne(query, StockModel.class);
        if(dbSm == null){
            if(autoIncrement){
                try {
                    sm.setSeq(getNextSequenceId(null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mongoOperation.save(sm);
        }else{
            Update update = new Update();
            update.set("dailyStocks", sm.getDailyStocks());
            mongoOperation.updateFirst(query, update, StockModel.class);
        }
        return true;*/
    }

    @Override
    public StockModel load(String stockName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("stockName").is(stockName));
        StockModel dbSm = mongoOperation.findOne(query, StockModel.class);
        return dbSm;
    }

// TODO need to find a ORM mapping
    @Override
    public Object loadNext() {
        Query query = new Query();
        query.addCriteria(Criteria.where("seq").is(seqIndex));
        StockModel dbSm = mongoOperation.findOne(query, StockModel.class);
        seqIndex++;
        return dbSm;
    }

    private DBCursor getCurrentStockModelDBCursor(){
        if(cursor == null){
            cursor = mongoTemplate.getCollection("stock_model").find();
        }else{
            while(cursor.hasNext()){
                return cursor;
            }
        }
        return cursor;
    }

    @Override
    public long getNextSequenceId(String key) throws Exception {
        //get sequence id
        Query query = new Query();
        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);
        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        //this is the magic happened.
        StockModel sm =
                mongoOperation.findAndModify(query, update, options, StockModel.class);
        //if no id, throws
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (sm == null) {
            throw new Exception("Unable to get sequence id for key : " + key);
        }
        return sm.getSeq();
    }

    @Override
    public boolean isDBExist(String DBName) {
        return mongoOperation.collectionExists(DBName);
    }

    @Override
    public StockModel getNextStock(){
        if(stockModelCloseableIterator == null){
            stockModelCloseableIterator = mongoOperation.stream(new Query(), StockModel.class);
        }
        if(stockModelCloseableIterator.hasNext()){
            return stockModelCloseableIterator.next();
        }else{
            stockModelCloseableIterator.close();
            return null;
        }

    }
}
