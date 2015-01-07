package com.insight.web.controller;

import com.mongodb.util.JSON;
import generator.dao.RawPatternDao;
import generator.model.RawPatternModel;
import generator.parser.StockParser;
import generator.service.StockDataGeneratorService;
import generator.service.StockParserService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
            RawPatternModel rpm = (RawPatternModel) rawPatternDao.loadNext(Integer.parseInt(index));
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(byteArrayOutputStream, rpm);

        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("stockSample", JSON.serialize(byteArrayOutputStream.toString()));
        return "helloworld";
    }

    // TODO Useless view, just a way to triger data generator for installation. only use when the first time install
    @RequestMapping("/init")
    public String init(Model model) throws Exception {
        stockDataGeneratorService.generate();
        stockParserService.parse();
        return "init";
    }

    //TODO should redirect to this page when the installation is done
    @RequestMapping("/init/success")
    public String initSuccess(Model model){
        return "init-success";
    }

}
