package com.insight.validation;

import java.util.Date;

/**
 * Created by tli on 4/12/2015.
 */
public interface Validation {
    void validateSignature(Date date, String signature) throws Exception;
    void validateSignature() throws Exception;
    void validatePatternMatrix(String patternMatixIndex);
    void validatePatternMatrix();
}
