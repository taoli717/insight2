package com.insight.generator.service;

import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.PatternMatrixDao;
import com.insight.generator.dao.RawPatternDao;
import com.insight.generator.dao.RawPatternDaoImp;
import com.insight.model.DailyStockModel;
import com.insight.model.PatternMatrix;
import com.insight.model.RawPatternModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
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

    // @PostConstruct
    @Override
    public void parse() throws Exception {
       StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //for(String stockName : TestStockName.ALL_STOCK_NAME){

        for(String stockName : TestStockName.ALL_STOCK_NAME){
            long i = 0;
            RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(i, stockName);
            if(rpm!=null && rpm.getStockName()!=null){
                logger.info(" Parsing " + rpm.getStockName());
            }
            while(rpm!=null && rpm.getStockName()!= null){
                PatternMatrix pm = parseRawPatternModel(rpm, i);
                savePatternMatrix(pm);
                rpm = (RawPatternModel) rawPatternDao.load(++i, stockName);
            }
            logger.info(" total " + i);
            //}
        }
        stopWatch.stop();
        logger.info("time used: " + stopWatch);
    }

    @Override
    public PatternMatrix parseRawPatternModel(RawPatternModel rpm, long seq) throws Exception {
        LinkedHashMap<Date, DailyStockModel> models = rpm.getDailyStocks();
        List<Double> highs = new ArrayList<>();
        List<Double> lows = new ArrayList<>();
        List<Double> opens = new ArrayList<>();
        List<Double> closes = new ArrayList<>();
        List<Double> vloumns = new ArrayList<>();
        Set<Date> dateSet = models.keySet();
        for(Date date : dateSet){
            if(date.compareTo(rpm.getBuyingDate())<=0){
                DailyStockModel dailyStockModel = models.get(date);
                highs.add(Double.parseDouble(dailyStockModel.getHigh()));
                lows.add(Double.parseDouble(dailyStockModel.getLow()));
                opens.add(Double.parseDouble(dailyStockModel.getOpen()));
                closes.add(Double.parseDouble(dailyStockModel.getClose()));
                vloumns.add(Double.parseDouble(dailyStockModel.getVolume()));
            }
        }

        RealMatrix diffMeanMatrix = generateDiffMeanMatrix(highs, lows, opens, closes, vloumns);
        return convertRpm2Pm(rpm, diffMeanMatrix, seq);
    }

    public static PatternMatrix convertRpm2Pm(RawPatternModel rpm, RealMatrix diffMeanMatrix, long seq){
        PatternMatrix pm = new PatternMatrix();
        pm.setStockName(rpm.getStockName());
        pm.setBuyingDate(rpm.getBuyingDate());
        pm.setSellingDate(rpm.getSellingDate());
        pm.setDiffMeanMatrix(diffMeanMatrix);
        pm.setDiffMeanMatrixNorm(caculate6RowNormVector(diffMeanMatrix));
        pm.setSeq(seq);
        pm.setIndex();
        pm.setMean(getMeanFromArray(diffMeanMatrix.getRow(5)));
        pm.setMax(getMaxFromArray(diffMeanMatrix.getRow(5)));
        pm.setMin(getMinFromArray(diffMeanMatrix.getRow(5)));
        return pm;
    }

    public static Double getMeanFromArray(double[] doubles){
        List<Double> list = Arrays.asList(ArrayUtils.toObject(doubles));
        return list.stream().mapToDouble(i->i).average().getAsDouble();
    }
    public static Double getMaxFromArray(double[] doubles){
        List<Double> list = Arrays.asList(ArrayUtils.toObject(doubles));
        return list.stream().mapToDouble(i ->i).max().getAsDouble();
    }
    public static Double getMinFromArray(double[] doubles){
        List<Double> list = Arrays.asList(ArrayUtils.toObject(doubles));
        return list.stream().mapToDouble(i ->i).min().getAsDouble();
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
        Double max = Collections.max(list);
        Double min = Collections.min(list);
        for(Double data : list){
            result.add((data - mean)/(max-min));
        }
        return result;
    }

    public static List<Double> generatePercentColumn(List<Double> list){
        List<Double> result = new LinkedList<>();
        for(int i=0; i<list.size(); i++){
            if(i==0){
                result.add(1D);
            }else{
                Double resultValue = list.get(i)/list.get(i-1);
                result.add((resultValue-1)*10);
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

    public static RealMatrix caculate6RowNormVector(RealMatrix matrix){
        RealMatrix resultMatrix = MatrixUtils.createRealMatrix(matrix.getData());
        for(int i=0; i<matrix.getRowDimension(); i++){
            RealVector vector = matrix.getRowVector(i);
            RealVector unityNormalizedVector = unityNormalizeVector(vector);
            resultMatrix.setRowVector(i,unityNormalizedVector);
        }
        return resultMatrix;
    }

    public static RealVector unityNormalizeVector(RealVector vector){
        double norm = vector.getNorm();
        for(int i=0; i<vector.getMaxIndex(); i++){
            vector.setEntry(i, vector.getEntry(i)/norm);
        }
        return vector;
    }
}
