package WebService.General;

import WebService.dao.DBUtils;
import org.apache.log4j.Logger;

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
        log.info("start create tables if not exists");
        try {
            DBUtils.createTables();
        } catch (SQLException e) {
            log.error(e);
        }
        log.info("create tables: OK");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
