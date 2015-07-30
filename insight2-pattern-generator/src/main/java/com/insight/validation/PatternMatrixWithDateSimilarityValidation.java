package com.insight.validation;

import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.dao.PatternMatrixDao;
import com.insight.generator.dao.StockDao;
import com.insight.generator.matching.service.PatternCosineSimilarityService;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockParserService;
import com.insight.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by PC on 4/12/2015.
 */
@Service
@Scope("prototype")
public class PatternMatrixWithDateSimilarityValidation extends AbstractValidation{

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

    private static Logger logger = Logger.getLogger(PatternMatrixWithDateSimilarityValidation.class);

    private Date date;

    @Override
    public void run() {
        validatePatternMatrix();
    }

    // TODO use reactive style
    @Override
    public void validateSignature(Date date, String prototype){
    }

    @Override
    public void validateSignature(String prototype){
    }

    @Override
    public void validatePatternMatrix(String patternMatixIndex){//crappy, need to change
        successCount = 0;
        totalCount = 0;
        logger.trace("validating " + getPatternMatrixIndex() + " " + getPriceSimilarityThreshold() + " " + getVolumeSimilarityThreshold() + " " + getSellingTarget());
        PatternMatrix pm = patternMatrixDao.get(getPatternMatrixIndex());
        for(String stockName : getTestStockPool()){
            try{
                StockModel sm = stockDao.load(stockName);
                if(sm != null){
                    comparePatternMatrix(pm, this.date, sm);
                }
                if(totalCount>SAMPLING_POOL && successCount/totalCount<0.3){
                    logDetails(pm.getIndex());
                    return;
                }
            }catch (Exception e){
                logger.error("unable to validate", e);
                logger.error(stockName + " skipped");
                logger.error("=================================================================================");
            }
        }
        logDetails(pm.getIndex());
    }

    @Override
    public void validatePatternMatrix() {
        validatePatternMatrix(null);
    }

    public boolean comparePatternMatrix(PatternMatrix pm, Date date, StockModel sm) throws Exception {
        //date = new Date(893217600000l);
        PatternMatrix pm2 = null;
        RawPatternModel rpm = stockParserService.getRPMWithDate4Testing(sm, date);
        if(rpm!=null)
            pm2 = patternMatrixService.parseRawPatternModel(rpm, 0);
        if(pm2!=null ){
            double priceSim  = patternCosineSimilarityService.comparePrice(pm, pm2);
            double volumeSim  = patternCosineSimilarityService.compareVolumn(pm, pm2);
            if(priceSim>getPriceSimilarityThreshold() && volumeSim>getVolumeSimilarityThreshold()){
                totalCount++;
                isSuccess(sm, pm2.getBuyingDate());
                logger.info("priceSim: " + getPriceSimilarityThreshold() + " volumeSim: " + getVolumeSimilarityThreshold() + ", " + pm.getIndex() + " against " + pm2.getStockName() + " Date: " + date);
                logger.info("=================================================================================");
                return true;
            }
        }
        return false;
    }

    public boolean comparePatternMatrixProxy(PatternMatrix pm, Date date, StockModel sm) throws Exception {
        PatternMatrix pm2 = null;
        boolean success = false;
        RawPatternModel rpm = stockParserService.getRPMWithDate4Testing(sm, date);
        if(rpm!=null)
            pm2 = patternMatrixService.parseRawPatternModel(rpm, 0);

        if(pm2!=null ){
            success = isSuccess(sm, pm2.getBuyingDate());
        }
        return success;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
