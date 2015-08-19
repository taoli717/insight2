package com.insight.generator.runner.prototpye;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.prototype.service.PrototypeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by tli on 3/31/2015.
 */
public class PrototypeRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PrototypeRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        //BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PatternAggregateService patternAggregateService = (PatternAggregateService) ctx.getBean("patternAggregateService");

        // aggregate data into prototype from pattern matrix
        try{
            patternAggregateService.parse();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
/*        try{
            prototypeService.cleanAndReduce(50);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }*/
    }
}
