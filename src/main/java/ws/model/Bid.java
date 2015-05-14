package ws.model;

import ws.dao.UserDao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * describe entity Bid
 */
@Entity
@Table(name = "bids")
public class Bid {

    //fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "value")
    private double value;

    @Column(name = "lotId")
    private int lotId;

    @Column(name = "ownerId")
    private int ownerId;

    @Column(name = "createdOn")
    private Date createdOn = new Date();

    //
    private int lotOwnerId;
    private double lotStartPrice;
    private User owner;
    @JoinColumn
    private String ownerName;
    private double lotMaxBidValue;
    private User lotOwner;
    private Lot lot;

    //constructors
    public Bid(double value, int lotId, User owner) {
        this.value = value;
        this.lotId = lotId;
        this.owner = owner;
        this.ownerId = owner.getId();

    }

    public Bid() {

    }
    //getters and setters
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

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
        owner = new UserDao().getUserByLotId(lotId);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

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

    public String getBidderName() {
        return owner.getFullName();
    }

    public void setLotOwnerId(int lotOwnerId) {
        this.lotOwnerId = lotOwnerId;
    }

    public String getOwnerName() {
        return owner.getFullName();
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerName(String ownerFirstName, String ownerLastName) {
        this.ownerName = ownerFirstName +
                 (ownerLastName == null ? "" : (" " + ownerLastName));
    }

    public double getLotStartPrice() {
        return lotStartPrice;
    }

    public void setLotStartPrice(double lotStartPrice) {
        this.lotStartPrice = lotStartPrice;
    }

    public double getLotMaxBidValue() {
        return lotMaxBidValue;
    }

    public void setLotMaxBidValue(double lotMaxBidValue) {
        this.lotMaxBidValue = lotMaxBidValue;
    }

    public void setLotOwner(User owner) {
        this.lotOwner = owner;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    //

}
