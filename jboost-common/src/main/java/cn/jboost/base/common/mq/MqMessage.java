package cn.jboost.base.common.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息Bean
 *
 * @Author ronwxy
 * @Date 2021/1/5 9:57
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class MqMessage {
    private String topic;
    private String tag;
    private String key;
    private Object body; //需要发送的消息体对象
}
