package com.yuanluesoft.customise.complaint.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
  * 参数配置
  * @author linchuan
  *
  */
public class Parameter extends ActionForm {
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