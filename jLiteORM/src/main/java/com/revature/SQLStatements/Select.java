package com.revature.SQLStatements;

import com.revature.utilities.Metamodel;

public class Select {

    //String statementSel;

    public Select(){
        super();
    }


    /*
  Creates generic select statement with passed in table name
   */
    public static String getSQLStatementSelect(String tableName) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);
        return sb.toString();
    }
}
