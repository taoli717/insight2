package com.insight.generator.runner.prototpye.validation;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.constant.TestStockName;
import com.insight.validation.AbstractValidation;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tli on 3/31/2015.
 */
public class PatternMatrixValidationRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PatternMatrixValidationRunner.class);
    public static final Set<String> matrixIndexes = new HashSet<>();
    private static AbstractValidation validation;
    private static ApplicationContext ctx;
    private static ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {}

    public static void init(){
        String[] matrixIndexArray = {"TDW*#*258"};
        logger.info(Arrays.toString(matrixIndexArray));
        matrixIndexes.addAll(Arrays.asList(matrixIndexArray));
    }

    public static void main(String[] args){
        PatternMatrixValidationRunner runner = new PatternMatrixValidationRunner();
        ClassLoader classLoader = runner.getClass().getClassLoader();
        PropertyConfigurator.configure(classLoader.getResourceAsStream("log4j.properties"));
        ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
        init();
        // aggregate data into prototype from pattern matrix
        try{
            runPatternMatrixValidation();
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
                taskExecutor.shutdown();
                break;
            }
        }
        logger.info("============================== End ========================================");
        System.exit(0);
    }

    public static void runPatternMatrixValidation(){
        for(String matrixIndex : matrixIndexes){
            //for(double sim=0.2; sim<1; sim+=0.1){
                validation = (AbstractValidation) ctx.getBean("patternMatrixSimilarityValidation");
                validation.setSellingTarget(1.10);
                validation.setPriceSimilarityThreshold(0.90);
                validation.setVolumeSimilarityThreshold(0.50);
                validation.setSamplingPool(100000);
                validation.setPatternMatrixIndex(matrixIndex);
                validation.setTestStockPool(getStockNames());
                taskExecutor.execute(validation);
           // }
        }
    }

    public static Date getDate(String input) throws ParseException {
        DateFormat format = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        Date date = format.parse(input);
        return date;
    }

    public static String[] getStockNames(){
        int dataPoolDividen = 4;
        String[] stockNames = TestStockName.ALL_STOCK_NAME;
        int endIndex = stockNames.length/dataPoolDividen;
        return Arrays.asList(TestStockName.ALL_STOCK_NAME).subList(0, endIndex).toArray(new String[endIndex]);
        //return TestStockName.TEST_STOCK_NAME;
    }
}
