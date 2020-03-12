package com.company.reservations;

import com.company.parsers.DateFactory;

public class FR5 {
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int BEGIN_STAY = 2;
    public static final int END_STAY = 3;
    public static final int ROOM_CODE = 4;
    public static final int RES_CODE = 5;


    /**
     * Sets fields for a given returned sql query
     * @param ColumnName - returned sql column name
     * @param value - value of the sql returned column
     */
    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "diff":
        }
    }

}
