package com.company.reservations;

import com.company.parsers.DateFactory;

public class FR3 extends FR{



    private String FirstName, LastName;
    private Integer daysOpenNext, daysOpenPrev, maxOcc;


    /**
     * Sets fields for a given returned sql query
     * @param ColumnName - returned sql column name
     * @param value - value of the sql returned column
     */
    public void setField(String ColumnName, String value)
        throws Exception
    {
        switch (ColumnName){
            case "RoomName":
                this.RoomName =value;
                break;
            case "CheckIn":
                this.CheckIn = DateFactory.StringToDate(value);
                break;
            case "Checkout":
                this.CheckOut = DateFactory.StringToDate(value);
                break;
            case "Rate":
                this.Rate=Double.parseDouble(value);
                break;
            case "FirstName":
                this.FirstName = value;
                break;
            case "LastName":
                this.LastName = value;
                break;
            case "Adults":
                this.Adults = Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids = Integer.parseInt(value);
                break;
            case "days_open_prior":
                this.daysOpenPrev = Integer.parseInt(value);
                break;
            case "days_open_after":
                this.daysOpenNext= Integer.parseInt(value);
                break;
            case "basePrice":
                this.basePrice=Double.parseDouble(value);
                break;
            case "maxOcc":
                this.maxOcc=Integer.parseInt(value);
                break;

        }
    }

    public Integer getDaysOpenNext() {
        return daysOpenNext;
    }

    public Integer getDaysOpenPrev() {
        return daysOpenPrev;
    }

    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public Integer getKids(){return this.Kids;}
    public Integer getAdults(){return this.Adults;}

    public Integer getMaxOcc() {
        return maxOcc;
    }
}
