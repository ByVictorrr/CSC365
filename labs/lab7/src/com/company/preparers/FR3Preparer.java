package com.company.preparers;

import com.company.ConnectionAdapter;
import com.company.parsers.DateFactory;
import com.company.reservations.FR;
import com.company.reservations.FR3;
import com.company.utilities.Pair;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import static com.company.reservations.FR3.*;

public class FR3Preparer extends Preparer {
    public PreparedStatement selectFR3(int RES_CODE)
            throws  Exception
    {

        final String FR3_FOLDER = "FR3";
        String query;
        query = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/"+FR3_FOLDER + "/"+"FR3_RES_CODE.sql")));
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(query);
        statement.setInt(1, RES_CODE);
        return statement;
    }

    public PreparedStatement updateFR3(Map<String, String> fieldValues, List<String> fields, double basePrice, int RES_CODE)
            throws Exception
    {
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(
                "UPDATE lab7_reservations " +
                        "SET FirstName = ?," +
                        "LastName = ?," +
                        "CheckIn = ?,"+
                        "Checkout = ?,"+
                        "Kids = ?,"+
                        "Adults = ?,"+
                        "Rate = ? " +
                        "WHERE CODE = ?;"
        );
        String CheckIn = fieldValues.get(fields.get(BEGIN_STAY));
        String CheckOut = fieldValues.get(fields.get(END_STAY));

        double rate = FR.totalRateOfStay(CheckIn, CheckOut, basePrice);

        statement.setString(1, fieldValues.get(fields.get(FIRST_NAME)));
        statement.setString(2, fieldValues.get(fields.get(LAST_NAME)));
        statement.setString(3, CheckIn);
        statement.setString(4, CheckOut);
        statement.setInt(5, Integer.parseInt(fieldValues.get(fields.get(KIDS))));
        statement.setInt(6, Integer.parseInt(fieldValues.get(fields.get(ADULTS))));
        statement.setDouble(7, (basePrice*week_weekend_days.getValue() + basePrice*1.1*week_weekend_days.getKey())*1.18);
        statement.setInt(8, RES_CODE);
        return statement;

    }
}
