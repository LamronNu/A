package ws.general;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import ws.dao.DaoUtils;
import ws.jobs.AuctionSchedule;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class StartWebServiceListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(StartWebServiceListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //new AuctionPublisher().publish();
//scheduler
        log.info("start AuctionSchedule");
        try {
            new AuctionSchedule().startActualizeLotStatesJob();
        } catch (SchedulerException e) {
            log.error("Scheduler ex", e);
        }
        log.info("end start AuctionSchedule: OK");


        //db
        log.info("start create tables if not exists");
        try {
            DaoUtils.createTables();
        } catch (SQLException e) {
            log.error(e);
        }
        log.info("create tables: OK");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
