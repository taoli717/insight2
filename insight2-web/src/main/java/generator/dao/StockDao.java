package generator.dao;

import generator.model.StockModel;

/**
 * Created by PC on 11/7/2014.
 */
public interface StockDao {
    public boolean save(StockModel sm, Boolean autoIncrement);
    public StockModel load(String stockName);
    public Object loadNext();
    public long getNextSequenceId(String key) throws Exception;
    public boolean isDBExist(String DBName);
}
