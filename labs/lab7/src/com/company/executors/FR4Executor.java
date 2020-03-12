package com.company.executors;


import com.company.preparers.FR4Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR4;
import com.company.validators.FR4Validator;

import java.security.interfaces.RSAKey;
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
            while ( isMyResultSetEmpty(resultSet)) {
                System.out.println("Enter a reservation code(c - back to main menu)");
                RES_CODE = new Scanner(System.in).next();
                if(RES_CODE.equals("c")){
                    return;
                }else if (!validator.valid(0, RES_CODE)) {
                    continue;
                }
                preparedStatement = preparer.selectFR4(Integer.parseInt(RES_CODE));
                if(isMyResultSetEmpty(resultSet = preparedStatement.executeQuery())){
                    System.out.println("Found no reservations with that code.");
                }
            }

            Map<Integer, FR> res = getReservations(resultSet, new FR4());
            FR4 data = (FR4)res.get(0);

            System.out.println("Would you like to cancel this reservation ? (Y/N)");
            System.out.println(data.toString());
            if (!(confirm = new Scanner(System.in).next()).equals("y") && !confirm.equals("Y")) {
                return;
            }

            preparedStatement = preparer.updateFR4(Integer.parseInt(RES_CODE));
            int i = preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
