package util;

import java.util.Properties;

/**
 * Created by Olga on 22.09.2014.
 */
public class Consts {
    //webService consts
    public static final String WEB_SERVICE_URL = getWebServiceUrl();
    public static final String TARGET_NAMESPACE = "http://www.auction-example.herokuapp.com/ws/wsdl";

    //user consts
    public static final String LOGIN_IS_ALREADY_EXISTS_MESSAGE = "USER_LOGIN_IS_ALREADY_EXISTS";
    public static final String USER_DATA_NOT_VALID_MESSAGE = "USER_DATA_NOT_VALID";

    //notify messages
    public static final String REFRESH_LOTS_MESSAGE = "RefreshLots";
    public static final String OK_MESSAGE = "Ok";
    public static final String REFRESH_BIDS_MESSAGE = "Refresh bids";

    //other strings
    public static final String EMPTY_STR = "----";
    public static final String PLEASE_SELECT_LOT_MESSAGE = "< Please, select lot >";

    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    //use profile
    public static final String PROFILE_DEFAULT = "prod";
    public static final String PROFILE_PRODUCTION = "prod";

    private static String getWebServiceUrl(){
        //get profile name
        Properties env = System.getProperties();
        String profile = env.getProperty("profile");
        System.out.println(">>>>>>>PROFILE: " + profile);
        profile = profile == null ? PROFILE_DEFAULT : profile;
        if ("prod".equals(profile))
            return "http://auction-example.herokuapp.com:80/service";
        else
            return "http://localhost:8100/a/service";

//        switch (profile){
//            case "dev":
//                return "http://localhost:8100/services/auction";
//            case "prod":
//                return "http://auction-example.herokuapp.com:8100/services/auction";
//            default: //as dev
//                return "http://localhost:8100/services/auction";
//        }
    }
}
