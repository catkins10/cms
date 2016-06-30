package com.yuanluesoft.enterprise.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:合同模板(enterprise_contract_template)
 * @author linchuan
 *
 */
public class EnterpriseContractTemplate extends Record {
	private String name; //模板名称
	private String appliedProjectTypes; //适用的项目类型
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayAppliedProjectTypes() {
		return appliedProjectTypes==null || appliedProjectTypes.equals("") ? null : appliedProjectTypes.substring(1, appliedProjectTypes.length()-1);
	}
	
	/**
	 * @return the appliedProjectTypes
	 */
	public String getAppliedProjectTypes() {
		return appliedProjectTypes;
	}
	/**
	 * @param appliedProjectTypes the appliedProjectTypes to set
	 */
	public void setAppliedProjectTypes(String appliedProjectTypes) {
		this.appliedProjectTypes = appliedProjectTypes;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}