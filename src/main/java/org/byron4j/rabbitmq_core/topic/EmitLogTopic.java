package org.byron4j.rabbitmq_core.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月27日
 *  <pre>生产者
 *  (1) - 创建connection、channel
 *  (2) - 声明交换
 *  (3) - 声明队列
 *  (4) - 绑定交换<>队列
 *  (5) - 生产消息到交换
 */
public class EmitLogTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv)
                  throws Exception {
    	String[] bindKeys = {"*.orange.*", "*.rabbit", "lazy.#"};
    	
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = "topic_logs";
        channel.queueDeclare(queueName, false, false, false, null);
        for (String bindingKey : bindKeys) {
  	      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
  	    }
        String routingKey = "routingKey";
        String message = "message";

        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println(" 生产者发送消息： '" + routingKey + "':'" + message + "'");

        //connection.close();
    }

}
