package com.insight.generator.parser;

import com.insight.generator.model.StockModel;

/**
 * Created by PC on 10/28/2014.
 */
public interface StockParser {
    StockModel parseStockModelFromJSON(String input) throws Exception;
}
