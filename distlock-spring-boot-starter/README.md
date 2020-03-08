# Spring Boot 分布式锁 starter

Starters are a set of convenient dependency descriptors that you can include in your application. You get a one-stop shop for all the Spring and related technologies that you need without having to hunt through sample code and copy-paste loads of dependency descriptors. For example, if you want to get started using Spring and JPA for database access, include the spring-boot-starter-data-jpa dependency in your project.

The starters contain a lot of the dependencies that you need to get a project up and running quickly and with a consistent, supported set of managed transitive dependencies.

What’s in a name

All official starters follow a similar naming pattern; spring-boot-starter-*, where * is a particular type of application. This naming structure is intended to help when you need to find a starter. The Maven integration in many IDEs lets you search dependencies by name. For example, with the appropriate Eclipse or STS plugin installed, you can press ctrl-space in the POM editor and type “spring-boot-starter” for a complete list.

As explained in the “Creating Your Own Starter” section, third party starters should not start with spring-boot, as it is reserved for official Spring Boot artifacts. Rather, a third-party starter typically starts with the name of the project. For example, a third-party starter project called thirdpartyproject would typically be named thirdpartyproject-spring-boot-starter.



目的：
当项目有使用redis时，自动注入分布式锁工具类


有考虑分布式锁支持不同锁类型的情况，比如zookeeper分布式锁，redis分布式锁，但是如果使用方没有加载对应的依赖如何处理？

先实现Redis分布式锁先


在spring-boot-datasource项目中添加redis和distlock两个starter

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.xwl.demo</groupId>
			<artifactId>distlock-spring-boot-starter</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

在配置文件中添加添加redis配置以及启用lock：

spring.redis.database                 = 0
spring.redis.host                     = 192.168.43.104
spring.redis.port                     = 6379
org.xwl.lock.enable=true

然后就可以使用分布式锁了

需要注意的是，必须设置这个属性为true， 即org.xwl.lock.enable=true 才能使用。



spring-boot-configuration-processor 这个依赖的作用，使用场景，目前还没有概念。


https://docs.spring.io/spring-boot/docs/2.1.12.RELEASE/reference/html/boot-features-developing-auto-configuration.html#boot-features-custom-starter

## 49.5 Creating Your Own Starter
A full Spring Boot starter for a library may contain the following components:

* The autoconfigure module that contains the auto-configuration code.
* The starter module that provides a dependency to the autoconfigure module as well as the library and any additional dependencies that are typically useful. In a nutshell, adding the starter should provide everything needed to start using that library.

**You may combine the auto-configuration code and the dependency management in a single module if you do not need to separate those two concerns.**











