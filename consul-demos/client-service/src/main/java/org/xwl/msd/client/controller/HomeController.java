package org.xwl.msd.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {

	private String msg = "Hello, World!";

	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping("/")
	public String home() {
		String resp = restTemplate.getForObject("http://consul-service/consul-service", String.class);
		return (resp == null ? msg : resp);
	}

	
}
