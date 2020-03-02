package com.company;

import org.omg.PortableInterceptor.NON_EXISTENT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Executor {
    private String command;
    private static PreparedStatement preparedStatement;
    private static ConnectionAdapter connectionAdapter;
    private static Executor instance;

    private Executor(){
    }

    public static Executor getInstance() {
        if(instance==null){
            instance=new Executor();
            connectionAdapter = ConnectionAdapter.getInstance();
        }
        return instance;
    }


    public void setCommand(String command) {
        this.command = command;
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
            preparedStatement = connectionAdapter.getConnection().prepareStatement(
                    "SELECT DISTINCT rm.RoomName,\n" +
                            "    ROUND((COUNT(*) OVER (PARTITION BY rs.ROOM))*100/180,2) rm_pop_percent,\n" +
                            "    mr.most_recent_day as next_avail_chkin,\n" +
                            "    DATEDIFF(most_recent_day, most_recent_ckin) as recent_duration\n" +
                            "FROM lab7_reservations rs, lab7_rooms rm,\n" +
                            "    -- returns the Room, most_recent day \n" +
                            "\t(SELECT ROOM, MAX(CheckOut) as most_recent_day, MAX(CheckIn) as most_recent_ckin\n" +
                            "\tFROM lab7_reservations rs GROUP BY ROOM) AS mr\n" +
                            "\n" +
                            "WHERE rs.ROOM=mr.ROOM AND rm.RoomCode=rs.ROOM AND DATEDIFF(mr.most_recent_day, rs.CheckIn) <= 180\n" +
                            "ORDER BY rm_pop_percent DESC;"
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            printResultSet(resultSet);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void optionFR2(){

        final int max_occ  = getMaxOccupancy();
        final List<String> fields = Arrays.asList(
                "First Name",
                "Last Name",
                "Room Code",
                "Bed Type",
                "Begin Date Of Stay",
                "End Date Of Stay",
                "Number Of Children",
                "Number Of Adults"
        );

        HashMap<String, String> field_values = getFields(fields);
        // step -1 - if user typed in "c" then return to main menu
        if(field_values==null){
            return;
        }
        // step 0 - build and then return the query of rooms
        preparedStatement = connectionAdapter.getConnection().prepareStatement("" +
                "")


        // step 1 - use fields to output the available rooms
            // Case 1.1 - if no matches are found then output 5 suggested possibilities for different rooms or dates

            /* These 5 rooms should be chosen based on similarity to desired reservation
            *  Similarity = nearby dates and with similar features decor ect.
            */

            /* Consider adults+children <= max_room_cap  and dates don't overlap with another res*/
            if (max_occ < Integer.getInteger(field_values.get("Number Of Children")) +
                    Integer.getInteger(field_values.get("Number Of Adults"))){
                    System.out.println("No Suitable rooms are available");
            }

        // step 2 - option to input booking number(ROOM CODE) of one of those rooms





    }

    /**
     * @return - the max occupancy of all rooms in the INN
     */
    private final int getMaxOccupancy(){
        /*Write a sql query to return max occupancy of a room in the INN*/
        return 0;
    }

    /**
     * gets the value of the field_names of each field
     * @param field_names - the field names want a user to input
     * @return map that maps the field names to value or null if they want to go back to main menu
     */
    private HashMap<String, String> getFields(List<String> field_names){
        int counter=0;
        final HashMap<String, String> fields = new HashMap<>(field_names.size());
        String value;
        while(counter < field_names.size()) {
            System.out.println("input " + field_names.get(counter)
                    + "(c - return to main menu): ");
            if((value = new Scanner(System.in).next()).equals( "c")){
                return null;
            }
            fields.put(field_names.get(counter), value);
        }
        return fields;
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

}
