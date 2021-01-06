package cn.jboost.base.common.mq;

/**
 * 消息队列生产者接口
 *
 * @Author ronwxy
 * @Date 2021/1/5 9:57
 * @Version 1.0
 */
public interface IMqProducer {
    void sendMessage(MqMessage message);
}
