package com.insight.generator.dao;

import com.insight.model.StockModel;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by tli on 11/7/2014.
 */
public interface StockDao {
    boolean save(StockModel sm);

    StockModel load(String stockName);
    Object loadNext();
    long getNextSequenceId(String key) throws Exception;
    boolean isDBExist(String DBName);
    StockModel getNextStock();
}
