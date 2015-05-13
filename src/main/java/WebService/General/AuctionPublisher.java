package WebService.General;

import Library.Consts;
import WebService.dao.DBUtils;
import org.apache.log4j.Logger;

import javax.xml.ws.Endpoint;
import java.sql.SQLException;

public class AuctionPublisher {
    private static final Logger log = Logger.getLogger(AuctionPublisher.class);//LogManager.getRootLogger();
    public static void main(String[] args) {

        new AuctionPublisher().publish();

    }

    public void publish() {
        log.info("------------------------");
        //
        log.info("publish webService");
        Endpoint.publish(Consts.WEB_SERVICE_URL,   new Auction());
        log.info("publish webService: OK");

        //scheduler
//        log.info("start AuctionSchedule");
//        try {
//            new AuctionSchedule().startActualizeLotStatesJob();
//        } catch (SchedulerException e) {
//            log.error("Scheduler ex", e);
//        }
//        log.info("end start AuctionSchedule: OK");

        //db
        log.info("start create tables if not exists");
        try {
            DBUtils.createTables();
        } catch (SQLException e) {
            log.error("SQL ex", e);
        }
        log.info("create tables: OK");
    }
}
