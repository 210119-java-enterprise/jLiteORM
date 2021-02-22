package com.revature.repos;

import com.revature.annotations.Column;
import com.revature.annotations.ForeignKey;
import com.revature.annotations.PrimaryKey;
import com.revature.annotations.Serial;
import com.revature.utilities.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/*

 */
public class RepoHelper {


    public RepoHelper(){
        super();
    }

    /**
     *
     * @param obj
     * @return
     */
    //*****DONE*******
    public static ArrayList<Object> getObjectValues(Object obj){

        //List to hold all the values of a passed object
        ArrayList<Object> objVals = new ArrayList<>();
        //Gets all the fields from the object
        Field[] fields = obj.getClass().getDeclaredFields();
        //Iterates through object's fields
        for(Field f : fields){
            //Makes all fields accessible
            f.setAccessible(true);
            //Gets the annotation of field marked by Serial
            Serial ser = f.getAnnotation(Serial.class);
            //Gets the annotation of field marked by Column
            Column col = f.getAnnotation(Column.class);
            //Gets the annotation of field marked by ForeignKey
            ForeignKey fore = f.getAnnotation(ForeignKey.class);
            //Gets the annotation of field marked by PrimaryKey
            PrimaryKey pri = f.getAnnotation(PrimaryKey.class);
            /*
            First checks if the field is annotated as a column, if so it is added
            to our list of objects. Next, checks if field is a primary key but not a
            serial value. If so, it is added to the list of objects. This is because SQL
            assigns serial values, not the user. Finally, it checks if the vaue is a foreign key.
            If so, it is added to the list.
             */
            if(col != null | (pri != null && ser == null) | fore != null) {
                try {
                    //Creates object to hold value of unknown type, uses field method to get() the value.
                    Object val = f.get(obj);
                    //Adding to our running list of objects
                    objVals.add(val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //Return list of object's values after iterating through all its fields
        return objVals;
    }

    /**
     *
     * @param rs
     * @param md
     * @param obj
     * @param metamodel
     * @return
     */
    public static List<Object> mapResultSet(ResultSet rs, ResultSetMetaData md, Object obj, Metamodel<?> metamodel){
        //Holds our list of objects to be returned
        List<Object> objects = new LinkedList<>();
        try {
            //Holds the column names from table/object
            List<String> resultSetCols = new LinkedList<>();
            //Gets the number of columns in table/object
            int bound = md.getColumnCount();
            //Loop through the table columns
            //INDEXES ARE 1-BASED IN THE getColumnName() !!!
            for(int i = 0; i < bound; i++){

                //Adds string name of table column
                resultSetCols.add(md.getColumnName(i+1));
            }

            //Result set represents all the rows in a table starting with the first we iterate through
            while(rs.next()){

                /*
                 Used to create a new instance, Constructor class method is more flexible than Class classs
                 with args number. We need a new object to map each row in the table to an object
                 */
                Object newObject = obj.getClass().getConstructor().newInstance();

                //Loops through the names of the columns
                for(String s : resultSetCols){

                    //Returns the type of a field if has been annotated as a Column, ForeignKey or Id.
                    Class<?> type = metamodel.getColumnClass(s);
                    //Gets the specific column value as object from the current iterated row of the ResultSet
                    Object objectVal = rs.getObject(s);
                    //Gets us the name of the column's field
                    String name = metamodel.colFieldName(s);
                    //Takes the name of the field and capitalizes the first letter
                    String methodName = name.substring(0,1).toUpperCase() + name.substring(1);
                    /*
                    Concatenates the word "set" to the capitalized field name, and then uses that
                    String to invoke the set method on the object.
                     */
                    Method method = obj.getClass().getMethod("set" + methodName, type);

                    //Invokes the set method on the object and passes in the object value from the ResultSet
                    method.invoke(newObject, objectVal);
                }
                //Adds our value populated object to our running list of objects
                objects.add(newObject);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Returns list of value populated objects
        return objects;
    }




}
