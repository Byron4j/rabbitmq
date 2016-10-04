/**
 * 
 */
package org.byron4j.rabbitmq_core.publish;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author Byron.Y.Y
 * @date 2016年10月4日
 */
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	  public static void main(String[] argv) throws Exception {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    
	    /**
	     * 消费者 ———— 声明交换器、声明队列、绑定交换器与队列
	     */
	    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	    String queueName = channel.queueDeclare().getQueue();
	    channel.queueBind(queueName, EXCHANGE_NAME, "");

	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    Consumer consumer = new DefaultConsumer(channel) {
	    
	    /**
	     * 该方法在消费者获取到消息处理消息时，会执行
	     */
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope,
	                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
	        String message = new String(body, "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	      }
	    };
	    channel.basicConsume(queueName, true, consumer);
	  }
}
