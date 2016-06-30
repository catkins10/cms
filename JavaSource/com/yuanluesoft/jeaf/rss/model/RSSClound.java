package com.yuanluesoft.jeaf.rss.model;

/**
 * RSS注册进程，以获得 feed 更新的立即通知
 * @author linchuan
 *
 */
public class RSSClound {
	private String domain; //loud程序所在机器的域名或IP地址   radio.xmlstoragesystem.com  
	private int port; //访问clound程序所通过的端口   80 
	private String path; //程序所在路径（不一定是真实路径）   /RPC2 
	private String registerProcedure; //注册的可提供的服务或过程   xmlStorageSystem.rssPleaseNotify  
	private String protocol; //协议 xml-rpc, soap , http-post 之一   xml-rpc
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	/**
	 * @return the registerProcedure
	 */
	public String getRegisterProcedure() {
		return registerProcedure;
	}
	/**
	 * @param registerProcedure the registerProcedure to set
	 */
	public void setRegisterProcedure(String registerProcedure) {
		this.registerProcedure = registerProcedure;
	}
}
