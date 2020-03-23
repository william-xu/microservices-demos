package org.xwl.demo.cloud.sentinel.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xwl.demo.cloud.sentinel.service.impl.TestServiceImpl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@RestController
public class SentinelTestController {

	@Autowired
    private TestServiceImpl service;
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@GetMapping("/hello")
	public String hello() {
		return service.hello(counter.addAndGet(1));
	}

	
	@SentinelResource("test1")
	@GetMapping(value = "/test1", produces = "application/json")
	@ResponseBody
	public String test1() {
		return "this path is blocked";
	}
	
	
	@GetMapping(value = "/test2", produces = "application/json")
	@SentinelResource(value = "test2", blockHandler = "exceptionHandler", fallback = "helloFallback")
	public String testWithDefaultFallback(@RequestParam long s) {
		service.test();
		return "return msg";
	}
	
	public void defaultFallback() {
        System.out.println("Go to default fallback");
    }
	
	// Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String helloFallback(long s) {
        return String.format("Halooooo %d", s);
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(long s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }
	
	
}
