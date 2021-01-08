package cn.jboost.base.starter.rocketmq;

import cn.hutool.json.JSONUtil;
import com.aliyun.mq.http.MQClient;
import com.aliyun.mq.http.MQProducer;
import com.aliyun.mq.http.model.TopicMessage;
import cn.jboost.base.common.mq.IMqProducer;
import cn.jboost.base.common.mq.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharEncoding;

/**
 * 外网http环境下使用
 *
 * @Author ronwxy
 * @Date 2021/1/5 10:18
 * @Version 1.0
 */
@Slf4j
public class RocketMqRemoteProducer implements IMqProducer {

    private RocketMqProperties rocketMqProperties;

    public RocketMqRemoteProducer(RocketMqProperties rocketMqProperties) {
        this.rocketMqProperties = rocketMqProperties;
    }

    @Override
    public void sendMessage(MqMessage message) {
        MQClient mqClient = new MQClient(rocketMqProperties.getNameSrvAddr(), rocketMqProperties.getAccessKey(), rocketMqProperties.getSecretKey());
        // 所属的 Topic
        final String topic = message.getTopic();
        // Topic所属实例ID，默认实例为空
        final String instanceId = rocketMqProperties.getInstanceId();
        // 获取Topic的生产者
        MQProducer producer = mqClient.getProducer(instanceId, topic);
        String body = JSONUtil.toJsonStr(message.getBody());
        try {
            TopicMessage msg = new TopicMessage(
                    // 消息内容
                    body.getBytes(CharEncoding.UTF_8),
                    // 消息标签
                    message.getTag()
            );
            // 同步发送消息，只要不抛异常就是成功
            producer.publishMessage(msg);
            log.info("发送MQ消息成功: [{}]", body);
        } catch (Throwable e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            log.error("发送MQ消息失败，", e);
        } finally {
            mqClient.close();
        }
    }
}
