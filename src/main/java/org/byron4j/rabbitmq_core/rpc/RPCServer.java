package org.byron4j.rabbitmq_core.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月28日
 *  RPC Server端 -- rabbitmq队列名称长度不大于255
 */
public class RPCServer {

	/**
	 * 定义队列名称
	 */
	public static String RPC_QUEUE_NAME = "rpc_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		
		//声明队列
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		
		//设置公平分配任务
		channel.basicQos(1);
		
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		
		/**
		 * 服务端从请求队列中获得客户端请求的消息内容
		 */
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
		
		System.out.println("服务端等待RPC请求中..." + "timestamp - " + DateUtil.getNowTime());
		
		while( true ){
			//等待下一个消息传递并返回
			Delivery delivery = consumer.nextDelivery();
			//消息的属性
			BasicProperties props = delivery.getProperties();
			System.out.println("服务端接收的请求的关系id：" + props.getCorrelationId() + "timestamp - " + DateUtil.getNowTime());
			
			//响应消息的属性
			BasicProperties replyProps = new BasicProperties()
											.builder()
											.correlationId(props.getCorrelationId())
											.build();
			//获得消息内容
			String message = new String(delivery.getBody());
			System.out.println("服务端接收到的消息内容:" + message + "timestamp - " + DateUtil.getNowTime());
			
			int n = Integer.valueOf(message);
			
			String response = "" + Fibonacci.generate(n);
			
			System.out.println("服务端生成的斐波那契数值：" + response + "timestamp - " + DateUtil.getNowTime());
			
			/**
			 * 发布消息-服务端发送响应结果到客户端
			 */
			channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes());
			
			System.out.println("服务端发送斐波那契数值到客户端成功." + "timestamp - " + DateUtil.getNowTime());
			
			//设置消息应答
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
		}
		
		
	}
}
