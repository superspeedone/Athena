package com.superspeed.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQConsumer {

    private static final String username = "admin";
    private static final String password = "admin";
    //端口可以随意定义
    private static final String url = "tcp://10.7.187.217:61616";

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection;
        //会话 接受或者发送消息的线程
        final Session session;
        //消息的目的地
        Destination destination;
        //消息生产者
        MessageConsumer consumer;

        try {
            //创建连接工厂
            connectionFactory = new ActiveMQConnectionFactory(username, password, url);
            //创建连接
            connection = connectionFactory.createConnection();
            //打开连接
            connection.start();
            //创建回话  开启事物  自动确认
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //设置获取消息目的地
            destination = session.createQueue("test-queue");
            //创建消息消费者
            consumer = session.createConsumer(destination);
            //接收消息
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage message1 = (TextMessage) message;
                    try {
                        System.out.println("接收到消息：" + message1.getText());
                        session.commit();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
