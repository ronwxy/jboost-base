package cn.jboost.base.starter.rocketmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuxk
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2020/1/13 16:33
 */
@ConfigurationProperties(prefix = "aliyun.rocketmq")
@Getter
@Setter
public class RocketMqProperties {
    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;
    private String groupId;
    private String instanceId;
    private String sendMsgTimeoutMillis;


}
