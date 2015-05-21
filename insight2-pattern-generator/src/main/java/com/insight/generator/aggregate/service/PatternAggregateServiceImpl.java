package com.insight.generator.aggregate.service;

import com.google.common.primitives.Doubles;
import com.insight.generator.aggregate.dao.AggregateDao;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.generator.constant.TestStockName;
import com.insight.generator.dao.PatternMatrixDao;
import com.insight.model.Aggregation;
import com.insight.model.PatternMatrix;
import com.insight.model.PatternPrototype;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by PC on 3/22/2015.
 */
@Service
//@DependsOn("patternCosineSimilarityService")
public class PatternAggregateServiceImpl implements PatternAggregateService{

    @Autowired
    private PatternMatrixDao patternMatrixDao;

    @Autowired
    private AggregateDao aggregateDao;

    @Autowired
    private PrototypeDao prototypeDao;

    private static final Logger logger = Logger.getLogger(PatternAggregateServiceImpl.class);
    public static final int MASK_SIZE = 8;
    public static final int PATTERN_LENGTH = 64;
    Map<String, Integer> cacheResult = new HashMap<>();

    //@PostConstruct
    public void init(){
        this.parse();
    }

    @Override
    public void parse(){
        for(String stockName : TestStockName.ALL_STOCK_NAME) {
            try {
                long index = 0;
                PatternMatrix pm = patternMatrixDao.get(stockName, index++);
                while (pm != null) {
                    Aggregation aggregation = convertPM2Aggregation(pm);
                    updatePrototype(aggregation);
                    logger.trace(aggregation.getIndex() + " saved");
                    pm = patternMatrixDao.get(stockName, index++);
                }
                cacheResult.entrySet().stream().sorted((p1, p2) -> {
                    return p2.getValue().compareTo(p1.getValue());
                }).forEach(p -> {
                    logger.info(p.getKey()+ " - " + p.getValue());
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Aggregation convertPM2Aggregation(PatternMatrix pm){
        double[] diffMeanAvgRow = pm.diffMeanMatrixNorm.getRow(5);
        List<Double> list = Doubles.asList(diffMeanAvgRow);
        ArrayList<Double> diffMeanAvgList = new ArrayList<>();
        diffMeanAvgList.addAll(list);
        Double max = diffMeanAvgList.stream().max(Double::compareTo).get();
        Double min = diffMeanAvgList.stream().min(Double::compareTo).get();
        double yAixUnit = (max-min)/(MASK_SIZE);
        RealMatrix pricePatternMask = new Array2DRowRealMatrix(MASK_SIZE, MASK_SIZE);
        //calculate average price
        for(int i=0; i<MASK_SIZE; i++){
            double price = takeAverage(diffMeanAvgList, i, MASK_SIZE)-min;
            int unit = (int) (price/yAixUnit);
            double[] column = pricePatternMask.getColumn(i);
            int weightPosition = (MASK_SIZE-1)-unit;
            column[weightPosition] = 1;
            pricePatternMask.setColumn(i, column);//maybe not necessary;
        }

        StringBuilder patternSintaure = new StringBuilder();
        for(int i=0; i<MASK_SIZE; i++){
            StringBuilder result = new StringBuilder();
            Doubles.asList(pricePatternMask.getColumn(i)).stream().forEach(p->result.append(Integer.valueOf(p.intValue()).toString()));
            int decimal = Integer.parseInt(result.toString(),2);
            String hexStr = Integer.toString(decimal,16);
            //String columnValue = toHex(result.toString());
            //logger.info(hexStr);
            patternSintaure.append(uniform(hexStr));
        }
        Aggregation aggregation = new Aggregation();
        aggregation.setMask(pricePatternMask);
        aggregation.setIndex(pm.getIndex());
        aggregation.setSellingDate(pm.getSellingDate());
        aggregation.setBuyingDate(pm.getBuyingDate());
        aggregation.setStockName(pm.getStockName());
        aggregation.setPatternSignature(patternSintaure.toString());
        aggregation.setSeq(pm.getSeq());
        return aggregation;
    }

    public PatternPrototype updatePrototype(Aggregation aggregation){
        String patternSignature = aggregation.getPatternSignature();
        PatternPrototype pp = prototypeDao.get(patternSignature.toString());
        if(pp == null){
            pp = new PatternPrototype();
            pp.setId(patternSignature.toString());
            Set<Aggregation> members = new HashSet<>();
            members.add(aggregation);
            pp.setMembers(members);
        }else{
            Set<Aggregation> members = pp.getMembers();
            if(members==null){
                members = new HashSet<>();
            }
            members.add(aggregation);
            pp.setMembers(members);
        }
        pp.setSize(pp.getMembers().size());
        Integer count = cacheResult.getOrDefault(patternSignature.toString(), 0);
        cacheResult.put(patternSignature.toString(), ++count);
        prototypeDao.save(pp);
        return pp;
    }

    public static String uniform(String input){

        Double powerOf2 = Math.log(Double.valueOf(MASK_SIZE).doubleValue())/Math.log(2d);
        double length = Math.pow(2d, powerOf2-2);
        while(input.length() != length){
            input = 0 + input;
        }
        return input;
    }

    public static double takeAverage(List<Double> list, int i, int maskSize){
        int unitLength = PATTERN_LENGTH/maskSize;
        List<Double> resultList = list.subList((i)*unitLength, (i+1)*unitLength-1);
        return resultList.stream().mapToDouble(l->l).average().getAsDouble();
    }

}