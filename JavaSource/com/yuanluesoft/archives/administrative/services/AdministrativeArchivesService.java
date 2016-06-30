/*
 * Created on 2006-9-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.services;

import java.util.Date;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 文书档案服务
 * @author linchuan
 * 
 */
public interface AdministrativeArchivesService {
	
	/**
	 * 归档
	 * @param subject
	 * @param keyword
	 * @param type
	 * @param docCategory
	 * @param docWord
	 * @param responsibilityPerson
	 * @param fondsName
	 * @param secureLevel
	 * @param rotentionPeriod
	 * @param unit
	 * @param signDate
	 * @param filingYear
	 * @param count
	 * @param pageCount
	 * @param bodyFileNames 正文文件名列表,当使用远程归档时(soap/rmi)需要上传正文文件,TODO:基于axis的文件上传
	 * @param attachmentFileNames 附件文件名列表,当使用远程归档时(soap/rmi)需要上传附件文件
	 * @param readers
	 * @param remark
	 * @throws ServiceException
	 */
	public void filing(String subject,
					   String keyword,
					   String type,
					   //String categoryId, //TODO
					   String docCategory, 
					   String docWord,
					   String responsibilityPerson,
					   String fondsName,
					   String secureLevel,
					   String rotentionPeriod,
					   String unit,
					   Date signDate,
					   int filingYear,
					   int count,
					   int pageCount,
					   List bodyFileNames,
					   List attachmentFileNames,
					   List readers,
					   String remark) throws ServiceException;
}