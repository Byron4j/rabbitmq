package org.byron4j.rabbitmq_core.rpc;

import java.sql.Timestamp;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月28日
 *  This comment is ...
 */
public class DateUtil {

	public static String getNowTime(){
//		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return new Timestamp(System.currentTimeMillis()).toString();
	}

}
