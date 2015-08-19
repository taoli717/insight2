package com.insight;

import com.insight.learner.PatternMatrixSimilarityLearner;
import com.insight.validation.*;
import org.springframework.cache.CacheManager;
import com.insight.generator.aggregate.dao.AggregateDao;
import com.insight.generator.aggregate.dao.AggregateDaoImpl;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.generator.prototype.dao.PrototypeDaoImpl;
import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.aggregate.service.PatternAggregateServiceImpl;
import com.insight.generator.config.DataGeneratorAppConfig;
import com.insight.generator.config.MongoConfig;
import com.insight.generator.dao.PatternMatrixDao;
import com.insight.generator.dao.PatternMatrixDaoImpl;
import com.insight.generator.matching.dao.PatternCosineSimilarityDao;
import com.insight.generator.matching.dao.PatternCosineSimilarityDaoImpl;
import com.insight.generator.matching.service.PatternCosineSimilarityService;
import com.insight.generator.matching.service.PatternCosineSimilarityServiceImpl;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.generator.prototype.service.PrototypeServiceImpl;
import com.insight.generator.service.PatternMatrixService;
import com.insight.generator.service.PatternMatrixServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by tli on 3/28/2015.
 */

@Configuration
@ComponentScan({"com.insight"})
//@EnableCaching
@Import({ MongoConfig.class, DataGeneratorAppConfig.class})
public class PatternGeneratorConfig {


    @Bean
    public PatternMatrixDao patternMatrixDao(){
        PatternMatrixDao patternMatrix = new PatternMatrixDaoImpl();
        return patternMatrix;
    }

    @Bean
    public PatternMatrixService patternMatrixService(){
        PatternMatrixService patternMatrixService = new PatternMatrixServiceImpl();
        return patternMatrixService;
    }


    @Bean
    public PatternCosineSimilarityService patternCosineSimilarityService(){
        PatternCosineSimilarityService patternCosineSimilarityService = new PatternCosineSimilarityServiceImpl();
        return patternCosineSimilarityService;
    }

    @Bean
    PatternCosineSimilarityDao patternCosineSimilarityDao(){
        PatternCosineSimilarityDao patternCosineSimilarityDao = new PatternCosineSimilarityDaoImpl();
        return patternCosineSimilarityDao;
    }

    @Bean
    AggregateDao aggregateDao(){
        AggregateDao aggregateDao = new AggregateDaoImpl();
        return aggregateDao;
    }

    @Bean
    PatternAggregateService patternAggregateService(){
        PatternAggregateService patternAggregateService = new PatternAggregateServiceImpl();
        return patternAggregateService;
    }

    @Bean
    PrototypeDao prototypeDao(){
        PrototypeDao prototypeDao = new PrototypeDaoImpl();
        return prototypeDao;
    }

    @Bean
    PrototypeService prototypeService(){
        PrototypeService prototypeService = new PrototypeServiceImpl();
        return prototypeService;
    }

    @Bean
    Validation prototypeValidation(){
        Validation prototypeValidation = new PrototypeValidation();
        return prototypeValidation;
    }

    @Bean
    @Qualifier("patternMatrixSimilarityValidation")
    Validation patternMatrixSimilarityValidation(){
        Validation patternMatrixSimilarityValidation = new PatternMatrixSimilarityValidation();
        return patternMatrixSimilarityValidation;
    }

    @Bean
    @Qualifier("prototypeSimilarityValidation")
    Validation prototypeSimilarityValidation(){
        Validation prototypeSimilarityValidation = new PrototypeSimilarityValidation();
        return prototypeSimilarityValidation;
    }

    @Bean
    @Qualifier("PatternMatrixWithDateSimilarityValidation")
    Validation patternMatrixWithDateSimilarityValidation(){
        Validation patternMatrixWithDateSimilarityValidation = new PatternMatrixWithDateSimilarityValidation();
        return patternMatrixWithDateSimilarityValidation;
    }

    @Bean
    @Qualifier("PatternMatrixSimilarityLearner")
    Validation patternMatrixSimilarityLearner(){
        Validation patternMatrixSimilarityLearner = new PatternMatrixSimilarityLearner();
        return patternMatrixSimilarityLearner;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(6);
        pool.setMaxPoolSize(100);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

/*
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }
    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }*/
}
