package ws.general;

import org.apache.log4j.Logger;
import util.ex.LoginIsAlreadyExistsException;
import util.ex.UserDataNotValidException;
import ws.dao.DaoFactory;
import ws.dao.UserDao;
import ws.model.User;

/**
 * Created by Olga on 28.09.2014.
 */
public class UserActions {
    private static final Logger log = Logger.getLogger(UserActions.class);

    private UserDao userDao;
    private User user;

    public UserActions() {
        this.userDao = DaoFactory.getUserDao();
    }


    //User methods
    public int RegisterUser(String login, String password, String firstName, String lastName)
            throws LoginIsAlreadyExistsException {
        //check on exist in Auction.Bin.WebService.dao
        log.info("try register user " + login);
        this.user = new User(login, password, firstName, lastName);
        String userLogin = this.user.getLogin();
        if (checkIsLoginExistsInDB(userLogin)) { // if exists -- then will be exception
            throw new LoginIsAlreadyExistsException(userLogin);
        } else {
            saveUserToDb();//save
        }
        log.info("user " + user.getLogin() + " is created (id: " + user.getId() + ")");
        return this.user.getId();
    }

    private boolean checkIsLoginExistsInDB(String userLogin) {
        return (userDao.findByLogin(userLogin) != null);
    }

    private void saveUserToDb() {
        log.info("try save new user to dao");
        this.userDao.save(this.user);
        log.info("success.");
    }

    public User authenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException {
        log.info("start authenticate user " + userLogin);
        //System.out.println("compare passwords: from user " + userPassword);
        if (!checkIsLoginExistsInDB(userLogin)
                || !checkIsLoginAndPasswordAreCorrect(userLogin, userPassword)
                ) {
            throw new UserDataNotValidException();
        }
        log.info("user is successfully authenticate");
        return this.userDao.findByLogin(userLogin);
    }

    private boolean checkIsLoginAndPasswordAreCorrect(String login, String password) {
        log.info("check login-password");
        User dbUser = this.userDao.findByLogin(login);
        boolean IsPasswordCorrect = password.equals(dbUser.getPassword());
        if (IsPasswordCorrect) {
            log.info("password is correct");
        } else {
            log.warn("password is not correct!");
        }
        return IsPasswordCorrect;
    }

    public String GetUserName(int userID) {
        return this.userDao.findById(userID).getFullName();
    }

//    public User GetFromDB(String Lgn, String pwd) {
//        return new User(Lgn,pwd,"","");
//    }
}
