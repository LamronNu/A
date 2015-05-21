package ws.general.web;

import util.Consts;
import util.ex.LoginIsAlreadyExistsException;
import util.ex.LotUpdateException;
import util.ex.UserDataNotValidException;
import ws.general.BidActions;
import ws.general.LotActions;
import ws.general.UserActions;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

//@Stateless//todo read about it
@WebService(
        portName = "AuctionPort",
        serviceName = "AuctionService",
        targetNamespace = Consts.TARGET_NAMESPACE,
        endpointInterface = "ws.general.web.AuctionWebService")
public class Auction implements AuctionWebService {

    private final UserActions userActions;
    private final LotActions lotActions;
    private final BidActions bidActions;

    public Auction() {
        userActions = new UserActions();
        lotActions = new LotActions();
        bidActions = new BidActions();
    }

    @Override
    @WebMethod
    public int createNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException {
        return userActions.RegisterUser(login, password, firstName, lastName);
    }

    @Override
    @WebMethod
    public User authenticateUser(String userLogin, String userPassword) throws UserDataNotValidException {
        return userActions.authenticateUser(userLogin, userPassword);
    }

    @Override
    @WebMethod
    public String getUserName(int userID) {
        return userActions.GetUserName(userID);
    }

//    @Override
//    public boolean createNewLot(String name, Date finishDate, double startPrice, String description, int ownerId) {
//        return lotActions.addNewLot(name, finishDate, startPrice, description, ownerId);
//    }

    @Override
    @WebMethod
    public boolean createNewLot(Lot lot) {
        return lotActions.addNewLot(lot.getName(), lot.getFinishDate(), lot.getStartPrice(), lot.getDescription(), lot.getOwner());//todo better!!
    }

    @Override
    @WebMethod
    public boolean updateLot(Lot lot) {
        return lotActions.updateLot(lot);
    }

    @Override
    @WebMethod
    public void cancelLotTrades(int lotId, int userId) throws LotUpdateException {
        lotActions.updateLotState(lotId, userId, Lot.CANCELLED);
    }

    @Override
    @WebMethod
    public void changeLotState(int lotId, int userId, String newState) throws LotUpdateException {
        lotActions.updateLotState(lotId, userId, newState);
    }

    @Override
    @WebMethod
    public List<Lot> getAllLotsForUser(int userId) {
        return lotActions.getAllLotsForUser(userId);
    }

    @Override
    @WebMethod
    public Lot getLotById(int lotId) {
        return lotActions.getLotById(lotId);
    }

    @Override
    @WebMethod
    public List<Bid> getAllBidsForLot(int lotId) {
        List<Bid> result = bidActions.getAllBidsForLot(lotActions.getLotById(lotId));
        return result;
    }

    @Override
    @WebMethod
    public boolean createNewBid(Double bidValue, User owner, Lot lot) {
        return bidActions.createNewBid(bidValue, owner, lot);
    }

    @Override
    @WebMethod
    public void actualizeLotStates() {
        lotActions.actualizeLotStates();
    }
}
