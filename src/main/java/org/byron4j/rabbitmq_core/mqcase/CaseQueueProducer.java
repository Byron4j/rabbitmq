package org.byron4j.rabbitmq_core.mqcase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.MessageProperties;
/**
 * 
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月14日
 *  生产者
 */
public class CaseQueueProducer extends CaseQueue {
	
	static String headFlag = "生产者--";
	
	/**
	 * empty contructor
	 */
	public CaseQueueProducer() {
	}
	
	
	/**
	 * 生产者构造器
	 * @param queName - 绑定的队列名称
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws URISyntaxException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public CaseQueueProducer(String queName) throws IOException,
			TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		super(queName);
	}

	
	
	/**
	 * 发送消息到队列
	 * @param msg - 需要发送的消息
	 * @throws IOException 
	 */
	public void sendMessage(String msg) throws IOException {
		if( channel == null ){
			System.out.println(headFlag + "未连接到服务器，请检查服务");
		}
		
		/*
		 * 生产数据 - 发布 
		 * 交换的名称，路由key，消息的属性（这里指定消息持久化），消息内容编码
		 * 第一个参数""表示指定使用默认的交换或者无名字的交换
		 */
//		channel.basicPublish("", queName, null, msg.getBytes());
		//此处第三个参数设置消息为持久化(MessageProperties.PERSISTENT_TEXT_PLAIN)
		 channel.basicPublish(exchangeName, RoutingKeyEnum.RED.getName(),
			        MessageProperties.PERSISTENT_TEXT_PLAIN,
			        msg.getBytes("UTF-8"));
		 
		 /**
		  * 设置回调 - 指明需要回调通知的队列名称
		  */
		 BasicProperties props =
				 new BasicProperties().builder().replyTo(this.queName).build();
		 channel.basicPublish(exchangeName, RoutingKeyEnum.RED.getName(),
				 props, msg.getBytes("UTF-8"));
		 
	}
	
}
