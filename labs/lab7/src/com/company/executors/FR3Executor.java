package com.company.executors;

import com.company.parsers.DateFactory;
import com.company.preparers.FR3Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR3;
import com.company.validators.FR3Validator;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FR3Executor extends Executor {
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int BEGIN_STAY = 2;
    public static final int END_STAY = 3;
    public static final int ADULTS = 4;
    public static final int KIDS = 5;

    private final List<String> fields = Arrays.asList(
            "First Name",
            "Last Name",
            "Begin date",
            "End date",
            "Number of adults",
            "Number of children"
    );

    public void execute() {

        Map<String, String> field_values;
        FR3Validator validator = new FR3Validator();
        PreparedStatement preparedStatement;
        String RES_CODE = null;
        ResultSet resultSet = null;
        double rate;

        try {
            /** Step 1 - get existing reservation code from the user**/
            while (isMyResultSetEmpty(resultSet)) {
                System.out.println("Enter a reservation code (c - to cancel and return to the main menu)");
                if ((RES_CODE = new Scanner(System.in).next()).equals("c")){
                    return;
                }else if(!RES_CODE.matches("\\d+")){
                    System.out.println("Please Enter a valid res code");
                    continue;
                }else if(!RES_CODES.contains(Integer.parseInt(RES_CODE))) {
                    System.out.println("No reservation code found");
                    continue;
                }
                preparedStatement = FR3Preparer.select(Integer.parseInt(RES_CODE));
                resultSet = preparedStatement.executeQuery();
            }

            /** Step 2 - store the selected reservation code into an object **/
            Map<Integer, FR> res = getReservations(resultSet, new FR3());
            FR3 data = (FR3) res.get(0);
            validator.setFr3(data);

            /** Step 3 - get user data inputted on what they what they want to change **/
            System.out.println("If you don't want change in one of the fields type: no");
            if((field_values = getFields(fields, validator))==null){
                return;
            }
            rate = getRate(field_values, fields, data);

            /** Step 4 - if user enters "no", then change data field to field in data **/
            noChangeMap(field_values, fields, data);


            /** Step 5 - update that record found with the corresponding information  **/
            PreparedStatement statement = FR3Preparer.update(field_values, fields, rate, Integer.parseInt(RES_CODE));
            int i = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Looks through the map for values of no and sets it to the old contents
     * @param map
     * @param fields
     * @param data
     */
    private void noChangeMap(Map<String, String> map, final List<String> fields, FR3 data) {
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value.equals("no")) {
                if (key.equals(fields.get(FIRST_NAME))) {
                    value = data.getFirstName();
                } else if (key.equals(fields.get(LAST_NAME))) {
                    value = data.getLastName();
                } else if (key.equals(fields.get(BEGIN_STAY))) {
                    value = DateFactory.DateToString(data.getCheckIn());
                } else if (key.equals(fields.get(END_STAY))) {
                    value = DateFactory.DateToString(data.getCheckOut());
                } else if (key.equals(fields.get(ADULTS))) {
                    value = Integer.toString(data.getAdults());
                } else {
                    value = Integer.toString(data.getKids());
                }
                map.put(key, value);
            }
        }
    }
    private Double getRate(Map<String, String> field_values, List<String> fields, FR3 data)
            throws Exception
    {
        /** If the base price didn't change**/
        Double rate;
        if(field_values.get(fields.get(BEGIN_STAY)).equals("no") && field_values.get(fields.get(END_STAY)).equals("no")){
            rate=data.getRate();
        }else if(field_values.get(fields.get(END_STAY)).equals("no")){
            rate=FR.totalRateOfStay(
                    DateFactory.StringToDate(field_values.get(fields.get(BEGIN_STAY)))
                    ,data.getCheckOut()
                    ,data.getBasePrice());
        }else if(field_values.get(fields.get(BEGIN_STAY)).equals("no")){
            // don't
            rate=FR.totalRateOfStay(
                    data.getCheckIn()
                    ,DateFactory.StringToDate(field_values.get(fields.get(END_STAY)))
                    ,data.getBasePrice());
        }else{
            // not no and not no
            rate=FR.totalRateOfStay(
                     DateFactory.StringToDate(field_values.get(fields.get(BEGIN_STAY)))
                    ,DateFactory.StringToDate(field_values.get(fields.get(END_STAY)))
                    ,data.getBasePrice());
        }

        return rate;
    }
}
