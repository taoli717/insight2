package generator.parser;

import generator.model.DailyStockModel;
import generator.model.StockModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by PC on 10/28/2014.
 */
public interface StockParser {
    StockModel parseStockModelFromJSON(String input) throws Exception;
}
