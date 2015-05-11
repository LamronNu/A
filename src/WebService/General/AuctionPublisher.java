package WebService.General;

import javax.xml.ws.Endpoint;

public class AuctionPublisher {
    public static void main(String[] args) {
        //this.publish();
        Endpoint.publish("http://localhost:8000/services/auction",
                new Auction());

    }

    public void publish() {
        Endpoint.publish("http://localhost:8000/services/auction",
                new Auction());
    }
}
