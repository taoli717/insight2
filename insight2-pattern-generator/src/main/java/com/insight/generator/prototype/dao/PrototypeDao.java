package com.insight.generator.prototype.dao;

import com.insight.model.PatternPrototype;

import java.util.List;

/**
 * Created by PC on 3/23/2015.
 */
public interface PrototypeDao {
    public PatternPrototype get(String id);
    public void save(PatternPrototype pp);
    void cleanAndReduce(int sizeThreshold );
    List<PatternPrototype> filter(int sizeThreshold);
}
