# 项目简介

Spring boot consul 简单示例

使用开发模式启动Consul： 
consul agent -data-dir=C:\dataDev\consul\demo -bind=192.168.43.104 -dev -ui



## 配置

添加配置项： config/cloud-consul,dev/data
内容如下，修改相应的ip和数据库配置即可：

```
spring:
  datasource:
    driverClassName: "com.mysql.cj.jdbc.Driver"
    url: "jdbc:mysql://192.168.43.104:3306/demodb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8"
    username: "xwl"
    password: "adm123"
    testWhileIdle: "false"
    validationQuery: "SELECT 1"
```

运行