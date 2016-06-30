package com.yuanluesoft.jeaf.sms.model;

/**
 * 短信业务
 * @author linchuan
 *
 */
public class SmsBusiness {
	private String name; //名称
	private boolean sendPopedomConfig; //是否需要发送权限配置
	private boolean receivePopedomConfig; //是否需求接收权限配置
	
	public SmsBusiness(String name, boolean sendPopedomConfig, boolean receivePopedomConfig) {
		super();
		this.name = name;
		this.sendPopedomConfig = sendPopedomConfig;
		this.receivePopedomConfig = receivePopedomConfig;
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
	/**
	 * @return the receivePopedomConfig
	 */
	public boolean isReceivePopedomConfig() {
		return receivePopedomConfig;
	}
	/**
	 * @param receivePopedomConfig the receivePopedomConfig to set
	 */
	public void setReceivePopedomConfig(boolean receivePopedomConfig) {
		this.receivePopedomConfig = receivePopedomConfig;
	}
	/**
	 * @return the sendPopedomConfig
	 */
	public boolean isSendPopedomConfig() {
		return sendPopedomConfig;
	}
	/**
	 * @param sendPopedomConfig the sendPopedomConfig to set
	 */
	public void setSendPopedomConfig(boolean sendPopedomConfig) {
		this.sendPopedomConfig = sendPopedomConfig;
	}
}