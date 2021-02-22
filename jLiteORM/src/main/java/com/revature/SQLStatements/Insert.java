package com.revature.SQLStatements;

import java.util.List;

/**
 * Contains static methods for assisting in creation of SQL insert statement.
 */
public class Insert {

    public Insert(){
        super();
    }


    /**
     * Method for creating the SQL insert statement.
     * @param tableName The table to select from.
     * @param columnNames  The columns we want to insert into.
     * @return Returns the SQL statement.
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
