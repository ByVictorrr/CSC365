package com.company.preparers;


import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FR6Preparer extends Preparer {
    private static final String FR6_FOLDER = "FR6";
    public PreparedStatement selectFR6()
            throws Exception
    {
        final String QUERY = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/" + FR6_FOLDER + "/"+"FR6.sql")));
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(QUERY);
        return statement;
    }
}
