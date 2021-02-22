package com.revature.utilities;

import com.revature.repos.CRUD;

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
 Implemented
  */
    public List<?> findAll(Object obj){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        return crud.select(metaModel,obj);

    }

    /*
    Not fully implemented
    */
    public List<?> findSome(Object obj, String... theColumns){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        return crud.selectSome(metaModel,obj,theColumns);

    }

    /*
    Implemented
     */
    public void update(Object objAfter, Object objBefore){

        Metamodel<?> metaModel = getAppropriateMetamodel(objBefore);
        crud.update(metaModel, objAfter, objBefore);

    }

    /*
    Implemented
     */
    public void delete(Object obj){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        crud.delete(metaModel,obj);

    }


    /*
   Takes in an object from the user and returns the appropriate metamodel if it exists
    */
    public Metamodel<?> getAppropriateMetamodel(Object o){

        for (Metamodel<?> metamodel : entityMan.getMetamodelList()) {

      //System.out.println(o.getClass().getSimpleName());
      //System.out.println(metamodel.getSimpleClassName());

            if(metamodel.getSimpleClassName().equals(o.getClass().getSimpleName())){
                //System.out.println("Found matching metamodel for: "+ o.getClass().getSimpleName());
                return metamodel;
            }
        }
        System.out.println("No matching metamodel");
        return null;
    }

}
