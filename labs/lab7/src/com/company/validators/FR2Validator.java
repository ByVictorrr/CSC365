package com.company.validators;

import com.company.executors.Executor;
import com.company.parsers.DateFactory;
import com.company.reservations.FR2;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FR2Validator implements Validator{
    /* Provides validation for FR2 */

    private Map<String, String> fieldsValues;
    private List<String> fields;

    public boolean valid(int index, String value)
        throws Exception
    {

        switch (index){
            case FR2.FIRST_NAME:
                if(!value.matches(NAME_FORMAT)){
                    System.out.println("Please enter a valid first name");
                    return false;
                }
                break;
            case FR2.LAST_NAME:
                if(!value.matches(NAME_FORMAT)){
                    System.out.println("Please enter a valid last name");
                    return false;
                }
                break;
            case FR2.ROOM_CODE:
                if(!Executor.ROOM_CODES.contains(value) && !value.equals("ANY")) {
                    System.out.println("No such room code found!");
                    return false;
                }
                break;
            case FR2.BED:
                if(!Executor.BED_TYPES.contains(value) && !value.equals("ANY")) {
                    System.out.println("No such bed type found!");
                    return false;
                }
                break;
            case FR2.BEGIN_STAY:
                if(!value.matches(DATE_FORMAT)) {
                    System.out.println("Please enter in a valid date format for begin stay");
                    return false;
                }
                break;
            case FR2.END_STAY:
                if(!value.matches(DATE_FORMAT)) {
                    System.out.println("Please enter in a valid date format for end stay");
                    return false;
                }
                Date end = DateFactory.StringToDate(value);
                Date start = DateFactory.StringToDate(fieldsValues.get(fields.get(FR2.BEGIN_STAY)));
                if (end.compareTo(start) < 0) {
                    System.out.println("Enter a date later than start date");
                    return false;
                }
                break;
            case FR2.ADULTS:
                if(!value.matches(NUMBER_FORMAT) || Integer.parseInt(value) < 0){
                    System.out.println("Please enter in a valid integer for adults");
                    return false;
                }else if(Executor.MAX_ADULTS+Executor.MAX_KIDS < Integer.parseInt(value)){
                    System.out.println("Not enough room for all the adults");
                    return false;
                }
                break;
            case FR2.KIDS:
                if(!value.matches(NUMBER_FORMAT) || Integer.parseInt(value) <0){
                    System.out.println("Please enter in a valid integer for kids");
                    return false;
                }else if(Integer.parseInt(fieldsValues.get(fields.get(FR2.ADULTS)))+ Integer.parseInt(value)
                        > Executor.MAX_KIDS+ Executor.MAX_ADULTS){
                    System.out.println("No available rooms to fit everyone");
                    return false;
                }
                break;
        }

        return true;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setFieldsValues(Map<String, String> fieldsValues) {
        this.fieldsValues = fieldsValues;
    }
}
