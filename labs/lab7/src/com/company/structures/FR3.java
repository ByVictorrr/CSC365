package com.company.structures;

public class FR3 extends FR{
    public static final int RES_CODE = 0;
    public static final int FIRST_NAME = 1;
    public static final int LAST_NAME = 2;
    public static final int BEGIN_STAY = 3;
    public static final int END_STAY = 4;
    public static final int ADULTS = 5;
    public static final int KIDS = 6;


    private int diff, Adults, Kids;
    private String CheckOut, CheckIn;
    public void setField(String ColumnName, String value){
        switch (ColumnName){
            case "diff":
                this.diff = Integer.parseInt(value);
                break;
            case "Checkout":
                this.CheckOut = value;
                break;
            case "Adults":
                this.Adults = Integer.parseInt(value);
                break;
            case "Kids":
                this.Kids = Integer.parseInt(value);
                break;
        }
    }

    public int getAdults() {
        return Adults;
    }

    public int getDiff() {
        return diff;
    }

    public int getKids() {
        return Kids;
    }

    public String getCheckIn() {
        return CheckIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }
}
