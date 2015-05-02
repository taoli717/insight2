package com.insight.generator.service;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.StockDao;
import com.insight.generator.setup.SetUpService;
import com.insight.model.StockModel;
import com.insight.generator.retriever.PriceRetriever;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    @Qualifier("yahooPriceRetriever")
    PriceRetriever priceRetriever;

    public static final String days = "40000";
    Set<String> workingStock = new HashSet<>();
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
        //String[] stocksNames = TestStockName.ALL_STOCK_NAME;
        String[] stocksNames = {"ECOM", "ONE", "SPB", "REN"};
        int i = 0;
        //TODO use Stopwatch
        long startTime = System.currentTimeMillis();
        for(String stockName : stocksNames){
            getAndSaveStockModel(stockName, i++);
            //Thread.sleep(1500);
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

    public void getAndSaveStockModel(String stockName, int i) throws Exception{
        getAndSaveStockModel(stockName, this.days, i);
    }

    public void getAndSaveStockModel(String stockName,String days, int i) throws Exception{
        StockModel sm = getStockModel(stockName, days, i);
        if(sm != null){
            stockDao.save(sm);
            workingStock.add(stockName);
            logger.info("Stock #: " + i);
        }
    }

    public StockModel getStockModel(String stockName,String days, int i) throws Exception{
        StockModel sm = priceRetriever.sendGet(stockName, days, i);
        if(sm == null){
            Thread.sleep(3000);
            sm = priceRetriever.sendGet(stockName, days, i);
        }
        if(sm == null){
            logger.error("StockName: " + stockName + " date not generated.");
            return null;
        }else{
            return sm;
        }
    }

    @Override
    public void synchronizeStockData(){
        StockModel sm;
        while((sm = stockDao.getNextStock()) != null){
            StockModel latestSm = null;
            long dayDiff = calculateDayDiff(sm);
            logger.info(sm.getStockName() + " time diff: " + String.valueOf(dayDiff));
            try{
                latestSm = priceRetriever.sendGet(sm.getStockName(), String.valueOf(dayDiff), sm.getSeq());
                if(latestSm!=null){
                    Thread.sleep(2000);
                    sm.getDailyStocks().putAll(latestSm.getDailyStocks());
                    stockDao.save(sm);
                    logger.info(sm.getStockName() + " updated");
                }
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static long calculateDayDiff(StockModel sm){
        TreeSet<Date> keyMap = new TreeSet<>(sm.getDailyStocks().keySet());
        Date lastDate = keyMap.last();
        Date currentDate = Calendar.getInstance().getTime();
        return getDateDiff(lastDate, currentDate, TimeUnit.DAYS);
    }

}
