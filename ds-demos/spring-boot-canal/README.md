# Spring Boot应用使用canal、rocketmq同步mysql数据库


1. mysql数据库开启日志，设置要同步的数据库
2. 配置canal要同步的mysql数据库地址，配置使用rocketmq进行同步
3. 启动rocketmq服务以及canal服务
4. 项目配置rocketmq地址，订阅的主题，redis相关配置，并启动
5. 这里使用user表作为示例，在数据库中创建该表后进行增删改，然后到redis中查看数据更新情况

```
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```


### 注意事项

1. 如果启动rocketmq-console时有出现类似下面的错误：
 DESC: No topic route info in name server for the topic: %RETRY%
  * 解决方法： 启动mqbroker时添加 autoCreateTopicEnable=true 参数
2. 如果canal有更新但没同步到mq，且查看日志有下面这种错误信息的话，可能是磁盘可用空间较少。
	CODE: 14  DESC: service not available now, maybe disk full, CL:  0.93 CQ:  0.93 INDEX:  0.93, maybe your broker machine memory too small.
  * 解决方法： windows下面可以尝试在runbroker.cmd中添加设置磁盘警戒水平比率参数替换默认设置，如果使用空间超过这个比率会拒绝接收新消息。
   set "JAVA_OPT=%JAVA_OPT% -Drocketmq.broker.diskSpaceWarningLevelRatio=0.95"




