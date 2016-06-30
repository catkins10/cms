package com.yuanluesoft.eai.client.local;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.eai.client.exception.EAIException;
import com.yuanluesoft.eai.client.model.EAI;
import com.yuanluesoft.eai.server.EAIServer;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * EAI客户端
 * @author linchuan
 *
 */
public class EAIClientImpl implements EAIClient {
	private String applicationSetName; //应用集合名称
	private EAIServer eaiServer; //EAI服务器
	private String webApplicationUrl; //应用URL
	private String webApplicationSafeUrl; //应用URL
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		//20秒后启动更新应用配置
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					String applicationSetXML = FileUtils.readStringFromFile(Environment.getWebinfPath() + "applications-config.xml", "utf-8");
					applicationSetXML = applicationSetXML.replaceAll("\\x7bWEBAPPLICATIONURL\\x7d", webApplicationUrl)
									 	.replaceAll("\\x7bWEBAPPLICATIONSAFEURL\\x7d", webApplicationSafeUrl)
									 	.replaceAll("URL=\"/", "URL=\"" + webApplicationUrl + "/");
					updateApplicationSet(applicationSetXML);
				}
				catch (Exception e) {
					Logger.exception(e);
				}
				timer.cancel(); 
			}
		}, 10000); //10秒
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#getApplicationTitle(java.lang.String)
	 */
	public String getApplicationTitle(String applicationName) throws EAIException {
		return eaiServer.getApplicationTitle(applicationSetName, applicationName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#getEAI(java.lang.String)
	 */
	public EAI getEAI(String userLoginName) throws EAIException {
		return eaiServer.getEAI(userLoginName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#listApplicationPrivileges(java.lang.String, java.lang.String)
	 */
	public List listApplicationPrivileges(String userLoginName, String applicationName) throws EAIException {
		return eaiServer.listApplicationPrivileges(userLoginName, applicationSetName, applicationName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#listLinkPrivileges(java.lang.String, java.lang.String)
	 */
	public List listLinkPrivileges(String userLoginName, String linkName) throws EAIException {
		return eaiServer.listLinkPrivileges(userLoginName, linkName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#isEAIManager(java.lang.String)
	 */
	public boolean isEAIManager(String userLoginName) throws EAIException {
		return eaiServer.isEAIManager(userLoginName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#isEAIVisitor(java.lang.String)
	 */
	public boolean isEAIVisitor(String userLoginName) throws EAIException {
		return eaiServer.isEAIVisitor(userLoginName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#listVisitors(java.lang.String, java.lang.String)
	 */
	public List listVisitors(String applicationName, String manageUnit) throws EAIException {
		return eaiServer.listVisitors(applicationSetName, applicationName, manageUnit);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#listWorkflows(java.lang.String)
	 */
	public List listWorkflows(String applicationName) throws EAIException {
		return eaiServer.listWorkflows(applicationSetName, applicationName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.client.EAIClient#updateApplicationSet(java.lang.String)
	 */
	public void updateApplicationSet(String applicationSetXML) throws EAIException {
		eaiServer.updateApplicationSet(applicationSetName, applicationSetXML);
	}
	
	/**
	 * @return the applicationSetName
	 */
	public String getApplicationSetName() {
		return applicationSetName;
	}
	/**
	 * @param applicationSetName the applicationSetName to set
	 */
	public void setApplicationSetName(String applicationSetName) {
		this.applicationSetName = applicationSetName;
	}
	/**
	 * @return the eaiServer
	 */
	public EAIServer getEaiServer() {
		return eaiServer;
	}
	/**
	 * @param eaiServer the eaiServer to set
	 */
	public void setEaiServer(EAIServer eaiServer) {
		this.eaiServer = eaiServer;
	}

	/**
	 * @return the webApplicationSafeUrl
	 */
	public String getWebApplicationSafeUrl() {
		return webApplicationSafeUrl;
	}

	/**
	 * @param webApplicationSafeUrl the webApplicationSafeUrl to set
	 */
	public void setWebApplicationSafeUrl(String webApplicationSafeUrl) {
		this.webApplicationSafeUrl = webApplicationSafeUrl;
	}

	/**
	 * @return the webApplicationUrl
	 */
	public String getWebApplicationUrl() {
		return webApplicationUrl;
	}

	/**
	 * @param webApplicationUrl the webApplicationUrl to set
	 */
	public void setWebApplicationUrl(String webApplicationUrl) {
		this.webApplicationUrl = webApplicationUrl;
	}
}