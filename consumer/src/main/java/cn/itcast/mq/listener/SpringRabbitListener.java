package cn.itcast.mq.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

//@Component
public class SpringRabbitListener {
    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "hmall.queue1")
    public void listenFanoutQueueMessage1(String msg) throws InterruptedException {
        System.out.println("spring 消费者1接收到消息：【" + msg + "】");
    }

    @RabbitListener(queues = "hmall.queue2")
    public void listenFanoutQueueMessage2(String msg) throws InterruptedException {
        System.out.println("spring 消费者2接收到消息：【" + msg + "】");
    }


    @RabbitListener(queues = "work.queue1")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue1")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    // direct
    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String msg) {
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "direct.q2")
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }


}