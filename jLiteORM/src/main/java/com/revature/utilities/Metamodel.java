package com.revature.utilities;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    }

    public String getClassName() {
        return clazz.getName();
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





}
