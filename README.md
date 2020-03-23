# microservices-demos项目介绍

放置微服务相关学习示例

# 框架、软件版本
 Spring Boot：  2.1.11.RELEASE
 Spring Cloud： Greenwich.SR4
 Maven： 3.6.0+
 

# 示例说明

## eureka-demos

包含3个服务

#### eureka-server

eureka服务器，默认运行一个节点,可修改配置以3节点集群方式运行

#### hello-service

简单的服务测试类，提供一个返回字符串内容的restful接口


#### hello-consumer

hello-service的消费服务，三种方式调用hello-service:
* DiscoveryClient
* OpenFeignClient
* RestTemplate


## task-scheduling

定时任务示例
需要MySQL数据库,Nacos配置中心


## seata-demos
seata相关示例

#### spring-cloud-nacos-seata



## distlock-spring-boot-starter 

分布式锁spring boot starter简单示例


## security-demos
安全相关示例

#### spring-boot-sample-web-secure-custom
spring security

#### spring-boot-shiro

shiro


## consul-demos
consul相关示例

#### consul-service

读取consul配置以及注册到consul上面的服务


#### client-service
调用consul服务的客户端

## cache-demos

#### spring-boot-memcached
memcached 连接以及简单操作

 
## ds-demos
数据库相关

#### spring-boot-canal
使用canal以及rocketmq同步mysql数据库更新到redis

#### spring-boot-druid
druid的简单使用


## nginx-demos

#### demo-response
nginx代理的启动多个实例的接收项目

#### demo-request
客户端调用demo-response的nginx代理地址。


## mq-demos

#### spring-cloud-stream-rabbitmq
使用Spring cloud stream binder 
接收两个主机rabbitmq实例的消息


#### spring-boot-rabbitmq

Spring boot 应用连接rabbitmq


## sentinel-demos

#### spring-cloud-sentinel

Spring Cloud使用sentinel进行流量限制，规则配置在nacos上可动态修改









