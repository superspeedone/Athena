package com.superspeed.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQProducer {

    private static final String username = "admin";
    private static final String password = "admin";
    //端口可以随意定义
    private static final String url = "tcp://10.7.187.217:61616";

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory = null;
        //连接
        Connection connection = null;
        //会话 接受或者发送消息的线程
        Session session = null;
        //消息的目的地
        Destination destination = null;
        //消息生产者
        MessageProducer producer = null;

        try {
            //创建连接工厂
            connectionFactory = new ActiveMQConnectionFactory(username, password, url);
            //创建连接
            connection = connectionFactory.createConnection();
            //打开连接
            connection.start();
            //创建回话  开启事物  自动确认
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建消息队列
            destination = session.createQueue("test-queue");
            //创建消息生产者
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //发送消息
            producer.send(session.createTextMessage("hello"));
            //提交
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }


}
