package cn.jboost.base.starter.alisms;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ronwxy
 * @Date 2020/5/22 16:26
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(AliSmsProperties.class)
@ConditionalOnClass({IAcsClient.class, SendSmsRequest.class})
public class AliSmsAutoConfiguration {

    @Bean
    public AliSmsProvider aliSmsManager(AliSmsProperties properties) {
        return new AliSmsProvider(properties);
    }
}
