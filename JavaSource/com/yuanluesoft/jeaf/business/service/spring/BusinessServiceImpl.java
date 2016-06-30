/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.business.service.spring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.LazyBody;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;


/**
 * 
 * @author linchuan
 *
 */
public class BusinessServiceImpl implements BusinessService {
	private DatabaseService databaseService; //spring容器加载
	private BusinessDefineService businessDefineService; //业务逻辑定义服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		return databaseService.findRecordById(recordClass.getName(), id, listLazyLoadProperties(recordClass));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException  {
		databaseService.saveRecord(record);
		saveLzayBody(record, true); //保存延迟加载的正文
		return record;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException  {
		databaseService.updateRecord(record);
		saveLzayBody(record, false); //保存延迟加载的正文
		return record;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		databaseService.deleteRecord(record);
		//删除附件
		deleteAttachments(record.getClass(), record.getId());
	}
	
	/**
	 * 获取需要延迟加载的属性列表
	 * @param recordClass
	 * @return
	 * @throws ServiceException
	 */
	protected List listLazyLoadProperties(Class recordClass) throws ServiceException {
		List lazyLoadProperties = new ArrayList();
		//加载业务对象
		BusinessObject businessObject = businessDefineService.getBusinessObject(recordClass);
		if(businessObject!=null && businessObject.getFields()!=null) {
			for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
				Field field = (Field)iterator.next();
				if("components".equals(field.getType()) && field.isPersistence() && !"false".equals(field.getParameter("lazyLoad"))) {
					lazyLoadProperties.add(field.getName());
				}
			}
		}
		return lazyLoadProperties.isEmpty() ? null : lazyLoadProperties;
	}
	
	/**
	 * 删除附件
	 * @param pojo
	 * @throws ServiceException
	 */
	private void deleteAttachments(Class pojoClass, long id) throws ServiceException {
		BusinessObject businessObject = businessDefineService.getBusinessObject(pojoClass);
		for(Iterator iteratorField = businessObject.getFields().iterator(); iteratorField.hasNext();) {
			Field fieldDefine = (Field)iteratorField.next();
			if(!"attachment".equals(fieldDefine.getType()) && !"image".equals(fieldDefine.getType()) && !"video".equals(fieldDefine.getType())) {
				continue; //不是附件、图片和视频
			}
			String serviceName = (String)fieldDefine.getParameter("serviceName");
			AttachmentService attachmentService = (AttachmentService)Environment.getService(serviceName==null || serviceName.equals("") ? fieldDefine.getType() + "Service" : serviceName);
			attachmentService.deleteAll(businessObject.getApplicationName(), null, id); //删除所有类型的附件
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#synchUpdate(java.lang.Object)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		Record record = (Record)object;
		//获取旧记录
		Record oldRecord = load(object.getClass(), record.getId());
		
		//保存主记录
		if(oldRecord==null) { //旧记录不存在
			save(record); //保存新的记录
		}
		else { //旧记录已经存在
			update(record); //更新旧记录
		}
		
		//保存组成部分
		BusinessObject businessObject = businessDefineService.getBusinessObject(object.getClass()); //获取业务逻辑定义
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!"components".equals(field.getType()) ||
			   !field.isPersistence() ||
			   "false".equals(field.getParameter("lazyLoad")) ||
			   "lazyBody".equals(field.getName())) {
				continue;
			}
			//获取新记录的组成部分
			Collection components = null;
			try {
				components = (Collection)PropertyUtils.getProperty(object, field.getName());
			}
			catch(Exception e) {
				Logger.error("field name is " + field.getName());
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
			
			Collection oldComponents = null;
			//删除不在新的组成部分列表中的数据
			if(oldRecord!=null) { //旧记录存在
				try {
					oldComponents = (Collection)PropertyUtils.getProperty(oldRecord, field.getName());
				}
				catch(Exception e) {
					Logger.error("field name is " + field.getName());
					Logger.exception(e);
					throw new ServiceException(e.getMessage());
				}
				for(Iterator iteratorComponent = oldComponents==null ? null : oldComponents.iterator(); iteratorComponent!=null && iteratorComponent.hasNext();) {
					Record component = (Record)iteratorComponent.next();
					if(ListUtils.findObjectByProperty(components, "id", new Long(component.getId()))!=null) { //在新的组成部分列表中
						continue;
					}
					//删除不在新的组成部分列表中的数据
					try {
						delete(component); //使用业务逻辑服务删除组成部分
					}
					catch (Exception e) {
						try {
							databaseService.deleteRecord(component); //使用DAO删除
						}
						catch (Exception ex) {
							
						}
					}
				}
			}
			//保存新增加的组成部分
			for(Iterator iteratorComponent = components==null ? null : components.iterator(); iteratorComponent!=null && iteratorComponent.hasNext();) {
				Record component = (Record)iteratorComponent.next();
				boolean isNew = (ListUtils.findObjectByProperty(oldComponents, "id", new Long(component.getId()))==null); //判断是否新记录
				try {
					if(isNew) {
						save(component); //使用业务逻辑服务保存组成部分
					}
					else {
						update(component); //使用业务逻辑服务更新组成部分
					}
				}
				catch (Exception e) {
					try {
						if(isNew) {
							databaseService.saveRecord(component); //使用DAO保存
						}
						else {
							databaseService.updateRecord(component); //使用DAO更新
						}
					}
					catch (Exception ex) {
						
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#synchDelete(java.lang.Object)
	 */
	public void synchDelete(Object object, String senderName) throws ServiceException {
		try {
			Record record = (Record)object;
			record = load(object.getClass(), record.getId()); //获取旧记录
			delete(record); //删除记录
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#processExchangedObject(java.lang.Object, java.lang.String)
	 */
	public Serializable processExchangedObject(Object object, String senderName) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#validate(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("attachments".equals(itemsName)) {
			List attachments = null;
			try {
				attachments = (List)PropertyUtils.getProperty(bean, "attachmentSelector.attachments");
			}
			catch(Exception e) {
				
			}
			if(attachments==null || attachments.isEmpty()) {
				return null;
			}
			List items = new ArrayList();
			for(int i = 0; i < attachments.size(); i++) {
				Attachment attachment = (Attachment)attachments.get(i);
				items.add(new String[]{attachment.getTitle(), attachment.getName()});
			}
			return items;
		}
		return null;
	}
	
	/**
	 * 保存延迟加载的正文
	 * @param object
	 */
	private void saveLzayBody(Record record, boolean isNew) {
		try {
			Set lazyBody = (Set)PropertyUtils.getProperty(record, "lazyBody");
			if(lazyBody!=null && !lazyBody.isEmpty()) {
				LazyBody body = (LazyBody)lazyBody.iterator().next();
				body.setId(record.getId());
				if(isNew) { //新记录
					getDatabaseService().saveRecord(body);
				}
				else {
					try {
						getDatabaseService().updateRecord(body);
					}
					catch(Exception e) {
						getDatabaseService().saveRecord(body);
					}
				}
			}
		}
		catch(Exception e) {
			
		}
	}

	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
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