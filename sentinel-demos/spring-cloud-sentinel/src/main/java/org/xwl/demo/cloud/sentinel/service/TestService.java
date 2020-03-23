package org.xwl.demo.cloud.sentinel.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public interface TestService {

    void test();

    String hello(long s) throws BlockException;

    String helloAnother(String name);
    
    String test2(long s);
}
