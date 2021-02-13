package com.revature.utilities;

import com.revature.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/*
Metamodel class, a generic class with all the fields and methods for scraping the
information required to map an object to a table in SQL database
 */

public class Metamodel<T> {


    //The class we are scraping
    private Class<T> clazz;

    //Primary key from class we are scraping
    private IdField primaryKeyField;

    //All the column fields from scraped class
    private List<ColumnField> columnFields;

    //Need to change name
    //All the getter methods from scraped class
    private List<GetterField> getterFields;

    //The setId method
    private SetterField sF;

    //All the foreign keys from scraped class
    private List<ForeignKeyField> foreignKeyFields;

    //The table name of the class we are scraping, redundant?
    private TableClass<T> tclazz;

    public static <T> Metamodel<T> of(Class<T> clazz) {
        if (clazz.getAnnotation(Table.class) == null) {
            throw new IllegalStateException("Cannot create Metamodel object! Provided class, " + clazz.getName() + "is not annotated with @Entity");
        }
        return new Metamodel<>(clazz);
    }

/*
    Is there any need to add more of the instance variables to a constructor?
 */
    public Metamodel(Class<T> clazz) {
        this.clazz = clazz;
        this.columnFields = new LinkedList<>();
        this.foreignKeyFields = new LinkedList<>();
        this.getterFields = new LinkedList<>();
    }

    public String getClassName() {
        return clazz.getName();
    }

    //Returns the class of the metamodel for use in scraping object values
    public Class<T> getClazz() {return clazz;}

    public IdField getPrimaryKey() {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id primaryKey = field.getAnnotation(Id.class);
            if (primaryKey != null) {
                return new IdField(field);
            }
        }
        throw new RuntimeException("Did not find a field annotated with @Id in: " + clazz.getName());
    }

    public List<ColumnField> getColumns() {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columnFields.add(new ColumnField(field));
            }
        }

        if (columnFields.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        return columnFields;
    }

    public List<ForeignKeyField> getForeignKeys() {

        List<ForeignKeyField> foreignKeyFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            JoinColumn column = field.getAnnotation(JoinColumn.class);
            if (column != null) {
                foreignKeyFields.add(new ForeignKeyField(field));
            }
        }

        return foreignKeyFields;

    }

/*
Not exactly sure about this method signature, return type and creation of new object type.
 */
    public <T> TableClass<T> getTable(){

       return new TableClass(clazz);

    }


    public List<GetterField> getGetters() {

        Method[] methods = clazz.getMethods();

        for (Method meth : methods) {

            if(meth.isAnnotationPresent(Getter.class)){
                Getter get = meth.getAnnotation(Getter.class);
                getterFields.add(new GetterField(meth));
            }
        }
        if (getterFields.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }
        return getterFields;
    }

    public SetterField getSetter(){

        Method[] methods = clazz.getMethods();
        for (Method meth : methods) {

            if(meth.isAnnotationPresent(Setter.class)){
                Setter set = meth.getAnnotation(Setter.class);
                //setterFields.add(new GetterField(meth));
                if (set==null) {
                    throw new RuntimeException("No method found in: " + clazz.getName());
                }
                sF = new SetterField(meth);
                return sF;

            }

        }
        return null;
    }

}
