package com.yuanluesoft.municipal.facilities.service.spring;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.XMLType;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.pojo.WorkItem;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.municipal.facilities.pdaservice.model.EventEntity;
import com.yuanluesoft.municipal.facilities.pdaservice.model.SzcgEventReportParameter;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValiateProDepartHandleResultResult;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValidateEvent;
import com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent;
import com.yuanluesoft.municipal.facilities.pojo.FacilitiesEventParameter;
import com.yuanluesoft.municipal.facilities.pojo.PdaUser;
import com.yuanluesoft.municipal.facilities.service.FacilitiesService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 *
 */
public class FacilitiesServiceImpl extends BusinessServiceImpl implements FacilitiesService {
	private SessionService sessionService;
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private SoapConnectionPool soapConnectionPool; //SOAP链接池,获取上报的附件
	private SoapPassport facilitiesSoapPassport;
	private String facilitiesAttachmentPath = "E:/RSDATA/";
	private AttachmentService attachmentService; //附件管理服务
	private int photoSynchDelay = 60; //照片同步延时
	private NumerationService numerationService; //编号服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#getEvent(long)
	 */
	public FacilitiesEvent getEvent(long id) throws ServiceException {
		return (FacilitiesEvent)load(FacilitiesEvent.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#reportEvent(com.yuanluesoft.municipal.facilities.pdaservice.model.SzcgEventReportParameter)
	 */
	public void reportEvent(SzcgEventReportParameter entity) throws ServiceException {
		String workflowInstanceId = null;
		SessionInfo sessionInfo = null;
		try {
			//获取流程入口
			sessionInfo = sessionService.getSessionInfo(SessionService.ANONYMOUS);
			final EventEntity eventEntity =  entity.eventBody;
			sessionInfo.setUserName(eventEntity.ObserverName==null ? "PDA" : eventEntity.ObserverName);
			
			List workflowEntries = workflowExploitService.listWorkflowEntries("municipal/facilities", null, sessionInfo);
			WorkflowEntry workflowEntry = (WorkflowEntry)workflowEntries.get(0);
			
			//创建市政设施监控事件
			final FacilitiesEvent event = new FacilitiesEvent();
			event.setId(UUIDLongGenerator.generateId());
			event.setSource("PDA承办"); //案件来源,信息采集员/PDA即办/PDA承办/12345诉求/12345来电/110联动/公众举报/短息举报/领导批办
			event.setZone(eventEntity.GridName); //区域
			event.setObserver(eventEntity.ObserverName); //上报人
			event.setObserverNumber(eventEntity.ObserverPdaNum); //上报号码
			event.setReporter(eventEntity.Reporter); //举报人
			event.setContect(eventEntity.ContactTel); //联系方式
			event.setIsReceipt(eventEntity.IsReceipt ? '1' : '0'); //是否需回复
			event.setReceiptMode(eventEntity.ContactWay); //回复方式,电话回复/短信回复
			//event.setReceiptTo(receiptTo); //回复对象
			//event.setRecorder(recorder); //接线员
			//event.setPartCode(partCode); //部件编号
			//event.setTimeLimit(timeLimit); //处理时限
			event.setLevel(eventEntity.ImportantName==null ? "一般性案件" : eventEntity.ImportantName); //案件等级,一般性案件/重大案件
			event.setCategory(eventEntity.BigclassName); //案件分类
			event.setChildCategory(eventEntity.SmallclassName); //案件子分类
			switch(eventEntity.DuplicateCase) {
			case 0:
				event.setDuplicate("非重复案件"); //重复案件,非重复案件/1次复案件/2次复案件/3次及以上重复案件
				break;
			
			case 1:
				event.setDuplicate("1次复案件"); //重复案件,非重复案件/1次复案件/2次复案件/3次及以上重复案件
				break;
				
			case 2:
				event.setDuplicate("2次复案件"); //重复案件,非重复案件/1次复案件/2次复案件/3次及以上重复案件
				break;
				
			default:
				event.setDuplicate("3次及以上重复案件"); //重复案件,非重复案件/1次复案件/2次复案件/3次及以上重复案件
			}
			event.setXPos(eventEntity.XPos); //定位结果X
			event.setYPos(eventEntity.YPos); //定位结果Y
			event.setPosition(eventEntity.PositionDesc); //事发位置
			event.setDescription(eventEntity.ProbDesc); //案件描述
			event.setRemark(eventEntity.Remark); //备注
			//event.setcreator; //登记人
			//event.setcreatorId; //登记人ID
			event.setCreated(DateTimeUtils.now()); //登记时间
			event.setEventNumber(generateEventNumber(event, false));
			
			//创建流程实例并发送
			Element activity = (Element)workflowEntry.getActivityEntries().get(0);
			WorkflowMessage workflowMessage = new WorkflowMessage("设施监控案件", event.getDescription(), null);
			workflowInstanceId = workflowExploitService.createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), event, workflowMessage, null, sessionInfo);
			
			//保存事件
			event.setWorkflowInstanceId(workflowInstanceId);
			getDatabaseService().saveRecord(event);
			
			//同步更新照片
			synchPhotos(event.getId(), "images", eventEntity.Code);
		}
		catch(Exception e) {
			try {
				workflowExploitService.removeWorkflowInstance(workflowInstanceId, null, sessionInfo);
			}
			catch(Exception we) {
				
			}
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 同步更新照片
	 * @param eventId
	 * @param photoType
	 * @param eventCode
	 */
	private void synchPhotos(final long eventId, final String photoType, final String eventCode) {
		//等待60秒后获取附件
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					for(int j=0; j<3; j++) { //最多同步3次
						String attachmentNames = null;
						if("images".equals(photoType)) { //事前照片
							if(Logger.isDebugEnabled()) {
								Logger.debug("FacilitiesService: synch photos for case " + eventCode);
							}
							attachmentNames = (String)soapConnectionPool.invokeRemoteMethod("PDAService.asmx", "GetEventAttachInfo", XMLType.XSD_STRING, null, facilitiesSoapPassport, new Object[]{eventCode}, new String[]{"eventId"}, new QName[]{XMLType.XSD_STRING}, null, null);
						}
						else if("processImages".equals(photoType)) { //处理后照片
							if(Logger.isDebugEnabled()) {
								Logger.debug("FacilitiesService: synch fixed photos for event that id is " + eventId);
							}
							attachmentNames = (String)soapConnectionPool.invokeRemoteMethod("PDAService.asmx", "GetEventDealAttachInfo", XMLType.XSD_STRING, null, facilitiesSoapPassport, new Object[]{"" + eventId}, new String[]{"eventId"}, new QName[]{XMLType.XSD_STRING}, null, null);
						}
						if(Logger.isDebugEnabled()) {
							Logger.debug("FacilitiesService: synch photos, names are " + attachmentNames);
						}
						if(attachmentNames!=null && !attachmentNames.equals("")) {
							String[] fileNames = attachmentNames.split(",");
							for(int i=0; i<fileNames.length; i++) {
								attachmentService.uploadFile("municipal/facilities", photoType, null, eventId, facilitiesAttachmentPath + fileNames[i] + ".jpg");
							}
							break;
						}
						Thread.sleep(photoSynchDelay * 1000); //再等待一个周期
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		};
		timer.schedule(task, photoSynchDelay * 1000);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#listPdaUsers(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listPdaUsers(SessionInfo sessionInfo) throws ServiceException {
		//TODO:只获取当前用户所在单位的
		List users = getDatabaseService().findRecordsByHql("from PdaUser PdaUser order by PdaUser.name");
		if(users==null || users.isEmpty()) {
			return null;
		}
		//获取登录情况,并转换为PdaUser模型
		for(int i=0; i<users.size(); i++) {
			PdaUser pdaUser = (PdaUser)users.get(i);
			pdaUser.setLogin(isPadUserLogin(pdaUser.getCode()));
			pdaUser.setGpsLogin(isPadUserGPSLogin(pdaUser.getCode()));
		}
		Collections.sort(users, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				PdaUser pdaUser0 = (PdaUser)arg0;
				PdaUser pdaUser1 = (PdaUser)arg1;
				if(pdaUser0.isGpsLogin() && !pdaUser1.isGpsLogin()) {
					return -1;
				}
				else if(!pdaUser0.isGpsLogin() && pdaUser1.isGpsLogin()) {
					return 1;
				}
				else if(pdaUser0.isLogin() && !pdaUser1.isLogin()) {
					return -1;
				}
				else if(!pdaUser0.isLogin() && pdaUser1.isLogin()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
		return users;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#isPadUserLogin(java.lang.String)
	 */
	public boolean isPadUserLogin(String userCode) throws ServiceException {
		try {
			return ((Boolean)soapConnectionPool.invokeRemoteMethod("PDAService.asmx", "CheckObserverStatus", XMLType.XSD_BOOLEAN, null, facilitiesSoapPassport, new Object[]{userCode}, new String[]{"pdaLoginName"}, new QName[]{XMLType.XSD_STRING}, null, null)).booleanValue();
		}
		catch (Exception e) {
			Logger.exception(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#isPadUserGPSLogin(java.lang.String)
	 */
	public boolean isPadUserGPSLogin(String userCode) throws ServiceException {
		try {
			return ((Boolean)soapConnectionPool.invokeRemoteMethod("PDAService.asmx", "CheckObserverGPSStatus", XMLType.XSD_BOOLEAN, null, facilitiesSoapPassport, new Object[]{userCode}, new String[]{"pdaLoginName"}, new QName[]{XMLType.XSD_STRING}, null, null)).booleanValue();
		}
		catch (Exception e) {
			Logger.exception(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#countPdaUserTasks(java.lang.String)
	 */
	public int countPdaUserTasks(String pdaUserCode) throws ServiceException {
		SessionInfo sessionInfo = getPdaUserSessionInfoByCode(pdaUserCode);
		return getDatabaseService().countTodoRecords(FacilitiesEvent.class.getName(), null, null, null, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#listPdaUserTasks(java.lang.String, int, int)
	 */
	public List listPdaUserTasks(String pdaUserCode, int pageIndex, int pageSize) throws ServiceException {
		SessionInfo sessionInfo = getPdaUserSessionInfoByCode(pdaUserCode);
		return getDatabaseService().findTodoRecords(FacilitiesEvent.class.getName(), null, null, null, "FacilitiesEvent.created DESC", null, null, (pageIndex-1) * pageSize, pageSize, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#completePdaValidateResult(com.yuanluesoft.municipal.facilities.pdaservice.model.ValiateProDepartHandleResultResult)
	 */
	public void completePdaValidateResult(ValiateProDepartHandleResultResult validateResult) throws ServiceException {
		//获取事件
		FacilitiesEvent facilitiesEvent = getEvent(Long.parseLong(validateResult.MessageId));
		//回填验证结果
		facilitiesEvent.setFixed(validateResult.IsTrue ? '1' : '0');
		facilitiesEvent.setFixDescription(validateResult.Comment);
		completePdaValidate(facilitiesEvent, validateResult.Comment, validateResult.PDAUserCode);
		getDatabaseService().updateRecord(facilitiesEvent);
		//同步更新照片
		synchPhotos(facilitiesEvent.getId(), "processImages", null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#completePdaValidateTruth(com.yuanluesoft.municipal.facilities.pdaservice.model.ValidateEvent)
	 */
	public void completePdaValidateTruth(ValidateEvent validateResult) throws ServiceException {
		//获取事件
		FacilitiesEvent facilitiesEvent = getEvent(Long.parseLong(validateResult.MessageId));
		//回填验证结果
		facilitiesEvent.setIsTruly(validateResult.IsTrue ? '1' : '0');
		facilitiesEvent.setTruthDescription(validateResult.Comment);
		completePdaValidate(facilitiesEvent, validateResult.Comment, validateResult.PDAUserCode);
		getDatabaseService().updateRecord(facilitiesEvent);
		//同步更新照片
		synchPhotos(facilitiesEvent.getId(), "processImages", null);
	}
	
	/**
	 * 完成PDA验证
	 * @param facilitiesEvent
	 * @param transactLog
	 * @param pdaUserCode
	 * @throws ServiceException
	 */
	private void completePdaValidate(FacilitiesEvent facilitiesEvent, String transactLog, String pdaUserCode) throws ServiceException {
		//获取PDA用户会话
		SessionInfo sessionInfo = getPdaUserSessionInfoByCode(pdaUserCode);
		//获取流程出口
		WorkItem workItem = (WorkItem)ListUtils.findObjectByProperty(facilitiesEvent.getWorkItems(), "participantId", new Long(sessionInfo.getUserId()));
		workflowExploitService.lockWorkflowInstance(facilitiesEvent.getWorkflowInstanceId(), sessionInfo);
		WorkflowExit workflowExit = workflowExploitService.getWorkflowExit(facilitiesEvent.getWorkflowInstanceId(), workItem.getWorkItemId(), false, facilitiesEvent, null, null, sessionInfo);
		workflowExploitService.writeTransactLog(facilitiesEvent.getWorkflowInstanceId(), workItem.getWorkItemId(), transactLog, sessionInfo);
		//完成办理
		workflowExploitService.completeWorkItem(facilitiesEvent.getWorkflowInstanceId(), workItem.getWorkItemId(), false, null, workflowExit, facilitiesEvent, null, null, sessionInfo);
		workflowExploitService.unlockWorkflowInstance(facilitiesEvent.getWorkflowInstanceId(), sessionInfo);
	}

	/**
	 * 获取PDA用户会话
	 * @param pdaUserCode
	 * @return
	 * @throws ServiceException
	 */
	private SessionInfo getPdaUserSessionInfoByCode(String pdaUserCode) throws ServiceException {
		String hql = "from PdaUser PdaUser" +
					 " where PdaUser.code='" + JdbcUtils.resetQuot(pdaUserCode) + "'";
		PdaUser pdaUser = (PdaUser)getDatabaseService().findRecordByHql(hql);
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setLoginName(pdaUser.getName()); //登录用户名
		sessionInfo.setPassword(null); //密码
		sessionInfo.setUserName(pdaUser.getName()); //用户名
		sessionInfo.setUserId(pdaUser.getId()); //用户ID
		sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER); //用户类型,学生/家长/老师/普通用户
		sessionInfo.setDepartmentId(0); //所属部门ID
		sessionInfo.setDepartmentName(null); //所属部门名称
		sessionInfo.setDepartmentIds("0"); //所属部门及上级部门ID,用逗号分隔
		sessionInfo.setUnitId(0); //所在单位ID
		sessionInfo.setRoleIds(null); //角色ID,用逗号分隔
		return sessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#loadEventParameter()
	 */
	public FacilitiesEventParameter loadEventParameter() throws ServiceException {
		return (FacilitiesEventParameter)getDatabaseService().findRecordByHql("from FacilitiesEventParameter FacilitiesEventParameter");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.municipal.facilities.service.FacilitiesService#generateEventNumber(com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent, boolean)
	 */
	public String generateEventNumber(FacilitiesEvent event, boolean preview) throws ServiceException {
		FacilitiesEventParameter eventParameter = loadEventParameter();
		return numerationService.generateNumeration("市政设施监控", "案件编号", (eventParameter==null ? "<年*4><月*2><日*2><序号*4>" : eventParameter.getEventNumberFormat()), preview, null);
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
	 * @return the facilitiesSoapPassport
	 */
	public SoapPassport getFacilitiesSoapPassport() {
		return facilitiesSoapPassport;
	}

	/**
	 * @param facilitiesSoapPassport the facilitiesSoapPassport to set
	 */
	public void setFacilitiesSoapPassport(SoapPassport facilitiesSoapPassport) {
		this.facilitiesSoapPassport = facilitiesSoapPassport;
	}

	/**
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the facilitiesAttachmentPath
	 */
	public String getFacilitiesAttachmentPath() {
		return facilitiesAttachmentPath;
	}

	/**
	 * @param facilitiesAttachmentPath the facilitiesAttachmentPath to set
	 */
	public void setFacilitiesAttachmentPath(String facilitiesAttachmentPath) {
		this.facilitiesAttachmentPath = facilitiesAttachmentPath;
	}
	
	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	
	/**
	 * @return the photoSynchDelay
	 */
	public int getPhotoSynchDelay() {
		return photoSynchDelay;
	}

	/**
	 * @param photoSynchDelay the photoSynchDelay to set
	 */
	public void setPhotoSynchDelay(int photoSynchDelay) {
		this.photoSynchDelay = photoSynchDelay;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}
}