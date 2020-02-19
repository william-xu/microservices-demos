package org.xwl.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
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
		return "Current Cron Expression is :" + taskConf.getCustCronExpr1();
	}	
	
	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulingApplication.class, args);
	}

	/**
	 * 定时表达式的方式实现简单定时任务
	 */
	@Scheduled(cron = "0/28 * * * * ?")
	public void hello2() {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				+ " ---- Cron Task per 28 second in 1 minute [0, 28, 56]");
	}

	/**
	 * 固定频率的方式，下面是每15秒执行一次
	 */
	@Scheduled(fixedRate = 1000 * 15)
	public void hello1() {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				+ " ---- Simple Task per 15 second");
	}
	
	
}
