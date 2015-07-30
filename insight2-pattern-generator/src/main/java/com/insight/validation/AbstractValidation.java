package com.insight.validation;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.service.StockParserServiceImpl;
import com.insight.model.StockModel;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by PC on 5/20/2015.
 */
public abstract class AbstractValidation implements Validation, Runnable{

    double priceSimilarityThreshold = 0.90;
    double volumeSimilarityThreshold = 0.40;
    double SELLING_TARGET = 1.10;
    long SAMPLING_POOL = 1000;
    public long successCount = 0;
    public long totalCount = 0;
    public long totalTraverseCount = 0l;
    static Logger logger = Logger.getLogger(AbstractValidation.class);

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
        LinkedList<Date> dates = new LinkedList<>(sm.getDailyStocks().keySet().stream().sorted((d1, d2) ->
                        d1.compareTo(d2)
        ).collect(Collectors.toList()));
        int buyingIndex = dates.indexOf(buyingdate);
        if(buyingIndex != -1 &&
                (dates.size()-1) >= (buyingIndex + StockParserServiceImpl.holdingPeriod)){
            LinkedList<Date> holdingList = new LinkedList(dates.subList(buyingIndex+1, buyingIndex + StockParserServiceImpl.holdingPeriod+1));
            Double buyingPrice = Double.valueOf(sm.getDailyStocks().get(buyingdate).getClose());
            Double sellingTarget = buyingPrice*(SELLING_TARGET);
            correctMatch = holdingList.stream().filter(a->{
                Double currentPrice = Double.valueOf(sm.getDailyStocks().get(a).getHigh());
                if(sellingTarget <= currentPrice){
                    logger.trace("Success, " + a + " buying price: " + buyingPrice + ". selling price: " + currentPrice);
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
        boolean success = toContinue();
        if(success){
            logger.info("FinalSuccess: " + this.patternMatrixIndex + ", prototype: " + prototype + ", priceSim: " + getPriceSimilarityThreshold() + " volumnSim: " + getVolumeSimilarityThreshold() + ", target: " + getSellingTarget()
                    + ", success rate: " + (successCount*10000/totalCount)/100 + ", total: " + totalCount + " totalTraverseCount: " + totalTraverseCount);
        }else{
            logger.info("Aborted: " + this.patternMatrixIndex + ", prototype: " + prototype + ", priceSim: " + getPriceSimilarityThreshold() + " volumnSim: " + getVolumeSimilarityThreshold() + ", target: " + getSellingTarget()
                    + ", success rate: " + (successCount*10000/totalCount)/100 + ", total: " + totalCount  + " totalTraverseCount: " + totalTraverseCount);
        }
    }

    public boolean toContinue(){
        if(totalCount<4){
            return true;
        }else if(totalCount<10){
            if(successCount/totalCount<0.15){
                return false;
            }
        }else if(totalCount<20){
            if(successCount/totalCount<0.2){
                return false;
            }
        }else if(totalCount<50){
            if(successCount/totalCount<0.25){
                return false;
            }
        }else if(successCount/totalCount<0.30){
            return false;
        }
        return true;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

}
