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
    private static final int MASK_SIZE = 8;
    private static final int PATTERN_LENGTH = 64;

    //@PostConstruct
    public void init(){
        this.parse();
    }

    @Override
    public void parse(){
        for(String stockName : TestStockName.ALL_STOCK_NAME){
            try{
                long index = 0;
                PatternMatrix pm = patternMatrixDao.get(stockName, index++);
                Map<String, Integer> cacheResult = new HashMap<>();
                while(pm!=null){
                    double[] diffMeanAvgRow = pm.diffMeanMatrixNorm.getRow(5);
                    List<Double> list = Doubles.asList(diffMeanAvgRow);
                    ArrayList<Double> diffMeanAvgList = new ArrayList<>();
                    diffMeanAvgList.addAll(list);
                    Double max = diffMeanAvgList.stream().max(Double::compareTo).get();
                    Double min = diffMeanAvgList.stream().min(Double::compareTo).get();
                    double yAixUnit = (max-min)/(MASK_SIZE-1);
                    RealMatrix pricePatternMask = new Array2DRowRealMatrix(MASK_SIZE, MASK_SIZE);
                    if(pm.getSeq()==85){
                        logger.trace("");
                    }
                    //calculate average price
                    for(int i=0; i<MASK_SIZE; i++){
                        double price = takeAverage(diffMeanAvgList, i, MASK_SIZE)-min;
                        int unit = (int) (price/yAixUnit);
                        double[] column = pricePatternMask.getColumn(i);
                        column[(MASK_SIZE-1)-unit] = 1;
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
                        patternSintaure.append(keepTwoDigit(hexStr));
                    }
                    Aggregation aggregation = new Aggregation();
                    aggregation.setMask(pricePatternMask);
                    aggregation.setIndex(pm.getIndex());
                    aggregation.setSellingDate(pm.getSellingDate());
                    aggregation.setBuyingDate(pm.getBuyingDate());
                    aggregation.setStockName(pm.getStockName());
                    aggregation.setPatternSignature(patternSintaure.toString());
                    aggregation.setSeq(pm.getSeq());
                    PatternPrototype pp = prototypeDao.get(patternSintaure.toString());
                    if(pp == null){
                        pp = new PatternPrototype();
                        pp.setId(patternSintaure.toString());
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
                    Integer count = cacheResult.getOrDefault(patternSintaure.toString(),0);
                    cacheResult.put(patternSintaure.toString(),count+1);
                    prototypeDao.save(pp);
                    //aggregateDao.save(aggregation);
                    logger.info(aggregation.getIndex() + " saved");
                    pm = patternMatrixDao.get(stockName, index++);
                }
                cacheResult.entrySet().stream().sorted((p1,p2)->{
                    return p2.getValue().compareTo(p1.getValue());
                }).forEach(p->{
                    logger.info(p.getKey());
                    logger.info(p.getValue());
                    logger.info("======================================");
                });
            }catch (Exception e){
                logger.error(e);
            }
        }

    }
    public static String keepTwoDigit(String input){
        if(input.length()==1){
            return 0+input;
        }else{
            return input;
        }
    }

    public static double takeAverage(List<Double> list, int i, int maskSize){
        int unitLength = PATTERN_LENGTH/maskSize;
        List<Double> resultList = list.subList((i)*unitLength, (i+1)*unitLength-1);
        return resultList.stream().mapToDouble(l->l).average().getAsDouble();
    }
}
