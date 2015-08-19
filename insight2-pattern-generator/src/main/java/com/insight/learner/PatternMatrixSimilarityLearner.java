package com.insight.learner;

import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.dao.PatternMatrixDao;
import com.insight.generator.dao.StockDao;
import com.insight.generator.matching.service.PatternCosineSimilarityService;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockParserService;
import com.insight.generator.service.StockParserServiceImpl;
import com.insight.validation.AbstractValidation;
import com.insight.model.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by tli on 4/12/2015.
 */
@Service
@Scope("prototype")
public class PatternMatrixSimilarityLearner extends AbstractValidation{

    @Autowired
    StockParserService stockParserService;

    @Autowired
    StockDao stockDao;

    @Autowired
    PatternMatrixService patternMatrixService;

    @Autowired
    PatternAggregateService patternAggregateService;

    @Autowired
    PatternCosineSimilarityService patternCosineSimilarityService;

    @Autowired
    PrototypeDao prototypeDao;

    @Autowired
    PatternMatrixDao patternMatrixDao;

    private static Logger logger = Logger.getLogger(PatternMatrixSimilarityLearner.class);

    private static final double learningRate = 0.02;

    @Override
    public void run() {
        validatePatternMatrix();
    }

    // TODO use reactive style
    @Override
    public void validateSignature(Date date, String prototype){
    }

    @Override
    public void validateSignature(){
    }

    @Override
    public void validatePatternMatrix(String patternMatixIndex){//crappy, need to change
        logger.trace("validating " + getPatternMatrixIndex() + " " + getPriceSimilarityThreshold() + " " + getVolumeSimilarityThreshold() + " " + getSellingTarget());
        PatternMatrix pm = patternMatrixDao.get(getPatternMatrixIndex());
        double y = 0l;
        boolean firstRun = true;
        for(int i=0; i<=63; i++) {
            boolean keepLoop = true;
            boolean initialAttempt = true;
            boolean incremental = true;
            while(keepLoop){
                successCount = 0;
                totalCount = 0;
                if (initialAttempt  && !firstRun && i!=64) {
                    RealMatrix rm = pm.getDiffMeanMatrix();
                    rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i] + learningRate);
                    pm.setDiffMeanMatrix(rm);
                }
                for (String stockName : getTestStockPool()) {
                    try {
                        StockModel sm = stockDao.load(stockName);
                        if (sm != null) {
                            for (Date date : sm.getDailyStocks().keySet()) {
                                PatternMatrix pm2 = null;
                                RawPatternModel rpm = stockParserService.getRPMWithDate4Testing(sm, date);
                                if (rpm != null)
                                    pm2 = patternMatrixService.parseRawPatternModel(rpm, 0);
                                if (pm2 != null) {
                                    double priceSim = patternCosineSimilarityService.comparePrice(pm, pm2);
                                    double volumeSim = patternCosineSimilarityService.compareVolumn(pm, pm2);
                                    if (priceSim > getPriceSimilarityThreshold() && volumeSim > getVolumeSimilarityThreshold()) {
                                        totalCount++;
                                        boolean isSuccess = isSuccess(sm, pm2.getBuyingDate());
                                        logger.trace("priceSim: " + getPriceSimilarityThreshold() + " volumeSim: " + getVolumeSimilarityThreshold() + ", " + pm.getIndex() + " against " + pm2.getStockName() + " Date: " + date + " rate: " + successCount / totalCount + ", Total: " + totalCount);
                                        logger.trace("=================================================================================");
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        logger.error("unable to validate", e);
                        logger.error(stockName + " skipped");
                        logger.error("=================================================================================");
                    }
                }
                if(firstRun){
                    firstRun = false;
                    y = (successCount * 10000/totalCount);
                    logCurrent(i, pm, incremental,  successCount, totalCount, "first run");
                    continue;
                }

                logCurrent(i, pm, incremental,  successCount, totalCount, i==64?"final run":"");
                if ( y >= (successCount * 10000/totalCount) ) {
                    if(initialAttempt){
                        RealMatrix rm = pm.getDiffMeanMatrix();
                        rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i] - learningRate*2);
                        pm.setDiffMeanMatrix(rm);
                        incremental = false;
                        initialAttempt = false;
                        y = (successCount * 10000/totalCount);
                        continue;
                    }
                    keepLoop = false;
                    if(incremental){
                        RealMatrix rm = pm.getDiffMeanMatrix();
                        rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i]-learningRate);
                        pm.setDiffMeanMatrix(rm);
                    }else{
                        RealMatrix rm = pm.getDiffMeanMatrix();
                        rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i]+learningRate);
                        pm.setDiffMeanMatrix(rm);
                    }
                } else {
                    if(initialAttempt){
                        initialAttempt = false;
                    }
                    if(incremental){
                        RealMatrix rm = pm.getDiffMeanMatrix();
                        rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i]+learningRate);
                        pm.setDiffMeanMatrix(rm);
                    }else{
                        RealMatrix rm = pm.getDiffMeanMatrix();
                        rm.setEntry(5, i, pm.getDiffMeanMatrix().getRow(5)[i]-learningRate);
                        pm.setDiffMeanMatrix(rm);
                    }
                }
                y = (successCount * 10000/totalCount);
            }
        }

        logCurrent(64, pm, false, successCount, totalCount, "last run");
    }

    public static void logCurrent(int iteration, PatternMatrix pm, boolean incremetal, double success, double total, String extra ){
        logger.info("==================================== " + extra==null||extra.isEmpty()?"":(extra + ", ") + pm.getIndex() + " ================================");
        logger.info("Rate:" + round(success/total*100, 2) + ", Success Total#:" + success);
        logger.info("Incremental:" + incremetal +", i:" + iteration);
        logger.info(Arrays.toString(pm.getDiffMeanMatrix().getRow(5)));
        logger.info("");
    }

    @Override
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
            Double sellingTarget = buyingPrice*(getSellingTarget());
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


    @Override
    public void validatePatternMatrix() {
        validatePatternMatrix(null);
    }

    public boolean comparePatternMatrix(PatternMatrix pm, Date date, StockModel sm) throws Exception {
        return false;
    }

    public boolean comparePatternMatrixProxy(PatternMatrix pm, Date date, StockModel sm) throws Exception {
        return false;
    }

    public static void logDetails(String s){

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
