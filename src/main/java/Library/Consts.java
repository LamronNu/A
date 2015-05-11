package Library;

/**
 * Created by Olga on 22.09.2014.
 */
public class Consts {

    //webService consts
    public static final String WEB_SERVICE_URL = "http://localhost:8100/services/auction";
    public static final String TARGET_NAMESPACE = "http://www.jbs.com.ua/wsdl";

    //user consts
    public static final String LOGIN_IS_ALREADY_EXISTS_MESSAGE = "USER_LOGIN_IS_ALREADY_EXISTS";
    public static final String USER_DATA_NOT_VALID_MESSAGE = "USER_DATA_NOT_VALID";

    //lot states
    public static final String ACTIVE_LOT_STATE = "Active";
    public static final String CANCELLED_LOT_STATE = "Cancelled";
    public static final String SOLD_LOT_STATE = "Sold";
    public static final String NOT_SOLD_LOT_STATE = "Not sold";

    //notify messages
    public static final String REFRESH_LOTS_MESSAGE = "RefreshLots";
    public static final String OK_MESSAGE = "Ok";
    public static final String REFRESH_BIDS_MESSAGE = "Refresh bids";

    //other strings
    public static final String EMPTY_STR = "----";
    public static final String PLEASE_SELECT_LOT_MESSAGE = "< Please, select lot >";

    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
}
