package com.company;

import com.company.structures.FR2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static com.company.structures.FR2.*;

public class QueryPreparer {
    private String Query;
    private static QueryPreparer instance;
    private static final String BASE_DIR = "/home/victord/CSC365/labs/lab7";
    private static ConnectionAdapter connectionAdapter;

    public static QueryPreparer getInstance() {
        if(instance == null) {
            instance = new QueryPreparer();
            connectionAdapter = ConnectionAdapter.getInstance();
        }

        return instance;
    }

    /**
     *
     * @return - the query for the FR1 option
     */
    public PreparedStatement FR1() throws Exception {
        PreparedStatement statement;
        final String FR1_FOLDER = "FR1";
        final String QUERY = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/" + FR1_FOLDER + "/"+"FR1.sql")));
       return connectionAdapter.getConnection().prepareStatement(QUERY) ;
    }



    public PreparedStatement selectFR2(final String BED_TYPE, final String ROOM_TYPE,
                                        HashMap<String, String > fields, final List<String> keys)
            throws  Exception
    {



        final String FR2_FOLDER = "FR2";
        PreparedStatement statement;
        String query;
        String file;
        if (BED_TYPE.equals("ANY") && ROOM_TYPE.equals("ANY")){
            file="FR2_ANY_ANY.sql";
        }else if(BED_TYPE.equals("ANY") && !ROOM_TYPE.equals("ANY")){
            file="FR2_ANY_BED.sql";
        }else if(!BED_TYPE.equals("ANY") && ROOM_TYPE.equals("ANY")){
            file="FR2_ANY_ROOM.sql";
        }else{
            file="FR2.sql";
        }
        query = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/"+FR2_FOLDER + "/"+file)));
        statement = connectionAdapter.getConnection().prepareStatement(query);
        switch (file){
            case "FR2_ANY_ANY.sql":
                statement.setString(1, fields.get(keys.get(BEGIN_STAY)));
                statement.setString(2, fields.get(keys.get(END_STAY)));
                statement.setInt(3, Integer.parseInt(fields.get(keys.get(ADULTS))));
                statement.setInt(4, Integer.parseInt(fields.get(keys.get(KIDS))));
                break;
            case "FR2_ANY_BED.sql":
                statement.setString(1, fields.get(keys.get(BEGIN_STAY)));
                statement.setString(2, fields.get(keys.get(END_STAY)));
                statement.setInt(3, Integer.parseInt(fields.get(keys.get(ADULTS))));
                statement.setInt(4, Integer.parseInt(fields.get(keys.get(KIDS))));
                statement.setString(5, fields.get(keys.get(ROOM_CODE)));
                break;
            case "FR2_ANY_ROOM.sql":
                statement.setString(1, fields.get(keys.get(BEGIN_STAY)));
                statement.setString(2, fields.get(keys.get(END_STAY)));
                statement.setInt(3, Integer.parseInt(fields.get(keys.get(ADULTS))));
                statement.setInt(4, Integer.parseInt(fields.get(keys.get(KIDS))));
                statement.setString(5, fields.get(keys.get(BED)));

                break;
            default:
                statement.setString(1, fields.get(keys.get(BEGIN_STAY)));
                statement.setString(2, fields.get(keys.get(END_STAY)));
                statement.setInt(3, Integer.parseInt(fields.get(keys.get(ADULTS))));
                statement.setInt(4, Integer.parseInt(fields.get(keys.get(KIDS))));
                statement.setString(5, fields.get(keys.get(BED)));
                statement.setString(6, fields.get(keys.get(ROOM_CODE)));
                break;
        }

            return statement;

    }
    // data coming from user
    public PreparedStatement insertFR2(FR2 res, int CODE_COUNT)
            throws SQLException
    {
        PreparedStatement preparedStatement = ConnectionAdapter
                .getInstance().getConnection().prepareStatement("INSERT INTO lab7_reservations" +
                        "(CODE, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids) VALUES"+
                        "(?,?,?,?,?,?,?,?,?)") ;

        preparedStatement.setInt(1, CODE_COUNT);
        preparedStatement.setString(2, res.getRoomCode());
        preparedStatement.setString(3,  res.getCheckIn());
        preparedStatement.setString(4, res.getCheckOut());
        preparedStatement.setDouble(5, res.getRate());
        preparedStatement.setString(6, res.getLastName());
        preparedStatement.setString(7, res.getFirstName());
        preparedStatement.setInt(8, res.getAdults());
        preparedStatement.setInt(9, res.getKids());

        return preparedStatement;
    }

    public PreparedStatement selectFR3(String RES_CODE)
                throws  Exception
    {

        final String FR3_FOLDER = "FR3";
        String query;
        query = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/"+FR3_FOLDER + "/"+"FR3_RES_CODE.sql")));
        PreparedStatement statement = connectionAdapter.getConnection().prepareStatement(query);
        statement.setString(1, RES_CODE);
        return statement;
    }
    /**
     * @return - the max occupancy of all rooms in the INN
     *
     */
    public final PreparedStatement MAX_KIDS()
            throws SQLException
    {
        PreparedStatement p;
        p = connectionAdapter.getConnection().prepareStatement("SELECT MAX(Kids) FROM lab7_reservations");
        return p;
    }
    public final PreparedStatement MAX_ADULTS()
            throws SQLException
    {
        PreparedStatement p;
        p = connectionAdapter.getConnection().prepareStatement("SELECT MAX(Adults) FROM lab7_reservations");
        return p;
    }

    public final PreparedStatement ROOM_TYPES()
            throws SQLException
    {

        PreparedStatement p;
        p = connectionAdapter.getConnection().prepareStatement("SELECT DISTINCT RoomCode FROM lab7_rooms");
        return p;

    }
    public final PreparedStatement BED_TYPES()
            throws SQLException
    {
        PreparedStatement p;
        p = connectionAdapter.getConnection().prepareStatement("SELECT DISTINCT bedType FROM lab7_rooms");
        return p;
    }
    public final PreparedStatement MAX_RES_CODE()
            throws  SQLException
    {
        PreparedStatement p;
        p = connectionAdapter.getConnection().prepareStatement("SELECT MAX(CODE) FROM lab7_reservations");
        return p;
    }




}
