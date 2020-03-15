package com.company.reservations;

import com.company.parsers.DateFactory;
import com.company.utilities.Pair;

import java.util.Date;

public class FR2 extends FR{


    private static int userTimeStay;
    private static int Kids, Adults;
    private static String FirstName, LastName;
    static public void  setUserTimeStay(int timeStay){FR2.userTimeStay = timeStay;}
    static public void setAdults(int adults){FR2.Adults=adults;}
    static public void setKids(int kids){FR2.Kids=kids;}
    static public void setLastName(String LN){FR2.LastName=LN;}
    static public void setFirstName(String FN){FR2.FirstName=FN;}


    /**
     * Gives you the number of days between check in and checkout
     * @return the number of days between checkout and check in
     */
    public long numDaysOfStay() { return DateFactory.daysBetween(this.CheckOut, this.CheckIn);}
    /**
     * Sets the objects fields given a returned sql query
     * @param ColumnName - returned sql column name
     * @param value - value of the sql returned column
     * @throws Exception
     */
    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "RoomName":
                this.RoomName=value;
                break;
            case "RoomCode":
                this.RoomCode= value;
                break;
            case "Checkout":
                this.CheckIn=  DateFactory.StringToDate(value);
                break;
            case "decor":
                this.Decor=value;
                break;
            case "basePrice":
                this.basePrice=Double.parseDouble(value);
                this.Rate = FR.totalRateOfStay(CheckIn, CheckOut, basePrice);
                break;
            case "bedType":
                this.BedType=value;
                break;
            case "diff":
                this.CheckOut = DateFactory.addDays(userTimeStay, this.CheckIn);
                break;
        }

    }

    public static String getLastName() {
        return LastName;
    }

    public static String getFirstName() {
        return FirstName;
    }

    public static int getKids() {
        return FR2.Kids;
    }

    public static int getAdults() {
        return FR2.Adults;
    }

    @Override
    public String toString() {
        return "Available Reservations{" +
                "RoomName='" + RoomName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", Decor='" + Decor + '\'' +
                ", BedType='" + BedType + '\'' +
                ", CheckIn=" + CheckIn +
                ", CheckOut=" + CheckOut +
                ", Adults=" + FR2.Adults +
                ", Kids=" + FR2.Kids +
                ", Rate=" + Rate +
                '}';
    }

    public String PickedToString() {
        return  "FirstName='"  + FirstName + '\'' +
                ", LastName='"  + LastName + '\'' +
                ", RoomName='" + RoomName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", CheckIn=" + CheckIn +
                ", CheckOut=" + CheckOut +
                ", Adults=" + Adults +
                ", Kids=" + Kids +
                ", total_rate=" + Rate +
                '}';
    }





}

