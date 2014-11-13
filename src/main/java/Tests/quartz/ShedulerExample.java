package Tests.quartz;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class ShedulerExample {
    private static final Logger log = Logger.getLogger(ShedulerExample.class);

    public static void testQuartz(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();


        sched.start();

        // define the jo!b and tie it to our HelloJob class
        JobDetail job = newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .build();

        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(40)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
    }

    public static void main(String[] args) {
        String arr[] = {""};
        try {
            testQuartz(arr);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
