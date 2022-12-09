package org.data.services;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlDataService implements DataInterface {
    private final String connectionString = "jdbc:mysql://localhost:3306/shop" +
            "?useUnicode=true&characterEncoding=UTF-8";
    private final String dbUser = "root";
    private final String dbPass = "";
    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        connectionString, dbUser, dbPass);
                System.out.println("Connection");
            } catch (Exception ex) {
                System.out.println("MysqlDataService::getConnection() -- " +
                        ex.getMessage());
            }
        }
        return connection;
    }
}

