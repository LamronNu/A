package Tests.testLog;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MyApp {

    // Define a static logger variable so that it references the
// Logger instance named "MyApp".
    static final Logger logger = LogManager.getLogger(MyApp.class.getName());

    public static void main(String[] args) {

// Set up a simple configuration that logs on the console.
        //log??

        logger.trace("Entering application.");
        Bar bar = new Bar();
        if (!bar.doIt()) {
            logger.error("Didn't do it.");
        }
        logger.trace("Exiting application.");                 }
}
