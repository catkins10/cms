package com.yuanluesoft.cms.publicservice.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceOpinion;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceSN;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceSms;
import com.yuanluesoft.cms.publicservice.pojo.WorkflowSetting;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectoryPopedom;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.smssubscription.model.SmsContentDefinition;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.timetable.services.TimetableService;
import com.yuanluesoft.jeaf.timetable.services.TimetableServiceListener;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;
import com.yuanluesoft.workflow.client.model.resource.ProgrammingParticipant;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceImpl extends BusinessServiceImpl implements PublicService, TimetableServiceListener {
	private SiteService siteService;
	private SessionService sessionService;
	private WorkflowExploitService workflowExploitService; //工作流利用服务
    private SmsService smsService; //短信服务
    private ExchangeClient exchangeClient; //业务逻辑服务
    private PageService pageService; //页面服务
    private RecordControlService recordControlService; //记录控制服务
    private TimetableService timetableService; //时间表服务
	private boolean forceValidateCode = true; //是否强制验证码校验,默认需要校验
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof com.yuanluesoft.cms.publicservice.pojo.PublicService) {
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)record;
			//计算办理结束时间
			if(publicService.getWorkingDay()>0) {
				try {
					publicService.setWorkingDate(timetableService.addWorkDays(publicService.getCreated(), publicService.getWorkingDay(), 0));
				}
				catch(Exception e) {
					publicService.setWorkingDate(DateTimeUtils.add(publicService.getCreated(), Calendar.DAY_OF_MONTH, publicService.getWorkingDay()));
				}
			}
			if((publicService.getWorkflowInstanceId()==null || publicService.getWorkflowInstanceId().isEmpty()) && //没有创建过流程
				!Boolean.TRUE.equals(publicService.getExtendPropertyValue("importData"))) { //不是导入的数据
				try {
					publicService.setWorkflowInstanceId(createWorkflowInstance(publicService)); //创建流程实例
				}
				catch(ServiceException e) {
					if(exchangeClient==null || !exchangeClient.isExchangeable()) { //不能数据交换
						throw e;
					}
				}
			}
			if(isExhangeable(publicService)) {
				//同步更新
				exchangeClient.synchUpdate(record, null, 2000);
			}
			//重新生成静态页面
			if(pageService!=null && isPageRebuildable(publicService)) {
				pageService.rebuildStaticPageForModifiedObject(publicService, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof com.yuanluesoft.cms.publicservice.pojo.PublicService)) {
			return super.update(record);
		}
		com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)record;
		String pojoClassName = record.getClass().getName();
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
		Object[] values = (Object[])getDatabaseService().findRecordByHql("select " + pojoClassName + ".publicPass, " + pojoClassName + ".workingDay from " + pojoClassName + " " + pojoClassName + " where " + pojoClassName + ".id=" + publicService.getId());
		if(publicService.getWorkingDay()<=0) {
			publicService.setWorkingDate(null);
		}
		else if(((Number)values[1]).intValue()!=publicService.getWorkingDay()) { //工作日有调整
			try {
				publicService.setWorkingDate(timetableService.addWorkDays(publicService.getCreated(), publicService.getWorkingDay(), 0));
			}
			catch(Exception e) {
				publicService.setWorkingDate(DateTimeUtils.add(publicService.getCreated(), Calendar.DAY_OF_MONTH, publicService.getWorkingDay()));
			}
		}
		super.update(record);
		if(isExhangeable(publicService)) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		//重新生成静态页面
		if(pageService!=null && isPageRebuildable(publicService)) {
			pageService.rebuildStaticPageForModifiedObject(publicService, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else if(pageService!=null && (((Character)values[0]).charValue()=='1' || publicService.getIsDeleted()==1)) { //撤销发布、或者已经删除
			pageService.rebuildStaticPageForModifiedObject(publicService, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#unpublishTimeoutRecords()
	 */
	public void unpublishTimeoutRecords() throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("PublicService: unpublish timeout records.");
		}
		List lazyLoadProperties = listLazyLoadProperties(com.yuanluesoft.cms.publicservice.pojo.PublicService.class);
		String hql = "from PublicService PublicService" +
					 " where PublicService.publicPass='1'" + //已发布的
					 " and PublicService.publicEnd is not null" + //截止时间不为空
					 " and PublicService.publicEnd<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")"; //大于当前时间
		for(int i=0; i<1000; i++) {
			List records = getDatabaseService().findRecordsByHql(hql, lazyLoadProperties, 0, 200);
			for(Iterator iterator = records==null ? null : records.iterator(); iterator!=null && iterator.hasNext();) {
				com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)iterator.next();
				try {
					publicService.setPublicPass('0');
					update(publicService);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
			if(records==null || records.size()<200) {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchUpdate(java.lang.Object, java.lang.String)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		if(object instanceof PublicServiceSN) {
			PublicServiceSN publicServiceSN = (PublicServiceSN)getDatabaseService().findRecordByHql("from PublicServiceSN PublicServiceSN");
			if(publicServiceSN!=null) {
				PublicServiceSN newSN = (PublicServiceSN)object;
				publicServiceSN.setDay(newSN.getDay());
				publicServiceSN.setSn(newSN.getSn());
				update(publicServiceSN);
				return;
			}
		}
		super.synchUpdate(object, senderName);
		if(object instanceof com.yuanluesoft.cms.publicservice.pojo.PublicService) {
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)object;
			if(publicService.getWorkflowInstanceId()==null || publicService.getWorkflowInstanceId().isEmpty()) { //没有创建过流程
				publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)load(publicService.getClass(), publicService.getId());
				if(publicService.getWorkflowInstanceId()==null || publicService.getWorkflowInstanceId().isEmpty()) { //没有创建过流程
					try {
						//创建流程实例
						publicService.setWorkflowInstanceId(createWorkflowInstance(publicService));
						getDatabaseService().updateRecord(publicService);
					}
					catch(ServiceException e) {
					
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof com.yuanluesoft.cms.publicservice.pojo.PublicService) {
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)record;
			if(isExhangeable(publicService)) {
				exchangeClient.synchDelete(record, null, 2000);
			}
			if(pageService!=null && isPageRebuildable(publicService)) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //重新生成静态页面
			}
		}
	}

	/**
	 * 是否允许被交换
	 * @param publicService
	 * @return
	 * @throws ServiceException
	 */
	protected boolean isExhangeable(com.yuanluesoft.cms.publicservice.pojo.PublicService publicService) throws ServiceException {
		WebSite site = siteService.getParentSite(publicService.getSiteId());
		return site!=null && site.getIsInternal()=='0'; //不是内部网站
	}
	
	/**
	 * 检查静态页面是否需要重建
	 * @param publicService
	 * @return
	 * @throws ServiceException
	 */
	protected boolean isPageRebuildable(com.yuanluesoft.cms.publicservice.pojo.PublicService publicService) throws ServiceException {
		return publicService.getPublicPass()=='1' && publicService.getIsDeleted()!=1;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.complaint.service.ComplaintService#getSN()
	 */
	public String getSN() throws ServiceException {
		PublicServiceSN publicServiceSN = (PublicServiceSN)getDatabaseService().findRecordByHql("from PublicServiceSN PublicServiceSN");
		int sn = 1;
		Date today = DateTimeUtils.date();
		if(publicServiceSN==null) {
			publicServiceSN = new PublicServiceSN();
			publicServiceSN.setId(UUIDLongGenerator.generateId());
			publicServiceSN.setDay(today);
			publicServiceSN.setSn(sn);
			getDatabaseService().saveRecord(publicServiceSN);
		}
		else {
			if(publicServiceSN.getDay().equals(today)) {
				sn = publicServiceSN.getSn() + 1;
			}
			else {
				publicServiceSN.setDay(today);
			}
			publicServiceSN.setSn(sn);
			getDatabaseService().updateRecord(publicServiceSN);
		}
		exchangeClient.synchUpdate(publicServiceSN, null, 1000); //同步更新
		return new SimpleDateFormat("yyMMdd").format(today) + StringUtils.formatNumber(sn, 5, true);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#loadPublicService(java.lang.String, java.lang.String, java.lang.String)
	 */
	public com.yuanluesoft.cms.publicservice.pojo.PublicService loadPublicService(String pojoClassName, String sn, String queryPassword) throws ServiceException {
		try {
			Class recordClass = Class.forName(pojoClassName);
			pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
			String hql = "from " + pojoClassName + " " + pojoClassName +
						 " where " + pojoClassName + ".sn='" + JdbcUtils.resetQuot(sn) + "'";
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(recordClass));
			if(publicService==null) {
				return null;
			}
			if(publicService.getQueryPassword()==null || publicService.getQueryPassword().isEmpty()) {
				return publicService;
			}
			if(!publicService.getQueryPassword().equals(queryPassword)) {
				return null;
			}
			return publicService;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#getWorkflowSetting(java.lang.String, long, boolean)
	 */
	public WorkflowSetting getWorkflowSetting(String applicationName, long siteId, boolean inheritParentEnabled) throws ServiceException {
		String hql;
		if(!inheritParentEnabled || siteId==0) {
			hql = "from WorkflowSetting WorkflowSetting" +
				  " where WorkflowSetting.applicationName='" + applicationName + "'" +
				  " and WorkflowSetting.siteId=" + siteId;
		}
		else {
			//查找当前站点意见上级站点的流程配置
			hql = "select WorkflowSetting" +
				  " from WorkflowSetting WorkflowSetting, WebDirectorySubjection WebDirectorySubjection" +
				  " where WorkflowSetting.siteId=WebDirectorySubjection.parentDirectoryId" +
				  " and WorkflowSetting.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
				  " and WebDirectorySubjection.directoryId=" + siteId +
				  " order by WebDirectorySubjection.id";
		}
		return (WorkflowSetting)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsContentService#listSmsContentDefinitions()
	 */
	public List listSmsContentDefinitions() throws ServiceException {
		List contentDefinitions = new ArrayList();
		contentDefinitions.add(new SmsContentDefinition("公众服务受理情况查询", "按受理编号和查询密码查询领导信箱、网上投诉、信息公开申请等的办理情况", SmsContentService.SEND_MODE_REPLY, "受理编号,查询密码"));
		return contentDefinitions;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getContentDescription(java.lang.String, java.lang.String, long)
	 */
	public String getContentDescription(String contentName, String subscribeParameter, long siteId) throws ServiceException {
		return contentName;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#sendSmsToCreator(java.lang.String, long)
	 */
	public void sendSmsToCreator(String applicationName, long publicServiceId) throws ServiceException {
		com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)load(com.yuanluesoft.cms.publicservice.pojo.PublicService.class, publicServiceId);
		if(publicService==null || publicService.getCreatorMobile()==null || publicService.getCreatorMobile().equals("")) {
			return;
		}
		//获取办结短信格式配置
		PublicServiceSms smsFormatConfig = loadSmsFormatConfig(applicationName, publicService.getSiteId());
		if(smsFormatConfig==null || smsFormatConfig.getSmsFormat()==null || smsFormatConfig.getSmsFormat().equals("")) {
			return;
		}
		String message = smsFormatConfig.getSmsFormat().replaceAll("<主题>", publicService.getSubject()==null ? "" : publicService.getSubject())
													   .replaceAll("<提交人>", publicService.getCreator()==null ? "" : publicService.getCreator())
													   .replaceAll("<编号>", publicService.getSn()==null ? "" : publicService.getSn())
													   .replaceAll("<办结时间>", DateTimeUtils.formatTimestamp(DateTimeUtils.now(), "yyyy-MM-dd HH:mm"));
		if(smsFormatConfig.getSmsFormat().indexOf("<办理意见>")!=-1) {
			String opinions = null;
			//输出办理意见
			if(publicService.getOpinions()!=null) {
				for(Iterator iterator = publicService.getOpinions().iterator(); iterator.hasNext();) {
					Opinion opinion = (Opinion)iterator.next();
					opinions = (opinions==null ? "" : opinions + "\r\n") + opinion.getOpinionType() + "意见：" +
							   opinion.getOpinion() + " " +
							   DateTimeUtils.formatTimestamp(opinion.getCreated(), "yy-MM-dd HH:mm");
				}
			}
			message = message.replaceAll("<办理意见>", opinions==null ? "" : opinions);
		}
		try {
			smsService.sendShortMessage(0, "公众服务", siteService.getParentSite(publicService.getSiteId()).getOwnerUnitId(), "系统消息", null, publicService.getCreatorMobile(), message, null, -1, false, null, false);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getSmsReplyContent(java.lang.String, java.util.Map, java.lang.String, java.lang.String, long)
	 */
	public String getSmsReplyContent(String contentName, Map fieldValueMap, String message, String senderNumber, long siteId) throws ServiceException {
		//公众服务受理情况查询
		try {
			String sn = (String)fieldValueMap.get("受理编号");
			String queryPassword = (String)fieldValueMap.get("查询密码");
			String hql = "from PublicService PublicService" +
						 " where PublicService.sn='" + JdbcUtils.resetQuot(sn) + "'" +
						 " and PublicService.queryPassword='" + JdbcUtils.resetQuot(queryPassword) + "'";
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("opinions,workItems", ","));
			if(publicService!=null) {
				//输出当前流程状态
				String reply = "受理情况：" + publicService.getWorkflowStatus() + "。";
				//输出办理意见
				if(publicService.getOpinions()!=null) {
					for(Iterator iterator = publicService.getOpinions().iterator(); iterator.hasNext();) {
						Opinion opinion = (Opinion)iterator.next();
						reply += "\r\n" + opinion.getOpinionType() + "意见：" + opinion.getOpinion() + " " + DateTimeUtils.formatTimestamp(opinion.getCreated(), "yy-MM-dd HH:mm");
					}
				}
				return reply;
			}
		}
		catch(Exception e) {
			
		}
		return "查询失败，请确认受理编号或密码是否正确。";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#loadSmsFormatConfig(java.lang.String, long)
	 */
	public PublicServiceSms loadSmsFormatConfig(String applicationName, long siteId) throws ServiceException {
		String hql = "from PublicServiceSms PublicServiceSms" +
					 " where PublicServiceSms.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
					 " and PublicServiceSms.siteId=" + siteId;
		return (PublicServiceSms)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long siteId = RequestUtils.getParameterLongValue(notifyRequest, "siteId"); //站点ID
		String applicationName = RequestUtils.getParameterStringValue(notifyRequest, "applicationName");
		WorkflowSetting workflowSetting = getWorkflowSetting(applicationName, siteId, false);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程已删除
			getDatabaseService().deleteRecord(workflowSetting);
			return;
		}
		boolean isNew = (workflowSetting==null);
		if(isNew) {
			workflowSetting = new WorkflowSetting();
			workflowSetting.setId(UUIDLongGenerator.generateId()); //ID
			workflowSetting.setApplicationName(applicationName); //应用名称
			workflowSetting.setSiteId(siteId); //绑定的站点ID
		}
		workflowSetting.setWorkflowId(Long.parseLong(workflowId)); //绑定的流程ID
		workflowSetting.setWorkflowName(workflowPackage.getName()); //绑定的流程名称
		if(isNew) {
			getDatabaseService().saveRecord(workflowSetting);
		}
		else {
			getDatabaseService().updateRecord(workflowSetting);
		}
	}
	
	/**
	 * 短信提交
	 * @param publicServiceClass
	 * @param fieldValueMap
	 * @param senderNumber
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	protected com.yuanluesoft.cms.publicservice.pojo.PublicService smsSubmit(Class publicServiceClass, Map fieldValueMap, String senderNumber, final long siteId) throws Exception {
		//领导信箱写信
		String queryPassword = (String)fieldValueMap.get("查询密码");
		String body = (String)fieldValueMap.get("正文");
		if(body==null) {
			throw new ServiceException("未输入正文,提交失败。");
		}
		
		//创建公众服务对象
		com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)publicServiceClass.newInstance();
		publicService.setId(UUIDLongGenerator.generateId()); //ID
		publicService.setSn(getSN()); //编号
		publicService.setQueryPassword(queryPassword); //查询密码
		publicService.setSubject(StringUtils.slice(body, 200, "...")); //主题
		publicService.setWorkingDay(15); //指定工作日
		publicService.setCreator("短信用户"); //创建人姓名
		publicService.setCreated(DateTimeUtils.now()); //创建时间
		publicService.setCreatorTel("　"); //联系电话
		publicService.setCreatorMail("　"); //邮箱
		publicService.setCreatorSex('M'); //性别
		publicService.setCreatorCertificateName("身份证"); //创建人证件名称
		publicService.setCreatorIdentityCard("　"); //创建人身份证/证件号码
		publicService.setCreatorIP("　"); //创建人IP
		publicService.setCreatorMobile(senderNumber); //创建人手机
		publicService.setCreatorFax("　"); //创建人传真
		publicService.setCreatorUnit("　"); //创建人所在单位
		publicService.setCreatorJob("　"); //创建人职业
		publicService.setCreatorAddress("　"); //创建人地址
		publicService.setCreatorPostalcode("　"); //创建人邮编
		publicService.setIsPublic('1'); //是否允许公开
		publicService.setRemark(null); //附注
		publicService.setSiteId(siteId); //隶属站点ID
		publicService.setContent(body); //正文
		publicService.setPublicPass('0'); //同意公开
		publicService.setPublicBody('0'); //公开正文
		publicService.setPublicWorkflow('0'); //公开办理流程
		publicService.setPublicSubject(null); //公开的主题
		
		//创建流程实例
		publicService.setWorkflowInstanceId(createWorkflowInstance(publicService));
		
		//保存公众服务
		getDatabaseService().saveRecord(publicService);
        return publicService;
	}

	/**
	 * 创建流程实例
	 * @param publicService
	 * @return
	 * @throws Exception
	 */
	protected String createWorkflowInstance(final com.yuanluesoft.cms.publicservice.pojo.PublicService publicService) throws ServiceException {
		if(workflowExploitService==null) {
			throw new ServiceException();
		}
		//获取业务对象定义
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(publicService.getClass());
		//获取流程配置
		WorkflowSetting workflowSetting = getWorkflowSetting(businessObject.getApplicationName(), publicService.getSiteId(), true);
		if(workflowSetting==null) {
			throw new ServiceException("流程未定义，提交失败。");
		}
		//获取流程入口
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(SessionService.ANONYMOUS);
		} 
		catch (SessionException e) {
			throw new ServiceException(e.getMessage());
		}
		if(publicService.getCreator()!=null && !publicService.getCreator().equals("")) {
			sessionInfo.setUserName(publicService.getCreator());
		}
		WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry("" + workflowSetting.getWorkflowId(), null, publicService, sessionInfo);
		if(workflowEntry==null) {
			throw new ServiceException("流程未定义，提交失败。");
		}
		
		//创建流程实例并自动发送
		String message = (publicService.getSubject()!=null ? publicService.getSubject() : (publicService.getContent()==null ? businessObject.getTitle() : StringUtils.slice(StringUtils.filterHtmlElement(publicService.getContent(), false), 30, "...")));
		WorkflowMessage workflowMessage = new WorkflowMessage(businessObject.getTitle(), message, null);
		Element activity = (Element)workflowEntry.getActivityEntries().get(0);
		
		WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {
			public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				if(anyoneParticipate) { //允许所有人办理,指定站点编辑和站点管理员为办理人
					participants = new ArrayList();
					participants.add(new ProgrammingParticipant("siteManager", "站点管理员"));
					participants.add(new ProgrammingParticipant("siteEditor", "站点编辑"));
				}
				return participants;
			}
			public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return retrieveProgrammingParticipants(publicService, programmingParticipantId, programmingParticipantName, sessionInfo);
			}
			public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return false;
			}
		};
		return workflowExploitService.createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), publicService, workflowMessage, participantCallback, sessionInfo);
	}
	
	/**
	 * 获取编程决定的办理人
	 * @param publicService
	 * @param programmingParticipantId
	 * @param programmingParticipantName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	protected  List retrieveProgrammingParticipants(com.yuanluesoft.cms.publicservice.pojo.PublicService publicService, String programmingParticipantId, String programmingParticipantName, SessionInfo sessionInfo) throws ServiceException {
		if("siteEditor".equals(programmingParticipantId)) {
			return siteService.listSiteEditors(publicService.getSiteId(), false, false, 0);
		}
		else if("siteManager".equals(programmingParticipantId)) {
			return siteService.listSiteManagers(publicService.getSiteId(), false, false, 0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureRecordService#processCapturedRecord(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, com.yuanluesoft.cms.capture.model.CaptureRecord)
	 */
	public void processCapturedRecord(CmsCaptureTask captureTask, CapturedRecord capturedRecord) throws ServiceException {
		//处理抓取到的信息
		com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)capturedRecord.getRecord();
		if(publicService.getSubject()==null || publicService.getSubject().isEmpty()) {
			publicService.setSubject(publicService.getPublicSubject());
			if(publicService.getSubject()==null || publicService.getSubject().isEmpty()) { //没有主题,不处理
				throw new ServiceException("subject is empty");
			}
		}
		if(publicService.getCreated()==null) {
			throw new ServiceException("created is empty"); //创建时间不能为空
		}
		publicService.setPublicPass('1'); //允许公开
		publicService.setPublicBody('1'); //公开正文
		publicService.setPublicWorkflow('1'); //公开办理流程
		publicService.setSiteId(StringUtils.getPropertyLongValue(captureTask.getExtendedParameters(), "relationSiteId", 0)); //站点ID
		//保存主记录
		save(publicService);

		//添加查看权限
		List visitors = siteService.listSiteManagers(publicService.getSiteId(), false, false, 0);
		List editors = siteService.listSiteEditors(publicService.getSiteId(), false, false, 0);
		if(visitors==null) {
			visitors = new ArrayList();
		}
		if(editors!=null) {
			visitors.addAll(editors);
		}
		if(recordControlService==null) {
			recordControlService = (RecordControlService)Environment.getService("recordControlService");
		}
		for(Iterator iterator = visitors.iterator(); iterator.hasNext();) {
			WebDirectoryPopedom popedom = (WebDirectoryPopedom)iterator.next();
			recordControlService.appendVisitor(publicService.getId(), com.yuanluesoft.cms.publicservice.pojo.PublicService.class.getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
		}

		//保存办理意见
		if(publicService.getOpinions()!=null && !publicService.getOpinions().isEmpty()) {
			for(Iterator iterator = publicService.getOpinions().iterator(); iterator.hasNext();) {
				PublicServiceOpinion opinion = (PublicServiceOpinion)iterator.next();
				if(opinion.getOpinion()!=null && !opinion.getOpinion().isEmpty()) {
					if(opinion.getCreated()==null) {
						opinion.setCreated(DateTimeUtils.now());
					}
					getDatabaseService().saveRecord(opinion);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.PublicService#modifyReaders(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedIds, String modifyMode, boolean selectedOnly, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String[] userIds = null;
		if(!"synchSiteVisitor".equals(modifyMode)) { //不是同步站点访问者
			if(readerIds==null || readerIds.isEmpty()) { //用户ID为空
				return;
			}
			userIds = readerIds.split(",");
		}
		if(selectedOnly) { //选中的文章
			if(selectedIds==null || selectedIds.isEmpty()) {
				return;
			}
			List ids = ListUtils.generateList(selectedIds, ",");
			String pojoName = view.getPojoClassName();
			pojoName = pojoName.substring(pojoName.lastIndexOf('.') + 1);
			for(int i=0; i<ids.size() ; i+=100) {
				String hql = "from " + pojoName + " " + pojoName +
							 " where " + pojoName + ".id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(ids.subList(i, Math.min(i+100, ids.size())), ",", false)) + ")";
				List resources = getDatabaseService().findRecordsByHql(hql, i, 100);
				doModifyReaders(resources, modifyMode, userIds);
			}
			return;
		}
		try {
			view = (View)view.clone();
		}
		catch(CloneNotSupportedException e) {
			
		}
		view.setFilter(null); //不过滤,处理所有的记录
		view.setPageRows(100);
		ViewService viewService = ViewUtils.getViewService(view);
		//构造视图包
		ViewPackage viewPackage = new ViewPackage();
		viewPackage.setView(view);
		viewPackage.setSearchConditions(searchConditions);
		viewPackage.setSearchMode(searchConditions!=null);
		viewPackage.setCategories(currentCategories);
		for(int page=1; ; page++) {
			//设置当前页
			viewPackage.setCurPage(page);
			try {
				viewService.retrieveViewPackage(viewPackage, view, 0, true, false, false, request, sessionInfo);
			}
			catch (PrivilegeException e) {
				
			}
			//获取记录
			if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
				break;
			}
			//更新访问者
			doModifyReaders(viewPackage.getRecords(), modifyMode, userIds);
			if(page>=viewPackage.getPageCount()) {
				break;
			}
		}
	}
	
	/**
	 * 修改记录的访问者
	 * @param resources
	 * @param modifyMode
	 * @param deleteNotColumnVisitor
	 * @param readerIds
	 * @throws ServiceException
	 */
	private void doModifyReaders(List resources, String modifyMode, String[] readerIds) throws ServiceException {
		for(Iterator iterator = resources.iterator(); iterator.hasNext(); ) {
			com.yuanluesoft.cms.publicservice.pojo.PublicService record = (com.yuanluesoft.cms.publicservice.pojo.PublicService)iterator.next();
			if("addUser".equals(modifyMode)) { //添加用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.appendVisitor(record.getId(), record.getClass().getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("deleteUser".equals(modifyMode)) { //删除用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.removeVisitor(record.getId(), record.getClass().getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("synchSiteVisitor".equals(modifyMode)) { //同步栏目访问者
				//获取访问者
				String hql = "from WebDirectoryPopedom WebDirectoryPopedom where WebDirectoryPopedom.directoryId=" + record.getSiteId();
				List columnPopedoms = getDatabaseService().findRecordsByHql(hql);
				if(columnPopedoms==null || columnPopedoms.isEmpty()) {
					continue;
				}
				for(Iterator iteratorPopedom = columnPopedoms.iterator(); iteratorPopedom.hasNext();) {
					WebDirectoryPopedom popedom = (WebDirectoryPopedom)iteratorPopedom.next();
					recordControlService.appendVisitor(record.getId(), record.getClass().getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableServiceListener#onWorkDayChanged(java.sql.Date, java.sql.Date)
	 */
	public void onWorkDayChanged(Date beginDate, Date endDate) throws ServiceException {
		if(beginDate==null) {
			return;
		}
		String hql = "from PublicService PublicService" +
					 " where PublicService.workingDate>DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 (endDate==null ? "" : " and PublicService.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
					 " order by PublicService.id";
		for(int i=0; ; i+=200) {
			List publicServices = getDatabaseService().findRecordsByHql(hql, i, 200); //每次处理200条记录
			for(Iterator iterator = publicServices==null ? null : publicServices.iterator(); iterator!=null && iterator.hasNext();) {
				com.yuanluesoft.cms.publicservice.pojo.PublicService publicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)iterator.next();
				try {
					Timestamp workingDate = timetableService.addWorkDays(publicService.getCreated(), publicService.getWorkingDay(), 0);
					if(!workingDate.equals(publicService.getWorkingDate())) {
						publicService.setWorkingDate(workingDate);
						getDatabaseService().updateRecord(publicService);
					}
				}
				catch (Exception e) {
					Logger.exception(e);
				}
			}
			if(publicServices==null || publicServices.size()<200) {
				break;
			}
		}
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the smsService
	 */
	public SmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}

	/**
	 * @return the forceValidateCode
	 */
	public boolean isForceValidateCode() {
		return forceValidateCode;
	}

	/**
	 * @param forceValidateCode the forceValidateCode to set
	 */
	public void setForceValidateCode(boolean forceValidateCode) {
		this.forceValidateCode = forceValidateCode;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
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