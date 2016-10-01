package org.byron4j.rabbitmq_core.rpc;

import java.util.UUID;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月28日
 *  RPC Client端
 */
public class RPCClient {
	/**
	 * 连接
	 */
	private Connection connection;
	/**
	 * 通道
	 */
	private Channel channel;
	/**
	 * 请求队列（获取消息数据）
	 */
	private String requestQueueName = "rpc_queue";
	/**
	 * 响应队列（应答消息给服务端）
	 */
	private String replyQueueName;
	/**
	 * 队列消费者
	 */
	private QueueingConsumer consumer;
	
	
	public RPCClient() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		
		replyQueueName = channel.queueDeclare().getQueue();
		
		System.out.println("客户端反馈队列名称：" + replyQueueName + "timestamp - " + DateUtil.getNowTime());
		
		consumer = new QueueingConsumer(channel);
		
		
		/**
		 * 从响应队列获取响应消息数据
		 */
		channel.basicConsume(replyQueueName, true, consumer);
	}
	
	
	/**
	 * 客户端通过此方法发送请求消息内容到服务端,并获得服务端的响应数据
	 * @return
	 * @throws Exception
	 */
	public String call(String message) throws Exception{
		
		System.out.println("客户端将要请求到服务端的消息参数：" + message + "timestamp - " + DateUtil.getNowTime());
		String response = null;
		
		//生成唯一的关系id（与请求对应）
		String corrId = UUID.randomUUID().toString();
		System.out.println("客户端生成的关系id：" + corrId + "timestamp - " + DateUtil.getNowTime());
		
		/**
		 * 客户端设置回调信息属性
		 */
		BasicProperties props = new BasicProperties()
										.builder()
										.correlationId(corrId)
										.replyTo(replyQueueName)
										.build();
		/**
		 * 发送请求消息到请求队列中
		 */
		channel.basicPublish("", requestQueueName, props, message.getBytes());
		
		System.out.println("客户端开始获取从服务端得到的响应消息start..." + "timestamp - " + DateUtil.getNowTime());
		while(true){
			Delivery delivery = consumer.nextDelivery();
			
			//比较关系id是否一致，一致则读取消息内容
			if( delivery.getProperties().getCorrelationId().equals(corrId) ){
				//获得服务端的相应消息数据
				response = new String(delivery.getBody());
				System.out.println("客户端已经从服务端获得的响应消息成功，内容为：" + response + "timestamp - " + DateUtil.getNowTime());
	            break;
			}
		}
		
		if(null == response) System.out.println("客户端开始获取从服务端得到的响应消息为空." + "timestamp - " + DateUtil.getNowTime());
		return response;
	}
	
	/**
	 * 关闭连接
	 * @throws Exception
	 */
	public void close() throws Exception{
		connection.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		RPCClient client = new RPCClient();
		String response = client.call("10");
		System.out.println("客户端获得的响应：" + response + "timestamp - " + DateUtil.getNowTime());
	}
	

}
