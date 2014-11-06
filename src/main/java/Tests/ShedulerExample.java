package Tests;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ShedulerExample {
    private static final Logger log = Logger.getLogger(ShedulerExample.class);

    public  void testQuartz(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();


        sched.start();

        // define the job and tie it to our HelloJob class
//        JobDetail job = new Job(HelloJob.class)
//                .withIdentity("myJob", "group1")
//                .build();
//
//        // Trigger the job to run now, and then every 40 seconds
//        Trigger trigger = new Trigger()
//                .withIdentity("myTrigger", "group1")
//                .startNow()
//                .withSchedule(simpleSchedule()
//                        .withIntervalInSeconds(40)
//                        .repeatForever())
//                .build();

        // Tell quartz to schedule the job using our trigger
//        sched.scheduleJob(job, trigger);
    }
}
