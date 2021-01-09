## 服务端基础组件

提供
1. 基础工具类，常量类
2. 依赖管理
3. 代码生成
4. 常用功能 starter

### 下载安装

```shell
git clone https://github.com/ronwxy/jboost-base.git
cd jboost-base
#安装到本地
mvn clean install 
#部署到maven仓库
#mvn clean deploy 
```

### jboost-common

基础类库，包括工具类，常量类等

### Bean 转换
BaseConverter： Bean 转换，主要用于 entity 与 dto 之间的转换。使用 MapStruct 框架。
针对需要转换的 Bean，创建相应接口，示例
```java
@Mapper(componentModel = BaseConverter.SPRING,uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter extends BaseConverter<User, UserDTO> {
}
```

### 异常定义

1. BizException： 业务异常基类
2. ClientSideException： 客户端异常，如参数错误
3. UnauthorizedException： 鉴权失败异常，如token过期
4. ForbiddenException： 访问受限异常，如访问资源未授权
5. ServerSideException： 服务端异常，服务端内部操作异常，如数据库访问出错
6. ExceptionUtil： 异常工具类

根据条件抛出异常，交由统一异常处理机制处理（记录日志，返回客户端错误提示）
```java
    User existUser = findUserByName(name);
    if (existUser != null) {
        ExceptionUtil.rethrowClientSideException("用户名已存在");
    }
```

捕获异常后重新抛出异常，交由统一异常处理机制处理（记录日志，返回客户端错误提示）
```java
    try {
        aliSmsProvider.sendVerifyCode(phone, verifyCode.getCode());
    } catch (ClientException ex) {
        ExceptionUtil.rethrowServerSideException("发送验证码失败，请稍后重试", ex);
    }
```

### 消息队列抽象

使用消息队列的地方可以基于该接口与消息对象调用，屏蔽具体消息中间件信息

* MqMessage：消息对象，自身包含 topic, tag, key 等信息
* IMqProducer：消息生产者接口，使用具体消息中间件时，生产者服务实现该接口

### 其它工具与常量类

安全相关：
1. EncryptUtil：RSA非对称加密（公钥/私钥）工具类
2. JwtUser：用于 Spring Security 认证的 user bean
3. JwtUtil：Jwt 工具类
4. SecurityUtil：Security 工具类，可获取当前登录用户的相关信息

其它：
1. EasyPoiUtil：文件上传工具类
2. FileUtil：文件操作工具类
3. LocalDateTimeUtil：日期工具类
4. MyBatisUtil：可用于 mybatis xml 文件条件判断的工具类
5. ResponseWrapper：客户端响应封装
6. VerifyCodeUtil：图形验证码工具类
7. WebUtil：Web工具类，设置req-id；获取客户端IP；输出响应

### 依赖管理

