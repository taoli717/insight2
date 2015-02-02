package com.insight.web.controller;

        import com.insight.generator.dao.RawPatternDao;
        import com.insight.generator.model.RawPatternModel;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.PathVariable;
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
//TODO add defualt path variable
    @RequestMapping(value="sample/{id}")
    public RawPatternModel getRpm(@PathVariable("id") int i){
        RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(Integer.parseInt(String.valueOf(i)), null);
        return rpm;
    }

    @RequestMapping(value="sample")
    public RawPatternModel removeMe(){
        RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(Integer.parseInt(String.valueOf(0)), null);
        return rpm;
    }

    @RequestMapping(value="rawPattern/{db}/{id}")
    public RawPatternModel getSampleRpm(@PathVariable("db") String db, @PathVariable("id") int id){
        RawPatternModel rpm = (RawPatternModel) rawPatternDao.load(Integer.parseInt(String.valueOf(id)), db);
        return rpm;
    }

}