package com.revature.utilities;

import com.revature.annotations.Column;

import java.lang.reflect.Field;

/**
 * Acts as a wrapper class for Column annotated Fields.  Gives us methods to
 * get the names, types, and annotation names of a field.
 */
public class ColumnField {

    private Field field;

    /**
     * One arg constructor that takes a field.
     * @param field the field we are wrapping.
     */
    public ColumnField(Field field) {
        this.field = field;
    }

    /**
     * Gives the name of the field.
     * @return the name as String.
     */
    public String getName() {
        return field.getName();
    }

    /**
     * Gives the type of the field
     * @return class type of the field.
     */
    public Class<?> getType() {
        return field.getType();
    }

    /**
     * Gives the annotation name attached to field.
     * @return String name of annotation.
     */
    public String getColumnName() {
        return field.getAnnotation(Column.class).columnName();
    }
}
