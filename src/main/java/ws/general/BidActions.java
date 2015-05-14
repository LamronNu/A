package ws.general;

import org.apache.log4j.Logger;
import ws.dao.BidDao;
import ws.model.Bid;

import java.util.List;

public class BidActions {
    private static final Logger log = Logger.getLogger(BidActions.class);
    private BidDao bidDao;

    public BidActions() {
        bidDao = new BidDao();
    }

    public List<Bid> getAllBidsForLot(int lotId) {
        log.info("get bids for lot " + lotId);
        List<Bid> bids = bidDao.getAllBidsForLot(lotId);
        log.info("success. count of bids: " + bids.size());
        return bids;
    }

    public boolean createNewBid(Double bidValue, int ownerId, int lotId) {
        log.info("try to add new bid " + bidValue + " for lot " + lotId);
        bidDao.addNewBid(bidValue, ownerId, lotId);
        log.info("success.");
        return true;
    }
}
