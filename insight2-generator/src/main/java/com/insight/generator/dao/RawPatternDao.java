package com.insight.generator.dao;


import com.insight.model.RawPatternModel;

/**
 * Created by PC on 11/29/2014.
 */
public interface RawPatternDao {
    public boolean save(RawPatternModel rpm);

    long getNextSequenceId(String tableName) throws Exception;

    Object load(long seqIndex, String tableName);
}
