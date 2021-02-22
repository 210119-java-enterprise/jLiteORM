package com.revature.utilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is exposed to the user of the framework.  They must add their annotated
 * classes into a Configuration object. The annotated classes are stored in the object
 * within a list of Metamodels. The metamodels are accessible through a public method.
 */
public class Configuration {


    /*
    Currently not using these 3 String fields because they are pulled from application.properties
     */
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private List<Metamodel<Class<?>>> metamodelList;


    /**
     * Used for adding user annotated classes into the configuration object.
     * The annotated classes are stored within a list of Metamodels.
     * @param annotatedClass A user annotated class.
     * @return Returns the Configuration object that will contain added classes.
     */
    @SuppressWarnings({ "unchecked" })
    public Configuration addAnnotatedClass(Class annotatedClass) {

        if (metamodelList == null) {
            metamodelList = new LinkedList<>();
        }

        metamodelList.add(Metamodel.of(annotatedClass));

        return this;
    }

    /**
     * Returns the Metamodels contained within the Configuration class.
     * @return A list of Metamodels.
     */
    public List<Metamodel<Class<?>>> getMetamodels() {
        return (metamodelList == null) ? Collections.emptyList() : metamodelList;
    }


}
