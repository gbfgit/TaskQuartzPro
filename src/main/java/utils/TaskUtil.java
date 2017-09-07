package utils;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import pojo.DataTranJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class TaskUtil {
    public final static Logger log = Logger.getLogger(TaskUtil.class.getName());

    public static void invokeMethod(DataTranJob dataTranJob){

    }

    public static List<DataTranJob> getAll(){
        List<DataTranJob> jobs = new ArrayList<DataTranJob>();
         for(int i = 0; i < 6; i++) {
             DataTranJob job = new DataTranJob();
             job.setJobName("dataTranJob" + i);
             job.setJobGroup("transGroup");
             double num = Math.random() * (10 - i);
             String s = num + "";
             s = s.substring(0, s.indexOf("."));
             int rate = Integer.valueOf(s);
              if (rate < 1) {
                   rate = 1;
              }
             job.setCronExpression("10/" + rate +" * * * * ?");
              if (i % 2 ==0) {
                   job.setRunStatus("1");
              } else {
                  job.setRunStatus("0");
              }
              jobs.add(job);
         }
         return jobs;
    }
    /**
     * 获取所有计划中的任务列表
     * @return
     * @throws SchedulerException
     */
    public static List<DataTranJob> getAllJobs() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<DataTranJob> dataJobs = new ArrayList<DataTranJob>();
        for (JobKey jobkey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobkey);
            for (Trigger t : triggers) {
                DataTranJob job = new DataTranJob();
                job.setJobName(jobkey.getName());
                job.setJobGroup(jobkey.getGroup());
                Trigger.TriggerState state = scheduler.getTriggerState(t.getKey());
                job.setDescription("触发器： " + t.getKey() + " 状态：" + state.name());
                job.setJobStatus(state.name());
                 if (t instanceof CronTrigger) {
                      CronTrigger c = (CronTrigger) t;
                      String cronExpression = c.getCronExpression();
                      job.setCronExpression(cronExpression);
                 }
                 dataJobs.add(job);
            }
        }
        return dataJobs;
    }

    /**
     * 获取正在执行的job
     * @return
     * @throws SchedulerException
     */
    public static List<DataTranJob> getRunningJobs() throws SchedulerException{
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        List<JobExecutionContext> jecs = scheduler.getCurrentlyExecutingJobs();
        List<DataTranJob> jobs = new ArrayList<DataTranJob>(jecs.size());
        for (JobExecutionContext jec:jecs){
            DataTranJob djob = new DataTranJob();
            //从JobExecutionContext中获取JobDetail
            JobDetail jd = jec.getJobDetail();
            JobKey jk = jd.getKey();
            Trigger t = jec.getTrigger();
            Trigger.TriggerState ts = scheduler.getTriggerState(t.getKey());
            djob.setJobName(jk.getName());
            djob.setJobGroup(jk.getGroup());
            djob.setDescription("触发器： " + t.getKey() + " 状态： " + ts.name());
            djob.setJobStatus(ts.name());
             if (t instanceof CronTrigger) {
                  CronTrigger ct = (CronTrigger) t;
                  String cronExpression = ct.getCronExpression();
                  djob.setCronExpression(cronExpression);
             }
             jobs.add(djob);
        }
        return jobs;
    }

    /**
     * 暂停定时任务
     * @param data
     * @throws SchedulerException
     */
    public static void pauseJob(DataTranJob data) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobKey jk = JobKey.jobKey(data.getJobName(), data.getJobGroup());
        scheduler.pauseJob(jk);
    }

    /**
     * 恢复定时任务
     * @param data
     * @throws SchedulerException
     */
    public static void resumeJob(DataTranJob data) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobKey jk = JobKey.jobKey(data.getJobName(), data.getJobGroup());
        scheduler.resumeJob(jk);
    }

    /**
     * 删除定时任务
     * @param data
     * @throws SchedulerException
     */
    public static void deleteJob(DataTranJob data) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobKey jk = JobKey.jobKey(data.getJobName(), data.getJobGroup());
        scheduler.deleteJob(jk);
    }

    /**
     * 立即执行job
     * @param data
     * @throws SchedulerException
     */
    public static void runJobImme(DataTranJob data) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobKey jk = JobKey.jobKey(data.getJobName(), data.getJobGroup());
        scheduler.triggerJob(jk);
    }

    /**
     * 更新定时任务时间表达式
     * @param data
     * @throws SchedulerException
     */
    public static void updateJob(DataTranJob data) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobKey jk = JobKey.jobKey(data.getJobName(), data.getJobGroup());
        TriggerKey tk = TriggerKey.triggerKey(data.getJobName(), data.getJobGroup());
        CronTrigger ct = (CronTrigger) scheduler.getTrigger(tk);
        CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(data.getCronExpression());
        ct = ct.getTriggerBuilder().withIdentity(tk).withSchedule(csb).build();
        scheduler.rescheduleJob(tk, ct);
    }
}
