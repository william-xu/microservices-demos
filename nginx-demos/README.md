# Nginx Demos
Nginx反向代理示例

## demo-response
接收方项目，使用不同profiles启动

java -jar target\demo-response-01-0.0.1-SNAPSHOT.jar --spring.profiles.active=resp01
java -jar target\demo-response-01-0.0.1-SNAPSHOT.jar --spring.profiles.active=resp02
java -jar target\demo-response-01-0.0.1-SNAPSHOT.jar --spring.profiles.active=resp03

或者
mvn spring-boot:run -Dspring.profile.active=resp01

## demo-request
 请求方项目（在启动前需先启动demo-response项目），启动直接打开项目地址 
 http://localhost:9900/demo-request 
 即会调用接收方项目接口
 http://localhost/demo-response/recieveApi


## nginx服务器端配置

全局配置处添加了日志和pid文件配置：

```
error_log  C:\\temp\\logs\\nginx\\error.log;
pid        C:\\temp\\logs\\nginx\\nginx.pid;

http {
    ...
    #要反向代理的服务器列表
    upstream myapp1 {
        server server1:9901;
        server server2:9902;
        server server3:9903;
    }
    ...
    server {
        listen       80;
		 
		 #监听的是localhost
        server_name  localhost;
        ...
        location / {
            root   html;
            index  index.html index.htm;
            #与反向代理列表名称对应
            proxy_pass http://myapp1;
        }
        ...
    }
    ...
}
```
