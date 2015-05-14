package WebService.entity;


import javax.persistence.*;

/**
 * Created by Olga on 22.09.2014.
 */
@Entity
@Table(name = "users")
public class User {
    //fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;

    //constructors
    //by fields
    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName == null ? "" : lastName;
    }
    //by userCard
//    public User(UserCard Card) {
//        this.login = Card.getLogin();
//        this.password = Card.getPassword();
//        this.firstName = Card.getFirstName();
//        this.lastName =  Card.getLastName();
//    }

    public User() {

    }

    public User(int ownerId) {

    }

    //setters and getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.getLogin())) return false;
        if (!firstName.equals(user.getFirstName())) return false;
        if (lastName != null ? !lastName.equals(user.getLastName()) : user.getLastName() != null) return false;
        if (!password.equals(user.getPassword())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }


}
