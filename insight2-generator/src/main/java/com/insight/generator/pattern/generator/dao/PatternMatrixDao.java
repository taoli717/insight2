package com.insight.generator.pattern.generator.dao;

import com.insight.generator.pattern.generator.model.PatternMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by PC on 2/1/2015.
 */
public interface PatternMatrixDao {

    PatternMatrix get(String index);

    PatternMatrix get(String code, long id);

    PatternMatrix get(long seq);

    void save(PatternMatrix matrix);

}
