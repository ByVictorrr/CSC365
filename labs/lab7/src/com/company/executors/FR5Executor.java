package com.company.executors;

import com.company.preparers.FR5Preparer;
import com.company.validators.FR5Validator;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FR5Executor extends Executor{
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int BEGIN_DAY = 2;
    public static final int END_DAY = 3;
    public static final int ROOM_CODE = 4;
    public static final int RES_CODE = 5;

    private static final List<String> fields = Arrays.asList(
            "First Name",
            "Last Name",
            "Begin date",
            "End date",
            "Room code",
            "Reservation Code"

    );

    public void execute(){

        Map<String, String> field_values;
        FR5Validator validator = new FR5Validator();
        PreparedStatement statement;
        try {
            System.out.println("Type 'ANY' to search for any value of that field");
            /** Step 1 - get input for searching for reservations of above fields */
            if((field_values = getFields(fields, validator))== null){
                return;
            }
            statement= FR5Preparer.select(field_values, fields);
            ResultSet resultSet = statement.executeQuery();
            /** Step 2 - if the reservation result is empty then return and tell user **/
            if(isMyResultSetEmpty(resultSet)){
                System.out.println("Sorry couldn't find any records matching those inputs");
                return;
            }
            printResultSet(resultSet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
