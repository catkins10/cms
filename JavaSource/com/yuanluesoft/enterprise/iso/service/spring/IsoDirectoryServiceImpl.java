/*
 * Created on 2007-7-1
 *
 */
package com.yuanluesoft.enterprise.iso.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.iso.pojo.IsoDirectory;
import com.yuanluesoft.enterprise.iso.pojo.IsoDirectoryPopedom;
import com.yuanluesoft.enterprise.iso.pojo.IsoDirectorySubjection;
import com.yuanluesoft.enterprise.iso.pojo.IsoDocument;
import com.yuanluesoft.enterprise.iso.service.IsoDirectoryService;
import com.yuanluesoft.enterprise.iso.service.IsoDocumentService;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class IsoDirectoryServiceImpl extends DirectoryServiceImpl implements IsoDirectoryService {
	private IsoDocumentService isoDocumentService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return IsoDirectory.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		return new DirectoryType[] {
				new DirectoryType("isoDirectory", "isoDirectory", "ISO目录", IsoDirectory.class, "/enterprise/iso/icons/isoDirectory.gif", null, false)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("verifier", "审核人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("approver", "批准人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("modifyApprover", "修改批准人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("loanApprover", "借阅批准人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("destroyApprover", "销毁批准人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("reader", "查询人员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/enterprise/iso/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/enterprise/iso/documentView.shtml?directoryId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		IsoDirectory isoDirectory = (IsoDirectory)newDirectory;
		//isoDirectory.setCategory(category); //文件类别
		//isoDirectory.setVersion(version); //版本号
		//isoDirectory.setControl(control); //受控状态
		//isoDirectory.setUrgency(urgency); //紧急程度
		//isoDirectory.setSecurity(security); //文件密级
		//isoDirectory.setStorage(storage); //保存期限
		//isoDirectory.setStorageDepartment(storageDepartment); //管理部门
		isoDirectory.setNumberingRule(null); //编号规则
		isoDirectory.setCreateWorkflowId(null); //登记流程ID
		isoDirectory.setCreateWorkflowName(null); //登记流程名称
		isoDirectory.setModifyWorkflowId(null); //修改流程ID
		isoDirectory.setModifyWorkflowName(null); //修改流程名称
		isoDirectory.setLoanWorkflowId(null); //借阅流程ID
		isoDirectory.setLoanWorkflowName(null); //借阅流程名称
		isoDirectory.setDestroyWorkflowId(null); //销毁流程ID
		isoDirectory.setDestroyWorkflowName(null); //销毁流程名称
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		String hql = "select IsoDocument" +
					 " from IsoDocument IsoDocument" +
					 " left join IsoDocument.subjections IsoDocumentSubjection" +
					 " where IsoDocumentSubjection.directoryId=" + directory.getId();
		List documents = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
		if(documents!=null && !documents.isEmpty()) {
			for(Iterator iterator = documents.iterator(); iterator.hasNext(); ) {
				IsoDocument document = (IsoDocument)iterator.next();
				if(document.getSubjections().size()<2) { //文档只属于一个栏目
					isoDocumentService.delete(document); //删除文档
				}
				else { //文档属于多个栏目
					//删除文档隶属记录
					getDatabaseService().deleteRecord((Record)ListUtils.findObjectByProperty(document.getSubjections(), "directoryId", new Long(directory.getId())));
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		//创建根站点
		IsoDirectory isoDirectory = new IsoDirectory();
		isoDirectory.setDirectoryName("ISO文件");
		save(isoDirectory);
		
		//更新隶属关系
		IsoDirectorySubjection subjection = new IsoDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//授权
		IsoDirectoryPopedom popedom = new IsoDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDirectoryService#getWorkflowId(long, java.lang.String)
	 */
	public String getWorkflowId(long directoryId, String workflowType) throws ServiceException {
		String hql = "select IsoDirectory." + workflowType + "WorkflowId" +
			  		 " from IsoDirectory IsoDirectory left join IsoDirectory.childSubjections IsoDirectorySubjection" +
			  		 " where IsoDirectorySubjection.directoryId=" + directoryId +
			  		 " and not IsoDirectory." + workflowType + "WorkflowId is null" +
			  		 " order by IsoDirectorySubjection.id";
		return (String)getDatabaseService().findRecordByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDirectoryService#getDocumentNumberingRule(long)
	 */
	public String getDocumentNumberingRule(long directoryId) throws ServiceException {
		//查找当前目录的配置
		String hql = "select IsoDirectory.numberingRule" +
			  		 " from IsoDirectory IsoDirectory left join IsoDirectory.childSubjections IsoDirectorySubjection" +
			  		 " where IsoDirectorySubjection.directoryId=" + directoryId +
			  		 " and not IsoDirectory.numberingRule is null" +
			  		 " order by IsoDirectorySubjection.id";
		return (String)getDatabaseService().findRecordByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long directoryId = RequestUtils.getParameterLongValue(notifyRequest, "directoryId"); //目录ID
		IsoDirectory directory = (IsoDirectory)getDirectory(directoryId);
		String workflowType = RequestUtils.getParameterStringValue(notifyRequest, "workflowType"); //流程类型
		String workflowName = null;
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) {
			workflowId = null;
		}
		else {
			workflowName = workflowPackage.getName();
		}
		PropertyUtils.setProperty(directory, workflowType + "WorkflowId", workflowId); //流程ID 
		PropertyUtils.setProperty(directory, workflowType + "WorkflowName", workflowName); //流程名称
		update(directory);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		ApplicationTreeNavigator applicationTreeNavigator = (ApplicationTreeNavigator)super.getApplicationNavigator(applicationName, request, sessionInfo);
		if("0".equals(applicationTreeNavigator.getTree().getRootNode().getNodeId())) { //根目录
			String icon = Environment.getWebApplicationUrl() + "/jeaf/application/icons/view.gif";
			applicationTreeNavigator.getTree().appendChildNode("todoDocument", "待办文件", "view", icon, false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=enterprise/iso&viewName=todoDocument");
			applicationTreeNavigator.getTree().appendChildNode("processingDocument", "在办文件", "view", icon, false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=enterprise/iso&viewName=processingDocument");
			applicationTreeNavigator.getTree().appendChildNode("modifyDocument", "修改记录", "view", icon, false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=enterprise/iso&viewName=modifyDocument");
			applicationTreeNavigator.getTree().appendChildNode("destroyDocument", "销毁记录", "view", icon, false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=enterprise/iso&viewName=destroyDocument");
		}
		return applicationTreeNavigator;
	}
	
	/**
	 * @return the isoDocumentService
	 */
	public IsoDocumentService getIsoDocumentService() {
		return isoDocumentService;
	}

	/**
	 * @param isoDocumentService the isoDocumentService to set
	 */
	public void setIsoDocumentService(IsoDocumentService isoDocumentService) {
		this.isoDocumentService = isoDocumentService;
	}
}
