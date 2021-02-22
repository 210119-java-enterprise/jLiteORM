package com.revature.SQLStatements;

import com.revature.utilities.Metamodel;

import java.util.ArrayList;

/**
 * Contains static methods for assisting in creation of SQL select statement.
 */
public class Select {

    public Select(){
        super();
    }


    /**
     * For getting the specified selectFrom columns into an ArrayList of strings.
     * @param metamodel The instance that is used to scrape a POJO.
     * @param colNames Specified columns to be selected.
     * @return Returns list of Strings containing selected column names.
     */
    public static ArrayList<String> selectFrom(Metamodel<?> metamodel, String... colNames){

        // Gets the table name of passed object through class's Table annotation
        String tableName = metamodel.getTable().getTableName();
        //Makes an array list for column String names
        ArrayList<String> listCols = new ArrayList<>();
        //Iterates through the passed in list of Strings containing column names
        for(String s : colNames){
            //Adds those passed in column names to the array list
            listCols.add(s);
        }
        //Returns the String array list of passed column names
        return listCols;
    }

    /**
     * Generates the select from SQL statement.
     * @param tableName Table to select from.
     * @param tableColumns Specified columns to select from.
     * @return Returns the SQL statement as a String.
     */
    public static String getSQLStatementSelectFrom(String tableName, ArrayList<String> tableColumns){
        String sql;
        //Based on user passed list of columns
        int bound = tableColumns.size();
        //For building the SQL statement
        StringBuilder selectKeyword = new StringBuilder("SELECT ");
        //Iterate through bound - number of passed columns
        for(int i = 0; i < bound; i++){

            //For the last item
            if(i == (bound-1)){
                //If we are at the last of our passed columns, add a space to the end
                selectKeyword.append(tableColumns.get(i)).append(" ");
            }
            //For any item except the last
            else {
                //If we are not at the last of our passed columns, add a comma to the end
                selectKeyword.append(tableColumns.get(i)).append(", ");
            }
        }

        //Concatenation of SELECT and the desired columns with FROM and the table name
        sql = selectKeyword.toString() + "FROM" + " " + tableName;
    //System.out.println("This is the select from statement: "+sql);
        //Return finished SQL select from statement
        return sql;
    }

    /**
     * Generates the SQL select * statement.
     * @param tableName Table to be selected from.
     * @return SQL statement as a String.
     */
    public static String getSQLStatementSelect(String tableName) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);
        return sb.toString();
    }
}
