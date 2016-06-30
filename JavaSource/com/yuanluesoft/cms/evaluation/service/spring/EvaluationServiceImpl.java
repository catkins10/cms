package com.yuanluesoft.cms.evaluation.service.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.evaluation.model.EvaluationTargetPerson;
import com.yuanluesoft.cms.evaluation.model.total.EvaluationItemTotal;
import com.yuanluesoft.cms.evaluation.model.total.EvaluationOptionTotal;
import com.yuanluesoft.cms.evaluation.model.total.EvaluationTotal;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationItem;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationMark;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationOption;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationScore;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopicPrivilege;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationServiceImpl extends BusinessServiceImpl implements EvaluationService {
	private PageService pageService; //页面服务
	private PersonService personService; //用户服务
	private OrgService orgService; //组织机构服务
	private RecordControlService recordControlService; //记录控制服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof EvaluationTopic) { //测评主题
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof EvaluationTopic) { //测评主题
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else if(record instanceof EvaluationOption) { //测评选择项
			EvaluationOption option = (EvaluationOption)record;
			//更新评分
			String hql = "from EvaluationScore EvaluationScore where EvaluationScore.optionId=" + option.getId() + " order by EvaluationScore.id";
			for(int i=0; ; i+=100) { //每次处理100条记录
				List scores = getDatabaseService().findRecordsByHql(hql, i, 100);
				if(scores==null || scores.isEmpty()) {
					break;
				}
				for(Iterator iterator = scores.iterator(); iterator.hasNext();) {
					EvaluationScore score = (EvaluationScore)iterator.next();
					score.setScore(option.getScore());
					getDatabaseService().updateRecord(score);
				}
				if(scores.size()<100) {
					break;
				}
			}
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof EvaluationTopic) { //测评主题
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#listEvaluatedTargetPersons(com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listEvaluatedTargetPersons(EvaluationTopic evaluationTopic, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List evaluateTargetPersons = (List)request.getAttribute("evaluateTargetPersons");
		if(evaluateTargetPersons!=null) {
			return evaluateTargetPersons;
		}
		//如果是匿名测评,从session获取已经测评过的记录
		if(evaluationTopic.getAnonymous()=='1') {
			evaluateTargetPersons = (List)request.getSession().getAttribute("evaluationMarks_" + evaluationTopic.getId());
			if(evaluateTargetPersons!=null) {
				evaluateTargetPersons = new ArrayList(evaluateTargetPersons);
			}
		}
		else {
			//实名测评
			String hql = "from EvaluationMark EvaluationMark " +
						 " where EvaluationMark.evaluatePersonId=" + sessionInfo.getUserId() +
						 " and EvaluationMark.topicId=" + evaluationTopic.getId();
			evaluateTargetPersons = getDatabaseService().findRecordsByHql(hql);
		}
		if(evaluateTargetPersons==null) {
			evaluateTargetPersons = new ArrayList();
		}
		//转换为EvaluationTargetPerson
		for(int i=0; i<evaluateTargetPersons.size(); i++) {
			EvaluationMark mark = (EvaluationMark)evaluateTargetPersons.get(i);
			EvaluationTargetPerson targetPerson = new EvaluationTargetPerson();
			targetPerson.setPersonId(mark.getTargetPersonId()); //用户ID
			targetPerson.setPersonName(mark.getTargetPersonName()); //用户名
			Person person = personService.getPerson(mark.getTargetPersonId());
			if(person!=null) {
				targetPerson.setOrgName(orgService.getDirectoryFullName(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId(), "/", "unit,school")); //所在组织机构名称
			}
			targetPerson.setEvluateTime(mark.getEvaluateTime()); //测评时间
			evaluateTargetPersons.set(i, targetPerson);
		}
		request.setAttribute("evaluateTargetPersons", evaluateTargetPersons);
		return evaluateTargetPersons;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#listToEvaluateTargetPersons(com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listToEvaluateTargetPersons(EvaluationTopic evaluationTopic, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取所有待测评用户
		List targetPersons = ListUtils.getSubListByProperty(evaluationTopic.getVisitors(), "accessLevel", new Character(RecordControlService.ACCESS_LEVEL_PREREAD));
		//获取已经测评过的用户
		List evaluatedTargetPersons = listEvaluatedTargetPersons(evaluationTopic, request, sessionInfo);
		//转换为EvaluationTargetPerson
		Person person;
		for(int i=0; i<targetPersons.size(); i++) {
			EvaluationTopicPrivilege privilege = (EvaluationTopicPrivilege)targetPersons.get(i);
			//检查是否已经测评过
			if(ListUtils.findObjectByProperty(evaluatedTargetPersons, "personId", new Long(privilege.getVisitorId()))!=null || //测评过
			   (person=personService.getPerson(privilege.getVisitorId()))==null) { //已经不在用户表中
				targetPersons.remove(i);
				i--;
				continue;
			}
			//转换为EvaluationTargetPerson
			EvaluationTargetPerson targetPerson = new EvaluationTargetPerson();
			targetPerson.setPersonId(person.getId()); //用户ID
			targetPerson.setPersonName(person.getName()); //用户名
			targetPerson.setOrgName(orgService.getDirectoryFullName(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId(), "/", "unit,school")); //所在组织机构名称
			//targetPerson.setEvluateTime(null); //测评时间
			targetPersons.set(i, targetPerson);
		}
		return targetPersons;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#loadEvaluationMark(long, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public EvaluationMark loadEvaluationMark(long evaluationTopicId, long targetPersonId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名
			List evaluationMarks = (List)request.getSession().getAttribute("evaluationMarks_" + evaluationTopicId);
			return (EvaluationMark)ListUtils.findObjectByProperty(evaluationMarks, "targetPersonId", new Long(targetPersonId));
		}
		String hql = "from EvaluationMark EvaluationMark " +
					 " where EvaluationMark.evaluatePersonId=" + sessionInfo.getUserId() +
					 " and EvaluationMark.targetPersonId=" + targetPersonId +
					 " and EvaluationMark.topicId=" + evaluationTopicId;
		return (EvaluationMark)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("scores", ","));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#sumitEvaluation(long, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public EvaluationMark submitEvaluation(long evaluationTopicId, long targetPersonId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//删除原来的测评记录
		EvaluationMark evaluationMark = loadEvaluationMark(evaluationTopicId, targetPersonId, request, sessionInfo);
		if(evaluationMark!=null) {
			delete(evaluationMark);
			if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名
				List evaluationMarks = (List)request.getSession().getAttribute("evaluationMarks_" + evaluationTopicId);
				ListUtils.removeObjectByProperty(evaluationMarks, "targetPersonId", new Long(targetPersonId)); //从列表中删除
			}
		}
		evaluationMark = new EvaluationMark();
		evaluationMark.setId(UUIDLongGenerator.generateId()); //ID
		evaluationMark.setTopicId(evaluationTopicId); //主题ID
		evaluationMark.setTargetPersonId(targetPersonId); //被测评人ID
		evaluationMark.setTargetPersonName(personService.getPersonName(targetPersonId)); //被测评人姓名
		if(sessionInfo!=null && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //实名
			evaluationMark.setEvaluatePersonId(sessionInfo.getUserId()); //测评人ID
			evaluationMark.setEvaluatePersonName(sessionInfo.getUserName()); //测评人名称
		}
		evaluationMark.setEvaluatePersonIP(request.getRemoteHost()); //测评人IP
		evaluationMark.setEvaluateTime(DateTimeUtils.now()); //测评时间
		evaluationMark.setScores(new HashSet()); //测评分数列表
		//保存评分
		EvaluationTopic evaluationTopic = (EvaluationTopic)load(EvaluationTopic.class, evaluationTopicId);
		for(Iterator iterator = evaluationTopic.getItems().iterator(); iterator.hasNext();) {
			EvaluationItem item = (EvaluationItem)iterator.next();
			//获取选中的选项ID
			long optionId = RequestUtils.getParameterLongValue(request, "option_" + item.getId());
			String remark = RequestUtils.getParameterStringValue(request, "remark_" + item.getId());
			EvaluationScore score = new EvaluationScore();
			score.setId(UUIDLongGenerator.generateId()); //ID
			score.setMarkId(evaluationMark.getId()); //测评记录ID
			score.setItemId(item.getId()); //测评项目ID
			score.setOptionId(optionId); //选择项ID
			EvaluationOption option = (EvaluationOption)ListUtils.findObjectByProperty(evaluationTopic.getOptions(), "id", new Long(optionId));
			score.setOption(option.getName()); //选择项名称
			score.setScore(option.getScore()); //分值
			score.setRemark(remark); //备注
			save(score);
			evaluationMark.getScores().add(score);
		}
		//保存测评成绩
		save(evaluationMark);
		//匿名,记录到会话中
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名
			List evaluationMarks = (List)request.getSession().getAttribute("evaluationMarks_" + evaluationTopicId);
			if(evaluationMarks==null) {
				evaluationMarks = new ArrayList();
				request.getSession().setAttribute("evaluationMarks_" + evaluationTopicId, evaluationMarks);
			}
			evaluationMarks.add(evaluationMark);
		}
		return evaluationMark;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#copyEvaluationTopic(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public EvaluationTopic copyEvaluationTopic(long evaluationTopicId, SessionInfo sessionInfo) throws ServiceException {
		EvaluationTopic evaluationTopic = (EvaluationTopic)load(EvaluationTopic.class, evaluationTopicId);
		EvaluationTopic newEvaluationTopic = new EvaluationTopic();
		try {
			PropertyUtils.copyProperties(newEvaluationTopic, evaluationTopic);
		}
		catch (Exception e) {
			
		}
		newEvaluationTopic.setId(UUIDLongGenerator.generateId()); //ID
		newEvaluationTopic.setEndTime(null);  //截止日期
		newEvaluationTopic.setIssueTime(null); //发布时间
		newEvaluationTopic.setIssue('0'); //是否发布
		newEvaluationTopic.setCreated(DateTimeUtils.now()); //创建时间
		newEvaluationTopic.setCreatorId(sessionInfo.getUserId()); //创建者ID
		newEvaluationTopic.setCreator(sessionInfo.getUserName()); //创建者
		newEvaluationTopic.setMarks(null); //测评记录列表
		//复制选择项
		newEvaluationTopic.setOptions(new LinkedHashSet()); //选择项列表
		for(Iterator iterator = (evaluationTopic.getOptions()==null ? null : evaluationTopic.getOptions().iterator()); iterator!=null && iterator.hasNext();) {
			EvaluationOption option = (EvaluationOption)iterator.next();
			EvaluationOption newOption = new EvaluationOption();
			try {
				PropertyUtils.copyProperties(newOption, option);
			}
			catch (Exception e) {
				
			}
			newOption.setId(UUIDLongGenerator.generateId()); //ID
			newOption.setTopicId(newEvaluationTopic.getId()); //主题ID
			getDatabaseService().saveRecord(newOption);
			newEvaluationTopic.getOptions().add(newOption);
		}
		//复制测评项目
		newEvaluationTopic.setItems(new LinkedHashSet()); //测评项目列表
		for(Iterator iterator = (evaluationTopic.getItems()==null ? null : evaluationTopic.getItems().iterator()); iterator!=null && iterator.hasNext();) {
			EvaluationItem item = (EvaluationItem)iterator.next();
			EvaluationItem newItem = new EvaluationItem();
			try {
				PropertyUtils.copyProperties(newItem, item);
			}
			catch (Exception e) {
				
			}
			newItem.setId(UUIDLongGenerator.generateId()); //ID
			newItem.setTopicId(newEvaluationTopic.getId()); //主题ID
			getDatabaseService().saveRecord(newItem);
			newEvaluationTopic.getItems().add(newItem);
		}
		//授权当前用户可编辑
		newEvaluationTopic.setVisitors(new LinkedHashSet()); //访问者列表
		newEvaluationTopic.getVisitors().add(recordControlService.appendVisitor(newEvaluationTopic.getId(), EvaluationTopic.class.getName(), newEvaluationTopic.getCreatorId(), RecordControlService.ACCESS_LEVEL_EDITABLE));
		save(newEvaluationTopic);
		return newEvaluationTopic;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.service.EvaluationService#evaluationTotal(long)
	 */
	public List evaluationTotal(long evaluationTopicId) throws ServiceException {
		EvaluationTopic evaluationTopic = (EvaluationTopic)load(EvaluationTopic.class, evaluationTopicId);
		List totals = ListUtils.getSubListByProperty(evaluationTopic.getVisitors(), "accessLevel", new Character(RecordControlService.ACCESS_LEVEL_PREREAD));
		for(int i=0; i<totals.size(); i++) {
			EvaluationTopicPrivilege topicPrivilege = (EvaluationTopicPrivilege)totals.get(i);
			
			//转换为EvaluationTotal
			EvaluationTotal total = new EvaluationTotal();
			totals.set(i, total);
			total.setScore(0); //得分
			total.setTargetPersonId(topicPrivilege.getVisitorId()); //被测评用户ID
			Person person = personService.getPerson(topicPrivilege.getVisitorId());
			if(person==null) { //用户已经被删除
				continue;
			}
			total.setTargetPersonName(person.getName()); //被测评用户姓名
			total.setTargetPersonOrg(orgService.getDirectoryFullName(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId(), "/", "unit,school")); //所在组织机构名称
			
			//初始化选项统计
			total.setOptionTotals(new ArrayList());
			for(Iterator iterator = (evaluationTopic.getOptions()==null ? null : evaluationTopic.getOptions().iterator()); iterator!=null && iterator.hasNext();) {
				EvaluationOption option = (EvaluationOption)iterator.next();
				EvaluationOptionTotal optionTotal = new EvaluationOptionTotal();
				optionTotal.setOptionId(option.getId()); //选项ID
				optionTotal.setCount(0); //选中次数
				total.getOptionTotals().add(optionTotal);
			}
			
			//初始化测评项目统计
			total.setItemTotals(new ArrayList());
			int noneScoreItemCount = 0; //未设置分数的项目数
			double leftItemScore = 100; //扣除已设置分数,剩余的分数 
			for(Iterator iterator = (evaluationTopic.getItems()==null ? null : evaluationTopic.getItems().iterator()); iterator!=null && iterator.hasNext();) {
				EvaluationItem item = (EvaluationItem)iterator.next();
				if(item.getScore()==0) {
					noneScoreItemCount++;
				}
				else {
					leftItemScore -= item.getScore();
				}
				EvaluationItemTotal itemTotal = new EvaluationItemTotal();
				itemTotal.setItemId(item.getId()); //测评项目ID
				itemTotal.setScore(0); //得分
				itemTotal.setMarkCount(0); //打分次数
				//选项列表
				itemTotal.setOptionTotals(new ArrayList());
				for(Iterator iteratorOption = (evaluationTopic.getOptions()==null ? null : evaluationTopic.getOptions().iterator()); iteratorOption!=null && iteratorOption.hasNext();) {
					EvaluationOption option = (EvaluationOption)iteratorOption.next();
					EvaluationOptionTotal optionTotal = new EvaluationOptionTotal();
					optionTotal.setOptionId(option.getId()); //选项ID
					optionTotal.setCount(0); //选中次数
					itemTotal.getOptionTotals().add(optionTotal);
				}
				total.getItemTotals().add(itemTotal);
			}
			
			//测评项目统计
			String hql = "select EvaluationScore.itemId, EvaluationScore.optionId, count(EvaluationScore.id), sum(EvaluationScore.score)" +
						 " from EvaluationScore EvaluationScore, EvaluationMark EvaluationMark" +
						 " where EvaluationScore.markId=EvaluationMark.id" +
						 " and EvaluationMark.topicId=" + evaluationTopicId +
						 " and EvaluationMark.targetPersonId=" + total.getTargetPersonId() +
						 " group by EvaluationScore.itemId, EvaluationScore.optionId";
			List results = getDatabaseService().findRecordsByHql(hql);
			if(results==null || results.isEmpty()) {
				continue;
			}
			for(Iterator iterator = results.iterator(); iterator.hasNext();) {
				Object[] values = (Object[])iterator.next();
				long itemId = ((Number)values[0]).longValue(); //测评项目ID
				long optionId = ((Number)values[1]).longValue(); //选中的选择项ID
				int count = ((Number)values[2]).intValue(); //选中次数
				double score = ((Number)values[3]).intValue(); //得分
				//获取测评项目统计
				EvaluationItemTotal itemTotal = (EvaluationItemTotal)ListUtils.findObjectByProperty(total.getItemTotals(), "itemId", new Long(itemId));
				if(itemTotal==null) {
					continue;
				}
				itemTotal.setScore(itemTotal.getScore() + score); //累积得分
				itemTotal.setMarkCount(itemTotal.getMarkCount() + count); //打分次数
				//累积测评项目选项选中次数
				EvaluationOptionTotal optionTotal = (EvaluationOptionTotal)ListUtils.findObjectByProperty(itemTotal.getOptionTotals(), "optionId", new Long(optionId));
				if(optionTotal!=null) {
					optionTotal.setCount(optionTotal.getCount() + count);
				}
				//累积选项选中次数
				optionTotal = (EvaluationOptionTotal)ListUtils.findObjectByProperty(total.getOptionTotals(), "optionId", new Long(optionId));
				if(optionTotal!=null) {
					optionTotal.setCount(optionTotal.getCount() + count);
				}
			}
			//计算总得分
			for(Iterator iterator = total.getItemTotals().iterator(); iterator.hasNext();) {
				EvaluationItemTotal itemTotal = (EvaluationItemTotal)iterator.next();
				EvaluationItem item = (EvaluationItem)ListUtils.findObjectByProperty(evaluationTopic.getItems(), "id", new Long(itemTotal.getItemId()));
				itemTotal.setScore(itemTotal.getScore()/itemTotal.getMarkCount()); //当前选项得分,百分制
				//总分
				total.setScore(total.getScore() + (item.getScore()==0 ? leftItemScore/noneScoreItemCount : item.getScore()) * (itemTotal.getScore()/100.0));
			}
		}
		//排序
		Collections.sort(totals, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return (int)(((EvaluationTotal)arg1).getScore() * 100 - ((EvaluationTotal)arg0).getScore() * 100);
			}
		});
		return totals;
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
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
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
}