package com.revature.utilities;


import com.revature.annotations.Column;
import com.revature.annotations.Table;

import java.lang.reflect.Field;

public class TableClass<T> {

    /*
    Not a field but a Type, used to get the name of the table.
     */

    private Class<T> clazz;

    public TableClass(Class<T> clazz) { this.clazz = clazz; }
    public String getName() { return clazz.getSimpleName(); }

    public String getType() { return clazz.getTypeName(); }

    public String getTableName() {
        return clazz.getAnnotation(Table.class).tableName();

    }

}
