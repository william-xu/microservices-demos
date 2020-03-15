package org.xwl.demo.boot.tp.memcached.controller;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xwl.demo.boot.tp.memcached.config.MemcachedConfig;

import net.spy.memcached.MemcachedClient;

@RestController
public class MemcachedController {

	@Autowired
	private MemcachedConfig config;

	@Bean
	public MemcachedClient client() {
		MemcachedClient client = null;
		try {
			client = new MemcachedClient(new InetSocketAddress(config.getHost(), config.getPort()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return client;
	}

	@PostConstruct
	private void init() {
		MemcachedClient client = client();
		//设置过期时间1分20秒
		client.add("key1", 80, "Hello, Memcached!!");
		client.set("key2", 80, "Why, Why, Why ????");
	}

	@RequestMapping("/")
	public String memTest() {
		MemcachedClient client = client();
		String msg = "hello, world!";
		Object msg1 = null;
		Object msg2 = null;
		try {
			msg1 = client.get("key1"); 
			msg2 = client.get("key2");
//			 client.getAndTouch("key2", 30);
			 client.touch("key2", 30);
		}catch(Exception e) {
			System.out.println("读取缓存异常失败：" + e.getMessage());
		}				
		
		Object tmp = msg2 == null ? msg1 : msg2;
		
		if(tmp == null) init();
		
		msg = tmp == null ? msg : String.valueOf(tmp);
		
		return msg;
	}

}
