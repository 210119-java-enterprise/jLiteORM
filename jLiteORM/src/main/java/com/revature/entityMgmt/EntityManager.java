package com.revature.entityMgmt;

import com.revature.utilities.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {

    private List<Metamodel<Class<?>>> metamodelList;

    public EntityManager(){
        super();
    }

    public EntityManager(List<Metamodel<Class<?>>> listOfMM){

        this.metamodelList = listOfMM;
    }

    /*
    Takes in an object from the user and returns the appropriate metamodel
     */
    public Metamodel<?> getAppropriateMetamodel(Object o){

        for (Metamodel<?> metamodel : metamodelList) {

            //Maybe a comparison that does not include package info, prob fine?
            if(metamodel.getClassName()==o.getClass().getName()){
                System.out.println("Found matching metamodel for: "+ o.getClass().getName());
                return metamodel;
            }
        }
        System.out.println("No match");
        return null;
    }


    /*
    Takes in an object from the user and maps it to the correct DB table and insert it.
     */
    public void insert(Object obj){

        //Returns appropriate metamodel for object passed in
        Metamodel<?> metamodel = this.getAppropriateMetamodel(obj);

        //If no match method ends
        if(metamodel==null){
            System.out.println("No matching metamodel for object");
            return;
        }


        //Gets table name from metamodel/object
        String tableName = metamodel.getTable().getTableName();
        System.out.println("Name of the SQL table: " + tableName);

        //Gets a list of fields from metamodel/object
        //Requires two lists to get in string form
        List<ColumnField> columns = metamodel.getColumns();
        List<String> columnNames = new ArrayList<>();

        for(ColumnField cf:columns){
            columnNames.add(cf.getColumnName());
        }


        //Print all fields with stream
        System.out.print("Columns in the SQL table: ");
        System.out.println(columnNames.toString());
        //columnNames.forEach(System.out::println);

        //Gets the primary key, serial, so not so useful
        String primaryKey = metamodel.getPrimaryKey().getName();
        System.out.println("Name of the primary key: " + primaryKey);


        /*
        Map used for connecting wildcard location to column
         */
        Map<String,Integer> wcMap  = new HashMap<String,Integer>();
        Integer wildcarOrder = 0;//new Integer(1);

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

        //Calling the getObjectFieldValues, currently just prints the methods
        this.getObjectFieldValues(metamodel,obj);

        //Trying to get values from AppUser object


        String s = metamodel.getClassName();
        System.out.println(s);
        //Class<?>  aClass = metamodel.getClassName();


//        try {
//            Field field = aClass.getField("someField");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        //MyObject objectInstance = new MyObject();

        //Object value = field.get(objectInstance);

        //field.set(objetInstance, value);


        /*
        Pasted from save() method in bankingApp. Needs to be uncommented to fully work
        waiting on the implementation of a generic SQL insert to fully run it.
         */

        //  try (Connection conn = ConnectionFactory.getInstance().getConnection()) {


//            String sql = "INSERT INTO app_users (username, password, first_name, last_name)"+
//                    "VALUES (?, ?, ?, ?)";

//            PreparedStatement pstmt = conn.prepareStatement(sb.toString(), new String[] {primaryKey});
//
//            pstmt.setString(1, newObj.getUsername());
//            pstmt.setString(2, newObj.getPassword());
//            pstmt.setString(3, newObj.getFirstName());
//            pstmt.setString(4, newObj.getLastName());
//
//            int rowsInserted = pstmt.executeUpdate();
//
//            if (rowsInserted != 0) {
//                ResultSet rs = pstmt.getGeneratedKeys();
//                while (rs.next()) {
//                    newObj.setId(rs.getInt("user_id"));
//                }
//            }
//
//        }
//        catch(SQLException e){
//            System.out.println("Cannot connect to database, try again");
//            //e.printStackTrace();
//        }

    }

    public void getObjectFieldValues(Metamodel<?> mod,Object obj){

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
        //methNames.forEach(System.out::println);





    }



    /*
    The logic from the Driver class demo by Wez
     */
//    public void placeHolderMethod(){
//
//        for (Metamodel<?> metamodel : metamodelList) {
//
//            System.out.printf("Printing metamodel for class: %s\n", metamodel.getClassName());
//            IdField idField = metamodel.getPrimaryKey();
//            List<ColumnField> columnFields = metamodel.getColumns();
//            List<ForeignKeyField> foreignKeyFields = metamodel.getForeignKeys();
//            //Not sure about this type with generics
//            TableClass<TableClass> tableField = metamodel.getTable();
//            System.out.printf("\tFound a table field of type %s, which maps to the table name: %s \n", tableField.getName(), tableField.getTableName());
//
//            System.out.printf("\tFound a primary key field named %s of type %s, which maps to the column with the name: %s\n", idField.getName(), idField.getType(), idField.getColumnName());
//
//            for (ColumnField columnField : columnFields) {
//                System.out.printf("\tFound a column field named: %s of type %s, which maps to the column with the name: %s\n", columnField.getName(), columnField.getType(), columnField.getColumnName());
//            }
//
//            for (ForeignKeyField foreignKeyField : foreignKeyFields) {
//                System.out.printf("\tFound a foreign key field named %s of type %s, which maps to the column with the name: %s\n", foreignKeyField.getName(), foreignKeyField.getType(), foreignKeyField.getColumnName());
//            }
//
//            System.out.println();
//        }
//    }

}
