package com.yuanluesoft.land.landcertificate.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 土地证服务
 * @author linchuan
 *
 */
public interface LandCertificateService {

	/**
	 * 从文件导入收件记录
	 * @param uploadFiles
	 * @param siteId
	 * @throws ServiceException
	 */
	public void importCases(List uploadFiles) throws ServiceException;
}