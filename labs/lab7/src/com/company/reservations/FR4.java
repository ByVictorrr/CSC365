package com.company.reservations;

import com.company.parsers.DateFactory;

public class FR4 extends FR{
    // this object store information to cancel a reservation

    private String LastName, FirstName;
    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "CODE":
                this.resCode=Integer.parseInt(value);
                break;
            case "Room":
                this.RoomCode = value;
                break;
            case "CheckIn":
                this.CheckIn= DateFactory.StringToDate(value);
                break;
            case "Checkout":
                this.CheckOut=DateFactory.StringToDate(value);
                break;
            case "Rate":
                this.Rate = Double.parseDouble(value);
                break;
            case "LastName":
                this.LastName=value;
                break;
            case "FirstName":
                this.FirstName = value;
                break;
            case "Adults":
                this.Adults=Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids=Integer.parseInt(value);
                break;
        }
    }

    public String getFirstName() {
        return FirstName;
    }
    public String getLastName(){
        return LastName;
    }

    @Override
    public String toString() {
        return "FR4{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", RoomCode='" + RoomCode + '\'' +
                ", CheckIn=" + CheckIn +
                ", CheckOut=" + CheckOut +
                ", Adults=" + Adults +
                ", Kids=" + Kids +
                ", resCode=" + resCode +
                ", Rate=" + Rate +
                '}';
    }
}
