# 项目简介

Spring Cloud应用程序使用nacos作为配置注册中心和服务注册中心

### Data ID格式
Nacos服务器端Data ID格式:  <spring.application.name>[-<profile>].<file-extention>

例如添加Data ID = cloud-nacos.yml 的配置，那么对应的应用程序配置为：

spring.application.name=cloud-nacos
spring.cloud.nacos.config.file-extension=yml

例如应用程序使用spring.profiles.active=dev或者通过命令行指定--spring.profiles.active=dev时

那么Nacos服务器端对应配置的Data ID名称是 cloud-nacos-dev.yml


# 其他

