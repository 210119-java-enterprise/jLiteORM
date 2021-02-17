package com.revature.repos;

import com.revature.annotations.SetterId;
import com.revature.testModels.AppUser;
import com.revature.utilities.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

/*
This class contains the CRUD methods that interact directly with the DB. It is not directly
exposed to the user, but indirectly through the Session class's methods.
 */

public class CRUD {

    private Connection conn;

    public CRUD(Connection conn){
        this.conn = conn;
    }


    /*
    Method inserts an object into the DB and also sets the objects id afterwards
     */
    public void insert(Metamodel<?> metamodel, Object obj){

        /*
        Troubleshooting, insert is trying to assign the user_id which is serial
        Need to prevent user_id from being added by making a more specific annotation
         */
        //Gets table name from metamodel/object.
        String tableName = metamodel.getTable().getTableName();
        //Get columns and sorted
        List<String> columnNames = getColumnsInsertAsSortedStringList(metamodel);
        //Gets the primary key
        String primaryKey = metamodel.getPrimaryKey().getColumnName();
        //Returns map of object values and method called for retrieval
        //Problem in getObjectFieldValues(metamodel,obj)
        Map<String,Object> objValues = this.getObjectFieldValues(metamodel,obj);
        Map<String, Object> sortedMap = this.getMapSortedByKeys(objValues);
        String sqlString = this.getSQLStatementInsert(tableName, columnNames);


        try  {
            //Statement generic except takes primary key variable
            PreparedStatement pstmt = conn.prepareStatement(sqlString, new String[] {primaryKey});
            /*
            Key is getter method name which corresponds to column name in table.
            Value is object's value obtained from getter method. Works because alphabetized
            and there is a correlation between column names and the getter method names
             */
            int wildCardSpot = 1;
            for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
                //Changed this if statement, was getId, I dont think it matters, i hope
                if(entry.getKey()=="getUserId"){
                    continue;
                }
                pstmt.setObject(wildCardSpot, entry.getValue());
                wildCardSpot+=1;
            }
            int rowsInserted = pstmt.executeUpdate();
            /*
            This part is used to give the id value to the user object because it won't
            have one until DB has assigned it.
             */
            if (rowsInserted != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                while (rs.next()) {

                    SetterIdField m = metamodel.getSetterId();
                    String methName = m.getMethodName();
                    Method method = null;
                    int id = rs.getInt(primaryKey);

                    try {
                        /*
                        Need to redo this part, because I am hand inputting the arg type
                        Should use ResultSetMetaData
                         */
                        Class[] arg = new Class[1];
                        arg[0] = int.class;
                        method = metamodel.getClazz().getMethod(methName,arg);
                        method.invoke(obj,id);

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println("Cannot connect to database, try again");
            e.printStackTrace();
        }

    }

    /*
    Fully implemented
     */
    public List<?> select (Metamodel<?> metamodel, Object obj){

        //Holds the objects to be returned
        List<Object> objList = new LinkedList<>();
        //Gets the table name of passed object
        String tableName = metamodel.getTable().getTableName();
        //Returns StringBuilder generated SQL statement
        String sqlString = getSQLStatementSelect(tableName);

        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);
            ResultSetMetaData md = rs.getMetaData();
            objList = mapResultSet(metamodel,rs,md,obj);
            return objList;

        } catch (SQLException e) {
            System.out.println("Cannot connect to database, try again");
            //e.printStackTrace();
        }

        return objList;
    }


    /*
    Method for getting rows that only include specified columns
    Not implemented
     */
    public List<?> selectSome(Metamodel<?> metamodel, Object obj, String[] theColumns){
        System.out.println("Not implemented");
        List<Object> objList = new LinkedList<>();

        return objList;
    }

    /*
    Not implemented
     */
    public void update(Metamodel<?> metamodel, Object objBefore, Object objAfter){

        System.out.println("Not implemented");

    }

    /*
    Not implemented
     */
    public void delete(Metamodel<?> metamodel, Object obj){

        String tableName = metamodel.getTable().getTableName();
        String primaryKey = metamodel.getPrimaryKey().getColumnName();
        this.getSQLStatementDelete(metamodel,tableName,obj);



        System.out.println("Deleted");




    }

    /*
      Need to invoke the getUserId method on the object, this method is annotated
      by GetterDel.  Should be able to do get most of this from previously written
      code
       */
    //Should return string
    public void getSQLStatementDelete(Metamodel<?> metamodel, String tableName, Object obj){



        //delete from app_users where username ='user6'
        String columnName = "";
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName);
        sb.append( " WHERE ");
        sb.append(columnName);
        sb.append("=");
        sb.append("?");



