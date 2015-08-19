package com.insight.generator.runner.prototpye.validation;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.constant.TestStockName;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.validation.AbstractValidation;
import com.insight.model.PatternPrototype;
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
import java.util.stream.Collectors;

/**
 * Created by tli on 3/31/2015.
 */
public class PrototypeValidationRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PrototypeValidationRunner.class);
    public static final Set<String> signatures = new HashSet<>();
    private static AbstractValidation validation;
    private static ApplicationContext ctx;
    private static String[] dates;
    private static ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {}

    public static void init(){
        //String[] signatureArray = {"0101020204080810" , "0101020202020420" , "0101010204040840" , "0102040404040810" , "0102020202040810" , "0102020404040810" , "0102020408080810" , "0101020202040820" , "0101010202041020" , "0101010204040810" , "0101010101010220" , "0101020204040810" , "0101010102040404" , "0101010202040808" , "0101010102020820" , "0101020404040820" , "0101010202040810" , "0101020202040810" , "0101020404040810" , "0101010202040408" , "0101010202020420" , "0101010202040420" , "0101010101020410" , "0101020204040808" , "0101010102040410" , "0101020404080820" , "0101020202040808" , "0101010102040808" , "0101020204080820" , "0101010102040810" , "0101010204040420" , "0101020204040408" , "0101020404080810" , "0101020404040408" , "0101010204040408" , "0101010101010408" , "0101010101020208" , "0101010102020410" , "0101010101020420" , "0101010101010210" , "0101020202040408" , "0101010204080820" , "0101020408080810" , "0101010202040820" , "0101010102020208" , "0101020408080820" , "0101010102040420" , "0101010202020408" , "0101010204040820" , "0101020204040410" , "0101020202020408" , "0101010101040408" , "0101010102020420" , "0101020404040410" , "0101020204080808" , "0101020202020410" , "0101020404080808" , "0101010204080808" , "0101020202040410" , "0101010101020404" , "0101020404040808" , "0101010102040820" , "0101010202040410" , "0101010101020408" , "0101010102041020" , "0102020204040810" , "0101010202040840" , "0101010204080810" , "0101010102040840" , "0101010102042020" , "0101010102040408" , "0101010102020408" , "0101010101010420" , "0101010101020440" , "0101020204040820" , "0102020204040820" , "0101010104040408" , "0101020408080808" , "0101010204040410" , "0101010101010410" , "0101010101010208" , "0101010204040808" , "0101010204041020" , "0102020404080810" , "0101010202020410"  };
        String[] signatureArray = {"8040201004020201", "8040200804040201", "8040200802020201", "8040200804020101", "8040200804020101", "8040100804020201"};
        signatures.addAll(Arrays.asList(signatureArray));
        logger.info("afterPropertySet");
    }

    public static void main(String[] args){
        //BasicConfigurator.configure();
        PrototypeValidationRunner runner = new PrototypeValidationRunner();
        String result = "";
        ClassLoader classLoader = runner.getClass().getClassLoader();
        PropertyConfigurator.configure(classLoader.getResourceAsStream("log4j.properties"));
        ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
        PrototypeDao prototypeDao = (PrototypeDao) ctx.getBean("prototypeDao");
        List<PatternPrototype> prototypeList = prototypeDao.filter(50);
        List<String> prototypeIndexList = prototypeList.stream().map(p -> p.getId()).collect(Collectors.toList());
        init();
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
    }

    public static void runSignatureValidation(String prototypeSignature){
        validation = (AbstractValidation) ctx.getBean("prototypeSimilarityValidation");
        validation.setSellingTarget(1.10);
        validation.setPriceSimilarityThreshold(0.90);
        validation.setVolumeSimilarityThreshold(0.40);
        //validation.setSamplingPool(100);
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
