package com.insight.generator.service;

import com.insight.model.RawPatternModel;
import com.insight.model.StockModel;

import java.util.Date;

/**
 * Created by tli on 1/4/2015.
 */
public interface StockParserService {
    void setTargetPect(double targetPect);
    void parse() throws Exception;
    public RawPatternModel getRPMWithDate4Testing(StockModel sm, Date date) throws Exception;
}
