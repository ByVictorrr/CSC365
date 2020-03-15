package com.company.executors;

import com.company.parsers.DateFactory;
import com.company.preparers.FR2Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR2;
import com.company.validators.FR2Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static com.company.reservations.FR2.*;

public class FR2Executor extends Executor{

    public static final int LAST_NAME=0;
    public static final int FIRST_NAME=1;
    public static final int ROOM_CODE=2;
    public static final int BED=3;
    public static final int BEGIN_STAY=4;
    public static final int END_STAY=5;
    public static final int ADULTS=6;
    public static final int KIDS=7;

    private final List<String> fields = Arrays.asList(
            "First Name",
            "Last Name",
            "Room Code",
            "Bed Type",
            "Begin Date Of Stay",
            "End Date Of Stay",
            "Number Of Adults",
            "Number Of Children"
    );

    public void execute() {

        PreparedStatement preparedStatement;
        HashMap<String, String> field_values;
        ResultSet rs;
        String option, confirm;
        FR2Validator validator;
        try {
            System.out.println("optional 'ANY' for Room code, bed type");
            /** Step 1 - get the inputted fields given in the fields and store in field_values **/
            if ((field_values = getFields(fields, (validator=new FR2Validator()))) == null) {
                return;
            }

            /** Step 2 - prepare the query using the inputted fields **/
            preparedStatement = FR2Preparer.select(
                    field_values.get(fields.get(BED)),
                    field_values.get(fields.get(ROOM_CODE)),
                    field_values,
                    fields
            );
            /** What if the result of the query is empty??? */
            if (isMyResultSetEmpty((rs=preparedStatement.executeQuery()))) {
                System.out.println("Sorry no rooms found that meet those requirements." +
                        "\nHere are some similar rooms: "
                );
                /** Look for reservations such that it matches "ANY" bed type and room type **/
                preparedStatement = FR2Preparer.select(
                        "ANY",
                        "ANY",
                        field_values,
                        fields
                );
                if(isMyResultSetEmpty((rs=preparedStatement.executeQuery()))){
                    System.out.println("Sorry couldn't find any available rooms with those in and out times");
                    return;
                }
            }
            /** Step 3 - store the user typed data to show in the formatted reservations**/
            FR2.setUserTimeStay(DateFactory.daysBetween(field_values.get(fields.get(END_STAY)), field_values.get(fields.get(BEGIN_STAY))));
            FR2.setAdults(Integer.parseInt(field_values.get(fields.get(ADULTS))));
            FR2.setKids(Integer.parseInt(field_values.get(fields.get(KIDS))));
            FR2.setFirstName(field_values.get(fields.get(FIRST_NAME)));
            FR2.setLastName(field_values.get(fields.get(LAST_NAME)));

            /** Step 4 - format the reservations returned and print them out*/
            Map<Integer, FR> res = getReservations(rs, new FR2());
            System.out.println("Choose number below to book (c - cancel and go back to the main menu)");
            res.forEach((k, v) -> System.out.println( k+ " " + ((FR2)v).toString()));

            while (!(option = new Scanner(System.in).next()).matches("\\d+") &&
                    !res.keySet().contains(Integer.parseInt(option))) {
                // go back to main menu
                if (option.equals("c")) {
                    return;
                }
                System.out.println("Please enter a valid number");
            }
            /** Step 5 - get the picked reservation **/
            FR2 pickedRes = (FR2) res.get(Integer.parseInt(option));
            System.out.println(pickedRes.PickedToString());
            System.out.println("Y/N to to confirm reservation");
            if (!((confirm = new Scanner(System.in).next()).matches("Y")) && !(confirm.matches("y"))) {
                return;
            }
            /** Step 6 - insert record into db **/
            preparedStatement = FR2Preparer.insert(pickedRes, NEXT_RES_CODE);
            int i = preparedStatement.executeUpdate();
            RES_CODES.add(NEXT_RES_CODE++);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
