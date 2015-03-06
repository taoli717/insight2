package com.insight.generator.matching.service;

import com.insight.generator.matching.dao.PatternCosineSimilarityDao;
import com.insight.generator.matching.model.PatternCosineSimilarity;
import com.insight.generator.pattern.generator.dao.PatternMatrixDao;
import com.insight.generator.pattern.generator.model.PatternMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by PC on 3/3/2015.
 */
public class PatternCosineSimilarityServiceImpl implements PatternCosineSimilarityService {

    @Autowired
    private PatternCosineSimilarityDao patternCosineSimilarityDao;

    public enum PatternType{
        DIFF_MEAN, PERCENT
    }

    @Autowired
    PatternMatrixDao patternMatrixDao;

    private static final Logger logger = Logger.getLogger(PatternCosineSimilarityServiceImpl.class);

    @PostConstruct
    public void postContruct(){
/**/        for(long i=0l; ; i++){
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
                Map<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> cosines = pcs.getCosineValues();
                Stream<Map.Entry<String, Map<PatternType, Map<Integer, Double>>>> st = cosines.entrySet().stream();
                LinkedHashMap<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> temp = new LinkedHashMap<>();
                st.sorted((e1, e2) -> Double.compare(e2.getValue().get(PatternType.DIFF_MEAN).get(0),e1.getValue().get(PatternType.DIFF_MEAN).get(0)))
                        .forEach(e -> temp.put(e.getKey(), e.getValue()));
                LinkedHashMap<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> result = new LinkedHashMap<>();
                int size = 0;
                int limit = 50;
                for(Map.Entry<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> entry : temp.entrySet()){
                    if(size>=limit){
                        break;
                    }
                    result.put(entry.getKey(),entry.getValue());
                    size++;
                }
                pcs.setCosineValues(result);
                logger.info(pm.index + " stored ");
                patternCosineSimilarityDao.save(pcs);
            }
        }
    }

    @Override
    public PatternCosineSimilarity compare(PatternMatrix patternMatrix, PatternMatrix patternMatrix2, PatternCosineSimilarity pcs) {
        Map<PatternType, Map<Integer, Double>> resultMap = new HashMap<>();
        Map<Integer, Double> diffMeanMap = new HashMap<>();
        Map<Integer, Double> percentMap = new HashMap<>();
        for(int i=0; i<=5; i++){
            RealVector diffMeanRV = new ArrayRealVector(patternMatrix.getDiffMeanMatrix().getRow(i));
            RealVector diffMeanRV2 = new ArrayRealVector(patternMatrix2.getDiffMeanMatrix().getRow(i));
            Double meanCosineDiff = diffMeanRV.cosine(diffMeanRV2);
            diffMeanMap.put(i, meanCosineDiff);

            RealVector percentRV = new ArrayRealVector(patternMatrix.getPercentMatrix().getRow(i));
            RealVector percentRV2 = new ArrayRealVector(patternMatrix2.getPercentMatrix().getRow(i));
            Double percentCosineDiff = percentRV.cosine(percentRV2);
            percentMap.put(i, percentCosineDiff);
        }
        resultMap.put(PatternType.DIFF_MEAN, diffMeanMap);
        resultMap.put(PatternType.PERCENT, percentMap);

        if(pcs == null){
            pcs = new PatternCosineSimilarity();
            pcs.setIndex(patternMatrix.getIndex());
            pcs.setBuyingDate(patternMatrix.getBuyingDate());
            pcs.setStockName(patternMatrix.getStockName());
            pcs.setSellingDate(patternMatrix.getSellingDate());
            pcs.setSeq(patternMatrix.getSeq());
        }

        // public void setCosineValues(Map<String, Map<String, Map>> cosineValues)
        Map<String, Map<PatternType, Map<Integer, Double>>> cosineValue = pcs.getCosineValues();
        if(cosineValue == null){
            cosineValue = new HashMap<>();
        }
        cosineValue.put(patternMatrix2.getIndex(), resultMap);
        pcs.setCosineValues(cosineValue);
        return pcs;
    }
}
