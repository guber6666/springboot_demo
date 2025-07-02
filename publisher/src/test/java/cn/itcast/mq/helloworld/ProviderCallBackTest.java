package cn.itcast.mq.helloworld;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ProviderCallBackTest {

    @Autowired
    RabbitTemplate rabbitTemplate;


    // 1.消息推送到server，但是在server里找不到交换机
    @Test
    public void testProviderMessageBack1() {
        CorrelationData data = new CorrelationData();
        data.setId("111");
        rabbitTemplate.convertAndSend("non-existent-exchange", "key1", "测试生产者消息回调",data);
    }


    // 2.消息推送到server，但是在server里找不到队列,key不正确
    @Test
    public void testProviderMessageBack2() {
        CorrelationData data = new CorrelationData();
        data.setId("222");
        rabbitTemplate.convertAndSend("hmall.direct", "red11", "测试生产者消息回调2",data);
    }

}
