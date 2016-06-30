/*
 * Created on 2005-4-5
 *
 */
package com.yuanluesoft.j2oa.dispatch.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.archives.administrative.services.AdministrativeArchivesService;
import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchBody;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchFilingConfig;
import com.yuanluesoft.j2oa.dispatch.service.DispatchService;
import com.yuanluesoft.j2oa.exchange.soap.ExchangeDocumentSoapClient;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.sso.soap.SsoSessionSoapClient;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 *
 * @author linchuan
 *
 */
public class DispatchServiceImpl extends BusinessServiceImpl implements DispatchService {
	private AdministrativeArchivesService administrativeArchivesService; //文书档案服务
	private DatabankDataService databankDataService; //资料库服务
	private AttachmentService attachmentService; //附件服务
	private RecordControlService recordControlService; //记录权限控制服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private RemoteDocumentService remoteDocumentService; //远程文档服务
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchService#getDispatch(long)
	 */
	public Dispatch getDispatch(long id) throws ServiceException {
		return (Dispatch)load(Dispatch.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchService#saveBody(com.yuanluesoft.j2oa.dispatch.pojo.Dispatch, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public void saveBody(final Dispatch dispatch, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新正文
				remoteDocumentService.updateRecordFile(documentPath, dispatch, "body", dispatch.getSubject() + ".doc");
				//更新正式文件
				if(officalDocumentPath!=null) {
					remoteDocumentService.updateRecordFile(officalDocumentPath, dispatch, "official", dispatch.getSubject() + ".doc");
				}
		    	//更新页数
		    	if(dispatch.getPageCount()!=pageCount) {
		    		dispatch.setPageCount(pageCount);
		    		getDatabaseService().updateRecord(dispatch);
		    	}
				//读取HTML内容
				String htmlBody = remoteDocumentService.retrieveRecordHtmlBody(htmlPagePath, htmlFilesPath, pageWidth, dispatch, "html", Environment.getContextPath() + "/j2oa/dispatch/downloadAttachment.shtml?id=" + dispatch.getId() + "&fileName=$1");
				//保存html正文
		    	DispatchBody dispatchBody = (DispatchBody)getDatabaseService().findRecordByKey(DispatchBody.class.getName(), "dispatchId", new Long(dispatch.getId()));
		    	if(dispatchBody!=null) {
					dispatchBody.setHtmlBody(htmlBody);
				    getDatabaseService().updateRecord(dispatchBody);
			    }
			    else { //新文档
			        dispatchBody = new DispatchBody();
				    dispatchBody.setId(UUIDLongGenerator.generateId());
				    dispatchBody.setDispatchId(dispatch.getId());
				    dispatchBody.setHtmlBody(htmlBody);
				    getDatabaseService().saveRecord(dispatchBody);
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchService#getFilingConfig()
	 */
	public DispatchFilingConfig getFilingConfig() throws ServiceException {
		return (DispatchFilingConfig)getDatabaseService().findRecordByHql("from DispatchFilingConfig DispatchFilingConfig");
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchService#distribute(com.yuanluesoft.j2oa.dispatch.pojo.Dispatch, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void distribute(Dispatch dispatch, String workItemId , SessionInfo sessionInfo) throws ServiceException {
		//内部分发
		RecordVisitorList visitors = getRecordControlService().getVisitors(dispatch.getId(), Dispatch.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		WorkflowMessage workflowMessage = new WorkflowMessage();
		workflowMessage.setType("发文"); //消息类型
		workflowMessage.setContent(dispatch.getSubject());
		workflowMessage.setHref(Environment.getContextPath() + "/j2oa/dispatch/dispatch.shtml?act=edit&id=" + dispatch.getId()); //链接
		if(visitors!=null && visitors.getVisitorIds()!=null) {
		    String[] ids = visitors.getVisitorIds().split(",");
		    String[] names = visitors.getVisitorNames().split(",");
		    for(int i=0; i<ids.length; i++) {
	        	workflowExploitService.addVisitor(dispatch.getWorkflowInstanceId(), workItemId, ids[i], names[i], workflowMessage, dispatch, sessionInfo);
		    }
		}
		//分发到公文交换平台
		if(soapPassport!=null) {
			try {
				//登录
				SsoSessionSoapClient ssoSessionSoapClient = new SsoSessionSoapClient(soapConnectionPool, soapPassport);
				String ssoSessionId = ssoSessionSoapClient.createSsoSession(sessionInfo.getLoginName(), Encoder.getInstance().md5Encode(sessionInfo.getPassword()));
				//创建公文
				ExchangeDocumentSoapClient exchangeDocumentSoapClient = new ExchangeDocumentSoapClient(soapConnectionPool, soapPassport);
				long documentId = exchangeDocumentSoapClient.createDocument(
						"" + dispatch.getId(),
						dispatch.getSubject(),
						sessionInfo.getUnitName(),
						dispatch.getSignPerson(),
						dispatch.getDocWord(),
						dispatch.getGenerateDate(),
						dispatch.getDocType(),
						dispatch.getSecureLevel(),
						dispatch.getSecureTerm(),
						dispatch.getPriority(),
						dispatch.getKeyword(),
						dispatch.getPrintNumber(),
						DateTimeUtils.date(),
						dispatch.getMainSend(),
						dispatch.getCopySend(),
						null,
						null,
						ssoSessionId);
				//上传正文
				List attachments = attachmentService.list("j2oa/dispatch", "official", dispatch.getId(), false, 0, null);
				ListUtils.removeObjectByProperty(attachments, "name", "办理单.html"); //排除办理单
				exchangeDocumentSoapClient.uploadFiles(documentId, true, attachments, ssoSessionId);
				
				//上传附件
				attachments = attachmentService.list("j2oa/dispatch", "attachment", dispatch.getId(), false, 0, null);
				exchangeDocumentSoapClient.uploadFiles(documentId, false, attachments, ssoSessionId);
				
				//发布公文
				exchangeDocumentSoapClient.issueDocument(documentId, ssoSessionId);
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
		}
		//设置发布时间
		dispatch.setDistributeDate(DateTimeUtils.date());
		update(dispatch);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchService#filing(com.yuanluesoft.j2oa.dispatch.pojo.Dispatch, com.yuanluesoft.j2oa.dispatch.pojo.DispatchFilingConfig, com.yuanluesoft.archives.administrative.model.FilingOption, long, java.lang.String)
	 */
	public void filing(Dispatch dispatch, DispatchFilingConfig filingConfig, FilingOption filingOption, long filingPersonId, String filingPersonName) throws ServiceException {
		try {
			List bodyFileNames = new ArrayList(); //正文文件列表
			List attachmentFileNames = new ArrayList(); //附件文件列表
			//获取正式文件
			List attachments = attachmentService.list("j2oa/dispatch", "official", dispatch.getId(), false, 0, null);
			Attachment handling = (Attachment)ListUtils.removeObjectByProperty(attachments, "name", "办理单.html");
			if(handling!=null) {
				attachmentFileNames.add(handling.getFilePath());
			}
			if(attachments!=null && !attachments.isEmpty()) {
				bodyFileNames.add(((Attachment)attachments.get(0)).getFilePath());
			}
			//获取附件文件路径列表
			attachments = attachmentService.list("j2oa/dispatch", "attachment", dispatch.getId(), false, 0, null);
			for(Iterator iterator = attachments==null ? null : attachments.iterator(); iterator!=null && iterator.hasNext();) {
				Attachment attachment = (Attachment)iterator.next();
				attachmentFileNames.add(attachment.getFilePath());
			}
			if(filingConfig.getToArchives()=='1') { //归档到档案系统
				administrativeArchivesService.filing(dispatch.getSubject(),
													 dispatch.getKeyword(),
													 "发文",
													 filingOption.getDocCategory(),
													 dispatch.getDocWord(),
													 filingOption.getResponsibilityPerson(),
													 filingOption.getFondsName(),
													 filingOption.getSecureLevel(),
													 filingOption.getRotentionPeriod(),
													 filingOption.getUnit(),
													 dispatch.getSignDate(),
													 filingOption.getFilingYear(),
													 dispatch.getPrintNumber(),
													 dispatch.getPageCount(),
													 bodyFileNames,
													 attachmentFileNames,
													 recordControlService.listVisitors(dispatch.getId(), Dispatch.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY),
													 dispatch.getRemark());
			}
			if(filingConfig.getToDatabank()=='1') { //归档到资料库
				String htmlBody = null;
				if(dispatch.getBodies()!=null && !dispatch.getBodies().isEmpty()) {
					htmlBody = ((DispatchBody)dispatch.getBodies().iterator().next()).getHtmlBody();
				}
				databankDataService.filing(dispatch.getId(),
										   filingConfig.getDirectoryId(),
										   filingOption.getFilingYear(),
										   filingConfig.getCreateDirectoryByYear()=='1',
										   dispatch.getSubject(),
										   htmlBody,
										   dispatch.getSignDate()==null ? null : new Timestamp(dispatch.getSignDate().getTime()),
										   dispatch.getDocWord(),
										   null,
										   dispatch.getSecureLevel(),
										   dispatch.getDocType(),
										   filingPersonId,
										   filingPersonName,
										   bodyFileNames,
										   attachmentFileNames,
										   recordControlService.listVisitors(dispatch.getId(), Dispatch.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY),
										   dispatch.getRemark());
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/**
	 * @return Returns the administrativeArchivesService.
	 */
	public AdministrativeArchivesService getAdministrativeArchivesService() {
		return administrativeArchivesService;
	}
	/**
	 * @param administrativeArchivesService The administrativeArchivesService to set.
	 */
	public void setAdministrativeArchivesService(
			AdministrativeArchivesService administrativeArchivesService) {
		this.administrativeArchivesService = administrativeArchivesService;
	}
	/**
	 * @return Returns the attachmentService.
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}
	/**
	 * @param attachmentService The attachmentService to set.
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	/**
	 * @return Returns the recordControlService.
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}
	/**
	 * @param recordControlService The recordControlService to set.
	 */
	public void setRecordControlService(
			RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the databankDataService
	 */
	public DatabankDataService getDatabankDataService() {
		return databankDataService;
	}

	/**
	 * @param databankDataService the databankDataService to set
	 */
	public void setDatabankDataService(DatabankDataService databankDataService) {
		this.databankDataService = databankDataService;
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
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the soapPassport
	 */
	public SoapPassport getSoapPassport() {
		return soapPassport;
	}

	/**
	 * @param soapPassport the soapPassport to set
	 */
	public void setSoapPassport(SoapPassport soapPassport) {
		this.soapPassport = soapPassport;
	}

	/**
	 * @return the remoteDocumentService
	 */
	public RemoteDocumentService getRemoteDocumentService() {
		return remoteDocumentService;
	}

	/**
	 * @param remoteDocumentService the remoteDocumentService to set
	 */
	public void setRemoteDocumentService(RemoteDocumentService remoteDocumentService) {
		this.remoteDocumentService = remoteDocumentService;
	}
}