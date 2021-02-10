package com.revature;
import com.revature.reflection.testModels.AppUser;
import com.revature.utilities.ColumnField;
import com.revature.utilities.IdField;
import com.revature.utilities.Metamodel;
import com.revature.utilities.TableClass;

import java.util.List;

/*
Driver makes metamodels of our model/POJO classes using the annotations provided.
The utilities - ColumnField, IdField, and TableClass wrap our scrapped annotations/reflection
data.

 */
public class Driver {

    public static void main(String[] args) {

        Metamodel<AppUser> userMetamodel = Metamodel.of(AppUser.class);

        IdField idField = userMetamodel.getPrimaryKey();
        List<ColumnField> columnFields = userMetamodel.getColumns();
        TableClass tableField = userMetamodel.getTable();

        System.out.printf("The primary key of User is: %s; which maps to the column with the name: %s\n", idField.getName(), idField.getColumnName());
        System.out.printf("The name of the class is: %s; which maps to the table with the name: %s\n", tableField.getName(), tableField.getTableName());

        for (ColumnField columnField : columnFields) {
            System.out.printf("The User class contains a column called: %s; which maps to the column with the name: %s\n", columnField.getName(), columnField.getColumnName());
        }

    }




}
