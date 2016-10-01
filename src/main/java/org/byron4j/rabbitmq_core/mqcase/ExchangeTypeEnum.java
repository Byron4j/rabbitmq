package org.byron4j.rabbitmq_core.mqcase;

import lombok.Getter;
import lombok.Setter;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月27日
 *  交换类型的枚举类--【direct】,【topic】,【headers】,【fanout】
 */
public enum ExchangeTypeEnum {
	DIRECT("direct","直接"),
	TOPIC("topic","主题"),
	HEADERS("headers","标题"),
	FANOUT("fanout","广播");
	
	private @Getter @Setter String name;
	private @Getter @Setter String desc;
	
	private ExchangeTypeEnum(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
