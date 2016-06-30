package com.yuanluesoft.j2oa.infocontribute.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.soap.SoapService;

/**
 * 信息投稿服务
 * @author linchuan
 *
 */
public interface InfoContributeService extends BusinessService, SoapService {

	/**
	 * 添加或者更新刊物定义
	 * @param id
	 * @param name
	 * @param unitName
	 * @throws ServiceException
	 */
	public void addOrUpdateMagazineDefine(long id, String name, String unitName) throws ServiceException;
	
	/** 删除刊物定义
	 * @param id
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteMagazineDefine(long id) throws ServiceException;
	
	/**
	 * 获取投稿列表
	 * @param unitName
	 * @param beginTime
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listContributeInfos(String unitName, Timestamp beginTime, int offset, int limit) throws ServiceException;
	
	/**
	 * 退改稿
	 * @param reviseId
	 * @param infoId
	 * @param reviseOpinion
	 * @param revisePersonId
	 * @param revisePersonName
	 * @param revisePersonTel
	 * @throws ServiceException
	 */
	public void reviseInfo(long reviseId, long infoId, String reviseOpinion, long revisePersonId, String revisePersonName, String revisePersonTel) throws ServiceException;

	/**
	 * 添加采用情况
	 * @param useId
	 * @param infoIds
	 * @param level
	 * @param sendTime
	 * @param useTime
	 * @param magazineId
	 * @param magazine
	 * @throws ServiceException
	 */
	public void addUsage(long useId, String infoIds, int level, Timestamp sendTime, Timestamp useTime, long magazineId, String magazine) throws ServiceException;
	
	/**
	 * 删除采用情况
	 * @param useId
	 * @throws ServiceException
	 */
	public void deleteUsage(long useId) throws ServiceException;
	
	/**
	 * 添加领导批示
	 * @param instructId
	 * @param infoIds
	 * @param leader
	 * @param level
	 * @param instruct
	 * @param instructTime
	 * @param creatorId
	 * @param creator
	 * @param created
	 * @throws ServiceException
	 */
	public void addInstruct(long instructId, String infoIds, String leader, int level, String instruct, Timestamp instructTime, long creatorId, String creator, Timestamp created) throws ServiceException;
	
	/**
	 * 删除领导批示
	 * @param instructId
	 * @throws ServiceException
	 */
	public void deleteInstruct(long instructId) throws ServiceException;
	
	/**
	 * 添加刊物
	 * @param magazineId
	 * @param name
	 * @param issueTime
	 * @param sn
	 * @param snTotal
	 * @param level
	 * @param useInfoIds
	 * @param visitorIds
	 * @throws ServiceException
	 */
	public void addMagazine(long magazineId, String name, Timestamp issueTime, long sn, long snTotal, int level, String useInfoIds, String visitorIds) throws ServiceException;
	
	/**
	 * 删除刊物
	 * @param magazineId
	 * @throws ServiceException
	 */
	public void deleteMagazine(long magazineId) throws ServiceException;
	
	/**
	 * 添加补录信息
	 * @param infoId
	 * @param subject
	 * @param keywords
	 * @param magazineName
	 * @param magazineSN
	 * @param magazineLevel
	 * @param fromUnit
	 * @param fromUnitId
	 * @param body
	 * @param secretLevel
	 * @param supplementTime
	 * @throws ServiceException
	 */
	public void addSupplementInfo(long infoId, String subject, String keywords, String magazineName, int magazineSN, int magazineLevel, String fromUnit, long fromUnitId, String body, String secretLevel, Timestamp supplementTime) throws ServiceException;

	/**
	 * 删除补录信息
	 * @param infoId
	 * @throws ServiceException
	 */
	public void deleteSupplementInfo(long infoId) throws ServiceException;
	
	/**
	 * 更新信息接收单位
	 * @param info
	 * @param receiveUnitNames
	 * @throws ServiceException
	 */
	public void updateInfoReceiveUnits(InfoContribute info, String[] receiveUnitNames) throws ServiceException;
	
	/**
	 * 获取接收单位列表
	 * @return
	 */
	public String getReceiveUnits();
}