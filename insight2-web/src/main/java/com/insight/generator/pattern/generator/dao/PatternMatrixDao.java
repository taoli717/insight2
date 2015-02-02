package com.insight.generator.pattern.generator.dao;

import com.insight.generator.pattern.generator.model.PatternMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by PC on 2/1/2015.
 */
public interface PatternMatrixDao {

    RealMatrix get(String index);

    RealMatrix get(String code, long id);

    void save(PatternMatrix matrix);

}
