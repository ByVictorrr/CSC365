package com.company.preparers;


import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

public class FR6Preparer{
    private static final String FR6_FOLDER = "FR6";

    private FR6Preparer(){}
    public static PreparedStatement select()
            throws Exception
    {
        final String QUERY = new String(Files.readAllBytes(Paths.get(FR6_FOLDER + "/"+"FR6.sql")));
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(QUERY);
        return statement;
    }
}
