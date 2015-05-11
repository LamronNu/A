package WebService.General;

import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.UserDataNotValidException;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.jbs.com.ua/wsdl")//http://superbiz.org/wsdl")
public interface AuctionWs {

    public int CreateNewUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException;

    public int AuthenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException;

    public String GetUserName(int userID);
}
