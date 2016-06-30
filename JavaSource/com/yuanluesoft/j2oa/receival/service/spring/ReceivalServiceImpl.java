/*
 * Created on 2006-8-25
 *
 */
package com.yuanluesoft.j2oa.receival.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.archives.administrative.services.AdministrativeArchivesService;
import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
import com.yuanluesoft.j2oa.receival.pojo.Receival;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalTemplateConfig;
import com.yuanluesoft.j2oa.receival.service.ReceivalService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 *
 * @author linchuan
 *
 */
public class ReceivalServiceImpl extends BusinessServiceImpl implements ReceivalService {
	private DatabankDataService databankDataService; //资料库服务
	private AdministrativeArchivesService administrativeArchivesService; //文书档案服务
	private AttachmentService attachmentService; //附件服务
	private RecordControlService recordControlService; //记录权限控制服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.service.ReceivalService#filing(com.yuanluesoft.j2oa.receival.pojo.Receival, java.lang.String, com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig, com.yuanluesoft.archives.administrative.model.FilingOption, long, java.lang.String)
	 */
	public void filing(Receival receival, String handling, ReceivalFilingConfig filingConfig, FilingOption filingOption, long filingPersonId, String filingPersonName) throws ServiceException {
		//获取正文文件路径列表
		List bodyFileNames = null;
		List attachments = attachmentService.list("j2oa/receival", "body", receival.getId(), false, 0, null);
		if(attachments!=null) {
			bodyFileNames = new ArrayList();
			for(Iterator iterator = attachments.iterator(); iterator.hasNext();) {
				Attachment attachment = (Attachment)iterator.next();
				bodyFileNames.add(attachment.getFilePath());
			}
		}
		//获取附件文件路径列表
		List attachmentFileNames = new ArrayList();
		attachments = attachmentService.list("j2oa/receival", "attachment", receival.getId(), false, 0, null);
		for(Iterator iterator = attachments==null ? null : attachments.iterator(); iterator!=null && iterator.hasNext();) {
			Attachment attachment = (Attachment)iterator.next();
			attachmentFileNames.add(attachment.getFilePath());
		}
		if(handling!=null && !handling.isEmpty()) {
			String path = temporaryFileManageService.createTemporaryDirectory(null);
			FileUtils.saveStringToFile(path + "办理单.html", handling, "utf-8", true);
			attachmentFileNames.add(path + "办理单.html");
		}
		if(filingConfig.getToArchives()=='1') { //归档到档案系统
			administrativeArchivesService.filing(receival.getSubject(),
												 receival.getKeyword(),
												 "收文",
												 filingOption.getDocCategory(),
												 receival.getDocWord(),
												 filingOption.getResponsibilityPerson(),
												 filingOption.getFondsName(),
												 filingOption.getSecureLevel(),
												 filingOption.getRotentionPeriod(),
												 filingOption.getUnit(),
												 receival.getSignDate(),
												 filingOption.getFilingYear(),
												 receival.getReceivalCount(),
												 receival.getPageCount(),
												 bodyFileNames,
												 attachmentFileNames,
												 recordControlService.listVisitors(receival.getId(), Receival.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY),
												 receival.getRemark());
		}
		if(filingConfig.getToDatabank()=='1') { //归档到资料库
			databankDataService.filing(receival.getId(),
									   filingConfig.getDirectoryId(),
									   filingOption.getFilingYear(),
									   filingConfig.getCreateDirectoryByYear()=='1',
									   receival.getSubject(),
									   receival.getContent(),
									   receival.getSignDate()==null ? null : new Timestamp(receival.getSignDate().getTime()),
									   receival.getDocWord(),
									   receival.getFromUnit(),
									   receival.getSecureLevel(),
									   receival.getDocType(),
									   filingPersonId,
									   filingPersonName,
									   bodyFileNames,
									   attachmentFileNames,
									   recordControlService.listVisitors(receival.getId(), Receival.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY),
									   receival.getRemark());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.service.ReceivalService#getFilingConfig()
	 */
	public ReceivalFilingConfig getFilingConfig() throws ServiceException {
		return (ReceivalFilingConfig)getDatabaseService().findRecordByHql("from ReceivalFilingConfig ReceivalFilingConfig");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.service.ReceivalService#getReceivalTemplateConfig()
	 */
	public ReceivalTemplateConfig getReceivalTemplateConfig() throws ServiceException {
		return (ReceivalTemplateConfig)getDatabaseService().findRecordByHql("from ReceivalTemplateConfig ReceivalTemplateConfig");
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
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
	}
}
