package com.yuanluesoft.cms.inquiry.services.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.inquiry.model.InquiryMatch;
import com.yuanluesoft.cms.inquiry.model.InquiryResult;
import com.yuanluesoft.cms.inquiry.model.InquiryVoterTotal;
import com.yuanluesoft.cms.inquiry.pojo.Inquiry;
import com.yuanluesoft.cms.inquiry.pojo.InquiryFeedback;
import com.yuanluesoft.cms.inquiry.pojo.InquiryOption;
import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.pojo.InquiryVote;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.report.graph.GraphReportService;
import com.yuanluesoft.jeaf.report.graph.model.ChartData;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class InquiryServiceImpl extends BusinessServiceImpl implements InquiryService {
	private ExchangeClient exchangeClient; //数据交换服务
	private PageService pageService; //页面服务
	private boolean forceValidateCode = true; //是否强制验证码校验,默认需要校验
	private GraphReportService graphReportService; //图形报表服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.dao.Record)
	 */
	public Record save(Record record) throws ServiceException {
		exchangeClient.synchUpdate(record, null, 2000);
		if((record instanceof InquiryFeedback) || (record instanceof InquiryOption) || ((record instanceof InquirySubject) && ((InquirySubject)record).getIsPublic()=='1')) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.dao.Record)
	 */
	public Record update(Record record) throws ServiceException {
		exchangeClient.synchUpdate(record, null, 2000);
		if((record instanceof InquiryFeedback) || (record instanceof InquiryOption) || ((record instanceof InquirySubject) && ((InquirySubject)record).getIsPublic()=='1')) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		if(record instanceof InquirySubject) {
			InquirySubject inquirySubject = (InquirySubject)record;
			if(inquirySubject.getIsPublic()!='1') { //未发布
				String hql = "select InquirySubject.isPublic from InquirySubject InquirySubject where InquirySubject.id=" + record.getId();
				if(((Character)getDatabaseService().findRecordByHql(hql)).charValue()=='1') { //之前是发布的
					pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
				}
			}
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.dao.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		exchangeClient.synchDelete(record, null, 2000);
		if((record instanceof InquiryFeedback) || (record instanceof InquiryOption) || ((record instanceof InquirySubject) && ((InquirySubject)record).getIsPublic()=='1')) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(com.yuanluesoft.jeaf.database.Record)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		if(record instanceof InquiryOption) {
			InquiryOption option = (InquiryOption)record;
			String hql = "select InquiryOption.id" +
						 " from InquiryOption InquiryOption" +
						 " where InquiryOption.id!=" + option.getId() +
						 " and InquiryOption.inquiryId=" + option.getInquiryId() +
						 " and InquiryOption.inquiryOption='" + JdbcUtils.resetQuot(option.getInquiryOption()) + "'";
			return getDatabaseService().findRecordByHql(hql)==null ? null  : ListUtils.generateList("选择“" + option.getInquiryOption() + "”已经存在，不允许重复输入");
		}
		return super.validateBusiness(record, isNew);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#listInquirySubjects(long)
	 */
	public List listInquirySubjects(long siteId) throws ServiceException {
		String hql = "from InquirySubject InquirySubject" +
				 	" where InquirySubject.siteId=" + siteId +
					" order by InquirySubject.created DESC";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#isAnonymous(java.lang.String)
	 */
	public boolean isAnonymous(String inquiryIds) throws ServiceException {
		if(inquiryIds==null || inquiryIds.isEmpty()) {
			return false;
		}
		String hql = "select min(InquirySubject.isAnonymous)" +
					 " from InquirySubject InquirySubject left join InquirySubject.inquiries Inquiry" +
					 " where Inquiry.id in (" + JdbcUtils.validateInClauseNumbers(inquiryIds) + ")";
		Character isAnonymous = (Character)getDatabaseService().findRecordByHql(hql);
		return isAnonymous!=null && isAnonymous.charValue()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#isAnonymous(long)
	 */
	public boolean isAnonymous(long subjectId) throws ServiceException {
		String hql = "select InquirySubject.isAnonymous" +
					 " from InquirySubject InquirySubject" +
					 " where InquirySubject.id=" + subjectId;
		Character isAnonymous = (Character)getDatabaseService().findRecordByHql(hql);
		return isAnonymous!=null && isAnonymous.charValue()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#isHideResult(java.lang.String)
	 */
	public boolean isHideResult(String inquiryIds) throws ServiceException {
		String hql = "select InquirySubject.publishResult, InquirySubject.endTime" +
					 " from InquirySubject InquirySubject left join InquirySubject.inquiries Inquiry" +
					 " where Inquiry.id in (" + JdbcUtils.validateInClauseNumbers(inquiryIds) + ")" +
					 " and InquirySubject.publishResult!='2'";
		List records = getDatabaseService().findRecordsByHql(hql);
		if(records==null || records.isEmpty()) { //总是公开
			return false;
		}
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			if(((Character)values[0]).charValue()=='0' || values[1]==null || ((Timestamp)values[1]).after(DateTimeUtils.now())) { //总是不公开、或者调查截止时间未到
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#hasQuestionnaire(java.lang.String)
	 */
	public boolean hasQuestionnaire(String inquiryIds) throws ServiceException {
		String hql = "select max(InquirySubject.isQuestionnaire)" +
					 " from InquirySubject InquirySubject left join InquirySubject.inquiries Inquiry" +
					 " where Inquiry.id in (" + JdbcUtils.validateInClauseNumbers(inquiryIds) + ")";
		Character isQuestionnaire = (Character)getDatabaseService().findRecordByHql(hql);
		return isQuestionnaire!=null && isQuestionnaire.charValue()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#isQuestionnaire(long)
	 */
	public boolean isQuestionnaire(long subjectId) throws ServiceException {
		String hql = "select InquirySubject.isQuestionnaire" +
					 " from InquirySubject InquirySubject" +
					 " where InquirySubject.id=" + subjectId;
		Character isQuestionnaire = (Character)getDatabaseService().findRecordByHql(hql);
		return isQuestionnaire!=null && isQuestionnaire.charValue()=='1';
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#saveInquiry(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public void submitInquiry(long inquiryId, String selectedOptions, String ip, SessionInfo sessionInfo) throws ServiceException {
		//获取调查主题
		String hql = "from InquirySubject InquirySubject" +
					 " where InquirySubject.id=(" +
					 "  select Inquiry.subjectId from Inquiry Inquiry where Inquiry.id=" + inquiryId +
					 " )";
		InquirySubject inquirySubject = (InquirySubject)getDatabaseService().findRecordByHql(hql);
		if(inquirySubject.getEndTime()!=null && inquirySubject.getEndTime().before(DateTimeUtils.now())) {
			return; //投票已经结束,不保存投票结果
		}
		if(inquirySubject.getIsAnonymous()!='1') { //实名投票
			//删除之前的投票
			hql = "select InquiryVote" +
				  " from InquiryVote InquiryVote, InquiryOption InquiryOption" + 
				  " where InquiryVote.voterId=" + sessionInfo.getUserId() + 
				  " and InquiryVote.optionId=InquiryOption.id" + 
				  " and InquiryOption.inquiryId=" + inquiryId;
			List inquiryResults = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = (inquiryResults==null ? null : inquiryResults.iterator()); iterator!=null && iterator.hasNext();) {
				InquiryVote inquiryVote = (InquiryVote)iterator.next();
				delete(inquiryVote);
			}
		}
		else if(inquirySubject.getIpRestriction()!='0') { //匿名投票,且需要IP限制
			hql = "select InquiryVote.id" +
				  " from InquiryVote InquiryVote, InquiryOption InquiryOption" + 
				  " where InquiryVote.ip='" + JdbcUtils.resetQuot(ip) + "'" +
				  " and InquiryVote.optionId=InquiryOption.id" + 
				  " and InquiryOption.inquiryId=" + inquiryId +
				  (inquirySubject.getIpRestriction()=='1' ? " and InquiryVote.created>TIMESTAMP(" + (DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -inquirySubject.getIpRestrictionHours()), null)) + ")" : "");
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return;
			}
		}
		String[] options = selectedOptions.split(",");
		for(int i=0; i<options.length; i++) {
			String[] values = options[i].split("@");
			InquiryVote inquiryVote = new InquiryVote();
			inquiryVote.setId(UUIDLongGenerator.generateId()); //ID
			inquiryVote.setOptionId(Long.parseLong(values[0])); //选项ID
			inquiryVote.setSupplement(values.length==1 ? null : values[1].replaceAll("\\[COMMA\\]", ",").replaceAll("\\[SEMICOLON\\]", ";").replaceAll("\\[AT\\]", "@")); //补充说明,supplement.innerHTML.replace(',', '[COMMA]').replace(';', '[SEMICOLON]').replace('@', '[AT]');
			inquiryVote.setIp(ip); //IP
			inquiryVote.setCreated(DateTimeUtils.now()); //投票时间
			if(inquirySubject.getIsAnonymous()!='1') { //实名投票,记录用户信息
				inquiryVote.setVoter(sessionInfo.getUserName()); //投票人
				inquiryVote.setVoterId(sessionInfo.getUserId()); //投票人ID
			}
			save(inquiryVote);
		}
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(inquirySubject, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}
	
	/*
	 * （非 Javadoc）by松溪十大感动人物评选
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#submitInquiryBySingleOption(long, long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void submitInquiryBySingleOption(long inquiryId, long optionId, String ip, SessionInfo sessionInfo) throws ServiceException{
		//获取调查主题
		String hql = "from InquirySubject InquirySubject" +
		" where InquirySubject.id=(" +
		"  select Inquiry.subjectId from Inquiry Inquiry where Inquiry.id=" + inquiryId +
		" )";
		List lazyLoadProperties=new ArrayList();
		lazyLoadProperties.add("inquiries");
		InquirySubject inquirySubject = (InquirySubject)getDatabaseService().findRecordByHql(hql, lazyLoadProperties);
		if(inquirySubject==null){
			throw new ServiceException("该投票主题不存在或已删除"); 
		}
		if(inquirySubject.getIsPublic()=='0'){
			throw new ServiceException("该投票主题未发布，不能进行投票"); 
		}
		if(inquirySubject.getEndTime()!=null && inquirySubject.getEndTime().before(DateTimeUtils.now())) {
			throw new ServiceException("投票已结束"); //投票已经结束,不保存投票结果
		}
		if(inquirySubject.getIsAnonymous()!='1') { //实名投票
			//删除之前的投票
			hql = "select InquiryVote" +
			" from InquiryVote InquiryVote, InquiryOption InquiryOption" + 
			" where InquiryVote.voterId=" + sessionInfo.getUserId() + 
			" and InquiryVote.optionId=InquiryOption.id" + 
			" and InquiryOption.inquiryId=" + inquiryId;
			List inquiryResults = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = (inquiryResults==null ? null : inquiryResults.iterator()); iterator!=null && iterator.hasNext();) {
				InquiryVote inquiryVote = (InquiryVote)iterator.next();
				delete(inquiryVote);
			}
		}
		else if(inquirySubject.getIpRestriction()!='0') { //匿名投票,且需要IP限制
			Inquiry inquiry=(Inquiry) ListUtils.findObjectByProperty(inquirySubject.getInquiries(), "id", new Long(inquiryId));
			if(inquiry==null){
				throw new ServiceException("该项调查未发布或已删除");
			}
			hql="select InquiryVote" +
			" from InquiryVote InquiryVote, InquiryOption InquiryOption" + 
			" where InquiryVote.ip='" + JdbcUtils.resetQuot(ip) + "'" +
			" and InquiryVote.optionId=InquiryOption.id" + 
			" and InquiryOption.inquiryId=" + inquiryId ;
			List votes=getDatabaseService().findRecordsByHql(hql);
			if(inquiry.getIsMultiSelect()=='1'&&votes!=null&&inquiry.getMaxVote()>0&&votes.size()>=inquiry.getMaxVote()){//每个IP最多投票数
				throw new ServiceException("每个IP每项只允许投票"+inquiry.getMaxVote()+"次");
			}
			if(ListUtils.findObjectByProperty(votes, "optionId", new Long(optionId))!=null){
				throw new ServiceException("该选票已投过，不能重复投票");				
			}
			/*
			hql = "select InquiryVote.id" +
			" from InquiryVote InquiryVote, InquiryOption InquiryOption" + 
			" where InquiryVote.ip='" + JdbcUtils.resetQuot(ip) + "'" +
			" and InquiryVote.optionId=InquiryOption.id" + 
			" and InquiryOption.inquiryId=" + inquiryId +
//			时间限制，即多少小时内只能投一次
			(inquirySubject.getIpRestriction()=='1' ? " and InquiryVote.created>TIMESTAMP(" + (DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -inquirySubject.getIpRestrictionHours()), null)) + ")" : "");
			*/
		}
		InquiryVote inquiryVote = new InquiryVote();
		inquiryVote.setId(UUIDLongGenerator.generateId()); //ID
		inquiryVote.setOptionId(optionId); //选项ID
		inquiryVote.setIp(ip); //IP
		inquiryVote.setCreated(DateTimeUtils.now()); //投票时间
		if(inquirySubject.getIsAnonymous()!='1') { //实名投票,记录用户信息
			inquiryVote.setVoter(sessionInfo.getUserName()); //投票人
			inquiryVote.setVoterId(sessionInfo.getUserId()); //投票人ID
		}
		save(inquiryVote);
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(inquirySubject, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#listInquiryVotes(long, long)
	 */
	public List listInquiryVotes(long subjectId, long personId) throws ServiceException {
		String hql = "select InquiryVote" +
					 " from InquiryVote InquiryVote, InquiryOption InquiryOption, Inquiry Inquiry" + 
					 " where InquiryVote.voterId=" + personId + 
					 " and InquiryVote.optionId=InquiryOption.id" + 
					 " and InquiryOption.inquiryId=Inquiry.id" +
					 " and Inquiry.subjectId=" + subjectId;
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#totalInquiryVoter(long)
	 */
	public InquiryVoterTotal totalInquiryVoter(long subjectId) throws ServiceException {
		InquiryVoterTotal voterTotal = new InquiryVoterTotal();
		//获取参与投票的用户
		String hql = "select distinct InquiryVote.voter, InquiryVote.voterId " +
					 " from InquiryVote InquiryVote, InquiryOption InquiryOption, Inquiry Inquiry" +
					 " where InquiryVote.optionId=InquiryOption.id" + 
					 " and InquiryOption.inquiryId=Inquiry.id" +
					 " and Inquiry.subjectId=" + subjectId +
					 " and InquiryVote.voterId>0" +
					 " order by InquiryVote.voter";
		List voters = getDatabaseService().findRecordsByHql(hql);
		if(voters!=null) {
			voterTotal.setVotePersonNumber(voters.size()); //投票参与人员数量
			String names = null;
			for(Iterator iterator = voters.iterator(); iterator.hasNext();) {
				Object[] values = (Object[])iterator.next();
				names = (names==null ? "" : names + "、") + values[0];
			}
			voterTotal.setVotePersonNames(names); //参与投票人员,以"、"分隔
		}
		//获取未参与投票的用户
		hql = "select Person.name" +
			  " from Person Person" +
			  " where not Person.id in (" +
			  "  select distinct InquiryVote.voterId " +
			  "   from InquiryVote InquiryVote, InquiryOption InquiryOption, Inquiry Inquiry" +
			  "   where InquiryVote.optionId=InquiryOption.id" + 
			  "   and InquiryOption.inquiryId=Inquiry.id" +
			  "   and Inquiry.subjectId=" + subjectId + 
			  "   and InquiryVote.voterId>0)" +
			  " order by Person.name";
		List notVotePersons = getDatabaseService().findRecordsByHql(hql);
		voterTotal.setPersonNumber((notVotePersons==null ? 0 : notVotePersons.size()) + voterTotal.getVotePersonNumber()); //总用户数量
		voterTotal.setVotePercent(voterTotal.getVotePersonNumber()==0 ? 0d : (voterTotal.getVotePersonNumber() + 0.0d)/(voterTotal.getPersonNumber() + 0.0d)); //投票参与率
		voterTotal.setNotVotePersonNames(ListUtils.join(notVotePersons, "、", false)); //未参与投票人员,以"、"分隔
		return voterTotal;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#retrieveInquiryResults(com.yuanluesoft.cms.inquiry.pojo.InquirySubject)
	 */
	public void retrieveInquiryResults(InquirySubject inquirySubject) throws ServiceException {
		if(inquirySubject.getInquiries()==null || inquirySubject.getInquiries().isEmpty()) {
			return;
		}
		for(Iterator iterator = inquirySubject.getInquiries().iterator(); iterator.hasNext();) {
			Inquiry inquiry = (Inquiry)iterator.next();
			retrieveInquiryResults(inquiry, true);
		}
	}
	
	/**
	 * 获取单个投票的投票结果
	 * @param inquiry
	 * @param sort
	 * @throws ServiceException
	 */
	private void retrieveInquiryResults(Inquiry inquiry, boolean sort) throws ServiceException {
		//获取选项列表
		String hql = "from InquiryOption InquiryOption" +
					 " where InquiryOption.inquiryId=" + inquiry.getId() +
					 " order by InquiryOption.priority DESC";
		List options = getDatabaseService().findRecordsByHql(hql);
		if(options==null || options.isEmpty()) {
			return;
		}
		//获取调查结果
		hql = "select InquiryVote.optionId, count(InquiryVote.id)" +
			  " from InquiryVote InquiryVote" +
			  " where InquiryVote.optionId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(options, "id", ",", false)) + ")" +
			  " group by InquiryVote.optionId";
		List results = getDatabaseService().findRecordsByHql(hql);
		int total = 0;
		for(int i=0; i<options.size(); i++) {
			InquiryOption inquiryOption = (InquiryOption)options.get(i);
			InquiryResult inquiryResult = new InquiryResult();
			inquiryResult.setOptionId(inquiryOption.getId()); //选项ID
			inquiryResult.setOption(inquiryOption.getInquiryOption()); //选项
			inquiryResult.setNeedSupplement(inquiryOption.getNeedSupplement()); //是否需要补充说明
			//查找调查结果
			for(Iterator iteratorResult = results==null ? null : results.iterator(); iteratorResult!=null && iteratorResult.hasNext();) {
				Object[] values = (Object[])iteratorResult.next();
				if(((Long)values[0]).longValue()==inquiryOption.getId()) {
					inquiryResult.setVoteNumber(((Number)values[1]).intValue());
					break;
				}
			}
			total += inquiryResult.getVoteNumber();
			options.set(i, inquiryResult);
		}
		if(sort) {
			//排序
			Collections.sort(options, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((InquiryResult)o2).getVoteNumber() - ((InquiryResult)o1).getVoteNumber();
				}
			});
		}
		//计算百分比
		for(Iterator iteratorOption = options.iterator(); iteratorOption.hasNext();) {
			InquiryResult inquiryResult = (InquiryResult)iteratorOption.next();
			inquiryResult.setVotePercent(total==0 ? 0 : (inquiryResult.getVoteNumber()+0.0d)/(total + 0.0));
		}
		inquiry.setResults(options); //调查结果
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#retrieveInquiryMatchs(com.yuanluesoft.cms.inquiry.pojo.InquirySubject)
	 */
	public void retrieveInquiryMatchs(InquirySubject inquirySubject) throws ServiceException {
		if(inquirySubject.getInquiries()==null || inquirySubject.getInquiries().isEmpty()) {
			return;
		}
		for(Iterator iterator = inquirySubject.getInquiries().iterator(); iterator.hasNext();) {
			Inquiry inquiry = (Inquiry)iterator.next();
			if(inquiry.getResults()==null || inquiry.getResults().isEmpty()) {
				continue;
			}
			List results = inquiry.getResults().size()<=inquiry.getMaxVote() ? inquiry.getResults() : inquiry.getResults().subList(0, inquiry.getMaxVote());
			//获取匹配的投票数量
			String hql = "select InquiryVote.voter, count(InquiryVote.id)" +
						 " from InquiryVote InquiryVote" +
						 " where InquiryVote.optionId in (" + ListUtils.join(results, "optionId", ",", false) + ")" +
						 " and InquiryVote.voterId>0" +
						 " group by InquiryVote.voter, InquiryVote.voterId" +
						 " order by InquiryVote.voter";
			List matchs = getDatabaseService().findRecordsByHql(hql);
			if(matchs==null || matchs.isEmpty()) {
				continue;
			}
			for(int i=0; i<matchs.size(); i++) {
				Object[] values = (Object[])matchs.get(i);
				InquiryMatch match = new InquiryMatch();
				match.setPersonName((String)values[0]); //用户名
				match.setMatchNumber(((Number)values[1]).intValue()); //匹配数量
				match.setMatchRate((match.getMatchNumber() + 0.0d)/inquiry.getMaxVote()); //匹配率
				matchs.set(i, match);
			}
			//排序
			Collections.sort(matchs, 
					 new Comparator() {
						public int compare(Object o1, Object o2) {
							return ((InquiryMatch)o2).getMatchNumber() - ((InquiryMatch)o1).getMatchNumber();
						}
					});
			inquiry.setMatchs(matchs); //匹配情况
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#retrieveInquiryResults(java.lang.String)
	 */
	public List retrieveInquiryResults(final String inquiryIds) throws ServiceException {
		//按调查ID获取对应的调查主题列表
		String hql = "select InquirySubject" +
					 " from InquirySubject InquirySubject" +
					 " where InquirySubject.id in (" +
					 " 	select Inquiry.subjectId" +
					 "   from Inquiry Inquiry" +
					 " 	 where Inquiry.id in (" + JdbcUtils.validateInClauseNumbers(inquiryIds) + ")" +
					 ")";
		List inquirySubjects = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("inquiries"));
		Collections.sort(inquirySubjects, new Comparator() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object arg0, Object arg1) {
				InquirySubject inquirySubject0 = (InquirySubject)arg0;
				InquirySubject inquirySubject1 = (InquirySubject)arg1;
				int index0 = indexOfInquiry(inquirySubject0);
				int index1 = indexOfInquiry(inquirySubject1);
				return (index0==index1 ? 0 : (index0<index1 ? -1 : 1));
			}
			/**
			 * 调查定位
			 * @param inquirySubject
			 * @return
			 */
			private int indexOfInquiry(InquirySubject inquirySubject) {
				int inquiryIndex = Integer.MAX_VALUE;
				for(Iterator iterator = inquirySubject.getInquiries().iterator(); iterator.hasNext();) {
					Inquiry inquiry = (Inquiry)iterator.next();
					int index = ("," + inquiryIds + ",").indexOf("," + inquiry.getId() + ",");
					if(index!=-1 && index<inquiryIndex) {
						inquiryIndex = index;
					}
				}
				return inquiryIndex;
			}
		});
		for(Iterator iterator = inquirySubjects.iterator(); iterator.hasNext();) {
			InquirySubject inquirySubject = (InquirySubject)iterator.next();
			retrieveInquiryResults(inquirySubject);
		}
		return inquirySubjects;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#writeInquiryResultChart(long, int, int, java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void writeInquiryResultChart(long inquiryId, int chartWidth, int chartHeight, String chartMode, HttpServletResponse response) throws ServiceException {
		//获取投票
		Inquiry inquiry = (Inquiry)getDatabaseService().findRecordById(Inquiry.class.getName(), inquiryId);
		InquirySubject inquirySubject = (InquirySubject)inquiry.getInquirySubject();
		if(inquirySubject.getPublishResult()!='2' && inquirySubject.getEndTime()!=null && inquirySubject.getEndTime().after(DateTimeUtils.now())) { //不是总是公开且投票未结束
			return;
		}
		retrieveInquiryResults(inquiry, false);
		List chartDataList = new ArrayList();
		for(Iterator iterator = inquiry.getResults()==null ? null : inquiry.getResults().iterator(); iterator!=null && iterator.hasNext();) {
			InquiryResult inquiryResult = (InquiryResult)iterator.next();
			chartDataList.add(new ChartData(inquiryResult.getOption(), inquiryResult.getVoteNumber()));
		}
		graphReportService.writeChart(chartDataList, chartWidth, chartHeight, chartMode, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#setVoteNumber(long, int)
	 */
	public void setVoteNumber(long optionId, int count) throws ServiceException {
		String hql = "select count(InquiryVote.id) from InquiryVote InquiryVote where InquiryVote.optionId=" + optionId;
		Number voteCount = (Number)getDatabaseService().findRecordByHql(hql);
		if(voteCount!=null && voteCount.intValue()>count) { //超过需要的数量
			//删除投票记录
			hql = "from InquiryVote InquiryVote" +
				  " where InquiryVote.optionId=" + optionId +
				  " order by InquiryVote.ip DESC, InquiryVote.id";
			for(int i=count; ;) {
				List votes = getDatabaseService().findRecordsByHql(hql, i, 100); //每次删除100个
				if(votes==null || votes.isEmpty()) {
					break;
				}
				for(Iterator iterator = votes.iterator(); iterator.hasNext();) {
					InquiryVote vote = (InquiryVote)iterator.next();
					delete(vote);
				}
				if(votes.size()<100) {
					break;
				}
			}
		}
		else { //数量不够
			//添加投票记录
			for(int i=(voteCount==null ? 0 : voteCount.intValue()); i<count; i++) {
				InquiryVote vote = new InquiryVote();
				vote.setId(UUIDLongGenerator.generateId()); //ID
				vote.setIp("0");
				vote.setOptionId(optionId);
				//vote.setVoter(voter); //投票人姓名
				//vote.setVoterId(voterId); //投票人ID
				save(vote);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#hasFeedback(long)
	 */
	public boolean hasFeedback(long subjectId) throws ServiceException {
		String hql = "select InquiryFeedback.id from InquiryFeedback InquiryFeedback where InquiryFeedback.subjectId=" + subjectId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#getInquiryFeedback(java.lang.String)
	 */
	public InquiryFeedback getInquiryFeedback(String inquiryIds) throws ServiceException {
		String hql = "select InquiryFeedback" +
					 " from InquiryFeedback InquiryFeedback, Inquiry Inquiry" +
					 " where InquiryFeedback.subjectId=Inquiry.subjectId" +
					 " and Inquiry.id=" + inquiryIds.split(",")[0];
		return (InquiryFeedback)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryService#getInquiryFeedback(long)
	 */
	public InquiryFeedback getInquiryFeedback(long subjectId) throws ServiceException {
		String hql = "from InquiryFeedback InquiryFeedback where InquiryFeedback.subjectId=" + subjectId;
		return (InquiryFeedback)getDatabaseService().findRecordByHql(hql);
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
	 * @return the graphReportService
	 */
	public GraphReportService getGraphReportService() {
		return graphReportService;
	}

	/**
	 * @param graphReportService the graphReportService to set
	 */
	public void setGraphReportService(GraphReportService graphReportService) {
		this.graphReportService = graphReportService;
	}
}