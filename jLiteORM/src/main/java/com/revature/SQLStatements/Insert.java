package com.revature.SQLStatements;

import java.util.List;

//****Done****
public class Insert {

    public Insert(){
        super();
    }

 /*
  StringBuilder for the SQL insert statement
  Currently, we are making an insert statement with too many columns, need
  to cut this off before arrives into this method
   */


    public static String getSQLStatementInsert(String tableName, List<String> columnNames) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append(" (");
        for (int i = 0; i < columnNames.size(); i++) {

            if (i == columnNames.size() - 1) {
                sb.append(columnNames.get(i) + ")");
                break;
            }
            sb.append(columnNames.get(i) + ", ");
        }
        sb.append(" VALUES ");
        sb.append("(");
        for (int i = 0; i < columnNames.size(); i++) {

            if (i == columnNames.size() - 1) {
                sb.append("?)");
                break;
            }
            sb.append("?, ");
        }
        return sb.toString();
    }
}
