package com.revature.utilities;
import java.util.*;


/*
Could add the ability to get a new Session from inside this class, returning a new session
and also the EntityManager
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
    Takes in an object from the user and returns the appropriate metamodel
     */
    public Metamodel<?> getAppropriateMetamodel(Object o){

        for (Metamodel<?> metamodel : metamodelList) {

            //Maybe a comparison that does not include package info, prob fine?
            if(metamodel.getClassName()==o.getClass().getName()){
                System.out.println("Found matching metamodel for: "+ o.getClass().getName());
                return metamodel;
            }
        }
        System.out.println("No match");
        return null;
    }


}
