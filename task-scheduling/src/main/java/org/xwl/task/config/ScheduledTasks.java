package org.xwl.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xwl.task.service.BizService;
import org.xwl.task.service.impl.OtherBizServiceImpl;

@Configuration
public class ScheduledTasks {
	
	final String CURR_JOB_ID_01 = "CustJob01";
	final String CURR_JOB_ID_02 = "CustJob02";
	
	@Autowired
	private BizService service;

	@Autowired
	private OtherBizServiceImpl otherService;
	
	
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
	
	@Bean(name = "CustJob02")
	public CustRunnable runnable2() {
		return new CustRunnable() {
			@Override
			public void run() {
				otherService.execute();
			}
			@Override
			public String getId() {
				return CURR_JOB_ID_02;
			}
		};	
	}	
	
}
