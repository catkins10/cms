package com.yuanluesoft.eai.server.local;

import java.util.List;

import com.yuanluesoft.eai.client.exception.EAIException;
import com.yuanluesoft.eai.client.model.EAI;
import com.yuanluesoft.eai.server.EAIServer;
import com.yuanluesoft.eai.server.configure.service.EAIConfigureService;
import com.yuanluesoft.eai.server.service.EAIService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class EAIServerImpl implements EAIServer {
	private EAIService eaiService; //EAI服务
	private EAIConfigureService eaiConfigureService; //EAI配置服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#getApplicationTitle(java.lang.String, java.lang.String)
	 */
	public String getApplicationTitle(String applicationSetName, String applicationName) throws EAIException {
		try {
			return eaiService.getApplicationTitle(applicationSetName, applicationName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#getEAI(java.lang.String)
	 */
	public EAI getEAI(String userLoginName) throws EAIException {
		try {
			return eaiService.getEAI(userLoginName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#isEAIManager(java.lang.String)
	 */
	public boolean isEAIManager(String userLoginName) throws EAIException {
		try {
			return eaiService.isEAIManager(userLoginName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#isEAIVisitor(java.lang.String)
	 */
	public boolean isEAIVisitor(String userLoginName) throws EAIException {
		try {
			return eaiService.isEAIVisitor(userLoginName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#listApplicationPrivileges(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List listApplicationPrivileges(String userLoginName, String applicationSetName, String applicationName) throws EAIException {
		try {
			return eaiService.listApplicationPrivileges(userLoginName, applicationSetName, applicationName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#listLinkPrivileges(java.lang.String, java.lang.String)
	 */
	public List listLinkPrivileges(String userLoginName, String linkName) throws EAIException {
		try {
			return eaiService.listLinkPrivilegesByName(userLoginName, linkName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#listVisitors(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List listVisitors(String applicationSetName, String applicationName, String manageUnit) throws EAIException {
		try {
			return eaiService.listVisitors(applicationSetName, applicationName, manageUnit);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#listWorkflows(java.lang.String, java.lang.String)
	 */
	public List listWorkflows(String applicationSetName, String applicationName) throws EAIException {
		try {
			return eaiService.listWorkflows(applicationSetName, applicationName);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.eai.server.EAIServer#updateApplicationSet(java.lang.String, java.lang.String)
	 */
	public void updateApplicationSet(String applicationSetName, String applicationSetXML) throws EAIException {
		try {
			eaiConfigureService.updateApplicationSet(applicationSetName, applicationSetXML);
		} 
		catch (ServiceException e) {
			Logger.exception(e);
			throw new EAIException(e.getMessage());
		}
	}

	/**
	 * @return the eaiService
	 */
	public EAIService getEaiService() {
		return eaiService;
	}

	/**
	 * @param eaiService the eaiService to set
	 */
	public void setEaiService(EAIService eaiService) {
		this.eaiService = eaiService;
	}

	/**
	 * @return the eaiConfigureService
	 */
	public EAIConfigureService getEaiConfigureService() {
		return eaiConfigureService;
	}

	/**
	 * @param eaiConfigureService the eaiConfigureService to set
	 */
	public void setEaiConfigureService(EAIConfigureService eaiConfigureService) {
		this.eaiConfigureService = eaiConfigureService;
	}
}