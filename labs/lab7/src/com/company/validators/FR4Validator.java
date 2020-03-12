package com.company.validators;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.company.executors.Executor.NEXT_RES_CODE;


public class FR4Validator implements Validator{
    public boolean valid(int index, String value) throws Exception {
        if(!value.matches(NUMBER_FORMAT) || Integer.parseInt(value) > NEXT_RES_CODE) {
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
