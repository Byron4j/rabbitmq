/**
 * 
 */
package org.byron4j.rabbitmq_core.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Byron.Y.Y
 * @date 2016年10月2日
 */
public class NewTask {
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
		//String message = "Hello World!";
		
		String message = getMessage(argv);

		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
	
	private static String getMessage(String[] strings){
	    if (strings.length < 1)
	        return "Hello World!";
	    return joinStrings(strings, " ");
	}
	
	private static String joinStrings(String[] strings, String delimiter) {
	    int length = strings.length;
	    if (length == 0) return "";
	    StringBuilder words = new StringBuilder(strings[0]);
	    for (int i = 1; i < length; i++) {
	        words.append(delimiter).append(strings[i]);
	    }
	    return words.toString();
	}
}
