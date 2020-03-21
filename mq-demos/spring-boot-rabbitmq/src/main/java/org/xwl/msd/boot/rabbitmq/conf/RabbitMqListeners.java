package org.xwl.msd.boot.rabbitmq.conf;

import java.io.IOException;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import com.rabbitmq.client.Channel;

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
	 * 
	 * @param msg
	 */
	@RabbitListener(containerFactory = "primaryContainerFactory", // 设置不持久化，重启rabbitmq-server服务就不存在了
			bindings = @QueueBinding(value = @Queue(value = QUEUE_02, durable = "false", declare = "true"), exchange = @Exchange(value = QUEUE_02_EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC), key = QUEUE_02_ROUTING_KEY))
	public void recieveInput(Message msg, Channel channel) {
		try {
			System.out.println("Message recieved: " + new String(msg.getBody()));
			channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final String QUEUE_03 = "boot_queue_03";

	/**
	 * 监听接收已存在队列“queue_01”上的消息
	 * @param msg
	 * @throws IOException
	 */
	@RabbitListener(containerFactory = "primaryContainerFactory", queues = { QUEUE_03 })
	public void recieveQueue03(@Payload Message msg, Channel channel) throws IOException {
		try {
			System.out.println("Message recieved from " + QUEUE_03 + ": " + new String(msg.getBody()));
			System.out.println("消息不进行确认仍会保留在队列上处于unacked状态。可以在控制台进入对应队列，"
					+ "\nAck Mode选择 Ack message requeue false，设置要获取的message数量，点击Get Message会使用确认模式读取消息。");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
