package cn.itcast.mq.helloworld;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "dp.queue1";
        // 消息
        String message = "hello, dp.queue1来消息啦";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }


    /**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     */
    @Test
    public void testWorkQueue() throws InterruptedException {
        // 0 队列名称
        String queueName = "work.queue1";
        // 1 消息
        String message = "hello, this is message___";
        for (int i = 0; i < 50; i++) {
            // 发送消息，每20毫秒发送一次，相当于每秒发送50条消息
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }


    /**
     * 发送到Fanout交换机进行转发
     */
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "dp.fanout";
        // 消息
        String message = "hello, everyone__fanout__!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }


    /**
     * 发送到Directout交换机进行转发
     */
    @Test
    public void testDirectExchange() {
        // 交换机名称
        String exchangeName = "dp.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        rabbitTemplate.convertAndSend(exchangeName, "key.2", message);
    }

    /**
     * topicExchange
     *  交换机绑定queue key: #.dp
     *  交换机绑定queue key: dp.*
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "dp.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        // 发送消息
//        rabbitTemplate.convertAndSend(exchangeName, "dp.1", message);
        rabbitTemplate.convertAndSend(exchangeName, "1.key", message);
    }
}