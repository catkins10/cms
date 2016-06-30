/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.business.service;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;


/**
 * 
 * @author linchuan
 *
 */
public interface BusinessService {
	
	/**
	 * 加载数据
	 * @param recordClass
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public Record load(Class recordClass, long id) throws ServiceException;

	/**
	 * 保存
	 * @param record
	 * @return
	 */
	public Record save(Record record) throws ServiceException;
	
	/**
	 * 更新数据
	 * @param record
	 * @return
	 * @throws ServiceException
	 */
	public Record update(Record record) throws ServiceException;
	
	/**
	 * 删除数据
	 * @param record
	 * @throws ServiceException
	 */
	public void delete(Record record) throws ServiceException;
	
	/**
	 * 同步更新数据,由数据交换服务调用
	 * @param object
	 * @param senderName
	 * @throws ServiceException
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException;
	
	/**
	 * 同步删除数据,由数据交换服务调用
	 * @param object
	 * @param senderName
	 * @throws ServiceException
	 */
	public void synchDelete(Object object, String senderName) throws ServiceException;
	
	/**
	 * 处理接收到的远程对象
	 * @param object
	 * @param senderName
	 * @return
	 * @throws ServiceException
	 */
	public Serializable processExchangedObject(Object object, String senderName) throws ServiceException;
	
	/**
	 * 业务逻辑校验
	 * @param record
	 * @param isNew
	 * @return
	 * @throws ServiceException
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException;
	
	/**
	 * 数据完整性校验
	 * @param record
	 * @param isNew
	 * @return
	 * @throws ServiceException
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException;
	
	/**
	 * 获取选择条目(Object[],Object[0]是条目名称,Object[1]是条目值)列表
	 * @param itemsName
	 * @param bean
	 * @param fieldDefine
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
}