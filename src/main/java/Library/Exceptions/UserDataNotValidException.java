package Library.Exceptions;


import util.Consts;

/**
 * Created by Olga on 22.09.2014.
 */
public class UserDataNotValidException extends AuctionException {

    public UserDataNotValidException(String message) {
        super(message);
    }

    public UserDataNotValidException() {
        super(Consts.USER_DATA_NOT_VALID_MESSAGE);
    }
}
