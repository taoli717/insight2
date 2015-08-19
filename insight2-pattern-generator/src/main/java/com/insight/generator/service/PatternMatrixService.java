package com.insight.generator.service;

import com.insight.model.PatternMatrix;
import com.insight.model.RawPatternModel;

/**
 * Created by tli on 2/1/2015.
 */
public interface PatternMatrixService {

    PatternMatrix parseRawPatternModel(RawPatternModel rpm, long seq) throws Exception;

    void savePatternMatrix(PatternMatrix pm);

    void parse() throws Exception;
}
