package com.insight.generator.runner.prototpye;

import com.insight.generator.PatternGeneratorConfig;
import com.insight.generator.prototype.service.PrototypeService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.PrintWriter;

/**
 * Created by PC on 3/31/2015.
 */
public class PrototypeReductionRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PrototypeReductionRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PrototypeService prototypeService = (PrototypeService) ctx.getBean("prototypeService");

        PrintWriter writer = null;
        // aggregate data into prototype from pattern matrix
        try {
            prototypeService.cleanAndReduce(500);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
