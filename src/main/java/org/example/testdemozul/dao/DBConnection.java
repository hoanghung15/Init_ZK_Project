package org.example.testdemozul.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/identity_service";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Mỗi lần gọi sẽ trả về một Connection mới
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối MySQL thành công: " + conn);
            } else {
                System.out.println("❌ Kết nối thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
