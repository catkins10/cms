/*
 * Created on 2005-9-7
 *
 */
package com.yuanluesoft.jeaf.attachmentmanage.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 临时文件管理服务
 * @author linchuan
 *
 */
public interface TemporaryFileManageService {

	/**
	 * 记录临时附件
	 * @param recordClassName
	 * @param applicationName
	 * @param recordId
	 * @param expiresHours 过期小时数
	 * @throws ServiceException
	 */
	public void registTemporaryAttachment(String recordClassName, String applicationName, long recordId, int expiresHours) throws ServiceException;
	
	/**
	 * 删除临时附件记录
	 * @param recordClassName
	 * @param recordId
	 * @throws ServiceException
	 */
	public void unregistTemporaryAttachment(String recordClassName, long recordId) throws ServiceException;
	
	/**
	 * 记录临时文件
	 * @param filePath
	 * @param expiresHours 过期小时数
	 * @throws ServiceException
	 */
	public void registTemporaryFile(String filePath, int expiresHours) throws ServiceException;
	
	/**
	 * 删除临时文件记录
	 * @param filePath
	 * @throws ServiceException
	 */
	public void unregistTemporaryFile(String filePath) throws ServiceException;
	
	/**
	 * 清除临时附件和临时文件,由定时器调用,每天执行一次
	 * @throws ServiceException
	 */
	public void cleanTemporaryFiles() throws ServiceException;
	
	/**
	 * 创建一个临时文件目录,返回目录名称,名称以"/"结束
	 * @param directoryName 指定临时目录名称,如果不指定,则由系统随机生成
	 * @return
	 */
	public String createTemporaryDirectory(String directoryName);
}