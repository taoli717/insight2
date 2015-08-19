package com.insight.generator.parser;

import com.insight.model.StockModel;

/**
 * Created by tli on 10/28/2014.
 */
public interface StockParser {
    StockModel parseStockModelFromJSON(String input) throws Exception;
}
