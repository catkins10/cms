package com.yuanluesoft.workflow.client.model.runtime;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class DataFieldValue implements Serializable {
	private String dataFieldId;
	private Object dataFieldValue;
	
	/**
	 * @return the dataFieldId
	 */
	public String getDataFieldId() {
		return dataFieldId;
	}
	/**
	 * @param dataFieldId the dataFieldId to set
	 */
	public void setDataFieldId(String dataFieldId) {
		this.dataFieldId = dataFieldId;
	}
	/**
	 * @return the dataFieldValue
	 */
	public Object getDataFieldValue() {
		return dataFieldValue;
	}
	/**
	 * @param dataFieldValue the dataFieldValue to set
	 */
	public void setDataFieldValue(Object dataFieldValue) {
		this.dataFieldValue = dataFieldValue;
	}
}