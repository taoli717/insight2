package com.insight.generator.matching.service;

import com.insight.generator.dao.PatternMatrixDao;
import com.insight.generator.matching.dao.PatternCosineSimilarityDao;
import com.insight.model.Constants;
import com.insight.model.PatternCosineSimilarity;
import com.insight.model.PatternMatrix;
import com.insight.model.PatternType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by PC on 3/3/2015.
 */
@Service
//@DependsOn("patternMatrixService")
public class PatternCosineSimilarityServiceImpl implements PatternCosineSimilarityService {

    @Autowired
    private PatternCosineSimilarityDao patternCosineSimilarityDao;

    @Autowired
    PatternMatrixDao patternMatrixDao;

    private static final Logger logger = Logger.getLogger(PatternCosineSimilarityServiceImpl.class);

    //@PostConstruct
    public void postContruct(){
        for(long i=0l; i<200; i++){
            PatternMatrix pm = patternMatrixDao.get(i);
            if(pm == null){
                break;
            }
            logger.info(pm.index);
            PatternCosineSimilarity pcs = null;
            for(long j=0l; ; j++){
                if(j == i){
                    continue;
                }
                PatternMatrix pm2 = patternMatrixDao.get(j);
                if(pm2 == null){
                    break;
                }
                logger.info(pm.index + " processing " + pm2.getIndex());
                pcs = this.compare(pm, pm2, pcs);
            }
            if(pcs != null){
                Map<String, Map<PatternType, Map<Integer, Double>>> cosines = pcs.getCosineValues();
                Stream<Map.Entry<String, Map<PatternType, Map<Integer, Double>>>> st = cosines.entrySet().stream();
                LinkedList<Map.Entry<String, Map<PatternType, Map<Integer, Double>>>> percentList = new LinkedList<>();
                //st.sorted((e1, e2) -> Double.compare(e2.getValue().get(PatternType.PERCENT).get(0),e1.getValue().get(PatternType.PERCENT).get(0)))
                //        .forEach(e -> percentList.add(new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue())));
                LinkedList<Map.Entry<String, Map<PatternType, Map<Integer, Double>>>> meanList = new LinkedList<>();
                st = cosines.entrySet().stream();
                st.sorted((e1, e2) -> Double.compare(e2.getValue().get(PatternType.DIFF_MEAN).get(0),e1.getValue().get(PatternType.DIFF_MEAN).get(0)))
                        .forEach(e -> meanList.add(new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue())));
                LinkedHashMap<String, Map<PatternType, Map<Integer, Double>>> result = new LinkedHashMap<>();
                int size = 0;
                int limit = 50;
                for(int k=0; k<limit; k++){

                    Map.Entry<String, Map<PatternType, Map<Integer, Double>>> entry = null;

/*                    if(k%2==0){
                        entry = percentList.get(k);
                    }else{
                        entry = meanList.get(k);
                    }*/

                    entry = meanList.get(k);
                    if(entry == null){
                        break;
                    }

                    result.put(entry.getKey(), entry.getValue());
                    size++;
                }
                pcs.setCosineValues(result);
                Double[] normMeanPrice =  ArrayUtils.toObject(pm.diffMeanMatrixNorm.getRow(5));
                pcs.setAverageNormPriceAbsoluteTotal(computeAbsoluteMean(Arrays.asList(normMeanPrice)));
                logger.info(pm.index + " stored ");
                patternCosineSimilarityDao.save(pcs);
            }
        }
    }

    @Override
    public PatternCosineSimilarity compare(PatternMatrix patternMatrix, PatternMatrix patternMatrix2, PatternCosineSimilarity pcs) {
        Map<PatternType, Map<Integer, Double>> resultMap = new HashMap<>();
        Map<Integer, Double> diffMeanMap = new HashMap<>();
        //Map<Integer, Double> percentMap = new HashMap<>();
        for(int i=0; i<=5; i++){
            RealVector diffMeanRV = new ArrayRealVector(patternMatrix.getDiffMeanMatrix().getRow(i));
            RealVector diffMeanRV2 = new ArrayRealVector(patternMatrix2.getDiffMeanMatrix().getRow(i));
            Double meanCosineDiff = diffMeanRV.cosine(diffMeanRV2);
            diffMeanMap.put(i, meanCosineDiff);

/*            RealVector percentRV = new ArrayRealVector(patternMatrix.getPercentMatrix().getRow(i));
            RealVector percentRV2 = new ArrayRealVector(patternMatrix2.getPercentMatrix().getRow(i));
            Double percentCosineDiff = percentRV.cosine(percentRV2);
            percentMap.put(i, percentCosineDiff);*/
        }
        resultMap.put(PatternType.DIFF_MEAN, diffMeanMap);
        //resultMap.put(PatternType.PERCENT, percentMap);

        if(pcs == null){
            pcs = new PatternCosineSimilarity();
            pcs.setStockName(patternMatrix.getStockName());
            pcs.setBuyingDate(patternMatrix.getBuyingDate());
            pcs.setSellingDate(patternMatrix.getSellingDate());
            pcs.setSeq(patternMatrix.getSeq());
            pcs.setIndex();
        }

        // public void setCosineValues(Map<String, Map<String, Map>> cosineValues)
        Map<String, Map<PatternType, Map<Integer, Double>>> cosineValue = pcs.getCosineValues();
        if(cosineValue == null){
            cosineValue = new HashMap<>();
        }
        cosineValue.put(patternMatrix2.getStockName() + Constants.DELIMITER + patternMatrix2.getSeq(), resultMap);
        pcs.setCosineValues(cosineValue);
        return pcs;
    }

    public double compare(PatternMatrix patternMatrix, PatternMatrix patternMatrix2) {
        RealVector diffMeanRV = new ArrayRealVector(patternMatrix.getDiffMeanMatrix().getRow(5));
        RealVector diffMeanRV2 = new ArrayRealVector(patternMatrix2.getDiffMeanMatrix().getRow(5));
        Double meanCosineDiff = diffMeanRV.cosine(diffMeanRV2);
        return meanCosineDiff;
    }


    public static Double computeAbsoluteMean(Collection<Double> values){
        return values.stream().reduce(0d,(a,b)->{
            return Math.abs(a) + Math.abs(b);
        });
    }
}
