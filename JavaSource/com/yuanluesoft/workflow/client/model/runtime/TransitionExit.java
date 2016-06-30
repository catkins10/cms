/*
 * Created on 2006-4-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class TransitionExit extends BaseExit {
	private List fieldValueList; //比较中用到的字段值
	
	/**
	 * @return Returns the fieldValueList.
	 */
	public List getFieldValueList() {
		return fieldValueList;
	}
	/**
	 * @param fieldValueList The fieldValueList to set.
	 */
	public void setFieldValueList(List fieldValueList) {
		this.fieldValueList = fieldValueList;
	}
}
