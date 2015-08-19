package com.insight.generator.dao;

import com.insight.model.PatternMatrix;
import com.mongodb.DBObject;

/**
 * Created by tli on 3/22/2015.
 */
public class PatternMatrixAdapter {

    //TODO not done yet
    public static PatternMatrix convert(DBObject dbObject){
        PatternMatrix patternMatrix = new PatternMatrix();
        patternMatrix.setStockName((String)dbObject.get(""));
        return null;
    }
}
