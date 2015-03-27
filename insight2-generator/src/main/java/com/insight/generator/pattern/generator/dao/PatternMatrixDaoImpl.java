package com.insight.generator.pattern.generator.dao;

import com.insight.generator.matching.model.PatternCosineSimilarity;
import com.insight.generator.pattern.generator.model.PatternMatrix;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 2/1/2015.
 */
@Service
public class PatternMatrixDaoImpl implements PatternMatrixDao {

    @Autowired
    private MongoOperations mongoOperation;

    private DBCursor cursor;

    private long seq = 0l;

    @Override
    public PatternMatrix get(String index) {
        Query query = new Query();
        query.addCriteria(Criteria.where("index").is(index));
        PatternMatrix pm = (PatternMatrix) mongoOperation.findOne(query, PatternMatrix.class);
        return pm;
    }

    @Override
    public PatternMatrix get(String code, long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        query.addCriteria(Criteria.where("id").is(id));
        PatternMatrix pm = (PatternMatrix) mongoOperation.findOne(query, PatternMatrix.class);
        return pm;
    }

    @Override
    public PatternMatrix get(long seq) {
        Query query = new Query();
        query.addCriteria(Criteria.where("seq").is(seq));
        PatternMatrix pm = (PatternMatrix) mongoOperation.findOne(query, PatternMatrix.class);
        return pm;
    }

    @Override
    public void save(PatternMatrix matrix) {
        mongoOperation.save(matrix);
    }


    @Override
    public PatternMatrix getNext(){
        return get(seq++);
    }

    private DBCursor getCursor(){
        if(this.cursor == null) {
            DBCollection collection = mongoOperation.getCollection("pattern_cosine_similarity");
            this.cursor = collection.find();
        }
        return this.cursor;
    }

    private void updateCursor(){

    }

}
