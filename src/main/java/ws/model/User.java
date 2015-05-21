package ws.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @OneToMany(mappedBy = "owner")
    List<Lot> lots;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    List<Bid> bids;
    //fields
    @Id
    @Column
    @GeneratedValue
    private int id;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
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

    //setters and getters


    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }
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
