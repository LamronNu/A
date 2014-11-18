package WebService.General;

import Library.Consts;
import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.LotUpdateException;
import Library.Exceptions.UserDataNotValidException;
import WebService.entity.Bid;
import WebService.entity.Lot;

import javax.jws.WebService;
import java.util.List;

//@Stateless//todo read about it
@WebService(
        portName = "AuctionPort",
        serviceName = "AuctionService",
        targetNamespace = Consts.TARGET_NAMESPACE,
        endpointInterface = "WebService.General.AuctionWs")
public class Auction implements AuctionWs {

    private final UserActions userActions;
    private final LotActions lotActions;
    private final BidActions bidActions;

    public Auction() {
        userActions = new UserActions();
        lotActions = new LotActions();
        bidActions = new BidActions();
    }

    @Override
    public int createNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException {
        return userActions.RegisterUser(login, password, firstName, lastName);
    }

    @Override
    public int authenticateUser(String userLogin, String userPassword) throws UserDataNotValidException {
        return userActions.AuthenticateUser(userLogin, userPassword);
    }

    @Override
    public String getUserName(int userID) {
        return userActions.GetUserName(userID);
    }

//    @Override
//    public boolean createNewLot(String name, Date finishDate, double startPrice, String description, int ownerId) {
//        return lotActions.addNewLot(name, finishDate, startPrice, description, ownerId);
//    }

    @Override
    public boolean createNewLot(Lot lot) {
        return lotActions.addNewLot(lot.getName(),lot.getFinishDate(),lot.getStartPrice(),lot.getDescription(),lot.getOwnerId());//todo better!!
    }

    @Override
    public boolean updateLot(Lot lot) {
        return lotActions.updateLot(lot);
    }

    @Override
    public void cancelLotTrades(int lotId, int userId) throws LotUpdateException {
        lotActions.updateLotState(lotId, userId, Consts.CANCELLED_LOT_STATE);
    }

    @Override
    public void changeLotState(int lotId, int userId, String newState) throws LotUpdateException {
        lotActions.updateLotState(lotId, userId, newState);
    }

    @Override
    public List<Lot> getAllLotsForUser(int userId) {
        return lotActions.getAllLotsForUser(userId);
    }

    @Override
    public Lot getLotById(int lotId) {
        return lotActions.getLotById(lotId);
    }

    @Override
    public List<Bid> getAllBidsForLot(int lotId) {
        List<Bid> result = bidActions.getAllBidsForLot(lotId);
        return result;
    }

    @Override
    public boolean createNewBid(Double bidValue, int ownerId, int lotId) {
        return bidActions.createNewBid(bidValue, ownerId, lotId);
    }

    @Override
    public void actualizeLotStates(){
        lotActions.actualizeLotStates();
    }
}
