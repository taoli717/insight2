package com.insight.generator.runner;

import com.insight.generator.config.DataGeneratorAppConfig;
import com.insight.generator.service.StockParserService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by tli on 3/31/2015.
 */
public class RawPatternRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(RawPatternRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DataGeneratorAppConfig.class);
        StockParserService stockParserService = (StockParserService) ctx.getBean("stockParserService");
        try{
            stockParserService.parse();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
