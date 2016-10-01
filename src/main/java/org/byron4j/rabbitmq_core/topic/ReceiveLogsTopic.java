package org.byron4j.rabbitmq_core.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月27日
 *  <pre>消费者
 *  (1) - 创建connection、channel
 *  (2) - 声明交换
 *  (3) - 绑定交换<>队列
 *  (4) - 从队列获取消费信息</pre>
 */
public class ReceiveLogsTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

	  public static void main(String[] argv) throws Exception {
		  
		String[] bindKeys = {"oyy.orange.oyy", "oyy.rabbit", "lazy.oyy"};
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
//	    String queueName = channel.queueDeclare().getQueue();
	    String queueName = "topic_logs";

	    if (bindKeys.length < 1) {
	      System.err.println("未指定可用的绑定路由key");
	      System.exit(1);
	    }

	    for (String bindingKey : bindKeys) {
	      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
	    }

	    System.out.println(" 消费者等待接收消息...");

	    Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope,
	                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
	        String message = new String(body, "UTF-8");
	        System.out.println(" 消费者接收到消息： '" + envelope.getRoutingKey() + "':'" + message + "'");
	      }
	    };
	    channel.basicConsume(queueName, true, consumer);
	  }
}
