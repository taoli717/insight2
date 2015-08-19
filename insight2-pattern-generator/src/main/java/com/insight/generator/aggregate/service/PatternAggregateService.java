package com.insight.generator.aggregate.service;

import com.insight.model.Aggregation;
import com.insight.model.PatternMatrix;

/**
 * Created by tli on 3/22/2015.
 */
public interface PatternAggregateService {
    void parse();
    Aggregation convertPM2Aggregation(PatternMatrix pm);
}
