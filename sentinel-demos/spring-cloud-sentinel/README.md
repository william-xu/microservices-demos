# spring-cloud-sentinel

Spring Cloud使用sentinel进行限流，配置保存在nacos上，同时也有nacos多配置使用示例。
(未完成)


## 动态数据源支持
https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel

```
#文件
spring.cloud.sentinel.datasource.ds1.file.file=classpath: degraderule.json
spring.cloud.sentinel.datasource.ds1.file.rule-type=flow

#spring.cloud.sentinel.datasource.ds1.file.file=classpath: flowrule.json
#spring.cloud.sentinel.datasource.ds1.file.data-type=custom
#spring.cloud.sentinel.datasource.ds1.file.converter-class=com.alibaba.cloud.examples.JsonFlowRuleListConverter
#spring.cloud.sentinel.datasource.ds1.file.rule-type=flow

#nacos
spring.cloud.sentinel.datasource.ds2.nacos.server-addr=localhost:8848
spring.cloud.sentinel.datasource.ds2.nacos.data-id=sentinel
spring.cloud.sentinel.datasource.ds2.nacos.group-id=DEFAULT_GROUP
spring.cloud.sentinel.datasource.ds2.nacos.data-type=json
spring.cloud.sentinel.datasource.ds2.nacos.rule-type=degrade

#zookeeper
spring.cloud.sentinel.datasource.ds3.zk.path = /Sentinel-Demo/SYSTEM-CODE-DEMO-FLOW
spring.cloud.sentinel.datasource.ds3.zk.server-addr = localhost:2181
spring.cloud.sentinel.datasource.ds3.zk.rule-type=authority

#apollo
spring.cloud.sentinel.datasource.ds4.apollo.namespace-name = application
spring.cloud.sentinel.datasource.ds4.apollo.flow-rules-key = sentinel
spring.cloud.sentinel.datasource.ds4.apollo.default-flow-rule-value = test
spring.cloud.sentinel.datasource.ds4.apollo.rule-type=param-flow
```

### Note
d1, ds2, ds3, ds4 是 ReadableDataSource 的名字，可随意编写。后面的 file ，zk ，nacos , apollo 就是对应具体的数据源，它们后面的配置就是这些具体数据源各自的配置。注意数据源的依赖要单独引入（比如 sentinel-datasource-nacos)。
每种数据源都有两个共同的配置项： data-type、 converter-class 以及 rule-type。

data-type 配置项表示 Converter 类型，Spring Cloud Alibaba Sentinel 默认提供两种内置的值，分别是 json 和 xml (不填默认是json)。 如果不想使用内置的 json 或 xml 这两种 Converter，可以填写 custom 表示自定义 Converter，然后再配置 converter-class 配置项，该配置项需要写类的全路径名(比如 spring.cloud.sentinel.datasource.ds1.file.converter-class=com.alibaba.cloud.examples.JsonFlowRuleListConverter)。

rule-type 配置表示该数据源中的规则属于哪种类型的规则(flow，degrade，authority，system, param-flow, gw-flow, gw-api-group)。


### 其他备忘

**拒绝访问**
http://localhost:12403/cloud-sentinel/test1   
**设置QPS访问限制**
http://localhost:12403/cloud-sentinel/hello

**设置fallback和异常处理**
http://localhost:12403/cloud-sentinel/test2
（未完成)



### nacos上的配置

**配置一：**
Data ID: cloud-sentinel.yaml
Group:   DEFAULT_GROUP

使用yaml格式，配置项只有一个：

`homeText: "Hello, Sentinel!"`

**配置二：**

Data ID: sentinel
Group:   sentinel-group

使用JSON格式，配置内容如下：

```
[
  {
    "resource": "/hello",
    "controlBehavior": 0,
    "count": 2,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0
  },
  {
    "resource": "/test1",
    "controlBehavior": 0,
    "count": 0,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0
  },
  {
    "resource": "/test2",
    "controlBehavior": 0,
    "count": 2,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0
  }
]
```

**配置三：**

Data ID: sentinel-multi1.properties
Group:   sentinel-group

使用Properties格式，配置内容如下：

`extendedValue=" value from extended configuration"`





 
  
 
