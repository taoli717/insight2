package com.insight.generator.retriever;


import com.insight.model.StockModel;

/**
 * Created by tli on 11/16/2014.
 */
public interface PriceRetriever {
    public StockModel sendGet(String stockName, String days, long i) throws Exception;
}
