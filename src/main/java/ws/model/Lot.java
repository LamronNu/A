package ws.model;

import org.joda.time.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
    @Column
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;

    @Column
    private double startPrice = 1.;

    @Column
    private String description;

    @Column
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User owner;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private List<Bid> bids;

    //    @JoinColumn
    @Transient
    private double maxBidValue;//??

//    @Column(name = "ownerId")
//    private int ownerId;
//@JoinColumn
//private String ownerName;

    //


    //constructors
    public Lot() {

    }

    public Lot(int id) {

    }

    public Lot(String name, Date finishDate, double startPrice, String description) {
        this.name = name;
        this.finishDate = finishDate;
        this.startPrice = startPrice;
        this.description = description;
//        this.ownerId = ownerId;
        this.state = ACTIVE;
        // owner = new UserDao().getUserById(ownerId);
    }
    public Lot(String name, Date finishDate, double startPrice, String description, int ownerId, String state) {
        this.name = name;
        this.finishDate = finishDate;
        this.startPrice = startPrice;
        this.description = description;
//        this.ownerId = ownerId;
        this.state = state;
        //owner = new UserDao().getUserById(ownerId);
    }

    //setters-getters


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

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
//

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    //
//    public String getOwnerName(){
//        return ownerName;//owner.getFullName();
//    }
//
//    public void setOwnerName(String ownerName) {
//        this.ownerName = ownerName;
//    }

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
                ", state='" + state + '\'' +
//                ", OwnerName=" + this.getOwnerName() +
                ", RemainingTime='" + this.getRemainingTime() + '\'' +
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
