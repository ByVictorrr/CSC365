package com.company.executors;


import com.company.preparers.FR4Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR4;
import com.company.validators.FR4Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Scanner;

public class FR4Executor extends Executor{

    public void execute() {

        final FR4Preparer preparer = new FR4Preparer();
        PreparedStatement preparedStatement;

        String RES_CODE = "", confirm;
        ResultSet resultSet = null;
        FR4Validator validator = new FR4Validator();
        try{
            while (isMyResultSetEmpty(resultSet)) {

                System.out.println("Enter a reservation code");
                if (!(RES_CODE = new Scanner(System.in).next()).matches("\\d+") || Integer.parseInt(RES_CODE) > NEXT_RES_CODE) {
                    System.out.println("enter a valid res code");
                    continue;
                }
                preparedStatement = preparer.selectFR4(Integer.parseInt(RES_CODE));
                resultSet = preparedStatement.executeQuery();
            }

            Map<Integer, FR> res = getReservations(resultSet, new FR4());
            FR4 data = (FR4)res.get(0);

            System.out.println("Hey " + data.getFirstName() + " would you like to cancel you reservation ? (Y/N)");
            if (!(confirm = new Scanner(System.in).next()).equals("y") && !confirm.equals("Y")) {
                // go back, we dont want to cancel res code
                return;
            }

            preparedStatement = preparer.updateFR4(Integer.parseInt(RES_CODE));
            int i = preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
