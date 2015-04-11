package com.insight.model;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by PC on 3/22/2015.
 */
@Document(collection = "aggregation")
public class Aggregation {

    @Id
    private String index;

    private RealMatrix mask;

    private String stockName;

    private Date buyingDate;

    private Date sellingDate;

    public long seq;

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getPatternSignature() {
        return patternSignature;
    }

    public void setPatternSignature(String patternSignature) {
        this.patternSignature = patternSignature;
    }

    private String patternSignature;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public RealMatrix getMask() {
        return mask;
    }

    public void setMask(RealMatrix mask) {
        this.mask = mask;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(Date buyingDate) {
        this.buyingDate = buyingDate;
    }

    public Date getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(Date sellingDate) {
        this.sellingDate = sellingDate;
    }
}
