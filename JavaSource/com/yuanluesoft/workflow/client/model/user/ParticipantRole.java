/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 *
 * @author LinChuan
 * 角色办理人
 *
 */
public class ParticipantRole extends Element implements Serializable, WorkflowParticipant {
	private String personIds;
	private String personNames; 
	
	public ParticipantRole() {
		super();
	}
	
	public ParticipantRole(String id, String name) {
		super(id, name);
	}
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		return getName() + (personNames==null ? "(任意一人办理)":"(由" + personNames + "办理)"); 
	}
	/**
	 * @return the personIds
	 */
	public String getPersonIds() {
		return personIds;
	}
	/**
	 * @param personIds the personIds to set
	 */
	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}
	/**
	 * @return the personNames
	 */
	public String getPersonNames() {
		return personNames;
	}
	/**
	 * @param personNames the personNames to set
	 */
	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}
}
