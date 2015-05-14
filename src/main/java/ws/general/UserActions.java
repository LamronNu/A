package ws.general;

import org.apache.log4j.Logger;
import util.ex.LoginIsAlreadyExistsException;
import util.ex.UserDataNotValidException;
import ws.dao.UserDao;
import ws.model.User;

/**
 * Created by Olga on 28.09.2014.
 */
public class UserActions {
    private static final Logger log = Logger.getLogger(UserActions.class);
    private final UserDao userDao;
    private User user;

    public UserActions() {
        this.userDao = new UserDao();
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
        return userLogin.equals(this.userDao.getUserByLogin(userLogin).getLogin());
    }

    private void SaveUserToDB() {
        log.info("try save new user to dao");
        this.userDao.addUser(this.user);
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
        return this.userDao.getUserByLogin(userLogin).getId();
    }

    private boolean CheckIsLoginAndPasswordAreCorrect(String login, String password) {
        log.info("check login-password");
        User dbUser = this.userDao.getUserByLogin(login);
        boolean IsPasswordCorrect = password.equals(dbUser.getPassword());
        if (IsPasswordCorrect) {
            log.info("password is correct");
        } else {
            log.warn("password is not correct!");
        }
        return IsPasswordCorrect;
    }

    public String GetUserName(int userID) {
        return this.userDao.getUserById(userID).getFullName();
    }

//    public User GetFromDB(String Lgn, String pwd) {
//        return new User(Lgn,pwd,"","");
//    }
}
