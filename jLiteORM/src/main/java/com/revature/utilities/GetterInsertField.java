package com.revature.utilities;

import com.revature.annotations.Getter;
import com.revature.annotations.GetterInsert;

import java.lang.reflect.Method;

public class GetterInsertField {

    private Method method;

    public GetterInsertField(Method method) {
        this.method = method;
    }

    public String getName() {
        return method.getName();
    }

    public String getMethodName() {
        return method.getAnnotation(GetterInsert.class).getterName();
    }
}
