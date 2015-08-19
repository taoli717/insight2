package com.insight.generator.dao;

import com.insight.model.PatternMatrix;

/**
 * Created by tli on 2/1/2015.
 */
public interface PatternMatrixDao {

    PatternMatrix get(String index);

    PatternMatrix get(String code, long id);

    PatternMatrix get(long seq);

    void save(PatternMatrix matrix);

    public PatternMatrix getNext();

}
