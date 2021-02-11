package com.revature;
import com.revature.reflection.testModels.AppUser;
import com.revature.reflection.testModels.CheckingAccount;
import com.revature.utilities.*;

import java.util.List;

/*
In this Driver we create a Configuration object and give it the annotated classes via the
addAnnotatedClass method.  We are trying to model what the user of our framework would
give us in order to initiate the framework/app. From this point we have all we need to scrape the classes
for the required information - fields/columns, primary/foreign keys, types/table names.


 */
public class Driver {


    /*
    Treating this like the main from bankingApplication and trying to make it as lite
    as possible.
     */
    public static void main(String[] args) {


        /*
        Moved to Configuration class for now, not sure what is best
        ConnectionFactory.getInstance().getConnection();
         */


        /*
        Configuration object kicks off the scraping of classes
        User of framework will give us the annotated classes
         */
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(AppUser.class)
                .addAnnotatedClass(CheckingAccount.class);

        /*
        All this logic below demonstrates the information we can get from our framework
        using the Reflections API.  I think below mimics the various method calls
        that we will be making during the running of the framework/application.
         */
        for (Metamodel<?> metamodel : cfg.getMetamodels()) {

            System.out.printf("Printing metamodel for class: %s\n", metamodel.getClassName());
            IdField idField = metamodel.getPrimaryKey();
            List<ColumnField> columnFields = metamodel.getColumns();
            List<ForeignKeyField> foreignKeyFields = metamodel.getForeignKeys();
            //Not sure about this type with generics
            TableClass<TableClass> tableField = metamodel.getTable();
            System.out.printf("\tFound a table field of type %s, which maps to the table name: %s \n", tableField.getName(), tableField.getTableName());

            System.out.printf("\tFound a primary key field named %s of type %s, which maps to the column with the name: %s\n", idField.getName(), idField.getType(), idField.getColumnName());

            for (ColumnField columnField : columnFields) {
                System.out.printf("\tFound a column field named: %s of type %s, which maps to the column with the name: %s\n", columnField.getName(), columnField.getType(), columnField.getColumnName());
            }

            for (ForeignKeyField foreignKeyField : foreignKeyFields) {
                System.out.printf("\tFound a foreign key field named %s of type %s, which maps to the column with the name: %s\n", foreignKeyField.getName(), foreignKeyField.getType(), foreignKeyField.getColumnName());
            }

            System.out.println();
        }

    }
}
