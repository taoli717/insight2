package com.insight.generator.prototype.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.insight.generator.aggregate.service.PatternAggregateServiceImpl;
import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.model.PatternPrototype;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by tli on 4/6/2015.
 */
@Service
@Slf4j
public class PrototypeServiceImpl implements PrototypeService{

    @Autowired
    PrototypeDao prototypeDao;

    public static int calcuateDiff(String a, String b){
        int unitLength = a.length()/ PatternAggregateServiceImpl.MASK_SIZE;
        int diffResult = 0;
        int length = a.length();
        for(int i=0; i<length; i+=unitLength){
            String int1 = a.substring(0, unitLength);
            String int2 = b.substring(0, unitLength);
            a = a.substring(unitLength, a.length());
            b = b.substring(unitLength, b.length());
            diffResult = diffResult + intDiff(int1, int2);
        }
        return diffResult;
    }

    public static int intDiff(String first, String second){
        StringBuilder firstBuilder = new StringBuilder();
        StringBuilder secondBuilder = new StringBuilder();
        for(int i=0; i<first.length(); i++){
            firstBuilder.append(uniform(Integer.toString(Integer.valueOf(Character.toString(first.charAt(i))), 2)));
            secondBuilder.append(uniform(Integer.toString(Integer.valueOf(Character.toString(second.charAt(i))), 2)));
        }
        int result = Math.abs(Integer.valueOf(firstBuilder.indexOf("1"))-Integer.valueOf(secondBuilder.indexOf("1")));
        return result;
    }

    public static String uniform(String input){
        Double powerOf2 = Math.log(Double.valueOf(PatternAggregateServiceImpl.MASK_SIZE).doubleValue())/Math.log(2d);
        double length = powerOf2 +1 ;
        while(input.length() != length){
            input = 0 + input;
        }
        return input;
    }

    @Override
    public void cleanAndReduce(int sizeThreshold){
        prototypeDao.cleanAndReduce(sizeThreshold);
    }

    @Override
    public List<PatternPrototype> filter(long sizeThreshold){
        return prototypeDao.filter(sizeThreshold);
    }

    @Override
    public Multimap<Integer, PatternPrototype> start() throws Exception{
        log.info("Started");
        Multimap<Integer, PatternPrototype> resultMap = ArrayListMultimap.create();
        List<PatternPrototype> patternList = this.filter(50l);// focus on prototype has more than 50 occurrences
        patternList.sort((p1, p2)->-Long.valueOf(p1.getSize()).compareTo(Long.valueOf(p2.getSize())));
        PatternPrototype first = patternList.get(0);
        int j = 1;
        patternList.stream().forEach(p->{
            int diff = calcuateDiff(first.getId(),p.getId());
                resultMap.put(diff, p);
        });
        //writer = new PrintWriter("the-file-name.txt", "UTF-8");
        for(int i=1; i<5; i++){
            log.info(String.valueOf(i));
            log.info(first.getId());
            for(PatternPrototype p:resultMap.get(i)){
                //writer.print("\"" + s + "\", ");
                log.info(p.getId() + " " + p.getSize());
            }
            //writer.println("");
            log.info("");
        }
        log.info("======================================= Prototype Similarity Finding Done! ==============================================");
        return resultMap;
    }
}
