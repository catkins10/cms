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
public class Application extends Element implements Serializable {
	private List formalParameterList;
	private String type; //类型:procedure/过程,decision/判断
	private String service; //服务
	private String function; //函数
	private String finishMode; //结束方式
	
	/**
	 * @return Returns the formalParameterList.
	 */
	public List getFormalParameterList() {
		return formalParameterList;
	}
	/**
	 * @param formalParameterList The formalParameterList to set.
	 */
	public void setFormalParameterList(List formalParameterList) {
		this.formalParameterList = formalParameterList;
	}
	/**
	 * @return Returns the function.
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * @param function The function to set.
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * @return Returns the service.
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service The service to set.
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return Returns the finishMode.
	 */
	public String getFinishMode() {
		return finishMode;
	}
	/**
	 * @param finishMode The finishMode to set.
	 */
	public void setFinishMode(String finishMode) {
		this.finishMode = finishMode;
	}
}
