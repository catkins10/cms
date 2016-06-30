package com.yuanluesoft.customise.complaint.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
  * 参数配置
  * @author linchuan
  *
  */
public class CustomiseParameter extends Record {
	private String type; //投诉类型

	/**
	  * @return the type
	  */
	public String getType() {
		return type;
	}

	/**
	  * @param type the type to set
	  */
	public void setType(String type) {
		this.type = type;
	}
}