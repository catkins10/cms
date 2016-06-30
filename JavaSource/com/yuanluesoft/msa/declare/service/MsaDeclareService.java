package com.yuanluesoft.msa.declare.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.msa.declare.model.Bulletin;

/**
 * 电子申报服务
 * 从福建海事业务申报网(http://61.154.10.90:90)获取今日申报、通知公告
 * @author linchuan
 *
 */
public interface MsaDeclareService {
	
	/**
	 * 获取申报信息
	 * @param pageContent
	 * @return
	 * @throws ServiceException
	 */
	public List listDeclares() throws ServiceException;
	
	/**
	 * 获取通知公告列表
	 * @return
	 * @throws ServiceException
	 */
	public List listBulletins() throws ServiceException;
	
	/**
	 * 获取通知公告
	 * @param sourceUrl
	 * @return
	 */
	public Bulletin getBulletin(String sourceUrl) throws ServiceException ;	
}