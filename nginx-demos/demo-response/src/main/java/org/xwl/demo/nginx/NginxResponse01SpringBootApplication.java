package org.xwl.demo.nginx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NginxResponse01SpringBootApplication {

	@Value("${spring.application.name}")
	private String appName;
	
	@RequestMapping("/")
	public String home() {
		return appName;
	}

	@RequestMapping(value = "/recieveApi", method = RequestMethod.POST)
	public String recieveApi(@RequestParam(value="msg", required=false) String msg) {
		return "msg from " + appName;
	}	
	
	public static void main(String[] args) {
		SpringApplication.run(NginxResponse01SpringBootApplication.class, args);
	}


}




