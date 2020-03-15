package com.company.preparers;

import com.company.ConnectionAdapter;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ConstantsPreparer{
    private ConstantsPreparer(){}

    /**
     * @return - the max occupancy of all rooms in the INN
     */
    public final static PreparedStatement MAX_OCC()
            throws SQLException
    {
        PreparedStatement p;
        p =  ConnectionAdapter.getConnection().prepareStatement("SELECT MAX(maxOcc) FROM lab7_rooms");
        return p;
    }

    public final static PreparedStatement ROOM_TYPES()
            throws SQLException
    {

        PreparedStatement p;
        p = ConnectionAdapter.getConnection().prepareStatement("SELECT DISTINCT RoomCode FROM lab7_rooms");
        return p;

    }
    public final static PreparedStatement BED_TYPES()
            throws SQLException
    {
        PreparedStatement p;
        p = ConnectionAdapter.getConnection().prepareStatement("SELECT DISTINCT bedType FROM lab7_rooms");
        return p;
    }
    public final static PreparedStatement MAX_RES_CODE()
            throws  SQLException
    {
        PreparedStatement p;
        p = ConnectionAdapter.getConnection().prepareStatement("SELECT MAX(CODE) FROM lab7_reservations");
        return p;
    }
    public final static PreparedStatement RES_CODES()
            throws SQLException
    {
        PreparedStatement p;
        p =  ConnectionAdapter.getConnection().prepareStatement("SELECT CODE FROM lab7_reservations");
        return p;
    }


}
