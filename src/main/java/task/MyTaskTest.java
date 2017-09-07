package task;

import manager.TaskManager;

public class MyTaskTest {
    public static void main(String[] args) {
         try {
             String jobName1 = "动态任务调度";
             System.out.println("【系统启动】开始（每秒输出一次）...");
             TaskManager.addJob(jobName1, MyTaskJob.class, "0/1 * 19 * * ?");
//             Thread.sleep(5000);
//             System.out.println("【修改时间】开始（每2秒输出一次）...");
//             TaskManager.modifyJobTime(jobName1, "10/2 * * * * ?");
//             Thread.sleep(6000);
//             System.out.println("【移除定时任务】开始...");
//             TaskManager.removeJob(jobName1);
//             System.out.println("移除任务【" + jobName1 + "】成功");
//             System.out.println("【再次添加定时任务】开始（每10秒输出一次）...");
//             TaskManager.addJob(jobName1, MyTaskJob.class, "*/10 * * * * ?");
//             Thread.sleep(60000);
//             System.out.println("【移除定时任务】开始...");
//             TaskManager.removeJob(jobName1);
//             System.out.println("移除任务【" + jobName1 + "】成功");
//             TaskManager.shutdownJobs();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
