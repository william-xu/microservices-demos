package org.xwl.msd.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloApplication {

	@Value("${hello.msg}")
	private String msg;
	
	@RequestMapping("/")
	public String home() {
		System.out.println(" the welcome message is : " + (msg == null ? "Hello world" : msg));
		return msg == null ? "Hello world" : msg;
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}
/*
	@Bean
	public InfoContributor customizedInfo() {
		return new InfoContributor() {
			@Autowired
		    private ApplicationContext ctx;
			
			@Override
			public void contribute(Builder builder) {
				Map<String, Object> details = new HashMap<>();
		        details.put("bean-definition-count", ctx.getBeanDefinitionCount());
		        details.put("startup-date", ctx.getStartupDate());
		        builder.withDetail("context", details);	
			}			
		};
	}
	*/
}
