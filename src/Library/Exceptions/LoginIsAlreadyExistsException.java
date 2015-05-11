package Library.Exceptions;

import Library.Consts.UserConsts;

/**
 * Created by Olga on 22.09.2014.
 */
public class LoginIsAlreadyExistsException extends UserException {

    public LoginIsAlreadyExistsException() {
        super(UserConsts.LOGIN_IS_ALREADY_EXISTS_MESSAGE);
    }

    public LoginIsAlreadyExistsException(String login) {
        super(UserConsts.LOGIN_IS_ALREADY_EXISTS_MESSAGE + ": " + login);
    }
}
