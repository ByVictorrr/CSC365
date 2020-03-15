package com.company.preparers;

import com.company.ConnectionAdapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FR4Preparer{

    private FR4Preparer(){}

    public static PreparedStatement select(int RES_CODE) throws SQLException{
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(
                "SELECT * FROM lab7_reservations WHERE CODE = ?"
        );
        statement.setInt(1, RES_CODE);
        return statement;
    }


    public static PreparedStatement update(int RES_CODE) throws SQLException {
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(
                "DELETE FROM lab7_reservations WHERE CODE = ?"
        );
        statement.setInt(1, RES_CODE);
        return statement;
    }
}
