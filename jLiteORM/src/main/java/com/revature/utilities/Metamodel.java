package com.revature.utilities;

import com.revature.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/*
This class is a generic class with all the fields and methods required for scraping
information needed to map an generic object to a table in a SQL database.
 */

public class Metamodel<T> {


    //The class we are scraping
    private Class<T> clazz;

    //Primary key from class we are scraping
    private IdField primaryKeyField;

    //All the column fields from scraped class
    private List<ColumnField> columnFields;

    //All the column fields from scraped class
    private List<ColumnInsertField> columnInsertFields;

    //All the getter methods from scraped class
    private List<GetterField> getterFields;

    //All the getter methods from scraped class
    private List<GetterInsertField> getterInsertFields;

    //All the setter methods from scraped class
    private List<SetterField> setterFields;

    //The setId method
    private SetterIdField setterId;

    private GetterDelField getterDel;

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
        this.setterFields = new LinkedList<>();
        this.columnInsertFields = new LinkedList<>();
        this.getterInsertFields = new LinkedList<>();
    }

    public String getClassName() {
        return clazz.getName();
    }

    //Returns the class of the metamodel for use in scraping object values
    public Class<T> getClazz() {return clazz;}

    public List<ColumnInsertField> getColumnInsertFields(){
        return columnInsertFields;
    }

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


    public List<ColumnInsertField> getColumnsInsert() {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            ColumnInsert column = field.getAnnotation(ColumnInsert.class);
            if (column != null) {
                columnInsertFields.add(new ColumnInsertField(field));
            }
        }

        if (columnInsertFields.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        return columnInsertFields;
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

    public List<GetterInsertField> getGettersInsert() {

        Method[] methods = clazz.getMethods();

        for (Method meth : methods) {

            if(meth.isAnnotationPresent(GetterInsert.class)){
                GetterInsert get = meth.getAnnotation(GetterInsert.class);
                getterInsertFields.add(new GetterInsertField(meth));
            }
        }
        if (getterInsertFields.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }
        return getterInsertFields;
    }

    public List<SetterField> getSetter(){

        Method[] methods = clazz.getMethods();
        for (Method meth : methods) {
            //System.out.println(meth.toString());

            if(meth.isAnnotationPresent(Setter.class)){
                Setter set = meth.getAnnotation(Setter.class);
                //System.out.println(set.toString());
                setterFields.add(new SetterField(meth));
                if (set==null) {
                    throw new RuntimeException("No method found in: " + clazz.getName());
                }
            }

            //System.out.println("What we are adding to the array: "+setterFields.toString());
        }
        return setterFields;
    }

    public SetterIdField getSetterId(){

        Method[] methods = clazz.getMethods();
        for (Method meth : methods) {

            if(meth.isAnnotationPresent(SetterId.class)){
                SetterId set = meth.getAnnotation(SetterId.class);
                setterId = new SetterIdField(meth);
                if (set==null) {
                    throw new RuntimeException("No method found in: " + clazz.getName());
                }
            }
        }
        return setterId;
    }

    //Write this late, may need to check
    public GetterDelField getGetterDel() {

        Method[] methods = clazz.getMethods();
        for (Method meth : methods) {

            if (meth.isAnnotationPresent(GetterDel.class)) {
                GetterDel set = meth.getAnnotation(GetterDel.class);
                getterDel = new GetterDelField(meth);
                if (set == null) {
                    throw new RuntimeException("No method found in: " + clazz.getName());
                }
            }
        }
        return getterDel;
    }
    /*
    Returns column class type
     */
    public Class<?> getColumnClass(String column){
        for(ColumnField cF : this.getColumns()){
            if(cF.getColumnName().equals(column)){
                return cF.getType();
            }
        }
        if(getPrimaryKey().getColumnName().equals(column)){
            return getPrimaryKey().getType();
        }
        for(ForeignKeyField fKF : getForeignKeys()){
            if(fKF.getColumnName().equals(column)){
                return fKF.getType();
            }
        }
        return null;
    }

}
