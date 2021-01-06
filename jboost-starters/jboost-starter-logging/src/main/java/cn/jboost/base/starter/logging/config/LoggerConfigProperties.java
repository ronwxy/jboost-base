package cn.jboost.base.starter.logging.config;

import cn.jboost.base.starter.logging.provider.Slf4jLogProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author ronwxy
 * @Date 2020/5/30 18:02
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "aoplog")
@Getter
@Setter
public class LoggerConfigProperties {
    /**
     * 日志记录实现类，可以自定义
     */
    private Class serviceImplClass = Slf4jLogProvider.class;
    /**
     * 集合（数组）类型解析元素个数
     */
    private Integer collectionDepthThreshold = 10;
}
