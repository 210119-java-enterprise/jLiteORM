package com.revature.utilities;

import com.revature.annotations.Id;

import java.lang.reflect.Field;

public class IdField {

    private Field field;

    public IdField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getColumnName() {
        return field.getAnnotation(Id.class).columnName();
    }
}
