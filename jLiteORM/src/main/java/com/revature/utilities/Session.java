package com.revature.utilities;

import com.revature.repos.CRUD;

import java.sql.Connection;
import java.util.List;

/*
Save is mostly implemented and there is a rough outline for others

 */

public class Session {

    private final EntityManager entityMan;
    private final CRUD crud;

    public Session(Connection conn, EntityManager eM){

        this.entityMan = eM;
        this.crud = new CRUD(conn);
    }

    public EntityManager getEntityMan() {
        return entityMan;
    }

    /*
        Finds if there is a matching metamodel for the passed object, if so the
        CRUD method insert is called with parameters of metamodel and object
        More testing to do on CRUD end of the save/insert method
     */
    public void save(Object obj){

        Metamodel<?> metaModel = getAppropriateMetamodel(obj);
        if(metaModel == null){
            throw new RuntimeException("No matching metamodel for " + obj.getClass().getName());
        }

        crud.insert(metaModel,obj);

    }


    public void update(Object objBefore, Object objAfter){

        System.out.println("Not fully implemented");
        Metamodel<?> metaModel = getAppropriateMetamodel(objBefore);


        crud.update(metaModel, objBefore,objAfter);


    }

    public void delete(Object obj){

        System.out.println("Not fully implemented");
        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        crud.delete(metaModel,obj);


    }

    public List<?> findAll(Object obj){

        System.out.println("Not fully implemented");
        Metamodel<?> metaModel = getAppropriateMetamodel(obj);

        return crud.select(metaModel,obj);

    }

    /*
    May need another more specific findAll() method
     */





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
