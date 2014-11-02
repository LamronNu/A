package WebService.db;

import WebService.Domain.Bid;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDAO {
    private static final Logger log = Logger.getLogger(BidDAO.class);
    private final Connection connection;

    public BidDAO() {
        connection = DBUtils.getConnection();
    }

    public List<Bid> getAllBidsForLot(int lotId) {
        List<Bid> bids = new ArrayList<Bid>();
        try {
            String sqlQuery = "select * from bids " +
                    "where lotId = ?";
            PreparedStatement preparedStatement = connection.
                    prepareStatement(sqlQuery);
            preparedStatement.setInt(1, lotId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Bid bid = new Bid();
                bid.setId(rs.getInt("id"));
                bid.setValue(rs.getDouble("value"));
                bid.setLotId(rs.getInt("lotId"));
                bid.setOwnerId(rs.getInt("ownerId"));
                bid.setCreatedOnDate(new java.util.Date(rs.getDate("createdOn").getTime()));
                bids.add(bid);
            }
        } catch (SQLException e) {
            log.error("catch exception:", e);
        }

        return bids;
    }

    public boolean  addNewBid(Double bidValue, int ownerId, int lotId) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into bids(value, ownerId, lotId) values (?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setDouble(1, bidValue);
            preparedStatement.setInt(2, ownerId);
            preparedStatement.setInt(3, lotId);
            preparedStatement.executeUpdate();
            result = true;
        } catch (SQLException e) {
            // e.printStackTrace();
            log.error("catch exception:", e);
        }
        return result;
    }
}
