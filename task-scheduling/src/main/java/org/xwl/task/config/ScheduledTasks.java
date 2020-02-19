package org.xwl.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xwl.task.service.BizService;

@Configuration
public class ScheduledTasks {
	
	final String CURR_JOB_ID_01 = "CustJob01";
	
	@Autowired
	private BizService service;
	
	@Bean(name = "CustJob01")
	public CustRunnable runnable1() {
		return new CustRunnable() {
			@Override
			public void run() {
				service.execute();
			}
			@Override
			public String getId() {
				return CURR_JOB_ID_01;
			}
		};	
	}
	
	
	
}
