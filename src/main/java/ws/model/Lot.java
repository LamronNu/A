package ws.model;

import org.joda.time.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * describe entity Lot
 */
@Entity
@Table(name = "lots")
public class Lot {

    //lot states
    public static final String ACTIVE = "Active";
    public static final String CANCELLED = "Cancelled";
    public static final String SOLD = "Sold";
    public static final String NOT_SOLD = "Not sold";

    //fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "finishDate")
    private Date finishDate = new Date();

    @Column(name = "startPrice")
    private double startPrice = 1.;

    @Column(name = "description")
    private String description;

    @Column(name = "ownerId")
    private int ownerId;

    @Column(name = "state")
    private String state;

    //
    @JoinColumn
    private String ownerName;
    @JoinColumn
    private double maxBidValue;

    //constructors
    public Lot() {

    }

    public Lot(int id) {

    }
    public Lot(String name, Date finishDate, double startPrice, String description, int ownerId) {
        this(name, finishDate, startPrice, description, ownerId, ACTIVE);
    }
    public Lot(String name, Date finishDate, double startPrice, String description, int ownerId, String state) {
        this.name = name;
        this.finishDate = finishDate;
        this.startPrice = startPrice;
        this.description = description;
        this.ownerId = ownerId;
        this.state = state;
        //owner = new UserDao().getUserById(ownerId);
    }

    //setters-getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //finish date
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Timestamp getSqlFinishDate() {
        Timestamp result = new Timestamp(finishDate.getTime());
        return result;
    }

    public void setFinishDateFromSql(Timestamp finishDate) {
        this.finishDate = new Date(finishDate.getTime());

    }
    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        //owner = new UserDao().getUserById(ownerId);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    //
    public String getOwnerName(){
        return ownerName;//owner.getFullName();
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRemainingTime(){
        DateTime today = new DateTime();
        DateTime endDate = new DateTime(finishDate);
        String result = Days.daysBetween(today, endDate).getDays() + " days, "
                + Hours.hoursBetween(today, endDate).getHours() % 24 + " hours, "
                + Minutes.minutesBetween(today, endDate).getMinutes() % 60 + " minutes and "
                + Seconds.secondsBetween(today, endDate).getSeconds() % 60 + " seconds."
        ;
        return result;
    }

    @Override
    public String toString() {
        return "Lot{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", finishDate=" + finishDate +
                ", startPrice=" + startPrice +
                ", description='" + description + '\'' +
                ", ownerId=" + ownerId +
                ", state='" + state + '\'' +
                ", ownerName=" + this.getOwnerName() +
                ", remainingTime='" + this.getRemainingTime() + '\'' +
                '}';
    }

    public double getTotalLotPrice() {
        return (maxBidValue == 0) ? startPrice : maxBidValue;
    }

    public double getMaxBidValue() {
        return maxBidValue;
    }

    public void setMaxBidValue(double maxBidValue) {
        this.maxBidValue = maxBidValue;
    }
}
