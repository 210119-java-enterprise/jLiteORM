package com.revature.SQLStatements;

import com.revature.annotations.Id;
import com.revature.utilities.Metamodel;

import java.lang.reflect.Field;

public class Delete {

    public Delete(){
        super();
    }


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
