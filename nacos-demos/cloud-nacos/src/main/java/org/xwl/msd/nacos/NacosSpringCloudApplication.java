package org.xwl.msd.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@RefreshScope
public class NacosSpringCloudApplication {

	@Value("${hello.msg}")
	private String msg;
	
	@RequestMapping("/")
	public String home() {
		System.out.println(" the welcome message is : " + (msg == null ? "Hello world" : msg));
		return msg == null ? "Hello world" : msg;
	}

	public static void main(String[] args) {
		SpringApplication.run(NacosSpringCloudApplication.class, args);
	}

}
