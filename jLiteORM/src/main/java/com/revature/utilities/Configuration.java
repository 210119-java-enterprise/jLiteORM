package com.revature.utilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Configuration {

    /*
    Connection to DB will be handled elsewhere, but here for now.
     */

    //Attempt to make connection to database inside Configuration class
    static{
        //Not connecting for now
        //ConnectionFactory.getInstance().getConnection();
    }

    /*
    Currently not using these 3 String fields because they are pulled from application.properties
     */
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private List<Metamodel<Class<?>>> metamodelList;



    @SuppressWarnings({ "unchecked" })
    public Configuration addAnnotatedClass(Class annotatedClass) {

        if (metamodelList == null) {
            metamodelList = new LinkedList<>();
        }

        metamodelList.add(Metamodel.of(annotatedClass));

        return this;
    }

    public List<Metamodel<Class<?>>> getMetamodels() {
        return (metamodelList == null) ? Collections.emptyList() : metamodelList;
    }


}
