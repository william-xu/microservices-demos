package org.xwl.task.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;

@EnableScheduling
@Configuration
@ConfigurationProperties("quartz.cust")
public class TaskConfig {
	
	ScheduledTaskRegistrar reg;
	
	private String custCronExpr1;
	
	@Autowired
	private CustRunnable runnable1;

	public String getCustCronExpr1() {
		return custCronExpr1;
	}

	public void setCustCronExpr1(String custCronExpr1) {
		boolean changed = !"".equalsIgnoreCase(this.custCronExpr1) && !custCronExpr1.equalsIgnoreCase(this.custCronExpr1);
		this.custCronExpr1 = custCronExpr1;
		if (changed) {
			ScheduledTaskRegistrarHandler.rescheduleTask(reg, runnable1.getId());
		}
	}
		


	/**
	 * 通过实现SchedulingConfigurer接口创建定时任务
	 */
	@Bean
	public SchedulingConfigurer simpleTask() {

		Trigger trigger = (triggerContext) -> {
			return new CronSequenceGenerator(custCronExpr1).next(new Date());
		};
		SchedulingConfigurer config = (taskRegistrar) -> {
			this.reg = taskRegistrar;
			taskRegistrar.addTriggerTask(runnable1, trigger);
		};
		return config;
	}
	
}
