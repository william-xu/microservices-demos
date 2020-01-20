package org.xwl.task.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xwl.task.config.CustJob;
import org.xwl.task.dao.JobInfoRepository;
import org.xwl.task.model.JobInfo;
import org.xwl.task.model.JobInfoKey;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/job")
@Api(value="动态定时任务操作接口类")
public class QuartzJobController {

	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	/**
	 * 添加定时任务
	 * @param jobClassName： 自定义的任务类名称，例如：CustJob，不需要包和.class后缀
	 * @param jobGroupName：自定义的任务组，可随意设置
	 * @param cronExpression： 定时表达式
	 * @throws Exception
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value="添加定时任务", notes="jobClassName为自定义的任务类，只需要输入类名即可，不需包含完整路径和.class后缀")
	public boolean addjob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName,
			@RequestParam(value = "cronExpression") String cronExpression) throws Exception {

		try {
			// 启动调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.start();
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(CustJob.class).withIdentity(jobClassName, jobGroupName).build();
			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
					.withSchedule(scheduleBuilder).build();
			
			scheduler.scheduleJob(jobDetail, trigger);

		} catch (SchedulerException e) {
			System.out.println("创建定时任务失败" + e);
			throw new Exception("创建定时任务失败");
		}	
		return true;
	}

	/**
	 * 对现有任务重新安排
	 * @param jobClassName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	@PostMapping(value = "/reschedule")
	@ApiOperation(value="重新安排定时任务", notes="如果先暂停后再重安排可能会更好")
	public boolean reschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		try {
			System.out.println("进入更新定时任务方法：：" + LocalDateTime.now());			
			Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			// 表达式调度构建器			
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 按新的cronExpression表达式重新构建trigger
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionFireAndProceed();
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			System.out.println("更新定时任务失败" + e);
			throw new Exception("更新定时任务失败");
		}
		return true;
	}
	
	@ApiOperation(value="暂停指定定时任务执行")
	@PostMapping(value = "/pause")
	public boolean pausejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
		schedulerFactory.getScheduler().pauseJob(JobKey.jobKey(jobClassName, jobGroupName));	
		return true;		
		
	}
	
	@ApiOperation(value="继续指定定时任务执行")
	@PostMapping(value = "/resume")
	public boolean resumejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
		schedulerFactory.getScheduler().resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
		return true;
	}		

	@PostMapping(value = "/delete")
	public boolean deletejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {

		schedulerFactory.getScheduler().pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		schedulerFactory.getScheduler().unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		schedulerFactory.getScheduler().deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
		System.out.println("Job-" + jobClassName + "-" + jobGroupName + " is deleted.");
		return true;
	}
		
	@Autowired
	private JobInfoRepository repo;
	
	@ApiOperation(value="根据主键查询定时任务")
	@GetMapping(value = "/queryByKey")
	public Optional<JobInfo> queryjobByKey(@RequestBody JobInfoKey key) {
		
		return repo.findByKey(key.getSchedName(), key.getJobName(), key.getJobGroup());
	}
	
}
