package ws.dao.jdbc;

import org.apache.log4j.Logger;
import ws.dao.BidDao;
import ws.dao.LotDao;
import ws.dao.UserDao;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class LotDaoJdbc implements LotDao {
    private static final Logger log = Logger.getLogger(LotDaoJdbc.class);


    private Connection connection;
    private UserDao userDao;
    private BidDao bidDao;


    public LotDaoJdbc() {
        this.connection = DaoUtils.getConnection();
        userDao = new UserDaoJdbc(this);
        bidDao = new BidDaoJdbc();
    }

    @Override
    public boolean save(Lot lot) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into Lots(name, finishDate,startPrice,description,ownerId) values (?, ?, ?, ?,? )");
            // Parameters start with 1
            preparedStatement.setString(1, lot.getName());
            preparedStatement.setTimestamp(2, lot.getSqlFinishDate());
            preparedStatement.setDouble(3, lot.getStartPrice());
            preparedStatement.setString(4, lot.getDescription());
            preparedStatement.setInt(5, lot.getOwner().getId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            // e.printStackTrace();
            log.error("catch exception:", e);
        }
        return result;
    }

    @Override
    public Lot findByIdWithBids(int id) {
        Lot lot = findById(id);
        List<Bid> bids = bidDao.findAllByLot(lot);
        lot.setBids(bids);
        return lot;
    }

    @Override
    public boolean updateState(Lot lot) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update lots set state=? where Id=?");
            preparedStatement.setString(1, lot.getState());
            preparedStatement.setInt(2, lot.getId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return result;
    }


    @Override
    public List<Lot> findAllByUser(User user) {
        List<Lot> lots = new ArrayList<Lot>();
        int ownerId = user.getId();
        try {
            String sqlQuery = ("select L.id, L.name, L.finishDate," +
                    " L.startPrice, L.description, L.ownerId," +
                    " L.state, " +
                    " concat(O.firstName, ' ', case when O.lastName is null then '' else O.lastName end) as lotOwnerName, " +
                    " (select MAX(value) from bids where lotId = L.id) as lotMaxBidValue" +
                    " from lots as L " +
                    " join users as O on O.id = L.ownerId"
                    + (ownerId == -1 ? "" : " where ownerId=?"));
            PreparedStatement preparedStatement = connection.
                    prepareStatement(sqlQuery);
            if (ownerId != -1) {
                preparedStatement.setInt(1, ownerId);
            }
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Lot lot = new Lot();
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setFinishDateFromSql(rs.getTimestamp("finishDate"));
                lot.setStartPrice(rs.getDouble("startPrice"));
                lot.setDescription(rs.getString("description"));
//                lot.setOwnerId(rs.getInt("ownerId"));
                lot.setState(rs.getString("state"));
                //joined columns
//                lot.setOwnerName(rs.getString("lotOwnerName"));
                lot.setOwner(user);
                lot.setMaxBidValue(rs.getDouble("lotMaxBidValue"));
                lots.add(lot);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return lots;
    }


    @Override
    public Lot findById(int lotId) {
        Lot lot = new Lot();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select L.id, L.name, L.finishDate," +
                            " L.startPrice, L.description, L.ownerId," +
                            " L.state, " +
                            " concat(O.firstName, ' ', case when O.lastName is null then '' else O.lastName end) as lotOwnerName" +
                            " from lots as L " +
                            " join users as O on O.id = L.ownerId" +
                            " where L.id=?");
            preparedStatement.setInt(1, lotId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setFinishDateFromSql(rs.getTimestamp("finishDate"));
                lot.setStartPrice(rs.getDouble("startPrice"));
                lot.setDescription(rs.getString("description"));
//                lot.setOwnerId(rs.getInt("ownerId"));
                lot.setState(rs.getString("state"));
                lot.setOwner(userDao.findById(rs.getInt("ownerId")));
                //
//                lot.setOwnerName(rs.getString("lotOwnerName"));
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return lot;
    }

    @Override
    public boolean delete(Lot lot) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE lots where Id=?");
            preparedStatement.setInt(1, lot.getId());
            preparedStatement.execute();
            result = true;
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return result;
    }

    @Override
    public List<Lot> findAllOverdueLots() {
        List<Lot> lots = new ArrayList<Lot>();
        try {
            String sqlQuery = "select L.id, L.name, L.ownerId, "
                    + " (select MAX(value) from bids where lotId = L.id) as lotMaxBidValue"
                    + " from lots as L "
                    + " where  TIMESTAMPDIFF(MINUTE,finishDate,now() ) >= 0 "    //in past
                    + " and State = 'Active'";                                  //active (other lots is or cancelled manually, or sold/not sold automatically
            ;
            PreparedStatement preparedStatement = connection.
                    prepareStatement(sqlQuery);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Lot lot = new Lot();
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setOwner(userDao.findById(rs.getInt("ownerId")));
                lot.setMaxBidValue(rs.getDouble("lotMaxBidValue"));
                lots.add(lot);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return lots;
    }

    @Override
    public List<Lot> findAll() {
        List<Lot> lots = new ArrayList<Lot>();
        try {
            String sqlQuery = "select L.id, L.name, L.ownerId, "
                    + " (select MAX(value) from bids where lotId = L.id) as lotMaxBidValue"
                    + " from lots as L "
//                    + " where  TIMESTAMPDIFF(MINUTE,finishDate,now() ) >= 0 "    //in past
//                    + " and State = 'Active'";                                  //active (other lots is or cancelled manually, or sold/not sold automatically
                    ;
            PreparedStatement preparedStatement = connection.
                    prepareStatement(sqlQuery);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Lot lot = new Lot();
                lot.setId(rs.getInt("id"));
                lot.setName(rs.getString("name"));
                lot.setOwner(userDao.findById(rs.getInt("ownerId")));
                lot.setMaxBidValue(rs.getDouble("lotMaxBidValue"));
                lots.add(lot);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return lots;
    }

    public boolean update(Lot lot) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update lots set name=?, finishDate=?,startPrice=?,description=?,ownerId=?,state=?" +
                            "where Id=?");
            // Parameters start with 1
            preparedStatement.setString(1, lot.getName());
            preparedStatement.setTimestamp(2, lot.getSqlFinishDate());
            preparedStatement.setDouble(3, lot.getStartPrice());
            preparedStatement.setString(4, lot.getDescription());
            preparedStatement.setInt(5, lot.getOwner().getId());
            preparedStatement.setString(6, lot.getState());
            //id
            preparedStatement.setInt(7, lot.getId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return result;
    }
}
