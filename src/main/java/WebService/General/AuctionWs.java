
package WebService.General;

import Library.Consts;
import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.LotUpdateException;
import Library.Exceptions.UserDataNotValidException;
import WebService.entity.Bid;
import WebService.entity.Lot;

import javax.jws.WebService;
import java.util.List;


@WebService(name = "AuctionWs",
        targetNamespace = Consts.TARGET_NAMESPACE)

public interface AuctionWs {

    public int createNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException;

    public int authenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException;

    public String getUserName(int userID);


    public void cancelLotTrades(int lotId, int userId) throws LotUpdateException;

    public void changeLotState(int lotId, int userId, String newState) throws LotUpdateException;

    public List<Lot> getAllLotsForUser(int userId);

    public Lot getLotById(int lotId);

    public List<Bid> getAllBidsForLot(int lotId);

    public boolean createNewBid(Double bidValue, int ownerId, int lotId);

    public void actualizeLotStates();

    //public boolean createNewLot(String name, Date finishDate, double startPrice, String description, int ownerId);

    public boolean createNewLot(Lot lot);

    public boolean updateLot(Lot lot);
}
