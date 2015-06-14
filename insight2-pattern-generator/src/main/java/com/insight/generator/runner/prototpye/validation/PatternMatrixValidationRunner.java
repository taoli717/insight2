package com.insight.generator.runner.prototpye.validation;

import com.insight.generator.PatternGeneratorConfig;
import com.insight.generator.constant.TestStockName;
import com.insight.generator.validation.AbstractValidation;
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
 * Created by PC on 3/31/2015.
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
        String[] matrixIndexArray = {"AMP*#*67", "DKS*#*401", "CNC*#*533", "CNC*#*534", "NPO*#*425", "NPO*#*426", "GGG*#*906", "MOD*#*833", "MOD*#*834", "MOD*#*836"
                , "JLL*#*690", "JLL*#*691", "JLL*#*692", "TDY*#*526", "MSI*#*2042"};
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

    public static Date getDate(String input) throws ParseException {
        DateFormat format = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        Date date = format.parse(input);
        return date;
    }

    public static void runPatternMatrixValidation(){
        for(String matrixIndex : matrixIndexes){
            validation = (AbstractValidation) ctx.getBean("patternMatrixSimilarityValidation");
            validation.setSellingTarget(1.10);
            validation.setPriceSimilarityThreshold(0.90);
            validation.setVolumeSimilarityThreshold(0.40);
            validation.setSamplingPool(100000);
            validation.setPatternMatrixIndex(matrixIndex);
            validation.setTestStockPool(getStockNames());
            taskExecutor.execute(validation);
        }
    }

    public static String[] getStockNames(){
        int dataPoolDividen = 1;
        String[] stockNames = TestStockName.ALL_STOCK_NAME;
        int endIndex = stockNames.length/dataPoolDividen;
        return Arrays.asList(TestStockName.ALL_STOCK_NAME).subList(0, endIndex).toArray(new String[endIndex]);
    }
}
