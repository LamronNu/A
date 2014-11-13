package WebService.jobs;

import WebService.General.LotActions;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActualizeLotStatesJob implements Job {
    private static final Logger log = Logger.getLogger(ActualizeLotStatesJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("start Job [ActualizeLotStatesJob]");
        new LotActions().actualizeLotStates();
        log.info("end Job [ActualizeLotStatesJob]");
    }
}
