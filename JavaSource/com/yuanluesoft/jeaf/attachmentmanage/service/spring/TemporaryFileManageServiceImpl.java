package com.yuanluesoft.jeaf.attachmentmanage.service.spring;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.attachmentmanage.pojo.TemporaryFile;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class TemporaryFileManageServiceImpl implements TemporaryFileManageService {
	private String temporaryDirectory; //临时文件目录
	private DatabaseService databaseService; //数据库服务
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService#registTemporaryAttachment(java.lang.String, java.lang.String, long, int)
	 */
	public void registTemporaryAttachment(String recordClassName, String applicationName, long recordId, int expiresHours) throws ServiceException {
		registTemporaryFile(getAttachmentSavePath(recordClassName, applicationName, recordId), expiresHours);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService#unregistTemporaryAttachment(java.lang.String, long)
	 */
	public void unregistTemporaryAttachment(String recordClassName, long recordId) throws ServiceException {
		if(recordClassName!=null) {
			unregistTemporaryFile(getAttachmentSavePath(recordClassName, null, recordId));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService#registTemporaryFile(java.lang.String, int)
	 */
	public void registTemporaryFile(String filePath, int expiresHours) throws ServiceException {
		if(expiresHours<=0) {
			return;
		}
		try {
			TemporaryFile temporaryFile = new TemporaryFile();
			temporaryFile.setId(UUIDLongGenerator.generateId()); //ID
			temporaryFile.setFilePath(new File(filePath).getPath().replace('\\', '/')); //文件路径
			temporaryFile.setExpires(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, expiresHours)); //过期时间
			databaseService.saveRecord(temporaryFile);
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService#unregistTemporaryFile(java.lang.String)
	 */
	public void unregistTemporaryFile(String filePath) throws ServiceException {
		try {
			String hql = "from TemporaryFile TemporaryFile" +
						 " where TemporaryFile.filePath='" + new File(filePath).getPath().replace('\\', '/') + "'";
			databaseService.deleteRecordsByHql(hql);
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentRegistService#cleanTemporaryAttachment()
	 */
	public void cleanTemporaryFiles() throws ServiceException {
		Logger.info("*************** clean temporary files ***************");
		//清除临时文件目录,删除3小时前创建的文件,以避免删除在用文件
		Timestamp fileTime = DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -3);
		FileUtils.deleteFilesInDirectory(temporaryDirectory, fileTime);
		
		//清除过期的临时附件
		String hql = "from TemporaryFile TemporaryFile" +
					 " where TemporaryFile.expires<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")";
		for(int i=0; i<100; i++) {
			List temporaryAttachments = databaseService.findRecordsByHql(hql, 0, 100);
			if(temporaryAttachments==null || temporaryAttachments.isEmpty()) {
				break;
			}
			for(Iterator iterator = temporaryAttachments.iterator(); iterator.hasNext();) {
				TemporaryFile temporaryFile = (TemporaryFile)iterator.next();
				try {
					databaseService.deleteRecord(temporaryFile);
					File file = new File(temporaryFile.getFilePath());
					if(file.isDirectory()) {
						FileUtils.deleteDirectory(temporaryFile.getFilePath());
					}
					else {
						FileUtils.deleteFile(temporaryFile.getFilePath());
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService#createTemporaryDirectory()
	 */
	public String createTemporaryDirectory(String directoryName) {
		String path = FileUtils.createDirectory(temporaryDirectory + (directoryName==null ?  UUIDLongGenerator.generateId() + "" : directoryName));
		return new File(path).getAbsolutePath().replace('\\', '/') + "/";
	}
	
	/**
	 * 获取附件保存路径
	 * @param recordClassName
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	private String getAttachmentSavePath(String recordClassName, String applicationName, long recordId) throws ServiceException {
		String attachmentServiceName = null;
		if(recordClassName!=null) {
			BusinessObject businessObject = businessDefineService.getBusinessObject(recordClassName);
			if(applicationName==null) {
				applicationName = businessObject.getApplicationName();
			}
			for(Iterator iteratorField = businessObject.getFields().iterator(); iteratorField.hasNext();) {
				Field fieldDefine = (Field)iteratorField.next();
				if("attachment".equals(fieldDefine.getType()) && !"image".equals(fieldDefine.getType()) && !"video".equals(fieldDefine.getType())) {
					attachmentServiceName = (String)fieldDefine.getParameter("serviceName");
					break;
				}
			}
		}
		AttachmentService attachmentService = (AttachmentService)Environment.getService(attachmentServiceName==null || attachmentServiceName.equals("") ? "attachmentService" : attachmentServiceName);
		return attachmentService.getSavePath(applicationName, null, recordId, false);
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the temporaryDirectory
	 */
	public String getTemporaryDirectory() {
		return temporaryDirectory;
	}

	/**
	 * @param temporaryDirectory the temporaryDirectory to set
	 */
	public void setTemporaryDirectory(String temporaryDirectory) {
		try {
			FileUtils.createDirectory(temporaryDirectory);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		this.temporaryDirectory = temporaryDirectory;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}
}