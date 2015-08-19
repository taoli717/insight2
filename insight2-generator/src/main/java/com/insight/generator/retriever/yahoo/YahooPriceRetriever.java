package com.insight.generator.retriever.yahoo;

import com.insight.generator.retriever.PriceRetriever;
import com.insight.model.DailyStockModel;
import com.insight.model.StockModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tli on 4/27/2015.
 */
@Service
@Qualifier("yahooPriceRetriever")
public class YahooPriceRetriever implements PriceRetriever{


    //CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"Date", "Open", "High", "Low", "Close", "Volume", "Adj Close"};

    //Student attributes
    private static final String DATE = "Date";
    private static final String OPEN = "Open";
    private static final String HIGH = "High";
    private static final String LOW = "Low";
    private static final String CLOSE = "Close";
    private static final String VOLUME = "Volume";
    private static final String ADJ_CLOSE = "Adj Close";

    private static final Logger logger = Logger.getLogger(YahooPriceRetriever.class);

    @Override
    public StockModel sendGet(String stockName, String days, long i) throws Exception {
        try {
            return convert2CSVAndParse(stockName, i);
        } catch (MalformedURLException mue) {
            logger.error(mue.getMessage(), mue);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } finally {
        }
        return null;
    }

    public static StockModel convert2CSVAndParse(String stockName, long i) throws MalformedURLException {

        URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s="+stockName+"&c=1962");
        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {

            StockModel sm = new StockModel();
            sm.setStockName(stockName);
            sm.setSeq(i);
            LinkedHashMap<Date, DailyStockModel> dailyStockMap = new LinkedHashMap<Date, DailyStockModel>();
            //Create a new list of student to be filled by CSV file data
            List students = new ArrayList();

            //initialize CSVParser object
            csvFileParser = CSVParser.parse(url, Charset.defaultCharset(), csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int j = 1; j < csvRecords.size(); j++) {
                DailyStockModel dsm = new DailyStockModel();
                CSVRecord record = (CSVRecord) csvRecords.get(j);
                String dateString = record.get(DATE);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date =  df.parse(dateString);
                dsm.setDate(date);
                dsm.setClose(record.get(CLOSE));
                dsm.setHigh(record.get(HIGH));
                dsm.setLow(record.get(LOW));
                dsm.setOpen(record.get(OPEN));
                dsm.setVolume(record.get(VOLUME));
                if(Integer.valueOf(record.get(VOLUME))<=0){
                    logger.warn(record.get(VOLUME) + " " + stockName + " " + dsm.getDate());
                    continue;
                }
                dailyStockMap.put(date, dsm);
            }
            sm.setDailyStocks(dailyStockMap);
            return sm;
        }
        catch (Exception e) {
            logger.error("Error in CsvFileReader !!!");
            logger.error(e.getLocalizedMessage());
        } finally {
            try {
                if(csvFileParser!=null)
                    csvFileParser.close();
            } catch (IOException e) {
                logger.error("Error while closing fileReader/csvFileParser !!!");
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
}