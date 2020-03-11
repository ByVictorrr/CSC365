package com.company.validators;

import java.util.List;
import java.util.Map;

public class FR4Validator implements Validator{
    public boolean valid(int index, String value) throws Exception {
        return true;
    }
    public void setFields(List<String> fields) {

    }
    public void setFieldsValues(Map<String, String> fieldsValues) {
        return;
    }
}
