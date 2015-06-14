package com.insight.generator.prototype.service;

import com.insight.model.PatternPrototype;

import java.util.List;

/**
 * Created by PC on 4/6/2015.
 */
public interface PrototypeService {
    void cleanAndReduce(int sizeThreshold );
    List<PatternPrototype> filter(long sizeThreshold);
}
