package task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.DateUtil;

public class ShowInfoTask implements Job{

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("当前时间是：" + DateUtil.getCurrentDateString() + " ShowInfoTask在执行！");
    }
}
