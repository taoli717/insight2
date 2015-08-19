package com.insight.generator.matching.service;

import com.insight.model.PatternCosineSimilarity;
import com.insight.model.PatternMatrix;

/**
 * Created by tli on 3/3/2015.
 */
public interface PatternCosineSimilarityService {

    PatternCosineSimilarity compare(PatternMatrix patternMatrix, PatternMatrix patternMatrix2, PatternCosineSimilarity pcs);
    double comparePrice(PatternMatrix patternMatrix, PatternMatrix patternMatrix2);
    double compareVolumn(PatternMatrix patternMatrix, PatternMatrix patternMatrix2);

}
