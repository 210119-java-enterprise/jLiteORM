package com.revature.utilities;
import java.util.*;


/*
This class holds Metamodels and is exposed to the framework user.  It gives the user the
appropriate metamodel for their object and it also provides the user with a Session object.
A session object contains a DB connection and an instance of the EntityManager class
 */

/**
 * Exposed to the user of the framework. This class holds a list of metamodels and has a method
 * to generate user sessions.
 */
public class EntityManager {


    private List<Metamodel<Class<?>>> metamodelList;

    /**
     * Constructor that takes a list of Metamodels.
     * @param listOfMM list of Metamodels.
     */
    public EntityManager(List<Metamodel<Class<?>>> listOfMM){
        this.metamodelList = listOfMM;
    }

    /**
     * Used to get the list of Metamodels.
     * @return List of Metamodels.
     */
    public List<Metamodel<Class<?>>> getMetamodelList() {
        return metamodelList;
    }


    /*

    /**
     *  Returns a Session object which contains a DB connection from ConnectionFactory
        as well an instance of the EnitityManager class.
     * @return
     */
    public Session getSession(){

        Session sesh = null;

        sesh = new Session(ConnectionFactory.getInstance().getConnection(), this);

        return sesh;


    }


}
