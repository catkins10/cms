/*
 * Created on 2006-9-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.services;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface ArchivesCodeService extends BusinessService {
	
	/**
	 * 生成档号
	 * @param archivesType 档案类别
	 * @param fondsCode 全宗号
	 * @param filingYear 归档年度
	 * @param rotentionPeriod 保管期限
	 * @param archivesUnit 机构或问题
	 * @param serialNumber 件号,当件号为零时,自动生成新件号
	 * @return
	 * @throws ServiceException
	 */
	public String generateArchivesCode(String archivesType, String fondsCode, int filingYear, String rotentionPeriod, String archivesUnit, int[] serialNumber) throws ServiceException; 
}
