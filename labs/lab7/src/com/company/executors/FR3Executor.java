package com.company.executors;

import com.company.parsers.DateFactory;
import com.company.preparers.FR3Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR3;
import com.company.validators.FR3Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FR3Executor extends Executor{

    public void execute(){

        final FR3Preparer preparer = new FR3Preparer();
        final List<String> fields = Arrays.asList(
                "First Name",
                "Last Name",
                "Begin date",
                "End date",
                "Number of adults",
                "Number of children"

        );
        // allow users to change their reservations make sure they dont interere with others
        Map<String, String> field_values;
        FR3Validator validator = new FR3Validator();
        PreparedStatement preparedStatement;

        try {
            // step 1 - get existing reservation
            String RES_CODE = "";
            ResultSet resultSet = null;
            while (isMyResultSetEmpty(resultSet)) {

                System.out.println("Enter a reservation code");
                if (!(RES_CODE = new Scanner(System.in).next()).matches("\\d+") || Integer.parseInt(RES_CODE) > NEXT_RES_CODE) {
                    System.out.println("enter a valid res code");
                    continue;
                }
                preparedStatement = preparer.selectFR3(Integer.parseInt(RES_CODE));
                resultSet = preparedStatement.executeQuery();
            }

            Map<Integer, FR> res = getReservations(resultSet, new FR3());
            FR3 data = (FR3) res.get(0);
            validator.setFr3(data);


            // step 2 - read in values
            field_values = getFields(fields, validator);
            // step 2 - find it then update it according to next getFields


            Date availCheckOut = DateFactory.addDays(data.getDaysAvailableNextRes(), data.getCheckOut());
            Date wantedCheckOut = DateFactory.addDays(0, field_values.get(fields.get(FR3.END_STAY)));


            String num = field_values.get(fields.get(FR3.ADULTS));
            // if cant extend reservation
            if (availCheckOut.compareTo(wantedCheckOut) < -1) {
                System.out.println("Sorry we cant extend your reservation");
                return;
                // if cant add more ppl to room
            } else if (data.getAdults() + data.getKids() < Integer.parseInt(field_values.get(fields.get(FR3.ADULTS))) + Integer.parseInt(field_values.get(FR3.KIDS))) {
                System.out.println("Sorry we cant add more people to your reservation");
                return;
            }
            // else we can add more to the reservation
            PreparedStatement statement = preparer.updateFR3(field_values, fields, data.getBasePrice(), Integer.parseInt(RES_CODE));
            int i = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
