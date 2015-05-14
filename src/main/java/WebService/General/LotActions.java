package WebService.General;

import WebService.entity.Lot;
import org.apache.log4j.Logger;
import util.Consts;
import util.ex.LotUpdateException;
import ws.dao.LotDAO;

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
    public boolean addNewLot(String name, Date finishDate, double startPrice, String description, int ownerId) {
        log.info("try to add new lot");
        lot = new Lot(name, finishDate, startPrice, description, ownerId);
        boolean IsLotAdd = lotDAO.addLot(lot);
        log.info(IsLotAdd ? "success." : "failure");
        return IsLotAdd;
    }

    public void updateLotState(int lotId, int userId, String newState) throws LotUpdateException {
        lot = lotDAO.getLotById(lotId);//todo receive lot, not id
        log.info("try to change state to " + newState + " for lot " + lot.getId());
        //check can user cancel
        int lotOwnerId = lot.getOwnerId();
        String message = "";
        if (lotOwnerId != userId) {
            message = "Can't change state to " + newState + " for lot " + lot.getId() +
                    ", because current user is not its owner";
            log.warn(message);
            throw new LotUpdateException(message);
        }
        String currentState = lot.getState();
        //check can change state
        if (!Consts.ACTIVE_LOT_STATE.equals(currentState)) {
            message = "Can't change state from " + currentState +
                    " to " + newState + " for lot " + lot.getId() +
                    " (owner: " + lot.getOwnerName() + ")";
            log.warn(message);
            throw new LotUpdateException(message);
        }
        //update state in dao
        lot.setState(newState);
        if (!lotDAO.updateLotState(lot)) {
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

    public void actualizeLotStates() {
        log.info("start actualize lot states");
        List<Lot> lots = lotDAO.getLots();
        if (lots.size() == 0) {
            log.info("there are no non-actual lots");
            return;
        }
        Double maxPrice;
        String resultState;
        String logMessage;
        for (Lot lot : lots) {
            try {
                maxPrice = lot.getMaxBidValue();
                resultState = maxPrice == 0 ? Consts.NOT_SOLD_LOT_STATE : Consts.SOLD_LOT_STATE;
                logMessage = "Lot " + lot.getId() + " is "
                        + (maxPrice == 0 ? " not sold" : (" sold by price " + maxPrice));

                updateLotState(lot.getId(), lot.getOwnerId(), resultState);
                log.info(logMessage);
            } catch (LotUpdateException e) {
                log.error("catch ex on lot " + lot.getId(), e);
            }
        }
        log.info("successfully changed states. lots count: " + lots.size());
    }

    public boolean updateLot(Lot lot) {
        log.info("try update lot " + lot.getId());
        boolean result = lotDAO.updateLot(lot);
        log.info("success. lot " + lot.getId() + " is updated");
        return result;
    }
}
