package com.yuanluesoft.cms.monitor.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 监察参数:采集对象配置(monitor_parameter_content)
 * @author linchuan
 *
 */
public class MonitorContentParameter extends Record {
	private long parameterId; //参数配置ID
	private String contentClassName; //采集对象类名称
	private String timeout; //超时参数
	private MonitorParameter monitorParameter; //监察参数配置
	
	//扩展参数
	private String contentName; //内容名称
	private boolean timeoutSupport; //是否支持超时监察

	/**
	 * @return the contentClassName
	 */
	public String getContentClassName() {
		return contentClassName;
	}
	/**
	 * @param contentClassName the contentClassName to set
	 */
	public void setContentClassName(String contentClassName) {
		this.contentClassName = contentClassName;
	}
	/**
	 * @return the parameterId
	 */
	public long getParameterId() {
		return parameterId;
	}
	/**
	 * @param parameterId the parameterId to set
	 */
	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}
	/**
	 * @return the timeout
	 */
	public String getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	/**
	 * @return the timeoutSupport
	 */
	public boolean isTimeoutSupport() {
		return timeoutSupport;
	}
	/**
	 * @param timeoutSupport the timeoutSupport to set
	 */
	public void setTimeoutSupport(boolean timeoutSupport) {
		this.timeoutSupport = timeoutSupport;
	}
	/**
	 * @return the contentName
	 */
	public String getContentName() {
		return contentName;
	}
	/**
	 * @param contentName the contentName to set
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	/**
	 * @return the monitorParameter
	 */
	public MonitorParameter getMonitorParameter() {
		return monitorParameter;
	}
	/**
	 * @param monitorParameter the monitorParameter to set
	 */
	public void setMonitorParameter(MonitorParameter monitorParameter) {
		this.monitorParameter = monitorParameter;
	}
}