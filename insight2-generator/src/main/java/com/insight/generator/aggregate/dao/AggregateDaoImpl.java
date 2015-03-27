package com.insight.generator.aggregate.dao;

import com.insight.generator.aggregate.model.Aggregation;
import com.insight.generator.matching.model.PatternCosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 3/22/2015.
 */
@Service
public class AggregateDaoImpl implements AggregateDao {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public boolean save(Aggregation pcs) {
        mongoOperation.save(pcs);
        return true;
    }
}
