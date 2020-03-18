package org.xwl.msd.canal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xwl.msd.canal.model.User;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.FlatMessage;

@Service
@RocketMQMessageListener(topic = "${demo.rocketmq.topic}", consumerGroup = "example_consumer")
public class ExampleConsumer implements RocketMQListener<String> {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
    @Override
    public void onMessage(String message) {

    	System.out.printf("------- Message received: %s \n", message);
    	
    	try {
    		FlatMessage msg = JSON.parseObject(message, FlatMessage.class);
    		List<Map<String,String>>ds = msg.getData();

    		Map<String,String> insertOrUpdate = new HashMap<>();
    		ArrayList<String> delList = new ArrayList<>();
    		ds.forEach((m)->{
    			if("user".equalsIgnoreCase(msg.getTable())) {
    				String userStr = JSON.toJSONString(m);
        			User u = JSON.parseObject(userStr,User.class);
        			if(u != null) System.out.println(u);
        			if("INSERT".equalsIgnoreCase(msg.getType()) || "UPDATE".equalsIgnoreCase(msg.getType())) {
        				insertOrUpdate.put(u.getUserId(), userStr);
        			}else if("DELETE".equalsIgnoreCase(msg.getType())) {
        				delList.add(u.getUserId());
        			}        			
        		}
    		});
    		//删除
    		if(delList.size()>0) {
    			Object[] users = new String[delList.size()];
    			delList.toArray(users);
    			redisTemplate.opsForHash().delete("users", users);    			
    		}
    		//新增或更新
    		if(insertOrUpdate.size()>0) {
    			redisTemplate.opsForHash().putAll("users", insertOrUpdate);	
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    }
}