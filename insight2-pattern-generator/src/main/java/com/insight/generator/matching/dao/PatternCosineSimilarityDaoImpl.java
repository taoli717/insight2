package com.insight.generator.matching.dao;

import com.insight.model.PatternCosineSimilarity;
import com.insight.model.PatternPrototype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 3/4/2015.
 */
@Service
public class PatternCosineSimilarityDaoImpl implements PatternCosineSimilarityDao {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public boolean save(PatternCosineSimilarity pcs) {
        mongoOperation.save(pcs);
        return true;
    }

    @Override
    public PatternCosineSimilarity get(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        PatternCosineSimilarity pcs = (PatternCosineSimilarity) mongoOperation.findOne(query, PatternCosineSimilarity.class);
        return pcs;
    }

}
