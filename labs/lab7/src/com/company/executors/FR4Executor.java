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

        PreparedStatement preparedStatement;

        String RES_CODE = "", confirm;
        ResultSet resultSet = null;
        FR4Validator validator = new FR4Validator();
        try{
            /** Step 1 - get user entered reservation code */
            while (isMyResultSetEmpty(resultSet)) {
                System.out.println("Enter a reservation code(c - back to main menu)");
                if((RES_CODE=new Scanner(System.in).next()).equals("c")){
                    return;
                }else if (!validator.valid(0, RES_CODE)) {
                    continue;
                }
                preparedStatement = FR4Preparer.select(Integer.parseInt(RES_CODE));
                if(isMyResultSetEmpty((resultSet = preparedStatement.executeQuery()))){
                    System.out.println("Found no reservations with that code.");
                }
            }
            /** Step 2 - convert the result to a FR4 object **/
            Map<Integer, FR> res = getReservations(resultSet, new FR4());
            FR4 data = (FR4)res.get(0);

            System.out.println("Would you like to cancel this reservation ? (Y/N)");
            System.out.println(data.toString());
            if (!(confirm = new Scanner(System.in).next()).equals("y") && !confirm.equals("Y")) {
                return;
            }

            /** Step 3 - delete the reservation **/
            preparedStatement = FR4Preparer.update(Integer.parseInt(RES_CODE));
            int i = preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
