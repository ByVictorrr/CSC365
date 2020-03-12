package com.company.validators;


import java.util.List;
import java.util.Map;

public interface Validator {

    String DATE_FORMAT =  new String("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    String NUMBER_FORMAT = new String("\\d+");
    String NAME_FORMAT = new String("^[a-zA-Z]*$");
    boolean valid(int index, String value) throws Exception;
    void setFields(List<String> fields);
    void setFieldsValues(Map<String, String> fieldsValues);
}

