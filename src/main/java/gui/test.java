package gui;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;


@WebService()
public class test {
    public static void main(String[] argv) {
        Object implementor = new test();
        String address = "http://localhost:9000/test";
        Endpoint.publish(address, implementor);
    }

    @WebMethod
    public String sayHelloWorldFrom(String from) {
        String result = "Hello, world, from " + from;
        System.out.println(result);
        return result;
    }
}
