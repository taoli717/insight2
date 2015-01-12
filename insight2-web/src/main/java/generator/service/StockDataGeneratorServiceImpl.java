package generator.service;

import generator.config.DataGeneratorAppConfig;
import generator.constant.TestStockName;
import generator.dao.StockDao;
import generator.init.SetUpService;
import generator.model.StockModel;
import generator.retriever.MarkItOnDemondPriceRetriever;
import generator.retriever.PriceRetriever;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by PC on 11/15/2014.
 */
@Service
public class StockDataGeneratorServiceImpl implements StockDataGeneratorService {

    private static final Logger logger = Logger.getLogger(StockDataGeneratorServiceImpl.class);

    @Autowired
    StockDao stockDao;

    @Autowired
    SetUpService setUpService;

    @PostConstruct
    public void init() throws Exception {
        logger.info("setUpService.isSetUp(): " + setUpService.isSetUp());
        if(!setUpService.isSetUp()){
            this.generate();
        }
    }

    @Override
    public void generate() throws Exception {
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(DataGeneratorAppConfig.class);
        //StockDao stockDao = (StockDao) ctx.getBean("stockDaoImp");
        String days = "30000";
        PriceRetriever http = new MarkItOnDemondPriceRetriever();
        //String[] stocksName = ArrayUtils.addAll(NASDAQStockName.STOCK_NAMES, NYSCStockNames.STOCK_NAMES);
        String[] stocksName = TestStockName.ALL_STOCK_NAME;
       // String[] stocksName = {"TSLA"};
        //I'm Ken2
        ArrayList<String> workingStock = new ArrayList<String>();
        int i = 0;
        long startTime = System.currentTimeMillis();
        for(String stockName : stocksName){
            Boolean autoIncre = true;
            try{
                StockModel sm = http.sendGet(stockName, days);
                if(i==0){
                    autoIncre = false;
                    sm.setSeq(0);
                }
                stockDao.save(sm, autoIncre);
                workingStock.add(sm.getStockName());
                i++;
                logger.info("Stock #: " + i);
            }catch(Exception e){
                if(e instanceof SocketException){
                    try{
                        Thread.sleep(2000);
                        StockModel sm = http.sendGet(stockName, days);
                        stockDao.save(sm, autoIncre);
                        workingStock.add(sm.getStockName());
                        i++;
                        logger.info("Stock #: " + i);
                    }catch(Exception ex){
                        logger.error("StockName: " + stockName);
                        logger.error(ex);
                    }
                }
                logger.error("StockName: " + stockName);
                logger.error(e);
            }
            Thread.sleep(1000);
        }
        String listString = "";

        long endTime = System.currentTimeMillis();

        logger.info("Total time spent: " + (endTime - startTime) / 1000);//total time in seconds
        for (String s : workingStock)
        {
            listString = listString + "\"" + s + "\", ";
        }

        logger.info(listString);
    }
    public static void main(String[] args) throws Exception {
        new StockDataGeneratorServiceImpl().generate();
    }
}
