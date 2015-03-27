package com.insight.generator.aggregate.dao;

import com.insight.generator.aggregate.model.PatternPrototype;
import com.insight.generator.pattern.generator.model.PatternMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 3/23/2015.
 */
@Service
public class PrototypeDaoImpl implements PrototypeDao {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public PatternPrototype get(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        PatternPrototype pp = (PatternPrototype) mongoOperation.findOne(query, PatternPrototype.class);
        return pp;
    }

    @Override
    public void save(PatternPrototype pp) {
        mongoOperation.save(pp);
    }


}
