package com.insight.generator.runner;

import com.insight.generator.PatternGeneratorConfig;
import com.insight.generator.service.PatternMatrixService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by PC on 3/31/2015.
 */
public class PatternMatrixGenerationRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PatternMatrixGenerationRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PatternMatrixService patternMatrixService = (PatternMatrixService) ctx.getBean("patternMatrixService");
        try{
            patternMatrixService.parse();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
