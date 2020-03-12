package com.company.preparers;

import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

public class FR5Preparer extends Preparer{
    private static final String FR5_FOLDER = "FR5";
    public PreparedStatement selectFR5()
            throws Exception
    {
        final String QUERY = new String(Files.readAllBytes(Paths.get(BASE_DIR+"/" +  FR5_FOLDER + "/"+"FR1.sql")));
        PreparedStatement statement = ConnectionAdapter.getConnection().prepareStatement(QUERY);

        // set the different fields
        statement.setInt(1, 1);
        statement.setInt(2, 2);
        statement.setInt(3, 3);
        statement.setInt(4, 4);
        statement.setInt(4, 5);

        return statement;
    }
}
