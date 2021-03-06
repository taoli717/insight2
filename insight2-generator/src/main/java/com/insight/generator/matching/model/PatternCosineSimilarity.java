package com.insight.generator.matching.model;

import com.insight.generator.matching.service.PatternCosineSimilarityServiceImpl;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by tli on 2/24/2015.
 */
@Document(collection = "pattern_cosine_similarity")
public class PatternCosineSimilarity {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    public static final String DELIMITER = "*#*";

    @Id
    public String index;

    public String stockName;

    public long seq;

    public Date buyingDate;

    public Date sellingDate;

    public Double getAverageNormPriceAbsoluteTotal() {
        return averageNormPriceAbsoluteTotal;
    }

    public void setAverageNormPriceAbsoluteTotal(Double averageNormPriceAbsoluteTotal) {
        this.averageNormPriceAbsoluteTotal = averageNormPriceAbsoluteTotal;
    }

    public Double averageNormPriceAbsoluteTotal;

    public Map<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> cosineValues;

    public String getIndex() {
        return getStockName() + DELIMITER + sdf.format(buyingDate);
    }

    public void setIndex() {
        this.index = getStockName() + DELIMITER + sdf.format(buyingDate);
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(Date buyingDate) {
        this.buyingDate = buyingDate;
    }

    public Date getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(Date sellingDate) {
        this.sellingDate = sellingDate;
    }

    public Map<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> getCosineValues() {
        return cosineValues;
    }

    public void setCosineValues(Map<String, Map<PatternCosineSimilarityServiceImpl.PatternType, Map<Integer, Double>>> cosineValues) {
        this.cosineValues = cosineValues;
    }
}
