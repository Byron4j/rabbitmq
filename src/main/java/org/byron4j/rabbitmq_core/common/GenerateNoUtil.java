package org.byron4j.rabbitmq_core.common;

import java.util.Random;

/**
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月14日
 *  流水号生成器
 */
public class GenerateNoUtil {

	/**
	 * 生成随机流水号
	 * @param prefix
	 * @return
	 */
	  public static String generateFlowNo(String prefix) {
		 String result = "" + (null == prefix ? "" : prefix.trim());
	    Random r = new Random();
	    for(int i = 1; i<=3 ; i++){
	    	result += r.nextInt(10);
	    }
	    result += System.currentTimeMillis();
	    for(int i = 1; i<=3 ; i++){
	    	result += r.nextInt(10);
	    }
	    return result;
	  }
}
