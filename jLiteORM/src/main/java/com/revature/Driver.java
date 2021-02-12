package com.revature;
import com.revature.entityMgmt.EntityManager;
import com.revature.testModels.AppUser;
import com.revature.testModels.CheckingAccount;
import com.revature.utilities.*;

import java.util.List;

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
        Configuration object gathers the annotated classes(user inputs)
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

        //Runs the demo method that prints a bunch of info related to the annotations
        //eM.placeHolderMethod();

        /*
        Try to persist an object, we know that we will get a 'validated' object from
        the user end.  We then need to handle that object in a generic way - get it's fields
        the names of those fields in the DB, get the table name the object belongs to. I think
        sending the object to a service class, then
         */


        AppUser user1 = new AppUser("user99","password10","Bill","Noon");

        //Example, user wants to add this new user to DB

        eM.insert(user1);












        /*
        All this logic below demonstrates the information we can get from our framework
        using the Reflections API.  I think below mimics the various method calls
        that we will be making during the running of the framework/application.
         */
//        for (Metamodel<?> metamodel : cfg.getMetamodels()) {
//
//            System.out.printf("Printing metamodel for class: %s\n", metamodel.getClassName());
//            IdField idField = metamodel.getPrimaryKey();
//            List<ColumnField> columnFields = metamodel.getColumns();
//            List<ForeignKeyField> foreignKeyFields = metamodel.getForeignKeys();
//            //Not sure about this type with generics
//            TableClass<TableClass> tableField = metamodel.getTable();
//            System.out.printf("\tFound a table field of type %s, which maps to the table name: %s \n", tableField.getName(), tableField.getTableName());
//
//            System.out.printf("\tFound a primary key field named %s of type %s, which maps to the column with the name: %s\n", idField.getName(), idField.getType(), idField.getColumnName());
//
//            for (ColumnField columnField : columnFields) {
//                System.out.printf("\tFound a column field named: %s of type %s, which maps to the column with the name: %s\n", columnField.getName(), columnField.getType(), columnField.getColumnName());
//            }
//
//            for (ForeignKeyField foreignKeyField : foreignKeyFields) {
//                System.out.printf("\tFound a foreign key field named %s of type %s, which maps to the column with the name: %s\n", foreignKeyField.getName(), foreignKeyField.getType(), foreignKeyField.getColumnName());
//            }
//
//            System.out.println();
//        }

    }
}
