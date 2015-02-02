package com.insight.generator.config;

import com.insight.generator.dao.*;
import com.insight.generator.init.SetUpService;
import com.insight.generator.parser.MarketItOnDemandStockParser;
import com.insight.generator.parser.StockParser;
import com.insight.generator.service.StockParserService;
import com.insight.generator.init.SetUpServiceImpl;
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
}