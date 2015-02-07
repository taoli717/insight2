package com.insight.generator.dao;


import com.insight.generator.model.RawPatternModel;

/**
 * Created by PC on 11/29/2014.
 */
public interface RawPatternDao {
    public boolean save(RawPatternModel rpm);

    long getNextSequenceId(String tableName) throws Exception;

    Object load(int seqIndex, String tableName);
}
