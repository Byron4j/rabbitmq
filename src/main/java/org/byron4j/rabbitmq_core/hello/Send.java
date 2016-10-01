package org.byron4j.rabbitmq_core.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Byron.Y.Y
 * @optDate 2016年10月1日
 */
public class Send {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		// 创建连接工厂类
		ConnectionFactory factory = new ConnectionFactory();
		// 设置需要连接到的RabbitMQ主机地址
		factory.setHost("localhost");
		// 创建一个连接
		Connection connection = factory.newConnection();
		// 创建一个信道
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}
