package com.insight.web.controller;

import generator.dao.RawPatternDao;
import generator.model.RawPatternModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tli on 1/22/15.
 */
@RestController
@RequestMapping("api")
public class InsightRestController {

    @Autowired
    RawPatternDao rawPatternDao;

    @RequestMapping(value="sample")
    public RawPatternModel getSampleRpm(){
        RawPatternModel rpm = (RawPatternModel) rawPatternDao.loadNext(Integer.parseInt(String.valueOf(0)));
        return rpm;
    }
}