package com.yuanluesoft.job.company.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPoolException;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.pojo.JobArea;
import com.yuanluesoft.job.company.pojo.JobCompany;
import com.yuanluesoft.job.company.pojo.JobCompanyApproval;
import com.yuanluesoft.job.company.pojo.JobCompanyIndustry;
import com.yuanluesoft.job.company.pojo.JobPush;
import com.yuanluesoft.job.company.pojo.JobType;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.job.company.service.JobParameterService;
import com.yuanluesoft.job.talent.pojo.JobTalent;

/**
 * 
 * @author linchuan
 *
 */
public class JobCompanyServiceImpl extends BusinessServiceImpl implements JobCompanyService {
	private PageService pageService; //页面服务
	private CryptService cryptService; //加密服务
	private JobParameterService jobParameterService; //参数配置服务
	private MailService mailService; //邮件服务
	private ViewDefineService viewDefineService;
	private ThreadPool pushThreadPool = new ThreadPool(30, 0, 120000); //推送线程池
	private int maxPush = 500; //推送上限

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof Job) {
			Job job = (Job)record;
			if(job.getTypes()!=null && !job.getTypes().isEmpty()) {
				job.setTypeArray(new int[job.getTypes().size()]);
				int i = 0;
				for(Iterator iterator = job.getTypes().iterator(); iterator.hasNext();) {
					JobType type = (JobType)iterator.next();
					job.getTypeArray()[i++] = type.getType();
				}
			}
			job.setAreaIds(ListUtils.join(job.getAreas(), "areaId", ",", false));
			job.setAreaNames(ListUtils.join(job.getAreas(), "area", ",", false));
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof JobCompany) {
			JobCompany company = (JobCompany)record;
			company.setLoginName(company.getLoginName().toLowerCase());
			company.setPassword(cryptService.encrypt(company.getPassword(), "" + company.getId(), true));
			JobCompanyApproval jobCompanyApproval = jobParameterService.getJobCompanyApproval();
			if(jobCompanyApproval!=null && jobCompanyApproval.getApprovalRequired()==0) { //企业信息不需要审核
				company.setStatus(1); //设为已审核通过
			}
		}
		record = super.save(record);
		if((record instanceof JobCompany) && ((JobCompany)record).getStatus()==1) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		if(record instanceof Job) {
			Job job = (Job)record;
			saveJobComponents(job); //保存组成部分
			if(job.getIsPublic()==1) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof JobCompany) {
			JobCompany company = (JobCompany)record;
			company.setLoginName(company.getLoginName().toLowerCase());
			company.setPassword(cryptService.encrypt(company.getPassword(), "" + company.getId(), true));
		}
		record = super.update(record);
		if((record instanceof JobCompany) && ((JobCompany)record).getStatus()==1) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		if(record instanceof Job) {
			saveJobComponents((Job)record); //保存组成部分
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return record;
	}
	
	/**
	 * 保存组成部分
	 * @param job
	 * @throws ServiceException
	 */
	private void saveJobComponents(Job job) throws ServiceException {
		if(job.getTypeArray()!=null) {
			for(Iterator iterator = job.getTypes()==null ? null : job.getTypes().iterator(); iterator!=null && iterator.hasNext();) {
				JobType type = (JobType)iterator.next();
				getDatabaseService().deleteRecord(type);
				iterator.remove();
			}
			if(job.getTypes()==null) {
				job.setTypes(new LinkedHashSet());
			}
			for(int i=0; i<job.getTypeArray().length; i++) {
				JobType jobType = new JobType();
				jobType.setId(UUIDLongGenerator.generateId());
				jobType.setJobId(job.getId());
				jobType.setType(job.getTypeArray()[i]);
				save(jobType);
				job.getTypes().add(jobType);
			}
		}
		if(job.getAreaIds()!=null) {
			for(Iterator iterator = job.getAreas()==null ? null : job.getAreas().iterator(); iterator!=null && iterator.hasNext();) {
				JobArea area = (JobArea)iterator.next();
				getDatabaseService().deleteRecord(area);
				iterator.remove();
			}
			if(job.getAreas()==null) {
				job.setAreas(new LinkedHashSet());
			}
			String[] ids = job.getAreaIds().split(",");
			String[] names = job.getAreaNames().split(",");
			for(int i=0; i<ids.length; i++) {
				JobArea area = new JobArea();
				area.setId(UUIDLongGenerator.generateId()); //ID
				area.setJobId(job.getId()); //职位ID
				area.setAreaId(Long.parseLong(ids[i])); //地区ID
				area.setArea(names[i]); //地区
				getDatabaseService().saveRecord(area);
				job.getAreas().add(area);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if((record instanceof Job) || (record instanceof JobCompany)) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#upadteCompanyIndustries(com.yuanluesoft.job.company.pojo.JobCompany, java.lang.String, java.lang.String)
	 */
	public void updateCompanyIndustries(JobCompany company, String industryIds, String industryNames) throws ServiceException {
		if(industryIds==null) {
			return;
		}
		for(Iterator iterator = company.getIndustries()==null ? null : company.getIndustries().iterator(); iterator!=null && iterator.hasNext();) {
			JobCompanyIndustry industry = (JobCompanyIndustry)iterator.next();
			getDatabaseService().deleteRecord(industry);
			iterator.remove();
		}
		if(company.getIndustries()==null) {
			company.setIndustries(new LinkedHashSet());
		}
		String[] ids = industryIds.split(",");
		String[] names = industryNames.split(",");
		for(int i=0; i<ids.length; i++) {
			JobCompanyIndustry industry = new JobCompanyIndustry();
			industry.setId(UUIDLongGenerator.generateId()); //ID
			industry.setCompanyId(company.getId()); //企业ID
			industry.setIndustryId(Long.parseLong(ids[i])); //行业ID
			industry.setIndustry(names[i]); //行业
			getDatabaseService().saveRecord(industry);
			company.getIndustries().add(industry);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#refreshJob(com.yuanluesoft.job.company.pojo.Job)
	 */
	public void refreshJob(Job job) throws ServiceException {
		Date today = DateTimeUtils.date();
		if(job.getRefreshTime()==null || job.getRefreshTime().before(today)) {
			job.setTodayRefreshTimes(1);
		}
		else if(job.getTodayRefreshTimes()>=jobParameterService.getJobRefreshLimitation()) {
			return;
		}
		else {
			job.setTodayRefreshTimes(job.getTodayRefreshTimes() + 1);
		}
		job.setRefreshTime(DateTimeUtils.now());
		update(job);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#getLastRefreshTime(long)
	 */
	public Timestamp getLastRefreshTime(long companyId) throws ServiceException {
		String hql = "select max(Job.refreshTime)" +
					 " from Job Job" +
					 " where Job.companyId=" + companyId;
		return (Timestamp)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#getRefreshTimesLeft(long)
	 */
	public int getRefreshTimesLeft(long companyId) throws ServiceException {
		String hql = "select min(Job.todayRefreshTimes)" +
					 " from Job Job" +
					 " where Job.companyId=" + companyId +
					 " and Job.refreshTime>=DATE(" + DateTimeUtils.formatDate(DateTimeUtils.date(), null) + ")";
		Number todayRefreshTimes = (Number)getDatabaseService().findRecordByHql(hql);
		return jobParameterService.getJobRefreshLimitation() - (todayRefreshTimes==null ? 0 : todayRefreshTimes.intValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#refreshAllJobs(long)
	 */
	public void refreshAllJobs(long companyId) throws ServiceException {
		String hql = " from Job Job" +
					 " where Job.companyId=" + companyId +
					 " and Job.isPublic=1" +
					 " order by Job.id";
		for(int i=0; ; i+=200) {
			List jobs = getDatabaseService().findRecordsByHql(hql, i, 200);
			for(Iterator iterator = jobs==null ? null : jobs.iterator(); iterator!=null && iterator.hasNext();) {
				Job job = (Job)iterator.next();
				refreshJob(job);
			}
			if(jobs==null || jobs.size()<200) {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#approvalCompany(com.yuanluesoft.job.company.pojo.JobCompany, boolean, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalCompany(JobCompany company, boolean pass, String failedReason, SessionInfo sessionInfo) throws ServiceException {
		company.setStatus(pass ? 1 : 2);
		company.setApprovalTime(DateTimeUtils.now());
		company.setApproverId(sessionInfo.getUserId());
		company.setApprover(sessionInfo.getUserName());
		if(!pass) {
			company.setFailedReason(failedReason);
		}
		update(company);
		//发送邮件
		JobCompanyApproval jobCompanyApproval = jobParameterService.getJobCompanyApproval();
		if(jobCompanyApproval==null) {
			return;
		}
		String mailTemplate = pass ? jobCompanyApproval.getPassMailTemplate() : jobCompanyApproval.getFailedMailTemplate();
		mailTemplate = mailTemplate.replaceAll("<企业名称>", company.getName())
								   .replaceAll("<未通过原因>", company.getFailedReason()==null ? "" : company.getFailedReason())
								   .replaceAll("<发送时间>", DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd"));
		try {
			mailService.sendMail(0, null, 0, company.getEmail(), (pass ? jobCompanyApproval.getPassMailSubject() : jobCompanyApproval.getFailedMailSubject()), StringUtils.generateHtmlContent(mailTemplate), false);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.job.company.service.JobCompanyService#pushJob(com.yuanluesoft.job.company.pojo.Job, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void pushJob(final Job job, final String mailSubject, final String mailContent, final String receiverIds,final String receivers, final SessionInfo sessionInfo, final HttpServletRequest request) throws ServiceException {
		final List talents;
		if(receiverIds!=null && !receiverIds.isEmpty()) {
			talents = getDatabaseService().findRecordsByHql("from JobTalent JobTalent where JobTalent.id in (" + JdbcUtils.resetQuot(receiverIds) + ")");
		}
		else {
			try {
				View view = viewDefineService.getView("job/talent", "pushTalent", sessionInfo);
				ViewService viewService = ViewUtils.getViewService(view);
				//构造视图包
				ViewPackage viewPackage = new ViewPackage();
				view.setPageRows(maxPush); //最多发送500个邮件
				viewPackage.setView(view);
				viewPackage.setCurPage(1);
				//获取记录
				request.setAttribute("jobId", new Long(job.getId()));
				viewService.retrieveViewPackage(viewPackage, view, 0, true, false, false, request, sessionInfo);
				talents = viewPackage.getRecords();
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
		}
		if(talents==null || talents.isEmpty()) {
			return;
		}
		JobPush jobPush = new JobPush();
		jobPush.setId(UUIDLongGenerator.generateId()); //ID
		jobPush.setJobId(job.getId()); //职位ID
		jobPush.setJobName(job.getName()); //职位名称
		jobPush.setCompanyId(job.getCompanyId()); //企业ID
		jobPush.setCompanyName(job.getCompany().getName()); //企业名称
		jobPush.setPushTime(DateTimeUtils.now()); //推送时间
		jobPush.setReceiverIds(StringUtils.slice(receiverIds==null || receiverIds.isEmpty() ? ListUtils.join(talents, "id", ",", true) : receiverIds, 4000, ".")); //接收人ID
		jobPush.setReceivers(StringUtils.slice(receiverIds==null || receiverIds.isEmpty() ? ListUtils.join(talents, "name", ",", true) : receivers, 4000, ".")); //接收人
		jobPush.setPusherId(sessionInfo.getUserId()); //推送人ID
		jobPush.setPusher(sessionInfo.getUserName()); //推送人
		save(jobPush);
		//发邮件
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		String url = RequestUtils.getRequestURL(request, false);
		url = url.substring(0, url.indexOf('/', url.indexOf("://") + 3)) + request.getContextPath() + "/";
		final String jobLink = "<a href=\"" + url + "job/company/job.shtml?id=" + job.getId() + (siteId>0 ? "&siteId=" + siteId : "") + "\">查看职位</a>";
		final String applyLink = "<a href=\"" + url + "job/apply/apply.shtml?jobId=" + job.getId() + (siteId>0 ? "&siteId=" + siteId : "") + "\">申请职位</a>";
		final String cancelLink = "<a href=\"" + url + "job/talent/cancelPush.shtml?talentId=<talentId>" + (siteId>0 ? "&siteId=" + siteId : "") + "\">退订</a>";
		try {
			pushThreadPool.execute(new Task() {
				public void process() {
					try {
						sendPushJobMails(job, mailSubject, mailContent, talents, jobLink, applyLink, cancelLink);
					
					}catch(Exception e) {
						Logger.exception(e);
					}
				}
			});
		}
		catch (ThreadPoolException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 发送推送邮件
	 * @param job
	 * @param mailSubject
	 * @param mailContent
	 * @param talents
	 * @throws Exception
	 */
	private void sendPushJobMails(Job job, String mailSubject, String mailContent, List talents, String jobLink, String applyLink, String cancelLink) throws Exception {
		mailContent = StringUtils.generateHtmlContent(mailContent);
		for(Iterator iterator = talents.iterator(); iterator.hasNext();) {
			JobTalent talent = (JobTalent)iterator.next();
			try {
				String content = mailContent.replaceAll("&lt;求职人姓名&gt;", talent.getName())
				   							.replaceAll("&lt;职位链接&gt;", jobLink)
				   							.replaceAll("&lt;求职链接&gt;", applyLink)
				   							.replaceAll("&lt;取消订阅链接&gt;", cancelLink.replaceFirst("<talentId>", Encoder.getInstance().desEncode("" + talent.getId(), "20050718", "utf-8", null)));
				mailService.sendMail(0, null, 0, talent.getEmail(), mailSubject, content, false);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		JobCompany company = getCompanyByLoginName(loginName);
		if(company==null) {
			return false;
		}
		if(validateOldPassword && !cryptService.encrypt(oldPassword, "" + company.getId(), true).equals(company.getPassword())) {
			throw new WrongPasswordException(); //密码错误
		}
		company.setPassword(newPassword); //加密口令
		update(company);
		return true;
	}
	
	/**
	 * 按登录用户名获取企业
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	private JobCompany getCompanyByLoginName(String loginName) throws ServiceException {
		String hql = "from JobCompany JobCompany" +
					 " where JobCompany.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'" +
					 " and JobCompany.status<2";
		return (JobCompany)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			JobCompany company = getCompanyByLoginName(loginName);
			if(company==null) {
				return null;
			}
			//设置用户信息
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setLoginName(company.getLoginName());
			sessionInfo.setUserType(PERSON_TYPE_JOB_COMPANY);
			sessionInfo.setInternalUser(false); //不是内部用户
			sessionInfo.setPassword(cryptService.decrypt(company.getPassword(), "" + company.getId(), true));
			sessionInfo.setUserName(company.getName());
			sessionInfo.setUserId(company.getId());
			sessionInfo.setDepartmentId(company.getId());
			sessionInfo.setDepartmentName(company.getName());
			sessionInfo.setUnitId(company.getId());
			sessionInfo.setUnitName(company.getName());
			//设置部门信息
			sessionInfo.setDepartmentIds("0," + company.getId());
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		JobCompany company = (JobCompany)load(JobCompany.class, memberId);
		if(company==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, company);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PERSON_TYPE_JOB_COMPANY);
		member.setMailAddress(company.getEmail());
		member.setOriginalRecord(company);
		return member;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		String hql = "select JobCompany.id" +
					 " from JobCompany JobCompany" +
					 " where JobCompany.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'" +
					 " and JobCompany.status<2"; //未审核或者已经审核通过
		Number id = (Number)getDatabaseService().findRecordByHql(hql);
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.isEmpty() || password==null || password.isEmpty()) {
    		return null;
    	}
		JobCompany company = getCompanyByLoginName(loginName);
	   	if(company==null) {
    		return null;
    	}
	   	if(company.getStatus()!=1) {
	   		throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT); //帐号停用
	   	}
	   	String correctPassword = cryptService.decrypt(company.getPassword(), "" + company.getId(), true); //正确的密码
		return new MemberLogin(company.getLoginName(), company.getId(), PERSON_TYPE_JOB_COMPANY, correctPassword, !passwordMatcher.matching(correctPassword, password));
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
	 * @return the jobParameterService
	 */
	public JobParameterService getJobParameterService() {
		return jobParameterService;
	}

	/**
	 * @param jobParameterService the jobParameterService to set
	 */
	public void setJobParameterService(JobParameterService jobParameterService) {
		this.jobParameterService = jobParameterService;
	}

	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}

	/**
	 * @return the maxPush
	 */
	public int getMaxPush() {
		return maxPush;
	}

	/**
	 * @param maxPush the maxPush to set
	 */
	public void setMaxPush(int maxPush) {
		this.maxPush = maxPush;
	}
}