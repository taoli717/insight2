package com.insight.generator.service;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.RawPatternDao;
import com.insight.generator.dao.StockDao;
import com.insight.generator.setup.SetUpService;
import com.insight.model.Constants;
import com.insight.model.RawPatternModel;
import com.insight.model.DailyStockModel;
import com.insight.model.StockModel;
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
//@DependsOn("stockDataGeneratorService")
public class StockParserServiceImpl implements StockParserService{

    public static double targetPect = .20;
    public static final int PATTERN_LENGTH = 64;
    public static final int buyingDayIndex = 120;
    public static final int holdingPeriod = 20;
    public static final int mimumIgonreSize = 180;

    private static final Logger logger = Logger.getLogger(StockParserServiceImpl.class);

    @Autowired
    RawPatternDao rawPatternDao;

    @Autowired
    SetUpService setUpService;

    @Autowired
    StockDao stockDao;

    //@PostConstruct
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
        StockModel sm = null;
        for(String stockName : TestStockName.ALL_STOCK_NAME) {
            long total = 0;
            logger.info("parsing " + stockName);
            try{
                sm = stockDao.load(stockName);
                LinkedHashMap<Date, DailyStockModel> dmMap = sm.getDailyStocks();
                Set<Date> dmKeySet = dmMap.keySet();
                LinkedList<Date> dateList = new LinkedList<Date>(dmKeySet);
                // ignore newly released com
                if (dmMap.size() > mimumIgonreSize) {
                    //TODO maybe it can be reorganized
                    for (int j = buyingDayIndex; j < dateList.size() - PATTERN_LENGTH; j++) {
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
                                List rpmStockDateList = (List) dateList.subList(j - PATTERN_LENGTH + 1, j + holdingPeriod);
                                rpm.setDailyStocks(generateRPMDailyStockModel(rpmStockDateList, sm));
                                rpm.setIndex(sm.getStockName() + Constants.DELIMITER + total++);
                                rawPatternDao.save(rpm);
                                break;
                            }
                        }
                    }
                }
                logger.info(sm.getStockName() + " " + total + " saved!");
            }catch(Exception e){
                logger.error("in parse", e);
            }
        }
    }

    @Override
    public RawPatternModel getRPMWithDate4Testing(StockModel sm, Date date) throws Exception{
        try{
            RawPatternModel rpm = new RawPatternModel();
            LinkedHashMap<Date, DailyStockModel> dmMap = sm.getDailyStocks();
            TreeSet<Date> dmKeySet = new TreeSet<>(dmMap.keySet());
            LinkedList<Date> dateList = new LinkedList<>(dmKeySet);
            int tailingEleIndex = dateList.indexOf(date);
            if(tailingEleIndex == -1 || tailingEleIndex - PATTERN_LENGTH<0){
                return null;
            }
            List rpmStockDateList = (List) dateList.subList(tailingEleIndex - PATTERN_LENGTH, tailingEleIndex);
            rpm.setDailyStocks(generateRPMDailyStockModel(rpmStockDateList, sm));
            rpm.setBuyingDate(date);
            rpm.setStockName(sm.getStockName());
            return rpm;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Double takeDailyAverage(DailyStockModel dsm) throws Exception{
        Double open = Double.parseDouble(dsm.getOpen());
        Double close = Double.parseDouble(dsm.getClose());
        Double high = Double.parseDouble(dsm.getHigh());
        Double low = Double.parseDouble(dsm.getLow());
        return (open + close + high + low)/4;
    }

    //make pattern from StockModel
    public static LinkedHashMap<Date,DailyStockModel> generateRPMDailyStockModel(List<Date> dateList, StockModel sm) throws Exception{
        LinkedHashMap<Date,DailyStockModel> smMap = sm.getDailyStocks();
        LinkedHashMap<Date,DailyStockModel> dsmMap = new LinkedHashMap<Date, DailyStockModel>();
        for(Date date : dateList){
            dsmMap.put(date, smMap.get(date));
        }
        return dsmMap;
    }

}