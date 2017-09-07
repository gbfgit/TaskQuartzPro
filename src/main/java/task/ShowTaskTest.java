package task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;

public class ShowTaskTest {
    public static void main(String[] args) {
        try {
            //声明调度器并从调度工厂获取调度器对象
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //打印调度器名称
            System.out.println("调度器名称： " + scheduler.getSchedulerName());
            /**
             *创建JobDetail对象并绑定域。使用静态方法newJob  JobDetail的变迁：1.8.X系列，JobDetail是一个类，到了2系（具体版本未知）列后
             * JobDetail变成了接口，不能直接按网上的例子new一个JobDetail对象，但其有实现类，放置在impl包下。CronTrigger类同，Trigger无实现类
             */
            JobDetail job = (JobDetail) newJob(ShowInfoTask.class).withIdentity("job1", "group1").build();
            //声明并创建触发器并设置执行频率  不同的触发器设置执行频率的方式不同
//            Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(simpleSchedule()
//                    .withIntervalInSeconds(5).withRepeatCount(10)).build();
            CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().
                    withSchedule(cronSchedule("0 0/5 18 * * ?")).build();
            //调度器绑定job和触发器
            scheduler.scheduleJob(job, trigger);
            /*根据API，使用triggerJob应该能启动任务，但在实验时无法执行  begin*/
            //版本1.8.X系列后，Trigger不再拥有set类方法，则以下语句不再适应高版本
//            scheduler.scheduleJob(trigger.setJobName(job.getKey()));
            //调用addjob方法向调度器添加Job后，调用triggerJob无法启动定时任务，原因不详。且会报错
//            scheduler.addJob(job, true);
//            scheduler.triggerJob(trigger.getJobKey());
            /*根据API，使用triggerJob应该能启动任务，但在实验时无法执行   end*/
            //启动调度器
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
