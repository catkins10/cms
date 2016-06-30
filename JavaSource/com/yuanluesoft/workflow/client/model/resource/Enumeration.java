/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.workflow.client.model.resource;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Enumeration extends Element implements Serializable {
	private List valueList; //值列表
	
	/**
	 * @return Returns the valueList.
	 */
	public List getValueList() {
		return valueList;
	}
	/**
	 * @param valueList The valueList to set.
	 */
	public void setValueList(List valueList) {
		this.valueList = valueList;
	}
}
