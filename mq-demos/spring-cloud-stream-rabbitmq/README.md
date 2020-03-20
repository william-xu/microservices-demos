# Spring Cloud Stream RabbitMQ Demos

## 依赖项
使用的Spring Cloud Stream Binder 组件

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-stream-binder-rabbit</artifactId>
		</dependency>

## 配置信息

1. 配置了两个不同主机的RabbitMQ实例
2. 配置两个方法分别接收两个实例的消息
3. 使用@EnableBinding注解进行频道绑定，以及自定义频道接口类
4. 配置多个主机实例：
   spring.cloud.steam.binders.<binder_name>.type
   spring.cloud.steam.binders.<binder_name>.defaultCandidate
   spring.cloud.steam.binders.<binder_name>.enviroment
5. 配置交换器、实例绑定、消息组
   spring.cloud.stream.bindings.<channel_name>.destination
   spring.cloud.stream.bindings.<channel_name>.binder
   spring.cloud.stream.bindings.<channel_name>.group
6. 配置消费者配置
   spring.cloud.stream.rabbit.bindings.<channel_name>.consumer.<configuration_option>
   

# END
