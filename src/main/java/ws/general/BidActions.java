package ws.general;

import org.apache.log4j.Logger;
import ws.dao.BidDao;
import ws.dao.DaoFactory;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import java.util.List;

public class BidActions {
    private static final Logger log = Logger.getLogger(BidActions.class);
    private BidDao bidDao;

    public BidActions() {
        bidDao = DaoFactory.getBidDao();
    }

    public List<Bid> getAllBidsForLot(Lot lot) {
        log.info("get bids for lot " + lot.getId());
        List<Bid> bids = bidDao.findAllByLot(lot);
        log.info("success. count of bids: " + bids.size());
        return bids;
    }

    public boolean createNewBid(Double bidValue, User owner, Lot lot) {
        log.info("try to add new bid " + bidValue + " for lot " + lot.getId());
        Bid bid = new Bid();
        bid.setValue(bidValue);
        bid.setLot(lot);
        bid.setOwner(owner);
        boolean result = bidDao.save(bid);
        log.info("success.");
        return result;
    }
}
