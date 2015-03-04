package com.insight.generator.matching.service;

import com.insight.generator.matching.model.PatternCosineSimilarity;
import com.insight.generator.pattern.generator.model.PatternMatrix;

/**
 * Created by PC on 3/3/2015.
 */
public interface PatternCosineSimilarityService {

    PatternCosineSimilarity compare(PatternMatrix patternMatrix, PatternMatrix patternMatrix2, PatternCosineSimilarity pcs);

}
