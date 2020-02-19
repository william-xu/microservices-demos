package org.xwl.task.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xwl.task.service.TaskService;

@Configuration
public class ScheduledTasks {

	final String CURR_JOB_ID_01 = "CustJob01";
	final String CURR_JOB_ID_02 = "CustJob02";

	@Autowired
	private TaskService service;

	private TaskService otherService = () -> {
		System.out.println("Simple Dynamic Spring Scheduled Task executing at -- "
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	};

	@Bean(name = "CustJob01")
	public CustRunnable runnable1() {
		return createCustRunnable(service, CURR_JOB_ID_01);
	}

	@Bean(name = "CustJob02")
	public CustRunnable runnable2() {
		return createCustRunnable(otherService, CURR_JOB_ID_02);
	}

	private CustRunnable createCustRunnable(TaskService service, String jobId) {
		return new CustRunnable() {
			@Override
			public void run() {
				service.execute();
			}
			@Override
			public String getId() {
				return jobId;
			}
		};
	}

}
