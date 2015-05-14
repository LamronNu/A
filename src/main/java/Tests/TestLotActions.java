package Tests;

import Library.Exceptions.LoginIsAlreadyExistsException;
import Library.Exceptions.UserDataNotValidException;
import WebService.General.Auction;
import WebService.General.AuctionWs;
import WebService.entity.Lot;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestLotActions {
    private static final Logger log = Logger.getLogger(TestLotActions.class);

    public static void main(String[] args) {
        log.info("init WS");
        AuctionWs auction = new Auction();
        int userId = 14;//olga
        //user
        log.info("create new user");
        try {
            userId = auction.createNewUser("Nika","qwerty","Nikolya","");
            userId = auction.createNewUser("Nik","qwerty","Nikolya","");
            //log.info("user Nik is created");
        } catch (LoginIsAlreadyExistsException e) {
            log.error("catch ex:", e);
        }
        //auth
        log.info("Authenticate user");
        try {
            userId = auction.authenticateUser("Nik", "qwerty");
            auction.authenticateUser("Nik", "qwerty1");

        } catch (UserDataNotValidException e) {
            log.error("catch ex:", e);
        }
        //create lots
        for (int i = 11; i < 20; i++) {
            Date finishDate = new Date();
            finishDate.setMonth(11);
            finishDate.setDate(20 + i);
            boolean IsCreated = auction.createNewLot(new Lot("lot" + i, finishDate, 10. + i, "test_lot_" + i, userId));
            log.info(IsCreated ? "lot is created":"lot is not created");

        }

        //change lot state
        List<Lot> lotList = new ArrayList<Lot>(auction.getAllLotsForUser(userId));
        for(Lot lot:lotList){
            log.info("print lot info: " + lot.toString());
        }
    }
}
