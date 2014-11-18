package WebService.General;

import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.UserDataNotValidException;
import WebService.entity.User;
import WebService.dao.UserDAO;
import org.apache.log4j.Logger;

/**
 * Created by Olga on 28.09.2014.
 */
public class UserActions {
    private static final Logger log = Logger.getLogger(UserActions.class);
    private final UserDAO userDAO;
    private User user;

    public UserActions() {
        this.userDAO = new UserDAO();
    }


    //User methods
    public int RegisterUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException {
        //check on exist in Auction.Bin.WebService.dao
        log.info("try register user " + login);
        this.user = new User(login, password, firstName, lastName);
        String userLogin = this.user.getLogin();
        if (CheckIsLoginExistsInDB(userLogin)) { // if exists -- then will be exception
            throw new LoginIsAlreadyExistsException(userLogin);
        } else {
            SaveUserToDB();//save
        }
        log.info("user " + user.getLogin() + " is created (id: " + user.getId() + ")");
        return this.user.getId();
    }

    private boolean CheckIsLoginExistsInDB(String userLogin) {
        return userLogin.equals(this.userDAO.getUserByLogin(userLogin).getLogin());
    }

    private void SaveUserToDB() {
        log.info("try save new user to dao");
        this.userDAO.addUser(this.user);
        log.info("success.");
    }

    public int AuthenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException {
        log.info("start authenticate user " + userLogin);
        if (!CheckIsLoginExistsInDB(userLogin)
                || !CheckIsLoginAndPasswordAreCorrect(userLogin, userPassword)
                ) {
            throw new UserDataNotValidException();
        }
        log.info("user is successfy authenticate");
        return this.userDAO.getUserByLogin(userLogin).getId();
    }

    private boolean CheckIsLoginAndPasswordAreCorrect(String login, String password) {
        log.info("check login-password");
        User dbUser = this.userDAO.getUserByLogin(login);
        boolean IsPasswordCorrect = password.equals(dbUser.getPassword());
        if (IsPasswordCorrect) {
            log.info("password is correct");
        }else{
            log.warn("password is not correct!");
        }
        return IsPasswordCorrect;
    }

    public String GetUserName(int userID) {
        return this.userDAO.getUserById(userID).getFullName()  ;
    }

//    public User GetFromDB(String Lgn, String pwd) {
//        return new User(Lgn,pwd,"","");
//    }
}
