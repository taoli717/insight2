package com.insight.generator.parser;

import com.insight.model.DailyStockModel;
import com.insight.model.StockModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by PC on 1/20/2015.
 */
@Service
public class MarketItOnDemandStockParser implements StockParser{
    public MarketItOnDemandStockParser(){}

    @Override
    public StockModel parseStockModelFromJSON(String input) throws Exception{
        StockModel sm = new StockModel();
        LinkedHashMap<Date, DailyStockModel> dailyStockMap = new LinkedHashMap<Date, DailyStockModel>();
        JSONParser parser = new JSONParser();
        JSONObject jsonStock = (JSONObject) parser.parse(input);
        JSONArray datesArray = (JSONArray) JSONValue.parse(jsonStock.get("Dates").toString());
        JSONArray array = (JSONArray) JSONValue.parse(jsonStock.get("Elements").toString());
        JSONObject price = (JSONObject) parser.parse(array.get(0).toString());
        JSONObject prices = (JSONObject) parser.parse(price.get("DataSeries").toString());
        JSONObject open = (JSONObject) parser.parse(prices.get("open").toString());
        JSONArray openArray = (JSONArray) JSONValue.parse(open.get("values").toString());

        JSONObject close = (JSONObject) parser.parse(prices.get("close").toString());
        JSONArray closeArray = (JSONArray) JSONValue.parse(close.get("values").toString());

        JSONObject high = (JSONObject) parser.parse(prices.get("high").toString());
        JSONArray highArray = (JSONArray) JSONValue.parse(high.get("values").toString());

        JSONObject low = (JSONObject) parser.parse(prices.get("low").toString());
        JSONArray lowArray = (JSONArray) JSONValue.parse(low.get("values").toString());
        JSONObject volume = (JSONObject) parser.parse(array.get(1).toString());
        JSONObject volumes = (JSONObject) parser.parse(volume.get("DataSeries").toString());
        JSONObject volumeValue = (JSONObject) parser.parse(volumes.get("volume").toString());
        JSONArray volumeArray = (JSONArray) JSONValue.parse(volumeValue.get("values").toString());
        for(int i=0; i<datesArray.size(); i++){
            DailyStockModel dsm = new DailyStockModel();
            dsm.setClose(closeArray.get(i).toString());
            dsm.setHigh(highArray.get(i).toString());
            dsm.setLow(lowArray.get(i).toString());
            dsm.setOpen(openArray.get(i).toString());
            dsm.setVolume(volumeArray.get(i).toString());
            String dataString = datesArray.get(i).toString();
            String sd = dataString.substring(0,dataString.indexOf("T"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date =  df.parse(sd);
            dsm.setDate(date);
            dailyStockMap.put(date, dsm);
        }
        sm.setDailyStocks(dailyStockMap);
        return sm;
    }
}
