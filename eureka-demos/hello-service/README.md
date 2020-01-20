# 项目简介

1. 只提供一个简单的RestFul接口
2. 服务配置了注册到eureka注册中心
3. 服务有两个profile配置：hello1和hello2，不同的profile中变量hello.msg的内容不一样

# 其他

1. application.properties从maven pom文件中获取配置属性值
2. 在application.yml中使用application.properties中定义的属性变量
3. 配置了actuator/info接口信息