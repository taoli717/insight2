package com.insight.generator.runner.prototpye;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockDataGeneratorService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by PC on 3/31/2015.
 */
public class MasterRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(MasterRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PatternMatrixService patternMatrixService = (PatternMatrixService) ctx.getBean("patternMatrixService");
        StockDataGeneratorService stockDataGeneratorService = (StockDataGeneratorService) ctx.getBean("stockDataGeneratorService");

        // generate stock data, use only for empty db
        try{
            stockDataGeneratorService.generate();
        }catch (Exception e){
            logger.error("in runner", e);
        }
        try{
            patternMatrixService.parse();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
