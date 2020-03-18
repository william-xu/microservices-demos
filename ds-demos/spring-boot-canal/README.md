# Spring Boot应用使用canal、rocketmq同步mysql数据库


1. mysql数据库开启日志，设置要同步的数据库
2. 配置canal要同步的mysql数据库地址，配置使用rocketmq进行同步
3. 启动rocketmq服务以及canal服务
4. 项目配置rocketmq地址，订阅的主题，redis相关配置，并启动
5. 这里使用user表作为示例，在数据库中创建该表后进行增删改，然后到redis中查看数据更新情况

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci




