package com.revature;
import com.revature.utilities.EntityManager;
import com.revature.testModels.AppUser;
import com.revature.testModels.CheckingAccount;
import com.revature.utilities.*;

/*
In this Driver we create a Configuration object and give it annotated classes via the
addAnnotatedClass method. Configuration object should be passed on to EntityManager.From
this point we have all we need to scrape the classes for the required information -
fields/columns, primary/foreign keys, types/table names.

 */
public class Driver {


    /*
    Sort of treating the main like the main from bankingApplication and trying to make it as lite
    as possible.
     */
    public static void main(String[] args) {


        /*
        Configuration object gathers the annotated classes(user input)
        and adds them to a list of metamodels, they become metamodels of type
        whatever class is passed.
         */
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(AppUser.class)
                .addAnnotatedClass(CheckingAccount.class);

        /*
        Passing the metamodels to the constructor of EntityManager.
         */
        EntityManager eM = new EntityManager(cfg.getMetamodels());

        /*
        Believe this is how Session should function, check and see if you have any
        other instance of ConnectionFactory. Should probably make a new session object
        more automated from inside EntityManager
         */

        Session sesh = new Session(ConnectionFactory.getInstance().getConnection(), eM);


        /*
        Example, user wants to add this new user to DB. Simulate through calling Session's methods
        which will call CRUD's methods.
         */
        AppUser user1 = new AppUser("user99","password10","Bill","Noon");

        //sesh.save(user1);


    }
}
