package Tests.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {
    private static final Logger log = Logger.getLogger(HelloJob.class);

    public HelloJob() {
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException
    {
        System.err.println("Hello!  HelloJob is executing.");
    }
}




