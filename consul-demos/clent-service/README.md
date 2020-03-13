# 项目简介

Spring boot consul 简单示例

启动Consul,可参考以下命令： 
consul.exe agent -server -bootstrap-expect=1 -ui -client=0.0.0.0 -bind=192.168.43.104 -data-dir=C:\dataDev\consul\demo


## 配置

添加配置项： config/cloud-consul,server1/data
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

## 运行
可打开两个命令行启动两个实例
mvn spring-boot:run
mvn spring-boot:run --server.port=9104

启动客户端服务，访问： http://localhost:9199/client-service/
刷新测试负载均衡


