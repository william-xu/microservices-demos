# Spring Boot 使用 Memcached缓存框架

使用Spring Boot应用程序连接memcached和进行简单操作的示例。

需先安装memcached并启动服务

打开项目地址http://192.168.43.104:9105/boot-memcached/ 会自动添加数据到缓存，过期后可以刷新重复添加

add方法添加的缓存好像不会失效

memcached工作在大部分Linux和BSD类型操作系统上，有windows版本，但官方不提供支持。

命令行连接memcached，stats命令可以查看服务器信息，stats items可以查看缓存信息， stats cachedump <上面查看到的数据集编号> <idx>查看有哪些键，例如  stats cachedump 1 0,  查看数据集1，从索引0开始的所有键，值数据是以对象的形式保存的样子
 
```
telnet ip port
stats
stats items
stats cachedump 1 0
ITEM key2 [18 b; 1584252887 s]
ITEM key1 [18 b; 1584252899 s]

get key1
get key2

quit
```

其他相关命令可以参考文档： https://github.com/memcached/memcached/wiki/Commands


官网：https://memcached.org/
GitHub: https://github.com/memcached/memcached

