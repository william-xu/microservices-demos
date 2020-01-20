package org.xwl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xwl.task.config.TaskConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RestController
public class TaskSchedulingApplication {

	@Autowired
	private TaskConfig taskConf;
	
	@GetMapping("/")
	public String home() {
		return "Current Cron Expression is :" + taskConf.getCustCronExpr();
	}	
	
	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulingApplication.class, args);
	}

}
