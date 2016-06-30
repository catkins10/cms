package com.yuanluesoft.job.apply.service.spring;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.job.apply.pojo.JobApply;
import com.yuanluesoft.job.apply.pojo.JobFavorite;
import com.yuanluesoft.job.apply.service.JobApplyService;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.pojo.JobCompanyMailTemplate;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.job.company.service.JobParameterService;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class JobApplyServiceImpl extends BusinessServiceImpl implements JobApplyService {
	private JobCompanyService jobCompanyService; //企业服务
	private JobTalentService jobTalentService; //人才服务
	private JobParameterService jobParameterService; //参数配置服务
	private MailService mailService; //邮件服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof JobApply) {
			JobApply apply = (JobApply)record;
			apply.setJob((Job)jobCompanyService.load(Job.class, apply.getJobId()));
			apply.setTalent((JobTalent)jobTalentService.load(JobTalent.class, apply.getTalentId()));
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof JobApply) {
			JobApply apply = (JobApply)record;
			Job job = (Job)jobCompanyService.load(Job.class, apply.getJobId());
			job.setApplicantCount(job.getApplicantCount() + 1); //投递次数
			jobCompanyService.update(job);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.apply.service.JobApplyService#sendInvitationMail(long, java.lang.String, java.lang.String)
	 */
	public void sendInvitationMail(long companyId, String email, String mailContent) throws ServiceException {
		sendMail(companyId, email, mailContent, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.apply.service.JobApplyService#sendOfferMail(long, java.lang.String, java.lang.String)
	 */
	public void sendOfferMail(long companyId, String email, String mailContent) throws ServiceException {
		sendMail(companyId, email, mailContent, true);
	}

	/**
	 * 发送邮件
	 * @param companyId
	 * @param email
	 * @param mailContent
	 * @param isOffer
	 * @throws ServiceException
	 */
	private void sendMail(long companyId, String email, String mailContent, boolean isOffer) throws ServiceException {
		JobCompanyMailTemplate mailTemplate = jobParameterService.getMailTemplateByCompanyId(companyId);
		if(mailTemplate==null) {
			mailTemplate = jobParameterService.getMailTemplateByCompanyId(0);
		}
		String subject = mailTemplate==null ? (isOffer ? "录用通知" : "邀请函") : (isOffer ? mailTemplate.getOfferMailSubject() : mailTemplate.getInvitationMailSubject());
		mailService.sendMail(0, null, 0, email, subject, StringUtils.generateHtmlContent(mailContent), false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.apply.service.JobApplyService#addFavorite(long, long)
	 */
	public void addFavorite(long talentId, long jobId) throws ServiceException {
		if(isFavorite(talentId, jobId)) {
			return;
		}
		Job job = (Job)jobCompanyService.load(Job.class, jobId);
		JobFavorite favorite = new JobFavorite();
		favorite.setId(UUIDLongGenerator.generateId()); //ID
		favorite.setJobId(jobId); //职位ID
		favorite.setJobName(job.getName()); //职位名称
		favorite.setCompanyId(job.getCompanyId()); //公司ID
		favorite.setCompany(job.getCompany().getName()); //公司名称
		favorite.setTalentId(talentId); //求职人ID
		favorite.setDepartment(job.getDepartment()); //招聘部门
		favorite.setRecruitNumber(job.getRecruitNumber()); //招聘人数
		favorite.setPost(job.getPost()); //职能类别名称
		favorite.setLanguage(job.getLanguage()); //语言要求,如:英语熟练
		favorite.setMonthlyPayRange(job.getMonthlyPayRange()); //月薪
		favorite.setTarget(job.getTarget()); //招聘对象,社会人士、应届毕业生、实习生、劳务工
		favorite.setQualification(job.getQualification()); //学历要求,初中,高中,职业高中,职业中专,中专,大专,本科,MBA,硕士,博士
		favorite.setWorkYear(job.getWorkYear()); //工作年限,在读学生/-1,应届毕业生/0,1年,2年,3年,4年,5年,6年,7年,8年,9年,10年以上,不限
		favorite.setAgeRange(job.getAgeRange()); //年龄
		favorite.setSex(job.getSex()); //性别要求,不限制/A,男/M,女/F
		favorite.setRank(job.getRank()); //职称要求,不限,初级职称,中级职称,副高级职称,高级职称
		favorite.setEndDate(job.getEndDate()); //到期时间
		favorite.setCreated(DateTimeUtils.now()); //收藏时间
		save(favorite);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.apply.service.JobApplyService#removeFavorite(long, long)
	 */
	public void removeFavorite(long talentId, long jobId) throws ServiceException {
		String hql = "from JobFavorite JobFavorite" +
					 " where JobFavorite.talentId=" + talentId +
					 " and JobFavorite.jobId=" + jobId;
		getDatabaseService().deleteRecordsByHql(hql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.job.apply.service.JobApplyService#isFavorite(long, long)
	 */
	public boolean isFavorite(long talentId, long jobId) throws ServiceException {
		//检查是否收藏过
		String hql = "select JobFavorite.id" +
					 " from JobFavorite JobFavorite" +
					 " where JobFavorite.talentId=" + talentId +
					 " and JobFavorite.jobId=" + jobId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/**
	 * @return the jobCompanyService
	 */
	public JobCompanyService getJobCompanyService() {
		return jobCompanyService;
	}
	/**
	 * @param jobCompanyService the jobCompanyService to set
	 */
	public void setJobCompanyService(JobCompanyService jobCompanyService) {
		this.jobCompanyService = jobCompanyService;
	}
	/**
	 * @return the jobTalentService
	 */
	public JobTalentService getJobTalentService() {
		return jobTalentService;
	}
	/**
	 * @param jobTalentService the jobTalentService to set
	 */
	public void setJobTalentService(JobTalentService jobTalentService) {
		this.jobTalentService = jobTalentService;
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
}