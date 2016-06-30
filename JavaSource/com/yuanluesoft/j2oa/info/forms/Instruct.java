package com.yuanluesoft.j2oa.info.forms;

import com.yuanluesoft.j2oa.info.pojo.InfoInstruct;

/**
 * 
 * @author linchuan
 *
 */
public class Instruct extends Info {
	private InfoInstruct instruct = new InfoInstruct(); //领导批示

	/**
	 * @return the instruct
	 */
	public InfoInstruct getInstruct() {
		return instruct;
	}

	/**
	 * @param instruct the instruct to set
	 */
	public void setInstruct(InfoInstruct instruct) {
		this.instruct = instruct;
	}
}