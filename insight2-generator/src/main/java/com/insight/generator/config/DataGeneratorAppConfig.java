package com.insight.generator.config;

import com.insight.generator.aggregate.dao.AggregateDao;
import com.insight.generator.aggregate.dao.AggregateDaoImpl;
import com.insight.generator.aggregate.dao.PrototypeDao;
import com.insight.generator.aggregate.dao.PrototypeDaoImpl;
import com.insight.generator.aggregate.service.PatternAggregateService;
import com.insight.generator.aggregate.service.PatternAggregateServiceImpl;
import com.insight.generator.dao.*;
import com.insight.generator.matching.dao.PatternCosineSimilarityDao;
import com.insight.generator.matching.dao.PatternCosineSimilarityDaoImpl;
import com.insight.generator.matching.model.PatternCosineSimilarity;
import com.insight.generator.matching.service.PatternCosineSimilarityService;
import com.insight.generator.matching.service.PatternCosineSimilarityServiceImpl;
import com.insight.generator.setup.SetUpService;
import com.insight.generator.parser.MarketItOnDemandStockParser;
import com.insight.generator.parser.StockParser;
import com.insight.generator.service.StockParserService;
import com.insight.generator.setup.SetUpServiceImpl;
import com.insight.generator.service.StockDataGeneratorService;
import com.insight.generator.service.StockDataGeneratorServiceImpl;
import com.insight.generator.service.StockParserServiceImpl;
import com.insight.generator.pattern.generator.dao.PatternMatrixDao;
import com.insight.generator.pattern.generator.dao.PatternMatrixDaoImpl;
import com.insight.generator.pattern.generator.service.PatternMatrixService;
import com.insight.generator.pattern.generator.service.PatternMatrixServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.insight"})
@Import({ MongoConfig.class })
public class DataGeneratorAppConfig {

    @Bean
    public RawPatternDao rawPatternDao(){
        RawPatternDao rawPatternDao = new RawPatternDaoImp();
        return rawPatternDao;
    }

    @Bean
    public StockDao stockDao(){
        StockDao stockDao = new StockDaoImp();
        return stockDao;
    }

    @Bean
    public StockParserService stockParserService(){
        StockParserService stockParserService = new StockParserServiceImpl();
        return stockParserService;
    }

    @Bean
    public StockDataGeneratorService stockDataGeneratorService(){
        StockDataGeneratorService stockDataGeneratorService = new StockDataGeneratorServiceImpl();
        return stockDataGeneratorService;
    }

    @Bean
    public SetUpDao setUpDao(){
        SetUpDao setUpDao = new SetUpDaoImpl();
        return setUpDao;
    }

    @Bean
    public SetUpService setUpService(){
        SetUpService setUpService = new SetUpServiceImpl();
        return setUpService;
    }

    @Bean
    public StockParser stockParser(){
        StockParser stockParser = new MarketItOnDemandStockParser();
        return stockParser;
    }

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

}