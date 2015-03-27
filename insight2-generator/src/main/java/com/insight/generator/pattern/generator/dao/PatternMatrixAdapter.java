package com.insight.generator.pattern.generator.dao;

import com.insight.generator.pattern.generator.model.PatternMatrix;
import com.mongodb.DBObject;

/**
 * Created by PC on 3/22/2015.
 */
public class PatternMatrixAdapter {

    //TODO not done yet
    public static PatternMatrix convert(DBObject dbObject){
        PatternMatrix patternMatrix = new PatternMatrix();
        patternMatrix.setStockName((String)dbObject.get(""));
        return null;
    }
}
