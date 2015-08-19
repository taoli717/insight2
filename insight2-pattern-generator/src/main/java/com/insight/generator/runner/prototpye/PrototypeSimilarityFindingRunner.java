package com.insight.generator.runner.prototpye;

import com.insight.PatternGeneratorConfig;
import com.insight.generator.aggregate.service.PatternAggregateServiceImpl;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.model.PatternPrototype;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

/**
 * Created by tli on 3/31/2015.
 */
@Slf4j
public class PrototypeSimilarityFindingRunner implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        //BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PrototypeService prototypeService = (PrototypeService) ctx.getBean("prototypeService");

        try{
            prototypeService.start();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

}
