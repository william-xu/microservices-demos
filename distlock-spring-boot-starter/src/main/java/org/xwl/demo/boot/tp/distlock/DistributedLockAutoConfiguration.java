package org.xwl.demo.boot.tp.distlock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.xwl.demo.boot.tp.distlock.utils.DistributedLock;
import org.xwl.demo.boot.tp.distlock.utils.RedisDistributedLock;

@Configuration
@ConditionalOnProperty(name = {"org.xwl.lock.enable"}, matchIfMissing = false, havingValue = "true")
public class DistributedLockAutoConfiguration {

	
	@Bean	
	@ConditionalOnBean(RedisConnectionFactory.class)
	public DistributedLock distributedLock(RedisConnectionFactory connectionFactory) {
		RedisDistributedLock lock = new RedisDistributedLock();
		lock.setConnectionFactory(connectionFactory);
		return lock;
	}
	
}
