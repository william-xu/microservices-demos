package org.xwl.msd.canal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xwl.msd.canal.model.User;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.FlatMessage;

@Service
@RocketMQMessageListener(topic = "${demo.rocketmq.topic}", consumerGroup = "example_consumer")
public class ExampleConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 按序消费
	 * 如果异常消息会重发
	 */
	@Override
	public void prepareStart(DefaultMQPushConsumer consumer) {
		// Register callback to execute on arrival of messages fetched from brokers.
		consumer.registerMessageListener(new MessageListenerOrderly() {
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				
				System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
				MessageExt msg;
				for (int i = 0; i < msgs.size(); i++) {
					msg = msgs.get(i);
					//可以通过消息id来保证不重复消费？
					System.out.println(msg.getBornTimestamp() + "--" + msg.getMsgId());
					FlatMessage flatMsg = JSON.parseObject(new String(msg.getBody()), FlatMessage.class);
					List<Map<String, String>> ds = flatMsg.getData();

					Map<String, String> insertOrUpdate = new HashMap<>();
					ArrayList<String> delList = new ArrayList<>();
					ds.forEach((m) -> {
						if ("user".equalsIgnoreCase(flatMsg.getTable())) {
							String userStr = JSON.toJSONString(m);
							User u = JSON.parseObject(userStr, User.class);
							if (u != null)
								System.out.println(u);
							if ("INSERT".equalsIgnoreCase(flatMsg.getType())
									|| "UPDATE".equalsIgnoreCase(flatMsg.getType())) {
								insertOrUpdate.put(u.getUserId(), userStr);
							} else if ("DELETE".equalsIgnoreCase(flatMsg.getType())) {
								delList.add(u.getUserId());
							}
						}
					});

					/* 正常数据库一次变更不会同时有更新和删除，所以下面的操作只会执行一个，如果失败抛出异常的话RocketMQ会重发，需要设置重发以及考虑如何失败处理 */					
					// 新增或更新
					if (insertOrUpdate.size() > 0) {
						redisTemplate.opsForHash().putAll("users", insertOrUpdate);
						System.out.println(Thread.currentThread().getName() + "insert or update");
					}
					// 删除
					if (delList.size() > 0) {
						Object[] users = new String[delList.size()];
						delList.toArray(users);
						redisTemplate.opsForHash().delete("users", users);
						System.out.println(Thread.currentThread().getName() + "delete");
					}
				}

				return ConsumeOrderlyStatus.SUCCESS;
			}
		});
	}

	/**
	 * 如果上面的prepareStart方法为空，才会调用这个onMessage方法，如果消息被上面的DefaultMQPushConsumer消费了则不会调用
	 */
	@Override
	public void onMessage(MessageExt message) {
		System.out.printf("------- Message received: %s \n", message);
	}

}