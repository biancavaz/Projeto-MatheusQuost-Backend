package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private final String URL = "jdbc:mysql://localhost:3306/sistema_login?";
    private final String USER = "root";
    private final String PASSWORD = "root";

    public Connection getConnection() throws SQLException {
        //class - static method to connect to database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
