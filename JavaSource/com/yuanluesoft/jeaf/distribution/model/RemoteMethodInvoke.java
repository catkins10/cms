package com.yuanluesoft.jeaf.distribution.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class RemoteMethodInvoke implements Serializable {
	private String serviceName; //服务名称
	private String methodName; //方法名称
	private Serializable[] args; //参数列表
	
	public RemoteMethodInvoke(String serviceName, String methodName, Serializable[] args) {
		super();
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.args = args;
	}
	/**
	 * @return the args
	 */
	public Serializable[] getArgs() {
		return args;
	}
	/**
	 * @param args the args to set
	 */
	public void setArgs(Serializable[] args) {
		this.args = args;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}