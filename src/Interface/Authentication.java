package Interface;

import WebService.Domain.General.AuctionWs;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;


public class Authentication extends UI {

    @Override
    public void init(VaadinRequest request) {
        getPage().setTitle("Authentication");
        LoginWindow wnd = new LoginWindow(this);
        this.getCurrent().addWindow(wnd);
    }

    public static AuctionWs GetAuctionWebService()  {
        Service auctionService = null;
        try {
            auctionService = Service.create(
                    new URL("http://localhost:8000/services/auction?wsdl"),
                    new QName("http://www.jbs.com.ua/wsdl", "AuctionService"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AuctionWs auction = null;
        try {
            auction = auctionService.getPort(AuctionWs.class);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return auction;
    }
}
