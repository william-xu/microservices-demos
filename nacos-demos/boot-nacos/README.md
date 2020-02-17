# 项目简介

Spring boot 应用程序使用nacos作为配置中心和服务注册中心

在Spring Boot Nacos中，与Nacos服务器端Data ID的值是强匹配
即注解@NacosPropertySource(dataId = "boot-nacos", autoRefreshed = true) 中的dataId属性值与服务器端的Data ID值一致

在 Spring Cloud Nacos中，Nacos服务器端的Data ID必须是应用名称+后缀的格式，即<spring.application.name>.<file-extension>
应用配置处spring.cloud.nacos.config.data-id配置项不起作用。