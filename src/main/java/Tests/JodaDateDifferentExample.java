package Tests;

import org.apache.log4j.Logger;
import org.joda.time.*;

import java.text.SimpleDateFormat;
import java.util.Date;
public class JodaDateDifferentExample {
    private static final Logger log = Logger.getLogger(JodaDateDifferentExample.class);
    public static void main(String[] args) {

        String dateStart = "14/01/2012 09:29:58";
        String dateStop = "15/11/2014 10:31:48";

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = new Date();//null;
        Date d2 = null;
        String result = "";

        try {
           // d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            DateTime dt1 = new DateTime(d1);
            DateTime dt2 = new DateTime(d2);
            result = Days.daysBetween(dt1, dt2).getDays() + " days, "
                    + Hours.hoursBetween(dt1, dt2).getHours() % 24 + " hours, "
                    + Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes and "
                    + Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds."
                    ;
            System.out.println(result);

        } catch (Exception e) {
            log.error("catch ex:", e);
        }

    }
}
