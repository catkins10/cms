package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

/**
 * 签名
 * @author linchuan
 *
 */
public class WorkflowSign {
	private String signPersonName; //需要签的名字
	private boolean isAgent; //当前用户是否是代理
	private List signDataFields; //需要签的字段列表
	
	/**
	 * @return the isAgent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	/**
	 * @param isAgent the isAgent to set
	 */
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}
	/**
	 * @return the signDataFields
	 */
	public List getSignDataFields() {
		return signDataFields;
	}
	/**
	 * @param signDataFields the signDataFields to set
	 */
	public void setSignDataFields(List signDataFields) {
		this.signDataFields = signDataFields;
	}
	/**
	 * @return the signPersonName
	 */
	public String getSignPersonName() {
		return signPersonName;
	}
	/**
	 * @param signPersonName the signPersonName to set
	 */
	public void setSignPersonName(String signPersonName) {
		this.signPersonName = signPersonName;
	}
}