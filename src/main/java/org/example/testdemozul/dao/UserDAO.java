package org.example.testdemozul.dao;

import com.sun.tools.javac.Main;
import org.example.testdemozul.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        String sql = "select * from user";

        try (Statement statement = DBConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUser(String username) {
        String sql = "select * from user where username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            User user = new User();
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user.setPassword(resultSet.getString("password"));
                    user.setUsername(username);
                    user.setId(resultSet.getInt("id"));
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkLogin(String username, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String sql = "SELECT password FROM user WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    return passwordEncoder.matches(password, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
        for (User user : getUsers()) {
            System.out.println(user.toString());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(passwordEncoder.encode("15122003"));
        UserDAO userDAO = new UserDAO();
        System.out.println(userDAO.checkLogin("hoanghung15", "15122003"));
//        getUser("hoanghung15");
    }
}
