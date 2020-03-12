package com.company.reservations;

import com.company.parsers.DateFactory;

public class FR5 {
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int BEGIN_DAY = 2;
    public static final int END_DAY = 3;
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
            case "RoomCode":
                break;
            case "RoomName":
                break;
            case "Beds":
                break;
            case "bedType":
                break;
            case "maxOcc":
                break;
            case "basePrice":
                break;
            case "decor":
                break;
            case "CODE":
                break;
            case "CheckIn":
                break;
            case "Checkout":
                break;
            case "Rate":
                break;
            case "FirstName":
                break;
            case "Adults":
                break;
            case "Kids":
                break;
        }
    }

}
