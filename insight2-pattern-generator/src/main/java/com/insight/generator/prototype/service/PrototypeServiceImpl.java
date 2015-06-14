package com.insight.generator.prototype.service;

import com.insight.generator.prototype.dao.PrototypeDao;
import com.insight.model.PatternPrototype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PC on 4/6/2015.
 */
@Service
public class PrototypeServiceImpl implements PrototypeService{

    @Autowired
    PrototypeDao prototypeDao;

    @Override
    public void cleanAndReduce(int sizeThreshold){
        prototypeDao.cleanAndReduce(sizeThreshold);
    }

    @Override
    public List<PatternPrototype> filter(long sizeThreshold){
        return prototypeDao.filter(sizeThreshold);
    }
}
