/*
 * Created on 2005-11-14
 *
 */
package com.yuanluesoft.j2oa.todo.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 选项配置(todo_config_option)
 * @author linchuan
 *
 */
public class TodoOptionConfig extends Record {
	private String cetegories; //类别列表
	
	/**
	 * @return Returns the cetegories.
	 */
	public java.lang.String getCetegories() {
		return cetegories;
	}
	/**
	 * @param cetegories The cetegories to set.
	 */
	public void setCetegories(java.lang.String cetegories) {
		this.cetegories = cetegories;
	}
}
