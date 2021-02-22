package com.revature.utilities;

import com.revature.repos.CRUD;

import java.sql.Connection;
import java.util.List;


/**
 This class is exposed to the framework user and contains an instance of EntityManager
 which contains the metamodels relevant to their POJOs. It also contains an instance of
 the CRUD class which provides the CRUD methods to interact with the DB.  It also contains a connection.
 Has a number of CRUD like methods that end up calling the actual CRUD methods of the CRUD class.
 */
public class Session {

    private final EntityManager entityMan;
    private final CRUD crud;

    /**
     * Two arg constructor containing a Connection instance and an EntityManager.
     * @param conn Instance of Connection.
     * @param eM Instance of EntityManager.
     */
    public Session(Connection conn, EntityManager eM){

        this.entityMan = eM;
        //Session has instance of CRUD with its own connection
        this.crud = new CRUD(conn);
    }

    /**
     * Returns the EntityManager instance.
     * @return EntityManager instance.
     */
    public EntityManager getEntityMan() {
        return entityMan;
    }


  /**
   * Finds if there is a matching metamodel for the passed object, if so, the CRUD insert() method
   * is called with parameters of metamodel and object.
   * @param obj The POJO.
   */
  public void save(Object obj) {

        //Checking that the object has a metamodel
        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        if(metaModel == null){
            throw new RuntimeException("No matching metamodel for " + obj.getClass().getName());
        }
        //Call to the CRUD insert method
        crud.insert(metaModel,obj);
    }

    /**
     *Finds if there is a matching metamodel for the passed object, if so, the CRUD select() method
     * is called with parameters of metamodel and object.
     * @param obj The POJO.
     * @return List of objects representing all the records from a table.
     */
    public List<?> findAll(Object obj){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        return crud.select(metaModel,obj);

    }

  /**
   * Finds if there is a matching metamodel for the passed object, if so, the CRUD selectSome() method
   * is called with parameters of metamodel and object.
   * @param obj The POJO.
   * @param theColumns List of String. The user specified columns they want.
   * @return List of objects representing all the records  and specified columns from a table.
   */
  public List<?> findSome(Object obj, String... theColumns) {

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        return crud.selectSome(metaModel,obj,theColumns);

    }

  /**
   * Finds if there is a matching metamodel for the passed object, if so, the CRUD update()
   * method is called with parameters of metamodel and object.
   * @param objAfter Object with values of desired update.
   * @param objBefore Object with values before update.
   */
  public void update(Object objAfter, Object objBefore) {

        Metamodel<?> metaModel = getAppropriateMetamodel(objBefore);
        crud.update(metaModel, objAfter, objBefore);

    }

  /**
   * Finds if there is a matching metamodel for the passed object, if so, the CRUD delete() method
   * is called with parameters of metamodel and object.
   * @param obj The POJO.
   */
  public void delete(Object obj) {

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        crud.delete(metaModel,obj);

    }


    /**
     * Takes in a POJO from the user and returns the appropriate metamodel if it exists.
     * @param o The POJO.
     * @return The appropriate Metamodel.
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
