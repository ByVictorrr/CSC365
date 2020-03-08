package com.company.validators;

import com.company.Executor;
import com.company.structures.FR2;

import java.util.List;
import java.util.Map;
import static com.company.structures.FR3.*;

public class FR3Validator implements Validator{
    private List<String> fields;
    private Map<String, String> fieldValues;
    public boolean valid(int index, String value) throws Exception {

        final String DATE_FORMAT =  new String("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
        switch (index){
            case RES_CODE:
                if(!value.matches("\\d+")){
                    System.out.println("Enter a valid reservation code");
                    return false;
                }else if(Integer.parseInt(value) > Executor.NEXT_RES_CODE-1){
                    System.out.println("No such reservation is found");
                    return false;
                }
                break;
            case BEGIN_STAY:
                if(!value.matches(DATE_FORMAT)){
                   System.out.println("Enter a valid date for check in");
                   return false;
                }
                break;
            case END_STAY:
                if(!value.matches(DATE_FORMAT)){
                    System.out.println("Enter a valid date for end stay");
                    return false;
                }
                break;
            case ADULTS:
                if(!value.matches("\\d+")){
                    System.out.println("Please enter in a valid integer for adults");
                    return false;
                }else if(Executor.MAX_ADULTS+Executor.MAX_KIDS < Integer.parseInt(value) ){
                    System.out.println("Not enough room for all the adults");
                    return false;
                }
                break;
            case KIDS:
                if(!value.matches("\\d+")){
                    System.out.println("Please enter in a valid integer for kids");
                    return false;
                }else if(Integer.parseInt(fieldValues.get(fields.get(FR2.ADULTS)))+ Integer.parseInt(value)
                        > Executor.MAX_KIDS+ Executor.MAX_ADULTS){
                    System.out.println("No available rooms to fit everyone");
                    return false;
                }
                break;

        }
        return true;
    }
    public void setFields(List<String> fields) {
        this.fields=fields;
    }
    public void setFieldsValues(Map<String, String> fieldsValues){
        this.fieldValues=fieldsValues;
    }
}
