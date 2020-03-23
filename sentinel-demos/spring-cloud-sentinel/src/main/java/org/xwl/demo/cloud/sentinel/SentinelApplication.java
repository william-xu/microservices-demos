package org.xwl.demo.cloud.sentinel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class SentinelApplication {

    @Value("${homeText: default_msg}")
	private String homeText;
    
    @Value("${extendedValue: extendedValue}")
	private String extendedValue;
     
	@RequestMapping("/")
	public String home() {
		if(homeText == null || homeText.trim().length()==0) {
			homeText = "value not set!";
		} 
		return homeText + " ::: " + "[" + extendedValue + "]";
	}

	public static void main(String[] args) {
		SpringApplication.run(SentinelApplication.class, args);
	}

}

