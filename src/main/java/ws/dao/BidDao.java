package ws.dao;

import ws.model.Bid;
import ws.model.Lot;

import java.util.List;

public interface BidDao {

    public List<Bid> findAllByLot(Lot lot);

    public boolean save(Bid bid);

}
