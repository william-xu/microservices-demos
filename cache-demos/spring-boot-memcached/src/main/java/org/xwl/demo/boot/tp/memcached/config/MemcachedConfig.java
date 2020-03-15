package org.xwl.demo.boot.tp.memcached.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.spy.memcached.spring.MemcachedClientFactoryBean;

@Configuration
@ConfigurationProperties("memcached")
public class MemcachedConfig {
	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Bean
	public MemcachedClientFactoryBean memClientFactoryBean() {
		MemcachedClientFactoryBean factory = new MemcachedClientFactoryBean();
		factory.setServers(getHost()+":" + port);		
		return factory;
	}
}
