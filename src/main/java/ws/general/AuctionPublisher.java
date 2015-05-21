package ws.general;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import util.Consts;
import ws.general.web.Auction;
import ws.jobs.AuctionSchedule;

import javax.xml.ws.Endpoint;

public class AuctionPublisher {
    private static final Logger log = Logger.getLogger(AuctionPublisher.class);//LogManager.getRootLogger();

    public static void main(String[] args) {

        new AuctionPublisher().publish();

    }

    public void publish() {
        log.info("------------------------");
        //
        log.info("publish webService");
        Endpoint.publish(Consts.WEB_SERVICE_URL, new Auction());
        log.info("publish webService: OK");

        //scheduler
        log.info("start AuctionSchedule");
        try {
            new AuctionSchedule().startActualizeLotStatesJob();
        } catch (SchedulerException e) {
            log.error("Scheduler ex", e);
        }
        log.info("end start AuctionSchedule: OK");

        //db todo
//        log.info("start create tables if not exists");
//        try {
//            DaoUtils.createTables();
//        } catch (SQLException e) {
//            log.error("SQL ex", e);
//        }
//        log.info("create tables: OK");
    }
}
