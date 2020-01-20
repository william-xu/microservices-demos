## 简介 
这个个简单项目主要用来介绍如何在Spring Boot项目中进行任务调度安排，主要有两种方式：
1. 使用Spring框架自身的任务调度功能来实现
2. 使用Quartz框架来实现


## 软件版本 
Java版本： 1.8
SpringBoot版本: 2.1.11.RELEASE


## Spring任务
配置了3种任务：
* 使用注解创建的，按照固定频率执行的任务： @Scheduled(fixedRate = 1000 * 15)
* 使用注解创建的，在设定时间点执行的任务：@Scheduled(cron = "0/28 * * * * ?")
* 实现SchedulingConfigurer接口创建的任务

## nacos上项目的配置
 Data ID: task-scheduling.yml 

 Group: DEFAULT_GROUP 

```
quartz:
  cust:
    custCronExpr: 0/33 * * * * ?
```

## Quartz任务
这次我们使用@EnableScheduling注解来引入Quartz以及相关依赖。
由于我们需要保存任务相关信息，这里使用的MySQL数据库进行存储，需要进行的准备：
1. MySQL数据库，启动的nacos服务器端
2. 项目pom添加相关依赖项 （ quartz,mysql,lombok,swagger2,jpa,spring cloud nacos配置依赖）
3. 配置数据库连接
4. Quartz数据库表文件
	从quartz发布包中获得数据库表相关脚本文件并放到resources目录下面，记得移除文件中的注释内容，否则可能会导致脚本执行失败而出现找不到表的错误
5. 编写相应实现类
	使用jpa操作数据库，建立相关model、dao和控制器以及自定义的任务类（定时执行的业务逻辑）

## 测试
使用swagger2生成简单的接口文档，可以通 http://localhost:18080/swagger-ui.html 页面查看并测试操作定时任务


## 两种方式比较
这里实现的Spring任务在应用启动后会一直执行，需要停止应用后才能修改定时执行时间
使用Quart实现的任务（如果不使用MySQL也可以使用内置数据库）使用MySQL持久化,并且可以对其进行控制（创建新任务，暂定、删除已经重设定时表达式），功能要丰富和强大。

使用Spring实现的任务也可以将表达式配置在配置中心，通过配置自动刷新来实现动态重设定时表达式，需要注意的是通过配置刷新实现的动态任务，
由于设计原因新的定时表达式要下次执行时才能生效，需要自己编写代码控制实时变更，同样也可以自己编写代码通过接口来终止任务。

## 重新安排任务
使用Nacos做配置中心并采用Spring Cloud方式连接，配置定时任务表达式属性自动刷新
在属性变更时通过反射获取任务注册对象，取消正在已安排的任务，并对以及更新定时表达式的任务进行安排

不推荐使用这种方式来实现重新安排任务，但对可以加深对Spring定时任务调度安排的理解。
