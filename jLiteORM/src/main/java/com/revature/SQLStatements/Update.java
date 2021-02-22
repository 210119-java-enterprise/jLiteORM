package com.revature.SQLStatements;

import com.revature.annotations.*;
import com.revature.utilities.Metamodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains static methods used for assisting the generation of a SQL update statement.
 */
public class Update {

    String sqlStatementUpdate;

    public Update(){
        super();
    }


    /**
     * 
     * @param model
     * @param object
     * @return
     */
    public static ArrayList<String> getTableCols(Metamodel<?> model, Object object){
        // Gets the table name of passed object through class's Table annotation
        String tableName = model.getTable().getTableName();
        //Make String list for column names
        ArrayList<String> tableColumns = new ArrayList<>();
        //Iterate through the object's fields
        for(Field f : object.getClass().getDeclaredFields()){
            //If field has an Id annotation get it
            Id tag = f.getAnnotation(Id.class);
            //If field is not a serial value in the table
            if(tag == null){
                //Get the annotation if column
                Column column = f.getAnnotation(Column.class);
                //Get the annotation if primary key
                PrimaryKey primary = f.getAnnotation(PrimaryKey.class);
                //Get the annotation if foreign key
                ForeignKey foreign = f.getAnnotation(ForeignKey.class);
                //If its a column annotation
                if (column != null) {
                    //Add the string name of the column to running list
                    tableColumns.add(column.columnName());
                }
                //If its a primary key annotation
                else if(primary != null){
                    //Add the string name of the column to running list
                    tableColumns.add(primary.columnName());
                }
                //If its a foreign key annotation
                else if(foreign != null){
                    //Add the string name of the column to running list
                    tableColumns.add(foreign.columnName());
                }
            }
        }
        //Don't think sorting is necessary, check if it breaks
    //Collections.sort(tableColumns);
        return tableColumns;

    }

    public static String getSQLStatementUpdate(String tableName, ArrayList<String> tableColumns){
        String updateStatement;
        //Bound based on number of columns/fields object contains
        int bound = tableColumns.size();
        //String builder for set portion of statement
        StringBuilder setPortion = new StringBuilder("SET ");
        //String builder for where portion of statement
        StringBuilder wherePortion = new StringBuilder("WHERE ");

        //Bound based on number of fields/columns contained in object
        for(int i = 0; i < bound; i++){
            //For the last item
            if(i == (bound-1)){
                //If we are at the last of our passed columns, add a space to the end
                setPortion.append(tableColumns.get(i)).append(" = ").append(" ? ").append(" ");
                wherePortion.append(tableColumns.get(i)).append(" = ").append(" ? ").append(" ");
            }
            //For any item except the last
            else {
                //If we are not at the last of our passed columns, add a comma to the end
                setPortion.append(tableColumns.get(i)).append(" = ").append(" ? ").append(", ");
                wherePortion.append(tableColumns.get(i)).append(" = ").append(" ? ").append(" and ");
            }
        }
        /*
        Concatenation of UPDATE + table name + SET + various (columns names + wildcard)  +
        WHERE + various(column names +wildcard)
         */
        updateStatement = "UPDATE " + tableName + " " + setPortion.toString() + wherePortion.toString();
        //Return above statement
        return updateStatement;

    }



}
