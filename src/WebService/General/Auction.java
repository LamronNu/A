package WebService.General;

import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.UserDataNotValidException;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(
        portName = "AuctionPort",
        serviceName = "AuctionService",
        targetNamespace = "http://www.jbs.com.ua/wsdl",
        endpointInterface = "WebService.General.AuctionWs")
public class Auction implements AuctionWs {
    @Override
    public int CreateNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException {
        return new UserActions(login, password, firstName, lastName).RegisterUser();
    }

    @Override
    public int AuthenticateUser(String userLogin, String userPassword) throws UserDataNotValidException {
        return new UserActions().AuthenticateUser(userLogin, userPassword);
    }

    @Override
    public String GetUserName(int userID) {
        return new UserActions().GetUserName(userID);
    }

}
