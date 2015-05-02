package com.insight.generator.retriever.markItOnDemond;

import com.insight.generator.retriever.PriceRetriever;
import com.insight.model.StockModel;
import com.insight.generator.parser.StockParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC on 10/26/2014.
 */
public class MarkItOnDemondPriceRetriever implements PriceRetriever {

    @Autowired
    private StockParser stockParser;
    private final String USER_AGENT = "Mozilla/5.0";
    private static final Logger logger = Logger.getLogger(MarkItOnDemondPriceRetriever.class);

    // HTTP GET request
    @Override
    public StockModel sendGet(String stockName, String days, long i){
        DataOutputStream wr = null;
        BufferedReader in = null;
        try{
            StockModel sm = new StockModel();
            String url = "http://dev.markitondemand.com/Api/v2/InteractiveChart/jsonp";
            //String url = "http://dev.markitondemand.com/Api/v2/Lookup";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setDoOutput(true);
            String params = MarketItOnDemandQueryGenerator.getQuery(stockName, days);
            logger.info("Getting " + stockName);
            con.setRequestMethod("GET");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
            con.setUseCaches(false);

            wr = new DataOutputStream(con.getOutputStream ());
            wr.writeBytes(params);
            wr.flush();

            int responseCode = con.getResponseCode();
            logger.info("Response Code : " + responseCode);

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //logger.info(response.toString());
            Pattern pattern = Pattern.compile("[/(](.*?)[/)]");
            String input = response.toString().replace("(function () { })", "");
            logger.info(input);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find())
            {
                String stock = matcher.group(1);
                sm = stockParser.parseStockModelFromJSON(stock);
                sm.setStockName(stockName);
            }
            sm.setSeq(i);
            return sm;
        }catch(Exception e){
            try{
                if(wr != null){
                    wr.close();
                }
                if(in != null){
                    in.close();
                }
                return null;
            }catch (Exception e2){
                logger.error(e2);
                return null;
            }
        }
    }
}
