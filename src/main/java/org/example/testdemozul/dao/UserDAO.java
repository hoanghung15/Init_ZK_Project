package org.example.testdemozul.dao;

import com.sun.tools.javac.Main;
import org.example.testdemozul.model.User;

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

    public boolean checkLogin(String username, String password) {
        String sql = "select * from user where username=? and password=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
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

        UserDAO userDAO = new UserDAO();
        System.out.println(userDAO.checkLogin("hoanghung15", "aaaaa"));
    }
}
