package com.yuanluesoft.cms.monitor.service.spring;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.monitor.pojo.MonitorCaptureLog;
import com.yuanluesoft.cms.monitor.pojo.MonitorContentParameter;
import com.yuanluesoft.cms.monitor.pojo.MonitorParameter;
import com.yuanluesoft.cms.monitor.pojo.MonitorRecord;
import com.yuanluesoft.cms.monitor.pojo.MonitorUnitConfig;
import com.yuanluesoft.cms.monitor.pojo.MonitorUnitSql;
import com.yuanluesoft.cms.monitor.service.MonitorService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.timetable.services.TimetableService;
import com.yuanluesoft.jeaf.timetable.services.TimetableServiceListener;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorServiceImpl extends BusinessServiceImpl implements MonitorService, TimetableServiceListener {
	//监察采集对象类名称
	private String captureContentClasses = "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPublicInfo," +
										   "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPartyInfo," +
										   "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPublicInfoOpinion," +
										   "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPublicInfoRequest," +
										   "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPublicInfoReport," +
										   "com.yuanluesoft.cms.monitor.infopublic.pojo.MonitorPublicInfoRule," +
										   "com.yuanluesoft.cms.monitor.sms.pojo.MonitorSmsActivate," + 
										   "com.yuanluesoft.cms.monitor.sms.pojo.MonitorSmsSend," +
										   "com.yuanluesoft.cms.monitor.sms.pojo.MonitorSmsReceive";
	private CryptService cryptService; //加密服务
	private OrgService orgService; //组织机构服务
	private TimetableService timetableService; //作息时间服务

	/**
	 * 初始化
	 *
	 */
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("monitor", "监察", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof MonitorUnitConfig) {
			MonitorUnitConfig unitConfig = (MonitorUnitConfig)record;
			if(unitConfig.getDbPassword()!=null && !unitConfig.getDbPassword().isEmpty()) {
				unitConfig.setDbPassword("{" + unitConfig.getDbPassword() + "}");
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof MonitorUnitConfig) {
			MonitorUnitConfig unitConfig = (MonitorUnitConfig)record;
			unitConfig.setDbPassword(encryptDbPassword(unitConfig.getId(), unitConfig.getDbPassword())); //加密口令
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof MonitorUnitConfig) {
			MonitorUnitConfig unitConfig = (MonitorUnitConfig)record;
			unitConfig.setDbPassword(encryptDbPassword(unitConfig.getId(), unitConfig.getDbPassword())); //加密口令
		}
		else if(record instanceof MonitorRecord) {
			MonitorRecord monitorRecord = (MonitorRecord)record;
			//重新计算办理时间
			try {
				countProcessDays(monitorRecord, getBusinessDefineService().getBusinessObject(monitorRecord.getClass()));
			}
			catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.monitor.service.MonitorService#loadMonitorParameter(long)
	 */
	public MonitorParameter loadMonitorParameter(long id) throws ServiceException {
		MonitorParameter monitorParameter = (MonitorParameter)load(MonitorParameter.class, id);
		if(monitorParameter==null) {
			monitorParameter = new MonitorParameter();
			monitorParameter.setId(UUIDLongGenerator.generateId());
		}
		Set contentParameters = new LinkedHashSet();
		List contentBusinessObjects = listContentBusinessObjects();
		for(Iterator iterator = contentBusinessObjects==null ? null : contentBusinessObjects.iterator(); iterator!=null && iterator.hasNext();) {
			BusinessObject businessObject = (BusinessObject)iterator.next();
			MonitorContentParameter monitorContentParameter = (MonitorContentParameter)ListUtils.findObjectByProperty(monitorParameter.getContentParameters(), "contentClassName", businessObject.getClassName());
			if(monitorContentParameter==null) {
				monitorContentParameter = new MonitorContentParameter();
				monitorContentParameter.setContentClassName(businessObject.getClassName());
			}
			monitorContentParameter.setContentName(businessObject.getTitle());
			monitorContentParameter.setTimeoutSupport(businessObject.getFieldByParameter("monitorEnd", "true")!=null); //是否支持超时监察
			contentParameters.add(monitorContentParameter);
		}
		monitorParameter.setContentParameters(contentParameters);
		return monitorParameter;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.monitor.service.MonitorService#saveMonitorParameter(long, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public MonitorParameter saveMonitorParameter(long orgId, String orgName, HttpServletRequest request) throws ServiceException {
		MonitorParameter monitorParameter = (MonitorParameter)getDatabaseService().findRecordByHql("from MonitorParameter MonitorParameter where MonitorParameter.orgId=" + orgId, ListUtils.generateList("contentParameters"));
		boolean isNew = (monitorParameter==null);
		if(!isNew) {
			//删除原来的内容配置配置
			for(Iterator iterator = monitorParameter.getContentParameters()==null ? null : monitorParameter.getContentParameters().iterator(); iterator!=null && iterator.hasNext();) {
				MonitorContentParameter monitorContentParameter = (MonitorContentParameter)iterator.next();
				getDatabaseService().deleteRecord(monitorContentParameter);
			}
		}
		else {
			monitorParameter = new MonitorParameter();
			monitorParameter.setId(UUIDLongGenerator.generateId());
			monitorParameter.setOrgId(orgId);
			monitorParameter.setOrgName(orgName);
			getDatabaseService().saveRecord(monitorParameter);
		}
		//保存采集内容配置
		String[] timeoutValues = request.getParameterValues("timeout");
		List contentBusinessObjects = listContentBusinessObjects();
		for(int i=0; i<(contentBusinessObjects==null ? 0 : contentBusinessObjects.size()); i++) {
			BusinessObject businessObject = (BusinessObject)contentBusinessObjects.get(i);
			MonitorContentParameter monitorContentParameter = new MonitorContentParameter();
			monitorContentParameter.setId(UUIDLongGenerator.generateId()); //ID
			monitorContentParameter.setParameterId(monitorParameter.getId()); //参数配置ID
			monitorContentParameter.setContentClassName(businessObject.getClassName()); //采集对象类名称
			monitorContentParameter.setTimeout(timeoutValues[i]); //超时参数
			getDatabaseService().saveRecord(monitorContentParameter);
		}
		return monitorParameter;
	}

	/**
	 * 加密用户口令
	 * @param person
	 * @throws ServiceException
	 */
	private String encryptDbPassword(long unitConfigId, String password) throws ServiceException {
		if(password==null || password.isEmpty()) {
			return null;
		}
		if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
		//加密口令
		return cryptService.encrypt(password, "" + unitConfigId, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.monitor.service.MonitorService#processCaptureRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processCaptureRequest(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException {
		if("POST".equalsIgnoreCase(request.getMethod())) {
			try {
				saveCaptureData(request, response);
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
		}
		else if("retrieveUnitConfigs".equals(request.getParameter("action"))) { //获取单位配置
			List unitConfigs = listUnitConfigs(request.getRemoteAddr(), RequestUtils.getParameterLongValue(request, "unitId"));
	        if(unitConfigs!=null) {
	        	response.getOutputStream().write(ObjectSerializer.serialize(new ArrayList(unitConfigs)));
	        }
	        else {
	        	Logger.info("MonitorService: unit not found, remote address is " + request.getRemoteAddr());
	        }
		}
	}

	/**
	 * 获取单位配置
	 * @param unitIp
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	private List listUnitConfigs(String unitIp, long unitId) throws ServiceException {
		String hql = "from MonitorUnitConfig MonitorUnitConfig" +
					 " where MonitorUnitConfig.ip='" + JdbcUtils.resetQuot(unitIp) + "'" +
					 (unitId==0 ? "" : " and MonitorUnitConfig.unitId=" + unitId);
		List unitConfigs = getDatabaseService().findRecordsByHql(hql);
        if(unitConfigs==null || unitConfigs.isEmpty()) {
        	return null;
        }
    	for(Iterator iterator = unitConfigs.iterator(); iterator.hasNext();) {
    		MonitorUnitConfig unitConfig = (MonitorUnitConfig)iterator.next();
    		if(unitConfig.getSqls()==null) {
    			iterator.remove();
    			continue;
    		}
    		if(unitConfig.getDbPassword()!=null && !unitConfig.getDbPassword().isEmpty()) {
    			unitConfig.setDbPassword(cryptService.decrypt(unitConfig.getDbPassword(), "" + unitConfig.getId(), false)); //解密数据库密码
    		}
    		DatabaseDialect databaseDialect = JdbcUtils.getDatabaseDialect(unitConfig.getJdbcUrl()); //数据库方言
			unitConfig.setSqls(new LinkedHashSet(unitConfig.getSqls())); //转换为LinkedHashSet
			for(Iterator iteratorSql = unitConfig.getSqls().iterator(); iteratorSql.hasNext();) {
				MonitorUnitSql monitorUnitSql = (MonitorUnitSql)iteratorSql.next();
				if(monitorUnitSql.getLastCaptureTime()==null) {
					continue;
				}
				//重置SQL语句,增加采集时间判断
				BusinessObject businessObject = getBusinessDefineService().getBusinessObject(monitorUnitSql.getCaptureContentClass());
				if(businessObject==null) {
					iteratorSql.remove();
					continue;
				}
				//获取抓取时间字段
				Field createTimeField = businessObject.getFieldByParameter("createTime", "true");
				if(createTimeField==null) {
					Logger.error("MonitorService: create time field not found in class " + monitorUnitSql.getCaptureContentClass() + ".");
					iteratorSql.remove();
					continue;
				}
				Field monitorBeginField = businessObject.getFieldByParameter("monitorBegin", "true");
				Field monitorEndField = businessObject.getFieldByParameter("monitorEnd", "true");
				String captureSql = monitorUnitSql.getCaptureSql().trim();
				if(captureSql.endsWith(";")) {
					captureSql = captureSql.substring(0, captureSql.length()-1);
				}
				//抓取时间向前推一天,以避免因计算机时间差异造成漏采集
				String hqlCaptureTime = "TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(monitorUnitSql.getLastCaptureTime(), Calendar.DAY_OF_MONTH, -1), null) + ")";
				captureSql = "select * from (" + captureSql + ") t" +
							 " where t." + createTimeField.getName() + ">=" + hqlCaptureTime +
							 (monitorBeginField==null || monitorBeginField.getName().equals(createTimeField.getName()) ? "" : " or t." + monitorBeginField.getName() + ">=" + hqlCaptureTime) +
							 (monitorEndField==null || monitorEndField.getName().equals(createTimeField.getName()) ? "" : " or t." + monitorEndField.getName() + ">=" + hqlCaptureTime);
				monitorUnitSql.setCaptureSql(databaseDialect.replaceFunction(captureSql));
			}
    	}
        return unitConfigs;
	}

	/**
	 * 保存采集到的数据
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void saveCaptureData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long monitorSqlId = Long.parseLong(request.getHeader("monitorSqlId"));
		//获取SQL配置
		String hql = "select MonitorUnitSql" +
					 " from MonitorUnitSql MonitorUnitSql left join MonitorUnitSql.unitConfig MonitorUnitConfig" +
					 " where MonitorUnitSql.id=" + monitorSqlId +
					 " and MonitorUnitConfig.ip='" + request.getRemoteAddr() + "'";
		MonitorUnitSql monitorUnitSql = (MonitorUnitSql)getDatabaseService().findRecordByHql(hql);
		if(monitorUnitSql==null) {
			Logger.error("MonitorService: invalid request, unit ip is " + request.getRemoteAddr() + ", sql id is " + monitorSqlId + ".");
			return;
		}
		final BusinessObject businessObject = getBusinessDefineService().getBusinessObject(monitorUnitSql.getCaptureContentClass());
		if(businessObject==null) {
			return;
		}
		//获取抓取时间字段
		List records = (List)new ObjectInputStream(request.getInputStream()).readObject();
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			SqlResult sqlResult = (SqlResult)iterator.next();
			try {
				saveCaptureRecord(sqlResult, monitorUnitSql, businessObject); //保存采集的数据
			}
			catch(Exception e) {
				Logger.exception(e);
				saveCaptureLog(monitorUnitSql, e); //保存采集日志
			}
		}
		if("true".equals(request.getHeader("lastBatch"))) {
			//更新采集时间
			monitorUnitSql.setLastCaptureTime(DateTimeUtils.now());
			getDatabaseService().updateRecord(monitorUnitSql);
			//保存采集日志
			saveCaptureLog(monitorUnitSql, null);
		}
	}
	
	/**
	 * 保存采集的数据
	 * @param record
	 * @param monitorUnitSql
	 * @param businessObject
	 * @throws Exception
	 */
	private void saveCaptureRecord(SqlResult record, MonitorUnitSql monitorUnitSql, BusinessObject businessObject) throws Exception {
		MonitorRecord monitorRecord = (MonitorRecord)Class.forName(businessObject.getClassName()).newInstance();
		JdbcUtils.copyFields(monitorRecord, record); //拷贝字段值到POJO
		if(monitorRecord.getRecordId()==null || monitorRecord.getRecordId().isEmpty()) {
			return;
		}
		monitorRecord.setLastCaptureTime(DateTimeUtils.now()); //最后采集时间
		if(monitorRecord.getUnitId()==0) { //没有指定单位ID
			monitorRecord.setUnitId(monitorUnitSql.getUnitConfig().getUnitId());
			monitorRecord.setUnitName(monitorUnitSql.getUnitConfig().getUnitName());
		}
		else if(monitorRecord.getUnitName()==null || monitorRecord.getUnitName().isEmpty()) { //指定单位ID,且单位名称为空
			monitorRecord.setUnitName(orgService.getDirectoryName(monitorRecord.getUnitId()));
		}
		
		//按记录ID查找记录是否已经存在
		String pojoClassName = businessObject.getClassName().substring(businessObject.getClassName().lastIndexOf('.') + 1);
		String hql = "from " + pojoClassName + " " + pojoClassName +
	  		  		 " where " + pojoClassName + ".unitId=" + monitorRecord.getUnitId() +
	  		  		 " and " + pojoClassName + ".recordId='" + monitorRecord.getRecordId() + "'";
		MonitorRecord oldMonitorRecord = (MonitorRecord)getDatabaseService().findRecordByHql(hql);
		
		monitorRecord.setProcessDays(0);
		monitorRecord.setTimeoutLevel(0);
		monitorRecord.setTimeoutDays(0);
		if(monitorUnitSql.getLastCaptureTime()!=null) { //不是第一次抓取
			Field createTimeField = businessObject.getFieldByParameter("createTime", "true");
			Field monitorBeginField = businessObject.getFieldByParameter("monitorBegin", "true");
			Field monitorEndField = businessObject.getFieldByParameter("monitorEnd", "true");
			//创建时间校验,如果原来的创建时间不为空,则使用原来的时间;如果创建时间为空或者晚于当前时间,则使用当前时间
			Date createTime = (Date)PropertyUtils.getProperty(monitorRecord, createTimeField.getName());
			Date oldCreateTime = oldMonitorRecord==null ? null : (Date)PropertyUtils.getProperty(oldMonitorRecord, createTimeField.getName());
			if(oldCreateTime!=null) {
				createTime = oldCreateTime;
			}
			else if(createTime==null || createTime.after(DateTimeUtils.now())) {
				createTime = PropertyUtils.getPropertyType(monitorRecord, createTimeField.getName()).equals(java.sql.Date.class) ? (Date)DateTimeUtils.date() : DateTimeUtils.now();
			}
			PropertyUtils.setProperty(monitorRecord, createTimeField.getName(), createTime);
			
			//监察开始时间校验,如果原来的监察开始时间不为空,则使用原来的时间;如果监察开始时间晚于当前时间,则使用当前时间
			if(monitorBeginField!=null && !monitorBeginField.getName().equals(createTimeField.getName())) {
				Date monitorBegin = (Date)PropertyUtils.getProperty(monitorRecord, monitorBeginField.getName());
				Date oldMonitorBegin = oldMonitorRecord==null ? null : (Date)PropertyUtils.getProperty(oldMonitorRecord, monitorBeginField.getName());
				if(oldMonitorBegin!=null) {
					monitorBegin = oldMonitorBegin;
				}
				else if(monitorBegin!=null && monitorBegin.after(DateTimeUtils.now())) {
					monitorBegin = PropertyUtils.getPropertyType(monitorRecord, monitorBeginField.getName()).equals(java.sql.Date.class) ? (Date)DateTimeUtils.date() : DateTimeUtils.now();
				}
				PropertyUtils.setProperty(monitorRecord, monitorBeginField.getName(), monitorBegin);
			}
			
			//监察结束时间校验,如果原来的记录中有监察结束时间,则使用原来的时间;如果监察结束时间晚于当前时间,则使用当前时间;如果检查结束时间早于采集开始时间,则使用采集开始时间
			if(monitorEndField!=null && !monitorEndField.getName().equals(createTimeField.getName())) {
				Date monitorEnd = (Date)PropertyUtils.getProperty(monitorRecord, monitorEndField.getName());
				Date oldMonitorEnd = oldMonitorRecord==null ? null : (Date)PropertyUtils.getProperty(oldMonitorRecord, monitorEndField.getName());
				if(oldMonitorEnd!=null) {
					monitorEnd = oldMonitorEnd;
				}
				else if(monitorEnd!=null && monitorEnd.after(DateTimeUtils.now())) {
					monitorEnd = PropertyUtils.getPropertyType(monitorRecord, monitorEndField.getName()).equals(java.sql.Date.class) ? (Date)DateTimeUtils.date() : DateTimeUtils.now();
				}
				else if(monitorEnd!=null && monitorUnitSql.getLastCaptureTime()!=null && monitorEnd.before(monitorUnitSql.getLastCaptureTime())) {
					monitorEnd = PropertyUtils.getPropertyType(monitorRecord, monitorEndField.getName()).equals(java.sql.Date.class) ? (Date)new java.sql.Date(monitorUnitSql.getLastCaptureTime().getTime()) : new Timestamp(monitorUnitSql.getLastCaptureTime().getTime());
				}
				PropertyUtils.setProperty(monitorRecord, monitorEndField.getName(), monitorEnd);
			}
			
			//统计办理天数(工作日),并得出超时等级
			countProcessDays(monitorRecord, businessObject);
		}
		
		//保存或更新数据
		if(oldMonitorRecord==null) { //新记录
			monitorRecord.setId(UUIDLongGenerator.generateId());
			monitorRecord.setCaptureTime(DateTimeUtils.now()); //抓取时间
			getDatabaseService().saveRecord(monitorRecord);
		}
		else { //旧记录
			monitorRecord.setId(oldMonitorRecord.getId());
			monitorRecord.setCaptureTime(oldMonitorRecord.getCaptureTime()); //抓取时间
			getDatabaseService().updateRecord(monitorRecord);
		}
	}
	
	/**
	 * 统计办理天数(工作日),并得出超时等级
	 * @param monitorRecord
	 * @param businessObject
	 * @throws Exception
	 */
	private void countProcessDays(MonitorRecord monitorRecord, BusinessObject businessObject) throws Exception {
		monitorRecord.setProcessDays(0);
		monitorRecord.setTimeoutLevel(0);
		monitorRecord.setTimeoutDays(0);
		Field monitorEndField = businessObject.getFieldByParameter("monitorEnd", "true");
		if(monitorEndField==null) { //没有指定监察结束时间字段
			return;
		}
		//获取参数配置
		String hql = "select MonitorContentParameter" +
			  		 " from MonitorContentParameter MonitorContentParameter, MonitorParameter MonitorParameter, OrgSubjection OrgSubjection" +
			  		 " where MonitorContentParameter.parameterId=MonitorParameter.id" +
			  		 " and MonitorContentParameter.contentClassName='" + businessObject.getClassName() + "'" +
			  		 " and MonitorParameter.orgId=OrgSubjection.parentDirectoryId" +
			  		 " and OrgSubjection.directoryId=" + monitorRecord.getUnitId() +
			  		 " order by OrgSubjection.id";
		List contentParameters = getDatabaseService().findRecordsByHql(hql, 0, 2);
		String timeoutCheckParameter = null;
		if(contentParameters!=null && !contentParameters.isEmpty()) {
			MonitorContentParameter contentParameter = (MonitorContentParameter)contentParameters.get(0);
			if(contentParameters.size()>1 && contentParameter.getMonitorParameter().getOrgId()==monitorRecord.getUnitId()) {
				contentParameter = (MonitorContentParameter)contentParameters.get(1);
			}
			timeoutCheckParameter = contentParameter.getTimeout();
		}
		if(timeoutCheckParameter==null) { //没有配置参数
			return;
		}
		String[] timeoutLevels = timeoutCheckParameter.split(",");
		//计算天数
		Field createTimeField = businessObject.getFieldByParameter("createTime", "true");
		Field monitorBeginField = businessObject.getFieldByParameter("monitorBegin", "true");
		Field beginField = monitorBeginField==null ? createTimeField : monitorBeginField;
		Date monitorBegin = (Date)PropertyUtils.getProperty(monitorRecord, beginField.getName());
		if(monitorBegin==null && beginField.isRequired()) { //监察开始时间不允许为空
			monitorRecord.setTimeoutLevel(timeoutLevels.length); //设为最高超时等级
			return;
		}
		if(monitorBegin==null) { //开始时间为空
			monitorBegin = (Date)PropertyUtils.getProperty(monitorRecord, createTimeField.getName()); //使用创建时间
		}
		Date monitorEnd = (Date)PropertyUtils.getProperty(monitorRecord, monitorEndField.getName());
		if(monitorEnd==null) { //结束时间为空,使用最后时间
			monitorEnd = monitorRecord.getLastCaptureTime();
		}
		if(monitorEnd.before(monitorBegin)) { //结束时间早于开始时间
			monitorRecord.setTimeoutLevel(timeoutLevels.length); //设为最高超时等级
			return;
		}
		Timestamp monitorBeginTime = new Timestamp(monitorBegin.getTime());
		if(monitorBegin instanceof java.sql.Date) {
			monitorBeginTime = DateTimeUtils.add(monitorBeginTime, Calendar.DAY_OF_MONTH, 1);
		}
		Timestamp monitorEndTime = new Timestamp(monitorEnd.getTime());
		if(monitorEnd instanceof java.sql.Date) {
			monitorEndTime = DateTimeUtils.add(DateTimeUtils.add(monitorEndTime, Calendar.DAY_OF_MONTH, 1), Calendar.SECOND, -1);
		}
		monitorRecord.setProcessDays(timetableService.countWorkDays(monitorBeginTime, monitorEndTime, 0) - 1);
		int level = 0;
		try {
			if(timeoutLevels[0].indexOf('-')==-1) { //数字
				for(; level<timeoutLevels.length && monitorRecord.getProcessDays()>Double.parseDouble(timeoutLevels[level].trim()); level++);
				//设置超时天数
				monitorRecord.setTimeoutDays(monitorRecord.getProcessDays() - Double.parseDouble(timeoutLevels[0].trim()));
			}
			else { //日期
				for(; level<timeoutLevels.length; level++) {
					Timestamp date = DateTimeUtils.parseTimestamp(DateTimeUtils.getYear(monitorEnd) + "-" + timeoutLevels[level].trim() + "", "yyyy-MM-dd");
					date = DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1); //当天不算超时
					if(level==0) {
						monitorRecord.setTimeoutDays(timetableService.countWorkDays(date, monitorEndTime, 0) - 1); //设置超时天数
					}
					if(monitorEnd.before(date)) {
						break;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		monitorRecord.setTimeoutLevel(level);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableServiceListener#onWorkDayChanged(java.sql.Date, java.sql.Date)
	 */
	public void onWorkDayChanged(java.sql.Date beginDate, java.sql.Date endDate) throws ServiceException {
		if(beginDate==null) {
			return;
		}
		List contentBusinessObjects = listContentBusinessObjects();
		for(Iterator iterator = contentBusinessObjects==null ? null : contentBusinessObjects.iterator(); iterator!=null && iterator.hasNext();) {
			BusinessObject businessObject = (BusinessObject)iterator.next();
			Field monitorEndField = businessObject.getFieldByParameter("monitorEnd", "true");
			if(monitorEndField==null) { //没有指定监察结束时间字段
				continue; //不计算工作日
			}
			Field createTimeField = businessObject.getFieldByParameter("createTime", "true");
			Field monitorBeginField = businessObject.getFieldByParameter("monitorBegin", "true");
			String pojoName = businessObject.getClassName().substring(businessObject.getClassName().lastIndexOf('.') + 1);
			String hql = "from " + pojoName + " " + pojoName +
						 " where (" + pojoName + "." + monitorEndField.getName() + ">DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
						 "  or (" + pojoName + "." + monitorEndField.getName() + " is null" +
						 "  and " + pojoName + ".lastCaptureTime>DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")))";
			if(endDate!=null) {
				Field beginField = monitorBeginField==null ? createTimeField : monitorBeginField;
				hql += " and (" + pojoName + "." + beginField.getName() + "<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")";
				if(beginField==monitorBeginField && createTimeField!=monitorBeginField) {
					hql += "  or (" + pojoName + "." + monitorBeginField.getName() + " is null" +
						   "  and " + pojoName + "." + createTimeField.getName() + "<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + "))";
				}
				hql += ")";
			}
			hql += " order by " + pojoName + ".id";
			for(int i=0; ; i+=200) {
				List monitorRecords = getDatabaseService().findRecordsByHql(hql, i, 200); //每次处理200条记录
				for(Iterator iteratorRecord = monitorRecords==null ? null : monitorRecords.iterator(); iteratorRecord!=null && iteratorRecord.hasNext();) {
					MonitorRecord monitorRecord = (MonitorRecord)iteratorRecord.next();
					try {
						countProcessDays(monitorRecord, businessObject); //重新计算工作日
						getDatabaseService().updateRecord(monitorRecord);
					}
					catch (Exception e) {
						Logger.exception(e);
					}
				}
				if(monitorRecords==null || monitorRecords.size()<200) {
					break;
				}
			}
		}
	}

	/**
	 * 保存采集日志
	 * @param monitorUnitSql
	 * @param e
	 */
	private void saveCaptureLog(MonitorUnitSql monitorUnitSql, Exception e) {
		MonitorCaptureLog captureLog = new MonitorCaptureLog();
		captureLog.setId(UUIDLongGenerator.generateId()); //ID
		captureLog.setUnitSqlId(monitorUnitSql.getId()); //SQL配置ID
		captureLog.setUnitId(monitorUnitSql.getUnitConfig().getUnitId()); //单位ID
		captureLog.setUnitName(monitorUnitSql.getUnitConfig().getUnitName()); //单位名称
		captureLog.setCaptureSql(monitorUnitSql.getCaptureSql()); //SQL
		captureLog.setCaptureTime(DateTimeUtils.now()); //最后采集时间
		captureLog.setIsSuccess(e==null ? 1 : 0); //是否成功
		captureLog.setCaptureFailedReason(StringUtils.exceptionToString(e)); //采集失败原因
		getDatabaseService().saveRecord(captureLog);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.monitor.service.MonitorService#getCaptureTime()
	 */
	public String getCaptureTime() throws ServiceException {
		String hql = "select MonitorUnitConfig.captureTime" +
					 " from MonitorUnitConfig MonitorUnitConfig" +
					 " order by MonitorUnitConfig.created DESC";
		String captureTime = (String)getDatabaseService().findRecordByHql(hql);
		if(captureTime==null) {
			return "00:00";
		}
		//加5分钟
		Timestamp time;
		try {
			time = DateTimeUtils.parseTimestamp("2005-07-18 " + captureTime, "yyyy-MM-dd HH:mm");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		time = DateTimeUtils.add(time, Calendar.MINUTE, 5);
		return DateTimeUtils.formatTimestamp(time, "HH:mm");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.monitor.service.MonitorService#getSampleSql(java.lang.String)
	 */
	public String getSampleSql(String captureContentClass) throws ServiceException {
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(captureContentClass);
		String sql = "select";
		String[] names = {"id", "unitName", "captureTime"}; //ID、单位ID、单位名称、采集时间不需要输出
		for(int i=0; i<names.length; i++) {
			ListUtils.removeObjectByProperty(businessObject.getFields(), "name", names[i]);
		}
		for(Iterator iterator = ListUtils.getSubListByProperty(businessObject.getFields(), "persistence", Boolean.TRUE).iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			sql += "\r\n as " + field.getName() +
				   (iterator.hasNext() ? "," : "") +
				   " /*" + field.getTitle() + "(" + field.getType() + (field.getLength()==null ? "" : "," + field.getLength()) + ("unitId".equals(field.getName()) ? ",部署在同一个系统中时使用" : "") + ")" + "*/";
		}
		return sql + "\r\n from [表名];";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("captureContent".equals(itemsName)) { //采集内容
			List contentBusinessObjects = listContentBusinessObjects();
			for(int i=0; i<(contentBusinessObjects==null ? 0 : contentBusinessObjects.size()); i++) {
				BusinessObject businessObject = (BusinessObject)contentBusinessObjects.get(i);
				contentBusinessObjects.set(i, new String[]{businessObject.getTitle(), businessObject.getClassName()});
			}
			return contentBusinessObjects;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
	
	/**
	 * 获取采集内容业务对象
	 * @return
	 * @throws ServiceException
	 */
	private List listContentBusinessObjects() throws ServiceException {
		String[] classNames = captureContentClasses.split(",");
		List contentBusinessObjects = new ArrayList();
		for(int i=0; i<classNames.length; i++) {
			BusinessObject businessObject = getBusinessDefineService().getBusinessObject(classNames[i]);
			if(businessObject!=null) {
				contentBusinessObjects.add(businessObject);
			}
		}
		return contentBusinessObjects;
	}

	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the timetableService
	 */
	public TimetableService getTimetableService() {
		return timetableService;
	}

	/**
	 * @param timetableService the timetableService to set
	 */
	public void setTimetableService(TimetableService timetableService) {
		this.timetableService = timetableService;
	}
}