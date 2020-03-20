package org.xwl.msd.boot.rabbitmq.conf;

import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMqConfig {

	@Autowired
	public RabbitListenerAnnotationBeanPostProcessor rabbitPostProcessor;

	///////////////////////////////////////////////////////////////////////////////
	// 默认配置(连接本地）
	///////////////////////////////////////////////////////////////////////////////
	@Bean
	@ConfigurationProperties(prefix = "spring.rabbitmq")
	public RabbitProperties defaultRabbitMqProperties() {
		return new RabbitProperties();
	}

	@Bean(name = "defaultRabbitFactory")
	public ConnectionFactory defaultRabbitConnFactory() {
		return createRabbitConnectionFactory(defaultRabbitMqProperties());
	}

	@Bean(name = "defaultContainerFactory")
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> defaultContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer) {
		SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(containerFactory, defaultRabbitConnFactory());
		return containerFactory;
	}

	///////////////////////////////////////////////////////////////////////////////
	// 使用自定义配置作为首要配置
	///////////////////////////////////////////////////////////////////////////////
	@Bean
	@Primary
	@ConfigurationProperties("cust.rabbitmq")
	public RabbitProperties primaryRabbitMqProperties() {
		return new RabbitProperties();
	}

	@Bean(name = "primaryRabbitFactory")
	@Primary
	public ConnectionFactory primaryRabbitFactory() {
		return createRabbitConnectionFactory(primaryRabbitMqProperties());
	}

	@Bean("primaryContainerFactory")
	@Primary
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> primaryContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer) {
		SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(containerFactory, primaryRabbitFactory());
		return containerFactory;
	}

	///////////////////////////////////////////////////////////////////////////////
	// 公用方法
	///////////////////////////////////////////////////////////////////////////////
	private ConnectionFactory createRabbitConnectionFactory(RabbitProperties props) {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(props.getHost());
		factory.setUsername(props.getUsername());
		factory.setPassword(props.getPassword());
		factory.setPort(props.getPort());
		factory.setVirtualHost(props.getVirtualHost());
		return factory;
	}

}
