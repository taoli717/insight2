package com.insight.generator.aggregate.dao;

import com.insight.generator.aggregate.model.PatternPrototype;

/**
 * Created by PC on 3/23/2015.
 */
public interface PrototypeDao {
    public PatternPrototype get(String id);
    public void save(PatternPrototype pp);
}
