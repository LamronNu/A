package WebService.General;

import WebService.entity.Bid;
import WebService.dao.BidDAO;
import org.apache.log4j.Logger;

import java.util.List;

public class BidActions {
    private static final Logger log = Logger.getLogger(BidActions.class);
    private BidDAO bidDAO;

    public BidActions() {
        bidDAO = new BidDAO();
    }

    public List<Bid> getAllBidsForLot(int lotId) {
        log.info("get bids for lot " + lotId);
        List<Bid> bids = bidDAO.getAllBidsForLot(lotId);
        log.info("success. count of bids: " + bids.size());
        return bids;
    }

    public boolean createNewBid(Double bidValue, int ownerId, int lotId) {
        log.info("try to add new bid " + bidValue + " for lot " + lotId);
        bidDAO.addNewBid(bidValue,ownerId,lotId);
        log.info("success.");
        return true;
    }
}
