package com.revature;

import com.revature.testModels.AppUser;
import com.revature.testModels.CheckingAccount;
import com.revature.utilities.Configuration;
import com.revature.utilities.EntityManager;
import com.revature.utilities.Session;

import java.util.List;

public class Driver {

  public static void main(String[] args) {

    /*
    Configuration object gets the annotated classes(user hand input)
    and adds them to a list of metamodels, they become metamodels of type
    whatever classes are passed.
     */
    Configuration cfg = new Configuration();
    cfg.addAnnotatedClass(AppUser.class).addAnnotatedClass(CheckingAccount.class);
    /*
    Passing the metamodels to the constructor of EntityManager.
    Once again, hand inputted bu user of framework
     */
    EntityManager eM = new EntityManager(cfg.getMetamodels());
    // Creating session
    Session sesh = eM.getSession();
    /*
    Example, user wants to add/register this new user to DB. He inputs object info
    through the bankingApp console.  Then he calls the framework's methods on it.
    Methods are called in order: first through Session object, then through CRUD object.
    Simulating above:
     */
    // Adding simulation
    // AppUser new1 = new AppUser("user17","password10","Mint","Bags");
    // AppUser new2 = new AppUser("user12","password10","Manners","Name Change");
    // CheckingAccount new1 = new CheckingAccount(2500.00);
    // Troubleshooting save/insert
    // sesh.save(new1);

    // Select all simulation
    // List<AppUser> selectTry = (List<AppUser>) sesh.findAll(new AppUser());
    // selectTry.forEach(System.out::print);

    // List<CheckingAccount> selectTry = (List<CheckingAccount>) sesh.findAll(new1);
    // selectTry.forEach(System.out::print);

    // Delete simulation
    // AppUser delUser = new AppUser(12,"user13","password10","Bananas","Mono");
    // sesh.delete(delUser);
    // CheckingAccount new1 = new CheckingAccount(2,2500.00);
    // sesh.delete(new1);

    // Update simulation
    // AppUser before = new AppUser("greg10","password10","Greg","Gertsen");
    // AppUser after = new AppUser("greg10","password10","Gregory","Johnson");
    //      CheckingAccount before = new CheckingAccount(1,8545.0);
    //      CheckingAccount after = new CheckingAccount(1,5000.00);
    //      sesh.update(after, before);

    // Select from simulation
    // AppUser user2 = new AppUser("user14","password10","Bilbo","Bags");
    // List<AppUser> results = (List<AppUser>) sesh.findSome(user2,  "username", "password");
    // CheckingAccount new1 = new CheckingAccount(2500.00);
    // List<CheckingAccount> results = (List<CheckingAccount>) sesh.findSome(new1,  "balance");
    // results.forEach(System.out::print);

    //
  }
}
