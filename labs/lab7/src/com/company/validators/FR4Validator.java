package com.company.validators;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.company.executors.Executor.NEXT_RES_CODE;
import static com.company.executors.Executor.RES_CODES;


public class FR4Validator implements Validator{
    public boolean valid(int index, String value) throws Exception {
        if(!value.matches(NUMBER_FORMAT) && !RES_CODES.contains(Integer.parseInt(value))) {
            System.out.println("Enter a valid reservation code");
            return false;
        }
        return true;
    }

    @Override
    public void setFieldsValues(Map<String, String> fieldsValues) {
        ;
    }

    @Override
    public void setFields(List<String> fields) {
        ;
    }
}
