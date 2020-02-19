package org.xwl.task.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.xwl.task.service.OtherBizService;

@Service
public class OtherBizServiceImpl implements OtherBizService{

	@Override
	public void execute() {
		System.out.println("Another Dynamic Spring Scheduled Task executing at -- " 
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}

}
