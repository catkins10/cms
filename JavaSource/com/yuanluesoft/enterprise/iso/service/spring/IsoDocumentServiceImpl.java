package com.yuanluesoft.enterprise.iso.service.spring;

import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.iso.pojo.IsoDocument;
import com.yuanluesoft.enterprise.iso.pojo.IsoDocumentSubjection;
import com.yuanluesoft.enterprise.iso.service.IsoDirectoryService;
import com.yuanluesoft.enterprise.iso.service.IsoDocumentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author linchuan
 *
 */
public class IsoDocumentServiceImpl extends BusinessServiceImpl implements IsoDocumentService {
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private AttachmentService attachmentService; //附件服务
	private IsoDirectoryService isoDirectoryService; //ISO目录服务
	private NumerationService numerationService; //编号服务

	private double versionInitialValue; //版本好初始值
	private double versionIncrement; //版本号增量

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#getDocument(long)
	 */
	public IsoDocument getDocument(long documentId) throws ServiceException {
		return (IsoDocument)load(IsoDocument.class, documentId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#createModify(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public IsoDocument createModify(long sourceDocumentId, SessionInfo sessionInfo) throws ServiceException {
		return createModifyOrDestroy(sourceDocumentId, sessionInfo, true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#createDestroy(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public IsoDocument createDestroy(long sourceDocumentId, SessionInfo sessionInfo) throws ServiceException {
		return createModifyOrDestroy(sourceDocumentId, sessionInfo, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#complateCreate(com.yuanluesoft.enterprise.iso.pojo.IsoDocument, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeCreate(final IsoDocument document, SessionInfo sessionInfo) throws ServiceException {
		document.setIsValid('1');
		//编号
		String numberingRule = isoDirectoryService.getDocumentNumberingRule(((IsoDocumentSubjection)document.getSubjections().iterator().next()).getDirectoryId());
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("版本号".equals(fieldName)) {
					return "" + document.getVersion();
				}
				return null;
			}
		};
		document.setDocumentNumber(numerationService.generateNumeration("ISO管理", "文件编号", numberingRule, false, numerationCallback));
		getDatabaseService().updateRecord(document);
		//删除权限控制记录
		getDatabaseService().deleteRecordsByHql("from IsoDocumentPrivilege IsoDocumentPrivilege where IsoDocumentPrivilege.recordId=" + document.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#completeModify(com.yuanluesoft.enterprise.iso.pojo.IsoDocument, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeModify(IsoDocument documentModify, SessionInfo sessionInfo) throws ServiceException {
		try {
			//获取企业记录
			IsoDocument document = getDocument(documentModify.getSourceDocumentId());
			if(document==null) {
				documentModify.setIsValid('C');
				return;
			}

			//更新主记录
			String workflowInstanceId = document.getWorkflowInstanceId(); //工作流实例ID
			PropertyUtils.copyProperties(document, documentModify);

			document.setId(document.getSourceDocumentId()); //ID
			document.setSourceDocumentId(0);
			document.setIsValid('1');
			document.setIsModify('0');
			document.setModifyDescription(null);
			document.setWorkflowInstanceId(workflowInstanceId); //工作流实例ID
			//更新版本号
			document.setVersion(document.getVersion() + versionIncrement);
			getDatabaseService().updateRecord(document);
			
			//更新隶属目录
			getDatabaseService().deleteRecordsByHql("from IsoDocumentSubjection IsoDocumentSubjection where IsoDocumentSubjection.documentId=" + document.getId());
			for(Iterator iterator = documentModify.getSubjections().iterator(); iterator.hasNext();) {
	    		IsoDocumentSubjection subjection = (IsoDocumentSubjection)iterator.next();
	    		IsoDocumentSubjection newSubjection = new IsoDocumentSubjection();
	    		newSubjection.setId(UUIDLongGenerator.generateId()); //ID
	    		newSubjection.setDocumentId(document.getId()); //文档ID
	    		newSubjection.setDirectoryId(subjection.getDirectoryId()); //目录ID
	    		getDatabaseService().saveRecord(newSubjection);
	    	}
			
			//更新附件
			attachmentService.deleteAll("enterprise/iso", "attachments", document.getId());
			attachmentService.replace("enterprise/iso", documentModify.getId(), "attachments", "enterprise/iso", document.getId(), "attachments");

			documentModify.setIsValid('C');
		} 
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#completeDestroy(com.yuanluesoft.enterprise.iso.pojo.IsoDocument, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeDestroy(IsoDocument documentDestory, SessionInfo sessionInfo) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from IsoDocument IsoDocument where IsoDocument.id=" + documentDestory.getSourceDocumentId());
		documentDestory.setIsValid('C');
	}
	
	/**
	 * 创建修改或销毁记录
	 * @param enterpriseId
	 * @param sessionInfo
	 * @param isModify
	 * @return
	 * @throws ServiceException
	 */
	private IsoDocument createModifyOrDestroy(long sourceDocumentId, SessionInfo sessionInfo, boolean isModify) throws ServiceException {
		String workflowInstanceId = null;
		try {
			//获取源文件
			IsoDocument document = getDocument(sourceDocumentId);
			
			//复制
			IsoDocument newDocument = new IsoDocument();
			PropertyUtils.copyProperties(newDocument, document);
			newDocument.setId(UUIDLongGenerator.generateId()); //ID
			newDocument.setSourceDocumentId(sourceDocumentId); //变更前的ID
			newDocument.setIsValid('0');
			newDocument.setIsModify(isModify ? '1' : '0'); //设置为修改记录
			newDocument.setIsDestroy(isModify ? '0' : '1'); //设置为销毁记录
	    	newDocument.setOpinions(null);
	    	
	    	//复制隶属目录
	    	for(Iterator iterator = document.getSubjections().iterator(); iterator.hasNext();) {
	    		IsoDocumentSubjection subjection = (IsoDocumentSubjection)iterator.next();
	    		IsoDocumentSubjection newSubjection = new IsoDocumentSubjection();
	    		newSubjection.setId(UUIDLongGenerator.generateId()); //ID
	    		newSubjection.setDocumentId(newDocument.getId()); //文档ID
	    		newSubjection.setDirectoryId(subjection.getDirectoryId()); //目录ID
	    		getDatabaseService().saveRecord(newSubjection);
	    	}
	    	
	    	//复制附件
	    	attachmentService.replace("enterprise/iso", document.getId(), "attachments", "enterprise/iso", newDocument.getId(), "attachments");
	    	
			//获取流程
	    	String workflowId = isoDirectoryService.getWorkflowId(((IsoDocumentSubjection)document.getSubjections().iterator().next()).getDirectoryId(), (isModify ? "modify" : "destroy"));
			WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry(workflowId, null, document, sessionInfo);
			//创建工作流实例
			workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId(), false, newDocument, null, sessionInfo);

			newDocument.setWorkflowInstanceId(workflowInstanceId); //工作流实例ID
	    	getDatabaseService().saveRecord(newDocument);
			return newDocument;
		}
		catch(Exception e) {
			Logger.exception(e);
			try {
				//删除流程实例
				workflowExploitService.removeWorkflowInstance(workflowInstanceId, null, sessionInfo);
			}
			catch(Exception we) {

			}
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.iso.service.IsoDocumentService#updateDocumentSubjections(com.yuanluesoft.enterprise.iso.pojo.IsoDocument, boolean, java.lang.String)
	 */
	public void updateDocumentSubjections(IsoDocument document, boolean newDocument, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		if(!newDocument) { //不是新纪录
			//删除原来的隶属关系
			getDatabaseService().deleteRecordsByHql("from IsoDocumentSubjection IsoDocumentSubjection where IsoDocumentSubjection.documentId=" + document.getId());
		}
		//保存新的隶属关系
		String[] ids = subjectionDirectoryIds.split(",");
		for(int i=0; i<ids.length; i++) {
			IsoDocumentSubjection subjection = new IsoDocumentSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setDocumentId(document.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
		}
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the isoDirectoryService
	 */
	public IsoDirectoryService getIsoDirectoryService() {
		return isoDirectoryService;
	}

	/**
	 * @param isoDirectoryService the isoDirectoryService to set
	 */
	public void setIsoDirectoryService(IsoDirectoryService isoDirectoryService) {
		this.isoDirectoryService = isoDirectoryService;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}

	/**
	 * @return the versionIncrement
	 */
	public double getVersionIncrement() {
		return versionIncrement;
	}

	/**
	 * @param versionIncrement the versionIncrement to set
	 */
	public void setVersionIncrement(double versionIncrement) {
		this.versionIncrement = versionIncrement;
	}

	/**
	 * @return the versionInitialValue
	 */
	public double getVersionInitialValue() {
		return versionInitialValue;
	}

	/**
	 * @param versionInitialValue the versionInitialValue to set
	 */
	public void setVersionInitialValue(double versionInitialValue) {
		this.versionInitialValue = versionInitialValue;
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}