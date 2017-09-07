package manager;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;

public class TaskManager {

    private static SchedulerFactory sf = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "MY_TASK1_GROUP";
    private static String TRIGGER_GROUP_NAME = "MY_TRI1_GROUP";

    /**
     * 添加一个定时任务
     * @param jobName 任务名
     * @param cls  任务类
     * @param timestr  时间格式
     */
    public static void addJob(String jobName, Class cls, String timestr) {
        try {
            Scheduler scheduler = sf.getScheduler();
//            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
            JobDetail jobDetail = new JobDetailImpl(jobName, JOB_GROUP_NAME, cls);
            CronTriggerImpl triggerImpl = new CronTriggerImpl(jobName, TRIGGER_GROUP_NAME);
            triggerImpl.setCronExpression(timestr);
            CronTrigger trigger = triggerImpl;
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()){
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个新任务
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroup
     * @param cls
     * @param timestr
     */
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroup, Class cls, String timestr){
        try {
            Scheduler sd = sf.getScheduler();
            JobDetail jobDetail = new JobDetailImpl(jobName, jobGroupName, cls);
            CronTriggerImpl triggerImpl = new CronTriggerImpl(triggerName, triggerGroup);
            triggerImpl.setCronExpression(timestr);
            CronTrigger trigger = triggerImpl;
            sd.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改定时任务的触发时间
     * @param jobName
     * @param timestr
     */
    public static void modifyJobTime(String jobName, String timestr){
        try {
            Scheduler sd = sf.getScheduler();
            CronTrigger trigger = (CronTrigger) sd.getTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
            if (trigger == null){
                System.out.println("找不到触发器");
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(timestr)){
                JobDetail jobDetail = sd.getJobDetail(new JobKey(jobName, JOB_GROUP_NAME));
                Class cls = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, cls, timestr);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 修改定时任务的时间
     * @param triggerName
     * @param triggerGroupName
     * @param time
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String time){
        try{
            Scheduler sd = sf.getScheduler();
            CronTriggerImpl triggerImpl = (CronTriggerImpl) sd.getTrigger(new TriggerKey(triggerName, triggerGroupName));
            if(triggerImpl == null){
                System.out.println("找不到触发器");
                return;
            }
            String oldTime = triggerImpl.getCronExpression();
            if(!oldTime.equalsIgnoreCase(time)){
                triggerImpl.setCronExpression(time);
                CronTrigger ct = triggerImpl;
                sd.resumeTrigger(new TriggerKey(triggerName, triggerGroupName));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 删除定时任务
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sd = sf.getScheduler();
//            sd.pauseAll();
            sd.pauseTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
            sd.unscheduleJob(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
            sd.deleteJob(new JobKey(jobName, JOB_GROUP_NAME));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName){
        try {
            Scheduler sd = sf.getScheduler();
//            TriggerKey tk = new TriggerKey(triggerName, triggerGroupName);
            sd.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));
            sd.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));
            sd.deleteJob(new JobKey(jobName, jobGroupName));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 启动定时任务
     */
    public static void startJobs(){
        try {
            Scheduler sd = sf.getScheduler();
            sd.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void shutdownJobs(){
        try {
             Scheduler sd = sf.getScheduler();
              if (!sd.isShutdown()) {
                   sd.shutdown();
              }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
