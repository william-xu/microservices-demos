package org.xwl.msd.hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableFeignClients
@EnableDiscoveryClient
public class HelloConsumerApplication {

	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private HelloFeignClient feignClient;
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	
	@RequestMapping("/")
	public String home() {
		System.out.println("===================DiscoveryClient=====================");
		testDiscoveryClient();
		System.out.println("===================RestTemplate========================");
		testRestTemplate();
		System.out.println("===================FeignClient=========================");
		testFeignClient();
		return "done";
	}
	
	
	/**
	 * 使用自动注入RestTemplate并配置为负载均衡后，可通过服务id来调用接口方法
	 */
	protected void testRestTemplate() {
		String serviceName = "hello-service";
		String contextPath = "hello-service";
		ResponseEntity<String> resp;
		for(int i=0;i<6;i++) {
			resp = getRestTemplate().exchange("http://" + serviceName + "/" + contextPath, HttpMethod.GET, null, String.class);
			System.out.println(resp.getBody());
		}		
	}
	
	/**
	 * 使用OpenFeign客户端需要创建接口类以及对应的接口方法
	 */
	protected void testFeignClient() {
		for(int i=0;i<6;i++) {
			System.out.println("第" + i + "次获取：" + feignClient.getHelloServiceHome());
		}		
	}
	
	protected void testDiscoveryClient() {
		List<ServiceInstance> instances = discoveryClient.getInstances("hello-service");
		RestTemplate restTemplate = new RestTemplate();
		for(ServiceInstance instance: instances) {
			System.out.println(">>>>>" + instance.getUri().toString()+"-"+instance.getInstanceId()+"-"+instance.getServiceId());
			ResponseEntity<String> resp = restTemplate.exchange(instance.getUri().toString()+"/" + instance.getServiceId().toLowerCase(), HttpMethod.GET, null, String.class);
			System.out.println("response:: " + resp.getBody());
		}
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(HelloConsumerApplication.class, args);
	}

	/**
	 * OpenFeign客户端，如果服务有设置上下文路径(contextPath)时，需要设置path参数
	 * @author xwl
	 *
	 */
	@FeignClient(value = "hello-service", path = "/hello-service")
	public interface HelloFeignClient {
	    @RequestMapping(method= RequestMethod.GET, value="/", consumes="application/text")
	    String getHelloServiceHome();
	}	
	
}




