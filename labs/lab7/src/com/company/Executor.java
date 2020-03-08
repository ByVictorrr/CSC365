package com.company;

import com.company.structures.FR2;
import com.company.validators.FR2Validator;
import com.company.validators.Validator;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Executor {
    private static PreparedStatement preparedStatement;
    private static QueryPreparer queryPreparer;
    private static Executor instance;

    public static Integer MAX_ADULTS;
    public static Integer MAX_KIDS;
    public static List<String> ROOM_CODES;
    public static List<String> BED_TYPES;
    public static Integer NEXT_RES_CODE;

    private Executor(){ }

    private static void SET_CONSTRAINTS()
            throws SQLException
    {

        List<ResultSet> rs = new ArrayList<>();
            rs.add(queryPreparer.MAX_ADULTS().executeQuery());
            rs.add(queryPreparer.MAX_KIDS().executeQuery());
            rs.add(queryPreparer.MAX_RES_CODE().executeQuery());
            rs.add(queryPreparer.BED_TYPES().executeQuery());
            rs.add(queryPreparer.ROOM_TYPES().executeQuery());
            rs.get(0).next();
            rs.get(1).next();
            rs.get(2).next();
            MAX_ADULTS = rs.get(0).getInt(1);
            MAX_KIDS = rs.get(1).getInt(1);
            NEXT_RES_CODE = rs.get(2).getInt(1)+1;
            BED_TYPES = GET_LIST(rs.get(3));
            ROOM_CODES = GET_LIST(rs.get(4));
    }
    private static List<String > GET_LIST(ResultSet rs)
            throws SQLException
    {

        List<String> BED_TYPES = new ArrayList<>();
        while (rs.next()){
            BED_TYPES.add(rs.getString(1));
        }
        return BED_TYPES;
    }



    public static Executor getInstance()
    {
        ResultSet rs1,rs2;
        if(instance==null){
            instance=new Executor();
            queryPreparer = QueryPreparer.getInstance();
            try {
                SET_CONSTRAINTS();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return instance;
    }



    /**
     * When command is set to FR1 this function
     * executes
     *
     * It outputs a list of rooms to the user sorted
     * by popularity (highest to lowest)
     *
     */
    public void optionFR1(){
        try {
            ResultSet resultSet = queryPreparer.FR1().executeQuery();
            printResultSet(resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void optionFR2(){


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

        HashMap<String, String> field_values;
        ResultSet rs;
       try {
           // step 0 - if user typed in "c" then return to main menu
           if((field_values=getFields(fields, new FR2Validator()))==null){
               return;
           }
           Date in = new SimpleDateFormat("yyyy-MM-dd").parse(field_values.get(fields.get(FR2.BEGIN_STAY)));
           Date out = new SimpleDateFormat("yyyy-MM-dd").parse(field_values.get(fields.get(FR2.END_STAY)));
           FR2.setRes_days((int)daysBetween(out,in));
            // step 2 - build and then return the query of rooms
            preparedStatement = queryPreparer.selectFR2(
                    field_values.get(fields.get(FR2.BED)),
                    field_values.get(fields.get(FR2.ROOM_CODE)),
                    field_values,
                    fields

            );

           rs = preparedStatement.executeQuery();
            // Case 1.1 - if no matches are found then output 5 suggested possibilities for different rooms or dates
            if(isMyResultSetEmpty(rs)){
                System.out.println("Sorry no rooms found that meet those requirements " +
                        "\nHere are some similar rooms: ");
                /* These 5 rooms should be chosen based on similarity to desired reservation
                 *  Similarity = nearby dates and with similar features decor ect.
                 */
                // give suggestions
                preparedStatement = queryPreparer.selectFR2(
                                "ANY",
                              "ANY",
                                        field_values,
                                        fields
                );
                rs = preparedStatement.executeQuery();
            }
            System.out.println("Choose number below to book (c - cancel and go back to the main menu)");
            Map<Integer,FR2> res = getReservations(rs);
           // step 2 - option to input booking number(ROOM CODE) of one of those rooms
           System.out.println("Please enter the booking number to order");
           res.forEach((k,v)->System.out.println(k + " " + v));
            // show the returned list in of numbered options
            String option, confirm;
            while(!(option = new Scanner(System.in).next()).matches("\\d+") ||
                    !res.keySet().contains(Integer.parseInt(option))
            ){
                // go back to main menu
                if(option.equals("c")){
                    return;
                }
                System.out.println("Please enter a valid number");
            }
            FR2 pickedRes = res.get(Integer.parseInt(option));
            pickedRes.setFirstName(field_values.get(fields.get(FR2.FIRST_NAME)));
            pickedRes.setLastName(field_values.get(fields.get(FR2.LAST_NAME)));

           System.out.println(pickedRes.PickedToString());

           System.out.println("Y/N to to confirm reservation");
           if(!((confirm=new Scanner(System.in).next()).matches("Y")) && !(confirm.matches("y"))){
               // cancel reservation if not y or Y
                return;
           }

           // TODO update the database with their reservations
           preparedStatement= queryPreparer.insertFR2(pickedRes, NEXT_RES_CODE);

           int i = preparedStatement.executeUpdate();
           NEXT_RES_CODE++;
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     * gets the value of the field_names of each field
     * @param field_names - the field names want a user to input
     * @return map that maps the field names to value or null if they want to go back to main menu
     */
    private HashMap<String, String> getFields(List<String> field_names, Validator validator)
        throws Exception
    {
        File file = new File("/home/victord/CSC365/labs/lab7/tests/FR2");
        int counter=0;
        final HashMap<String, String> fields = new HashMap<>(field_names.size());
        validator.setFields(field_names);
        validator.setFieldsValues(fields);
        String value;

       Scanner scanner = new Scanner(file);
        while(counter < field_names.size() && scanner.hasNextLine()) {
            System.out.println("input " + field_names.get(counter)
                    + "(c - return to main menu): ");
            if((value = scanner.next()).equals( "c")){
                return null;
            }
            // check to see if a field is valid
            if(!validator.valid(counter, value)){
                continue;
            }
            fields.put(field_names.get(counter), value);
            counter++;
        }
        return fields;
    }


    private Map<Integer, FR2> getReservations(ResultSet rs) throws Exception{
        ResultSetMetaData rsmd = rs.getMetaData();
        int col_num = rsmd.getColumnCount();
        Map<Integer, FR2> res = new HashMap<>();
        int id=0;

        while (rs.next()){
            StringBuilder values = new StringBuilder();
            FR2 f = new FR2();
            for (int i=1; i <= col_num; i++){
                f.setField(rsmd.getColumnName(i), rs.getString(i));
            }
            res.put(id++, f);
        }
        return res;
    }
    private void printResultSet(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        int col_num = rsmd.getColumnCount();
        boolean isFirst = true;
        StringBuilder col_names = new StringBuilder();
        while (rs.next()){
            StringBuilder values = new StringBuilder();
            for (int i=1; i <= col_num; i++){
                if(i > 1){
                    values.append(", ");
                    if(isFirst) col_names.append(", ");
                }
                if (isFirst) col_names.append(rsmd.getColumnName(i));

                values.append(rs.getString(i));
            }
            if(isFirst){
                System.out.println(col_names);
                isFirst=false;
            }
            System.out.println(values);
        }
    }


    public static boolean isMyResultSetEmpty(ResultSet rs) throws SQLException {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }
    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }


}

