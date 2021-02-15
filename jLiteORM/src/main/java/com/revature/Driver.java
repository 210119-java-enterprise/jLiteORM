package com.revature;
import com.revature.utilities.EntityManager;
import com.revature.testModels.AppUser;
import com.revature.testModels.CheckingAccount;
import com.revature.utilities.*;

import java.util.List;

/*
In this Driver we create a Configuration object and give it annotated classes via the
addAnnotatedClass method. Configuration object should be passed to an EntityManager. An
EntityManager is then used to create a Session object.  These are the three classes
exposed to the user of the framework.  Below we are trying to simulate what a user would
give the framework for full functionality - Annotated classes, which classes are annotated,
new objects for the DB.
 */
public class Driver {
    /*
    Sort of treating the main like the main from bankingApplication and trying to make it as lite
    as possible.
     */
    public static void main(String[] args) {
        /*
        Configuration object gets the annotated classes(user hand input)
        and adds them to a list of metamodels, they become metamodels of type
        whatever classes are passed.
         */
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(AppUser.class)
                .addAnnotatedClass(CheckingAccount.class);
        /*
        Passing the metamodels to the constructor of EntityManager.
        Once again, hand inputted bu user of framework
         */
        EntityManager eM = new EntityManager(cfg.getMetamodels());
        //Creating session
        Session sesh = eM.getSession();
        /*
        Example, user wants to add/register this new user to DB. He inputs object info
        through the bankingApp console.  Then he calls the framework's methods on it.
        Methods are called in order: first through Session object, then through CRUD object.
        Simulating above:
         */
        AppUser user2 = new AppUser("user2","password10","John","Johnson");
        //Troubleshooting save/insert
        //sesh.save(user2);

        //List<AppUser> selectTry = (List<AppUser>) sesh.findAll(user1);
        //selectTry.forEach(System.out::print);

    }
}
