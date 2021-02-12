package com.revature.entityMgmt;

import com.revature.utilities.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public Metamodel<?> objectIntakeFromApp(Object o){

        for (Metamodel<?> metamodel : metamodelList) {

            if(metamodel.getClassName()==o.getClass().getName()){
                System.out.println("Matching class");
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
        Metamodel<?> metamodel = this.objectIntakeFromApp(obj);

        //If no match method ends
        if(metamodel==null){
            System.out.println("No matching metamodel for object");
            return;
        }

        //Gets table name from metamodel/object
        String tableName = metamodel.getTable().getTableName();
        System.out.println(tableName);

        //Gets a list of fields from metamodel/object
        //Requires two lists to get in string form
        List<ColumnField> columns = metamodel.getColumns();
        List<String> columnNames = new ArrayList<>();

        for(ColumnField cf:columns){
            columnNames.add(cf.getColumnName());
        }


        //Print all fields with stream
        columnNames.forEach(System.out::println);

        //Gets the primary key, serial, so not so useful
        String primaryKey = metamodel.getPrimaryKey().getName();
        System.out.println(primaryKey);


        /*
        Pasted from save() method in bankingApp. Needs to be uncommented to fully work
        waiting on the implementation of a generic SQL insert to fully run it. 
         */


  //      try (Connection conn = ConnectionFactory.getInstance().getConnection()) {


            StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(tableName);
            sb.append( " (");
            for (int i = 0; i < columnNames.size(); i++) {
                if(i==columnNames.size()-1){
                    sb.append(columnNames.get(i)+")");
                    break;
                }
                sb.append(columnNames.get(i)+", ");
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
            System.out.println(sb);

            /*
            Printing out public methods, a test
             */

        //Gets a list of methods from metamodel/object
        //Requires two lists to get in string form
        List<GetterField> meths = metamodel.getGetters();
        List<String> methNames = new ArrayList<>();

        for(GetterField gf:meths){
            methNames.add(gf.getMethodName());
        }


        //Print all fields with stream
        methNames.forEach(System.out::println);



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



    /*
    The logic from the Driver class demo by Wez
     */
    public void placeHolderMethod(){

        for (Metamodel<?> metamodel : metamodelList) {

            System.out.printf("Printing metamodel for class: %s\n", metamodel.getClassName());
            IdField idField = metamodel.getPrimaryKey();
            List<ColumnField> columnFields = metamodel.getColumns();
            List<ForeignKeyField> foreignKeyFields = metamodel.getForeignKeys();
            //Not sure about this type with generics
            TableClass<TableClass> tableField = metamodel.getTable();
            System.out.printf("\tFound a table field of type %s, which maps to the table name: %s \n", tableField.getName(), tableField.getTableName());

            System.out.printf("\tFound a primary key field named %s of type %s, which maps to the column with the name: %s\n", idField.getName(), idField.getType(), idField.getColumnName());

            for (ColumnField columnField : columnFields) {
                System.out.printf("\tFound a column field named: %s of type %s, which maps to the column with the name: %s\n", columnField.getName(), columnField.getType(), columnField.getColumnName());
            }

            for (ForeignKeyField foreignKeyField : foreignKeyFields) {
                System.out.printf("\tFound a foreign key field named %s of type %s, which maps to the column with the name: %s\n", foreignKeyField.getName(), foreignKeyField.getType(), foreignKeyField.getColumnName());
            }

            System.out.println();
        }


    }


}
