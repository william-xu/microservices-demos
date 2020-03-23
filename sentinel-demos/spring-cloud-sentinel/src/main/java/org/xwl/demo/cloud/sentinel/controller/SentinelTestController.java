package org.xwl.demo.cloud.sentinel.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xwl.demo.cloud.sentinel.service.TestService;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@RestController
public class SentinelTestController {

	@Autowired
    private TestService service;
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@GetMapping("/hello")
	@SentinelResource(value = "hello")
	public String hello() throws BlockException {
		return service.hello(counter.addAndGet(1));
	}

	@GetMapping(value = "/test1", produces = "application/json")	
	@SentinelResource("test1") 
	public void test1() throws BlockException {
		service.test();
	}
	
	
	@GetMapping(value = "/test2", produces = "application/json")	
	public String test2(@RequestParam long s) {
		return service.test2(s);
	}	
	
}
