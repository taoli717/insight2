package com.insight.generator.service;

/**
 * Created by tli on 1/4/2015.
 */
public interface StockDataGeneratorService {
    void generate() throws Exception;
    void synchronizeStockData();
}
