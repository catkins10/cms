/*
 * Created on 2006-5-9
 *
 */
package com.yuanluesoft.j2oa.databank.service.spring;

import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.databank.pojo.DatabankData;
import com.yuanluesoft.j2oa.databank.pojo.DatabankDirectory;
import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class DatabankDataServiceImpl extends BusinessServiceImpl implements DatabankDataService {
	private AttachmentService attachmentService; //附件管理服务
    private RecordControlService recordControlService; //记录控制服务
    private DatabankDirectoryService databankDirectoryService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.databank.service.DatabankDataService#filing(long, long, int, boolean, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String, java.util.List, java.util.List, java.util.List, java.lang.String)
	 */
	public void filing(long id, long directoryId, int filingYear, boolean createDirectoryByYear, String subject, String body, Timestamp created, String docmark, String fromUnit, String secureLevel, String type, long filingPersonId, String filingPersonName, List bodyFileNames, List attachmentFileNames, List readers, String remark) throws ServiceException {
		DatabankData data = (DatabankData)getDatabaseService().findRecordById(DatabankData.class.getName(), id);
		if(data!=null) {
			delete(data); //删除旧记录
		}
		data = new DatabankData();
		data.setId(id); //ID
		data.setCreated(DateTimeUtils.now()); //登记时间
		if(createDirectoryByYear) {
			List childDirectories = databankDirectoryService.listChildDirectories(directoryId, null, null, null, false, false, null, 0, 0);
			DatabankDirectory yearDirectory = (DatabankDirectory)ListUtils.findObjectByProperty(childDirectories, "directoryName", "" + filingYear);
			if(yearDirectory==null) {
				yearDirectory = (DatabankDirectory)databankDirectoryService.createDirectory(-1, directoryId, "" + filingYear, "directory", null, filingPersonId, filingPersonName);
			}
			directoryId = yearDirectory.getId();
		}
		data.setDirectoryId(directoryId); //目录ID
		data.setSubject(subject); //标题
		data.setBody(body); //正文
		data.setDocmark(docmark); //文件字号
		data.setFromUnit(fromUnit); //成文单位
		data.setSecureLevel(secureLevel); //密级
		data.setDataType(type); //文件类型
		data.setCreator(filingPersonName); //创建人
		data.setCreatorId(filingPersonId); //创建人ID
		data.setRemark(remark); //附注
		save(data);
	
		//上传正文
		if(bodyFileNames!=null) {
			for(Iterator iterator = bodyFileNames.iterator(); iterator.hasNext();) {
				attachmentService.uploadFile("j2oa/databank", "attachments", null, data.getId(), (String)iterator.next());
			}
		}
		//上传附件
		if(attachmentFileNames!=null) {
			for(Iterator iterator = attachmentFileNames.iterator(); iterator.hasNext();) {
				attachmentService.uploadFile("j2oa/databank", "attachments", null, data.getId(), (String)iterator.next());
			}
		}
		//设置访问者
		if(readers!=null && !readers.isEmpty()) {
			for(Iterator iterator = readers.iterator(); iterator.hasNext();) {
				RecordPrivilege recordPrivilege = (RecordPrivilege)iterator.next();
				recordControlService.appendVisitor(data.getId(), DatabankData.class.getName(), recordPrivilege.getVisitorId(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
		}
	}
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.databank.service.DataService#getDataAccessLevel(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public char getDataAccessLevel(long dataId, SessionInfo sessionInfo) throws ServiceException {
    	DatabankData data = (DatabankData)getDatabaseService().findRecordById(DatabankData.class.getName(), dataId);
        //检查用户对所在目录的访问权
        List popedoms = databankDirectoryService.listDirectoryPopedoms(data.getDirectoryId(), sessionInfo);
        if(popedoms==null) { //无目录访问权限,检查是否有额外授权
            return recordControlService.getAccessLevel(dataId, DatabankData.class.getName(), sessionInfo);
        }
        if(popedoms.contains("manager")) { //目录管理员
        	return RecordControlService.ACCESS_LEVEL_EDITABLE;
        }
        if(popedoms.contains("visitor")) { //目录管理员
        	return RecordControlService.ACCESS_LEVEL_READONLY;
        }
        return RecordControlService.ACCESS_LEVEL_NONE;
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.databank.service.DatabankDataService#importSystemDirectory(java.lang.String, long, long, java.lang.String)
	 */
	public void importSystemDirectory(String systemDirectory, long databankDirectoryId, long importPersonId, String importPersonName) throws ServiceException {
    	if(systemDirectory==null || systemDirectory.equals("")) {
    		return;
    	}
    	systemDirectory = systemDirectory.replaceAll("\\x5c", "/");
    	if(!systemDirectory.endsWith("/")) {
    		systemDirectory += "/";
		}
		File directory = new File(systemDirectory);
		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) { //目录
				//创建子目录
				DatabankDirectory childDirectory = (DatabankDirectory)databankDirectoryService.createDirectory(-1, databankDirectoryId, files[i].getName(), "directory", null, importPersonId, importPersonName);
				importSystemDirectory(systemDirectory + files[i].getName(), childDirectory.getId(), importPersonId, importPersonName);
				continue;
			}
			//文件,创建资料记录
			DatabankData data = new DatabankData();
			data.setId(UUIDLongGenerator.generateId());
			data.setCreated(new Timestamp(files[i].lastModified()));
			data.setDirectoryId(databankDirectoryId);
			data.setDataType(null);
			data.setCreator(importPersonName);
			data.setCreatorId(importPersonId);
			data.setCreated(DateTimeUtils.now());
			String subject = files[i].getName();
			int index = subject.lastIndexOf('.');
			if(index!=-1) {
				subject = subject.substring(0, index);
			}
			data.setSubject(subject);
			getDatabaseService().saveRecord(data);
			//导入附件
			attachmentService.uploadFile("j2oa/databank", "attachments", null, data.getId(), systemDirectory + files[i].getName());
		}
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
	 * @return the databankDirectoryService
	 */
	public DatabankDirectoryService getDatabankDirectoryService() {
		return databankDirectoryService;
	}

	/**
	 * @param databankDirectoryService the databankDirectoryService to set
	 */
	public void setDatabankDirectoryService(
			DatabankDirectoryService databankDirectoryService) {
		this.databankDirectoryService = databankDirectoryService;
	}
}
