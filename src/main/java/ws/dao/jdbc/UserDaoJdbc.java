package ws.dao.jdbc;

import org.apache.log4j.Logger;
import ws.dao.LotDao;
import ws.dao.UserDao;
import ws.model.Lot;
import ws.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoJdbc.class);
    private final Connection connection;
    private LotDao lotDao;

    public UserDaoJdbc() {
        this(new LotDaoJdbc());
    }

    public UserDaoJdbc(LotDao lotDao) {
        this.lotDao = lotDao;
        connection = DaoUtils.getConnection();
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into Users(Login, Password,FirstName,LastName) values (?, ?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword()); //todo encrypt??
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            // e.printStackTrace();
            log.error("catch exception:", e);
        }
        return false;

    }

    @Override
    public boolean delete(int userId) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from users where id=?");
            // Parameters start with 1
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return false;
    }

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

    @Override
    public List<User> findAll() {
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

    @Override
    public User findById(int userId) {
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

    @Override
    public User findByIdWithLots(int id) {
        User user = findById(id);
        List<Lot> lots = lotDao.findAllByUser(user);
        user.setLots(lots);
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

    @Override
    public User findByLogin(String userLogin) {
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