jboost-dependencies：依赖版本管理，管理常用依赖库的版本，其它继承或以 `dependencyManagement` 引入 jboost-dependencies 的项目引用依赖时可省略版本号

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.jboost.boot</groupId>
            <artifactId>jboost-dependencies</artifactId>
            <version>${project.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
    </dependency>
</dependencies>
```
### 代码生成

jboost-generator： 代码生成工具，采用 mybatis-plus-generator 实现，配置详见 `resource/generator.yaml` 文件示例

针对每个数据库表，可生成如下代码结构

```yaml
--controller
    UserController.java
--mapper
    UserMapper.java
--pojo
----dto
------converter
        UserConverter.java
      UserDTO.java
----entity
      UserEntity.java
----query
      UserQuery.java
--service
    UserService.java
```

### 父项目

jboost-parent：父项目，集成了数据库、redis、统一异常处理、统一响应封装、切面日志等功能

### starter

jboost-starters

#### jboost-starter-alimq

阿里 RocketMq 消息队列使用示例

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-alimq</artifactId>
</dependency>
```
2. application.yaml 文件中添加配置：
```yaml
aliyun:
  rocketmq:
    accessKey: xxxx
    secretKey: xxxx
    namesrvAddr: http://xxx.mqrest.cn-hangzhou.aliyuncs.com
    sendMsgTimeoutMillis: 3000
    #topic 所属实例ID
    instanceId: xxxx
    groupId: xxxx
```

3. 使用生产者发送消息（RocketMqAutoConfiguration 已配置了本地开发测试环境走HTTP协议 RocketMqRemoteProducer，云端生产环境走TCP协议 RocketMqInternalProducer）
```java
@Value("${socket.mq.topic}")
private String socketMqTopic;

@Value("${socket.mq.tag}")
private String socketMqTag;

@Autowired
private IMqProducer mqProducer;

public void noticeWeb(String macAddress, String data) {
    SocketMqMessage socketMqMessage = new SocketMqMessage(macAddress, data);
    mqProducer.sendMessage(new MqMessage(socketMqTopic, socketMqTag, SocketMQCommandEnum.noticeWebChannel.getCode(), socketMqMessage));
}
```

4. 消息消费者

#### jboost-starter-alioss

阿里云对象服务，上传图片、视频等

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-alioss</artifactId>
</dependency>
```
2. application.yaml 文件中添加配置：
```yaml
aliyun:
  oss:
    accessKeyId: xxxx
    accessKeySecret: xxxx
    bucket: xxxx
    domain: http://xxx.oss-cn-hangzhou.aliyuncs.com/
    endpoint: https://oss-cn-hangzhou.aliyuncs.com
```

3. 使用 AliOssProvider 来上传文件，或对文件路径进行签名（针对需要授权访问的资源）
```java
@Autowired
private AliOssProvider aliOssProvider;
//保存文件
String fileSave(String bizType, File file, String fileName);
//url签名
void signUrl(List<T> list, List<String> urlFields, String bucketName, long expire)
```
更多方法参考 `cn.jboost.base.starter.alioss.AliOssProvider`

#### jboost-starter-alisms

阿里短信服务

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-alisms</artifactId>
</dependency>
```
2. application.yaml 中添加配置

```yaml
aliyun:
  #阿里短信
  sms:
    accessKeyId: xxxx
    accessKeySecret: xxxx
    signName: 签名
    templateCode: xxxx
    regionId: xxxx
```

3. 使用

```java
@Autowired
private AliSmsProvider aliSmsProvider;
//发送验证码
aliSmsProvider.sendVerifyCode(phone, verifyCode.getCode());
//发送短信,支持以逗号分隔的形式进行批量调用，批量上限为20个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
aliSmsProvider.sendSms(String phoneNumber, String signName, String smsTempCode, String tempParam)
```

#### jboost-starter-error

统一异常处理

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-error</artifactId>
</dependency>
```
2. 使用 @RestControllerAdvice + @ExceptionHandler 统一处理抛出的异常

* BizException：以 400 返回异常消息
* AccessDeniedException：以 403 返回异常消息
* IllegalArgumentException，IllegalStateException：以 400 返回异常消息
* Exception： 以 500 返回异常消息

3. 打印异常栈

针对 profile "default","local","dev"， 打印异常栈；
针对 profile "test", "formal", "prod"，不打印异常栈。

> `org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController` 现在通过 request 传 trace=true 参数的方式来控制是否打印异常栈，后续可做相应调整

#### jboost-starter-limiter

提供基于 Redis 的分布式锁，基于 Guava RateLimiter（令牌桶算法） 的限速，及基于 Redis Lua 的限量实现

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-limiter</artifactId>
</dependency>
```

2. 分布式锁
在方法上添加注解 @DistributedLockable，对方法进行分布式环境下的同步，
   
```java
@DistributedLockable(key="", prefix="disLock:", expire=5)
public void syncDistributed() {
    //...    
}
```

* key：redis 使用 prefix+key 来作为缓存key
* prefix：redis key 前缀，默认为 `disLock:`
* expire：过期时间，默认为10s

3. 限流

限速：使用 @RateLimit 注解严格控制访问速率，在一次访问后，必须经过设定的时间间隔才能进行下一次访问
```java
@GetMapping("/rate")
@RateLimit(rate = 1.0/5, burst = 5.0, expire = 120, timeout = 1)
public String rateLimit(@RequestParam("key") String key){
    return "test rate limiter...";
}
```
上例表示以限制访问速度为5秒1次，一次访问后，直到5s之后才能再次访问。

支持的属性配置：

* key： redis 使用 prefix+key 来作为缓存 key
* prefix：redis key 前缀， 默认为 "rateLimit:";
* expire：表示令牌桶模型 RedisPermits redis key 的过期时间/秒，默认为 60; 
* rate：permitsPerSecond 值，表示以每秒rate的速率放置令牌，默认为 1.0; 
* burst：maxBurstSeconds 值，表示最多保留burst秒的令牌，默认为 1.0;
* timeout：取令牌的超时时间，秒，默认为 0，表示不等待立即返回;
* limitType： 默认 LimitType.METHOD;
   
LimitType 主要用于控制 key 的值，支持类型如下，

* IP：根据客户端IP限流
* USER：根据用户限流，用户已经登录，通过`SecurityUtil.getUserId()`获取当前用户ID
* METHOD：根据方法名全局限流
* CUSTOM：自定义，需要设置 key 的值，自定义 key 支持表达式，如 `#{id}`, `#{user.id}`

限量：使用 @CountLimit 注解来控制在一个时间窗口内，允许访问的次数，在允许次数内对访问速率不限制
```java
@GetMapping("/count")
@CountLimit(key = "#{key}", limit = 2, period = 10, limitType = LimitType.CUSTOM)
public String countLimit(@RequestParam("key") String key){
    return "test count limiter...";
}
```
上例表示在10s内限制访问2次，至于这两次以什么样的时间间隔访问不做限制。

支持的属性配置：

* key：redis 使用 prefix+key 来作为缓存 key
* prefix：key 前缀，默认 "countLimit:"; 
* limit：period 时间段内限制访问的次数，默认1;  
* period：表示时间段/秒，默认1; 
* limitType： 默认 LimitType.METHOD;

非注解形式限流：也可以使用如下形式对某段代码进行限流控制

```java
@GetMapping("/rate2")
public String testRateLimit(){
    RedisRateLimiter limiter = redisRateLimiterFactory.build("LimiterController.testRateLimit", 1.0/30, 30, 120);
    if(!limiter.tryAcquire("app.limiter", 0, TimeUnit.SECONDS)) {
        System.out.println(LocalDateTime.now());
        ExceptionUtil.rethrowClientSideException("您的访问过于频繁，请稍后重试");
    }
    return "test rate limiter 2...";
}
```

更多详细内容参考 [一个轻量级的基于RateLimiter的分布式限流实现](http://blog.jboost.cn/distributedratelimiter.html) 

#### jboost-starter-logging

切面日志功能

对用注解 `cn.jboost.base.starter.logging.annotation.Log` 修饰的类方法进行日志记录

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-logging</artifactId>
</dependency>
```
2. 在方法或类上（对类内所有方法有效，一般用于 Controller 类上）添加注解 @Log，
```java
import cn.jboost.base.starter.logging.annotation.Log;

@Log
public class UserController {
    //...
}
```
3. 日志打印
添加注解 @Log 后，当方法被调用时，默认将打印调用与返回日志，如
```shell
jboost-boot - [2021-01-09 09:41:21] [http-nio-8000-exec-1] INFO  [5ff909c1a94b6d1f4c62f021 - ] c.j.b.auth.controller.AuthController - call: getCaptcha()
jboost-boot - [2021-01-09 09:41:21] [http-nio-8000-exec-1] INFO  [5ff909c1a94b6d1f4c62f021 - 216] c.j.b.auth.controller.AuthController - return: getCaptcha():VerifyCodeUtil.VerifyCode(code=xqho, uuid=9d8ffc7e9e324ed2a5a9566d9a7f115d)
```

4. 其它配置
@Log 注解提供了两个属性配置
   
* logPoint：可配置在什么位置打印日志，有 LogPoint.IN（调用时打印）, LogPoint.OUT（返回时打印）, LogPoint.BOTH（调用与返回时都打印）
* logException：是否对异常进行日志记录，默认为true

可对日志输出的实现进行定制化，默认使用 `cn.jboost.base.starter.logging.provider.Slf4jLogProvider`，采用项目中slf4j的框架进行输出，如logback,log4j2等。
如果要自定义日志输出，则可提供一个 `cn.jboost.base.starter.logging.provider.ILogProvider` 接口的实现类，然后配置

```yaml
aoplog:
  service-impl-class: aop日志记录实现类，默认为 cn.jboost.base.starter.logging.service.Slf4jLogService
  collection-depth-threshold: 5   #集合类参数输出元素个数，默认为10
```

#### jboost-starter-web

日期格式化、跨域、添加请求ID、请求响应封装、Swagger集成等

1. 项目 pom.xml 中引入依赖：
```xml
<dependency>
    <groupId>cn.jboost.boot</groupId>
    <artifactId>jboost-starter-web</artifactId>
</dependency>
```

2. 日期格式化（序列化与反序列化时）
   
* LocalDateTime： `yyyy-MM-dd HH:mm:ss`
* LocalDate： `yyyy-MM-dd`
* LocalTime： `HH:mm:ss`

3. 跨域

默认开启，如要关闭，配置
```yaml
web:
  cors: disable  # 关闭跨域
```

4. 添加请求ID

默认开启，如要关闭，配置

```yaml
web:
  reqId: disable # 关闭日志中加入请求id
```
开启后，会在每一个请求的 Header 中添加 key 为 `Req-Id`， value 为 uuid 的 header，下游可通过 `WebUtil.getRequestId()` 获取（例如将其存入MDC中，在日志打印时输出请求ID，将日志进行串联）

5. 请求响应封装

默认开启，如要关闭，配置（建议默认开启，如果关闭，还需要在异常处理中将响应格式进行处理）

```yaml
web:
  responseWrapper: disable # 关闭响应消息封装
```
开启后，对请求响应按照 `cn.jboost.base.common.util.ResponseWrapper` 的结构进行封装

6. swagger集成

默认关闭，如要开启，配置

```yaml
swagger:
  #是否开启 swagger-ui
  enabled: true
  #定义扫描包,可配置多条扫描包,包之间用逗号隔开
  basePackages: cn.jboost
  #其它配置，可省略，使用默认配置
  title: "服务端接口文档"
  version: 1.0
  tokenHeader: Authorization
```
建议只在开发环境（application-dev.yaml）中开启

7. 过滤器中异常处理

主要是对响应结果进行封装统一： `cn.jboost.base.starter.web.ExceptionHandlerFilter`
