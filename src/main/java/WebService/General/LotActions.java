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
        log.info("try to add new lot");
        lot = new Lot(name,  finishDate,  startPrice,  description,  ownerId);
        boolean IsLotAdd = lotDAO.addLot(lot);
        log.info(IsLotAdd ? "success." : "failure");
        return IsLotAdd;
    }

    public void updateLotState(int lotId, int userId, String newState) throws LotUpdateException {

        lot = lotDAO.getLotById(lotId);
        log.info("try to change state to " + newState + " for lot " + lot.getName() );
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
        }
        log.info("update state is success");
    }

    public List<Lot> getAllLotsForUser(int userId) {
        log.info("get lots");
        List<Lot> lots = lotDAO.getAllLotsForOwner(userId);
        log.info("success. count of lots: " + lots.size());
        return lots;
    }

    public Lot getLotById(int lotId) {
        log.info("get lot by id " + lotId);
        Lot lot = lotDAO.getLotById(lotId);
        log.info("success.");
        return lot;
    }
}
