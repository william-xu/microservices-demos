package org.xwl.msd.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

@SpringBootApplication
@RestController
@NacosPropertySource(dataId = "boot-nacos", autoRefreshed = true)
public class NacosSpringBootApplication {

	@NacosValue(value = "${hello.msg: \"hello, world!\"}", autoRefreshed = true)
	private String msg;
	
	@RequestMapping("/")
	public String home() {
		return msg;
	}

	public static void main(String[] args) {
		SpringApplication.run(NacosSpringBootApplication.class, args);
	}

}


