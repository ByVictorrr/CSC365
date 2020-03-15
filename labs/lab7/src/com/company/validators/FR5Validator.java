package com.company.validators;


import com.company.executors.Executor;

import java.util.List;
import java.util.Map;

import static com.company.executors.FR5Executor.*;
public class FR5Validator implements Validator{
    private List<String> fields;
    private final static String EMPTY="ANY";

    public boolean valid(int index, String value) throws Exception {
        switch (index) {
            case BEGIN_DAY:
                if (!value.matches(DATE_FORMAT) && !value.equals(EMPTY)) {
                    System.out.println("Enter a valid date for start date");
                    return false;
                }
                break;
            case END_DAY:
                if (!value.matches(DATE_FORMAT) && !value.equals(EMPTY)) {
                    System.out.println("Enter a valid date for end date");
                    return false;
                }
                break;
            case FIRST_NAME:
                if (!value.matches(NAME_FORMAT) && !value.contains("%") && !value.contains("*") && !value.equals(EMPTY)) {
                    System.out.println("Please enter a valid first name");
                    return false;
                }
                break;
            case LAST_NAME:
                if (!value.matches(NAME_FORMAT) && !value.contains("%") && !value.contains("*") && !value.equals(EMPTY)) {
                    System.out.println("Please enter a valid last name");
                    return false;
                }
                break;
            case ROOM_CODE:
                if (!Executor.ROOM_CODES.contains(value) && !value.equals(EMPTY) && !value.contains("%") && !value.contains("*")) {
                    System.out.println("No such room code found/Invalid input!");
                    return false;
                }
                break;
            case RES_CODE:
                if(!value.matches(NUMBER_FORMAT) && !value.equals(EMPTY) && !value.contains("%") && !value.contains("*")){
                    System.out.println("Please enter valid valid reservation code (can us wildcards %/*)");
                    return false;
                }else if (value.matches(NUMBER_FORMAT) && !RES_CODES.contains(Integer.parseInt(value))) {
                    System.out.println("Please enter a valid reservation code");
                    return false;
                }
                break;
        }
        return true;
    }

    @Override
    public void setFields(List<String> fields) {
        ;
    }

    @Override
    public void setFieldsValues(Map<String, String> fieldsValues) {
        ;
    }
}
