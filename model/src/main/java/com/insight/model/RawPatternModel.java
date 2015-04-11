package com.insight.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by PC on 10/28/2014.
 */
@Document(collection = "raw_pattern_model")
public class RawPatternModel implements Serializable{

    public RawPatternModel(){}
    @Id
    public String index;
    public String stockName;
    public LinkedHashMap<Date, DailyStockModel> dailyStocks;
    public Date buyingDate;
    public Date sellingDate;

    public Date getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(Date sellingDate) {
        this.sellingDate = sellingDate;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(Date buyingDate) {
        this.buyingDate = buyingDate;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public LinkedHashMap<Date, DailyStockModel> getDailyStocks() {
        return dailyStocks;
    }

    public void setDailyStocks(LinkedHashMap<Date, DailyStockModel> dailyStocks) {
        this.dailyStocks = dailyStocks;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
