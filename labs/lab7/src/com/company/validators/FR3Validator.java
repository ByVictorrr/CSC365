package com.company.validators;

import com.company.reservations.FR2;
import com.company.reservations.FR3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static com.company.reservations.FR3.*;

public class FR3Validator implements Validator{
    private List<String> fields;
    private Map<String, String> fieldValues;
    private FR3 fr3;

    public boolean valid(int index, String value) throws Exception {
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
            case BEGIN_STAY:
                if(!value.matches(DATE_FORMAT) && !value.equals("no")){
                   System.out.println("Enter a valid date for check in");
                   return false;
                }
                break;
            case END_STAY:
                if(!value.matches(DATE_FORMAT) && !value.equals("no")){
                    System.out.println("Enter a valid date for end stay");
                    return false;
                }
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(fieldValues.get(fields.get(FR3.BEGIN_STAY)));
                if (end.compareTo(start) < 0) {
                    System.out.println("Enter a date later than start date");
                    return false;
                }
                break;
            case ADULTS:
                if(!value.matches(NUMBER_FORMAT) && !value.equals("no")){
                    System.out.println("Please enter in a valid integer for adults");
                    return false;
                }else if(fr3.getAdults() < Integer.parseInt(value)){
                    System.out.println("Not enough room for all the adults");
                    return false;
                }
                break;
            case KIDS:
                if(!value.matches(NUMBER_FORMAT) && !value.equals("no")){
                    System.out.println("Please enter in a valid integer for kids");
                    return false;
                }else if(fr3.getKids()+fr3.getAdults() < Integer.parseInt(fieldValues.get(fields.get(ADULTS))) + Integer.parseInt(value)){
                    System.out.println("No available rooms to fit everyone");
                    return false;
                }
                break;

        }
        return true;
    }

    public void setFr3(FR3 fr3) {
        this.fr3 = fr3;
    }

    public void setFields(List<String> fields) {
        this.fields=fields;
    }
    public void setFieldsValues(Map<String, String> fieldsValues){
        this.fieldValues=fieldsValues;
    }
}
