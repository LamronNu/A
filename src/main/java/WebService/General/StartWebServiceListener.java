package WebService.General;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartWebServiceListener implements  ServletContextListener {
    private static final Logger log = Logger.getLogger(StartWebServiceListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new AuctionPublisher().publish();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
