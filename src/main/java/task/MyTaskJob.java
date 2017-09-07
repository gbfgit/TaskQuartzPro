package task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.DateUtil;

public class MyTaskJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(DateUtil.getCurrentDateString() + " ★★★★★★★★★★★");
    }
}
