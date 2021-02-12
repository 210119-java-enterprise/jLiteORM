package com.revature.utilities;


import java.util.ArrayList;
import java.util.List;

public class SQLStatementBuilder {

    public static void main(String[] args) {

        /*
        Need a more generic sql statement, with flexible number of ?s and flexible variable inserts
        Something along the lines of
         */

        List<String> columnNames = new ArrayList<>();
        columnNames.add("user_name");
        columnNames.add("password");
        columnNames.add("first_name");
        columnNames.add("last_name");

        String tName = "app_user";

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tName);
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
        Next thing to build
         */




//        pstmt.setString(1, newObj.getUsername());
//        pstmt.setString(2, newObj.getPassword());
//        pstmt.setString(3, newObj.getFirstName());
//        pstmt.setString(4, newObj.getLastName());

    }


}
