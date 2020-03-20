# Spring Boot RabbitMq Demos

## 简介

Spring Boot连接两个RabbitMQ实例
运行前需要在104主机的虚拟主机/demo104上面创建队列boot_queue_01，
/demo250上面的boot_queue_02会自动创建，队列设置为不持久化重启就没了


## 依赖项

### spring-boot-starter-amqp

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>


## 其他说明

1. 不配置主机连接等相关信息时，使用本地地址（localhost)以及默认端口(5672)、默认用户（guest/guest)进行连接配置
2. 指定了虚拟主机地址，需要登录本地RabbitMQ控制台添加并为对应用户设置相应权限
3. 可以登录控制台，在Queues标签页面内点击示例使用的队列，进入队列页面后发送消息进行测试；
   也可以在Exchanges标签页点击示例指定虚拟主机的默认交换器（AMQP default)进入交换器页面，
	在发布消息区的Routing Key文本框输入队列名称，在Payload文本框输入消息后发送测试
4. 需要注意用户有无对应访问虚拟机权限，guest用户只能访问本地地址
5. 默认交换器隐式绑定当前虚拟机所有队列，并且以队列名称作为路由键（The default exchange is implicitly bound to every queue, with a routing key equal to the queue name. It is not possible to explicitly bind to, or unbind from the default exchange. It also cannot be deleted.）



# END
