package com.insight.generator.validation;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.service.StockParserServiceImpl;
import com.insight.model.StockModel;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by PC on 5/20/2015.
 */
public abstract class AbstractValidation implements Validation, Runnable{

    double priceSimilarityThreshold = 0.90;
    double volumeSimilarityThreshold = 0.90;
    double SELLING_TARGET = 1.10;
    long SAMPLING_POOL = 1000;
    public double successCount = 0;
    public double totalCount = 0;

    static Logger logger = Logger.getLogger(AbstractValidation.class);;

    String patternMatrixIndex;

    public String prototype;

    public String[] testStockPool = TestStockName.ALL_STOCK_NAME;

    public static int TOTAL_COUNT_LIMIT = 10;

    public double getSellingTarget() {
        return SELLING_TARGET;
    }

    public void setSellingTarget(double sellingTarget) {
        SELLING_TARGET = sellingTarget;
    }

    public long getSamplingPool() {
        return SAMPLING_POOL;
    }

    public void setSamplingPool(long SAMPLING_POOL) {
        this.SAMPLING_POOL = SAMPLING_POOL;
    }

    public String getPatternMatrixIndex() {
        return patternMatrixIndex;
    }

    public void setPatternMatrixIndex(String patternMatrixIndex) {
        this.patternMatrixIndex = patternMatrixIndex;
    }

    public String[] getTestStockPool() {
        return testStockPool;
    }

    public double getPriceSimilarityThreshold() {
        return priceSimilarityThreshold;
    }

    public void setPriceSimilarityThreshold(double priceSimilarityThreshold) {
        this.priceSimilarityThreshold = priceSimilarityThreshold;
    }


    public double getVolumeSimilarityThreshold() {
        return volumeSimilarityThreshold;
    }

    public void setVolumeSimilarityThreshold(double volumeSimilarityThreshold) {
        this.volumeSimilarityThreshold = volumeSimilarityThreshold;
    }

    public void setTestStockPool(String[] testStockPool) {
        this.testStockPool = testStockPool;
    }

    public Boolean isSuccess(StockModel sm, Date buyingdate){
        Boolean correctMatch = false;
        LinkedList<Date> dates = new LinkedList<>(sm.getDailyStocks().keySet());
        int buyingIndex = dates.indexOf(buyingdate);
        if(buyingIndex != -1 &&
                (dates.size()-1) >= (buyingIndex + StockParserServiceImpl.holdingPeriod)){
            LinkedList<Date> holdingList = new LinkedList(dates.subList(buyingIndex, buyingIndex + StockParserServiceImpl.holdingPeriod));
            Double buyingPrice = Double.valueOf(sm.getDailyStocks().get(buyingdate).getClose());
            Double sellingTarget = buyingPrice*(SELLING_TARGET);
            correctMatch = holdingList.stream().filter(a->{
                Double currentPrice = Double.valueOf(sm.getDailyStocks().get(a).getHigh());
                if(sellingTarget <= currentPrice){
                    logger.trace("Success, " + a + ". selling price: " + currentPrice);
                }
                return sellingTarget <= currentPrice;
            }).count()>0;
            if(!correctMatch){
                logger.trace("Failed");
            }else{
                logger.trace("Success");
                successCount++;
            }
        }
        return correctMatch;
    }

    void logDetails(String pmIndex){
        boolean success = successCount/totalCount > 0.5;
        if(success){
            logger.info("FinalSuccess: " + pmIndex + ", prototype: " + prototype + ", priceSim: " + getPriceSimilarityThreshold() + " volumnSim: " + getVolumeSimilarityThreshold() + ", target: " + getSellingTarget()
                    + ", success rate: " + successCount/totalCount + ", total: " + totalCount);
        }else{
            logger.info("Aborted: " + pmIndex + ", prototype: " + prototype + ", priceSim: " + getPriceSimilarityThreshold() + " volumnSim: " + getVolumeSimilarityThreshold() + ", target: " + getSellingTarget()
                    + ", success rate: " + successCount/totalCount + ", total: " + totalCount );
        }
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

}
