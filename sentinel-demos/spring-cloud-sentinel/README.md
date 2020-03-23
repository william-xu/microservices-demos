# spring-cloud-sentinel

Spring Cloud使用sentinel进行限流，配置保存在nacos上，同时也有nacos多配置使用示例。



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

**限流处理逻辑：** 默认情况下，当请求被限流时会返回默认的提示页面 Blocked by Sentinel (flow limiting)。您也可以通过 JVM 参数 -Dcsp.sentinel.web.servlet.block.page 或代码中调用 WebServletConfig.setBlockPage(blockPage) 方法设定自定义的跳转 URL，当请求被限流时会自动跳转至设定好的 URL。同样您也可以实现 UrlBlockHandler 接口并编写定制化的限流处理逻辑，然后将其注册至 WebCallbackManager 中。


一条限流规则主要由下面几个因素组成，我们可以组合这些元素来实现不同的限流效果：

resource：资源名，即限流规则的作用对象
count: 限流阈值
grade: 限流阈值类型（QPS 或并发线程数）
limitApp: 流控针对的调用来源，若为 default 则不区分调用来源
strategy: 调用关系限流策略
controlBehavior: 流量控制效果（直接拒绝、Warm Up、匀速排队）


**@SentinelResource 注解**


@SentinelResource 用于定义资源，并提供可选的异常处理和 fallback 配置项。 @SentinelResource 注解包含以下属性：

* value：资源名称，必需项（不能为空）
* entryType：entry 类型，可选项（默认为 EntryType.OUT）
* blockHandler / blockHandlerClass: blockHandler 对应处理 BlockException 的函数名称，可选项。blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException。blockHandler 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
* fallback：fallback 函数名称，可选项，用于在抛出异常的时候提供 fallback 处理逻辑。fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。fallback 函数签名和位置要求：
    * 返回值类型必须与原函数返回值类型一致；
    * 方法参数列表需要和原函数一致，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
    * fallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
* defaultFallback（since 1.6.0）：默认的 fallback 函数名称，可选项，通常用于通用的 fallback 逻辑（即可以用于很多服务或方法）。默认 fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。若同时配置了 fallback 和 defaultFallback，则只有 fallback 会生效。defaultFallback 函数签名要求：
    * 返回值类型必须与原函数返回值类型一致；
    * 方法参数列表需要为空，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
    * defaultFallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
* exceptionsToIgnore（since 1.6.0）：用于指定哪些异常被排除掉，不会计入异常统计中，也不会进入 fallback 逻辑中，而是会原样抛出。

注：1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理。

特别地，若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑。若未配置 blockHandler、fallback 和 defaultFallback，则被限流降级时会将 BlockException 直接抛出（若方法本身未定义 throws BlockException 则会被 JVM 包装一层 UndeclaredThrowableException）。

示例：

```
public class TestService {

    // 对应的 `handleException` 函数需要位于 `ExceptionUtil` 类中，并且必须为 static 函数.
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void test() {
        System.out.println("Test");
    }

    // 原函数
    @SentinelResource(value = "hello", blockHandler = "exceptionHandler", fallback = "helloFallback")
    public String hello(long s) {
        return String.format("Hello at %d", s);
    }
    
    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String helloFallback(long s) {
        return String.format("Halooooo %d", s);
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(long s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }
}

```





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
    "resource": "test2",
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





 
  
 **应用中的示例**

**拒绝访问**
http://localhost:12403/cloud-sentinel/test1   
**设置QPS访问限制**
http://localhost:12403/cloud-sentinel/hello

**自定义异常处理方法**
http://localhost:12403/cloud-sentinel/test2

 

 