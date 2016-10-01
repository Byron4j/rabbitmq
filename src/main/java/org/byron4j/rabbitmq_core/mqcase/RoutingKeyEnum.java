package org.byron4j.rabbitmq_core.mqcase;

import lombok.Getter;
import lombok.Setter;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月27日
 *  路由枚举类
 */
public enum RoutingKeyEnum {
	RED("RED","红"),
	ORANGE("ORANGE","橙"),
	YELLOW("YELLOW","黄"),
	GREEN("GREEN","绿"),
	BLUE("BLUE","蓝"),
	GRAY("GRAY","灰"),
	LIGHTGREEN("LIGHTGREEN","浅绿"),
	GRAPE("GRAPE","紫"),;
	
	private @Getter @Setter String name;
	private @Getter @Setter String desc;
	
	private RoutingKeyEnum(String name, String desc) {
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
