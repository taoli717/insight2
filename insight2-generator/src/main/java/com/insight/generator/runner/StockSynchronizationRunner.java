package com.insight.generator.runner;

import com.insight.generator.config.DataGeneratorAppConfig;
import com.insight.generator.service.StockDataGeneratorService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by PC on 3/31/2015.
 */
public class StockSynchronizationRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(StockSynchronizationRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DataGeneratorAppConfig.class);
        StockDataGeneratorService stockDataGeneratorService = (StockDataGeneratorService) ctx.getBean("stockDataGeneratorService");

        // add latest stock data
        try{
            stockDataGeneratorService.synchronizeStockData();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
