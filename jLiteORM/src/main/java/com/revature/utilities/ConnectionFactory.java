package com.revature.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
A copy of ConnectionFactory from bankingApplication, works fine.
 */

public class ConnectionFactory {

    private static ConnectionFactory connFactory = new ConnectionFactory();
    private Properties props = new Properties();

    static{

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("\nCould not connect to database, try again");
        }
    }

    private ConnectionFactory(){

        try {
            props.load(new FileReader("src/main/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Establishes a connection with the DB based on our login info
     */
    public static ConnectionFactory getInstance(){ return connFactory;};

    public Connection getConnection(){

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(

                    props.getProperty("url"),
                    props.getProperty("admin-usr"),
                    props.getProperty("admin-ps")
            );
        } catch (SQLException throwables) {
            System.out.println("\nCould connect to database, try again");
            throwables.printStackTrace();
        }

        System.out.println("Successfully connected to DB");
        return conn;

    }
}