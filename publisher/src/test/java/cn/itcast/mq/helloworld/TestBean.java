package cn.itcast.mq.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestBean {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 发送到Directout交换机进行转发
     */
    @Test
    public void testDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }



    /**
     * 发送到Topic交换机进行转发
     */
    @Test
    public void testTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "hello world！";
//        rabbitTemplate.convertAndSend(exchangeName, "a.news", message);
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }


    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    // 懒惰队列: 实现发送者的可靠性传输，优化存储到磁盘
//    @Test
//    public void testSendMessageToLazyQueue() throws InterruptedException {
//        // 准备消息
//        Message message = MessageBuilder
//                .withBody("lazy queue".getBytes(StandardCharsets.UTF_8))
//                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
//
//        for (int i = 0; i < 1000000; i++) {
//            // 发送消息
//            rabbitTemplate.convertAndSend("lazy.queue", message);
//        }
//    }

    @Test
    public void testWorkQueue() throws InterruptedException {

        String message = "hello, this is message___";
        rabbitTemplate.convertAndSend("simple.queue",null, message,new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                return message;
            }
        });
    }

    @Test
    public void testWorkQueue2() throws InterruptedException {

        rabbitTemplate.convertAndSend("simple.queue","hello, 123456");
    }

    /**
     *  发送延迟消息
     */
    @Test
    void testPublisherDelayMessage() {
        // 1.创建消息
        String message = "hello, delayed message";
        // 2.发送消息，利用消息后置处理器添加消息头
        rabbitTemplate.convertAndSend("delay.direct", "delay", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 添加延迟消息属性
                message.getMessageProperties().setDelay(2000);// 运行之后：2秒后发送
                return message;
            }
        });
    }

}
