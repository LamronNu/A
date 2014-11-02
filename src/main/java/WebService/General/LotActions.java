package WebService.General;

import Library.Consts;
import Library.Exceptions.LotUpdateException;
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
    public boolean addNewLot(String name, Date finishDate, double startPrice, String description, int ownerId){
        lot = new Lot(name,  finishDate,  startPrice,  description,  ownerId);
        return lotDAO.addLot(lot);
    }

    public void updateLotState(int lotId, int userId, String newState) throws LotUpdateException {
        lot = lotDAO.getLotById(lotId);
       //check can user cancel
        int lotOwnerId = lot.getOwnerId();
        String message = "";
        if (lotOwnerId != userId){
            message = "Can't change state to " + newState + " for lot " + lot.getName() +
                    ", because current user is not its owner";
            log.warn(message);
            //return false;
            throw new LotUpdateException(message);
        }
        String currentState = lot.getState();
        //check can change state
       if ( !Consts.ACTIVE_LOT_STATE.equals(currentState)) {
           message = "Can't change state from " + currentState +
                   " to " + newState + " for lot " + lot.getName() +
                   " (owner: " + lot.getOwnerName() + ")";
           log.warn(message);
           throw new LotUpdateException(message);
        }
        //update state in db
        lot.setState(newState);
        if (!lotDAO.updateLotState(lot)){
            throw new LotUpdateException("sql ex");
        };
    }

    public List<Lot> getAllLotsForUser(int userId) {
        return lotDAO.getAllLotsForOwner(userId);
    }

    public Lot getLotById(int lotId) {
        return lotDAO.getLotById(lotId);
    }
}
