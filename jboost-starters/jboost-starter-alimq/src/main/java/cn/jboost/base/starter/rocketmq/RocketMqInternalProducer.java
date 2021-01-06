package cn.jboost.base.starter.rocketmq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import cn.jboost.base.common.mq.IMqProducer;
import cn.jboost.base.common.mq.MqMessage;
import cn.jboost.base.common.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharEncoding;

import java.io.UnsupportedEncodingException;

/**
 * 内网TCP环境下使用
 *
 * @Author ronwxy
 * @Date 2021/1/5 10:15
 * @Version 1.0
 */
@Slf4j
public class RocketMqInternalProducer implements IMqProducer {

    private ProducerBean producerBean;

    public RocketMqInternalProducer(ProducerBean producerBean) {
        this.producerBean = producerBean;
    }

    @Override
    public void sendMessage(MqMessage message) {
        try {
            String body = GsonUtil.toJson(message.getBody());
            Message msg = new Message(message.getTopic(), message.getTag(), message.getKey(), body.getBytes(CharEncoding.UTF_8));
            producerBean.start();
            producerBean.sendAsync(msg, new SendCallback() {
                @Override
                public void onSuccess(final SendResult sendResult) {
                    if (sendResult == null) {
                        log.warn("发送MQ消息失败: [{}]", body);
                    } else {
                        log.info("发送MQ消息成功：[{}]", body);
                    }
                }

                @Override
                public void onException(final OnExceptionContext context) {
                    log.warn("发送MQ消息失败 -- Exception:{}", context.getException());
                }
            });
        } catch (UnsupportedEncodingException e) {
            log.warn("MQ消息处理异常", e);
        } catch (ONSClientException e) {
            log.warn("发送MQ消息失败", e);
        } finally {
            producerBean.shutdown();
        }
    }
}
