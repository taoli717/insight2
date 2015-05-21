package com.insight.generator.matching.dao;

import com.insight.model.PatternCosineSimilarity;
import com.insight.model.PatternPrototype;

/**
 * Created by PC on 3/4/2015.
 */
public interface PatternCosineSimilarityDao {

    boolean save(PatternCosineSimilarity pcs);

    public PatternCosineSimilarity get(String id);
}
