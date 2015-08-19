package com.insight.generator.retriever.markItOnDemond;

/**
 * Created by tli on 11/8/2014.
 */
public class MarketItOnDemandQueryGenerator {
    public static String getQuery(String stockName, String days){
        return "parameters={\"Normalized\":false," +
                "\"NumberOfDays\":" + days + "," +
                "\"DataPeriod\":\"Day\"," +
                "\"Elements\":[" +
                "{\"Symbol\":\"" + stockName + "\",\"Type\":\"price\",\"Params\":[\"ohlc\"]}," +
                "{\"Symbol\":\"" + stockName + "\",\"Type\":\"volume\"}" +
                "]}";
    }
}
