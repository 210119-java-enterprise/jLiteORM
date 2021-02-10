package com.revature.utilities;

import com.revature.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField {

    private Field field;

    public ColumnField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getColumnName() {
        return field.getAnnotation(Column.class).columnName();
    }
}
