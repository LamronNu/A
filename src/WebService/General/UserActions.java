package WebService.General;

import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.UserDataNotValidException;
import WebService.Domain.User.User;
import WebService.db.UserDAO;

/**
 * Created by Olga on 28.09.2014.
 */
public class UserActions {
    private final UserDAO userDAO;
    private User user;

    public UserActions() {
        this.userDAO = new UserDAO();
    }
    //constructor for registration
    public UserActions(String login, String password, String firstName, String lastName) {
        this();
        this.user = new User(login, password, firstName, lastName);
    }

    //User methods
    public int RegisterUser()
            throws LoginIsAlreadyExistsException {
        //check on exist in Auction.Bin.WebService.db
        String userLogin = this.user.getLogin();
        if (CheckIsLoginExistsInDB(userLogin)) { // if exists -- then will be exception
            throw new LoginIsAlreadyExistsException(userLogin);
        } else {
            SaveUserToDB();//save
        }
        return this.user.getId();
    }

    private boolean CheckIsLoginExistsInDB(String userLogin) {
//        String lgn = this.user.getLogin();
//        User dbUser = this.userDAO.getUserByLogin(this.user.getLogin());
//        boolean IsEqual = this.user.equals(dbUser);
        return userLogin.equals(this.userDAO.getUserByLogin(userLogin).getLogin());
    }

    private void SaveUserToDB() {
        this.userDAO.addUser(this.user);
    }

    public int AuthenticateUser(String userLogin, String userPassword)
            throws UserDataNotValidException {

        if (!CheckIsLoginExistsInDB(userLogin)
                || !CheckIsLoginAndPasswordAreCorrect(userLogin, userPassword)
                ) {
            throw new UserDataNotValidException();
        }
        return this.userDAO.getUserByLogin(userLogin).getId();
    }

    private boolean CheckIsLoginAndPasswordAreCorrect(String login, String password) {
        User dbUser = this.userDAO.getUserByLogin(login);
        return password.equals(dbUser.getPassword());
    }

    public String GetUserName(int userID) {
        return this.userDAO.getUserById(userID).getFirstName()
                + " " + this.userDAO.getUserById(userID).getLastName()  ;
    }

//    public User GetFromDB(String Lgn, String pwd) {
//        return new User(Lgn,pwd,"","");
//    }
}
