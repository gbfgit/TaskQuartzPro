package task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import pojo.DataTranJob;
import utils.TaskUtil;

import java.util.List;

public class ShowMyTask1 {
    public static void main(String[] args) {
        List<DataTranJob> list = TaskUtil.getAll();
        try {
            if (list != null && list.size() > 0) {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                for(int i = 0; i < list.size(); i++) {
                    addJob(list.get(i), scheduler);
                }
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static void addJob(DataTranJob job, Scheduler scheduler) throws SchedulerException {
         if (job == null || !DataTranJob.STATUS_RUNNING.equals(job.getRunStatus())) {
              return;
         }
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //如果不存在触发器，创建触发器
         if (cronTrigger == null) {
             //根据任务是否有状态创建对应的类
              Class cls = DataTranJob.CONCURRENT_IS.equals(job.getRunStatus()) ? NonStatusTask.class : StatusTask.class;
              //创建JobDetail
             JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(job.getJobName(), job.getJobGroup()).build();
             jobDetail.getJobDataMap().put("scheduleJob", job);
             CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(job.getCronExpression());
             cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(csb).build();
             scheduler.scheduleJob(jobDetail, cronTrigger);
         } else {
             //更新定时任务的时间设置
             CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(job.getCronExpression());
             //重构trigger
             cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(csb).build();
             //重新设置job执行
             scheduler.rescheduleJob(triggerKey, cronTrigger);
         }
    }
}
