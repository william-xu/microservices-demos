# 配置

## host配置
windows下面： 修改C:\Windows\System32\drivers\etc\host文件，添加以下配置
127.0.0.1       server1
127.0.0.1       server2
127.0.0.1       server3

linux下面是etc/hosts文件

## spring.profiles.active配置

默认active是server1

# 运行
可通过 mvn clean compile spring-boot:run 命令启动eueka server
默认profile是server1
可以通过通过参数指定profile，例如：
 mvn clean compile spring-boot:run --spring.profiles.active=server2

为了方便运行和测试，目前默认是单节点运行

### 集群模式
需要修改配置文件，重新编译后， 打开三个命令行窗口，分别执行以下命令

窗口1：
 java -jar target\eureka-server-0.0.1-SNAPSHOT.jar

窗口2：
 java -jar target\eureka-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=server2

窗口3：
 java -jar target\eureka-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=server3

