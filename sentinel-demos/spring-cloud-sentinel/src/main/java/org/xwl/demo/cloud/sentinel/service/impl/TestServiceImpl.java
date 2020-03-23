package org.xwl.demo.cloud.sentinel.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xwl.demo.cloud.sentinel.service.TestService;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Service
public class TestServiceImpl implements TestService {
	public final Logger logger = LoggerFactory.getLogger(getClass().getName());
	
    @Override    
    public void test() {
    	logger.info("in service Test" + LocalDateTime.now());
    }

    @Override
    public String hello(long s) throws BlockException{
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

	@Override
	@SentinelResource(value = "test2", blockHandler = "exceptionHandler")
	public String test2(long s) {
		return "value in test2:: " + s;
	}

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(long s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }
	
}