package com.company.executors;

import com.company.preparers.FR1Preparer;

import java.sql.ResultSet;

public class FR1Executor extends Executor{

    /**
     * When command is set to FR1 this function
     * It outputs a list of rooms to the user sorted
     * by popularity (highest to lowest)
     */
    public void execute() {
        try {
            ResultSet resultSet = FR1Preparer.FR1().executeQuery();
            printResultSet(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
