package org.xwl.task.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 自定义的动态配置任务类
 * 
 * @author xwl
 *
 */
public class CustJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 这里是固定的业务逻辑
		System.out.println(
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " -- Dynamic Quartz Job Biz Logic !! ");
	}
}
