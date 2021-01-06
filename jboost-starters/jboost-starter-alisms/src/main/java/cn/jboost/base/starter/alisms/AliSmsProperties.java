package cn.jboost.base.starter.alisms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author ronwxy
 * @Date 2020/5/22 16:25
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "aliyun.sms")
@Getter
@Setter
public class AliSmsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;
    private String endpoint;
    private String bucket;
}
