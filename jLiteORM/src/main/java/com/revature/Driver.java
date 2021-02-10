package com.revature;
import com.revature.reflection.ClassInspector;

/*
Driver is run and an instance of ClassInspector is made, so static block executes
and the reflection method executes
 */
public class Driver {

    public static void main(String[] args) {
        ClassInspector c = new ClassInspector();
        System.out.println("Nothing");
    }
}
