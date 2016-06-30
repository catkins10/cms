/*
 * Created on 2005-11-23
 *
 */
package com.yuanluesoft.jeaf.security.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface RecordControlService {
	public final char ACCESS_LEVEL_NONE = 0; //无访问权限
	public final char ACCESS_LEVEL_PREREAD = '1'; //预读
	public final char ACCESS_LEVEL_READONLY = '2'; //有读取权限
	public final char ACCESS_LEVEL_EDITABLE = '3'; //有读取和修改权限
	
	/**
	 * 获取访问者列表
	 * @param recordId
	 * @param pojoClassName
	 * @param accessLevel
	 * @return
	 * @throws ServiceException
	 */
	public List listVisitors(long recordId, String pojoClassName, char accessLevel) throws ServiceException;
	
	/**
	 * 获取访问者(Person)列表
	 * @param recordId
	 * @param pojoClassName
	 * @param accessLevel
	 * @return
	 * @throws ServiceException
	 */
	public List listVisitorPersons(long recordId, String pojoClassName, char accessLevel) throws ServiceException;
	
	/**
	 * 获取访问者信息
	 * @param recordId
	 * @param pojoClassName
	 * @param accessLevel
	 * @return
	 * @throws ServiceException
	 */
	public RecordVisitorList getVisitors(long recordId, String pojoClassName, char accessLevel) throws ServiceException;
	
	/**
	 * 获取记录的访问权限
	 * @param recordId
	 * @param pojoClassName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public abstract char getAccessLevel(long recordId, String pojoClassName, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 检查用户是否是指定级别的访问者
	 * @param recordId
	 * @param pojoClassName
	 * @param accessLevel
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public abstract boolean isVisitor(long recordId, String pojoClassName, char accessLevel, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 增加记录访问者,如果之前相同的访问者记录已经存在返回null
	 * @param recordId
	 * @param pojoClassName
	 * @param visitorId
	 * @param accessLevel
	 * @return
	 * @throws ServiceException
	 */
	public RecordPrivilege appendVisitor(long recordId, String pojoClassName, long visitorId, char accessLevel) throws ServiceException;
	
	/**
	 * 删除访问者
	 * @param recordId
	 * @param pojoClassName
	 * @param visitorId
	 * @param accessLevel
	 * @throws ServiceException
	 */
	public void removeVisitor(long recordId, String pojoClassName, long visitorId, char accessLevel) throws ServiceException;
	
	/**
	 * 删除记录访问者
	 * @param record
	 * @param accessLevel
	 * @throws ServiceException
	 */
	public void removeVisitors(long recordId, String pojoClassName, char accessLevel) throws ServiceException;
	
	/**
	 * 根据访问者ID和类型列表生成访问者列表并更新
	 * @param visitorIds
	 * @param mainRecord
	 * @param accessLevel
	 * @throws ServiceException
	 */
	public abstract List updateVisitors(long recordId, String pojoClassName, RecordVisitorList recordVisitors, char accessLevel) throws ServiceException;
	
	/**
	 * 拷贝访问者列表,返回新增的访问者列表
	 * @param visitors
	 * @param mainRecord
	 * @param accessLevel
	 * @throws ServiceException
	 */
	public abstract List copyVisitors(long fromRecordId, long toRecordId, char fromAccessLevel, char toAccessLevel, String pojoClassName) throws ServiceException;
	
	/**
	 * 记录用户对记录的访问权限
	 * @param recordId
	 * @param session
	 * @throws ServiceException
	 */
	public void registRecordAccessLevel(long recordId, char recordAccessLevel, HttpSession session) throws ServiceException;
	
	/**
	 * 删除用户对记录的访问权限记录
	 * @param recordId
	 * @param session
	 * @throws ServiceException
	 */
	public void unregistRecordAccessLevel(long recordId, HttpSession session) throws ServiceException;
	
	/**
	 * 获取用户对记录的访问权限
	 * @param recordId
	 * @param session
	 * @return
	 * @throws ServiceException
	 */
	public char getRegistedRecordAccessLevel(long recordId, HttpSession session) throws ServiceException;
}