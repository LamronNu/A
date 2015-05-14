package WebService.dao;

import WebService.entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olga on 22.09.2014.
 */
public class UserDAO {
    private static final Logger log = Logger.getLogger(UserDAO.class);
    private Connection connection;

    public UserDAO() {
        connection = DBUtils.getConnection();
    }


    public void addUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into Users(Login, Password,FirstName,LastName) values (?, ?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword()); //todo encrypt??
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // e.printStackTrace();
            log.error("catch exception:", e);
        }

    }

//    public void deleteUser(int userId) {
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("delete from users where id=?");
//            // Parameters start with 1
//            preparedStatement.setInt(1, userId);
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            log.error("catch exception:", e);
//        }
//    }
//
//    public void updateUser(User user) {
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("update users set login=?, password=?, firstname=?, lastname=?" +
//                            "where userid=?");
//            // Parameters start with 1
//            preparedStatement.setString(1, user.getLogin());
//            preparedStatement.setString(2, user.getPassword()); //todo encrypt??
//            preparedStatement.setString(3, user.getFirstName());
//            preparedStatement.setString(4, user.getLastName());
//            preparedStatement.setInt(5, user.getId());
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            log.error("catch exception:", e);
//        }
//    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return users;
    }

    public User getUserById(int userId) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from users where id=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return user;
    }

    public User getUserByLotId(int lotId) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from users where id=(select ownerId from lots where id=?)");
            preparedStatement.setInt(1, lotId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return user;
    }

    public User getUserByLogin(String userLogin) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from users where login=?");
            preparedStatement.setString(1, userLogin);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return user;
    }
}
