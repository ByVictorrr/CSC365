package com.company.validators;


import java.util.List;
import java.util.Map;

public interface Validator {
    boolean valid(int index, String value) throws Exception;
    void setFields(List<String> fields);
    void setFieldsValues(Map<String, String> fieldsValues);
}

