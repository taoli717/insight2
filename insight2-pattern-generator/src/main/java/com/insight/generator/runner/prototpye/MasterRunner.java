package com.insight.generator.runner.prototpye;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockDataGeneratorService;
import com.insight.generator.service.StockParserService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by tli on 3/31/2015.
 */
public class MasterRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(MasterRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        StockDataGeneratorService stockDataGeneratorService = (StockDataGeneratorService) ctx.getBean("stockDataGeneratorService");
        PatternMatrixService patternMatrixService = (PatternMatrixService) ctx.getBean("patternMatrixService");
        StockParserService stockParserService = (StockParserService) ctx.getBean("stockParserService");
        PrototypeService prototypeService = (PrototypeService) ctx.getBean("prototypeService");
        PatternAggregateService patternAggregateService = (PatternAggregateService) ctx.getBean("patternAggregateService");

        // generate stock data, use only for empty db
        try{
            // generate stock model
            //stockDataGeneratorService.generate();

            //raw pattern
            //stockParserService.parse();

            //pattern matrix
            // -here
            patternMatrixService.parse();
            patternAggregateService.parse();

            prototypeService.start();
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
