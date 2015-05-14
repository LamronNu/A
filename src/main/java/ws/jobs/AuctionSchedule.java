package ws.jobs;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AuctionSchedule {
    private static final Logger log = Logger.getLogger(AuctionSchedule.class);
    private final SchedulerFactory schedFact;
    public AuctionSchedule() {
        schedFact = new StdSchedulerFactory();
    }

    public void startActualizeLotStatesJob() throws SchedulerException {
        Scheduler sched = schedFact.getScheduler();

        sched.start();

        JobDetail job = newJob(ActualizeLotStatesJob.class)
                .withIdentity("ActualizeLotStatesJob", "group1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("ActualizeLotStatesTrigger", "group1")
                .startNow()                         // Trigger the job to run now
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(30)  // and repeat every 30 minutes
                        .repeatForever())
                .build();

        sched.scheduleJob(job, trigger);
    }
}
