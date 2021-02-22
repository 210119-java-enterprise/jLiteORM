package com.revature.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Used for creating connections to the database.
 */
public class ConnectionFactory {

    private static ConnectionFactory connFactory = new ConnectionFactory();
    private Properties props = new Properties();


    static{

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("\nFrom here 1, could not connect to database, try again");
        }
    }

    /**
     * Loads the property file containing DB connection credentials and location.
     */
    private ConnectionFactory(){

        try {
            props.load(new FileReader("src/main/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets an instance of the class for making a DB connection.
     * @return Returns the instance of the class.
     */
    public static ConnectionFactory getInstance(){ return connFactory;}

    /**
     * Makes a connection to the DB with the credentials inside of the application.properties file.
     * @return
     */
    public Connection getConnection(){

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(

                    props.getProperty("url"),
                    props.getProperty("admin-usr"),
                    props.getProperty("admin-ps")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("\nFrom here 2, could connect to database, try again");

        }
        //System.out.println("Successfully connected to DB");
        return conn;

    }
}
