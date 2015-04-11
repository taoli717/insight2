package com.insight.generator.service;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.StockDao;
import com.insight.generator.setup.SetUpService;
import com.insight.model.StockModel;
import com.insight.generator.retriever.MarkItOnDemondPriceRetriever;
import com.insight.generator.retriever.PriceRetriever;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

   //@PostConstruct
    public void init() {
        logger.info("setUpService.isSetUp(): " + setUpService.isSetUp());
/*        if(!setUpService.isSetUp()){
            try{
                this.generate();
            }catch(Exception e){
                logger.error("Initial installation failed");
            }
        }*/
    }

    @Override
    public void generate() throws Exception {
        //attempt to query 30000 days of history
        String days = "30000";
        PriceRetriever http = new MarkItOnDemondPriceRetriever();
        String[] stocksName = TestStockName.ALL_STOCK_NAME;
        ArrayList<String> workingStock = new ArrayList<String>();
        int i = 0;
        //TODO use Stopwatch
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

    public static void main(String[] args){
        System.out.println("test");
    }
}
