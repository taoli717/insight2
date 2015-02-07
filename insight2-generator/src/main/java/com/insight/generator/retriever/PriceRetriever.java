package com.insight.generator.retriever;


import com.insight.generator.model.StockModel;

/**
 * Created by PC on 11/16/2014.
 */
public interface PriceRetriever {
    public StockModel sendGet(String stockName, String days) throws Exception;
}
