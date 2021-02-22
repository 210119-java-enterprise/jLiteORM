package com.revature.utilities;


import com.revature.annotations.Column;
import com.revature.annotations.Table;


/**
 * Acts as a wrapper class for Table annotated Fields.  Gives us methods to
 * get the names, types, and annotation names of a field.
 */
public class TableClass<T> {

    /*
    Not a field but a Type, used to get the name of the table.
     */

    private Class<T> clazz;

    /**
     * One-arg constructor takes the field that it wraps.
     * @param clazz The field to wrap.
     */
    public TableClass(Class<T> clazz) { this.clazz = clazz; }

    /**
     * Returns the field name.
     * @return String name of field.
     */
    public String getName() { return clazz.getSimpleName(); }

    /**
     * Gives the type of the field
     * @return class type of the field.
     */
    public String getType() { return clazz.getTypeName(); }

    /**
     * Returns the String name of the field's annotation.
     * @return String name of annotation.
     */
    public String getTableName() {
        return clazz.getAnnotation(Table.class).tableName();

    }

}
