package com.revature.repos;

import com.revature.utilities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

public class RepoHelper {

    public RepoHelper(Metamodel<?> metamodel, Object obj){

    }

     /*
  Current approach is to get a string list of setter methods names that shares alpha order
  with a list of columns.  Iterating between the two should sync pulling object values
  from DB and setting object values
  */

    public static List<Object> mapResultSet(Metamodel<?> metamodel, ResultSet rs, ResultSetMetaData metaData, Object obj) {

        List<Object> objList = new LinkedList<>();
        // Get columns alphabetized
        List<String> listColumnsString = getColumnsAsSortedStringList(metamodel);
        // Get setter methods
        List<SetterField> listSetterMethods = metamodel.getSetter();
        List<String> listSetterMethodsString = new ArrayList<>();

        // SetterFields to setter method strings
        for (SetterField s : listSetterMethods) {
            listSetterMethodsString.add(s.getMethodName());
        }

        // Sorting to sync with columns order
        Collections.sort(listSetterMethodsString);
        // System.out.println("Sorted setter methods: " +listSetterMethodsString);
        int count = 0;

        try {
            while (rs.next()) {
                count = 0;
                Object newObject = obj.getClass().getConstructor().newInstance();
                for (String s : listColumnsString) {
                    // System.out.println("\nColumn in for loop: "+s);
                    Object objectValue = rs.getObject(s);
                    // System.out.println("Object value from SQL table: "+objectValue);
                    Class<?> type = metamodel.getColumnClass(s);
                    // System.out.println("Column Type: "+type);
                    // System.out.println("Setter method string name: "+listSetterMethodsString.get(count));
                    Method method = obj.getClass().getMethod(listSetterMethodsString.get(count), type);
                    method.invoke(newObject, objectValue);
                    count++;
                }
                objList.add(newObject);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return objList;
    }

    /*
  Helper method that returns a sorted list of columns alpha
   */
    public static List<String> getColumnsAsSortedStringList(Metamodel<?> metamodel) {

        List<ColumnField> columns = metamodel.getColumns();
        List<String> columnNames = new ArrayList<>();
        for (ColumnField cf : columns) {
            columnNames.add(cf.getColumnName());
        }
        Collections.sort(columnNames);
        return columnNames;
    }

    /*
    Helper method that returns a sorted list of columns alpha
     */
    public static List<String> getColumnsInsertAsSortedStringList(Metamodel<?> metamodel) {

        List<ColumnInsertField> columns = metamodel.getColumnsInsert();
        List<String> columnNames = new ArrayList<>();
        // Change
        for (ColumnInsertField cf : columns) {
            columnNames.add(cf.getColumnName());
        }
        Collections.sort(columnNames);
        // Is this empty
        return columnNames;
    }



     /*
  Get the objects values and returns them in a map where the key is the getter method
  name that returned the value and the value is the value returned from calling the getter
  method
   */

    public static Map<String, Object> getObjectFieldValuesAll(Metamodel<?> mod, Object obj) {

        // Gets a list of getter methods from metamodel/object
        // Think if I make a separate GetterFieldInsert
        List<GetterField> m = mod.getGetters();
        List<String> methNames = new ArrayList<>();

        // Puts the String version of method names in array
        for (GetterField gf : m) {
            methNames.add(gf.getMethodName());
        }
        // Object map for storing object values and the getter methods they came from
        Map<String, Object> objVals = new HashMap<>();
        Method method = null;
        try {
      /*
      Generic way to pass obj run and through all the get methods of the object
      adding their values + the method's name the values came from into a map
       */
            // Loop with all the getter method annotations
            for (String getterName : methNames) {
                // Gets a getter method
                method = mod.getClazz().getMethod(getterName);

                try {
                    // Invokes getter method on our user passed in object
                    Object returnValue = method.invoke(obj);
                    // Adds the getter value and the method it came from to our map
                    objVals.put(getterName, returnValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // Printing the map for verification
        System.out.println(objVals.toString());
        return objVals;
    }

     /*
  Get the objects values and returns them in a map where the key is the getter method
  name that returned the value and the value is the value returned from calling the getter
  method
   */
    public static Map<String, Object> getObjectFieldValues(Metamodel<?> mod, Object obj) {

        // Gets a list of getter methods from metamodel/object
        // Think if I make a separate GetterFieldInsert
        List<GetterInsertField> m = mod.getGettersInsert();
        List<String> methNames = new ArrayList<>();

        // Puts the String version of method names in array
        for (GetterInsertField gf : m) {
            methNames.add(gf.getMethodName());
        }
        // Object map for storing object values and the getter methods they came from
        Map<String, Object> objVals = new HashMap<>();
        Method method = null;
        try {
      /*
      Generic way to pass obj run and through all the get methods of the object
      adding their values + the method's name the values came from into a map
       */
            // Loop with all the getter method annotations
            for (String getterName : methNames) {
                // Gets a getter method
                method = mod.getClazz().getMethod(getterName);

                try {
                    // Invokes getter method on our user passed in object
                    Object returnValue = method.invoke(obj);
                    // Adds the getter value and the method it came from to our map
                    objVals.put(getterName, returnValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // Printing the map for verification
        System.out.println(objVals.toString());
        return objVals;
    }

    /*
  Helper method that returns a  sorted LinkedHashMap map using streams, used in insert() method
   */
    public static Map<String, Object> getMapSortedByKeys(Map<String, Object> objValues) {

        Map<String, Object> sorted =
                objValues.entrySet().stream()
                        .sorted(comparingByKey())
                        .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        return sorted;
    }


}
