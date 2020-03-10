package com.company.structures;

public class FR4 extends FR{
    // this object store information to cancel a reservation
    private String FirstName;

    public void setField(String ColumnName, String value)
            throws Exception
    {
        switch (ColumnName){
            case "Firstname":
                this.FirstName = value;
                break;
        }
    }

    public String getFirstName() {
        return FirstName;
    }
}
