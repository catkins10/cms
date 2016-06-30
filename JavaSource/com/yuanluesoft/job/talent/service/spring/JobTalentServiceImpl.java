package com.yuanluesoft.job.talent.service.spring;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

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
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.job.company.service.JobParameterService;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.pojo.JobTalentApproval;
import com.yuanluesoft.job.talent.pojo.JobTalentEmployment;
import com.yuanluesoft.job.talent.pojo.JobTalentIntention;
import com.yuanluesoft.job.talent.pojo.JobTalentIntentionArea;
import com.yuanluesoft.job.talent.pojo.JobTalentIntentionCompanyType;
import com.yuanluesoft.job.talent.pojo.JobTalentIntentionIndustry;
import com.yuanluesoft.job.talent.pojo.JobTalentIntentionPost;
import com.yuanluesoft.job.talent.pojo.JobTalentIntentionType;
import com.yuanluesoft.job.talent.pojo.JobTalentReport;
import com.yuanluesoft.job.talent.pojo.JobTalentSchooling;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class JobTalentServiceImpl extends BusinessServiceImpl implements JobTalentService {
	private CryptService cryptService; //加密服务
	private JobParameterService jobParameterService; //参数配置服务
	private MailService mailService; //邮件服务
	private PersonService personService; //用户服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof JobTalentIntention) {
			initIntentionComponents((JobTalentIntention)record); //初始化求职意向组成部分
		}
		else if(record instanceof JobTalent) {
			JobTalent talent = (JobTalent)record;
			for(Iterator iterator = talent.getIntentions()==null ? null : talent.getIntentions().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntention intention = (JobTalentIntention)iterator.next();
				initIntentionComponents(intention); //初始化求职意向组成部分
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof JobTalent) {
			JobTalent talent = (JobTalent)record;
			talent.setEmail(talent.getEmail().toLowerCase());
			talent.setPassword(cryptService.encrypt(talent.getPassword(), "" + talent.getId(), true));
			JobTalentApproval jobTalentApproval = jobParameterService.getJobTalentApproval();
			if(jobTalentApproval!=null && jobTalentApproval.getApprovalRequired()==0) { //人才不需要审核
				talent.setStatus(1); //设为已审核通过
			}
		}
		else if(record instanceof JobTalentIntention) {
			saveIntentionComponents((JobTalentIntention)record);
		}
		else if(record instanceof JobTalentEmployment) { //就业调查
			JobTalentEmployment employment = (JobTalentEmployment)record;
			employment.setIsNewest(1);
			String hql = "from JobTalentEmployment JobTalentEmployment" +
						 " where JobTalentEmployment.talentId=" + employment.getTalentId() +
						 " and JobTalentEmployment.isNewest=1";
			employment = (JobTalentEmployment)getDatabaseService().findRecordByHql(hql);
			if(employment!=null) {
				employment.setIsNewest(0);
				update(employment);
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof JobTalent) {
			JobTalent talent = (JobTalent)record;
			talent.setEmail(talent.getEmail().toLowerCase());
			talent.setPassword(cryptService.encrypt(talent.getPassword(), "" + talent.getId(), true));
		}
		else if(record instanceof JobTalentIntention) {
			saveIntentionComponents((JobTalentIntention)record);
		}
		record = super.update(record);
		if(!(record instanceof JobTalent) || ((JobTalent)record).getStatus()!=1) { //未通过审核
			return record;
		}
		JobTalent talent = (JobTalent)record;
		for(Iterator iterator = talent.getSchoolings()==null ? null : talent.getSchoolings().iterator(); iterator!=null && iterator.hasNext();) {
			JobTalentSchooling schooling = (JobTalentSchooling)iterator.next();
			if(schooling.getSchoolClassId()<=0) {
				continue;
			}
			personService.addPersonLink(talent.getId(), JobTalent.class.getName(), talent.getName(), talent.getEmail(), null, talent.getSex(), null, null, talent.getCell(), null, talent.getEmail(), "" + schooling.getSchoolClassId(), 0, null);
			break;
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if((record instanceof JobTalentEmployment) && ((JobTalentEmployment)record).getIsNewest()==1) { //就业调查
			String hql = "from JobTalentEmployment JobTalentEmployment" +
						 " where JobTalentEmployment.talentId=" + ((JobTalentEmployment)record).getTalentId() +
						 " order by JobTalentEmployment.created DESC";
			JobTalentEmployment employment = (JobTalentEmployment)getDatabaseService().findRecordByHql(hql);
			if(employment!=null) {
				employment.setIsNewest(1);
				update(employment);
			}
		}
		if(record instanceof JobTalent) {
			Person person = personService.getPerson(record.getId());
			if(person!=null) {
				personService.delete(person);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.talent.service.JobTalentService#approvalTalent(com.yuanluesoft.job.talent.pojo.JobTalent, boolean, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalTalent(JobTalent talent, boolean pass, String failedReason, SessionInfo sessionInfo) throws ServiceException {
		talent.setStatus(pass ? 1 : 2);
		talent.setApprovalTime(DateTimeUtils.now());
		talent.setApproverId(sessionInfo.getUserId());
		talent.setApprover(sessionInfo.getUserName());
		if(!pass) {
			talent.setFailedReason(failedReason);
		}
		update(talent);
		//发送邮件
		JobTalentApproval jobTalentApproval = jobParameterService.getJobTalentApproval();
		if(jobTalentApproval==null) {
			return;
		}
		String mailTemplate = pass ? jobTalentApproval.getPassMailTemplate() : jobTalentApproval.getFailedMailTemplate();
		mailTemplate = mailTemplate.replaceAll("<人才姓名>", talent.getName()==null ? talent.getEmail() : talent.getName())
								   .replaceAll("<未通过原因>", talent.getFailedReason()==null ? "" : talent.getFailedReason())
								   .replaceAll("<发送时间>", DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd"));
		try {
			mailService.sendMail(0, null, 0, talent.getEmail(), (pass ? jobTalentApproval.getPassMailSubject() : jobTalentApproval.getFailedMailSubject()), StringUtils.generateHtmlContent(mailTemplate), false);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/**
	 * 初始化求职意向组成部分
	 * @param intention
	 */
	private void initIntentionComponents(JobTalentIntention intention) {
		if(intention.getTypes()!=null && !intention.getTypes().isEmpty()) {
			intention.setTypeArray(new int[intention.getTypes().size()]);
			int i = 0;
			for(Iterator iterator = intention.getTypes().iterator(); iterator.hasNext();) {
				JobTalentIntentionType type = (JobTalentIntentionType)iterator.next();
				intention.getTypeArray()[i++] = type.getType();
			}
		}
		intention.setAreaIds(ListUtils.join(intention.getAreas(), "areaId", ",", false));
		intention.setAreaNames(ListUtils.join(intention.getAreas(), "area", ",", false));
		intention.setIndustryIds(ListUtils.join(intention.getIndustries(), "industryId", ",", false));
		intention.setIndustryNames(ListUtils.join(intention.getIndustries(), "industry", ",", false));
		intention.setPostIds(ListUtils.join(intention.getPosts(), "postId", ",", false));
		intention.setPostNames(ListUtils.join(intention.getPosts(), "post", ",", false));
		if(intention.getCompanyTypes()!=null && !intention.getCompanyTypes().isEmpty()) {
			intention.setCompanyTypeArray(new String[intention.getCompanyTypes().size()]);
			int i = 0;
			for(Iterator iterator = intention.getCompanyTypes().iterator(); iterator.hasNext();) {
				JobTalentIntentionCompanyType type = (JobTalentIntentionCompanyType)iterator.next();
				intention.getCompanyTypeArray()[i++] = type.getCompanyType();
			}
		}
	}
	
	/**
	 * 保存求职意向组成部分
	 * @param job
	 * @param isNew
	 * @throws ServiceException
	 */
	private void saveIntentionComponents(JobTalentIntention intention) throws ServiceException {
		if(intention.getTypeArray()!=null) {
			for(Iterator iterator = intention.getTypes()==null ? null : intention.getTypes().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntentionType type = (JobTalentIntentionType)iterator.next();
				getDatabaseService().deleteRecord(type);
				iterator.remove();
			}
			if(intention.getTypes()==null) {
				intention.setTypes(new LinkedHashSet());
			}
			for(int i=0; i<intention.getTypeArray().length; i++) {
				JobTalentIntentionType intentionType = new JobTalentIntentionType();
				intentionType.setId(UUIDLongGenerator.generateId());
				intentionType.setIntentionId(intention.getId());
				intentionType.setType(intention.getTypeArray()[i]);
				save(intentionType);
				intention.getTypes().add(intentionType);
			}
		}
		if(intention.getAreaIds()!=null) {
			for(Iterator iterator = intention.getAreas()==null ? null : intention.getAreas().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntentionArea area = (JobTalentIntentionArea)iterator.next();
				getDatabaseService().deleteRecord(area);
				iterator.remove();
			}
			if(intention.getAreas()==null) {
				intention.setAreas(new LinkedHashSet());
			}
			String[] ids = intention.getAreaIds().split(",");
			String[] names = intention.getAreaNames().split(",");
			for(int i=0; i<ids.length; i++) {
				JobTalentIntentionArea area = new JobTalentIntentionArea();
				area.setId(UUIDLongGenerator.generateId()); //ID
				area.setIntentionId(intention.getId()); //职位ID
				area.setAreaId(Long.parseLong(ids[i])); //地区ID
				area.setArea(names[i]); //地区
				getDatabaseService().saveRecord(area);
				intention.getAreas().add(area);
			}
		}
		if(intention.getIndustryIds()!=null) {
			for(Iterator iterator = intention.getIndustries()==null ? null : intention.getIndustries().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntentionIndustry industry = (JobTalentIntentionIndustry)iterator.next();
				getDatabaseService().deleteRecord(industry);
				iterator.remove();
			}
			if(intention.getIndustries()==null) {
				intention.setIndustries(new LinkedHashSet());
			}
			String[] ids = intention.getIndustryIds().split(",");
			String[] names = intention.getIndustryNames().split(",");
			for(int i=0; i<ids.length; i++) {
				JobTalentIntentionIndustry industry = new JobTalentIntentionIndustry();
				industry.setId(UUIDLongGenerator.generateId()); //ID
				industry.setIntentionId(intention.getId()); //职位ID
				industry.setIndustryId(Long.parseLong(ids[i])); //地区ID
				industry.setIndustry(names[i]); //地区
				getDatabaseService().saveRecord(industry);
				intention.getIndustries().add(industry);
			}
		}
		if(intention.getPostIds()!=null) {
			for(Iterator iterator = intention.getPosts()==null ? null : intention.getPosts().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntentionPost post = (JobTalentIntentionPost)iterator.next();
				getDatabaseService().deleteRecord(post);
				iterator.remove();
			}
			if(intention.getPosts()==null) {
				intention.setPosts(new LinkedHashSet());
			}
			String[] ids = intention.getPostIds().split(",");
			String[] names = intention.getPostNames().split(",");
			for(int i=0; i<ids.length; i++) {
				JobTalentIntentionPost post = new JobTalentIntentionPost();
				post.setId(UUIDLongGenerator.generateId()); //ID
				post.setIntentionId(intention.getId()); //职位ID
				post.setPostId(Long.parseLong(ids[i])); //地区ID
				post.setPost(names[i]); //地区
				getDatabaseService().saveRecord(post);
				intention.getPosts().add(post);
			}
		}
		if(intention.getCompanyTypeArray()!=null) {
			for(Iterator iterator = intention.getCompanyTypes()==null ? null : intention.getCompanyTypes().iterator(); iterator!=null && iterator.hasNext();) {
				JobTalentIntentionCompanyType type = (JobTalentIntentionCompanyType)iterator.next();
				getDatabaseService().deleteRecord(type);
				iterator.remove();
			}
			if(intention.getCompanyTypes()==null) {
				intention.setCompanyTypes(new LinkedHashSet());
			}
			for(int i=0; i<intention.getCompanyTypeArray().length; i++) {
				JobTalentIntentionCompanyType intentionCompanyType = new JobTalentIntentionCompanyType();
				intentionCompanyType.setId(UUIDLongGenerator.generateId());
				intentionCompanyType.setIntentionId(intention.getId());
				intentionCompanyType.setCompanyType(intention.getCompanyTypeArray()[i]);
				save(intentionCompanyType);
				intention.getCompanyTypes().add(intentionCompanyType);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.talent.service.JobTalentService#listRecommendJobs(long, int)
	 */
	public List listRecommendJobs(long talentId, int limit) throws ServiceException {
		//获取性别、出生日期、工作年限
		String hql = "select JobTalent.sex, JobTalent.birthday, JobTalent.workYear" +
					 " from JobTalent JobTalent" +
					 " where JobTalent.id=" + talentId;
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		int age = values[1]==null ? -1 : DateTimeUtils.getYear(DateTimeUtils.date()) - DateTimeUtils.getYear((Date)values[1]);
		
		//获取学历
		hql = "select max(JobTalentSchooling.qualification)" +
			  " from JobTalentSchooling JobTalentSchooling" +
			  " where JobTalentSchooling.talentId=" + talentId;
		Number qualification = (Number)getDatabaseService().findRecordByHql(hql);
		
		//获取求职意向
		hql = "from JobTalentIntention JobTalentIntention" +
			  " where JobTalentIntention.talentId=" + talentId +
			  " order by JobTalentIntention.id";
		List intentions = getDatabaseService().findRecordsByHql(hql);
		int left = limit;
		List recommendJobs = new ArrayList();
		for(Iterator iterator = intentions==null ? null : intentions.iterator(); left>0 && iterator!=null && iterator.hasNext();) {
			JobTalentIntention intention = (JobTalentIntention)iterator.next();
			String companyTypes = ListUtils.join(intention.getCompanyTypes(), "companyType", "','", false); //求职意向公司性质
			String postIds = ListUtils.join(intention.getPosts(), "postId", ",", false); //求职意向职能类别
			String types = ListUtils.join(intention.getTypes(), "type", ",", false); //求职意向工作性质
			String areaIds = ListUtils.join(intention.getAreas(), "areaId", ",", false); //求职意向地点
			hql = "select distinct Job.id, Job.refreshTime" +
				  " from Job Job" +
				  " left join Job.areas JobArea" +  //工作地点
				  " left join Job.types JobType" +  //工作性质
				  " left join Job.company JobCompany" + //企业
				  " where Job.isPublic=1" +
				  " and (Job.sex='A' or Job.sex='" + values[0] + "')" + //性别
				  (age<=0 ? "" : " and Job.minAge<=" + age + " and (Job.maxAge=0 or Job.maxAge>=" + age + ")") + //年龄
				  " and (Job.workYear=-2 or Job.workYear<=" + values[2] + ")" +  //工作年限
				  (qualification==null ? "" : " and (Job.qualification=-1 or Job.qualification<=" + qualification + ")") + //学历
				  (intention.getCompanyScale()<=0 ? "" : " and JobCompany.scale>=" + intention.getCompanyScale()) + //公司规模
				  (companyTypes==null ? "" : " and JobCompany.type in ('" +  companyTypes + "')") + //公司性质
				  (areaIds==null ? "" : " and JobArea.areaId in (select OrgSubjection.directoryId from OrgSubjection OrgSubjection where OrgSubjection.parentDirectoryId in (" + areaIds + "))") + //工作地点
				  (postIds==null ? "" : " and Job.postId in (" + postIds + ")") + //职能类别
				  (intention.getMinMonthlyPay()<=0 ? "" : " and ((Job.minMonthlyPay=0 and Job.maxMonthlyPay=0) or Job.minMonthlyPay>=" + intention.getMinMonthlyPay() + " or Job.maxMonthlyPay>=" + intention.getMinMonthlyPay() + ")") + //月薪要求
				  (types==null ? "" : " and JobType.type in (" + types + ")") + //工作性质
				  " order by Job.refreshTime DESC";
			List jobIds = getDatabaseService().findRecordsByHql(hql, 0, left);
			if(jobIds==null || jobIds.isEmpty()) {
				continue;
			}
			left -= jobIds.size();
			String ids = null;
			for(Iterator iteratorId = jobIds.iterator(); iteratorId.hasNext();) {
				Object[] jobValues = (Object[])iteratorId.next();
				ids = (ids==null ? "" : ids + ",") + jobValues[0];
			}
			List jobs = getDatabaseService().findRecordsByHql("from Job Job where Job.id in (" + ids + ")");
			recommendJobs.addAll(ListUtils.sortByProperty(jobs, "id", ids));
		}
		return recommendJobs.isEmpty() ? null : recommendJobs;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.talent.service.JobTalentService#cancelPush(java.lang.String)
	 */
	public void cancelPush(String encodedTalentId) throws ServiceException {
		long talentId;
		try {
			talentId = Long.parseLong(Encoder.getInstance().desDecode(encodedTalentId, "20050718", "utf-8", null));
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		JobTalent talent = (JobTalent)load(JobTalent.class, talentId);
		talent.setReceivePushMail(0);
		update(talent);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.talent.service.JobTalentService#listTalentRecports(long, java.lang.String, java.sql.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Date, java.sql.Date, long, long)
	 */
	public List listTalentReports(long schoolClassId, String schoolClass, Date graduateDate, String schoolingLength, String qualification, String specialty, String trainingMode, Date reportBegin, Date reportEnd, long noticeNumber, long reportNumber) throws ServiceException {
		//获取人才列表
		String hql = "select JobTalent" +
					 " from JobTalent JobTalent left join JobTalent.schoolings JobTalentSchooling" +
					 " where JobTalentSchooling.schoolClassId=" + schoolClassId +
					 " order by JobTalent.name";
		List talents = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("schoolings"));
		if(talents==null || talents.isEmpty()) {
			return null;
		}
		//获取就业报到记录列表
		hql = "from JobTalentReport JobTalentReport" +
			  " where JobTalentReport.schoolClassId=" + schoolClassId;
		List reports = getDatabaseService().findRecordsByHql(hql);
		for(int i = 0; i < talents.size(); i++) {
			JobTalent talent = (JobTalent)talents.get(i);
			JobTalentSchooling schooling = (JobTalentSchooling)ListUtils.findObjectByProperty(talent.getSchoolings(), "schoolClassId", new Long(schoolClassId));
			JobTalentReport report = (JobTalentReport)ListUtils.findObjectByProperty(reports, "talentId", new Long(talent.getId()));
			if(report!=null) {
				talents.set(i, report);
				continue;
			}
			report = new JobTalentReport();
			report.setSchoolClassId(schoolClassId); //班级ID
			report.setSchoolClass(schoolClass); //班级名称
			report.setGraduateDate(graduateDate); //毕业时间
			report.setSchoolingLength(schoolingLength); //学制
			report.setQualification(qualification); //学历层次
			report.setSpecialty(specialty); //专业
			report.setTrainingMode(trainingMode); //培养方式
			report.setReportBegin(reportBegin); //报到开始时间
			report.setReportEnd(reportEnd); //报到截止时间
			report.setTalentId(talent.getId()); //人才ID
			report.setName(talent.getName()); //姓名
			report.setSex(talent.getSex()); //性别
			report.setBirthday(talent.getBirthday()); //出生年月
			report.setResidence(talent.getResidence()); //入学前户口所在地
			report.setNation(talent.getNation()); //民族
			report.setPoliticalStatus(talent.getPoliticalStatus()); //政治面貌
			report.setStudentNumber(schooling==null ? null : schooling.getStudentNumber()); //学号
			report.setTel(talent.getCell()); //联系电话
			//report.setAddress(address); //家庭地址
			report.setEmail(talent.getEmail()); //电子邮箱
			talents.set(i, report);
		}
		//排序
		Collections.sort(talents, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				JobTalentReport report0 = (JobTalentReport)arg0;
				JobTalentReport report1 = (JobTalentReport)arg1;
				return StringUtils.parseInt(report0.getStudentNumber(), Integer.MAX_VALUE) - StringUtils.parseInt(report1.getStudentNumber(), Integer.MAX_VALUE);
			}
		});
		//自动编号
		for(int i = 0; i < talents.size(); i++) {
			JobTalentReport report = (JobTalentReport)talents.get(i);
			if(noticeNumber>0 && (report.getNoticeNumber()==null || report.getNoticeNumber().isEmpty())) {
				report.setNoticeNumber("" + (noticeNumber++)); //就业通知书起始编号
			}
			if(reportNumber>0 && (report.getReportNumber()==null || report.getReportNumber().isEmpty())) {
				report.setReportNumber("" + (reportNumber++)); //就业报到证起始编号
			}
		}
		return talents;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.talent.service.JobTalentService#saveTalentRecports(javax.servlet.http.HttpServletRequest)
	 */
	public void saveTalentReports(long schoolClassId, String schoolClass, HttpServletRequest request) throws ServiceException {
		String[] talentIds = request.getParameterValues("talentId");
		String hql = "from JobTalentReport JobTalentReport" +
			  		 " where JobTalentReport.talentId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(talentIds, ",", false)) + ")";
		List reports = getDatabaseService().findRecordsByHql(hql);
		for(int i = 0; i < talentIds.length; i++) {
			JobTalentReport report = (JobTalentReport)ListUtils.findObjectByProperty(reports, "talentId", new Long(talentIds[i]));
			boolean isNew = report==null;
			if(isNew) {
				report = new JobTalentReport();
				report.setId(UUIDLongGenerator.generateId());
				report.setTalentId(Long.parseLong(talentIds[i])); //人才ID
			}
			report.setSchoolClassId(schoolClassId); //班级ID
			report.setSchoolClass(schoolClass); //班级名称
			report.setCompany(request.getParameterValues("company")[i]); //单位名称
			report.setCompanyCode(request.getParameterValues("companyCode")[i]); //单位组织机构代码
			report.setCompanyType(request.getParameterValues("companyType")[i]); //单位性质
			report.setCompanySector(request.getParameterValues("companySector")[i]); //单位产业
			report.setCompanyIndustry(request.getParameterValues("companyIndustry")[i]); //单位行业
			report.setPersonnelFileCompany(request.getParameterValues("personnelFileCompany")[i]); //档案接收单位
			report.setPersonnelFileAddress(request.getParameterValues("personnelFileAddress")[i]); //档案接收地址
			report.setName(request.getParameterValues("name")[i]); //姓名
			String sex = request.getParameterValues("sex")[i];
			report.setSex(sex.isEmpty() ? 'M' : sex.charAt(0)); //性别
			try {
				report.setBirthday(DateTimeUtils.parseDate(request.getParameterValues("birthday")[i], null)); //出生年月
			}
			catch (ParseException e) {
				
			}
			try {
				report.setGraduateDate(DateTimeUtils.parseDate(request.getParameterValues("graduateDate")[i], null)); //毕业时间
			}
			catch (ParseException e) {
				
			}
			report.setResidence(request.getParameterValues("residence")[i]); //入学前户口所在地
			report.setNation(request.getParameterValues("nation")[i]); //民族
			report.setPoliticalStatus(request.getParameterValues("politicalStatus")[i]); //政治面貌
			report.setStudentNumber(request.getParameterValues("studentNumber")[i]); //学号
			report.setSchoolingLength(request.getParameterValues("schoolingLength")[i]); //学制
			report.setQualification(request.getParameterValues("qualification")[i]); //学历层次
			report.setSpecialty(request.getParameterValues("specialty")[i]); //专业
			report.setTrainingMode(request.getParameterValues("trainingMode")[i]); //培养方式
			report.setTel(request.getParameterValues("tel")[i]); //联系电话
			report.setAddress(request.getParameterValues("address")[i]); //家庭地址
			report.setEmail(request.getParameterValues("email")[i]); //电子邮箱
			report.setJobType(request.getParameterValues("jobType")[i]); //工作职位类别
			try {
				report.setReportBegin(DateTimeUtils.parseDate(request.getParameterValues("reportBegin")[i], null)); //报到开始时间
			}
			catch (ParseException e) {
				
			}
			try {
				report.setReportEnd(DateTimeUtils.parseDate(request.getParameterValues("reportEnd")[i], null)); //报到截止时间
			}
			catch (ParseException e) {
				
			}
			report.setNoticeNumber(request.getParameterValues("noticeNumber")[i]); //就业通知书编号
			report.setReportNumber(request.getParameterValues("reportNumber")[i]); //就业报到证编号
			report.setRemark(request.getParameterValues("remark")[i]); //备注
			if(isNew) {
				save(report);
			}
			else {
				update(report);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		JobTalent talent = getTalentByLoginName(loginName);
		if(talent==null) {
			return false;
		}
		if(validateOldPassword && !cryptService.encrypt(oldPassword, "" + talent.getId(), true).equals(talent.getPassword())) {
			throw(new WrongPasswordException()); //密码错误
		}
		talent.setPassword(newPassword); //加密口令
		update(talent);
		return true;
	}
	
	/**
	 * 按登录用户名获取企业
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	private JobTalent getTalentByLoginName(String loginName) throws ServiceException {
		String hql = "from JobTalent JobTalent" +
					 " where JobTalent.email='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'";
		return (JobTalent)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("intentions,schoolings", ","));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			JobTalent talent = getTalentByLoginName(loginName);
			if(talent==null) {
				return null;
			}
			//设置用户信息
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setLoginName(talent.getEmail());
			sessionInfo.setUserType(PERSON_TYPE_JOB_TALENT);
			sessionInfo.setInternalUser(false); //不是内部用户
			sessionInfo.setPassword(cryptService.decrypt(talent.getPassword(), "" + talent.getId(), true));
			sessionInfo.setUserName(talent.getName()==null ? talent.getEmail() : talent.getName());
			sessionInfo.setUserId(talent.getId());
			//设置部门信息
			sessionInfo.setDepartmentIds("0");
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
		JobTalent talent = (JobTalent)load(JobTalent.class, memberId);
		if(talent==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, talent);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PERSON_TYPE_JOB_TALENT);
		member.setLoginName(talent.getEmail());
		member.setMailAddress(talent.getEmail());
		member.setOriginalRecord(talent);
		return member;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		String hql = "select JobTalent.id" +
					 " from JobTalent JobTalent" +
					 " where JobTalent.email='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'";
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
		JobTalent talent = getTalentByLoginName(loginName);
		if(talent==null) {
			return null;
		}
		//statuc: 0/注册,1/审核通过,2/审核未通过,3/停用
		if(talent.getStatus()==3) { // && talent.getIntentions()!=null && !talent.getIntentions().isEmpty() && talent.getSchoolings()!=null && !talent.getSchoolings().isEmpty()) {
			throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT);
		}
		String correctPassword = cryptService.decrypt(talent.getPassword(), "" + talent.getId(), true); //正确的密码
		return new MemberLogin(talent.getEmail(), talent.getId(), PERSON_TYPE_JOB_TALENT, correctPassword, !passwordMatcher.matching(correctPassword, password));
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
}