package generator.service;

import generator.config.DataGeneratorAppConfig;
import generator.dao.RawPatternDao;
import generator.dao.StockDao;
import generator.init.SetUpService;
import generator.model.DailyStockModel;
import generator.model.RawPatternModel;
import generator.model.StockModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by PC on 2014/11/23.
 */
@Service
@DependsOn("StockDataGeneratorService")
public class StockParserServiceImpl implements StockParserService{
    public static double targetPect = .1;

    private static final Logger logger = Logger.getLogger(StockParserServiceImpl.class);

    @Autowired
    RawPatternDao rawPatternDao;

    @Autowired
    SetUpService setUpService;

    @Autowired
    StockDao stockDao;

    @PostConstruct
    public void init() throws Exception{
        logger.info("setUpService.isSetUp(): " + setUpService.isSetUp());
        if(!setUpService.isSetUp()){
            try{
                this.parse();
                setUpService.setUpSuccess();
            }catch(Exception e){
                logger.error("Initial install failed");
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
        while(sm!=null){
            if(stockDao.isDBExist(sm.stockName)){
                sm = (StockModel) stockDao.loadNext();
                logger.info(sm.stockName + " skipped");
                continue;
            }
            System.out.println("processing: " + total);
            total++;
            LinkedHashMap<Date,DailyStockModel> dmMap = sm.getDailyStocks();
            // ignore newly released com
            Set<Date> dmKeySet = dmMap.keySet();
            LinkedList<Date> dateList = new LinkedList<Date>(dmKeySet);
            if(dmMap.size()>mimumIgonreSize){
                for(int j = buyingDayIndex; j<dateList.size()-60; j++){
                    //don't consider the first three month after IPA
                    Collections.sort(dateList);
                    DailyStockModel dsm = dmMap.get(dateList.get(j));
                    Double buyingPrice = takeDailyAverage(dsm);
                    for(int i=0; i<holdingPeriod; i++){
                        Date sellingDay = dateList.get( i + j);
                        DailyStockModel tempDsm = dmMap.get(sellingDay);
                        Double currentPrice = takeDailyAverage(tempDsm);
                        if(currentPrice> buyingPrice*(1+targetPect)){
                            RawPatternModel rpm = new RawPatternModel();
                            rpm.setBuyingDate(dateList.get(j));
                            rpm.setSellingDate(sellingDay);
                            rpm.setStockName(sm.getStockName());
                            rpm.setStockCode(sm.getStockCode());
                            System.out.println("j: " + j);
                            List rpmStockDateList = (List) dateList.subList(j -60, j + i +20);
                            rpm.setDailyStocks(generateRPMDailyStockModel(rpmStockDateList, sm));
                            rawPatternDao.save(rpm);
                            break;
                        }
                    }
                }
            }
            sm = (StockModel) stockDao.loadNext();
        }
        System.out.println("done  " + total);
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