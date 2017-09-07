package task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pojo.DataTranJob;
import utils.DateUtil;

/**
 * 带状态的定时任务测试
 */
@DisallowConcurrentExecution
public class StatusTask implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DataTranJob job = (DataTranJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        System.out.println(DateUtil.getLocalDateString(job.getJobName()));
        System.out.println("任务时间表达式：" + job.getCronExpression() + " 实现类Job：" + StatusTask.class.getSimpleName());
    }
}
