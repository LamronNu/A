
package ws.general.web;

import util.Consts;
import util.ex.LoginIsAlreadyExistsException;
import util.ex.LotUpdateException;
import util.ex.UserDataNotValidException;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;


@WebService(name = "AuctionWs",
        targetNamespace = Consts.TARGET_NAMESPACE)

public interface AuctionWebService {
    @WebMethod
    public int createNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException;

    @WebMethod
    public User authenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException;

    @WebMethod
    public String getUserName(int userID);

    @WebMethod
    public void cancelLotTrades(int lotId, int userId) throws LotUpdateException;

    @WebMethod
    public void changeLotState(int lotId, int userId, String newState) throws LotUpdateException;

    @WebMethod
    public List<Lot> getAllLotsForUser(int userId);

    @WebMethod
    public Lot getLotById(int lotId);

    @WebMethod
    public List<Bid> getAllBidsForLot(int lotId);

    @WebMethod
    public boolean createNewBid(Double bidValue, User owner, Lot lot);

    @WebMethod
    public void actualizeLotStates();

    //public boolean createNewLot(String name, Date finishDate, double startPrice, String description, int ownerId);
    @WebMethod
    public boolean createNewLot(Lot lot);

    @WebMethod
    public boolean updateLot(Lot lot);

//    public List<Lot> findAllLots();
}
