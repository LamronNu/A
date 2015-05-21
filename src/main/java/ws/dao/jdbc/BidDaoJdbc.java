package ws.dao.jdbc;

import org.apache.log4j.Logger;
import ws.dao.BidDao;
import ws.dao.UserDao;
import ws.model.Bid;
import ws.model.Lot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDaoJdbc implements BidDao {
    private static final Logger log = Logger.getLogger(BidDaoJdbc.class);

    private final Connection connection;
    private UserDao userDao;

    public BidDaoJdbc() {
        connection = DaoUtils.getConnection();
        userDao = new UserDaoJdbc();
    }

    @Override
    public boolean save(Bid bid) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into bids(value, ownerId, lotId) values (?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setDouble(1, bid.getValue());
            preparedStatement.setInt(2, bid.getOwner().getId());
            preparedStatement.setInt(3, bid.getLot().getId());
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            // e.printStackTrace();
            log.error("catch exception:", e);
        }
        return result;
    }

    @Override
    public List<Bid> findAllByLot(Lot lot) {
        List<Bid> bids = new ArrayList<Bid>();
        int lotId = lot.getId();
        try {
            String sqlQuery = "select B.id, B.value, B.ownerId, B.createdOn," +
                    " L.ownerId as lotOwnerId, L.startPrice as lotStartPrice," +
//                    " O.firstName   as bidOwnerFirstName," +
//                    " O.lastName   as bidOwnerLastName," +
                    " concat(O.firstName, ' ', case when O.lastName is null then '' else O.lastName end)  as bidOwnerName," +
                    " (select MAX(value) from bids where lotId = B.lotId) as lotMaxBidValue " +
                    " from bids as B " +
                    " join lots as L on L.id = B.lotId " +
                    " join users as O on O.id = B.ownerId " +
                    " where lotId = ?";

            PreparedStatement preparedStatement = connection.
                    prepareStatement(sqlQuery);
            preparedStatement.setInt(1, lotId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Bid bid = new Bid();
                bid.setLot(lot);
                bid.setId(rs.getInt("id"));
                bid.setValue(rs.getDouble("value"));
                bid.setOwner(userDao.findById(rs.getInt("ownerId")));
                bid.setCreatedOnDate(rs.getTimestamp("createdOn"));
                //
//                bid.setLotOwnerId(rs.getInt("lotOwnerid"));
//                bid.setLotStartPrice(rs.getDouble("lotStartPrice"));
//                bid.setOwnerName(rs.getString("bidOwnerFirstName"), rs.getString("bidOwnerLastName"));
//                bid.setOwnerName(rs.getString("bidOwnerName"));
                bid.setLotMaxBidValue(rs.getDouble("lotMaxBidValue"));
                bids.add(bid);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }
        return bids;
    }
}
