package WebService.General;

import Library.Consts;
import WebService.jobs.AuctionSchedule;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.xml.ws.Endpoint;

public class AuctionPublisher {
    private static final Logger log = Logger.getLogger(AuctionPublisher.class);
    public static void main(String[] args) {

        new AuctionPublisher().publish();

    }

    public void publish() {
        log.info("------------------------");
        //
        log.info("publish webService");
        Endpoint.publish(Consts.WEB_SERVICE_URL,   new Auction());
        log.info("publish webService: OK");
        //
        log.info("start AuctionSchedule");
        try {
            new AuctionSchedule().startActualizeLotStatesJob();
        } catch (SchedulerException e) {
            log.error("Scheduler ex", e);
        }
        log.info("start AuctionSchedule: OK");
    }
}
