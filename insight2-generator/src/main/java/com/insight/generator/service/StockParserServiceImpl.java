package com.insight.generator.service;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.RawPatternDao;
import com.insight.generator.dao.StockDao;
import com.insight.generator.setup.SetUpService;
import com.insight.generator.model.RawPatternModel;
import com.insight.generator.model.DailyStockModel;
import com.insight.generator.model.StockModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by PC on 2014/11/23.
 */
@Service
@DependsOn("stockDataGeneratorService")
public class StockParserServiceImpl implements StockParserService{
    public static double targetPect = .1;

    private static final Logger logger = Logger.getLogger(StockParserServiceImpl.class);

    @Autowired
    RawPatternDao rawPatternDao;

    @Autowired
    SetUpService setUpService;

    @Autowired
    StockDao stockDao;

    public static final int PATTER_LENGTH = 64;

    @PostConstruct
    public void init() throws Exception{
        logger.info("setUpService.isSetUp(): " + setUpService.isSetUp());
        if(!setUpService.isSetUp()){
            try{
                this.parse();
                setUpService.setUpSuccess();
            }catch(Exception e){
                logger.error("Initial install failed", e);
            }
        }
    }

    @Override
    public void parse() throws Exception{
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(DataGeneratorAppConfig.class);
        StockModel sm = (StockModel) stockDao.loadNext();
        //StockDao stockDao = (StockDao) ctx.getBean("stockDaoImp");
        //RawPatternDao rpmDao = (RawPatternDao) ctx.getBean("rawPatternDaoImp");
        long total = 0;
        int buyingDayIndex = 120;
        int holdingPeriod = 20;
        int mimumIgonreSize = 180;

        for(String stockName : TestStockName.ALL_STOCK_NAME) {
            if (stockDao.isDBExist(stockName)) {
                //sm = (StockModel) stockDao.loadNext();
                sm = stockDao.load(stockName);
                logger.info(sm.stockName + " skipped");
                continue;
            }
            System.out.println("processing: " + total);
            total++;
            LinkedHashMap<Date, DailyStockModel> dmMap = sm.getDailyStocks();
            // ignore newly released com
            Set<Date> dmKeySet = dmMap.keySet();
            LinkedList<Date> dateList = new LinkedList<Date>(dmKeySet);
            if (dmMap.size() > mimumIgonreSize) {
                //TODO maybe it can be reorganized
                for (int j = buyingDayIndex; j < dateList.size() - PATTER_LENGTH; j++) {
                    //don't consider the first three month after IPA
                    Collections.sort(dateList);
                    DailyStockModel dsm = dmMap.get(dateList.get(j));
                    Double buyingPrice = takeDailyAverage(dsm);
                    for (int i = 0; i < holdingPeriod; i++) {
                        Date sellingDay = dateList.get(i + j);
                        DailyStockModel tempDsm = dmMap.get(sellingDay);
                        Double currentPrice = takeDailyAverage(tempDsm);
                        if (currentPrice > buyingPrice * (1 + targetPect)) {
                            RawPatternModel rpm = new RawPatternModel();
                            rpm.setBuyingDate(dateList.get(j));
                            rpm.setSellingDate(sellingDay);
                            rpm.setStockName(sm.getStockName());
                            //System.out.println("j: " + j);
                            List rpmStockDateList = (List) dateList.subList(j - PATTER_LENGTH, j + i + holdingPeriod);
                            rpm.setDailyStocks(generateRPMDailyStockModel(rpmStockDateList, sm));
                            rawPatternDao.save(rpm);
                            break;
                        }
                    }
                }
            }

            System.out.println("done  " + total);
        }
    }

    public static Double takeDailyAverage(DailyStockModel dsm){
        Double open = Double.parseDouble(dsm.getOpen());
        Double close = Double.parseDouble(dsm.getClose());
        Double high = Double.parseDouble(dsm.getHigh());
        Double low = Double.parseDouble(dsm.getLow());
        return (open + close + high + low)/4;
    }

    //make pattern from StockModel
    public static LinkedHashMap<Date,DailyStockModel> generateRPMDailyStockModel(List<Date> dateList, StockModel sm){
        LinkedHashMap<Date,DailyStockModel> smMap = sm.getDailyStocks();
        LinkedHashMap<Date,DailyStockModel> dsmMap = new LinkedHashMap<Date, DailyStockModel>();
        for(Date date : dateList){
            dsmMap.put(date, smMap.get(date));
        }
        return dsmMap;
    }

}