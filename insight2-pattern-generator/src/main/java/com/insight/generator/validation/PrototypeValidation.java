package com.insight.generator.validation;

import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.StockDao;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockDataGeneratorService;
import com.insight.generator.service.StockParserService;
import com.insight.generator.service.StockParserServiceImpl;
import com.insight.model.Aggregation;
import com.insight.model.PatternMatrix;
import com.insight.model.RawPatternModel;
import com.insight.model.StockModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by PC on 4/12/2015.
 */
@Service
public class PrototypeValidation extends AbstractValidation{

    @Autowired
    StockParserService stockParserService;

    @Autowired
    StockDao stockDao;

    @Autowired
    PatternMatrixService patternMatrixService;

    @Autowired
    PatternAggregateService patternAggregateService;

    @Autowired
    PrototypeService prototypeService;

    private static final Logger logger = Logger.getLogger(PrototypeValidation.class);

    // TODO use reactive style
    @Override
    public void validateSignature(Date date, String prototype){
        StockModel sm = null;
        for(String stockName : TestStockName.ALL_STOCK_NAME){
            try{
                sm = stockDao.load(stockName);
                if(sm != null){
                    PatternMatrix pm = null;
                    Aggregation aggregation = null;
                    RawPatternModel rpm = stockParserService.getRPMWithDate4Testing(sm, date);
                    if(rpm!=null)
                        pm = patternMatrixService.parseRawPatternModel(rpm, 0);
                    if(pm!=null)
                        aggregation = patternAggregateService.convertPM2Aggregation(pm);
                    if(aggregation!=null && prototype.equals(aggregation.getPatternSignature().trim())){
                        logger.info(aggregation.getStockName() + " matched! " + aggregation.getPatternSignature() + " Date: " + date);
                        isSuccess(sm, pm.getBuyingDate());
                        logger.info("=================================================================================");
                    }else if(aggregation!=null){
                        //logger.info(aggregation.getStockName() + " " + aggregation.getPatternSignature() + " skipped");
                    }
                }
            }catch (Exception e){
                System.err.println(e);
                logger.error("unable to validate", e);
                logger.error(stockName + " skipped");
                logger.info("=================================================================================");
            }
        }
    }

    @Override
    public void validateSignature(String prototype) throws Exception {
        StockModel sm = null;
        for(String stockName : TestStockName.ALL_STOCK_NAME){
            try{
                sm = stockDao.load(stockName);
                if(sm != null){
                    PatternMatrix pm = null;
                    Aggregation aggregation = null;
                    for(Date date : sm.getDailyStocks().keySet()){
                        RawPatternModel rpm = stockParserService.getRPMWithDate4Testing(sm, date);
                        if(rpm!=null)
                            pm = patternMatrixService.parseRawPatternModel(rpm, 0);
                        if(pm!=null)
                            aggregation = patternAggregateService.convertPM2Aggregation(pm);

                        if(aggregation!=null && prototype.equals(aggregation.getPatternSignature().trim())){
                            logger.info(aggregation.getStockName() + " matched! " + aggregation.getPatternSignature() + " Date: " + date);
                            isSuccess(sm, pm.getBuyingDate());
                            logger.info("=================================================================================");
                        }else if(aggregation!=null){
                            //logger.info(aggregation.getStockName() + " " + aggregation.getPatternSignature() + " skipped");
                        }
                    }
                }
            }catch (Exception e){
                System.err.println(e);
                logger.error("unable to validate", e);
                logger.error(stockName + " skipped");
                logger.info("=================================================================================");
            }

        }
    }

    @Override
    public void validatePatternMatrix(String patternMatixIndex) {
        logger.error("method not implemented.");
    }

    @Override
    public void validatePatternMatrix() {
    }

    @Override
    public void run() {
        logger.error("not implemented.");
    }
}
