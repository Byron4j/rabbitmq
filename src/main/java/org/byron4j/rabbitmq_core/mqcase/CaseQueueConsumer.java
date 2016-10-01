package org.byron4j.rabbitmq_core.mqcase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月14日
 *  <p><b>消费者 - 读取生产者发送到队列中的数据，实现了Runnable、Consumer接口</p>
 */
public class CaseQueueConsumer extends CaseQueue implements Runnable, Consumer{
	
	static String headFlag = "消费者--";
	
	/**
	 * empty contructor
	 */
	public CaseQueueConsumer() {
	}

	
	/**
	 * 消费者构造器
	 * @param queName - 绑定的队列名称
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws URISyntaxException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public CaseQueueConsumer(String queName) throws IOException,
			TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		super(queName);
	}


	
	

	/**
	 * 线程作业 - 获取队列数据信息
	 */
	public void run() {
		if( channel == null ){
			System.out.println(headFlag + "获取通道为空，请检查服务连接");
		}else{
			try {
				/*
				 * 消费 - 从队列获取消息
				 */
				channel.basicConsume(queName, false, this);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(headFlag + "获取数据发生异常");
			}
		}
	}

	
	

	
	/**
     * 当消费者接收到消息时会调用该方法
     * @param consumerTag 消费者的关联标签
     * @param envelope 对消息的打包结果
     * @param properties 消息头内容
     * @param body the message body (opaque, client-specific byte array)
     * @throws IOException if the consumer encounters an I/O error while processing the message
     * @see Envelope
     */
    public void handleDelivery(String consumerTag,
                        Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body)throws IOException{
		System.out.println(headFlag + "接收到的消息数据  - " + new String(body));
		
		//手动设置确认应答
		channel.basicAck(envelope.getDeliveryTag(), false);
	}
	
	
	
	
	public void handleConsumeOk(String consumerTag) {
		System.out.println(headFlag + "handleConsumeOk.");
	}


	public void handleCancelOk(String consumerTag) {
	}


	public void handleCancel(String consumerTag) throws IOException {
	}

	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
	}


	public void handleRecoverOk(String consumerTag) {
	}


}
