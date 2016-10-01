package org.byron4j.rabbitmq_core.mqcase;

import org.byron4j.rabbitmq_core.common.GenerateNoUtil;

/**
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月14日
 *  测试类
 */
public class CaseQueueTestCase {
	static String headFlag = "测试--";
	static String queName = "My-Durable-CaseQueue";
	
	/**
	 * empty constructor
	 */
	public CaseQueueTestCase() {
	}

	
	
	public static void main(String[] args) throws Exception {
		
		//1. 创建消费者
		CaseQueueConsumer consumer = new CaseQueueConsumer(queName);
		
		//2. 创建生产者
		CaseQueueProducer producer = new CaseQueueProducer(queName);
		
		Thread myThread = new Thread(consumer);
		
		myThread.start();
		
		for(int i=1; i <=10; i++){
			String msg = GenerateNoUtil.generateFlowNo(queName);
			producer.sendMessage(msg);
		}
		
		/**
		 * 关闭资源(生产者、消费者顺序关闭)
		 */
//		producer.close("生产者");
//		consumer.close("消费者");
	}
}
