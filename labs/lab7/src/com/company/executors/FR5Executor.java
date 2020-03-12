package com.company.executors;

import com.company.preparers.FR5Preparer;
import com.company.validators.FR5Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FR5Executor extends Executor{
    public void execute(){
        final FR5Preparer preparer = new FR5Preparer();
        final List<String> fields = Arrays.asList(
                "First Name",
                "Last Name",
                "Begin date",
                "End date",
                "Room code",
                "Reservation Code"

        );
        Map<String, String> field_values;
        FR5Validator validator = new FR5Validator();
        PreparedStatement statement;
        try {
            if((field_values = getFields(fields, validator))== null){
                return;
            }
            statement= preparer.selectFR5(field_values, fields);
            ResultSet resultSet = statement.executeQuery();
            printResultSet(resultSet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
