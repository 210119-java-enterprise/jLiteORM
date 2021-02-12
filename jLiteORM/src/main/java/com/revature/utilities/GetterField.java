package com.revature.utilities;

import com.revature.annotations.Column;
import com.revature.annotations.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GetterField {

    private Method method;

    public GetterField(Method method) {
        this.method = method;
    }

    public String getName() {
        return method.getName();
    }

    public String getMethodName() {
        return method.getAnnotation(Getter.class).getterName();
    }
}
