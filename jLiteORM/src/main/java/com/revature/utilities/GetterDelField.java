package com.revature.utilities;

import com.revature.annotations.GetterDel;
import com.revature.annotations.GetterInsert;

import java.lang.reflect.Method;

public class GetterDelField {

    private Method method;

    public GetterDelField(Method method) {
        this.method = method;
    }

    public String getName() {
        return method.getName();
    }

    public String getMethodName() {
        return method.getAnnotation(GetterDel.class).getterName();
    }
}
