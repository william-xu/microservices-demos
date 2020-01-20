# microservices-demos项目介绍

放置微服务相关学习示例

# 框架、软件版本
Spring Boot：  2.1.11.RELEASE
Spring Cloud： Greenwich.SR4
Maven： 3.6.0+


# 示例说明

## eureka-demos

包含3个服务

### eureka-server

eureka服务器，默认运行一个节点,可修改配置以3节点集群方式运行

### hello-service

简单的服务测试类，提供一个返回字符串内容的restful接口


### hello-consumer

hello-service的消费服务，三种方式调用hello-service:
* DiscoveryClient
* OpenFeignClient
* RestTemplate


## task-scheduling

定时任务示例
需要MySQL数据库





