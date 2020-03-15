package com.company.reservations;

public class FR6 extends FR{
    public static final int ROOM_NAME=0;
    public static final int ROOM_CODE=1;
    public static final int MONTH=2;
    public static final int MONTH_REV=3;

    private String RoomName, RoomCode;
    private int Month;
    private Double MonthRev;

    @Override
    public void setField(String ColumnName, String value) throws Exception {
        switch (ColumnName){
            case "RoomName":
                this.RoomName=value;
                break;
            case "Room":
                this.RoomCode=value;
                break;
            case "month":
                this.Month=Integer.parseInt(value);
                break;
            case "month_rev":
                this.MonthRev=roundTwoDecimal(Double.parseDouble(value));
                break;
        }
    }


    @Override
    public String getRoomCode() {
        return RoomCode;
    }
    public int getMonth(){
        return Month;
    }

    public double getMonthRev() {
        return MonthRev;
    }

    @Override
    public String getRoomName() {
        return RoomName;
    }
}
