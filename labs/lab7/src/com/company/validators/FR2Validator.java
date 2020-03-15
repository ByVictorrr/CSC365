package com.company.validators;

import com.company.executors.Executor;
import com.company.parsers.DateFactory;
import com.company.reservations.FR2;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.company.executors.FR2Executor.*;

public class FR2Validator implements Validator{
    /* Provides validation for 1 */

    private Map<String, String> fieldsValues;
    private List<String> fields;

    public boolean valid(int index, String value)
        throws Exception
    {

        switch (index){
            case FIRST_NAME:
                if(!value.matches(NAME_FORMAT)){
                    System.out.println("Please enter a valid first name");
                    return false;
                }
                break;
            case LAST_NAME:
                if(!value.matches(NAME_FORMAT)){
                    System.out.println("Please enter a valid last name");
                    return false;
                }
                break;
            case ROOM_CODE:
                if(!Executor.ROOM_CODES.contains(value) && !value.equals("ANY")) {
                    System.out.println("No such room code found!");
                    return false;
                }
                break;
            case BED:
                if(!Executor.BED_TYPES.contains(value) && !value.equals("ANY")) {
                    System.out.println("No such bed type found!");
                    return false;
                }
                break;
            case BEGIN_STAY:
                if(!value.matches(DATE_FORMAT)) {
                    System.out.println("Please enter in a valid date format for begin stay");
                    return false;
                }
                break;
            case END_STAY:
                if(!value.matches(DATE_FORMAT)) {
                    System.out.println("Please enter in a valid date format for end stay");
                    return false;
                }
                Date end = DateFactory.StringToDate(value);
                Date start = DateFactory.StringToDate(fieldsValues.get(fields.get(BEGIN_STAY)));
                if (end.compareTo(start) < 0) {
                    System.out.println("Enter a date later than start date");
                    return false;
                }
                break;
            case ADULTS:
                if(!value.matches(NUMBER_FORMAT) || Integer.parseInt(value) < 0){
                    System.out.println("Please enter in a valid integer for adults");
                    return false;
                }else if(Executor.MAX_OCC < Integer.parseInt(value)){
                    System.out.println("Not enough room for all the adults");
                    return false;
                }
                break;
            case KIDS:
                if(!value.matches(NUMBER_FORMAT) || Integer.parseInt(value) <0){
                    System.out.println("Please enter in a valid integer for kids");
                    return false;
                }else if(Integer.parseInt(fieldsValues.get(fields.get(ADULTS)))+ Integer.parseInt(value) > Executor.MAX_OCC){
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
