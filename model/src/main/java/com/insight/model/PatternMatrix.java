package com.insight.model;

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

    @Id
    public String index;

    public String stockName;

    public long seq;

    public Date buyingDate;

    public Date sellingDate;

    public RealMatrix diffMeanMatrix;

    public RealMatrix diffMeanMatrixNorm;

    public Double max;

    public Double min;

    public Double mean;


    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }
    public String getIndex() {
        return getStockName() + Constants.DELIMITER + getSeq();
    }

    public void setIndex() {
        this.index = getStockName() + Constants.DELIMITER + getSeq();
    }

    public RealMatrix getDiffMeanMatrixNorm() {
        return diffMeanMatrixNorm;
    }

    public void setDiffMeanMatrixNorm(RealMatrix diffMeanMatrixNorm) {
        this.diffMeanMatrixNorm = diffMeanMatrixNorm;
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
}
