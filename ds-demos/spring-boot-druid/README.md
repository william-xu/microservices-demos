# Spring Boot Druid 示例

文档参考：https://github.com/alibaba/druid/wiki


一些配置参考：

```
 <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close"> 
     <property name="url" value="${jdbc_url}" />
     <property name="username" value="${jdbc_user}" />
     <property name="password" value="${jdbc_password}" />

     <property name="filters" value="stat" />

     <property name="maxActive" value="20" />
     <property name="initialSize" value="1" />
     <property name="maxWait" value="60000" />
     <property name="minIdle" value="1" />

     <property name="timeBetweenEvictionRunsMillis" value="60000" />
     <property name="minEvictableIdleTimeMillis" value="300000" />

     <property name="testWhileIdle" value="true" />
     <property name="testOnBorrow" value="false" />
     <property name="testOnReturn" value="false" />

     <property name="poolPreparedStatements" value="true" />
     <property name="maxOpenPreparedStatements" value="20" />

     <property name="asyncInit" value="true" />
 </bean>
```

