package com.lenged.system.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @title: SyncProducer
 * @description: Producer端发送同步消息
 * 这种可靠性同步地发送方式使用的比较广泛，比如：重要的消息通知，短信通知
 * @auther: zhangjianyun
 * @date: 2022/8/8 16:03
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("lenged_group");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.20.211:9876");
        // 启动Producer实例
        producer.start();
//        for (int i = 0; i < 100; i++) {
//            // 创建消息，并指定Topic，Tag和消息体
//            Message msg = new Message("TOPIC_LENGED" /* Topic */,
//                    "SyncProducer" /* Tag */,
//                    ("SyncProducer Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
//            );
//            // 发送消息到一个Broker
//            SendResult sendResult = producer.send(msg);
//            // 通过sendResult返回消息是否成功送达
//            System.out.printf("%s%n", sendResult);
//        }
        Message msg = new Message("TOPIC_LENGED" /* Topic */,
                "SyncProducer" /* Tag */,
                ("SyncProducer Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
        );
        // 发送消息到一个Broker
        SendResult sendResult = producer.send(msg);
        // 通过sendResult返回消息是否成功送达
        System.out.printf("%s%n", sendResult);

        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
