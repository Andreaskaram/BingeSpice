package com.example.bingespice_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BingespiceDBManager {
    public static void testdb() {
        System.out.println("testdb");
        try{
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://bingespicedb.cva6i4ugasuu.eu-north-1.rds.amazonaws.com:3306/bingespicedb",
                    "admin",
                    "bingedbpw"
            );
            System.out.println("✅ Connected to the database successfully.");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");

            while (rs.next()) {
                System.out.println("HellowWorld");
                System.out.println(rs.getString(1));
            }

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void signup(){

    }
}
