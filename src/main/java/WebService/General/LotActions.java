package WebService.General;

import Library.Consts;
import WebService.Domain.Lot;
import WebService.db.LotDAO;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class LotActions {
    private static final Logger log = Logger.getLogger(LotActions.class);
    private final LotDAO lotDAO;
    private Lot lot;

    public LotActions() {
        this.lotDAO = new LotDAO();
    }


    //methods
    public boolean AddNewLot(String name, Date finishDate, double startPrice, String description, int ownerId){
        lot = new Lot(name,  finishDate,  startPrice,  description,  ownerId);
        return lotDAO.addLot(lot);
    }

    public boolean UpdateLotState(int lotId, int userId, String newState){
        lot = lotDAO.getLotById(lotId);
       //check can user cancel
        int lotOwnerId = lot.getOwnerId();
        if (lotOwnerId != userId){
            log.warn("Can't change state on " + newState + " for lot " + lot.getName() +
                    ", because current user is not its owner");
            return false;
        }
        String currentState = lot.getState();
        //check can change state
       if (currentState != Consts.ACTIVE_LOT_STATE) {
           log.warn("Can't change state from " + currentState +
                   " on " + newState + " for lot " + lot.getName() +
                    " (owner: " + lot.getOwnerName() + ")");
           return false;
        }
        //update state in db
        lot.setState(newState);
        return lotDAO.updateLotState(lot);
    }

    public List<Lot> GetAllLotsForUser(int userId) {
        return lotDAO.getAllLotsForOwner(userId);
    }
}
