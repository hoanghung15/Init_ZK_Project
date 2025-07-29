package org.example.testdemozul.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/identity_service";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        if (getConnection() != null) {
            System.out.printf(getConnection().toString());
            System.out.println("Test: Kết nối hoạt động bình thường.");
        } else {
            System.out.println("Test: Kết nối thất bại.");
        }
    }

}
