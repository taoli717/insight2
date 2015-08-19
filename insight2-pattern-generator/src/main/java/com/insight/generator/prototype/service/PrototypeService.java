package com.insight.generator.prototype.service;

import com.google.common.collect.Multimap;
import com.insight.model.PatternPrototype;

import java.util.List;

/**
 * Created by tli on 4/6/2015.
 */
public interface PrototypeService {
    void cleanAndReduce(int sizeThreshold );
    List<PatternPrototype> filter(long sizeThreshold);
    Multimap start() throws Exception;
}
