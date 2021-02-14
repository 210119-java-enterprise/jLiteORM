package com.revature.repos;

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

        //Gets table name from metamodel/object.
        String tableName = metamodel.getTable().getTableName();
        //Get columns and sorted
        List<String> columnNames = getColumnsAsSortedStringList(metamodel);
        //Gets the primary key
        String primaryKey = metamodel.getPrimaryKey().getColumnName();
        //Returns map of object values and method called for retrieval
        Map<String,Object> objValues = this.getObjectFieldValues(metamodel,obj);
        Map<String, Object> sortedMap = this.getMapSortedByKeys(objValues);
        String sqlString = this.getSQLStatementInsert(tableName, columnNames);

      //Cannot seem to use the existing connection that is an instance var in CRUD class
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            //Statement generic except takes primary key variable
            PreparedStatement pstmt = conn.prepareStatement(sqlString, new String[] {primaryKey});
            /*
            Key is getter method name which corresponds to column name in table.
            Value is object's value obtained from getter method. Works because alphabetized
            and there is a correlation between column names and the getter method names
             */
            int wildCardSpot = 1;
            for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
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

                    SetterField m = metamodel.getSetter();
                    String methName = m.getMethodName();
                    Method method = null;
                    int id = rs.getInt(primaryKey);

                    try {
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
    Not fully implemented
     */
    public List<?> select (Metamodel<?> metamodel, Object obj){

        List<Object> objList = new LinkedList<>();
        //Gets the table name of passed object
        String tableName = metamodel.getTable().getTableName();
        //Returns StringBuilder generated SQL statement
        String sqlString = getSQLStatementSelect(tableName);

        //Should reuse connections, original was not try with resources
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            //Generic
            Statement stmt = conn.createStatement();

            //SQL select * from passed table
            ResultSet rs = stmt.executeQuery(sqlString);
            //users = mapResultSet(rs);

            //Added, will need to use this metaData resource
            ResultSetMetaData md = rs.getMetaData();

            //Make generic, this mapping resultSetMetaData probably requires separate method
            /*
            Stopping here
             */
            while(rs.next()) {
                //Make generic
                System.out.println(rs.getString(1));
//                AppUser user = new AppUser();
//                user.setId(rs.getInt("user_id"));
//                user.setFirstName(rs.getString("first_name"));
//                user.setLastName(rs.getString("last_name"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                //Putting object into linked list
//                objList.insert(user);
            }

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

        System.out.println("Not implemented");

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
   StringBuilder build the SQL statement
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

        //Prints StringBuilder sql statement
        System.out.println("Generated SQL statement: "+ sb);
        return sb.toString();

    }

    /*
    Creates generic select statement with passed in table name
     */
    public String getSQLStatementSelect(String tableName){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);

        //Return generic select all with passed table name
        return sb.toString();

    }

    /*
    Helper method that returns a sorted list of columns
     */
    public List<String> getColumnsAsSortedStringList(Metamodel<?> metamodel){

        /*
         Gets a list of fields as String and sorted from metamodel/object.
         */
        List<ColumnField> columns = metamodel.getColumns();
        List<String> columnNames = new ArrayList<>();
        for(ColumnField cf:columns){
            columnNames.add(cf.getColumnName());
        }
        /*
        Sorts the columns alphabetically, combine with above to diff method
         */
        System.out.println("Before sorting: "+ columnNames);
        Collections.sort(columnNames);
        System.out.println("After sorting: "+columnNames);

        //Print all fields/columns. Part of above method
        System.out.print("Columns in the SQL table: ");
        System.out.println(columnNames.toString());

        return columnNames;

    }

    /*
    Get the objects values and returns them in a map where the key is the getter method
    name that returned the value and the value is the value returned from calling the getter
    method
     */
    public Map<String,Object> getObjectFieldValues(Metamodel<?> mod,Object obj){

        //Gets a list of getter methods from metamodel/object
        List<GetterField> m = mod.getGetters();
        List<String> methNames = new ArrayList<>();
        //Puts the String version of method names in array
        for(GetterField gf:m){
            methNames.add(gf.getMethodName());
        }

        //System.out.println(methNames.toString());

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
    public Connection getConn() {
        return conn;
    }

}
