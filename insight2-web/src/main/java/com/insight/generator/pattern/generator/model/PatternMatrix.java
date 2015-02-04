package com.insight.generator.pattern.generator.model;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by PC on 2/1/2015.
 */
@Document(collection = "pattern_matrix")
public class PatternMatrix {

    public static final String DELIMITER = "*#*";

    @Id
    public String index;

    public String stockName;

    public long seq;

    public Date buyingDate;

    public Date sellingDate;

    public RealMatrix diffMeanMatrix;

    public RealMatrix percentMatrix;
    public double fistDayPrice;

    public double meanValue;

    public RealVector percentMatrixNorm;

    public RealVector diffMeanMatrixNorm;

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return getStockName() + DELIMITER + getSeq();
    }

    public RealVector getDiffMeanMatrixNorm() {
        return diffMeanMatrixNorm;
    }

    public void setDiffMeanMatrixNorm(RealVector diffMeanMatrixNorm) {
        this.diffMeanMatrixNorm = diffMeanMatrixNorm;
    }

    public RealVector getPercentMatrixNorm() {
        return percentMatrixNorm;
    }

    public void setPercentMatrixNorm(RealVector percentMatrixNorm) {
        this.percentMatrixNorm = percentMatrixNorm;
    }

    public void setIndex() {
        this.index = getStockName() + DELIMITER + getSeq();
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
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

    public RealMatrix getDiffMeanMatrix() {
        return diffMeanMatrix;
    }

    public void setDiffMeanMatrix(RealMatrix diffMeanMatrix) {
        this.diffMeanMatrix = diffMeanMatrix;
    }

    public RealMatrix getPercentMatrix() {
        return percentMatrix;
    }

    public void setPercentMatrix(RealMatrix percentMatrix) {
        this.percentMatrix = percentMatrix;
    }

    public double getFistDayPrice() {
        return fistDayPrice;
    }

    public void setFistDayPrice(double fistDayPrice) {
        this.fistDayPrice = fistDayPrice;
    }

    public double getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(double meanValue) {
        this.meanValue = meanValue;
    }
}
