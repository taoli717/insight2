package com.insight.generator.pattern.generator.service;

import com.insight.generator.model.RawPatternModel;
import com.insight.generator.pattern.generator.model.PatternMatrix;

/**
 * Created by PC on 2/1/2015.
 */
public interface PatternMatrixService {

    PatternMatrix parseRawPatternModel(RawPatternModel rpm);

    void savePatternMatrix(PatternMatrix pm);
}
