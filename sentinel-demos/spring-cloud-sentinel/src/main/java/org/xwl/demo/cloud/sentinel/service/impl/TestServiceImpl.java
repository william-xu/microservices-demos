package org.xwl.demo.cloud.sentinel.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xwl.demo.cloud.sentinel.service.TestService;

@Service
public class TestServiceImpl implements TestService {
	public final Logger logger = LoggerFactory.getLogger(getClass().getName());
	
    @Override    
    public void test() {
    	logger.info("in service Test" + LocalDateTime.now());
    }

    @Override
    public String hello(long s) {
        return String.format("Hello at %d", s);
    }

    @Override
    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

}