package com.revature.SQLStatements;

import com.revature.annotations.Id;
import com.revature.utilities.Metamodel;

import java.lang.reflect.Field;

/**
 * Contains static methods that assist in the creation of a SQL Delete statement.
 */
public class Delete {

    public Delete(){
        super();
    }

    /**
     * Generates a SQL delete statement.
     * @param metamodel For scraping the passed POJO.
     * @param tableName Name of table where delete will take place.
     * @param obj Passed POJO that represents the table where delete will happen.
     * @return  Returns the SQL statement as a String.
     */
    public static String getSQLStatementDelete(Metamodel<?> metamodel, String tableName, Object obj) {
        //Get all the object fields
        Field[] fields = obj.getClass().getDeclaredFields();
        String idColumnName = "";
        //Iterate on all the fields
        for (Field field : fields) {
            //Grab the field annotated with Id and get its column name as String
            if (field.getAnnotation(Id.class) != null) {
                //Set to column name as String
                idColumnName = field.getAnnotation(Id.class).columnName();
            }
        }

        //String columnName = "";
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(idColumnName);
        sb.append("=");
        sb.append("?");

        return sb.toString();
    }

    /**
     * Gets the primary key value of the object we are deleting
     * @param obj The object we want to delete.
     * @return The int value of the primary key.
     */
    public static int getFieldForDelete(Object obj){
        //Get all fields
        Field[] fields = obj.getClass().getDeclaredFields();
        int idValue = 0;
        //Iterate on the fields
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotation(Id.class) != null) {
                try {
                    //Get the int id value
                    idValue = field.getInt(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //Return id value
        return idValue;

    }



}
