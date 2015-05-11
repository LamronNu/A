package Library.Exceptions;


import Library.Consts.UserConsts;

/**
 * Created by Olga on 22.09.2014.
 */
public class UserDataNotValidException extends UserException {
    public UserDataNotValidException(String message) {
        super(message);
    }
    public UserDataNotValidException() {
        super(UserConsts.USER_DATA_NOT_VALID_MESSAGE);
    }
}
