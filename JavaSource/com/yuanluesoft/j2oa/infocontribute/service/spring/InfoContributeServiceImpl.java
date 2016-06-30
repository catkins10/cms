package com.yuanluesoft.j2oa.infocontribute.service.spring;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeInstruct;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeMagazine;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeMagazineDefine;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeReceiveUnit;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeRevise;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeUse;
import com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class InfoContributeServiceImpl extends BusinessServiceImpl implements InfoContributeService {
	private SoapPassport serviceSoapPassport; //SOAP许可证
	private OrgService orgService; //组织机构服务 
	private RecordControlService recordControlService; //记录控制服务
	private int dayContributeLimit = 0; //每日投稿限额
	private String receiveUnits; //接收单位列表
	
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("infoContribute", "信息采编投稿", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
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
		if(record instanceof InfoContributeRevise) {
			InfoContributeRevise revise = (InfoContributeRevise)record;
			//获取信息正文
			String hql = "select InfoContributeBody.body" +
						 " from InfoContributeBody InfoContributeBody" +
						 " where InfoContributeBody.id=" + revise.getInfoId();
			revise.getInfo().setLazyBody(null);
			revise.getInfo().setBody((String)getDatabaseService().findRecordByHql(hql));
		}
		return record;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#addOrUpdateMagazineDefine(long, java.lang.String, java.lang.String)
	 */
	public void addOrUpdateMagazineDefine(long id, String name, String unitName) throws ServiceException {
		InfoContributeMagazineDefine magazineDefine = (InfoContributeMagazineDefine)load(InfoContributeMagazineDefine.class, id);
		if(magazineDefine==null) {
			magazineDefine = new InfoContributeMagazineDefine();
			magazineDefine.setId(id);
			magazineDefine.setName(name); //刊物名称
			magazineDefine.setUnitName(unitName); //单位名称
			save(magazineDefine);
		}
		else if(!magazineDefine.getName().equals(name)) {
			magazineDefine.setName(name);
			update(magazineDefine);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#deleteMagazineDefine(long)
	 */
	public void deleteMagazineDefine(long id) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from InfoContributeMagazineDefine InfoContributeMagazineDefine where InfoContributeMagazineDefine.id=" + id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#listContributeInfos(java.lang.String, java.sql.Timestamp, int, int)
	 */
	public List listContributeInfos(String unitName, Timestamp beginTime, int offset, int limit) throws ServiceException {
		String hql = "select InfoContribute" +
					 " from InfoContribute InfoContribute left join InfoContribute.receiveUnits InfoContributeReceiveUnit" +
					 " where InfoContributeReceiveUnit.unitName='" + JdbcUtils.resetQuot(unitName) + "'" +
					 (beginTime==null ? " and InfoContribute.contributeTime is not null" : " and InfoContribute.contributeTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")") +
					 " order by InfoContribute.contributeTime";
		List infos = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("lazyBody"), offset, limit);
		//根据单位名称,排除预选刊型ID
		hql = "select InfoContributeMagazineDefine.id" +
			  " from InfoContributeMagazineDefine InfoContributeMagazineDefine" +
			  " where InfoContributeMagazineDefine.unitName='" + JdbcUtils.resetQuot(unitName) + "'";
		List magazineDefineIds = getDatabaseService().findRecordsByHql(hql);
		for(int i=0; i<(magazineDefineIds==null ? 0 : magazineDefineIds.size()); i++) {
			magazineDefineIds.set(i, "" + magazineDefineIds.get(i));
		}
		for(Iterator iterator = infos==null ? null : infos.iterator(); iterator!=null && iterator.hasNext();) {
			InfoContribute info = (InfoContribute)iterator.next();
			if(info.getPresetMagazines()==null || info.getPresetMagazines().isEmpty()) {
				continue;
			}
			List presetMagazineIds = ListUtils.generateList(info.getPresetMagazines(), ",");
			presetMagazineIds = ListUtils.getInsideSubList(magazineDefineIds, null, presetMagazineIds, null);
			info.setPresetMagazines(ListUtils.join(presetMagazineIds, ",", false));
		}
		return infos;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#reviseInfo(long, long, java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public void reviseInfo(long reviseId, long infoId, String reviseOpinion, long revisePersonId, String revisePersonName, String revisePersonTel) throws ServiceException {
		//获取投稿
		InfoContribute info = (InfoContribute)load(InfoContribute.class, infoId);
		if(info==null) {
			throw new ServiceException("Info is not exists");
		}
		InfoContributeRevise revise = new InfoContributeRevise();
		revise.setId(reviseId); //ID
		revise.setInfoId(infoId); //稿件ID,原始稿件ID或者录用稿件ID
		revise.setSubject(info.getSubject()); //稿件标题
		revise.setRevisePersonId(revisePersonId); //退改稿人ID
		revise.setRevisePerson(revisePersonName); //退改稿人
		revise.setRevisePersonTel(revisePersonTel); //退改稿人电话
		revise.setReviseOpinion(reviseOpinion); //退改稿意见
		revise.setReviseTime(DateTimeUtils.now()); //退改稿时间
		try {
			save(revise);
		}
		catch(Exception e) {
			update(revise);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#addInstruct(long, java.lang.String, java.lang.String, int, java.lang.String, java.sql.Timestamp, long, java.lang.String, java.sql.Timestamp)
	 */
	public void addInstruct(long instructId, String infoIds, String leader, int level, String instruct, Timestamp instructTime, long creatorId, String creator, Timestamp created) throws ServiceException {
		//删除原来的批示
		deleteInstruct(instructId);
		//添加批示
		String[] ids = infoIds.split(",");
		for(int i=0; i<ids.length; i++) {
			InfoContributeInstruct infoInstruct = new InfoContributeInstruct();
			infoInstruct.setId(UUIDLongGenerator.generateId()); //ID
			infoInstruct.setInfoId(Long.parseLong(ids[i])); //稿件ID
			infoInstruct.setInstructId(instructId); //批示ID,采编端ID
			infoInstruct.setLeader(leader); //领导姓名
			infoInstruct.setLevel(level); //领导级别,1/县,2/市,3/省,4/国
			infoInstruct.setInstruct(instruct); //批示内容
			infoInstruct.setInstructTime(instructTime); //批示时间
			infoInstruct.setCreatorId(creatorId); //录入人ID
			infoInstruct.setCreator(creator); //录入人
			infoInstruct.setCreated(created); //录入时间
			save(infoInstruct);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#deleteInstruct(long)
	 */
	public void deleteInstruct(long instructId) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from InfoContributeInstruct InfoContributeInstruct where InfoContributeInstruct.instructId=" + instructId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#addUsage(long, java.lang.String, int, java.sql.Timestamp, java.sql.Timestamp, long, java.lang.String)
	 */
	public void addUsage(long useId, String infoIds, int level, Timestamp sendTime, Timestamp useTime, long magazineId, String magazine) throws ServiceException {
		//删除原来的使用情况
		deleteUsage(useId);
		//添加使用情况
		String[] ids = infoIds.split(",");
		for(int i=0; i<ids.length; i++) {
			InfoContributeUse use = new InfoContributeUse();
			use.setId(UUIDLongGenerator.generateId()); //ID
			use.setInfoId(Long.parseLong(ids[i])); //稿件ID
			use.setUseId(useId); //采用情况ID
			use.setLevel(level); //报送级别,1/县办,2/市办,3/省办,4/国办
			use.setSendTime(sendTime); //报送时间
			use.setUseTime(useTime); //采用时间
			use.setMagazineId(magazineId); //采用刊物ID
			use.setMagazine(magazine); //采用刊物名称
			save(use);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#deleteUsage(long)
	 */
	public void deleteUsage(long useId) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from InfoContributeUse InfoContributeUse where InfoContributeUse.useId=" + useId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#addMagazine(long, java.lang.String, java.sql.Timestamp, long, long, int, java.lang.String, java.lang.String)
	 */
	public void addMagazine(long magazineId, String name, Timestamp issueTime, long sn, long snTotal, int level, String useInfoIds, String visitorIds) throws ServiceException {
		//删除原来的刊物
		deleteMagazine(magazineId);
		//添加刊物
		InfoContributeMagazine magazine = new InfoContributeMagazine();
		magazine.setId(magazineId); //ID
		magazine.setName(name); //名称
		magazine.setIssueTime(issueTime); //发布时间
		magazine.setSn(sn); //期数
		magazine.setSnTotal(snTotal); //总期数
		save(magazine);
		//添加访问者
		String[] ids = visitorIds==null || visitorIds.isEmpty() ? null : visitorIds.split(",");
		for(int i=0; i<(ids==null ? 0 : ids.length); i++) {
			recordControlService.appendVisitor(magazineId, InfoContributeMagazine.class.getName(), Long.parseLong(ids[i]), RecordControlService.ACCESS_LEVEL_READONLY);
		}
		//添加录用记录
		ids = useInfoIds==null || useInfoIds.isEmpty() ? null : useInfoIds.split(",");
		for(int i=0; i<(ids==null ? 0 : ids.length); i++) {
			InfoContributeUse use = new InfoContributeUse();
			use.setId(UUIDLongGenerator.generateId()); //ID
			use.setInfoId(Long.parseLong(ids[i])); //稿件ID
			use.setUseId(magazineId); //采用情况ID
			use.setLevel(level); //报送级别,1/县办,2/市办,3/省办,4/国办
			use.setSendTime(issueTime); //报送时间
			use.setUseTime(issueTime); //采用时间
			use.setMagazineId(magazineId); //采用刊物ID
			use.setMagazine(name); //采用刊物名称
			save(use);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#deleteMagazine(long)
	 */
	public void deleteMagazine(long magazineId) throws ServiceException {
		//删除刊物
		InfoContributeMagazine magazine = (InfoContributeMagazine)load(InfoContributeMagazine.class, magazineId);
		if(magazine==null) {
			return;
		}
		delete(magazine);
		//删除录用记录
		getDatabaseService().deleteRecordsByHql("from InfoContributeUse InfoContributeUse where InfoContributeUse.magazineId=" + magazineId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#addSupplementInfo(long, java.lang.String, java.lang.String, java.lang.String, int, int, java.lang.String, long, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public void addSupplementInfo(long infoId, String subject, String keywords, String magazineName, int magazineSN, int magazineLevel, String fromUnit, long fromUnitId, String body, String secretLevel, Timestamp supplementTime) throws ServiceException {
		InfoContribute info = (InfoContribute)load(InfoContribute.class, infoId);
		boolean isNew = info==null;
		if(isNew) {
			info = new InfoContribute();
			info.setId(infoId);
		}
		else {
			getDatabaseService().deleteRecordsByHql("from InfoContributeUse InfoContributeUse where InfoContributeUse.infoId=" + infoId + " and InfoContributeUse.useId=0"); //删除录用记录
		}
		info.setSubject(subject); //标题
		info.setKeywords(keywords); //主题词
		info.setSecretLevel(secretLevel); //密级,普通 秘密 机密
		info.setFromUnit(fromUnit); //来稿单位名称
		info.setFromUnitId(fromUnitId); //来稿单位ID
		info.setCreated(supplementTime); //创建时间
		info.setContributeTime(supplementTime); //投稿时间
		info.setSupplementTime(supplementTime); //补录时间
		info.setBody(body); //正文
		if(isNew) {
			save(info);
		}
		else {
			update(info);
		}
		
		//添加录用记录
		InfoContributeUse use = new InfoContributeUse();
		use.setId(UUIDLongGenerator.generateId()); //ID
		use.setInfoId(infoId); //稿件ID
		use.setUseId(0); //采用情况ID
		use.setLevel(magazineLevel); //报送级别,1/县办,2/市办,3/省办,4/国办
		use.setSendTime(supplementTime); //报送时间
		use.setUseTime(supplementTime); //采用时间
		use.setMagazineId(0); //采用刊物ID
		use.setMagazine(magazineName); //采用刊物名称
		save(use);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#deleteSupplementInfo(long)
	 */
	public void deleteSupplementInfo(long infoId) throws ServiceException {
		InfoContribute info = (InfoContribute)load(InfoContribute.class, infoId);
		if(info!=null) {
			delete(info);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService#updateInfoReceiveUnits(com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute, java.lang.String[])
	 */
	public void updateInfoReceiveUnits(InfoContribute info, String[] receiveUnitNames) throws ServiceException {
		if(receiveUnitNames==null || receiveUnitNames.length==0) {
			return;
		}
		for(Iterator iterator = info.getReceiveUnits()==null ? null : info.getReceiveUnits().iterator(); iterator!=null && iterator.hasNext();) {
			InfoContributeReceiveUnit receiveUnit = (InfoContributeReceiveUnit)iterator.next();
			getDatabaseService().deleteRecord(receiveUnit);
		}
		info.setReceiveUnits(new LinkedHashSet());
		for(int i=0; i<receiveUnitNames.length; i++) {
			InfoContributeReceiveUnit receiveUnit = new InfoContributeReceiveUnit();
			receiveUnit.setId(UUIDLongGenerator.generateId()); //ID
			receiveUnit.setInfoId(info.getId()); //稿件ID
			receiveUnit.setUnitName(receiveUnitNames[i]); //单位名称
			getDatabaseService().saveRecord(receiveUnit);
			info.getReceiveUnits().add(receiveUnit);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("receiveUnitNames".equals(itemsName)) {
			return ListUtils.generateList(receiveUnits, ",");
		}
		else if("presetMagazines".equals(itemsName)) {
			String hql = "select InfoContributeMagazineDefine.name, InfoContributeMagazineDefine.id" +
					 	 " from InfoContributeMagazineDefine InfoContributeMagazineDefine" +
					 	 " order by InfoContributeMagazineDefine.unitName, InfoContributeMagazineDefine.name";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the dayContributeLimit
	 */
	public int getDayContributeLimit() {
		return dayContributeLimit;
	}

	/**
	 * @param dayContributeLimit the dayContributeLimit to set
	 */
	public void setDayContributeLimit(int dayContributeLimit) {
		this.dayContributeLimit = dayContributeLimit;
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
	 * @return the serviceSoapPassport
	 */
	public SoapPassport getServiceSoapPassport() {
		return serviceSoapPassport;
	}

	/**
	 * @param serviceSoapPassport the serviceSoapPassport to set
	 */
	public void setServiceSoapPassport(SoapPassport serviceSoapPassport) {
		this.serviceSoapPassport = serviceSoapPassport;
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
	 * @return the receiveUnits
	 */
	public String getReceiveUnits() {
		return receiveUnits;
	}

	/**
	 * @param receiveUnits the receiveUnits to set
	 */
	public void setReceiveUnits(String receiveUnits) {
		this.receiveUnits = receiveUnits;
	}
	
}