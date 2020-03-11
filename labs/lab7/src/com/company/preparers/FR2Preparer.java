package com.company.preparers;

import com.company.ConnectionAdapter;
import com.company.parsers.DateFactory;
import com.company.reservations.FR2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static com.company.reservations.FR2.*;

public class FR2Preparer extends Preparer{
    /**
     * @param BED_TYPE - type of bed entered
     * @param ROOM_TYPE - type of room entered
     * @param fields - map that maps entered values to value
     * @param keys - entered values
     * @return PreparedStatement - that gives back reservations for info entered
     * @throws Exception
     */
    public PreparedStatement selectFR2(final String BED_TYPE, final String ROOM_TYPE,
                                       final HashMap<String, String > fields, final List<String> keys)
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
        // These fields need to be set no matter what
        statement = ConnectionAdapter.getConnection().prepareStatement(query);

        statement.setString(1, fields.get(keys.get(BEGIN_STAY)));
        statement.setString(2, fields.get(keys.get(END_STAY)));
        statement.setInt(3, Integer.parseInt(fields.get(keys.get(ADULTS))));
        statement.setInt(4, Integer.parseInt(fields.get(keys.get(KIDS))));
        switch (file){
            case "FR2_ANY_BED.sql":
                statement.setString(5, fields.get(keys.get(ROOM_CODE)));
                break;
            case "FR2_ANY_ROOM.sql":
                statement.setString(5, fields.get(keys.get(BED)));
                break;
            case "FR2.sql":
                statement.setString(5, fields.get(keys.get(BED)));
                statement.setString(6, fields.get(keys.get(ROOM_CODE)));
                break;
        }
        return statement;
    }
    public PreparedStatement insertFR2(FR2 res, int CODE_COUNT)
            throws SQLException
    {
        PreparedStatement preparedStatement = ConnectionAdapter.getConnection().
                         prepareStatement("INSERT INTO lab7_reservations" +
                        "(CODE, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids) VALUES"+
                        "(?,?,?,?,?,?,?,?,?)") ;

        preparedStatement.setInt(1, CODE_COUNT);
        preparedStatement.setString(2, res.getRoomCode());
        preparedStatement.setDate(3,  new Date(res.getCheckIn().getTime()));
        preparedStatement.setDate(4, new Date(res.getCheckOut().getTime()));
        preparedStatement.setDouble(5, res.getRate());
        preparedStatement.setString(6, res.getLastName());
        preparedStatement.setString(7, res.getFirstName());
        preparedStatement.setInt(8, res.getAdults());
        preparedStatement.setInt(9, res.getKids());

        return preparedStatement;
    }

}
