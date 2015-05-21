package ws.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @Column
    @GeneratedValue
    private int id;

    @Column
    private double value;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotId")
    private Lot lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User owner;

    private double lotMaxBidValue;//??

//    @Column(name = "ownerId")
//    private int lotId;
//    @Column(name = "lotId")
//    private int ownerId;



    //calculated by joins
//    private int lotOwnerId;
//    private double lotStartPrice;
//    @JoinColumn
//    private String ownerName;


    //constructors
//    public Bid(double value, int lotId, int ownerId) {
//        this.value = value;
//        this.lotId = lotId;
//        this.ownerId = ownerId;
//        owner = new UserDao().getUserByLotId(lotId);
//    }

    public Bid() {

    }
    //getters and setters

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

//    public int getLotId() {
//        return lotId;
//    }
//
//    public void setLotId(int lotId) {
//        this.lotId = lotId;
//        owner = new UserDaoJpa().getUserByLotId(lotId);
//    }
//
//    public int getOwnerId() {
//        return ownerId;
//    }
//
//    public void setOwnerId(int ownerId) {
//        this.ownerId = ownerId;
//    }

    public void setCreatedOnDate(Date createdOnDate) {
        this.createdOn = createdOnDate;
    }

    public Date getCreatedOnDate() {
        return createdOn;
    }

    public void setCreatedOnDate(Timestamp createdOnDate) {
        this.createdOn = new Date(createdOnDate.getTime());//createdOnDate;
    }

    public Timestamp getSqlCreatedOnDate() {

        return new Timestamp(createdOn.getTime());
    }

//    public String getBidderName() {
//        return owner.getFullName();
//    }
//
//    public void setLotOwnerId(int lotOwnerId) {
//        this.lotOwnerId = lotOwnerId;
//    }
//
//    public void setOwnerName(String ownerFirstName, String ownerLastName) {
//        this.ownerName = ownerFirstName +
//                (ownerLastName == null ? "" : (" " + ownerLastName));
//    }
//
//    public String getOwnerName() {
//        return ownerName;
//    }
//
//    public void setOwnerName(String ownerName) {
//        this.ownerName = ownerName;
//    }
//
//    public double getLotStartPrice() {
//        return lotStartPrice;
//    }
//
//    public void setLotStartPrice(double lotStartPrice) {
//        this.lotStartPrice = lotStartPrice;
//    }

    public double getLotMaxBidValue() {
        return lotMaxBidValue;
    }

    public void setLotMaxBidValue(double lotMaxBidValue) {
        this.lotMaxBidValue = lotMaxBidValue;
    }

    //

}
