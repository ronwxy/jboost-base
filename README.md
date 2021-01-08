## 服务端基础组件

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

#### 1.jboost-starter-alimq

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

#### 2.jboost-starter-alioss

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

3. 使用 AliOssManager 来上传文件，或对文件路径进行签名
```java
@Autowired
private AliOssManager aliOssManager;

public void noticeWeb(String macAddress, String data) {
    SocketMqMessage socketMqMessage = new SocketMqMessage(macAddress, data);
    mqProducer.sendMessage(new MqMessage(socketMqTopic, socketMqTag, SocketMQCommandEnum.noticeWebChannel.getCode(), socketMqMessage));
}
```

#### 3.jboost-starter-alisms

阿里短信服务

#### 4.jboost-starter-error

统一异常处理

#### 5.jboost-starter-limiter

限流限速功能

#### 6.jboost-starter-logging

切面日志功能

#### 7.jboost-starter-web

跨域、请求ID、响应封装、Swagger集成等



