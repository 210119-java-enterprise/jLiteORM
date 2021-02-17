package com.revature.repos;

import com.revature.SQLStatements.Delete;
import com.revature.SQLStatements.Insert;
import com.revature.SQLStatements.Select;
import com.revature.utilities.*;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;


/*
This class contains the CRUD methods that interact directly with the DB. It is not directly
exposed to the user, only indirectly through the Session class's methods.
 */

public class CRUD {

  private Connection conn;

  public CRUD(Connection conn) {
    this.conn = conn;
  }

  /*
  Method inserts an object into the DB and also sets the objects id afterwards
   */
  public void insert(Metamodel<?> metamodel, Object obj) {


    // Get table name from metamodel.
    String tableName = metamodel.getTable().getTableName();
    // Get sorted columns
    List<String> columnNames = RepoHelper.getColumnsInsertAsSortedStringList(metamodel);
    // Get primary key
    String primaryKey = metamodel.getPrimaryKey().getColumnName();
    // Returns map of object values and method called for retrieval
    Map<String, Object> objValues = RepoHelper.getObjectFieldValues(metamodel, obj);
    Map<String, Object> sortedMap = RepoHelper.getMapSortedByKeys(objValues);
    String sqlString = Insert.getSQLStatementInsert(tableName, columnNames);

    try {
      // Statement generic except takes primary key variable
      PreparedStatement pstmt = conn.prepareStatement(sqlString, new String[] {primaryKey});
      /*
      Key is getter method name which corresponds to column name in table.
      Value is object's value obtained from getter method. Works because alphabetized
      and there is a correlation between column names and the getter method names
       */
      int wildCardSpot = 1;
      for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
        pstmt.setObject(wildCardSpot, entry.getValue());
        wildCardSpot += 1;
      }
      int rowsInserted = pstmt.executeUpdate();
      /*
      This part is used to give the id value to the user object because it won't
      have one until DB has assigned it. Could I do away with this part because in the
      banking app, user is required to login after creating new user account???
       */
      if (rowsInserted != 0) {
        ResultSet rs = pstmt.getGeneratedKeys();
        while (rs.next()) {

          SetterIdField m = metamodel.getSetterId();
          String methName = m.getMethodName();
          Method method = null;
          int id = rs.getInt(primaryKey);

          try {
            Class[] arg = new Class[1];
            arg[0] = int.class;
            method = metamodel.getClazz().getMethod(methName, arg);
            method.invoke(obj, id);

          } catch (NoSuchMethodException e) {
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (SQLException e) {
      System.out.println("Cannot connect to database, try again");
      e.printStackTrace();
    }
  }

  /*
  Fully implemented
   */
  public List<?> select(Metamodel<?> metamodel, Object obj) {

    // Holds the objects to be returned
    List<Object> objList = new LinkedList<>();
    // Gets the table name of passed object
    String tableName = metamodel.getTable().getTableName();
    // Returns StringBuilder generated SQL statement from Select class
    String sqlString = Select.getSQLStatementSelect(tableName);

    try {

      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sqlString);
      ResultSetMetaData md = rs.getMetaData();
      objList = RepoHelper.mapResultSet(metamodel, rs, md, obj);
      return objList;

    } catch (SQLException e) {
      System.out.println("Cannot connect to database, try again");
      // e.printStackTrace();
    }
    return objList;
  }

  /*
  Method for getting rows that only include specified columns. Not implemented
   */
  public List<?> selectSome(Metamodel<?> metamodel, Object obj, String[] theColumns) {
    System.out.println("Not implemented");
    List<Object> objList = new LinkedList<>();

    return objList;
  }

  /*
  Not implemented
   */
  public void update(Metamodel<?> metamodel, Object objBefore, Object objAfter) {

    System.out.println("Not implemented");
  }

 /*
 Implemented
  */
  public void delete(Metamodel<?> metamodel, Object obj) {


    String tableName = metamodel.getTable().getTableName();
    String sql = Delete.getSQLStatementDelete(metamodel, tableName, obj);
    System.out.println(sql);
    int idValue = Delete.getFieldForDelete(obj);

    try {
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, idValue);
      pstmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

}
