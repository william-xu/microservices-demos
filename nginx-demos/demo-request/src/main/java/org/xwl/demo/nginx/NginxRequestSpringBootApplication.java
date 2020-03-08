package org.xwl.demo.nginx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class NginxRequestSpringBootApplication {
	
	@RequestMapping("/")
	public String home() {
		//使用RestTemplate调用接收方地址，接收方使用nginx进行负载均衡
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> restExchange = restTemplate.exchange("http://localhost/demo-response/recieveApi", HttpMethod.POST, null, String.class);
		return restExchange.getBody();
	}

	public static void main(String[] args) {
		SpringApplication.run(NginxRequestSpringBootApplication.class, args);
	}


}




