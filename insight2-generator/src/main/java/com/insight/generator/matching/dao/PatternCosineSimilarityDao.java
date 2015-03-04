package com.insight.generator.matching.dao;

import com.insight.generator.matching.model.PatternCosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by PC on 3/4/2015.
 */
public interface PatternCosineSimilarityDao {

    boolean save(PatternCosineSimilarity pcs);

}
