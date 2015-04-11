package com.insight.generator.service;

import com.insight.model.PatternMatrix;
import com.insight.model.RawPatternModel;

/**
 * Created by PC on 2/1/2015.
 */
public interface PatternMatrixService {

    PatternMatrix parseRawPatternModel(RawPatternModel rpm, long seq) throws Exception;

    void savePatternMatrix(PatternMatrix pm);

    public void parse() throws Exception;
}
