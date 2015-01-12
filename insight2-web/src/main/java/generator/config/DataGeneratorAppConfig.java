package generator.config;

import generator.dao.*;
import generator.init.SetUpService;
import generator.init.SetUpServiceImpl;
import generator.service.StockDataGeneratorService;
import generator.service.StockDataGeneratorServiceImpl;
import generator.service.StockParserService;
import generator.service.StockParserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.insight.data.generator.config", "com.insight.data.generator.model",
        "com.insight.data.generator.dao", "com.insight.data.generator.service"})
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
}