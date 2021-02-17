package com.revature.utilities;

import com.revature.annotations.ColumnInsert;

import java.lang.reflect.Field;

public class ColumnInsertField {

    private Field field;

    public ColumnInsertField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getColumnName() {
        return field.getAnnotation(ColumnInsert.class).columnName();
    }
}
