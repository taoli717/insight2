package com.insight.web.controller;

import com.mongodb.util.JSON;
import com.insight.generator.dao.RawPatternDao;
import com.insight.generator.model.RawPatternModel;
import com.insight.generator.service.StockDataGeneratorService;
import com.insight.generator.service.StockParserService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class InsightIndexController {

    private static final Logger logger = Logger.getLogger(InsightIndexController.class);

    @Autowired
    RawPatternDao rawPatternDao;

    @Autowired
    StockDataGeneratorService stockDataGeneratorService;

    @Autowired
    StockParserService stockParserService;

    // sample page to show how the data looks like
    @RequestMapping("/index")
    public String hello(@RequestParam(value="index", required=false, defaultValue="0") String index, Model model) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(Integer.parseInt(index), null);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(byteArrayOutputStream, rpm);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnJson = JSON.serialize(byteArrayOutputStream.toString());
        model.addAttribute("stockSample", returnJson.substring(1, returnJson.length()-1).replace("\\\"","\""));
        logger.info(JSON.serialize(byteArrayOutputStream.toString()));
        return "index";
    }

}
