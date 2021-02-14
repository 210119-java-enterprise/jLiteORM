package com.revature.utilities;

import com.revature.repos.CRUD;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

/*
This class is exposed to the framework user and provides them with a instance of EntityManager
which contains the metamodels relevant to their classes/objects. It also contains an instance of
the CRUD class which provides the CRUD methods to interact with the DB.  It also provides a connection
Session itself, has a number of CRUD like methods that end up calling the actual CRUD methods of the CRUD class.
 */

public class Session {

    private final EntityManager entityMan;
    private final CRUD crud;

    public Session(Connection conn, EntityManager eM){

        this.entityMan = eM;
        //Session has instance of CRUD with its own connection
        this.crud = new CRUD(conn);
    }

    public EntityManager getEntityMan() {
        return entityMan;
    }

    /*
        Finds if there is a matching metamodel for the passed object, if so, the
        CRUD insert() method is called with parameters of metamodel and object
     */
    public void save(Object obj){

        //Checking that the object has a metamodel
        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        if(metaModel == null){
            throw new RuntimeException("No matching metamodel for " + obj.getClass().getName());
        }

        //Call to the CRUD insert method
        crud.insert(metaModel,obj);

    }

    /*
    Given an object return all the rows from its table
    Not fully implemented
     */
    public List<?> findAll(Object obj){


        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        return crud.select(metaModel,obj);

    }

    /*
Given an object and specific columns, return relevant rows
Not fully implemented
 */
    public List<?> findSome(Object obj, String... theColumns){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        return crud.selectSome(metaModel,obj,theColumns);

    }

    /*
    Used for fixing problems
     */
    public void troubleshooter(Object obj){

    }

    /*
    Not implemented
     */
    public void update(Object objBefore, Object objAfter){

        System.out.println("Not fully implemented");
        Metamodel<?> metaModel = getAppropriateMetamodel(objBefore);


        crud.update(metaModel, objBefore,objAfter);


    }

    /*
    Not implemented
     */
    public void delete(Object obj){

        System.out.println("Not fully implemented");
        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        crud.delete(metaModel,obj);


    }


    /*
   Takes in an object from the user and returns the appropriate metamodel if it exists
    */
    public Metamodel<?> getAppropriateMetamodel(Object o){

        for (Metamodel<?> metamodel : entityMan.getMetamodelList()) {

            if(metamodel.getClassName()==o.getClass().getName()){
                System.out.println("Found matching metamodel for: "+ o.getClass().getName());
                return metamodel;
            }
        }
        System.out.println("No matching metamodel");
        return null;
    }

}
