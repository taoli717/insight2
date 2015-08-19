package com.insight.generator.dao;

import com.insight.model.SetUpModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by tli on 1/11/2015.
 */
@Service
public class SetUpDaoImpl implements SetUpDao{

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public boolean isSetUp() {
        SetUpModel setUpModel = mongoOperation.findOne(new Query(), SetUpModel.class);
        return setUpModel!=null && setUpModel.isSetUp();
    }

    @Override
    public void setUpScccess() {
        SetUpModel setUpModel = mongoOperation.findOne(new Query(), SetUpModel.class);
        if(setUpModel==null){
            setUpModel = new SetUpModel();
        }
        setUpModel.setSetUp(true);
        mongoOperation.save(setUpModel);
    }

}
