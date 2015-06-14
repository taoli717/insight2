package com.insight.generator.runner.prototpye;

import com.insight.generator.PatternGeneratorConfig;
import com.insight.generator.aggregate.service.PatternAggregateServiceImpl;
import com.insight.generator.prototype.service.PrototypeService;
import com.insight.model.PatternPrototype;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by PC on 3/31/2015.
 */
public class PrototypeSimilarityFindingRunner implements InitializingBean {

    private static final Logger logger = Logger.getLogger(PrototypeSimilarityFindingRunner.class);

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public static void main(String[] args){
        //BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PatternGeneratorConfig.class);
        PrototypeService prototypeService = (PrototypeService) ctx.getBean("prototypeService");

        try{
            Map<Integer, Set<PatternPrototype>> resultMap = new HashMap<>();
            List<PatternPrototype> patternList = prototypeService.filter(50l);// focus on prototype has more than 50 occurrences
            patternList.sort((p1, p2)->-Long.valueOf(p1.getSize()).compareTo(Long.valueOf(p2.getSize())));
            PatternPrototype first = patternList.get(0);
                int j = 1;
                while(j<patternList.size()){
                    int diff = calcuateDiff(first.getId(),patternList.get(j).getId());
                    Set<PatternPrototype> currentSet = resultMap.getOrDefault(diff, new HashSet<>());
                    currentSet.add(patternList.get(j));
                    resultMap.put(diff, currentSet);
                    j++;
                }
            //writer = new PrintWriter("the-file-name.txt", "UTF-8");
            for(int i=1; i<5; i++){
                logger.info(i);
                logger.info(first.getId());
                for(PatternPrototype p:resultMap.get(i)){
                    //writer.print("\"" + s + "\", ");
                    logger.info("\"" + p.getId() + "\"" + p.getSize() + "\"");
                }
                //writer.println("");
                logger.info("");
            }
            logger.info("======================================= Prototype Similarity Finding Done! ==============================================");
        }catch (Exception e){
/*            if(writer!=null){
                writer.close();
            }*/
            logger.error(e.getMessage(), e);
        }
    }

    public static int calcuateDiff(String a, String b){
        int unitLength = a.length()/PatternAggregateServiceImpl.MASK_SIZE;
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
}
