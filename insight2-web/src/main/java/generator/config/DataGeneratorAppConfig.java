package generator.config;

import generator.dao.RawPatternDao;
import generator.dao.RawPatternDaoImp;
import generator.dao.StockDao;
import generator.dao.StockDaoImp;
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
    
}