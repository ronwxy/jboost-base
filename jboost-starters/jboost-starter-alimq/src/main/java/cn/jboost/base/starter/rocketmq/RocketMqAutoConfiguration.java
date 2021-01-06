package cn.jboost.base.starter.rocketmq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import cn.jboost.base.common.mq.IMqProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * @author yuxk
 * @version V1.0
 * @Title: 阿里rocketmq配置
 * @Description:
 * @date 2019/9/18 15:17
 */
@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqAutoConfiguration {

    private RocketMqProperties rocketmqProperties;

    public RocketMqAutoConfiguration(RocketMqProperties properties) {
        this.rocketmqProperties = properties;
    }

    /**
     * 获取初始化参数
     *
     * @return
     */
    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, rocketmqProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, rocketmqProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, rocketmqProperties.getNameSrvAddr());
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, rocketmqProperties.getSendMsgTimeoutMillis());
        return properties;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(getMqProperties());
        return producer;
    }

    /**
     * 生产环境在阿里云部署，走内网TCP协议
     * @param producerBean
     * @return
     */
    @Profile("prod")
    @Bean
    @ConditionalOnMissingBean(IMqProducer.class)
    public RocketMqInternalProducer rocketMqInternalProducer(ProducerBean producerBean) {
        return new RocketMqInternalProducer(producerBean);
    }

    /**
     * 本地开发测试环境，走外网HTTP协议
     * @param rocketMqProperties
     * @return
     */
    @Profile({"default","local","dev","test"})
    @Bean
    @ConditionalOnMissingBean(IMqProducer.class)
    public RocketMqRemoteProducer rocketMqRemoteProducer(RocketMqProperties rocketMqProperties) {
        return new RocketMqRemoteProducer(rocketMqProperties);
    }

}
