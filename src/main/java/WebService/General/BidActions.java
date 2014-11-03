package WebService.General;

import WebService.Domain.Bid;
import WebService.db.BidDAO;
import org.apache.log4j.Logger;

import java.util.List;

public class BidActions {
    private static final Logger log = Logger.getLogger(BidActions.class);
    private BidDAO bidDAO;

    public BidActions() {
        bidDAO = new BidDAO();
    }

    public List<Bid> getAllBidsForLot(int lotId) {

        List<Bid> result = bidDAO.getAllBidsForLot(lotId);
        return result;
    }

    public boolean createNewBid(Double bidValue, int ownerId, int lotId) {


        bidDAO.addNewBid(bidValue,ownerId,lotId);
        return true;
    }
}
