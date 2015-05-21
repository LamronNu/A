package ws.general;

import org.apache.log4j.Logger;
import util.ex.LotUpdateException;
import ws.dao.DaoFactory;
import ws.dao.LotDao;
import ws.model.Lot;
import ws.model.User;

import java.util.Date;
import java.util.List;

public class LotActions {
    private static final Logger log = Logger.getLogger(LotActions.class);

    private final LotDao lotDao;
    private Lot lot;

    public LotActions() {
        lotDao = DaoFactory.getLotDao();
        ;
    }


    //methods
    public boolean addNewLot(String name, Date finishDate, double startPrice, String description, User owner) {
        log.info("try to add new lot");
        lot = new Lot(name, finishDate, startPrice, description);
        lot.setOwner(owner);
        boolean IsLotAdd = lotDao.save(lot);
        log.info(IsLotAdd ? "success." : "failure");
        return IsLotAdd;
    }

    public void updateLotState(int lotId, int userId, String newState) throws LotUpdateException {
        lot = lotDao.findById(lotId);//todo receive lot, not id
        log.info("try to change state to " + newState + " for lot " + lot.getId());
        //check can user cancel
        int lotOwnerId = lot.getOwner().getId();
        String message = "";
        if (lotOwnerId != userId) {
            message = "Can't change state to " + newState + " for lot " + lot.getId() +
                    ", because current user is not its owner";
            log.warn(message);
            throw new LotUpdateException(message);
        }
        String currentState = lot.getState();
        //check can change state
        if (!Lot.ACTIVE.equals(currentState)) {
            message = "Can't change state from " + currentState +
                    " to " + newState + " for lot " + lot.getId() +
                    " (owner: " + lot.getOwner().getLogin() + ")";
            log.warn(message);
            throw new LotUpdateException(message);
        }
        //update state in dao
        lot.setState(newState);
        if (!lotDao.updateState(lot)) {
            throw new LotUpdateException("sql ex");
        }
        log.info("update state is success");
    }

    public List<Lot> getAllLotsForUser(int userId) {
        log.info("get lots");
        List<Lot> lots = userId == -1 ? lotDao.findAll() : lotDao.findAllByUser(new User());//todo
        log.info("success. count of lots: " + lots.size());
        return lots;
    }

    public Lot getLotById(int lotId) {
        log.info("get lot by id " + lotId);
        Lot lot = lotDao.findById(lotId);
        log.info("success.");
        return lot;
    }

    public void actualizeLotStates() {
        log.info("start actualize lot states");
        List<Lot> lots = lotDao.findAllOverdueLots();
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
                resultState = maxPrice == 0 ? Lot.NOT_SOLD : Lot.SOLD;
                logMessage = "Lot " + lot.getId() + " is "
                        + (maxPrice == 0 ? " not sold" : (" sold by price " + maxPrice));

                updateLotState(lot.getId(), lot.getOwner().getId(), resultState);
                log.info(logMessage);
            } catch (LotUpdateException e) {
                log.error("catch ex on lot " + lot.getId(), e);
            }
        }
        log.info("successfully changed states. lots count: " + lots.size());
    }

    public boolean updateLot(Lot lot) {
        log.info("try update lot " + lot.getId());
        boolean result = lotDao.update(lot);
        log.info("success. lot " + lot.getId() + " is updated");
        return result;
    }

    public List<Lot> findAllLots() {
        return lotDao.findAll();
    }
}
