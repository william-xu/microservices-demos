package org.xwl.msd.boot.rabbitmq.conf;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;


/**
 * 当前配置类接收2个队列消息
 * queue_01 来自默认配置（104），需要先创建
 * queue_02 来自首要配置（250），会自动创建
 * 
 * 
 * @author xwl
 *
 */
@Configuration
public class RabbitMqListeners {
	
	////////////////////////////////////////////////////////////////
	// 连接默认的RabbitMQ服务器（104）
	////////////////////////////////////////////////////////////////
	private static final String QUEUE_01 = "boot_queue_01";
	/**
	 * 监听接收已存在队列“queue_01”上的消息
	 * @param msg
	 */
	@RabbitListener(containerFactory = "defaultContainerFactory", queues = { QUEUE_01 })
	public void recieve(Message msg) {
		System.out.println("Message recieved from " + QUEUE_01 + ": " + new String(msg.getBody()));
	}
	
	private static final String QUEUE_02 = "boot_queue_02";
	private static final String QUEUE_02_EXCHANGE = "bootExchange";
	private static final String QUEUE_02_ROUTING_KEY = "*.demo";

	/**
	 * 会自动创建名称为demoExchange、类型是topic的并且将消息持久化的交换器，创建名称为boot_queue_02的队列绑定到demoExchange交换器，
	 * 如果发送到demoExchange交换器的路由键是“*.demo”格式，则会发送到此队列
	 * @param msg
	 */
	@RabbitListener(containerFactory = "primaryContainerFactory",     //设置不持久化，重启rabbitmq-server服务就不存在了
			bindings = @QueueBinding(value = @Queue(value = QUEUE_02, durable = "false", declare = "true"),
									 exchange = @Exchange(value = QUEUE_02_EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
									 key = QUEUE_02_ROUTING_KEY)
	)
	public void recieveInput(Message msg) {
		System.out.println("Message recieved: " + new String(msg.getBody()));
	}	
	
}
