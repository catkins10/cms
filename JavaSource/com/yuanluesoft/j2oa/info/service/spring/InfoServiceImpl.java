package com.yuanluesoft.j2oa.info.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoFilterPrivilege;
import com.yuanluesoft.j2oa.info.pojo.InfoInstruct;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineBody;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoReceive;
import com.yuanluesoft.j2oa.info.pojo.InfoRevise;
import com.yuanluesoft.j2oa.info.pojo.InfoSendHigher;
import com.yuanluesoft.j2oa.info.pojo.InfoWorkflow;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.j2oa.infocontribute.soap.InfoContributeSoapClient;
import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoContribute;
import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoReviseResult;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.LazyBody;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.document.model.RecordListChange;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 *
 */
public class InfoServiceImpl extends BusinessServiceImpl implements InfoService {
	private AttachmentService attachmentService; //附件服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private RecordControlService recordControlService; //记录控制服务
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证
	private RemoteDocumentService remoteDocumentService; //远程文档服务
	private int unitLevel; //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
	private String unitName; //单位名称,用于和专网对接
	
	//私有属性
	private long lastSynchTime; //最后同步时间
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof InfoReceive) {
			//获取退改稿结果
			InfoReceive infoReceive = (InfoReceive)record;
			InfoRevise revise = (InfoRevise)ListUtils.findObjectByProperty(infoReceive.getRevises(), "editorId", new Long(0));
			if(revise!=null) {
				getInfoReviseResult(revise, (LazyBody)infoReceive.getLazyBody().iterator().next());
			}
		}
		else if(record instanceof InfoFilter) {
			//获取退改稿结果
			InfoFilter info = (InfoFilter)record;
			InfoRevise revise = (InfoRevise)ListUtils.findObjectByProperty(info.getRevises(), "editorId", new Long(0));
			if(revise!=null) {
				getInfoReviseResult(revise, (LazyBody)info.getLazyBody().iterator().next());
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof InfoMagazine) { //刊物
			saveMagazine((InfoMagazine)record);
		}
		record = super.save(record);
		if(record instanceof InfoMagazineDefine) { //刊物配置
			InfoMagazineDefine magazineDefine = (InfoMagazineDefine)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addOrUpdateMagazineDefine(magazineDefine.getId(), magazineDefine.getName(), unitName);
		}
		else if(record instanceof InfoRevise) { //退改稿
			reviseInfo((InfoRevise)record);
		}
		else if(record instanceof InfoSendHigher) { //报送情况
			InfoSendHigher sendHigher = (InfoSendHigher)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addUsage(sendHigher.getId(), getReceiveInfoIds("" + sendHigher.getInfoId()), sendHigher.getLevel(), sendHigher.getSendTime(), sendHigher.getUseTime(), 0, sendHigher.getUseMagazine());
		}
		else if(record instanceof InfoInstruct) { //领导批示
			InfoInstruct instruct = (InfoInstruct)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addInstruct(instruct.getId(), getReceiveInfoIds("" + instruct.getInfoId()), instruct.getLeader(), instruct.getLevel(), instruct.getInstruct(), instruct.getInstructTime(), instruct.getCreatorId(), instruct.getCreator(), instruct.getCreated());
		}
		else if(record instanceof InfoFilter) { //信息,同步补录情况
			InfoFilter info = (InfoFilter)record;
			if(info.getSupplementTime()!=null) {
				new InfoContributeSoapClient(soapConnectionPool, soapPassport).addSupplementInfo(info.getInfoReceiveId(), info.getSubject(), info.getInfoReceive().getKeywords(), info.getMagazineName(), info.getMagazineSN(), unitLevel, info.getInfoReceive().getFromUnit(), info.getInfoReceive().getFromUnitId(), info.getBody(), info.getInfoReceive().getSecretLevel(), info.getSupplementTime());
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof InfoMagazineDefine) {
			InfoMagazineDefine magazineDefine = (InfoMagazineDefine)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addOrUpdateMagazineDefine(magazineDefine.getId(), magazineDefine.getName(), unitName);
		}
		else if(record instanceof InfoSendHigher) { //报送情况
			InfoSendHigher sendHigher = (InfoSendHigher)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addUsage(sendHigher.getId(), getReceiveInfoIds("" + sendHigher.getInfoId()), sendHigher.getLevel(), sendHigher.getSendTime(), sendHigher.getUseTime(), 0, sendHigher.getUseMagazine());
		}
		else if(record instanceof InfoInstruct) { //领导批示
			InfoInstruct instruct = (InfoInstruct)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).addInstruct(instruct.getId(), getReceiveInfoIds("" + instruct.getInfoId()), instruct.getLeader(), instruct.getLevel(), instruct.getInstruct(), instruct.getInstructTime(), instruct.getCreatorId(), instruct.getCreator(), instruct.getCreated());
		}
		else if(record instanceof InfoFilter) { //信息,同步补录情况
			InfoFilter info = (InfoFilter)record;
			if(info.getSupplementTime()!=null) {
				new InfoContributeSoapClient(soapConnectionPool, soapPassport).addSupplementInfo(info.getInfoReceiveId(), info.getSubject(), info.getInfoReceive().getKeywords(), info.getMagazineName(), info.getMagazineSN(), unitLevel, info.getInfoReceive().getFromUnit(), info.getInfoReceive().getFromUnitId(), info.getBody(), info.getInfoReceive().getSecretLevel(), info.getSupplementTime());
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof InfoMagazine) { //刊物
			InfoMagazine magazine = (InfoMagazine)record;
			//把采用稿件设置为待排版
			for(Iterator iterator = magazine.getUseInfos()==null ? null : magazine.getUseInfos().iterator(); iterator!=null && iterator.hasNext();) {
				InfoFilter info = (InfoFilter)iterator.next();
				updateInfoStatus(info, INFO_STATUS_TO_TYPESET, 0, 0, null, 0);
			}
		}
		super.delete(record);
		if(record instanceof InfoMagazineDefine) {
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).deleteMagazineDefine(record.getId());
		}
		else if(record instanceof InfoMagazine) { //刊物
			InfoMagazine magazine = (InfoMagazine)record;
			//检查刊物配置的期号,如果和当前刊物相同,则减一
			InfoMagazineDefine magazineDefine = (InfoMagazineDefine)load(InfoMagazineDefine.class, magazine.getDefineId());
			if(magazineDefine.getSn()==magazine.getSn() && magazineDefine.getSnTotal()==magazine.getSnTotal()) {
				magazineDefine.setSn(magazineDefine.getSn()-1);
				magazineDefine.setSnTotal(magazineDefine.getSnTotal()-1);
				update(magazineDefine);
			}
		}
		else if(record instanceof InfoSendHigher) { //报送情况
			InfoSendHigher sendHigher = (InfoSendHigher)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).deleteUsage(sendHigher.getId());
		}
		else if(record instanceof InfoInstruct) { //领导批示
			InfoInstruct instruct = (InfoInstruct)record;
			new InfoContributeSoapClient(soapConnectionPool, soapPassport).deleteInstruct(instruct.getId());
		}
		else if(record instanceof InfoFilter) { //信息,同步补录情况
			InfoFilter info = (InfoFilter)record;
			if(info.getSupplementTime()!=null) {
				new InfoContributeSoapClient(soapConnectionPool, soapPassport).deleteSupplementInfo(info.getInfoReceiveId());
			}
		}
	}
	
	/**
	 * 保存刊物
	 * @param magazine
	 * @throws ServiceException
	 */
	private synchronized void saveMagazine(InfoMagazine magazine)  throws ServiceException {
		InfoMagazineDefine magazineDefine = (InfoMagazineDefine)load(InfoMagazineDefine.class, magazine.getDefineId());
		magazine.setName(magazineDefine.getMagazineName()); //名称
		magazine.setSn(magazineDefine.getNextSn()); //期号
		magazine.setSnTotal(magazineDefine.getSnTotal() + 1); //总期号
		//更新刊物配置
		magazineDefine.setSn(magazine.getSn());
		magazineDefine.setSnTotal(magazine.getSnTotal());
		magazineDefine.setSnYear(DateTimeUtils.getYear(DateTimeUtils.date()));
		update(magazineDefine);
	}
	
	/**
	 * 退改稿
	 * @param infoRevise
	 * @throws ServiceException
	 */
	private void reviseInfo(InfoRevise infoRevise) throws ServiceException {
		//发送到专网
		long infoId;
		if(infoRevise.getIsReceive()==1) { //来稿
			infoId = infoRevise.getInfoId();
		}
		else { //拟采用
			String hql = "select InfoFilter.infoReceiveId" +
						 " from InfoFilter InfoFilter" +
						 " where InfoFilter.id=" + infoRevise.getInfoId();
			Number infoReceiveId = (Number)getDatabaseService().findRecordByHql(hql);
			if(infoReceiveId==null) {
				throw new ServiceException("来稿已删除,退改稿不能完成");
			}
			infoId = infoReceiveId.longValue();
		}
		new InfoContributeSoapClient(soapConnectionPool, soapPassport).reviseInfo(infoRevise.getId(), infoId, infoRevise.getReviseOpinion(), infoRevise.getRevisePersonId(), infoRevise.getRevisePerson(), infoRevise.getRevisePersonTel());
	}
	
	/**
	 * 获取来稿ID
	 * @param infoId
	 * @return
	 */
	private String getReceiveInfoIds(String infoIds) {
		String receiveInfoIds = null;
		String combineInfoIds = null;
		String hql = "select InfoFilter.infoReceiveId, InfoFilter.combineInfoIds" +
					 " from InfoFilter InfoFilter" +
					 " where InfoFilter.id in (" + infoIds + ")";
		List infos = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = infos==null ? null : infos.iterator(); iterator!=null && iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			if(values[1]==null || ((String)values[1]).isEmpty()) {
				receiveInfoIds = (receiveInfoIds==null ? "" : receiveInfoIds + ",") + values[0];
			}
			else {
				combineInfoIds = (combineInfoIds==null ? "" : combineInfoIds + ",") + values[1];
			}
		}
		if(combineInfoIds!=null) {
			hql = "select InfoFilter.infoReceiveId" +
				  " from InfoFilter InfoFilter" +
				  " where InfoFilter.id in (" + combineInfoIds + ")";
			receiveInfoIds = (receiveInfoIds==null ? "" : receiveInfoIds + ",") + ListUtils.join(getDatabaseService().findRecordsByHql(hql), ",", false);
		}
		return receiveInfoIds;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#synchContributeInfos()
	 */
	public void synchContributeInfos() throws ServiceException {
		if(System.currentTimeMillis()-lastSynchTime<30000) { //30秒前同步过,则不再同步
			return;
		}
		//获取最后的投稿时间
		String hql = "select max(InfoReceive.contributeTime) from InfoReceive InfoReceive where InfoReceive.contributeTime is not null";
		Timestamp lastContributeTime = (Timestamp)getDatabaseService().findRecordByHql(hql);
		InfoContributeSoapClient soapClient = new InfoContributeSoapClient(soapConnectionPool, soapPassport);
		for(int i=0; ; i+=1) { //每次获取1个信息
			InfoContribute[] infos = soapClient.listContributeInfos(unitName, lastContributeTime, i, 1);
			if(infos==null || infos.length==0) {
				break;
			}
			//检查是否已经同步过
			if(getDatabaseService().findRecordByHql("select InfoReceive.id from InfoReceive InfoReceive where InfoReceive.id=" + infos[0].getId())!=null) {
				continue;
			}
			//创建来稿记录
			InfoReceive infoReceive = new InfoReceive();
			try {
				PropertyUtils.copyProperties(infoReceive, infos[0]);
			} 
			catch(Exception e) {
				throw new ServiceException(e);
			}
			infoReceive.setContributeTime(DateTimeUtils.calendarToTimestamp(infos[0].getContributed()));
			save(infoReceive);
			//读取附件
			if(infos[0].getAttachmentFileNames()!=null && infos[0].getAttachmentFileNames().length>0) {
				String savePath = attachmentService.getSavePath("j2oa/info", "attachments", infoReceive.getId(), true);
				soapClient.readAttachments(infoReceive.getId(), infos[0].getAttachmentFileNames(), savePath);
			}
		}
		//获取退改稿修改结果
		long lastReviseId = 0;
		hql = "from InfoRevise InfoRevise" +
			  " where InfoRevise.editTime is null" +
			  " and InfoRevise.id>{lastReviseId}" +
			  " order by InfoRevise.id";
		for(;;) {
			List revises = getDatabaseService().findRecordsByHql(hql.replaceAll("\\{lastReviseId\\}", "" + lastReviseId), 0, 50); //每次处理50个信息
			for(Iterator iterator = revises==null ? null : revises.iterator(); iterator!=null && iterator.hasNext();) {
				InfoRevise revise = (InfoRevise)iterator.next();
				lastReviseId = revise.getId();
				getInfoReviseResult(revise, null);
			}
			if(revises==null || revises.size()<50) {
				break;
			}
		}
		//记录最后同步时间
		lastSynchTime = System.currentTimeMillis();
	}
	
	/**
	 * 获取退改稿修改结果
	 * @param revise
	 * @param sourceInfoBody
	 * @throws ServiceException
	 */
	private void getInfoReviseResult(InfoRevise revise, LazyBody sourceInfoBody) throws ServiceException {
		InfoReviseResult reviseResult = new InfoContributeSoapClient(soapConnectionPool, soapPassport).getInfoReviseResult(revise.getId());
		if(reviseResult==null) { //未修改完成
			return;
		}
		//更新退改稿记录
		revise.setNewBody(reviseResult.getNewBody()); //修改后的正文
		revise.setEditTime(DateTimeUtils.calendarToTimestamp(reviseResult.getEditTime())); //修改时间
		revise.setEditorId(reviseResult.getEditorId()); //修改人ID
		revise.setEditor(reviseResult.getEditor()); //修改人
		getDatabaseService().updateRecord(revise);
		//更新源稿件正文
		if(sourceInfoBody==null) {
			String hql;
			if(revise.getIsReceive()==1) {
				hql = "from InfoReceiveBody InfoReceiveBody where InfoReceiveBody.id=" + revise.getInfoId();
			}
			else {
				hql = "from InfoFilterBody InfoFilterBody where InfoFilterBody.id=" + revise.getInfoId();
			}
			sourceInfoBody = (LazyBody)getDatabaseService().findRecordByHql(hql);
		}
		sourceInfoBody.setBody(reviseResult.getNewBody());
		getDatabaseService().updateRecord(sourceInfoBody);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#filterInfo(com.yuanluesoft.j2oa.info.pojo.InfoReceive, java.lang.String[], java.lang.String, int, int, int, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void filterInfo(InfoReceive infoReceive, String[] magazineIds, String level, int isBrief, int isVerified, int isCircular, String opinion, SessionInfo sessionInfo) throws ServiceException {
		if(magazineIds==null || magazineIds.length==0) {
			return;
		}
		InfoWorkflow infoWorkflow = null; //流程配置
		WorkflowEntry workflowEntry = null; //流程入口
		for(int i=0; i<magazineIds.length; i++) {
			if(magazineIds[i].isEmpty()) {
				continue;
			}
			long magazineDefineId = Long.parseLong(magazineIds[i]);
			//检查是否已经过滤过
			if(ListUtils.findObjectByProperty(infoReceive.getInfoFilters(), "magazineDefineId", new Long(magazineDefineId))!=null) {
				continue;
			}
			//创建拟采用稿件记录
			InfoFilter infoFilter = new InfoFilter();
			infoFilter.setId(UUIDLongGenerator.generateId()); //ID
			infoFilter.setSubject(infoReceive.getSubject()); //主题
			infoFilter.setInfoReceiveId(infoReceive.getId()); //来稿ID
			infoFilter.setMagazineDefineId(magazineDefineId); //刊物配置ID
			String hql = "select InfoMagazineDefine.name, InfoMagazineDefine.hasBrief" +
						 " from InfoMagazineDefine InfoMagazineDefine" +
						 " where InfoMagazineDefine.id=" + magazineDefineId;
			Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
			infoFilter.setMagazineName((String)values[0]); //刊物名称
			infoFilter.setLevel(level); //采用级别,普通 优先
			infoFilter.setIsBrief(isBrief==1 && ((Number)values[1]).intValue()==1 ? 1 : 0); //是否简讯
			infoFilter.setIsVerified(isVerified); //是否核实
			infoFilter.setIsCircular(isCircular); //是否通报
			infoFilter.setIsCombined(0); //是否多条合一
			infoFilter.setIsBeCombined(0); //是否被合并
			infoFilter.setStatus(0); //状态,0/审核中,1/待排版,2/已排版
			infoFilter.setBody(infoReceive.getBody()); //正文
			save(infoFilter);
			//创建流程
			if(infoWorkflow==null) {
				infoWorkflow = getInfoWorkflow(WORKFLOW_TYPE_INFO);
				if(infoWorkflow==null) {
					throw new ServiceException("no workflow definition found");
				}
				workflowEntry = workflowExploitService.getWorkflowEntry("" + infoWorkflow.getWorkflowId(), null, infoFilter, sessionInfo);
				if(workflowEntry==null) {
					throw new ServiceException("no workflow entry found");
				}
			}
			WorkflowParticipantCallback participantCallback = new ParticipantCallback(magazineDefineId);
			Element activity = (Element)workflowEntry.getActivityEntries().get(0);
			WorkflowMessage workflowMessage = new WorkflowMessage("信息采编", infoReceive.getSubject(), Environment.getContextPath() + "/j2oa/info/info.shtml?act=edit&id=" + infoFilter.getId());
			try {
				infoFilter.setWorkflowInstanceId(workflowExploitService.createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), infoFilter, workflowMessage, participantCallback, sessionInfo));
				update(infoFilter);
			}
			catch(Exception e) {
				Logger.exception(e);
				workflowExploitService.removeWorkflowInstance(infoFilter.getWorkflowInstanceId(), infoFilter, sessionInfo);
				delete(infoFilter);
				throw new ServiceException(e);
			}
		}
		infoReceive.setFilterPersonId(sessionInfo.getUserId()); //筛选人ID
		infoReceive.setFilterPerson(sessionInfo.getUserName()); //筛选人
		infoReceive.setFilterTime(DateTimeUtils.now()); //筛选时间
		infoReceive.setFilterOpinion(opinion); //筛选意见
		update(infoReceive);
	}
	
	/**
	 * 工作流办理人回调
	 * @author linchuan
	 *
	 */
	private class ParticipantCallback implements WorkflowParticipantCallback {
		private long magazineDefineId;
		
		public ParticipantCallback(long magazineDefineId) {
			super();
			this.magazineDefineId = magazineDefineId;
		}

		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.pojo.WorkflowData, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
		 */
		public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			return recordControlService.isVisitor(magazineDefineId, InfoMagazineDefine.class.getName(), "magazineEditor".equals(programmingParticipantId) ? RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE, sessionInfo);
		}

		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.pojo.WorkflowData, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
		 */
		public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			return recordControlService.listVisitors(magazineDefineId, InfoMagazineDefine.class.getName(), "magazineEditor".equals(programmingParticipantId) ? RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE);
		}

		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback#resetParticipants(java.util.List, boolean, com.yuanluesoft.jeaf.workflow.pojo.WorkflowData, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
		 */
		public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			return participants;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getNextInfoId(com.yuanluesoft.j2oa.info.pojo.InfoReceive, boolean)
	 */
	public long getNextInfoId(InfoReceive infoReceive, boolean toFilterOnly) throws ServiceException {
		String hql = "select InfoReceive.id from InfoReceive InfoReceive" +
					 " where InfoReceive.contributeTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(infoReceive.getContributeTime(), null) + ")" +
					 " and InfoReceive.id!=" + infoReceive.getId() +
					 (toFilterOnly ? " and InfoReceive.filterTime is null" : "") +
					 " order by InfoReceive.contributeTime DESC";
		Number id = (Number)getDatabaseService().findRecordByHql(hql);
		return id==null ? 0 : id.longValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getPreviousReceiveInfoId(com.yuanluesoft.j2oa.info.pojo.InfoReceive, boolean)
	 */
	public long getPreviousReceiveInfoId(InfoReceive infoReceive, boolean toFilterOnly) throws ServiceException {
		String hql = "select InfoReceive.id from InfoReceive InfoReceive" +
					 " where InfoReceive.contributeTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(infoReceive.getContributeTime(), null) + ")" +
					 " and InfoReceive.id!=" + infoReceive.getId() +
					 (toFilterOnly ? " and InfoReceive.filterTime is null" : "") +
					 " order by InfoReceive.contributeTime";
		Number id = (Number)getDatabaseService().findRecordByHql(hql);
		return id==null ? 0 : id.longValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getReceiveInfoBody(long)
	 */
	public String getReceiveInfoBody(long infoReceiveId) throws ServiceException {
		String hql = "select InfoReceiveBody.body" +
					 " from InfoReceiveBody InfoReceiveBody" +
					 " where InfoReceiveBody.id=" + infoReceiveId;
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getFilterInfoBody(long)
	 */
	public String getFilterInfoBody(long infoFilterId) throws ServiceException {
		String hql = "select InfoFilterBody.body" +
				 	 " from InfoFilterBody InfoFilterBody" +
				 	 " where InfoFilterBody.id=" + infoFilterId;
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#useInfo(com.yuanluesoft.j2oa.info.pojo.InfoFilter)
	 */
	public void useInfo(InfoFilter info) throws ServiceException {
		if(info.getStatus()<InfoService.INFO_STATUS_TO_TYPESET) {
			info.setStatus(InfoService.INFO_STATUS_TO_TYPESET);
			update(info);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#listEditableMagazineDefines(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listEditableMagazineDefines(SessionInfo sessionInfo) throws ServiceException {
		return getDatabaseService().findPrivilegedRecords(InfoMagazineDefine.class.getName(), null, null, null, "InfoMagazineDefine.name", null, RecordControlService.ACCESS_LEVEL_READONLY, true, null, 0, 0, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#saveMagazineUseInfos(com.yuanluesoft.j2oa.info.pojo.InfoMagazine, java.lang.String, java.lang.String)
	 */
	public void saveMagazineUseInfos(InfoMagazine magazine, String magazineColumn, String magazineUseInfoIds) throws ServiceException {
		saveMagazineUseInfos(magazine, magazineColumn, magazineUseInfoIds, 2);
	}
	
	/**
	 * 保存刊物使用的稿件
	 * @param magazine
	 * @param magazineColumn
	 * @param magazineUseInfoIds
	 * @param infoType 0/非简讯, 1/简讯, 2/全部
	 * @throws ServiceException
	 */
	private void saveMagazineUseInfos(InfoMagazine magazine, String magazineColumn, String magazineUseInfoIds, int infoType) throws ServiceException {
		//更新采用的稿件
		Set useInfos = new LinkedHashSet();
		String[] infoIds = magazineUseInfoIds==null || magazineUseInfoIds.isEmpty() ? null : magazineUseInfoIds.split(";");
		for(int i=0; i<(infoIds==null ? 0 : infoIds.length); i++) {
			long infoId = Long.parseLong(infoIds[i]);
			InfoFilter info = (InfoFilter)getDatabaseService().findRecordById(InfoFilter.class.getName(), infoId);
			updateInfoStatus(info, INFO_STATUS_TYPESET, magazine.getId(), magazine.getSnTotal(), magazineColumn, infoIds.length - i);
			useInfos.add(info);
		}
		
		//获取原来采用的稿件
		Collection infos;
		if(magazineColumn==null || magazineColumn.isEmpty()) {
			infos = magazine.getUseInfos()==null ? null : new ArrayList(magazine.getUseInfos());
		}
		else {
			infos = ListUtils.getSubListByProperty(magazine.getUseInfos(), "magazineColumn", magazineColumn);
		}
		if(infoType!=2) {
			infos = ListUtils.getSubListByProperty(infos, "isBrief", new Integer(infoType));
		}
		
		//从使用记录中删除原来采用的稿件
		for(Iterator iterator = infos==null ? null : infos.iterator(); iterator!=null && iterator.hasNext();) {
			InfoFilter info = (InfoFilter)iterator.next();
			ListUtils.removeObjectByProperty(magazine.getUseInfos(), "id", new Long(info.getId()));
		}
		
		//添加新的使用记录
		if(magazine.getUseInfos()==null) {
			magazine.setUseInfos(useInfos);
		}
		else {
			magazine.getUseInfos().addAll(useInfos);
		}
		
		//把弃用稿件设为待排版
		List removedInfos =	ListUtils.getNotInsideSubList(useInfos, "id", infos, "id");
		for(Iterator iterator = removedInfos==null ? null : removedInfos.iterator(); iterator!=null && iterator.hasNext();) {
			InfoFilter info = (InfoFilter)iterator.next();
			updateInfoStatus(info, INFO_STATUS_TO_TYPESET, 0, 0, null, 0);
		}
	}
	
	/**
	 * 更新稿件状态
	 * @param info
	 * @param status
	 * @param magazineId
	 * @param magazineSN
	 * @param magazineColumn
	 * @param priority
	 */
	private void updateInfoStatus(InfoFilter info, int status, long magazineId, int magazineSN, String magazineColumn, double priority) {
		int oldStatus = info.getStatus();
		if(oldStatus==status && //状态没有变化
		  info.getPriority()==priority && //优先级没有变化
		  info.getMagazineId()==magazineId && //刊物ID没有变化
		  info.getMagazineSN()==magazineSN && //刊物期号没有变化
		  (magazineColumn==null ? "" : magazineColumn).equals(info.getMagazineColumn()==null ? "" : info.getMagazineColumn())) { //所属栏目没有变化
			return;
		}
		//更新记录
		info.setStatus(status); //状态
		info.setPriority(priority); //优先级
		info.setMagazineId(magazineId); //刊物ID
		info.setMagazineSN(magazineSN); //期数
		info.setMagazineColumn(magazineColumn); //栏目
		if(status==INFO_STATUS_ISSUE && info.getIssueTime()==null) { //已定版
			info.setIssueTime(DateTimeUtils.now());
		}
		getDatabaseService().updateRecord(info);
		
		//更新被合并的稿件
		List combineInfos = info.getIsCombined()==0 ? null : getDatabaseService().findRecordsByHql("from InfoFilter InfoFilter where InfoFilter.id in (" + info.getCombineInfoIds() + ")");
		for(Iterator iterator = combineInfos==null ? null : combineInfos.iterator(); iterator!=null && iterator.hasNext();) {
			InfoFilter combinedInfo = (InfoFilter)iterator.next();
			updateInfoStatus(combinedInfo, status, magazineId, magazineSN, magazineColumn, priority);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#listToTypesetInfos(long, boolean, int)
	 */
	public List listToTypesetInfos(long magazineDefineId, boolean isBrief, int limit) throws ServiceException {
		String hql = "from InfoFilter InfoFilter" +
					 " where InfoFilter.magazineDefineId=" + magazineDefineId +
					 " and InfoFilter.status=" + INFO_STATUS_TO_TYPESET +
					 " and InfoFilter.isBrief=" + (isBrief ? 1 : 0) +
					 " and InfoFilter.isBeCombined=0" +
					 " order by InfoFilter.subject";
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("lazyBody"), 0, limit);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#saveMagazineBody(com.yuanluesoft.j2oa.info.pojo.InfoMagazine, javax.servlet.http.HttpServletRequest)
	 */
	public void saveMagazineBody(final InfoMagazine magazine, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新正文
				remoteDocumentService.updateRecordFile(documentPath, magazine, "body", magazine.getName() + ".doc");
				//更新正式文件
				if(officalDocumentPath!=null) {
					remoteDocumentService.updateRecordFile(officalDocumentPath, magazine, "official", magazine.getName() + ".doc");
				}
				//更新刊物采用的稿件
				for(Iterator iterator = recordListChanges==null ? null : recordListChanges.iterator(); iterator!=null && iterator.hasNext();) {
					RecordListChange recordListChange = (RecordListChange)iterator.next();
					saveMagazineUseInfos(magazine, recordListChange.getParentRecordId(), recordListChange.getRecordIds().replace(',', ';'), recordListChange.getRecordListName().endsWith("columnUseBriefs") ? 1 : 0);
				}
				//读取HTML内容
				String htmlBody = remoteDocumentService.retrieveRecordHtmlBody(htmlPagePath, htmlFilesPath, pageWidth, magazine, "html", Environment.getContextPath() + "/j2oa/info/downloadAttachment.shtml?id=" + magazine.getId() + "&fileName=$1");
				//保存html正文
		    	InfoMagazineBody magazineBody = (InfoMagazineBody)getDatabaseService().findRecordByKey(InfoMagazineBody.class.getName(), "id", new Long(magazine.getId()));
		    	if(magazineBody!=null) {
		    		magazineBody.setBody(htmlBody);
				    getDatabaseService().updateRecord(magazineBody);
			    }
			    else { //新文档
			    	magazineBody = new InfoMagazineBody();
			    	magazineBody.setId(magazine.getId());
			    	magazineBody.setBody(htmlBody);
				    getDatabaseService().saveRecord(magazineBody);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#issueMagazine(com.yuanluesoft.j2oa.info.pojo.InfoMagazine, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issueMagazine(InfoMagazine magazine, SessionInfo sessionInfo) throws ServiceException {
		//设置刊物的定版时间
		magazine.setGenerateDate(DateTimeUtils.date()); //生成日期
		magazine.setIssuePersonId(sessionInfo.getUserId()); //定版人ID
		magazine.setIssuePerson(sessionInfo.getUserName()); //定版人
		magazine.setIssueTime(DateTimeUtils.now()); //定版时间
		update(magazine);
		//授权给在分发范围内的用户
		recordControlService.copyVisitors(magazine.getId(), magazine.getId(), RecordControlService.ACCESS_LEVEL_PREREAD, RecordControlService.ACCESS_LEVEL_READONLY, InfoMagazine.class.getName());
		//将采用稿件设置为已定版
		for(Iterator iterator = magazine.getUseInfos()==null ? null : magazine.getUseInfos().iterator(); iterator!=null && iterator.hasNext();) {
			InfoFilter info = (InfoFilter)iterator.next();
			updateInfoStatus(info, INFO_STATUS_ISSUE, magazine.getId(), magazine.getSnTotal(), info.getMagazineColumn(), info.getPriority());
		}
		//获取刊物分发范围
		List issueRange = recordControlService.listVisitors(magazine.getId(), InfoMagazine.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		//同步发布到专网
		InfoContributeSoapClient client = new InfoContributeSoapClient(soapConnectionPool, soapPassport);
		String visitorIds = issueRange==null || issueRange.isEmpty() ? "0" : ListUtils.join(issueRange, "visitorId", ",", false);
		String useInfoIds = getReceiveInfoIds(ListUtils.join(magazine.getUseInfos(), "id", ",", false));
		client.addMagazine(magazine.getId(), magazine.getName(), magazine.getIssueTime(), magazine.getSn(), magazine.getSnTotal(), unitLevel, useInfoIds, visitorIds);
		//发送刊物文件
		List attachments = attachmentService.list("j2oa/info", "official", magazine.getId(), false, 0, null);
		client.uploadMagazineFile(magazine.getId(), ((Attachment)attachments.get(0)).getFilePath());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getRevisePersonTel(long)
	 */
	public String getRevisePersonTel(long userId) throws ServiceException {
		String hql = "select InfoRevise.revisePersonTel" +
					 " from InfoRevise InfoRevise" +
					 " where InfoRevise.revisePersonId=" + userId +
					 " order by InfoRevise.reviseTime DESC";
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#combineInfos(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public InfoFilter combineInfos(String infoIds, SessionInfo sessionInfo) throws ServiceException {
		List infos = getDatabaseService().findRecordsByHql("from InfoFilter InfoFilter where InfoFilter.id in (" + infoIds + ")", ListUtils.generateList("lazyBody,visitors", ","));
		if(ListUtils.findObjectByProperty(infos, "isBeCombined", new Integer(1))!=null || ListUtils.findObjectByProperty(infos, "isCombined", new Integer(1))!=null) {
			throw new ServiceException("已经合并过，不允许多次合并");
		}
		infos = ListUtils.sortByProperty(infos, "id", infoIds);
		//合并字段
		String subject = ListUtils.join(infos, "subject", "　", true); //标题
		String body = ListUtils.join(infos, "body", "\r\n\r\n", false); //正文
		String fromUnit = ListUtils.join(infos, "infoReceive.fromUnit", "　", true); //来稿单位
		String editor = ListUtils.join(infos, "infoReceive.editor", "　", true);
		String editorTel = ListUtils.join(infos, "infoReceive.editorTel", "　", true);
		String signer = ListUtils.join(infos, "infoReceive.signer", "　", true);
		String signerTel = ListUtils.join(infos, "infoReceive.signerTel", "　", true);
		if(subject.length()>100) {
			subject = subject.substring(0, 100);
		}
		if(fromUnit.length()>100) {
			fromUnit = fromUnit.substring(0, 100);
		}
		if(editor.length()>25) {
			editor = editor.substring(0, 25);
		}
		if(editorTel.length()>50) {
			editorTel = editorTel.substring(0, 50);
		}
		if(signer.length()>25) {
			signer = signer.substring(0, 25);
		}
		if(signerTel.length()>50) {
			signerTel = signerTel.substring(0, 50);
		}
		
		//设为已合并,并挂起流程
		for(int i=0; i<infos.size(); i++) {
			InfoFilter info = (InfoFilter)infos.get(i);
			info.setIsBeCombined(1); //是否被合并
			getDatabaseService().updateRecord(info);
			//挂起流程
			workflowExploitService.suspendWorkflowInstance(info.getWorkflowInstanceId(), info, sessionInfo);
		}
		
		InfoFilter info;
		InfoReceive infoReceive;
		try {
			info = (InfoFilter)((InfoFilter)infos.get(0)).clone();
			infoReceive = (InfoReceive)info.getInfoReceive().clone();
		} 
		catch (CloneNotSupportedException e) {
			throw new ServiceException(e);
		}
		infoReceive.setId(UUIDLongGenerator.generateId()); //ID
		//重建来稿记录
		infoReceive.setSubject(subject); //标题
		infoReceive.setFromUnit(fromUnit); //来稿单位名称
		infoReceive.setFromUnitId(0); //来稿单位ID
		infoReceive.setBody(body); //正文
		infoReceive.setInfoFilters(null); //过滤后的信息列表
		infoReceive.setRevises(null); //退改稿记录
		infoReceive.setEditor(editor); //责任编辑
		infoReceive.setEditorTel(editorTel); //电话
		infoReceive.setSigner(signer); //签发领导
		infoReceive.setSignerTel(signerTel); //签发领导电话
		infoReceive.setCreatorId(sessionInfo.getUserId()); //创建人ID
		infoReceive.setCreator(sessionInfo.getUserName()); //创建人
		infoReceive.setContributeTime(null); //清空投稿时间
		save(infoReceive);
		
		//重建稿件记录
		info.setId(UUIDLongGenerator.generateId());
		info.setCombineInfoIds(infoIds); //被合并的稿件ID
		info.setInfoReceiveId(infoReceive.getId()); //来稿ID
		info.setInfoReceive(infoReceive); //来稿
		info.setIsCombined(1); //是否多条合一
		info.setIsBeCombined(0); //是否被合并
		info.setSubject(subject); //标题
		info.setBody(body); //正文
		info.setSendHighers(null); //报送情况
		info.setInstructs(null); //领导批示
		info.setRevises(null); //退改稿记录
		info.setOpinions(null);
		save(info);
		
		//拷贝附件
		for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
			InfoFilter infoFilter = (InfoFilter)iterator.next();
			List attachments = attachmentService.list("j2oa/info", "attachments", infoFilter.getInfoReceiveId(), false, 0, null);
			for(Iterator iteratorAttachment = (attachments==null ? null : attachments.iterator()); iteratorAttachment!=null && iteratorAttachment.hasNext();) {
				Attachment attachment = (Attachment)iteratorAttachment.next();
				attachmentService.uploadFile("j2oa/info", "attachments", null, infoReceive.getId(), attachment.getFilePath());
			}
		}
		
		//记录授权
		for(Iterator iterator = ((InfoFilter)infos.get(0)).getVisitors().iterator(); iterator.hasNext();) {
			InfoFilterPrivilege visitor = (InfoFilterPrivilege)iterator.next();
			recordControlService.appendVisitor(info.getId(), InfoFilter.class.getName(), visitor.getVisitorId(), visitor.getAccessLevel());
		}
		//恢复流程
		workflowExploitService.resumeWorkflowInstance(info.getWorkflowInstanceId(), info, sessionInfo);
		return info;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#uncombineInfos(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void uncombineInfos(String infoIds, SessionInfo sessionInfo) throws ServiceException {
		List infos = getDatabaseService().findRecordsByHql("from InfoFilter InfoFilter where InfoFilter.id in (" + infoIds + ")");
		infos = ListUtils.sortByProperty(infos, "id", infoIds);
		for(int i=0; i<infos.size(); i++) {
			InfoFilter info = (InfoFilter)infos.get(i);
			info.setIsBeCombined(0);
			getDatabaseService().updateRecord(info);
			workflowExploitService.resumeWorkflowInstance(info.getWorkflowInstanceId(), info, sessionInfo);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#listInfos(java.lang.String)
	 */
	public List listInfos(String infoIds) throws ServiceException {
		return getDatabaseService().findRecordsByHql("from InfoFilter InfoFilter where InfoFilter.id in (" + infoIds + ")", listLazyLoadProperties(InfoFilter.class));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#isMagazineEditor(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isMagazineEditor(SessionInfo sessionInfo) throws ServiceException {
		List records = getDatabaseService().findPrivilegedRecords(InfoMagazineDefine.class.getName(), null, null, null, null, null, RecordControlService.ACCESS_LEVEL_READONLY, false, null, 0, 1, sessionInfo);
		return records!=null && !records.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getInfoWorkflow(int)
	 */
	public InfoWorkflow getInfoWorkflow(int type) throws ServiceException {
		String hql = "from InfoWorkflow InfoWorkflow where InfoWorkflow.type=" + type;
		return (InfoWorkflow)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		int type = RequestUtils.getParameterIntValue(notifyRequest, "type");
		InfoWorkflow infoWorkflow = getInfoWorkflow(type);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程删除操作
			delete(infoWorkflow);
		}
		else if(infoWorkflow!=null) {
			infoWorkflow.setWorkflowId(Long.parseLong(workflowId)); //流程ID
			infoWorkflow.setWorkflowName(workflowPackage.getName()); //流程名称
			update(infoWorkflow);
		}
		else {
			infoWorkflow = new InfoWorkflow();
			infoWorkflow.setId(UUIDLongGenerator.generateId()); //ID
			infoWorkflow.setType(type); //流程类型,1/稿件,2/刊物
			infoWorkflow.setWorkflowId(Long.parseLong(workflowId)); //流程ID
			infoWorkflow.setWorkflowName(workflowPackage.getName()); //流程名称
			save(infoWorkflow);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("pointItem".equals(itemsName)) { //得分项目
			//0/采用,1/领导批示,2/报县(区)办,3/县(区)办采用,4/县(区)领导批示,5/报市办,6/市办采用,7/市领导批示,8/报省办,9/省办采用,10/省领导批示,11/报国办,12/国办采用,13/国办领导批示
			List items = new ArrayList();
			items.add(new Object[]{"采用", new Integer(0)});
			items.add(new Object[]{"领导批示", new Integer(1)});
			if(unitLevel<1) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"报县(区)办", new Integer(2)});
				items.add(new Object[]{"县(区)办采用", new Integer(3)});
				items.add(new Object[]{"县(区)领导批示", new Integer(4)});
			}
			if(unitLevel<2) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"报市办", new Integer(5)});
				items.add(new Object[]{"市办采用", new Integer(6)});
				items.add(new Object[]{"市领导批示", new Integer(7)});
			}
			if(unitLevel<3) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"报省办", new Integer(8)});
				items.add(new Object[]{"省办采用", new Integer(9)});
				items.add(new Object[]{"省领导批示", new Integer(10)});
			}
			if(unitLevel<4) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"报国办", new Integer(11)});
				items.add(new Object[]{"国办采用", new Integer(12)});
				items.add(new Object[]{"国办领导批示", new Integer(13)});
			}
			return items;
		}
		else if("sendLevel".equals(itemsName)) { //报送级别
			List items = new ArrayList();
			if(unitLevel<1) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"县办", new Integer(1)});
			}
			if(unitLevel<2) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"市办", new Integer(2)});
			}
			if(unitLevel<3) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"省办", new Integer(3)});
			}
			if(unitLevel<4) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"国办", new Integer(4)});
			}
			return items;
		}
		else if("leaderLevel".equals(itemsName)) { //报送级别、领导级别
			List items = new ArrayList();
			if(unitLevel<=1) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"县领导", new Integer(1)});
			}
			if(unitLevel<=2) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"市领导", new Integer(2)});
			}
			if(unitLevel<=3) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"省领导", new Integer(3)});
			}
			if(unitLevel<=4) { //单位级别,0/乡镇,1/县,2/市,3/省,4/国办
				items.add(new Object[]{"国家领导", new Integer(4)});
			}
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#getWordTemplate(long)
	 */
	public Attachment getWordTemplate(long magazineDefineId) throws ServiceException {
		List templates = attachmentService.list("j2oa/info", "template", magazineDefineId, false, 1, null);
		if(templates!=null && !templates.isEmpty()) {
			return (Attachment)templates.get(0);
		}
		Attachment template = new Attachment();
		template.setName("模板.doc");
		template.setFilePath(Environment.getWebinfPath() + "j2oa/info/template/模板.doc");
		return template;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.info.service.InfoService#saveWordTemplate(com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine, javax.servlet.http.HttpServletRequest)
	 */
	public void saveWordTemplate(final InfoMagazineDefine magazineDefine, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新模板
				remoteDocumentService.updateRecordFile(documentPath, magazineDefine, "template", magazineDefine.getName() + ".doc");
			}
		});
	}

	/**
	 * @return the unitLevel
	 */
	public int getUnitLevel() {
		return unitLevel;
	}

	/**
	 * @param unitLevel the unitLevel to set
	 */
	public void setUnitLevel(int unitLevel) {
		this.unitLevel = unitLevel;
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
	 * @return the soapPassport
	 */
	public SoapPassport getSoapPassport() {
		return soapPassport;
	}

	/**
	 * @param soapPassport the soapPassport to set
	 */
	public void setSoapPassport(SoapPassport soapPassport) {
		this.soapPassport = soapPassport;
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
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the remoteDocumentService
	 */
	public RemoteDocumentService getRemoteDocumentService() {
		return remoteDocumentService;
	}

	/**
	 * @param remoteDocumentService the remoteDocumentService to set
	 */
	public void setRemoteDocumentService(RemoteDocumentService remoteDocumentService) {
		this.remoteDocumentService = remoteDocumentService;
	}
}