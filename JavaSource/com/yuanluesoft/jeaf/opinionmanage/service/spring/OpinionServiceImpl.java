/*
 * Created on 2005-9-14
 *
 */
package com.yuanluesoft.jeaf.opinionmanage.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage;
import com.yuanluesoft.jeaf.opinionmanage.pojo.OftenUseOpinion;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.opinionmanage.pojo.OpinionType;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionServiceImpl extends BusinessServiceImpl implements OpinionService {
	private Cache cache; //意见类型缓存

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		clearOpinionTypeCache(record);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		clearOpinionTypeCache(record);
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		clearOpinionTypeCache(record);
	}
	
	/**
	 * 清理意见类型缓存
	 * @param opinionType
	 */
	private void clearOpinionTypeCache(Object opinionType) {
		try {
			OpinionType type = (OpinionType)opinionType;
			cache.remove("opinionType_" + type.getBusinessClassName());
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#listCustomOpinionTypes(java.lang.String, java.lang.String)
	 */
	public List listCustomOpinionTypes(String businessClassName) throws ServiceException {
		try {
			String key = "opinionType_" + businessClassName;
			List opinionTypes = (List)cache.get(key);
			if(opinionTypes==null) {
				String hql = "from OpinionType OpinionType" +
							 " where OpinionType.businessClassName='" + businessClassName + "'" +
							 " order by OpinionType.priority DESC, OpinionType.id";
				opinionTypes = getDatabaseService().findRecordsByHql(hql);
				cache.put(key, opinionTypes==null ? new ArrayList() : opinionTypes);
			}
			return opinionTypes;
		}
		catch(Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#loadDefaultOpinionTypes(java.lang.String)
	 */
	public void loadDefaultOpinionTypes(String businessClassName) throws ServiceException {
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(businessClassName);
		Field opinionsField = (Field)ListUtils.findObjectByProperty(businessObject.getFields(), "name", "opinions");
		String presettingOpinionTypes = (String)opinionsField.getParameter("presettingOpinionTypes"); //获取预设的意见类型
		if(presettingOpinionTypes==null) {
			return;
		}
		//删除当前自定义的意见类型
		getDatabaseService().deleteRecordsByHql("from OpinionType OpinionType where OpinionType.businessClassName='" + JdbcUtils.resetQuot(businessClassName) + "'");
		//加载默认配置
		String[] types = presettingOpinionTypes.split(",");
		for(int i=0; i<types.length; i++) {
			String[] values = types[i].split("\\x7c");
			OpinionType opinionType = new OpinionType();
			opinionType.setId(UUIDLongGenerator.generateId()); //ID
			opinionType.setBusinessClassName(businessClassName); //业务对象类名称
			opinionType.setOpinionType(values[0]); //意见类型
			opinionType.setRequired(values.length>1 && "required".equals(values[1]) ? '1' : '0'); //是否必须填写
			opinionType.setInputPrompt("尚未填写意见"); //没有填写时提示信息,默认:尚未填写意见
			opinionType.setPriority(types.length - i); //优先级
			getDatabaseService().saveRecord(opinionType);
		}
		try {
			cache.remove("opinionType_" + businessClassName);
		}
		catch(Exception e) {
			
		}
	}

	/**
	 * 获取常用意见
	 * @param personId
	 * @param applicationName
	 * @param opinion
	 * @return
	 */
	private OftenUseOpinion findOftenUseOpinion(long personId, String applicationName, String opinion) {
		String hql = "from OftenUseOpinion OftenUseOpinion where OftenUseOpinion.personId=" + personId +  " and OftenUseOpinion.applicationName='" + JdbcUtils.resetQuot(applicationName) + "' and OftenUseOpinion.opinion='" + JdbcUtils.resetQuot(opinion) + "'";
		return (OftenUseOpinion)getDatabaseService().findRecordByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.portal.service.OpinionService#appendOftenUseOpinion(long, java.lang.String, java.lang.String)
	 */
	public void appendOftenUseOpinion(long personId, String applicationName, String opinion) throws ServiceException {
		opinion = opinion.replaceAll("'", "").replaceAll("\"", "").replaceAll("|", "");
		if(findOftenUseOpinion(personId, applicationName, opinion)==null) {
			OftenUseOpinion oftenUseOpinion = new OftenUseOpinion();
			oftenUseOpinion.setId(UUIDLongGenerator.generateId());
			oftenUseOpinion.setPersonId(personId);
			oftenUseOpinion.setApplicationName(applicationName);
			oftenUseOpinion.setOpinion(opinion);
			getDatabaseService().saveRecord(oftenUseOpinion);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.portal.service.OpinionService#deleteOftenUseOpinion(long, java.lang.String, java.lang.String)
	 */
	public void deleteOftenUseOpinion(long personId, String applicationName, String opinion) throws ServiceException {
		opinion = opinion.replaceAll("'", "").replaceAll("\"", "").replaceAll("|", "");
		OftenUseOpinion oftenUseOpinion = findOftenUseOpinion(personId, applicationName, opinion);
		if(oftenUseOpinion!=null) {
			getDatabaseService().deleteRecord(oftenUseOpinion);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#loadOpinion(java.lang.String, long)
	 */
	public Opinion loadOpinion(String pojoClassName, long opinionId) throws ServiceException {
		return (Opinion)getDatabaseService().findRecordById(getOpinionPojoClass(pojoClassName).getName(), opinionId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#saveOpinion(java.lang.String, long, long, java.lang.String, java.lang.String, long, java.lang.String, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public Opinion saveOpinion(String pojoClassName, long opinionId, long mainRecordId, String opinionContent, String opinionType, long participantId, String participantName, long agentId, String agentName, String activityId, String activityName, String workItemId, Timestamp created) throws ServiceException {
		Opinion opinion;
		try {
			opinion = (Opinion)getOpinionPojoClass(pojoClassName).newInstance();
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		opinion.setId(opinionId==0 ? UUIDLongGenerator.generateId() : opinionId); //ID
		opinion.setOpinion(opinionContent); //意见内容
		opinion.setCreated(created==null ? DateTimeUtils.now() : created); //填写时间
		opinion.setMainRecordId(mainRecordId); //主记录ID
		opinion.setOpinionType(opinionType); //意见类型
		opinion.setWorkItemId(workItemId); //工作项ID
		opinion.setActivityId(activityId); //环节ID
		opinion.setActivityName(activityName); //环节名称
		opinion.setPersonId(participantId); //用户ID
		opinion.setPersonName(participantName); //用户名
		opinion.setAgentId(agentId); //代理人ID
		opinion.setAgentName(agentName); //代理人姓名
		//保存或更新意见
    	if(opinionId==0) { //保存意见
    		getDatabaseService().saveRecord(opinion);
    	}
    	else { //更新意见
    		getDatabaseService().updateRecord(opinion);
    	}
    	return opinion;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#deleteOpinion(java.lang.String, long)
	 */
	public void deleteOpinion(String pojoClassName, long opinionId) throws ServiceException {
		getDatabaseService().deleteRecordById(getOpinionPojoClass(pojoClassName).getName(), opinionId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#fillOpinionPackageByActivityId(com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage, java.util.Collection, java.lang.String, java.lang.String, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillOpinionPackageByActivityId(OpinionPackage opinionPackage, Collection opinions, String mainRecordClassName, String activityId, long participantId, SessionInfo sessionInfo) throws ServiceException {
		opinionPackage.setMainRecordClassName(mainRecordClassName); //主记录类名称
		retrieveOpinonModifiable(opinionPackage, mainRecordClassName, sessionInfo); //检查意见是否允许修改
		opinionPackage.setOpinionList(opinions);
	    if(opinionPackage.getOpinion()!=null && !opinionPackage.getOpinion().isEmpty()) {
	        return;
	    }
	    opinionPackage.setOpinionId(0);
		opinionPackage.setOpinion(null);
		if(opinions==null || activityId==null) {
			return;
		}
		for(Iterator iterator = opinions.iterator(); iterator.hasNext();) {
			Opinion opinion = (Opinion)iterator.next();
			try {
				if(activityId.equals(opinion.getActivityId())) {
					if(opinion.getPersonId()==participantId) {
						opinionPackage.setOpinionId(opinion.getId());
						opinionPackage.setOpinion(opinion.getOpinion());
						return;
					}
				}
			}
			catch(Exception e) {
				throw new ServiceException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.opinionmanage.service.OpinionService#fillOpinionPackageByWorkItemId(com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage, java.util.Collection, java.lang.String, java.lang.String, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillOpinionPackageByWorkItemId(OpinionPackage opinionPackage, Collection opinions, String mainRecordClassName, String wordItemId, long participantId, SessionInfo sessionInfo) throws ServiceException {
		opinionPackage.setMainRecordClassName(mainRecordClassName); //主记录类名称
		retrieveOpinonModifiable(opinionPackage, mainRecordClassName, sessionInfo); //检查意见是否允许修改
		opinionPackage.setOpinionList(opinions);
	    if(opinionPackage.getOpinion()!=null && !opinionPackage.getOpinion().equals("")) {
	        return;
	    }
		opinionPackage.setOpinionId(0);
		opinionPackage.setOpinion(null);
		if(opinions==null || wordItemId==null) {
			return;
		}
		for(Iterator iterator = opinions.iterator(); iterator.hasNext();) {
		    Opinion opinion = (Opinion)iterator.next();
			try {
				if(wordItemId.equals(opinion.getWorkItemId())) {
					if(opinion.getPersonId()==participantId) {
						opinionPackage.setOpinionId(opinion.getId());
						opinionPackage.setOpinion(opinion.getOpinion());
						return;
					}
				}
			}
			catch(Exception e) {
			    Logger.exception(e);
				throw new ServiceException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("opinionTypes".equals(itemsName)) {
			List opioionFields = FieldUtils.listRecordFields(request.getParameter("recordClassName"), "opinion", null, null, null, false, false, false, false, 0);
			List opinionTypes = ListUtils.generatePropertyList(opioionFields, "name");
			if(opinionTypes==null) {
				opinionTypes = new ArrayList();
			}
			opinionTypes.add(0, "全部");
			return opinionTypes;
		}
		else if("oftenUseOpinions".equals(itemsName)) { //常用意见列表
			if(!(bean instanceof ActionForm) || ((ActionForm)bean).getFormDefine()==null) {
				return null;
				}
			String hql = "select OftenUseOpinion.opinion" +
						 " from OftenUseOpinion OftenUseOpinion" +
						 " where OftenUseOpinion.personId=" + sessionInfo.getUserId() +
						 " and OftenUseOpinion.applicationName='" + JdbcUtils.resetQuot(((ActionForm)(bean)).getFormDefine().getApplicationName()) + "'" +
						 " order by OftenUseOpinion.opinion";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * 检查意见是否允许修改
	 * @param opinionPackage
	 * @param mainRecordClassName
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void retrieveOpinonModifiable(OpinionPackage opinionPackage, String mainRecordClassName, SessionInfo sessionInfo) throws ServiceException {
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(mainRecordClassName);
		if(businessObject==null) {
			return;
		}
		List acl = ((AccessControlService)Environment.getService("accessControlService")).getAcl(businessObject.getApplicationName(), sessionInfo);
		opinionPackage.setModifiable(acl!=null && acl.contains(AccessControlService.ACL_APPLICATION_MANAGER));
	}
	
	/**
	 * 获取意见类
	 * @param pojoClassName
	 * @return
	 */
	private Class getOpinionPojoClass(String pojoClassName) {
		for(int i=0; i<5; i++) {
			try {
				String className = pojoClassName.endsWith("Opinion") ? pojoClassName : pojoClassName + "Opinion";
				Class opinionClass = Class.forName(className); //检查类是否存在
				if(Opinion.class.isAssignableFrom(opinionClass)) {
					return opinionClass;
				}
			}
			catch (ClassNotFoundException e) {
				
			}
			try {
				pojoClassName = Class.forName(pojoClassName).getSuperclass().getName(); //查找父类对应的权限POJO
			}
			catch (ClassNotFoundException e) {
				
			}
		}
		return null;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
}