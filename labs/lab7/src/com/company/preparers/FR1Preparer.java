package com.company.preparers;

import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

public class FR1Preparer{

    private static final String FR1_FOLDER = "FR1";
    private FR1Preparer(){}
    /**
     * @return - the query for the FR1 option
     */
    public static PreparedStatement FR1() throws Exception {
        PreparedStatement statement;
        final String QUERY = new String(Files.readAllBytes(Paths.get(FR1_FOLDER + "/"+"FR1.sql")));
        return ConnectionAdapter.getConnection().prepareStatement(QUERY);
    }
}
