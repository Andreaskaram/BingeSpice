package com.example.bingespice_app;

import java.sql.*;

public class BingespiceDBManager {

    // DB Connection
    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://bingespicedb.cva6i4ugasuu.eu-north-1.rds.amazonaws.com:3306/bingespicedb",
                "admin",
                "bingedbpw"
        );
    }

    public static void testdb() {
        System.out.println("testdb");
        try{
            Connection conn = getConnection();
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

    public static String signup(String username, String firstName, String lastName, String email, String password, String gender, String country){
        String sql = "INSERT INTO User (Username, FirstName, LastName, Email, Password, Gender, Country) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, username);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, gender);
            stmt.setString(7, country);

            stmt.executeUpdate();
            System.out.println("✅ User registered successfully.");
            return null;

        }   catch (SQLIntegrityConstraintViolationException dupEx) {
            System.out.println("⚠️ Username already exists. Please choose another one.");
            return "Username already exists";
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            return "An unexpected SQL error occurred";
        } catch (Exception e) {
            e.printStackTrace();
            return "An unexpected error occurred";
        }
    }
}
