package WebService.Domain.Lot;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lots")
public class Lot {

//    ownerId int  NOT NULL,
//    state varchar (10) DEFAULT 'Active' NOT NULL
//fields
    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "finishDate")
    private Date finishDate;
    @Column(name = "startPrice")
    private float startPrice;
    @Column(name = "description")
    private String description;
    @Column(name = "ownerId")
    private int ownerId;
    @Column(name = "state")
    private String state;
}
