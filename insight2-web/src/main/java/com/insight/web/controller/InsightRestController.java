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
public class InsightRestController {

    @Autowired
    RawPatternDao rawPatternDao;

    @RequestMapping("/api/sample")
    public RawPatternModel getSampleRpm(){
        return (RawPatternModel) rawPatternDao.loadNext(Integer.parseInt(String.valueOf(0)));
    }
}