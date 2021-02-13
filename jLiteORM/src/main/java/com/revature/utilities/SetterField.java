package com.revature.utilities;

import com.revature.annotations.Getter;
import com.revature.annotations.Setter;

import java.lang.reflect.Method;

public class SetterField {

    private Method method;

    public SetterField(Method method) {
        this.method = method;
    }

    public String getName() {
        return method.getName();
    }

    public String getMethodName() {
        return method.getAnnotation(Setter.class).setterName();
    }
}
