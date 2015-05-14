package gui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.apache.log4j.Logger;
import util.Consts;
import ws.general.AuctionWs;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;


public class Authentication extends UI {
    private static final Logger log = Logger.getLogger(Authentication.class);
    private static AuctionWs auction = null;

    public static AuctionWs getAuctionWebService() {
        if (auction != null) {
            return auction;
        }
        log.info("start get AuctionWebService");
        Service auctionService = null;
        try {
            auctionService = Service.create(
                    new URL(Consts.WEB_SERVICE_URL + "?wsdl"),
                    new QName(Consts.TARGET_NAMESPACE, "AuctionService"));

        } catch (MalformedURLException e) {
            log.error("Catch Exception", e);
        }
//        AuctionWs auction = null;
        try {
            auction = auctionService.getPort(AuctionWs.class);
        } catch (NullPointerException e) {
            log.error("Catch Exception", e);
        }
        log.info("end get AuctionWebService");
        return auction;
    }

    @Override
    public void init(VaadinRequest request) {
        log.info("------------------------");
        log.info("Start auction");
        getPage().setTitle("Auction");
//        MainWindow wnd = new MainWindow(this,14);//for test
        LoginWindow wnd = new LoginWindow(this);
        this.getCurrent().addWindow(wnd);
    }
}
