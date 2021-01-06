### 服务端基础组件

1. jboost-common： 基础类库，包括工具类，常量类等
2. jboost-dependencies： 依赖版本管理，管理常用依赖库的版本，项目 pom.xml 中引用时可省略版本号
3. jboost-generator： 代码生成工具，配置详见 `resource/generator.yaml` 文件示例
4. jboost-parent：父项目，集成了数据库、redis、统一异常处理、统一响应封装、切面日志等功能
5. jboost-starters
- 5.1. jboost-starter-alimq： 阿里 RocketMq 消息队列
- 5.2. jboost-starter-alioss： 阿里对象服务，上传图片、视频等
- 5.3. jboost-starter-alisms： 阿里短信服务
- 5.4. jboost-starter-error： 统一异常处理
- 5.5. jboost-starter-limiter： 限流限速功能
- 5.6. jboost-starter-logging： 切面日志功能
- 5.7. jboost-starter-web： 跨域、请求ID、响应封装、Swagger集成等



