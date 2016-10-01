package org.byron4j.rabbitmq_core.mqcase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月14日
 *  MQ队列实例
 */
public class CaseQueue {
	/**
	 * 队列名称
	 */
	protected String queName = "My-Durable-CaseQueue";
	/**
	 * 主机
	 */
	protected String host = "localhost";
	protected String username = "guest";
	protected String password = "guest";
	
	/**
	 * 交换名称
	 */
	protected String exchangeName = "logs";
	
	
	
	protected Connection connection;
	protected Channel channel;
	
	
	/**
	 * empty contructor
	 */
	public CaseQueue() {
		super();
	}
	
	/**
	 * contructor whith queName
	 * @param queName
	 * @throws TimeoutException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public CaseQueue(String queName) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		super();
		this.queName = queName;
		
		//1. 方式一： 创建工厂 - 设置连接参数, 此 demo 暂时以方式二展示
		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost(host);
//		factory.setUsername(username);
//		factory.setPassword(password);
		
		// 方式二：创建工厂也可以使用setUri--factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost"); 
		factory.setUri("amqp://"+username +":" + password + "@" + 
		"localhost:5672"); 
		
		//2. 创建连接
		connection = factory.newConnection();
		
		//3. 获取通道
		channel = connection.createChannel();
		
		
		//3-1.交换类型: 【direct】, 【topic】, 【headers】 and 【fanout】.
		//这里我们声明一个交换，名为logs,类型为广播形式fanout
		channel.exchangeDeclare(exchangeName, ExchangeTypeEnum.FANOUT.getName());
		
		/*
		 * 3-2.为了让每个consumer的负载均匀，通过basicQos方法设置参数prefetchCount为1。
		 * 这样，consumer中的消息将不超过一条，当consumer处理完并发送反馈后RabbitMQ才会向它分发新的消息。
		 * RabbitMQ在接收到新消息后，如果有空闲的consumer就将消息发给它，否则就一直等待有空闲的consumer再分发
		 */
		//该参数默认为0无限制，为1则只保证consumer有一个在处理，处理完了才会分发任务
		//该方法保证“公平”分配任务
		channel.basicQos(1);
		
		//4. 声明一个消息队列（该方法是幂等的，若已存在则返回已存在的队列实例）
		//--第二个参数表示改队列会持久化, 地上那个参数表示是一个独占队列,第四个参数表示不再使用之后会自动删除该队列
		
		channel.queueDeclare(queName, true, false, true, null);
		
		//创建的随机临时队列名称形式如：amq.gen-o3yZdr1_-PJZN0_9XPLfUg , amq.gen-kJIq_-HDDfS6vzxt3VQzbw
		//String randomName = channel.queueDeclare().getQueue();
		//System.out.println("随机队列名称：" + randomName);
		
		
		
		//交换与队列的关系--绑定
		//告诉交换exchangeName，应该将消息往queName的队列里传输
		//如果交换是fanout广播式的，会忽略第三个参数
		//第三个参数(绑定key)的值依赖于交换的类型
		channel.queueBind(queName, exchangeName, "");
		
		/**
		 * 一个交换可以绑定多个路由,如：
		 * String queueName = channel.queueDeclare().getQueue();
		   String[] argv = {"info", "warning", "error"};
		   for(String severity : argv){    
  			 channel.queueBind(queueName, EXCHANGE_NAME, severity);
		   }
		 */
	}
	
	
	/**
	 * 关闭连接资源
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void close(String instanceName) throws IOException, TimeoutException{
		if( null != channel ){
			channel.close();
			System.out.println("关闭" + instanceName + "通道成功");
		}
		
		if( null != connection ){
			connection.close();
			System.out.println("关闭" + instanceName + "连接成功");
		}
	}
	
	
	
}
