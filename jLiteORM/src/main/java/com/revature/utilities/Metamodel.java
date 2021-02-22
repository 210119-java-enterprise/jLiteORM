package com.revature.utilities;

import com.revature.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/*
This class is a generic class with all the fields and methods required for scraping
information needed to map an generic object to a table in a SQL database.
 */

/**
 A generic class with all the fields and methods required for scraping
 information used to map a generic object to a table row of a SQL database.
 * @param <T> Takes the class of the object passed.
*/
public class Metamodel<T> {

    private Class<T> clazz;

    private IdField primaryKeyField;

    private List<ColumnField> columnFields;

    private List<ForeignKeyField> foreignKeyFields;

    private TableClass<T> tclazz;

    /**
     * Used to make a metamodel of the passed annotated POJO.
     * @param clazz The annotated POJO.
     * @param <T> The type of the passed annotated POJO.
     * @return Returns a Metamodel of the type that was passed.
     */
    public static <T> Metamodel<T> of(Class<T> clazz) {
        if (clazz.getAnnotation(Table.class) == null) {
            throw new IllegalStateException("Cannot create Metamodel object! Provided class, " + clazz.getName() + "is not annotated with @Entity");
        }
        return new Metamodel<>(clazz);
    }

    /**
     * Takes in annotated POJO and becomes its type.
     * @param clazz type of annotated POJO.
     */
    public Metamodel(Class<T> clazz) {
        this.clazz = clazz;
        this.columnFields = new LinkedList<>();
        this.foreignKeyFields = new LinkedList<>();

    }

    /**
     * Returns the class name of the annotated POJO.
     * @return The String name of the annotated POJO.
     */
    public String getClassName() {
        return clazz.getName();
    }

    /**
     Returns the class of the metamodel for use in scraping object values
     * @return The type
     */
    public Class<T> getClazz() {return clazz;}

    /**
     * Returns the simple class name as a String.
     * @return Returns the simple class name as a String.
     */
    public String getSimpleClassName() {

        return clazz.getSimpleName();
    }

    /**
     * Returns the IdField object of a POJO if it has a field annotated with Id.
     * @return Returns the IdField object.
     */
    public IdField getPrimaryKey() {

        //Get all fields from the scraped class
        Field[] fields = clazz.getDeclaredFields();
        //Iterate through all fields
        for (Field field : fields) {
            //If the field is annotated as a primary key grab it
            Id primaryKey = field.getAnnotation(Id.class);
            //If it exists
            if (primaryKey != null) {
                //Return it as IdField which has helpful methods
                return new IdField(field);
            }
        }
        throw new RuntimeException("Did not find a field annotated with @Id in: " + clazz.getName());
    }



    /**
     * Method to get all the fields of a class that are annotated as columns
     * @return a list of ColumnFields, object that allows us to access info from annotation
     */
    public List<ColumnField> getColumns() {

        //Gets all the declared fields from the class the metamodel is scraping
        Field[] fields = clazz.getDeclaredFields();
        //Iterate through the declared fields
        for (Field field : fields) {
            //Gets the Column annotation associated with the field
            Column column = field.getAnnotation(Column.class);
            //If the field is annotated by Column
            if (column != null) {
                //Add the field if it is annotated as a Column, added as a ColumnField object
                columnFields.add(new ColumnField(field));
            }
        }
        //After iteration, check if class has any fields that are annotated as columns
        if (columnFields.isEmpty()) {
            //Class has no fields annotated as columns
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        //Return the list of ColumnField objects that will give us access to the String column name
        return columnFields;
    }

    /**
     * Method to get all the fields of a class that are annotated as ForeignKeys.
     * @return A list of ForeignKeyFields, objects that allows us to access info from annotation
     */
    public List<ForeignKeyField> getForeignKeys() {

        //To hold list of foreign key objects
        List<ForeignKeyField> foreignKeyFields = new ArrayList<>();
        //Get all fields from class being scraped
        Field[] fields = clazz.getDeclaredFields();
        //Iterate through all fields
        for (Field field : fields) {
            //If field is annotated as a JoinColumn, grab it.
            JoinColumn column = field.getAnnotation(JoinColumn.class);
            //If it exists
            if (column != null) {
                //Add to running list of ForeignKeyFields objects
                foreignKeyFields.add(new ForeignKeyField(field));
            }
        }
        //Returns list of ForeignKeyFields objects
        return foreignKeyFields;
    }


    /**
     * Returns the class of the table.
     * @param <T> The class of the table.
     * @return The class of the table.
     */
    public <T> TableClass<T> getTable(){
       return new TableClass(clazz);
    }

    /**
     * Gets the class of a specific column
     * @param col String name of column.
     * @return The class of the specified column.
     */
    public Class<?> getColumnClass(String col){

        //Iterate through a list of ColumnField objects, aka all fields annotated as columns
        for(ColumnField colF : this.getColumns()){

            /*
            If the passed in string, which comes from a list of all the ResultSetMeta data columns
            of an object as Strings, matches our list of ColumnFields(fields that are annotated as columns)
            objects getColumnName() method. We return the type of that field.
             */
            if(colF.getColumnName().equals(col)){
                //Return the type of the matched field
                return colF.getType();
            }
        }

        /*
        If the passed column name represents a primary key, use its ID annotation
        to get its column name then the IdField to get its field type. Return the type of the field
         */
        if(getPrimaryKey().getColumnName().equals(col)){
            return getPrimaryKey().getType();
        }
         /*
        If the passed column name represents a foreign key, use its ID annotation
        to get its column name then the IdField class to get its field type. Return the type of the field
         */
        for(ForeignKeyField fKF : getForeignKeys()){
            
            if(fKF.getColumnName().equals(col)){
                return fKF.getType();
            }
        }
        //If no columns of any kind return null
        return null;
    }

    /**
     * Gets String name of the field associated with a particular column.
     * @param columnName String name of column
     * @return String name of field.
     */
    public String colFieldName(String columnName){
        //Iterate through a list of ColumnField objects, aka all fields annotated as columns
        for(ColumnField colF : this.getColumns()){

             /*
            If the passed in string, which comes from a list of all the ResultSetMeta data columns
            of an object as Strings, matches our list of ColumnFields(fields that are annotated as columns)
            objects getColumnName() method. We return the name of that field.
             */
            if(colF.getColumnName().equals(columnName)){
                return colF.getName();
            }
        }
         /*
        If the passed column name represents a primary key, use its ID annotation
        to get its name of column then the IdField to get the field's name .
         */
        if(getPrimaryKey().getColumnName().equals(columnName)){
            //Returns the name of the field
            return getPrimaryKey().getName();
        }

         /*
        If the passed column name represents a foreign key, use its ID annotation
        to get its column name then the IdField class to get field name.
         */
        for(ForeignKeyField fKF : getForeignKeys()){

            if(fKF.getColumnName().equals(columnName)){
                //Return the name of the field
                return fKF.getName();
            }
        }
        //If no columns of any kind return null
        return null;
    }

}
