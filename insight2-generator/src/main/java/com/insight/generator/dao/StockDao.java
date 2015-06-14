package com.insight.generator.dao;

import com.insight.model.StockModel;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by PC on 11/7/2014.
 */
public interface StockDao {
    public boolean save(StockModel sm);

    @Cacheable(value="loadStock", key="#stockName")
    public StockModel load(String stockName);
    public Object loadNext();
    public long getNextSequenceId(String key) throws Exception;
    public boolean isDBExist(String DBName);
    public StockModel getNextStock();
}
