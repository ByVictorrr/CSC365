package com.company.reservations;

import com.company.parsers.DateFactory;

public class FR3 extends FR{
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int BEGIN_STAY = 2;
    public static final int END_STAY = 3;
    public static final int ADULTS = 4;
    public static final int KIDS = 5;


    private int daysAvailableNextRes;
    private double basePrice;

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
                this.daysAvailableNextRes = Integer.parseInt(value);
                break;
            case "Checkout":
                this.CheckOut = DateFactory.StringToDate(value);
                break;
            case "Adults":
                this.Adults = Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids = Integer.parseInt(value);
                break;
            case "basePrice":
                this.basePrice=Double.parseDouble(value);
        }
    }

    public int getDaysAvailableNextRes() {
        return daysAvailableNextRes;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
