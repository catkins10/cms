/*
 * Created on 2006-8-24
 *
 */
package com.yuanluesoft.workflow.client.model.resource;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowResource {
    private List actions; //操作列表
	private List subForms; //子表单列表
	private List programmingParticipants; //编程的办理人列表
	private List enumerations; //枚举类型列表
	private List dataFields; //字段列表
	private List applications; //过程列表
	
    /**
     * @return Returns the actions.
     */
    public List getActions() {
        return actions;
    }
    /**
     * @param actions The actions to set.
     */
    public void setActions(List actions) {
        this.actions = actions;
    }
    /**
     * @return Returns the applications.
     */
    public List getApplications() {
        return applications;
    }
    /**
     * @param applications The applications to set.
     */
    public void setApplications(List applications) {
        this.applications = applications;
    }
    /**
     * @return Returns the dataFields.
     */
    public List getDataFields() {
        return dataFields;
    }
    /**
     * @param dataFields The dataFields to set.
     */
    public void setDataFields(List dataFields) {
        this.dataFields = dataFields;
    }
    /**
     * @return Returns the enumerations.
     */
    public List getEnumerations() {
        return enumerations;
    }
    /**
     * @param enumerations The enumerations to set.
     */
    public void setEnumerations(List enumerations) {
        this.enumerations = enumerations;
    }
    /**
     * @return Returns the subForms.
     */
    public List getSubForms() {
        return subForms;
    }
    /**
     * @param subForms The subForms to set.
     */
    public void setSubForms(List subForms) {
        this.subForms = subForms;
    }
	/**
	 * @return the programmingParticipants
	 */
	public List getProgrammingParticipants() {
		return programmingParticipants;
	}
	/**
	 * @param programmingParticipants the programmingParticipants to set
	 */
	public void setProgrammingParticipants(List programmingParticipants) {
		this.programmingParticipants = programmingParticipants;
	}
}
