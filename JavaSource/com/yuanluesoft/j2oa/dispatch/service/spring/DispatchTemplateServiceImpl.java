/*
 * Created on 2006-9-17
 *
 */
package com.yuanluesoft.j2oa.dispatch.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.dispatch.pojo.DispatchTemplateConfig;
import com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 *
 * @author linchuan
 *
 */
public class DispatchTemplateServiceImpl implements DispatchTemplateService {
	private FileDownloadService fileDownloadService; //文件下载服务
	private AttachmentService attachmentService; //附件服务
	private DatabaseService databaseService; //数据库访问
	private RemoteDocumentService remoteDocumentService; //远程文档服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService#getWordTemplateById(long)
	 */
	public Attachment getWordTemplateById(long templateId) throws ServiceException {
		List templates = templateId<=0 ? null : attachmentService.list("j2oa/dispatch", "template", templateId, false, 0, null);
		Attachment template = (Attachment)ListUtils.findObjectByProperty(templates, "name", "发文.doc");
		if(template==null) {
			//获取默认模板
			template = new Attachment();
			template.setName("发文.doc");
			template.setFilePath(Environment.getWebinfPath() + "j2oa/dispatch/template/发文.doc");
		}
		return template;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService#getWordTemplate(java.lang.String, java.lang.String)
	 */
	public Attachment getWordTemplate(String docType, String docMark) throws ServiceException {
		//获取匹配的模板
		DispatchTemplateConfig templateConfig = getTemplate(docType, docMark);
		return getWordTemplateById(templateConfig==null ? -1 : templateConfig.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService#saveWordTemplate(com.yuanluesoft.j2oa.dispatch.pojo.DispatchTemplateConfig, javax.servlet.http.HttpServletRequest)
	 */
	public void saveWordTemplate(final DispatchTemplateConfig templateConfig, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新模板
				remoteDocumentService.updateRecordFile(documentPath, templateConfig, "template", null);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService#getHandlingTemplate(java.lang.String, java.lang.String)
	 */
	public String getHandlingTemplate(String docType, String docMark) throws ServiceException {
		//获取匹配的模板
		DispatchTemplateConfig templateConfig = getTemplate(docType, docMark);
		return templateConfig==null ? null : templateConfig.getHandlingTemplate();
	}
	
	/**
	 * 获取模板配置
	 * @param docType
	 * @param docMark
	 * @return
	 * @throws ServiceException
	 */
	private DispatchTemplateConfig getTemplate(String docType, String docMark) throws ServiceException {
		//检查匹配的模板
		List templateConfigs = databaseService.findRecordsByHql("from DispatchTemplateConfig DispatchTemplateConfig");
		for(Iterator iterator = templateConfigs==null ? null : templateConfigs.iterator(); iterator!=null && iterator.hasNext();) {
			DispatchTemplateConfig config = (DispatchTemplateConfig)iterator.next();
			if(("," + config.getDocTypes() + ",").indexOf("," + docType + ",")==-1) {
				continue;
			}
			if(config.getDocWords()!=null && !config.getDocWords().isEmpty() && ("," + config.getDocWords() + ",").indexOf("," + docMark + ",")==-1) {
				continue;
			}
			return config;
		}
		return null;
	}

	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
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
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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
