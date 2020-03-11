package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionAdapter
{
    private static final String JDBC_URL = System.getenv("APP_JDBC_URL");
    private static final String DB_USERNAME = System.getenv("APP_JDBC_USER");
    private static final String DB_PASSWORD = System.getenv("APP_JDBC_PW");
    private static final Connection connection;
    static {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = conn;
    }
    private  ConnectionAdapter(){}

    public static Connection getConnection() {
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(connection!=null) {
            connection.close();
        }
    }
}
