/*
 * Created on 2006-5-9
 *
 */
package com.yuanluesoft.j2oa.databank.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface DatabankDataService extends BusinessService {
	
    /**
     * 获取资料的访问级别
     * @param dataId
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public char getDataAccessLevel(long dataId, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 归档到资料库
     * @param id
     * @param directoryId
     * @param filingYear 归档年度
     * @param createDirectoryByYear 按年创建目录
     * @param subject
     * @param body
     * @param created
     * @param docmark
     * @param fromUnit
     * @param secureLevel
     * @param type
     * @param registPersonId
 	 * @param bodyFileNames 正文文件名列表,当使用远程归档时(soap/rmi)需要上传正文文件,TODO:基于axis的文件上传
	 * @param attachmentFileNames 附件文件名列表,当使用远程归档时(soap/rmi)需要上传附件文件
     * @param remark
     * @throws ServiceException
     */
    public void filing(long id,
		    		   long directoryId,
					   int filingYear,
					   boolean createDirectoryByYear,
					   String subject,
					   String body,
					   Timestamp created,
					   String docmark,
					   String fromUnit,
					   String secureLevel,
					   String type,
					   long filingPersonId,
					   String filingPersonName,
					   List bodyFileNames,
					   List attachmentFileNames,
					   List readers,
					   String remark) throws ServiceException;
    
    /**
     * 从系统目录导入资料
     * @param systemDirectory
     * @param databankDirectoryId
     * @param importPersonId
     * @param importPersonName
     * @throws ServiceException
     */
    public void importSystemDirectory(String systemDirectory, long databankDirectoryId, long importPersonId, String importPersonName) throws ServiceException;
}
