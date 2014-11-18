package Library.Exceptions;

import Library.Consts;

/**
 * Created by Olga on 22.09.2014.
 */
public class LoginIsAlreadyExistsException extends AuctionException {


    public LoginIsAlreadyExistsException(String login) {
        super(Consts.LOGIN_IS_ALREADY_EXISTS_MESSAGE + ": " + login);
    }
}
