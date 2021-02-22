package com.revature.utilities;
import java.util.*;


/*
This class holds Metamodels and is exposed to the framework user.  It gives the user the
appropriate metamodel for their object and it also provides the user with a Session object.
A session object contains a DB connection and an instance of the EntityManager class
 */
public class EntityManager {


    private List<Metamodel<Class<?>>> metamodelList;

    public EntityManager(List<Metamodel<Class<?>>> listOfMM){
        this.metamodelList = listOfMM;
    }

    public List<Metamodel<Class<?>>> getMetamodelList() {
        return metamodelList;
    }


    /*
    Returns a Session object which contains a DB connection from ConnectionFactory
    as well an instance of the EnitityManager class
     */
    public Session getSession(){

        Session sesh = null;

        sesh = new Session(ConnectionFactory.getInstance().getConnection(), this);

        return sesh;


    }


}
