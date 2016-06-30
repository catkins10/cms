package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.RegistPerson;
import com.yuanluesoft.jeaf.usermanage.pojo.RegistStudent;
import com.yuanluesoft.jeaf.usermanage.pojo.RegistTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode;
import com.yuanluesoft.jeaf.usermanage.pojo.Student;
import com.yuanluesoft.jeaf.usermanage.pojo.StudentGenearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Teacher;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class RegistPersonServiceImpl extends BusinessServiceImpl implements RegistPersonService {
	private PersonService personService;
	private MemberServiceList memberServiceList; //成员服务列表
	private OrgService orgService;
	private MessageService messageService; //消息服务
	private String message = null; //短信内容,{ACCOUNT}表示账户名,{USERNAME}表示用户名,{CHILDNAME}表示孩子姓名
	
	private MailService mailService; //邮件服务
	private String schoolRegistMailSubject; //学校注册邮件标题
	private String schoolRegistMailBody; //学校注册邮件内容
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		RegistPerson registPerson = (RegistPerson)record;
		int type = 0;
		if(registPerson instanceof RegistStudent) { //学生
			type = PersonService.PERSON_TYPE_STUDENT;
		}
		else if(registPerson instanceof RegistTeacher) { //教师
			type = PersonService.PERSON_TYPE_TEACHER;
		}
		//TODO 用户名不区分大小写 registPerson.setLoginName(registPerson.getLoginName().toLowerCase()); //转换为小写
		//获取教师所在单位或学校的ID
		Org unit = orgService.getParentUnitOrSchool(registPerson.getOrgId());
		registPerson.setUnitId(unit.getId());
		registPerson.setUnitName(unit.getDirectoryName());
		registPerson.setType(type);
		if(type==PersonService.PERSON_TYPE_TEACHER) { //教师
			RegistTeacher registTeacher = (RegistTeacher)registPerson;
			//教师,直接注册到用户表中
			Teacher teacher = personService.addTeacher(registTeacher.getName(), registTeacher.getLoginName(), registTeacher.getPassword(), registTeacher.getSex(), registTeacher.getTel(), registTeacher.getTelFamily(), registTeacher.getMobile(), registTeacher.getFamilyAddress(), registTeacher.getMailAddress(), "" + registTeacher.getOrgId(), 0, null);
			//是否班主任
			if(registTeacher.getChargeClassId()>0) {
				orgService.addClassTeacher(registTeacher.getChargeClassId(), teacher.getId(), "班主任");
			}
		}
		registPerson.setCreated(DateTimeUtils.now()); //设置注册时间
		super.save(record);
		return registPerson;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		RegistPerson registPerson = (RegistPerson)record;
		List errors = new ArrayList();
		//检查用户名是否重复
		if(memberServiceList.isLoginNameInUse(registPerson.getLoginName(), 0)) {
			errors.add("用户名已经被占用");
		}
		if(registPerson instanceof RegistTeacher) {
			RegistTeacher registTeacher = (RegistTeacher)registPerson;
			if(registTeacher.getChargeClassId()>0) { //是班主任
				//如果是教师,检查任班主任的班级是否和注册的部门是同一所学校
				Org school = orgService.getParentUnitOrSchool(registTeacher.getOrgId());
				Org schoolCharge = orgService.getParentUnitOrSchool(registTeacher.getChargeClassId());
				if(school.getId()!=schoolCharge.getId()) {
					errors.add("您任班主任的班级不属于您所注册的学校");
				}
			}
		}
		return errors.isEmpty() ? null : errors;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#approvalRegistTeacher(long, long, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalRegistTeacher(long registTeacherId, long schoolId, boolean pass, SessionInfo sessionInfo) throws ServiceException {
		//获得注册记录
		RegistTeacher registTeacher = (RegistTeacher)getDatabaseService().findRecordById(RegistTeacher.class.getName(), registTeacherId);
		if(registTeacher.getApproverId()>0) { //已经审批过
			return;
		}
		//判断用户是否是该校的管理员
		if(registTeacher.getUnitId()!=schoolId) {
			throw new ServiceException("school is not matching");
		}
		if(pass) { //添加教师
			Teacher teacher = personService.addTeacher(registTeacher.getName(), registTeacher.getLoginName(), registTeacher.getPassword(), registTeacher.getSex(), registTeacher.getTel(), registTeacher.getTelFamily(), registTeacher.getMobile(), registTeacher.getFamilyAddress(), registTeacher.getMailAddress(), "" + registTeacher.getOrgId(), sessionInfo.getUserId(), sessionInfo.getUserName());
			//是否班主任
			if(registTeacher.getChargeClassId()>0) {
				orgService.addClassTeacher(registTeacher.getChargeClassId(), teacher.getId(), "班主任");
			}
		}
		//设置审批信息
		registTeacher.setApprovalTime(DateTimeUtils.now()); //审批时间
		registTeacher.setApprover(sessionInfo.getUserName()); //审批人
		registTeacher.setApproverId(sessionInfo.getUserId()); //审批人ID
		registTeacher.setIsPass(pass ? '1' : '0'); //是否审批通过
		getDatabaseService().saveRecord(registTeacher);
		getDatabaseService().flush();
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#approvalRegistStudent(long, long, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalRegistStudent(long registStudentId, long classId, boolean pass, SessionInfo sessionInfo) throws ServiceException {
		//获得注册记录
		RegistStudent registStudent = (RegistStudent)getDatabaseService().findRecordById(RegistStudent.class.getName(), registStudentId);
		if(registStudent.getApproverId()>0) { //已经审批过
			return;
		}
		//判断班级是否匹配
		if(registStudent.getOrgId()!=classId) {
			throw new ServiceException("class is not matching");
		}
		Student student = null;
		Genearch genearch = null;
		if(pass) {
			//添加学生
			student = personService.addStudent(registStudent.getName(), registStudent.getLoginName(), registStudent.getSeatNumber(), registStudent.getPassword(), registStudent.getSex(), registStudent.getTel(), registStudent.getTelFamily(), registStudent.getMobile(), registStudent.getFamilyAddress(), registStudent.getMailAddress(), null, null, null, registStudent.getOrgId(), sessionInfo.getUserId(), sessionInfo.getUserName());
			//添加家长
			genearch = personService.addGenearch(registStudent.getGenearchName(), null, null, null, registStudent.getGenearchTitle(), '0', registStudent.getGenearchMobile(), registStudent.getGenearchMail(), student.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
			//生成4位的数字验证码
			String validateCode = UUIDStringGenerator.generateId().replaceAll("0", "").substring(0, 4).toLowerCase(); //String.format("%6d", new Object[]{new Integer(new Random().nextInt(1000000))}); 
			registStudent.setGenearchValidateCode(validateCode);
		}
		//设置审批信息
		registStudent.setApprovalTime(DateTimeUtils.now()); //审批时间
		registStudent.setApprover(sessionInfo.getUserName()); //审批人
		registStudent.setApproverId(sessionInfo.getUserId()); //审批人ID
		registStudent.setIsPass(pass ? '1' : '0'); //是否审批通过
		getDatabaseService().updateRecord(registStudent);
		getDatabaseService().flush();
		
		//发送短信给家长
		if(message!=null && genearch!=null) {
			sendSmsToGenearch(genearch.getId(), genearch.getLoginName(), genearch.getName(), student.getName(), registStudent.getGenearchValidateCode());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#resendSmsToGenearch(long)
	 */
	public void resendSmsToGenearch(long schoolClassId) throws ServiceException {
		if(message==null) {
			return;
		}
		//根据班级ID获得该班级的学生及家长
		List studentList = orgService.listStudentsWithGenearchs(schoolClassId);
		//如果该班没有学生就返回
		if(studentList==null || studentList.isEmpty()) {
			return;
		}
		for(Iterator iterator=studentList.iterator(); iterator.hasNext();) {
			Student student = (Student)iterator.next();
			//根据学生的登录名获得该学生的注册信息
			RegistStudent registStudent=(RegistStudent)getDatabaseService().findRecordByHql("from RegistStudent RegistStudent where RegistStudent.loginName='" + JdbcUtils.resetQuot(student.getLoginName()) + "'");
			if(registStudent==null || 
			   registStudent.getGenearchValidateCode()==null || //家长的验证码为空
			   registStudent.getGenearchRegisted()=='1' ||
			   "".equals(registStudent.getGenearchValidateCode()) ||  
			   student.getGenearches().isEmpty()) { //没有添加家长 
				continue;
			}
			for(Iterator iteratorStudentGenearch = student.getGenearches().iterator(); iteratorStudentGenearch.hasNext();) {
				Person genearch = ((StudentGenearch)iteratorStudentGenearch.next()).getGenearch(); 
				if(genearch==null || !genearch.getName().equals(registStudent.getGenearchName())) {
					continue;
				}
				String mobile = genearch.getMobile();
				if(mobile==null || "".equals(mobile)) { //手机号码为空,不发送
					continue;
				}
				sendSmsToGenearch(genearch.getId(), genearch.getLoginName(), genearch.getName(), student.getName(), registStudent.getGenearchValidateCode());
			}
		}
	}

	/**
	 * 发送短信给家长
	 * @param genearch
	 * @param student
	 * @param validateCode
	 * @return
	 */
	private void sendSmsToGenearch(long genearchId, String genearchLoginName, String genearchName, String studentName, String validateCode) {
		//生成短信内容
		String message = this.message.replaceAll("\\x7bUSERNAME\\x7d", genearchName).replaceAll("\\x7bACCOUNT\\x7d", genearchLoginName).replaceAll("\\x7bCHILDNAME\\x7d", studentName).replaceAll("\\x7bVALIDATECODE\\x7d", validateCode);
		try {
			messageService.sendMessageToPerson(genearchId, message, 0, "www.9191edu.com", MessageService.MESSAGE_PRIORITY_NORMAL, 0, null, null, "sms", null, 0, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#retrieveGenearchAccount(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Person retrieveGenearchAccount(long classId, String classFullName, String childName, String name, String mobile, String validateCode) throws ServiceException {
		String hql = "select RegistStudent from RegistStudent RegistStudent" +
					 "  where RegistStudent.orgId=" + classId +
					 "  and RegistStudent.name='" + JdbcUtils.resetQuot(childName) + "'" +
					 "  and RegistStudent.genearchName='" + JdbcUtils.resetQuot(name).replaceAll("\\x20", "").replaceAll("　", "") + "'" +
					 "  and RegistStudent.genearchMobile='" + JdbcUtils.resetQuot(mobile) + "'" +
					 "  and RegistStudent.genearchRegisted!='1'" +
					 "  and RegistStudent.genearchValidateCode='" + JdbcUtils.resetQuot(validateCode) + "'";
		RegistStudent registStudent = (RegistStudent)getDatabaseService().findRecordByHql(hql);
		if(registStudent==null) {
			return null;
		}
		//查找学生信息
		Person student = (Person)personService.listPersonsByLoginNames(registStudent.getLoginName()).get(0);
		//获取家长
		List studentGenearchs = personService.listStudentGenearchs(student.getId());
		if(studentGenearchs==null || studentGenearchs.isEmpty()) {
			return null;
		}
		//设置为已注册
		registStudent.setGenearchRegisted('1');
		getDatabaseService().updateRecord(registStudent);
		return ((StudentGenearch)studentGenearchs.get(0)).getGenearch();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#modifyGenearchAccount(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void modifyGenearchAccount(String loginName, String password, String newLoginName, String newPassword) throws ServiceException {
		Genearch genearch = (Genearch)getDatabaseService().findRecordByHql("from Genearch Genearch where Genearch.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		//检查账户是否已经被占用
		if(!newLoginName.equals(loginName)) {
			if(memberServiceList.isLoginNameInUse(newLoginName, 0)) {
				throw new ServiceException(ERROR_LOGINNAME_IN_USE);
			}
			genearch.setLoginName(newLoginName);
		}
		//校验密码是否正确
		if(!personService.decryptPassword(genearch.getId(), genearch.getPassword()).equals(password)) {
			throw new ServiceException(ERROR_PASSWORD_INVALID);
		}
		genearch.setPassword(newPassword);
		personService.update(genearch);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#createSchoolRegistCode(long, String)
	 */
	public SchoolRegistCode createSchoolRegistCode(long schoolId, String code) throws ServiceException {
		SchoolRegistCode registCode = retireveSchoolRegistCode(schoolId);
		if(registCode==null) {
			registCode = new SchoolRegistCode();
			registCode.setId(UUIDLongGenerator.generateId());
			registCode.setAuthorId(0);
			registCode.setSchoolId(schoolId);
			registCode.setCode(code);
			getDatabaseService().saveRecord(registCode);
		}
		else {
			registCode.setCode(code);
			getDatabaseService().updateRecord(registCode);
		}
		return registCode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#retireveSchoolRegistCode(long)
	 */
	public SchoolRegistCode retireveSchoolRegistCode(long schoolId) throws ServiceException {
		return (SchoolRegistCode)getDatabaseService().findRecordByHql("from SchoolRegistCode SchoolRegistCode where SchoolRegistCode.schoolId=" + schoolId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#sendSchoolRegistMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendSchoolRegistMail(String mailAddress, String schoolName, String managerName, String managerPassword, String managerLoginName, String schoolRegistCode) throws ServiceException {
		String body = schoolRegistMailBody.replaceAll("\\x7bMANAGERNAME\\x7d", managerName).replaceAll("\\x7bSCHOOLNAME\\x7d", schoolName).replaceAll("\\x7bMANAGERLOGINNAME\\x7d", managerLoginName).replaceAll("\\x7bMANAGERPASSWORD\\x7d", managerPassword).replaceAll("\\x7bSCHOOLREGISTCODE\\x7d", schoolRegistCode).replaceAll("\\x7bDATE\\x7d", new SimpleDateFormat("yyyy年M月d日").format(DateTimeUtils.date()));
		mailService.sendMail(0, null, 0, mailAddress, schoolRegistMailSubject, body, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#resendSchoolRegistMail(long)
	 */
	public void resendSchoolRegistMail(long schoolId) throws ServiceException {
		//获取学校信息
		Org school = orgService.getOrg(schoolId);
		List managers = listManagers(school);
		if(managers==null || managers.isEmpty()) {
			return;
		}
		//获取学校注册码
		SchoolRegistCode registCode = retireveSchoolRegistCode(schoolId);
		//给每个管理员都发注册邮件
		for(Iterator iterator=managers.iterator(); iterator.hasNext();) {
			Person manager = (Person)iterator.next();
			if(manager==null) {
				continue;
			}
			String password = personService.decryptPassword(manager.getId(), manager.getPassword());
			if(manager.getMailAddress()!=null) { //发送
				sendSchoolRegistMail(manager.getMailAddress(), school.getDirectoryName(), manager.getName(), password, manager.getLoginName(), registCode.getCode());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RegistPersonService#retrieveRegistInfo(long)
	 */
	public String retrieveRegistInfo(long schoolId) throws ServiceException {
		//获取学校信息
		Org school = orgService.getOrg(schoolId);
		//学校必须是一个月内注册的
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		if(school.getCreated().before(calendar.getTime())) {
			return "只能查询近一个月内注册的学校";
		}
		//获取学校注册码
		SchoolRegistCode registCode = retireveSchoolRegistCode(schoolId);
		String registInfo = "教师注册码：" + registCode.getCode();
		//获取学校管理员列表
		List managers = listManagers(school);
		if(managers!=null && !managers.isEmpty()) {
			//获取每个管理员的信息
			for(Iterator iterator=managers.iterator(); iterator.hasNext();) {
				Person manager = (Person)iterator.next();
				String password = personService.decryptPassword(manager.getId(), manager.getPassword());
				registInfo += "，管理员：" + manager.getLoginName();
				registInfo += "，密码：" + password;
			}
		}
		return registInfo;
	}
	
	/**
	 * 获取机构管理员
	 * @param org
	 * @return
	 */
	private List listManagers(Org org) throws ServiceException {
		List popedomUsers = org.getMyVisitors("manager");
		return personService.listPersons(ListUtils.join(popedomUsers, "userId", ",", false));
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the schoolRegistMailBody
	 */
	public String getSchoolRegistMailBody() {
		return schoolRegistMailBody;
	}

	/**
	 * @param schoolRegistMailBody the schoolRegistMailBody to set
	 */
	public void setSchoolRegistMailBody(String schoolRegistMailBody) {
		this.schoolRegistMailBody = schoolRegistMailBody;
	}

	/**
	 * @return the schoolRegistMailSubject
	 */
	public String getSchoolRegistMailSubject() {
		return schoolRegistMailSubject;
	}

	/**
	 * @param schoolRegistMailSubject the schoolRegistMailSubject to set
	 */
	public void setSchoolRegistMailSubject(String schoolRegistMailSubject) {
		this.schoolRegistMailSubject = schoolRegistMailSubject;
	}

	/**
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}

	/**
	 * @return the messageService
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * @param messageService the messageService to set
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
