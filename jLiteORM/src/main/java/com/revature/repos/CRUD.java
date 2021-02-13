package com.revature.repos;

import com.revature.utilities.ColumnField;
import com.revature.utilities.ConnectionFactory;
import com.revature.utilities.GetterField;
import com.revature.utilities.Metamodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public Connection getConn() {
        return conn;
    }

    /*
        This method needs to be majorly broken up, also the part where an object is
        updated with the serial type user_id field does not work
         */
    public void insert(Metamodel<?> metamodel, Object obj){

        /*
        Gets table name from metamodel/object. Separate into diff method
         */
        String tableName = metamodel.getTable().getTableName();
        System.out.println("Name of the SQL table: " + tableName);

        /*
         Gets a list of fields from metamodel/object. Requires two lists to
         get in string form. Separate into diff method
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
        //columnNames.forEach(System.out::println);


        //Gets the primary key column.  Make diff method
        String primaryKey = metamodel.getPrimaryKey().getColumnName();
        System.out.println("Name of the primary key: " + primaryKey);

        /*
        Below should be its own method for StringBuilding the SQL statement
         */
         /*
        Map used for connecting wildcard order location to column name
         */
        Map<String,Integer> wcMap  = new HashMap<String,Integer>();
        Integer wildcarOrder = 0;

        /*
        StringBuilder build the SQL statement
         */
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append( " (");
        for (int i = 0; i < columnNames.size(); i++) {
            if(i==columnNames.size()-1){
                sb.append(columnNames.get(i)+")");
                wcMap.put(columnNames.get(i), ++wildcarOrder);
                break;
            }
            sb.append(columnNames.get(i)+", ");
            wcMap.put(columnNames.get(i), ++wildcarOrder);
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


        //Prints the column names and their wildcard order)
        System.out.println("Columns and their wildcard positions: "+wcMap.toString());


        /*
        Different method for getting and sorting object values
         */
        /*
        Calling the getObjectFieldValues, method returns a map of <String,Objects>
        contains the name of the getter method they come from and the value
         */

        Map<String,Object> objValues = this.getObjectFieldValues(metamodel,obj);

        System.out.println(objValues.toString());

        Map<String, Object> sorted = objValues
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        System.out.println("map after sorting by keys: " + sorted);

        //Breaks if I change the name of the Getter annotation

        /*
        Move on to integrating those values into the SQL statement
         */

        /*
        Try using a connection from the current Session object
         */

        //May want to pull this connection instance from somewhere else, where it already exists

        //Cannot use the existing 'conn' instance variable in this class for connection
        //try (Connection conn = ConnectionFactory.getInstance().getConnection())
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = sb.toString();

//            String sql = "INSERT INTO app_users (username, password, first_name, last_name)"+
//                    "VALUES (?, ?, ?, ?)";

            //primaryKey variable goes in the end of the statement,
            // represents the column name of primary key
            PreparedStatement pstmt = conn.prepareStatement(sb.toString(), new String[] {primaryKey});

            //Key is column name, Value is wildcard order
            int count = 1;
            for (Map.Entry<String, Object> entry : sorted.entrySet()) {

                pstmt.setObject(count, entry.getValue());
                count+=1;

            }
//            pstmt.setString(1, newObj.getUsername());
//            pstmt.setString(2, newObj.getPassword());
//            pstmt.setString(3, newObj.getFirstName());
//            pstmt.setString(4, newObj.getLastName());

            int rowsInserted = pstmt.executeUpdate();


//            if (rowsInserted != 0) {
//                ResultSet rs = pstmt.getGeneratedKeys();
//                while (rs.next()) {
//                    //Need to access this through reflection
//
//                    Method method = null;
//                    SetterField setF = metamodel.getSetter();
//                    int id = rs.getInt(primaryKey);
//
//                    try {
//                        method = metamodel.getClazz().getMethod(setF.getMethodName());
//                        System.out.println("Printing out the method: " +method.toString());
//
//                        method.invoke(obj);
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    //obj.setId(rs.getInt(primaryKey));
//                }
//            }

        }
        catch(SQLException e){
            System.out.println("Cannot connect to database, try again");
            e.printStackTrace();
        }


    }

    public void update(Metamodel<?> metamodel, Object objBefore, Object objAfter){

        System.out.println("Not implemented");

    }

    public void delete(Metamodel<?> metamodel, Object obj){

        System.out.println("Not implemented");


    }

    public List<?> select (Metamodel<?> metamodel, Object obj){

        System.out.println("Not implemented");
        List<Object> objList = new LinkedList<>();


        return objList;
    }

    /*

    May need a more specific findAll() method
     */



    public Map<String,Object> getObjectFieldValues(Metamodel<?> mod,Object obj){

        //Gets a list of methods from metamodel/object
        //Requires two lists to get in string form
        List<GetterField> m = mod.getGetters();
        List<String> methNames = new ArrayList<>();
        for(GetterField gf:m){
            methNames.add(gf.getMethodName());
        }
        //Print all fields with stream
        System.out.print("The getter methods to access object values: ");
        System.out.println(methNames.toString());

        //Test to return the class object
        //String s = mod.getClazz().toString();
        //System.out.println(s);

        //Object map for storing object values and the getter methods they came from
        Map<String,Object> objVals = new HashMap<>();

        //Class  aClass = AppUser.class;
        //get method that takes a String as argument
        Method method = null;
        try {
            /*
            Generic way to pass obj and use its type also generic way to run through
            all the get methods of the object and add their values+getter methods they came
            from to a map
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
            //method = mod.getClazz().getMethod("getUsername");
            //method = AppUser.class.getMethod("getUsername");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println(objVals.toString());
        return objVals;
    }


}
