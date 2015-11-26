package com.insight.generator.runner.prototpye;

import com.google.common.collect.Multimap;
import com.insight.PatternGeneratorConfig;
import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.constant.TestStockName;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.StockDataGeneratorService;
import com.insight.generator.service.StockParserService;
import com.insight.model.PatternPrototype;
import com.insight.validation.AbstractValidation;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by tli on 3/31/2015.
 */
public class MasterRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(MasterRunner.class);

    private static AbstractValidation validation;
    private static ThreadPoolTaskExecutor taskExecutor;
    static ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        StockDataGeneratorService stockDataGeneratorService = (StockDataGeneratorService) ctx.getBean("stockDataGeneratorService");
        PatternMatrixService patternMatrixService = (PatternMatrixService) ctx.getBean("patternMatrixService");
        StockParserService stockParserService = (StockParserService) ctx.getBean("stockParserService");
        PrototypeService prototypeService = (PrototypeService) ctx.getBean("prototypeService");
        PatternAggregateService patternAggregateService = (PatternAggregateService) ctx.getBean("patternAggregateService");

        // generate stock data, use only for empty db
        try{
            // generate stock model
            stockDataGeneratorService.generate();

            //raw pattern
            stockParserService.setTargetPect(0.15);
            stockParserService.parse();

            //pattern matrix
            // -here
            patternMatrixService.parse();

            patternAggregateService.parse();

            Multimap<Integer, PatternPrototype> mm = prototypeService.start();

            taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
            PrototypeDao prototypeDao = (PrototypeDao) ctx.getBean("prototypeDao");
            List<PatternPrototype> prototypeList = prototypeDao.filter(50);
            List<String> prototypeIndexList = prototypeList.stream().map(p -> p.getId()).collect(Collectors.toList());
            // aggregate data into prototype from pattern matrix
            try{
                for(String prototypeSignature : prototypeIndexList){
                    runSignatureValidation(prototypeSignature);
                }
            }catch (Exception e){
                logger.error("main", e);
            }

            for (;;) {
                int count = taskExecutor.getActiveCount();
                logger.info("Active Threads : " + count);
                try {
                    Thread.sleep(6000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count < 1) {
                    //taskExecutor.shutdown();
                    break;
                }
            }
            taskExecutor.shutdown();
            logger.info("======================================================================");
            System.exit(0);

        }catch (Exception e){
            logger.error("in runner", e);
        }
        try{
            patternMatrixService.parse();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public static void runSignatureValidation(String prototypeSignature){
        validation = (AbstractValidation) ctx.getBean("prototypeSimilarityValidation");
        validation.setSellingTarget(1.10);
        validation.setPriceSimilarityThreshold(0.90);
        validation.setVolumeSimilarityThreshold(0.40);
        validation.setPrototype(prototypeSignature);
        validation.setTestStockPool(getStockNames(5));
        taskExecutor.execute(validation);
    }

    public static Date getDate(String input) throws ParseException {
        DateFormat format = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        Date date = format.parse(input);
        return date;
    }

    public static String[] getStockNames(int dataPoolDividen){
        String[] stockNames = TestStockName.ALL_STOCK_NAME;
        int endIndex = stockNames.length/dataPoolDividen;
        return Arrays.asList(TestStockName.ALL_STOCK_NAME).subList(0, endIndex).toArray(new String[endIndex]);
    }
}
