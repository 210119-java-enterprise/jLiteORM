package com.revature.repos;

import com.revature.SQLStatements.Delete;
import com.revature.SQLStatements.Insert;
import com.revature.SQLStatements.Select;
import com.revature.SQLStatements.Update;
import com.revature.annotations.Column;
import com.revature.utilities.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class CRUD {

  private Connection conn;

  public CRUD(Connection conn) {
    this.conn = conn;
  }

  /**
   * @param metamodel
   * @param obj
   */

  // *****Done****
  public void insert(Metamodel<?> metamodel, Object obj) {

    // Gets the table name of passed object through class annotation
    String tableName = metamodel.getTable().getTableName();
    /*
    Get all the values that the object holds, with the exception of serial values
    Represents the information a user would hand to us to make a new table entry
     */
    ArrayList<Object> objectValues = RepoHelper.getObjectValues(obj);
    // Get all the fields in the object for iteration purposes
    Field[] fields = obj.getClass().getDeclaredFields();
    // List to hold all string names of columns
    List<String> columnNames = new ArrayList<>();
    // Iteration over all fields
    for (Field field : fields) {
      // Making all fields visible
      field.setAccessible(true);
      // If the field is annotated as a Column, we grab the annotation object
      Column column = field.getAnnotation(Column.class);
      // If annotation exists for this field
      if (column != null) {
        try {
          // If annotation exists for this field, we add the String name of the column to running
          // list
          columnNames.add(field.getAnnotation(Column.class).columnName());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    // Pass the list of String names for each assignable column to our SQL statement builder
    String sql = Insert.getSQLStatementInsert(tableName, columnNames);

    try {
      // Changing to new connection each time
      Connection conn = ConnectionFactory.getInstance().getConnection();
      // Pass generated SQL statement
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Iterate on number of values object has to assign
      for (int i = 0; i < objectValues.size(); i++) {
        /*
        Assign the object value to the SQL statement wild card. Everything syncs up
        because the order was determined by iterating through fields. Indexing is 1-based
         */
        pstmt.setObject(i + 1, objectValues.get(i));
      }
      // Once all wild cards are set with values we execute the SQL statement
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param metamodel
   * @param obj
   * @return
   */
  public List<?> select(Metamodel<?> metamodel, Object obj) {

    // Holds the objects to be returned
    List<Object> objList = new LinkedList<>();
    // Gets the table name of passed object through class annotation
    String tableName = metamodel.getTable().getTableName();
    // Returns StringBuilder generated SQL statement from Select class
    String sqlString = Select.getSQLStatementSelect(tableName);

    try {
      // Get a connection
      Connection conn = ConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      // Execute our select * statement
      ResultSet rs = stmt.executeQuery(sqlString);
      // Gets metadata
      ResultSetMetaData md = rs.getMetaData();
      /*
      Maps all the rows from our ResultSet to individual objects using reflection and returns a list
      of value populated objects
       */
      objList = RepoHelper.mapResultSet(rs, md, obj, metamodel);
      // Returns a list of value populated objects
      return objList;

    } catch (SQLException e) {
      System.out.println("Cannot connect to database, try again");
      // e.printStackTrace();
    }
    // Returns a list of value populated objects
    return objList;
  }

  /**
   * @param metamodel
   * @param obj
   * @param theColumns
   * @return
   */
  public List<?> selectSome(Metamodel<?> metamodel, Object obj, String[] theColumns) {

    // Gets the table name of passed object through class's Table annotation
    String tableName = metamodel.getTable().getTableName();
    // Gets the lists of column names as Strings that were passed by the user
    ArrayList<String> listCols = Select.selectFrom(metamodel, theColumns);
    // Generates our SQL statement with passing table name and the user passed columns
    String sql = Select.getSQLStatementSelectFrom(tableName, listCols);
    // Object list to hold all the rows from our query
    List<Object> listOfObjects = new ArrayList<>();

    try {
      // Changing to new connection each time
      Connection conn = ConnectionFactory.getInstance().getConnection();
      // Pass generated SQL statement
      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      ResultSetMetaData rsmd = rs.getMetaData();
      /*
      Maps all the rows from our ResultSet to individual objects using reflection and returns a list
      of value populated objects
       */
      listOfObjects = RepoHelper.mapResultSet(rs, rsmd, obj, metamodel);
      // Close connection
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Returns a list of value populated objects
    return listOfObjects;
  }
  /**
   * @param metamodel
   * @param objAfter
   * @param objBefore
   */
  public void update(Metamodel<?> metamodel, Object objAfter, Object objBefore) {

    // Gets the table name of passed object through class's Table annotation
    String tableName = metamodel.getTable().getTableName();
    // Gets all the columns names from the passed object
    ArrayList<String> tableColumns = Update.getTableCols(metamodel, objAfter);
    // Generates SQL update statement from the table name and column names
    String sql = Update.getSQLStatementUpdate(tableName, tableColumns);
    // System.out.println("SQL: "+sql);
    // Returns all the object's field values as list of objects
    ArrayList<Object> oldObjVal = RepoHelper.getObjectValues(objBefore);
    ArrayList<Object> newObjVal = RepoHelper.getObjectValues(objAfter);

    // Size of either of the objects value count is fine
    int size = oldObjVal.size();

    try {

      Connection conn = ConnectionFactory.getInstance().getConnection();
      // Pass SQL statement
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // Iterate on size of number of fields contained in an object
      for (int i = 0; i < size; i++) {

        /*
        Indexing is 1-based!  In first line below we are setting the new values
        for the new object(SET column = wildcard...).  In the second line we are
        setting the (WHERE column = wildcard values...) with the old object values.
        This allows us to update the correct entry in the table
         */
        pstmt.setObject(i + 1, newObjVal.get(i));
        pstmt.setObject(i + size + 1, oldObjVal.get(i));
      }
      // Execute the SQL statement
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  /**
   * @param metamodel
   * @param obj
   */
  public void delete(Metamodel<?> metamodel, Object obj) {

    // Gets the table name of passed object through class's Table annotation
    String tableName = metamodel.getTable().getTableName();
    // Generate the SQL statement
    String sql = Delete.getSQLStatementDelete(metamodel, tableName, obj);
    // System.out.println(sql);
    // Return the id value used in Where
    int idValue = Delete.getFieldForDelete(obj);

    try {
      // Changing to new connection each time
      Connection conn = ConnectionFactory.getInstance().getConnection();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, idValue);
      pstmt.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