        Map<String,Object> m = getObjectFieldValuesAll(metamodel,obj);
        String primaryKeyVal = "";
        for (Map.Entry<String, Object> entry : m.entrySet()) {
            if(entry.getKey()=="getUserId"){
                primaryKeyVal = entry.getValue().toString();
            }

        }
        System.out.println("Object values: "+m);
        System.out.println("primary key: "+primaryKeyVal);








    }


    /*
    Helper method that returns a  sorted LinkedHashMap map using streams, used in insert() method
     */
    public Map<String, Object> getMapSortedByKeys(Map<String,Object> objValues){

        Map<String, Object> sorted = objValues
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        return sorted;
    }

    /*
   StringBuilder for the SQL insert statement
   Currently, we are making an insert statement with too many columns, need
   to cut this off before arrives into this method
    */
    public String getSQLStatementInsert(String tableName, List<String> columnNames){

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append( " (");
        for (int i = 0; i < columnNames.size(); i++) {

            if(i==columnNames.size()-1){
                sb.append(columnNames.get(i)+")");
                //wcMap.put(columnNames.get(i), ++wildcarOrder);
                break;
            }
            sb.append(columnNames.get(i)+", ");
            //wcMap.put(columnNames.get(i), ++wildcarOrder);
        }
        sb.append( " VALUES ");
        sb.append("(");
        for (int i = 0; i < columnNames.size(); i++){

            if(i==columnNames.size()-1){
                sb.append("?)");
                break;
            }
            sb.append("?, ");
        }
        return sb.toString();

    }

    /*
    Creates generic select statement with passed in table name
     */
    public String getSQLStatementSelect(String tableName){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);
        return sb.toString();
    }

    /*
    Helper method that returns a sorted list of columns alpha
     */
    public List<String> getColumnsAsSortedStringList(Metamodel<?> metamodel){

        List<ColumnField> columns = metamodel.getColumns();
        List<String> columnNames = new ArrayList<>();
        for(ColumnField cf:columns){
            columnNames.add(cf.getColumnName());
        }
        Collections.sort(columnNames);
        return columnNames;
    }

    /*
Helper method that returns a sorted list of columns alpha
 */
    public List<String> getColumnsInsertAsSortedStringList(Metamodel<?> metamodel){

        List<ColumnInsertField> columns = metamodel.getColumnsInsert();
        List<String> columnNames = new ArrayList<>();
        //Change
        for(ColumnInsertField cf:columns){
            columnNames.add(cf.getColumnName());
        }
        Collections.sort(columnNames);
        //Is this empty
        return columnNames;
    }

       /*
         Current approach is to get a string list of setter methods names that shares alpha order
         with a list of columns.  Iterating between the two should sync pulling object values
         from DB and setting object values
         */

    public List<Object> mapResultSet(Metamodel<?> metamodel, ResultSet rs, ResultSetMetaData metaData, Object obj){

        List<Object> objList = new LinkedList<>();
        //Get columns alphabetized
        List<String> listColumnsString = getColumnsAsSortedStringList(metamodel);
        //Get setter methods
        List<SetterField> listSetterMethods = metamodel.getSetter();
        List<String> listSetterMethodsString = new ArrayList<>();

        //SetterFields to setter method strings
        for(SetterField s:listSetterMethods){
            listSetterMethodsString.add(s.getMethodName());
        }

        //Sorting to sync with columns order
        Collections.sort(listSetterMethodsString);
        //System.out.println("Sorted setter methods: " +listSetterMethodsString);
        int count = 0;

        try {
            while(rs.next()) {
                count=0;
                Object newObject = obj.getClass().getConstructor().newInstance();
                for(String s:listColumnsString){
                    //System.out.println("\nColumn in for loop: "+s);
                    Object objectValue = rs.getObject(s);
                    //System.out.println("Object value from SQL table: "+objectValue);
                    Class<?> type = metamodel.getColumnClass(s);
                    //System.out.println("Column Type: "+type);
                    //System.out.println("Setter method string name: "+listSetterMethodsString.get(count));
                    Method method = obj.getClass().getMethod(listSetterMethodsString.get(count),type);
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
    Get the objects values and returns them in a map where the key is the getter method
    name that returned the value and the value is the value returned from calling the getter
    method
     */
    public Map<String,Object> getObjectFieldValues(Metamodel<?> mod,Object obj){

        //Gets a list of getter methods from metamodel/object
        //Think if I make a separate GetterFieldInsert
        List<GetterInsertField> m = mod.getGettersInsert();
        List<String> methNames = new ArrayList<>();

        //Puts the String version of method names in array
        for(GetterInsertField gf:m){
            methNames.add(gf.getMethodName());
        }
        //Object map for storing object values and the getter methods they came from
        Map<String,Object> objVals = new HashMap<>();
        Method method = null;
        try {
            /*
            Generic way to pass obj run and through all the get methods of the object
            adding their values + the method's name the values came from into a map
             */
            //Loop with all the getter method annotations
            for(String getterName: methNames){
                //Gets a getter method
                method = mod.getClazz().getMethod(getterName);

                try {
                    //Invokes getter method on our user passed in object
                    Object returnValue = method.invoke(obj);
                    //Adds the getter value and the method it came from to our map
                    objVals.put(getterName,returnValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Printing the map for verification
        System.out.println(objVals.toString());
        return objVals;
    }

    public Map<String,Object> getObjectFieldValuesAll(Metamodel<?> mod,Object obj){

        //Gets a list of getter methods from metamodel/object
        //Think if I make a separate GetterFieldInsert
        List<GetterField> m = mod.getGetters();
        List<String> methNames = new ArrayList<>();

        //Puts the String version of method names in array
        for(GetterField gf:m){
            methNames.add(gf.getMethodName());
        }
        //Object map for storing object values and the getter methods they came from
        Map<String,Object> objVals = new HashMap<>();
        Method method = null;
        try {
            /*
            Generic way to pass obj run and through all the get methods of the object
            adding their values + the method's name the values came from into a map
             */
            //Loop with all the getter method annotations
            for(String getterName: methNames){
                //Gets a getter method
                method = mod.getClazz().getMethod(getterName);

                try {
                    //Invokes getter method on our user passed in object
                    Object returnValue = method.invoke(obj);
                    //Adds the getter value and the method it came from to our map
                    objVals.put(getterName,returnValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Printing the map for verification
        System.out.println(objVals.toString());
        return objVals;
    }
}
