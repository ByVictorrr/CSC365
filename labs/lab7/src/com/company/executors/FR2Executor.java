package com.company.executors;

import com.company.parsers.DateFactory;
import com.company.preparers.FR2Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR2;
import com.company.validators.FR2Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static com.company.reservations.FR2.*;

public class FR2Executor extends Executor{

   /*========================FOR TESTING ====================================*/
    private final static List<String> TESTS;
    private static int index = 0;
    static {
        List<String> _TESTS = new ArrayList<>();
        String s = null;
        try {
            Process process = Runtime.getRuntime().exec("ls /home/victord/CSC365/labs/lab7/tests/FR2_tests/*");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            while ((s = reader.readLine()) != null) {
                System.out.println("Script output: " + s);
                _TESTS.add(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TESTS= _TESTS;
    }
    /*========================================================================*/


    public void execute() {

        final FR2Preparer preparer = new FR2Preparer();
        final List<String> fields = Arrays.asList(
                "First Name",
                "Last Name",
                "Room Code",
                "Bed Type",
                "Begin Date Of Stay",
                "End Date Of Stay",
                "Number Of Adults",
                "Number Of Children"
        );

        PreparedStatement preparedStatement;
        HashMap<String, String> field_values;
        ResultSet rs;
        try {
            // step 0 - if user typed in "c" then return to main menu
            if ((field_values = getFields(fields, new FR2Validator(), "FR2_tests/" + TESTS.get(index))) == null) {
                return;
            }
            // step 1 - set the interval of which the user wants to stay
            FR2.setUserTimeStay(DateFactory.daysBetween(field_values.get(fields.get(END_STAY)), field_values.get(fields.get(BEGIN_STAY))));
            // step 2 - build and then return the query of rooms
            preparedStatement = preparer.selectFR2(
                    field_values.get(fields.get(BED)),
                    field_values.get(fields.get(ROOM_CODE)),
                    field_values,
                    fields
            );

            // Case 1.1 - if no matches are found then output 5 suggested possibilities for different rooms or dates
            if (isMyResultSetEmpty((rs=preparedStatement.executeQuery()))) {
                System.out.println("Sorry no rooms found that meet those requirements." +
                        "\nHere are some similar rooms: "
                );
                // this trys to give room avable
                preparedStatement = preparer.selectFR2(
                        "ANY",
                        "ANY",
                        field_values,
                        fields
                );
                if(isMyResultSetEmpty((rs=preparedStatement.executeQuery()))){
                    System.out.println("Sorry couldn't find any available with that time frame and number of people");
                    return;
                }
            }
            System.out.println("Choose number below to book (c - cancel and go back to the main menu)");
            Map<Integer, FR> res = getReservations(rs, new FR2());
            // step 2 - option to input booking number(ROOM CODE) of one of those rooms
            System.out.println("Please enter the booking number to order");
            res.forEach((k, v) -> System.out.println(k + " " + ((FR2)v).toString()));
            // show the returned list in of numbered options

            String option, confirm;
            while (!(option = new Scanner(System.in).next()).matches("\\d+") ||
                    !res.keySet().contains(Integer.parseInt(option))) {
                // go back to main menu
                if (option.equals("c")) {
                    return;
                }
                System.out.println("Please enter a valid number");
            }
            FR2 pickedRes = (FR2) res.get(Integer.parseInt(option));
            /* for formatting the confirmed reservation*/
            pickedRes.setFirstName(field_values.get(fields.get(FR2.FIRST_NAME)));
            pickedRes.setLastName(field_values.get(fields.get(FR2.LAST_NAME)));
            pickedRes.setKids(Integer.parseInt(field_values.get(fields.get(KIDS))));
            pickedRes.setAdults(Integer.parseInt(field_values.get(fields.get(ADULTS))));

            System.out.println(pickedRes.PickedToString());

            System.out.println("Y/N to to confirm reservation");
            if (!((confirm = new Scanner(System.in).next()).matches("Y")) && !(confirm.matches("y"))) {
                return;
            }
            // Insert into db
            preparedStatement = preparer.insertFR2(pickedRes, NEXT_RES_CODE);
            int i = preparedStatement.executeUpdate();
            NEXT_RES_CODE++;
            index++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
