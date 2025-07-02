package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 生产者消息回调配置类
 */
@Configuration
@Slf4j
public class ProviderCallBackConfig {

    @Resource
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMandatory(true);

        /**
         * TODO RabbitMQ生产者发送消息确认回调，解决消息可靠性问题
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                //消息发送成功后，更新数据库消息状态等逻辑
                log.info("------生产者发送消息至exchange成功,消息唯一标识: {}, 确认状态: {}, 造成原因: {}-----",correlationData, ack, cause);
            } else {
                //信息发送失败，打印日志后，可以根据业务选择是否重发消息
                log.info("------生产者发送消息至exchange失败,消息唯一标识: {}, 确认状态: {}, 造成原因: {}-----", correlationData, ack, cause);
            }
        });

        /**
         * TODO RabbitMQ生产者发送消息失败回调，解决消息可靠性问题
         * message      消息
         * replyCode    回应码
         * replyText    回应信息
         * exchange     交换机
         * routingKey   路由键
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("------生产者发送消息至exchange失败,回应码: {}, 回应信息: {}, 交换机: {}, 路由键: {}-----", replyCode, replyText, exchange, routingKey);
            }
        });
        return rabbitTemplate;
    }

}