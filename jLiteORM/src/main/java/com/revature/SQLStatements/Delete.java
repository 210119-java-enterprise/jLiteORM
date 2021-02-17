package com.revature.SQLStatements;

import com.revature.annotations.Id;
import com.revature.utilities.Metamodel;

import java.lang.reflect.Field;

public class Delete {

    public Delete(){
        super();
    }


    public static String getSQLStatementDelete(Metamodel<?> metamodel, String tableName, Object obj) {

        Field[] fields = obj.getClass().getDeclaredFields();
        String idColumnName = "";
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                idColumnName = field.getAnnotation(Id.class).columnName();
            }
        }

        String columnName = "";
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(idColumnName);
        sb.append("=");
        sb.append("?");

        return sb.toString();
    }

    public static int getFieldForDelete(Object obj){

        Field[] fields = obj.getClass().getDeclaredFields();
        int idValue = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                try {
                    idValue = field.getInt(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return idValue;

    }



}
