package com.insight.generator.pattern.generator.service;

import com.insight.generator.dao.RawPatternDao;
import com.insight.generator.dao.RawPatternDaoImp;
import com.insight.generator.model.DailyStockModel;
import com.insight.generator.model.RawPatternModel;
import com.insight.generator.pattern.generator.dao.PatternMatrixDao;
import com.insight.generator.pattern.generator.model.PatternMatrix;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by PC on 2/1/2015.
 */
@Service
public class PatternMatrixServiceImpl implements PatternMatrixService {

    @Autowired
    RawPatternDao rawPatternDao;

    @Autowired
    PatternMatrixDao patternMatrixDao;

    private static final Logger logger = Logger.getLogger(PatternMatrixServiceImpl.class);

    @PostConstruct
    public void parse() throws Exception {
        RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(0, RawPatternDaoImp.TEST_STOCK_NAME);
        logger.info(" Parsing " + rpm.getStockName() + rpm.getSeq());
        PatternMatrix pm = parseRawPatternModel(rpm);
        savePatternMatrix(pm);
    }

    @Override
    public PatternMatrix parseRawPatternModel(RawPatternModel rpm) throws Exception {
        LinkedHashMap<Date, DailyStockModel> models = rpm.getDailyStocks();
        List<Double> highs = new ArrayList<>();
        List<Double> lows = new ArrayList<>();
        List<Double> opens = new ArrayList<>();
        List<Double> closes = new ArrayList<>();
        List<Double> vloumns = new ArrayList<>();
        Set<Date> dateSet = models.keySet();
        for(Date date : dateSet){
            if(date.compareTo(rpm.getBuyingDate())<0){
                DailyStockModel dailyStockModel = models.get(date);
                highs.add(Double.parseDouble(dailyStockModel.getHigh()));
                lows.add(Double.parseDouble(dailyStockModel.getLow()));
                opens.add(Double.parseDouble(dailyStockModel.getOpen()));
                closes.add(Double.parseDouble(dailyStockModel.getClose()));
                vloumns.add(Double.parseDouble(dailyStockModel.getVolume()));
            }
        }

        RealMatrix diffMeanMatrix = generateDiffMeanMatrix(highs, lows, opens, closes, vloumns);
        RealMatrix percentMatrix = generatePercentMatrix(highs, lows, opens, closes, vloumns);
        PatternMatrix pm = new PatternMatrix();
        pm.setSeq(rpm.getSeq());
        pm.setStockName(rpm.getStockName());
        pm.setBuyingDate(rpm.getBuyingDate());
        pm.setSellingDate(rpm.getSellingDate());
        pm.setDiffMeanMatrix(diffMeanMatrix);
        pm.setPercentMatrix(percentMatrix);
        pm.setDiffMeanMatrixNorm(caculateAvgPriceVector(diffMeanMatrix));
        pm.setPercentMatrixNorm(caculateAvgPriceVector(percentMatrix));
        pm.setIndex();
        return pm;
    }

    @Override
    public void savePatternMatrix(PatternMatrix pm) {
        patternMatrixDao.save(pm);
    }

    public RealMatrix generateDiffMeanMatrix(List<Double> highs, List<Double> lows, List<Double> opens, List<Double> closes, List<Double> volumns ) throws Exception {
        return generateMatrix(generateDiffMeanColumn(highs),generateDiffMeanColumn(lows),generateDiffMeanColumn(opens),generateDiffMeanColumn(closes),generateDiffMeanColumn(volumns));
    }

    public RealMatrix generatePercentMatrix(List<Double> highs, List<Double> lows, List<Double> opens, List<Double> closes, List<Double> volumns ) throws Exception {
        return generateMatrix(generatePercentColumn(highs),generatePercentColumn(lows),generatePercentColumn(opens),generatePercentColumn(closes),generatePercentColumn(volumns));
    }

    public RealMatrix generateMatrix(List<Double> highs, List<Double> lows, List<Double> opens, List<Double> closes, List<Double> volumns ) throws Exception {

        double[][] doubles = new double[6][highs.size()];
        doubles[0] = list2Array(highs);
        doubles[1] = list2Array(lows);
        doubles[2] = list2Array(opens);
        doubles[3] = list2Array(closes);
        doubles[4] = list2Array(volumns);
        doubles[5] = takeAverage(highs, lows, opens,closes);
        RealMatrix m = MatrixUtils.createRealMatrix(doubles);
        return m;

    }

    public static double[] takeAverage(List<Double> highs, List<Double> lows, List<Double> opens, List<Double> closes) throws Exception {
        if(highs.size() == lows.size()&& opens.size() == closes.size() && lows.size() == opens.size()){
            double[] arr = new double[highs.size()];
            for(int i=0; i<highs.size(); i++){
                arr[i] = (highs.get(i) + lows.get(i) + opens.get(i) + closes.get(i)) / 4;
            }
            return arr;
        }else{
            throw new Exception("Price list length not equal");
        }

    }

    public static double[] list2Array(List<Double> list){
        Double[] arr = new Double[list.size()];
        arr = list.toArray(arr);
        return ArrayUtils.toPrimitive(arr);
    }

    public static List<Double> generateDiffMeanColumn(List<Double> list){

        double mean = getMean(list);
        List<Double> result = new LinkedList<>();
        for(Double data : list){
            result.add((data - mean)/mean);
        }
        return result;
    }

    public static List<Double> generatePercentColumn(List<Double> list){
        List<Double> result = new LinkedList<>();
        for(int i=0; i<list.size(); i++){
            if(i==0){
                result.add(1D);
            }else{
                result.add(list.get(i)/list.get(i-1));
            }
        }
        return result;
    }

    public static Double getMean(List<Double> list){
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for( int i = 0; i < list.size(); i++) {
            stats.addValue(list.get(i));
        }
        double mean = stats.getMean();
        return mean;
    }

    public static RealVector caculateAvgPriceVector(RealMatrix matrix){
        RealVector vector = matrix.getRowVector(4);
        return normalizeVector(vector);
    }

    public static RealVector normalizeVector(RealVector vector){
        double norm = vector.getNorm();
        for(int i=0; i<vector.getMaxIndex(); i++){
            vector.setEntry(i, vector.getEntry(i)/norm);
        }
        return vector;
    }
}
