package com.company.validators;

import com.company.parsers.DateFactory;
import com.company.reservations.FR2;
import com.company.reservations.FR3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static com.company.executors.FR3Executor.*;

public class FR3Validator implements Validator{
    private List<String> fields;
    private Map<String, String> fieldValues;
    private FR3 fr3;

    public boolean valid(int index, String value) throws Exception {
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
            case BEGIN_STAY:
                if(!value.matches(DATE_FORMAT) && !value.equals("no")){
                   System.out.println("Enter a valid date for check in");
                   return false;
                }else if(!value.equals("no")) {
                    Date availCheckIn = DateFactory.addDays(-fr3.getDaysOpenPrev(), fr3.getCheckIn());
                    Date wantedCheckIn = DateFactory.StringToDate(value);
                    // case where wanted checkIn is less than avail check in
                    if (wantedCheckIn.compareTo(availCheckIn) < 0 ) {
                        System.out.println("Sorry we cant push your checkIn earlier");
                        return false;
                    }
                }
                break;
            case END_STAY:
                if(!value.matches(DATE_FORMAT) && !value.equals("no")){
                    System.out.println("Enter a valid date for end stay");
                    return false;
                }else if(!value.equals("no")) {
                    Date wantedCheckOut = DateFactory.StringToDate(value);
                    Date availCheckOut = DateFactory.addDays(fr3.getDaysOpenNext(), fr3.getCheckOut());
                    if (wantedCheckOut.compareTo(availCheckOut) > 0) {
                        System.out.println("Sorry we cant push your checkOut later");
                        return false;
                    }
                }
                break;
            case ADULTS:
                if(!value.matches(NUMBER_FORMAT) && !value.equals("no")){
                    System.out.println("Please enter in a valid integer for adults");
                    return false;
                }else if(!value.equals("no") && fr3.getMaxOcc() < Integer.parseInt(value)){
                    System.out.println("Not enough room for all the adults");
                    return false;
                }
                break;
            case KIDS:
                if(!value.matches(NUMBER_FORMAT) && !value.equals("no")){
                    System.out.println("Please enter in a valid integer for kids");
                    return false;
                }else if(!value.equals("no") && fr3.getMaxOcc() < Integer.parseInt(fieldValues.get(fields.get(ADULTS))) + Integer.parseInt(value)){
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
