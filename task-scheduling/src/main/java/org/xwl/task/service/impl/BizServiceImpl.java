package org.xwl.task.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.xwl.task.service.BizService;

@Service
public class BizServiceImpl implements BizService{

	@Override
	public void execute() {
		System.out.println(" Dynamic Spring Scheduled Task executing at -- " 
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));		
	}

}
