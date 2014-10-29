package WebService.db;

import WebService.Domain.Lot;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olga on 22.09.2014.
 */
public class LotDAO {
    private Connection connection;
    private static final Logger log = Logger.getLogger(LotDAO.class);
    public LotDAO() {
        connection = DBUtils.getConnection();
    }




    public boolean addLot(Lot lot) {//String name, Date finishDate, float startPrice, String description, int ownerId, String state
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into Lots(name, finishDate,startPrice,description,ownerId) values (?, ?, ?, ?,? )");
            // Parameters start with 1
            preparedStatement.setString(1, lot.getName());
            preparedStatement.setDate(2, new java.sql.Date(lot.getFinishDate().getTime()));
            preparedStatement.setDouble(3, lot.getStartPrice());
            preparedStatement.setString(4, lot.getDescription());
            preparedStatement.setInt(5, lot.getOwnerId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
           // e.printStackTrace();
            log.error("catch exception:", e);
        }
        return result;
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
//    public void updateLot(Lot lot) {
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("update lots set name=?, finishDate=?,startPrice=?,description=?,ownerId=?,state=?" +
//                            "where Id=?");
//            // Parameters start with 1
//            preparedStatement.setString(1, lot.getName());
//            preparedStatement.setDate(2, lot.getFinishDate());
//            preparedStatement.setDouble(3, lot.getStartPrice());
//            preparedStatement.setString(4, lot.getDescription());
//            preparedStatement.setInt(5, lot.getOwnerId());
//            preparedStatement.setString(6, lot.getState());
//            //id
//            preparedStatement.setInt(7, lot.getId());
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            log.error("catch exception:", e);
//        }
//    }

    public boolean updateLotState(Lot lot) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update lots set state=? where Id=?");
            preparedStatement.setString(1, lot.getName());
            preparedStatement.setInt(2, lot.getId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return result;
    }

//    public List<Lot> getAllLots() {
//        List<Lot> lots = new ArrayList<Lot>();
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery("select * from lots");
//            while (rs.next()) {
//                Lot lot = new Lot();
//                lot.setId(rs.getInt("id"));
//                lot.setName(rs.getString("name"));
//                lot.setFinishDate(rs.getDate("finishDate"));
//                lot.setStartPrice(rs.getDouble("startPrice"));
//                lot.setDescription(rs.getString("description"));
//                lot.setOwnerId(rs.getInt("ownerId"));
//                lot.setState(rs.getString("state"));
//                lots.add(lot);
//            }
//        } catch (SQLException e) {
//            log.error("catch exception:", e);
//        }
//
//        return lots;
//    }

    public List<Lot> getAllLotsForOwner(int ownerId) {
        List<Lot> lots = new ArrayList<Lot>();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from lots where ownerId=?");
            preparedStatement.setInt(1, ownerId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Lot lot = new Lot();
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setFinishDate(new java.util.Date(rs.getDate("finishDate").getTime()));
                lot.setStartPrice(rs.getDouble("startPrice"));
                lot.setDescription(rs.getString("description"));
                lot.setOwnerId(rs.getInt("ownerId"));
                lot.setState(rs.getString("state"));
                lots.add(lot);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return lots;
    }


    public Lot getLotById(int lotId) {
        Lot lot = new Lot();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from lots where id=?");
            preparedStatement.setInt(1, lotId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setFinishDate(new java.util.Date(rs.getDate("finishDate").getTime()));
                lot.setStartPrice(rs.getDouble("startPrice"));
                lot.setDescription(rs.getString("description"));
                lot.setOwnerId(rs.getInt("ownerId"));
                lot.setState(rs.getString("state"));
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return lot;
    }
}
