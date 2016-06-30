/*
 * Created on 2006-3-13
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.io.FileInputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.soap.SoapService;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.pojo.Employee;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgLeader;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgSupervisor;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonLink;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSupervisor;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolClass;
import com.yuanluesoft.jeaf.usermanage.pojo.Student;
import com.yuanluesoft.jeaf.usermanage.pojo.StudentGenearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Teacher;
import com.yuanluesoft.jeaf.usermanage.replicator.spring.UserReplicateServiceList;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.CnToSpell;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 *
 * @author linchuan
 * 
 */
public class PersonServiceImpl extends BusinessServiceImpl implements PersonService, SoapService {
	private final String APPLICATION_NAME = "jeaf/usermanage";
	private final String IMAGE_TYPE_PORTRAIT = "portrait";
	
	private String sidSequence = null;
	private OrgService orgService; //组织机构服务
	private RoleService roleService; //角色管理服务
	private SessionService sessionService;
	private boolean selfRegistEnabled; //是否允许用户自行注册
	private UserSynchClientList userSynchClientList;
	private UserSynchClientList newUserSynchClientList;
	private UserReplicateServiceList userReplicateServiceList; //用户复制服务列表
	private CryptService cryptService; //加/解密服务
	private TemplateService templateService; //模板服务,清除模板缓存用
	private boolean createMailAccount; //是否由系统创建邮件帐号
	
	private String webApplicationUrl;
	private String webApplicationSafeUrl;
	private ImageService imageService; //图像服务
	private FileDownloadService fileDownloadService; //文件传输服务
	private MailService mailService; //邮件服务
	private MemberServiceList memberServiceList; //成员服务列表
	
	private SoapPassport serviceSoapPassport;
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
	    Person person = (Person)super.load(recordClass, id);
        if(person==null) {
        	return null;
        }
    	if(person.getPassword()==null || person.getPassword().equals("")) {
    	    person.setPassword(encryptPassword(person.getId(), person.getLoginName()));
    	}
    	person.setPassword("{" + person.getPassword() + "}");
    	return person;
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		Person person = (Person)record;
		person.setLastModified(DateTimeUtils.now());
		int type = 0;
		if(person instanceof Employee) {
			type = PERSON_TYPE_EMPLOYEE;
		}
		else if(person instanceof Teacher) {
			type = PERSON_TYPE_TEACHER;
		}
		else if(person instanceof Student) {
			type = PERSON_TYPE_STUDENT;
		}
		else if(person instanceof Genearch) {
			type = PERSON_TYPE_GENEARCH;
		}
		else if(person instanceof PersonLink) {
			type = PERSON_TYPE_LINK;
		}
		person.setType(type);
		
		//新用户,允许删除
		person.setDeleteDisable('0');
		//用户名不区分大小写
		person.setLoginName(person.getLoginName().toLowerCase()); //转换为小写
		person.setPreassign('0');
		if(sidSequence!=null) {
			person.setSid((int)getDatabaseService().getSequenceValue(sidSequence));
		}
		person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), person.getPassword())); //加密口令
		super.save(record);
		//更新组成部分
		updatePersonComponents(person);
		userReplicateServiceList.registPerson(person); //注册用户
		//用户同步
		synchPerson(person, null);
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		Person person = (Person)record;
		person.setLastModified(DateTimeUtils.now());
		person.setLoginName(person.getLoginName().toLowerCase()); //转换为小写
		String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
		person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), person.getPassword())); //加密口令
		//更新组成部分
		updatePersonComponents(person);
		super.update(record);
		userReplicateServiceList.modifyPerson(person); //修改用户信息
		//用户同步
		synchPerson(person, oldLoginName);
		try {
			if(!oldLoginName.equals(person.getLoginName())) { //登录用户名已修改
				sessionService.removeSessionInfo(oldLoginName); //删除原用户名的session info
			}
			else {
				sessionService.removeSessionInfo(person.getLoginName()); //删除当前用户的session info
			}
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
		return person;
	}
		
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		Person person = (Person)record;
		if(person.getDeleteDisable()=='1') {
			throw new ServiceException("delete disable");
		}
		if(record instanceof Student) { //学生
			//删除学生家长
			deleteStudentGenearches(((Student)record).getGenearches());
			getDatabaseService().flush();
		}
		//同步删除
		userSynchClientList.deletePerson((Person)record);
		try {
			sessionService.removeSessionInfo(person.getLoginName());
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
		super.delete(record);
		userReplicateServiceList.deletePerson(person.getId()); //删除用户
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#updatePersonSubjection(long, java.lang.String)
	 */
	public void updatePersonSubjection(long personId, String subjectionOrgIds) throws ServiceException {
		Person person = (Person)load(Person.class, personId);
		updatePersonSubjection(person, subjectionOrgIds);
		person.setOrgIds(subjectionOrgIds);
		synchPerson(person, null);
		//清除缓存的模板
		templateService.clearCachedTemplate();
	}
	
	/**
	 * 更新组成部分
	 * @param person
	 * @throws ServiceException
	 */
	private void updatePersonComponents(Person person) throws ServiceException {
		//更新用户隶属关系
		updatePersonSubjection(person, person.getOrgIds());
		//保存用户的分管领导
		updatePersonSupervisors(person.getId(), person.getName(), person.getSupervisorIds(), person.getSupervisorNames());
		//保存被分管的用户列表
		updateSupervisePersons(person.getId(), person.getName(), person.getSupervisePersonIds(), person.getSupervisePersonNames());
		//保存用户分管的部门
		updateSuperviseOrgs(person.getId(), person.getName(), person.getSuperviseOrgIds(), person.getSuperviseOrgNames());
		//保存用户任领导的部门
		updateLeadOrgs(person.getId(), person.getName(), person.getLeadOrgIds(), person.getLeadOrgNames());
	}
	
	/**
	 * 更新用户隶属关系
	 * @param person
	 * @param subjectionOrgIds
	 * @throws ServiceException
	 */
	private void updatePersonSubjection(Person person, String subjectionOrgIds) throws ServiceException {
		if(subjectionOrgIds==null || subjectionOrgIds.equals("")) {
			return;
		}
		//检查隶属的组织结构是否有变化
		try {
			if(subjectionOrgIds.equals(ListUtils.join(person.getSubjections(), "orgId", ",", false))) {
				return;
			}
		}
		catch(Exception e) {
			
		}
		String[] orgIds = subjectionOrgIds.split(","); 
		if(!(person instanceof Student)) { //不是学生
			//删除原来的隶属关系
			getDatabaseService().deleteRecordsByHql("from PersonSubjection PersonSubjection where PersonSubjection.personId=" + person.getId());
			//创建新的隶属关系
			for(int i=0; i<orgIds.length; i++) {
				PersonSubjection subjection = new PersonSubjection();
				subjection.setId(UUIDLongGenerator.generateId());
				subjection.setPersonId(person.getId());
				subjection.setOrgId(Long.parseLong(orgIds[i]));
				getDatabaseService().saveRecord(subjection);
			}
			return;
		}
		//获取学生原来所在班级
		PersonSubjection oldSubjection = (PersonSubjection)getDatabaseService().findRecordByHql("from PersonSubjection PersonSubjection where PersonSubjection.personId=" + person.getId() + " order by PersonSubjection.id DESC");
		//学生新的班级ID
		long newClassId = Long.parseLong(orgIds[0]);
		//判断原来的班级是否是已经毕业了的班级
		Org oldClass = (Org)orgService.getOrg(oldSubjection.getOrgId());
		if((oldClass instanceof SchoolClass) && ((SchoolClass)oldClass).getIsGraduated()=='1') { //已经毕业
			//添加为学生新的班级
			PersonSubjection subjection = new PersonSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setPersonId(person.getId());
			subjection.setOrgId(newClassId);
			getDatabaseService().saveRecord(subjection);
			//为学生家长添加新的班级
			String hql = "select StudentGenearch.genearchId" + 
						 " from StudentGenearch StudentGenearch" +
						 " where StudentGenearch.studentId=" + person.getId();
			List genearchIds = getDatabaseService().findRecordsByHql(hql);
			if(genearchIds!=null && !genearchIds.isEmpty()) {
				for(Iterator iterator = genearchIds.iterator(); iterator.hasNext();) {
					long genearchId = ((Long)iterator.next()).longValue();
					PersonSubjection genearchSubjection = new PersonSubjection();
					genearchSubjection.setId(UUIDLongGenerator.generateId());
					genearchSubjection.setOrgId(newClassId);
					genearchSubjection.setPersonId(genearchId);
					getDatabaseService().saveRecord(genearchSubjection);
				}
			}
		}
		else { //未毕业
			//更加家长所在机构
			synchGenearchSubjection(person.getId(), oldSubjection.getOrgId(), newClassId);
			//更新学生所在班级
			oldSubjection.setOrgId(newClassId);
			getDatabaseService().updateRecord(oldSubjection);
		}
	}

	/**
	 * 更新用户的分管领导
	 * @param personId
	 * @param personName
	 * @param supervisorIds
	 * @param supervisorNames
	 * @throws ServiceException
	 */
	private void updatePersonSupervisors(long personId, String personName, String supervisorIds, String supervisorNames) throws ServiceException {
		if(supervisorIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from PersonSupervisor PersonSupervisor where PersonSupervisor.personId=" + personId);
		if(supervisorIds.equals("")) {
			return;
		}
		String[] ids = supervisorIds.split(",");
		String[] names = supervisorNames.split(",");
		for(int i=0; i<ids.length; i++) {
			if(Long.parseLong(ids[i])==personId) {
				continue;
			}
			PersonSupervisor personSupervisor = new PersonSupervisor();
			personSupervisor.setId(UUIDLongGenerator.generateId()); //ID
			personSupervisor.setPersonId(personId); //用户ID
			personSupervisor.setPersonName(personName); //用户名
			personSupervisor.setSupervisorId(Long.parseLong(ids[i])); //分管领导ID
			personSupervisor.setSupervisor(names[i]); //分管领导姓名
			getDatabaseService().saveRecord(personSupervisor);
		}
	}
	
	/**
	 * 更新分管的用户
	 * @param personId
	 * @param personName
	 * @param supervisePersonIds
	 * @param supervisePersonNames
	 * @throws ServiceException
	 */
	private void updateSupervisePersons(long personId, String personName, String supervisePersonIds, String supervisePersonNames) throws ServiceException {
		if(supervisePersonIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from PersonSupervisor PersonSupervisor where PersonSupervisor.supervisorId=" + personId);
		if(supervisePersonIds.equals("")) {
			return;
		}
		String[] ids = supervisePersonIds.split(",");
		String[] names = supervisePersonNames.split(",");
		for(int i=0; i<ids.length; i++) {
			if(Long.parseLong(ids[i])==personId) {
				continue;
			}
			PersonSupervisor personSupervisor = new PersonSupervisor();
			personSupervisor.setId(UUIDLongGenerator.generateId()); //ID
			personSupervisor.setPersonId(Long.parseLong(ids[i])); //用户ID
			personSupervisor.setPersonName(names[i]); //用户名
			personSupervisor.setSupervisorId(personId); //分管领导ID
			personSupervisor.setSupervisor(personName); //分管领导姓名
			getDatabaseService().saveRecord(personSupervisor);
		}
	}
	
	/**
	 * 更新分管的部门
	 * @param personId
	 * @param personName
	 * @param superviseOrgIds
	 * @param superviseOrgNames
	 * @throws ServiceException
	 */
	private void updateSuperviseOrgs(long personId, String personName, String superviseOrgIds, String superviseOrgNames) throws ServiceException {
		if(superviseOrgIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from OrgSupervisor OrgSupervisor where OrgSupervisor.supervisorId=" + personId);
		if(superviseOrgIds.equals("")) {
			return;
		}
		String[] ids = superviseOrgIds.split(",");
		String[] names = superviseOrgNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OrgSupervisor orgSupervisor = new OrgSupervisor();
			orgSupervisor.setId(UUIDLongGenerator.generateId()); //ID
			orgSupervisor.setOrgId(Long.parseLong(ids[i])); //组织机构ID
			orgSupervisor.setOrgName(names[i]); //组织机构名称
			orgSupervisor.setSupervisorId(personId); //分管领导ID
			orgSupervisor.setSupervisor(personName); //分管领导姓名
			getDatabaseService().saveRecord(orgSupervisor);
		}
	}
	
	/**
	 * 更新用户任领导的部门
	 * @param personId
	 * @param personName
	 * @param leadOrgIds
	 * @param leadOrgNames
	 * @throws ServiceException
	 */
	private void updateLeadOrgs(long personId, String personName, String leadOrgIds, String leadOrgNames) throws ServiceException {
		if(leadOrgIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from OrgLeader OrgLeader where OrgLeader.leaderId=" + personId);
		if(leadOrgIds.equals("")) {
			return;
		}
		String[] ids = leadOrgIds.split(",");
		String[] names = leadOrgNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OrgLeader orgLeader = new OrgLeader();
			orgLeader.setId(UUIDLongGenerator.generateId()); //ID
			orgLeader.setOrgId(Long.parseLong(ids[i])); //组织机构ID
			orgLeader.setOrgName(names[i]); //组织机构名称
			orgLeader.setLeaderId(personId); //领导ID
			orgLeader.setLeader(personName); //领导姓名
			getDatabaseService().saveRecord(orgLeader);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#getPerson(long)
	 */
	public Person getPerson(long personId) throws ServiceException {
		return (Person)load(Person.class, personId);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#getPersonName(long)
	 */
	public String getPersonName(long personId) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select Person.name from Person Person where Person.id=" + personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#synchAllPerson()
	 */
	public void synchAllPerson() throws ServiceException {
		final int BATCH_SIZE = 100; //每次处理100个用户
		for(int i=0; ; i+=BATCH_SIZE) {
			List personList = getDatabaseService().findRecordsByHql("from Person Person order by Person.id", i, BATCH_SIZE);
			if(personList==null || personList.isEmpty()) {
				return;
			}
			for(Iterator iterator = personList.iterator(); iterator.hasNext(); ) {
				Person person = (Person)iterator.next();
				try {
					synchPerson(person, null, newUserSynchClientList);
				}
				catch(Exception e) {
					Logger.error("Person " + person.getLoginName() + " synch failed.");
				}
			}
		}
	}

	/**
	 * 同步更新用户
	 * @param person
	 * @param newPerson
	 * @throws ServiceException
	 */
	private void synchPerson(Person person, String oldLoginName) throws ServiceException {
		synchPerson(person, oldLoginName, userSynchClientList);
	}

	/**
	 * 同步更新用户
	 * @param person
	 * @param newPerson
	 * @throws ServiceException
	 */
	private void synchPerson(Person person, String oldLoginName, UserSynchClientList userSynchClientList) throws ServiceException {
		if(!userSynchClientList.isSynchRequired()) {
			return;
		}
		String password = person.getPassword();
		if(password==null || password.equals("")) { //口令未设置,则以用户登录名为口令
		    password = person.getLoginName();
		}
		else if(password.startsWith("{") && password.endsWith("}")) { //口令解密
		    password = decryptPassword(person.getId(), password.substring(1, password.length() - 1));
		}
		else {
			password = decryptPassword(person.getId(), password);
		}
		try {
			person.setPassword(password);
			userSynchClientList.savePerson(person, oldLoginName);
		}
		finally { //加密口令
			person.setPassword(encryptPassword(person.getId(), password));
		}
		getDatabaseService().updateRecord(person);
	}
	
	/**
	 * 加密用户口令
	 * @param person
	 * @throws ServiceException
	 */
	private String encryptPersonPassword(long personId, String personLoginName, String password) throws ServiceException {
		if(password==null || password.equals("")) { //口令未设置,则以用户登录名为口令
		    password = personLoginName;
		}
		else if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
		//加密口令
		return encryptPassword(personId, password);
	}
	
	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
        return cryptService.encrypt(password, "" + userId, false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#decryptPassword(long, java.lang.String)
	 */
	public String decryptPassword(long userId, String password) throws ServiceException {
		return  cryptService.decrypt(password, "" + userId, false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addGenearch(java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public Genearch addGenearch(String name, String loginName, String password, String passwordEncryptMode, String title, char sex, String mobile, String mailAddress, long studentId, long creatorId, String creatorName) throws ServiceException {
		Genearch genearch = new Genearch();
		//设置基本信息
		genearch.setId(UUIDLongGenerator.generateId()); //ID
		genearch.setName(name);
		//设置登录用户名,为空时,自动创建
		genearch.setLoginName(loginName==null ? generateLoginName(name) : loginName.toLowerCase());
		//设置用户口令,口令为空时自动生成一个
		genearch.setPassword(password==null || password.isEmpty() ? UUIDStringGenerator.generateId().substring(0, 6) : password);
		//设置性别
		if(sex=='0') {
			sex = (title=="母亲" || title=="妈妈" || title=="妈" || title=="祖母" || title=="奶奶" || title=="外祖母" || title=="姥姥" ? 'F' : 'M');  
		}
		genearch.setSex(sex);
		genearch.setMobile(mobile);
		genearch.setMailAddress(mailAddress);
		genearch.setCreatorId(creatorId);
		genearch.setCreator(creatorName);
		genearch.setCreated(DateTimeUtils.now());
		genearch.setType(PERSON_TYPE_GENEARCH);
		
		//新用户,允许删除
		genearch.setDeleteDisable('0');
		genearch.setPreassign('0');
		genearch.setSid((int)getDatabaseService().getSequenceValue("sequence_edu"));
		
		save(genearch); //保存
		
		//把家长和学生关联起来
		addStudentGenearch(studentId, genearch.getId(), title);
		return genearch;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addEmployee(long, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public Employee addEmployee(long id, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException {
		return (Employee)addPerson(id, PERSON_TYPE_NAMES[PERSON_TYPE_EMPLOYEE], name, loginName, password, sex, tel, telFamily, mobile, familyAddress, mailAddress, orgIds, creatorId, creatorName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addPersonLink(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public PersonLink addPersonLink(long id, final String userClassName, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException {
		return (PersonLink)addPerson(id, PERSON_TYPE_NAMES[PERSON_TYPE_LINK], name, loginName, password, sex, tel, telFamily, mobile, familyAddress, mailAddress, orgIds, creatorId, creatorName, new PersonModifyCallback() {
			/*
			 * (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.usermanage.service.spring.PersonServiceImpl.PersonModifyCallback#onBeforeSavePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person, boolean)
			 */
			public void onBeforeSavePerson(Person person, boolean isNew) {
				PersonLink personLink = (PersonLink)person;
				personLink.setUserClassName(userClassName);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addTeacher(java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public Teacher addTeacher(String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException {
		return (Teacher)addPerson(0, PERSON_TYPE_NAMES[PERSON_TYPE_TEACHER], name, loginName, password, sex, tel, telFamily, mobile, familyAddress, mailAddress, orgIds, creatorId, creatorName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addStudent(java.lang.String, java.lang.String, int, java.lang.String, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public Student addStudent(String name, String loginName, final int seatNumber, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, final String identityCard, final String identityCardName, final String remark, long classId, long creatorId, String creatorName) throws ServiceException {
		return (Student)addPerson(0, PERSON_TYPE_NAMES[PERSON_TYPE_STUDENT], name, loginName, password, sex, tel, telFamily, mobile, familyAddress, mailAddress, "" + classId, creatorId, creatorName, new PersonModifyCallback() {
			/*
			 * (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.usermanage.service.spring.PersonServiceImpl.PersonModifyCallback#onBeforeSavePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person, boolean)
			 */
			public void onBeforeSavePerson(Person person, boolean isNew) {
				Student student = (Student)person;
				student.setSeatNumber(seatNumber);
				student.setIdentityCard(identityCard);
				student.setIdentityCardName(identityCardName);
				student.setRemark(remark);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addPerson(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public Person addPerson(long id, String type, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException {
		return addPerson(id, type, name, loginName, password, sex, tel, telFamily, mobile, familyAddress, mailAddress, orgIds, creatorId, creatorName, null);
	}

	/**
	 * 添加用户
	 * @param id
	 * @param type
	 * @param name
	 * @param loginName
	 * @param password
	 * @param sex
	 * @param tel
	 * @param telFamily
	 * @param mobile
	 * @param familyAddress
	 * @param mailAddress
	 * @param orgIds
	 * @param creatorId
	 * @param creatorName
	 * @param callback
	 * @return
	 * @throws ServiceException
	 */
	private Person addPerson(long id, String type, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName, PersonModifyCallback callback) throws ServiceException {
		Person person = null;
		if(id==0) {
			id = UUIDLongGenerator.generateId();
		}
		else {
			person = (Person)load(Person.class, id);
		}
		boolean isNew = person==null;
		if(isNew) {
			if(PERSON_TYPE_NAMES[PERSON_TYPE_TEACHER].equals(type)) {
				person = new Teacher();
			}
			else if(PERSON_TYPE_NAMES[PERSON_TYPE_STUDENT].equals(type)) {
				person = new Student();
			}
			else if(PERSON_TYPE_NAMES[PERSON_TYPE_GENEARCH].equals(type)) {
				person = new Genearch();
			}
			else if(PERSON_TYPE_NAMES[PERSON_TYPE_LINK].equals(type)) {
				person = new PersonLink();
			}
			else {
				person = new Employee();
			}
		}
		name = name.trim();
		//设置基本信息
		person.setId(id); //ID
		person.setName(name);
		person.setLoginName(loginName==null ? generateLoginName(name) : loginName.trim().toLowerCase());
		person.setSex(sex);
		person.setTel(tel);
		person.setTelFamily(telFamily);
		person.setMobile(mobile);
		person.setFamilyAddress(familyAddress);
		person.setMailAddress(mailAddress);
		if(isNew) {
			person.setCreatorId(creatorId);
			person.setCreator(creatorName);
			person.setCreated(DateTimeUtils.now());
			person.setPassword(password==null || password.isEmpty() ? UUIDStringGenerator.generateId().substring(0, 6) : password);
			//新用户,允许删除
			person.setDeleteDisable('0');
			person.setPreassign('0');
			if(person instanceof Teacher) {
				person.setSid((int)getDatabaseService().getSequenceValue("sequence_edu"));
			}
			else if(person instanceof Student) {
				person.setSid((int)getDatabaseService().getSequenceValue("sequence_kd9191edu"));
			}
		}
		if(callback!=null) {
			callback.onBeforeSavePerson(person, isNew);
		}
		//设置用户所属组织机构
		person.setOrgIds(orgIds);
		//保存或者更新
		if(isNew) {
			save(person);
		}
		else {
			update(person);
		}
		return person;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#batchRegistEmployees(java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public void batchRegistEmployees(String dataFilePath, String password, long departmentId, long creatorId, String creatorName) throws ServiceException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(dataFilePath);
			Workbook book = (dataFilePath.toLowerCase().endsWith(".xlsx") ?  (Workbook)new XSSFWorkbook(fileInputStream) :  new HSSFWorkbook(fileInputStream));
			for(int i=0; i<book.getNumberOfSheets(); i++) {
				Sheet sheet = book.getSheetAt(i);
				registSheet(sheet, password, departmentId, creatorId, creatorName); //注册SHEET
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("注册失败");
		}
		finally {
			try {
				fileInputStream.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 注册一个Sheet
	 * @param sheet
	 * @param password
	 * @param departmentId
	 * @param creatorId
	 * @param creatorName
	 * @throws ServiceException
	 */
	private void registSheet(Sheet sheet, String password, long departmentId, long creatorId, String creatorName) throws ServiceException {
		List colNames = new ArrayList(); //表头行
		for(int i=0; i<sheet.getPhysicalNumberOfRows(); i++) {
			Row row = sheet.getRow(i);
			//如果表头为空,检查是否表头,判断依据该行必须要有“姓名”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					String content = ExcelUtils.getCellString(cell);
					if(content==null) {
						content = "";
					}
					else {
						content = content.replaceAll("[\\r\\n \\t]", "");
					}
					colNames.add(content.toLowerCase());
				}
				if(colNames.indexOf("姓名")==-1 && colNames.indexOf("联系人")==-1 && colNames.indexOf("name")==-1) {
					colNames.clear(); //不含“姓名”、"联系人"单元格,不是表头
				}
				continue;
			}
			String loginName=null;//登录的用户名
			String name = null; //姓名
			String sex = null; //性别
			String tel = null; //电话
			String telFamily = null; //家庭电话
			String mobile = null; //手机
			String familyAddress = null; //家庭地址
			String mailAddress = null; //邮箱
			String department = null; //部门
			for(int j=0; j<row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				String content = ExcelUtils.getCellString(cell);
				if(content!=null) {
					content = content.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
				}
				//根据表头设置属性
				String colName = (String)colNames.get(j);
				if("姓名".equals(colName) || "联系人".equals(colName) || "name".equals(colName)) {
					name = content;
				}
				else if("登录名".equals(colName) || "loginName".equals(colName) || "账号".equals(colName)){
					loginName=content;
				}
				else if("地址".equals(colName) || "address".equals(colName)){
					familyAddress=content;
				}
				else if(colName.indexOf("手机")!=-1 || colName.indexOf("mobile")!=-1) {
					mobile = content;
				}
				else if(colName.indexOf("性别")!=-1) {
					sex = content;
				}
				else if(colName.indexOf("家庭电话")!=-1) {
					telFamily = content;
				}
				else if(colName.indexOf("电话")!=-1) {
					tel = content;
				}
				else if(colName.indexOf("邮箱")!=-1 || colName.indexOf("邮件")!=-1 || colName.indexOf("mail")!=-1) {
					mailAddress = content;
				}
				else if(colName.indexOf("部门")!=-1) {
					department = content;
				}
			}
			if(name==null || name.isEmpty()) {
				continue;
			}
			long parentOrgId = departmentId;
			if(department!=null && !department.isEmpty()) { //指定了部门名称
				Org org = (Org)orgService.createDirectory(-1, departmentId, department, "unitDepartment", null, creatorId, creatorName);
				parentOrgId = org.getId();
			}
			//检查部门中是否已经注册过同名用户
			String hql = "select Person.id" +
						 " from Person Person left join Person.subjections PersonSubjection" +
						 " where Person.name='" + JdbcUtils.resetQuot(name) + "'" +
						 " and PersonSubjection.orgId=" + parentOrgId;
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				continue;
			}

			addEmployee(0, name, loginName, password, sex==null || (!"女".equals(sex) && !"F".equalsIgnoreCase(sex)) ? 'M' : 'F', tel, telFamily, mobile, familyAddress, mailAddress, "" + parentOrgId, creatorId, creatorName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#generateLoginName(java.lang.String)
	 */
	public String generateLoginName(String name) throws ServiceException {
		String loginName = CnToSpell.getFullSpell(name, true); //汉字转换成拼音
		if(!memberServiceList.isLoginNameInUse(loginName, 0)) {
			return loginName;
		}
		//用户名已经被占用,在末尾增加数字
		for(int i=1; i<100000; i++) {
			if(!isLoginNameInUse(loginName + i, 0)) {
				return loginName + i;
			}
		}
		throw new ServiceException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#addStudentGenearch(long, long, java.lang.String)
	 */
	public void addStudentGenearch(long studentId, long genearchId, String genearchTitle) throws ServiceException {
		//查找是否添加过
		if(getDatabaseService().findRecordByHql("from StudentGenearch StudentGenearch where StudentGenearch.studentId=" + studentId + " and StudentGenearch.genearchId=" + genearchId)!=null) {
			return;
		}
		StudentGenearch studentGenearch = new StudentGenearch();
		studentGenearch.setId(UUIDLongGenerator.generateId());
		studentGenearch.setStudentId(studentId); //学生ID
		studentGenearch.setGenearchId(genearchId); //家长ID
		studentGenearch.setRelation(genearchTitle); //家长称谓
		getDatabaseService().saveRecord(studentGenearch);

		//把当前学生所在班级加入到家长的所在机构列表中
		Person genearch = (Person)getDatabaseService().findRecordById(Person.class.getName(), genearchId);
		if(genearch.getType()==PERSON_TYPE_GENEARCH) { //用户类型为家长
			//设置隶属关系
			PersonSubjection subjection = new PersonSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setPersonId(genearch.getId());
			//设置家长所在机构为学生所在班级ID
			subjection.setOrgId(((Number)getDatabaseService().findRecordByHql("select PersonSubjection.orgId from PersonSubjection PersonSubjection where PersonSubjection.personId=" + studentId)).longValue());
			getDatabaseService().saveRecord(subjection);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#deleteStudentGenearches(java.lang.String[])
	 */
	public void deleteStudentGenearches(String[] genearchIds) throws ServiceException {
		if(genearchIds==null || genearchIds.length==0) {
			return;
		}
		String ids = genearchIds[0];
		for(int i=1; i<genearchIds.length; i++) {
			ids += "," + genearchIds[i];
		}
		List genearches = getDatabaseService().findRecordsByHql("from StudentGenearch StudentGenearch where StudentGenearch.id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")");
		deleteStudentGenearches(genearches);
	}
	
	/**
	 * 删除学生家长列表
	 * @param genearches
	 * @throws ServiceException
	 */
	private void deleteStudentGenearches(Collection genearches) throws ServiceException {
		if(genearches==null || genearches.isEmpty()) {
			return;
		}
		for(Iterator iterator = genearches.iterator(); iterator.hasNext();) {
			StudentGenearch studentGenearch = (StudentGenearch)iterator.next();
			Person genearch = studentGenearch.getGenearch();
			if(genearch.getType()==PERSON_TYPE_GENEARCH) { //用户类型是家长
				String hql = "select count(StudentGenearch.id)" +
							 " from StudentGenearch StudentGenearch" +
							 " where StudentGenearch.genearchId=" + genearch.getId();
				int count = ((Number)getDatabaseService().findRecordByHql(hql)).intValue();
				if(count<2) { //用户只是一个孩子的家长
					getDatabaseService().deleteRecord(genearch); //删除家长记录
					continue;
				}
				else {
					//从家长的组织机构列表中删除学生所在班级
					getDatabaseService().deleteRecordsByHql("from PersonSubjection PersonSubjection where PersonSubjection.personId=" + genearch.getId() + " and PersonSubjection.orgId in (select PersonSubjection.orgId from PersonSubjection PersonSubjection where PersonSubjection.personId=" + studentGenearch.getStudentId() + ")");
				}
			}
			getDatabaseService().deleteRecord(studentGenearch); //删除自己
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#downloadPersonPortrait(long, javax.servlet.http.HttpServletResponse)
	 */
	public void downloadPersonPortrait(long personId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		Image image = getPersonPortrait(personId, false);
		try {
			fileDownloadService.httpDownload(request, response, image.getFilePath(), null, false, null);
		}
		catch (FileTransferException e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#getPersonPortrait(long)
	 */
	public Image getPersonPortrait(long personId, boolean useHttps) throws ServiceException {
		return getPersonPortrait(personId, null, useHttps);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#getPersonPortraitByLoginName(java.lang.String, boolean)
	 */
	public Image getPersonPortraitByLoginName(String loginName, boolean useHttps) throws ServiceException {
		Person person = (Person)listPersonsByLoginNames(loginName).get(0);
		return getPersonPortrait(person.getId(), person, useHttps);
	}
	
	/**
	 * 获取用户头像
	 * @param personId
	 * @param person
	 * @param useHttps
	 * @return
	 * @throws ServiceException
	 */
	private Image getPersonPortrait(long personId, Person person, boolean useHttps) throws ServiceException {
		List images = imageService.list(APPLICATION_NAME, IMAGE_TYPE_PORTRAIT, personId, false, 1, null);
		Image image = null;
		if(images!=null && !images.isEmpty()) {
			image = (Image)images.get(0);
		}
		else {
			//获取系统默认头像
			image = new Image();
			String personType;
			String personSex;
			if(person==null) {
				Object[] values = (Object[])getDatabaseService().findRecordByHql("select Person.type, Person.sex from Person Person where Person.id=" + personId);
				personType = PERSON_TYPE_NAMES[values==null ? PERSON_TYPE_EMPLOYEE : ((Number)values[0]).intValue()];
				personSex = values==null ?  "male" : ((Character)values[1]).charValue()=='M' ? "male" : "female";
			}
			else {
				personType = PERSON_TYPE_NAMES[person.getType()];
				personSex = person.getSex()=='F' ? "female" : "male";
			}
			String filePath = Environment.getWebinfPath() + APPLICATION_NAME + "/" + IMAGE_TYPE_PORTRAIT + "/" + personType + "_" + personSex + ".gif";
			image.setFilePath(filePath);
		}
		image.setUrl((useHttps ? webApplicationSafeUrl : webApplicationUrl) + "/" + APPLICATION_NAME + "/portrait.shtml?personId=" + personId);
		return image;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getPersonByLoginName(java.lang.String)
	 */
	public Person getPersonByLoginName(String loginName) throws ServiceException {
		return (Person)getDatabaseService().findRecordByHql("from Person Person where Person.loginName='" + JdbcUtils.resetQuot(loginName) + "'", listLazyLoadProperties(Person.class));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#removeStudentFromClass(long)
	 */
	public void deleteStudentFromClass(long studentId) throws ServiceException {
		//获取学生原来所在班级的id
		PersonSubjection currentSubjection = (PersonSubjection)getDatabaseService().findRecordByHql("from PersonSubjection PersonSubjection where PersonSubjection.personId=" + studentId + " order by PersonSubjection.id DESC");
		Org org = orgService.getOrg(currentSubjection.getOrgId());
		if(!(org instanceof SchoolClass)) {
			return;
		}
		SchoolClass myClass = (SchoolClass)org;
		if(myClass.getIsGraduated()=='1') { //已经毕业或者不是班级
			return;
		}
		Org mySchool = orgService.getPersonalUnitOrSchool(studentId);
		//更加家长所在机构
		synchGenearchSubjection(studentId, currentSubjection.getOrgId(), mySchool.getId());
		//把学生所属机构改为所在学校
		currentSubjection.setOrgId(mySchool.getId());
		getDatabaseService().updateRecord(currentSubjection);

		//用户同步
		Person student = getPerson(studentId);
		userSynchClientList.deleteStudentFromClass(student, myClass);
		synchPerson(student, student.getLoginName());
	}
	
	/**
	 * 更加家长所在机构
	 * @param studentId
	 * @param oldOrgId
	 * @param newOrgId
	 * @throws ServiceException
	 */
	private void synchGenearchSubjection(long studentId, long oldOrgId, long newOrgId) throws ServiceException {
		//获取学生家长的隶属列表,并更新成新的班级ID
		String hql = "from PersonSubjection PersonSubjection" +
					 " where PersonSubjection.orgId=" + oldOrgId +
					 " and PersonSubjection.personId in (" +
					 "   select StudentGenearch.genearchId" + 
					 "	  from StudentGenearch StudentGenearch" +
					 "	  where StudentGenearch.studentId=" + studentId + ")";
		List genearchSubjections = getDatabaseService().findRecordsByHql(hql);
		if(genearchSubjections!=null && !genearchSubjections.isEmpty()) {
			for(Iterator iterator = genearchSubjections.iterator(); iterator.hasNext();) {
				PersonSubjection subjection = (PersonSubjection)iterator.next();
				subjection.setOrgId(newOrgId);
				getDatabaseService().updateRecord(subjection);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLongNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select Person.id from Person Person where Person.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		if(!selfRegistEnabled) { //禁止用户自行注册
			return false;
		}
		//检查注册用户表(未审批的)
		return (getDatabaseService().findRecordByHql("select RegistPerson.id from RegistPerson RegistPerson where RegistPerson.approverId=0 and RegistPerson.loginName='" + JdbcUtils.resetQuot(loginName) + "'")!=null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			List persons = listPersonsByLoginNames(loginName, true); //按登录用户名查找用户
			if(persons==null || persons.isEmpty()) {
				return null;
			}
			Person person = (Person)persons.get(0);
			//设置用户信息
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setLoginName(person.getLoginName());
			sessionInfo.setUserType(person.getType());
			sessionInfo.setInternalUser(true);
			String password = person.getPassword();
			if(password==null || password.equals("")) {
				password = person.getLoginName();
			}
			else { //口令解密
				password = decryptPassword(person.getId(), password);
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(person.getName());
			sessionInfo.setUserId(person.getId());
			String orgIds = orgService.listOrgIdsOfPerson("" + person.getId(), true);
			sessionInfo.setDepartmentId(Long.parseLong(orgIds.split(",")[0]));
			sessionInfo.setDepartmentName(orgService.getOrg(sessionInfo.getDepartmentId()).getDirectoryName());
			Org unit = orgService.getPersonalUnitOrSchool(person.getId());
			if(unit!=null) {
				sessionInfo.setUnitId(unit.getId());
				sessionInfo.setUnitName(unit.getDirectoryName());
			}
			//设置部门信息
			sessionInfo.setDepartmentIds(orgIds);
			//设置角色信息
			List roles = roleService.listRolesOfPerson("" + person.getId(), false);
			sessionInfo.setRoleIds(ListUtils.join(roles, "id", ",", false));
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		loginName = loginName.toLowerCase(); //用户名不区分大小写
    	//用户校验
    	List personList = listPersonsByLoginNames(loginName, true); //按登录用户名查找用户记录
		if(personList==null || personList.isEmpty()) {
			personList = listPersonsByName(loginName);
			if(personList==null || personList.size()!=1) {
				return null;
			}
    	}
    	Person person = (Person)personList.get(0);
    	String correctPassword = loginName; //正确的密码
		//密码校验
	    if(person.getPassword()!=null && !person.getPassword().equals("")) { //注:密码为空时,要求用户输入的密码必须和用户名相同
	    	correctPassword = decryptPassword(person.getId(), person.getPassword());
	    }
		boolean passwordWrong = PersonService.PERSON_NOT_LOGIN_PASSWORD.equals(correctPassword) || //只读用户
    						    !passwordMatcher.matching(correctPassword, password); //密码错误
    	return new MemberLogin(person.getLoginName(), person.getId(), person.getType(), correctPassword, passwordWrong);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		Person person = (Person)load(Person.class, memberId);
		if(person==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, person);
		}
		catch(Exception e) {
			
		}
		member.setProfileURL("javascript:DialogUtils.openDialog('" + Environment.getWebApplicationSafeUrl() + "/jeaf/usermanage/admin/personalData.shtml', 550, 320);");
		member.setMemberType(person.getType());
		member.setOriginalRecord(person);
		return member;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		List personList = listPersonsByLoginNames(loginName.toLowerCase(), true); //按登录用户名查找用户记录
		if(personList==null || personList.isEmpty()) {
			return false;
		}
    	Person person = (Person)personList.get(0); //按登录用户名查找用户记录
    	if(validateOldPassword) {
	    	if(person.getPassword()==null || person.getPassword().equals("")) {
			    if(!oldPassword.equals(person.getLoginName())) {
			        throw(new WrongPasswordException()); //密码错误
			    }
			}
			else {
				if(!encryptPassword(person.getId(), oldPassword).equals(person.getPassword())) {
					throw(new WrongPasswordException()); //密码错误
				}
			}
    	}
		person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), newPassword)); //加密口令
		userReplicateServiceList.modifyPerson(person); //修改用户信息
		getDatabaseService().updateRecord(person);
		synchPerson(person, person.getLoginName());
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#getPersonLoginName(long)
	 */
	public String getPersonLoginName(long personId) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listPersons(java.lang.String)
	 */
	public List listPersons(String personIds) throws ServiceException {
		if(personIds==null || personIds.equals("")) {
			return null;
		}
		List persons = getDatabaseService().findRecordsByHql("from Person Person where Person.id in (" + JdbcUtils.validateInClauseNumbers(personIds) + ")", 0, 0);
		//根据ID的顺序,调整输出列表顺序
		return ListUtils.sortByProperty(persons, "id", personIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listPersonsByLoginNames(java.lang.String)
	 */
	public List listPersonsByLoginNames(String personLoginNames) throws ServiceException {
		return listPersonsByLoginNames(personLoginNames, false);
	}
	
	/**
	 * 按登录用户名获取用户
	 * @param personLoginNames
	 * @param excludePesonLink
	 * @return
	 * @throws ServiceException
	 */
	private List listPersonsByLoginNames(String personLoginNames, boolean excludePesonLink) throws ServiceException {
		String hql = "from Person Person" +
					 " where Person.loginName in ('" + JdbcUtils.resetQuot(personLoginNames).replaceAll("\\x2C", "','") + "')" +
					 (excludePesonLink ? " and Person.type!=" + PERSON_TYPE_LINK : "");
		List persons = getDatabaseService().findRecordsByHql(hql, listLazyLoadProperties(Person.class));
		return ListUtils.sortByProperty(persons, "loginName", personLoginNames);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listPersonsByName(java.lang.String)
	 */
	public List listPersonsByName(String personName) throws ServiceException {
		String hql = "from Person Person where Person.name='" + JdbcUtils.resetQuot(personName) + "'";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listStudentGenearchs(long)
	 */
	public List listStudentGenearchs(long studentId) throws ServiceException {
		return getDatabaseService().findRecordsByHql("from StudentGenearch StudentGenearch where StudentGenearch.studentId=" + studentId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listPersonSupervisors(long)
	 */
	public List listPersonSupervisors(long personId) throws ServiceException {
		//获取用户自己的分管领导
		String hql = "select Person " +
					 " from Person Person, PersonSupervisor PersonSupervisor" +
					 " where PersonSupervisor.personId=" + personId +
					 " and Person.id=PersonSupervisor.supervisorId";
		List supervisors = getDatabaseService().findRecordsByHql(hql);
		if(supervisors!=null && !supervisors.isEmpty()) {
			return supervisors;
		}
		
		supervisors = new ArrayList();
		//获取用户任部门领导的部门
		hql = "select OrgLeader.orgId" +
			  " from OrgLeader OrgLeader" +
			  " where OrgLeader.leaderId=" + personId;
		List leadOrgIds = getDatabaseService().findRecordsByHql(hql);

		//获取部门的主管领导
		if(leadOrgIds!=null && !leadOrgIds.isEmpty()) {
			for(Iterator iterator = leadOrgIds.iterator(); iterator.hasNext();) {
				Number orgId = (Number)iterator.next();
				combinePersons(supervisors, listOrgSupervisors(orgId.longValue()));
			}
		}
		
		//获取所在部门的领导
		hql = "select PersonSubjection.orgId" +
			  " from PersonSubjection PersonSubjection" +
			  " where PersonSubjection.personId=" + personId;
		List personOrgIds = getDatabaseService().findRecordsByHql(hql);
		
		if(personOrgIds!=null && !personOrgIds.isEmpty()) {
			for(Iterator iterator=personOrgIds.iterator(); iterator.hasNext();) {
				Number orgId = (Number)iterator.next();
				if(leadOrgIds!=null && !leadOrgIds.isEmpty() && leadOrgIds.indexOf(orgId)!=-1) {
					continue;
				}
				combinePersons(supervisors, listOrgLeaders(orgId.longValue())); //合并用户列表
			}
		}
		return supervisors.isEmpty() ? null : supervisors;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listSupervisePersons(long)
	 */
	public List listSupervisePersons(long personId) throws ServiceException {
		//获取用户直接分管的用户
		String hql = "select Person" +
					 " from Person Person, PersonSupervisor PersonSupervisor" +
					 " where PersonSupervisor.supervisorId=" + personId +
					 " and Person.id=PersonSupervisor.personId";
		List supervisePersons = getDatabaseService().findRecordsByHql(hql);
		if(supervisePersons==null) {
			supervisePersons = new ArrayList();
		}
		
		//获取分管的部门
		hql = "select OrgSupervisor.orgId " +
			  " from OrgSupervisor OrgSupervisor" +
			  " where OrgSupervisor.supervisorId=" + personId;
		List superviseOrgIds = getDatabaseService().findRecordsByHql(hql);
		
		if(superviseOrgIds!=null && !superviseOrgIds.isEmpty()) {
			for(Iterator iterator = superviseOrgIds.iterator(); iterator.hasNext();) {
				Number superviseOrgId = (Number)iterator.next();
				listSupervisePersonsBySuperviseOrg(supervisePersons, superviseOrgId.longValue());
			}
		}
		
		//获取自己任部门领导的部门
		hql = "select OrgLeader.orgId " +
			  " from OrgLeader OrgLeader" +
			  " where OrgLeader.leaderId=" + personId;
		List leadOrgIds = getDatabaseService().findRecordsByHql(hql);
		if(leadOrgIds!=null && !leadOrgIds.isEmpty()) {
			for(Iterator iterator = leadOrgIds.iterator(); iterator.hasNext();) {
				Number leadOrgId = (Number)iterator.next();
				listSupervisePersonsByLeadOrg(supervisePersons, leadOrgId.longValue());
			}
		}
		if(supervisePersons.isEmpty()) {
			return null;
		}
		Collections.sort(supervisePersons, new Comparator() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object arg0, Object arg1) {
				Person person0 = (Person)arg0;
				Person person1 = (Person)arg1;
				return Collator.getInstance(Locale.CHINA).compare(person0.getName(), person1.getName());
			}
		});
		return supervisePersons;
	}
	
	/**
	 * 在用户主管部门,获取分管的用户
	 * @param superviseOrgId
	 * @return
	 * @throws ServiceException
	 */
	private void listSupervisePersonsBySuperviseOrg(List supervisePersons, long superviseOrgId) throws ServiceException {
		//获取被分管部门的领导
		String hql = "select OrgLeader.leaderId" +
			  		 " from OrgLeader OrgLeader" +
			  		 " where OrgLeader.orgId=" + superviseOrgId;
		List orgLeaderIds = getDatabaseService().findRecordsByHql(hql);
		if(orgLeaderIds==null || orgLeaderIds.isEmpty()) { //没有部门领导
			listSupervisePersonsByLeadOrg(supervisePersons, superviseOrgId); //把用户作为部门领导来处理
			return;
		}
		//剔除直接指定了分管领导的用户
		hql = "select Person" +
			  " from Person Person" +
			  " where Person.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(orgLeaderIds, ",", false)) + ")" +
			  " and Person.id not in (" + //没有配置分管领导
			  "   select PersonSupervisor.personId" +
			  "    from PersonSupervisor PersonSupervisor" +
			  "    where PersonSupervisor.personId=Person.id" +
			  " )";
		combinePersons(supervisePersons, getDatabaseService().findRecordsByHql(hql));
	}
	
	/**
	 * 递归:在用户任部门领导的部门,获取分管的用户
	 * @param leadOrgId
	 * @return
	 * @throws ServiceException
	 */
	private void listSupervisePersonsByLeadOrg(List supervisePersons, long leadOrgId) throws ServiceException {
		//获取部门非领导、没有指定分管领导的用户列表
		String hql = "select Person" +
					 " from Person Person, PersonSubjection PersonSubjection" +
					 " where Person.id=PersonSubjection.personId" +
					 " and PersonSubjection.orgId=" + leadOrgId +
					 " and Person.id not in (" + //不是部门领导
					 "  select OrgLeader.leaderId" +
					 "   from OrgLeader OrgLeader" +
					 "   where OrgLeader.leaderId=Person.id" +
					 " )" +
					 " and Person.id not in (" + //没有指定分管领导
					 "  select PersonSupervisor.personId" +
					 "   from PersonSupervisor PersonSupervisor" +
					 "   where PersonSupervisor.personId=Person.id" +
					 " )";
		combinePersons(supervisePersons, getDatabaseService().findRecordsByHql(hql));
		//获取下级部门中没有部门领导/分管领导的部门
		hql = "select Org.id" +
			  " from Org Org" +
			  " where Org.parentDirectoryId=" + leadOrgId +
			  " and Org.id not in (" + //没有部门领导
			  "  select OrgLeader.orgId" +
			  "   from OrgLeader OrgLeader" +
			  "   where OrgLeader.orgId=Org.id" +
			  " )" +
			  " and Org.id not in (" + //没有分管领导
			  "  select OrgSupervisor.orgId" +
			  "   from OrgSupervisor OrgSupervisor" +
			  "   where OrgSupervisor.orgId=Org.id" +
			  " )";
		List childOrgIds = getDatabaseService().findRecordsByHql(hql);
		if(childOrgIds==null || childOrgIds.isEmpty()) {
			return;
		}
		//递归获取下级部门的用户
		for(Iterator iterator = childOrgIds.iterator(); iterator.hasNext();) {
			Number childOrgId = (Number)iterator.next();
			listSupervisePersonsByLeadOrg(supervisePersons, childOrgId.longValue());
		}
	}
	
	/**
	 * 合并用户列表
	 * @param persons
	 * @param newPersons
	 * @throws ServiceException
	 */
	private void combinePersons(List persons, List newPersons) throws ServiceException {
		if(newPersons==null || newPersons.isEmpty()) {
			return;
		}
		for(Iterator iterator=newPersons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			if(ListUtils.findObjectByProperty(persons, "id", new Long(person.getId()))==null) {
				persons.add(person);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listOrgSupervisors(java.lang.String)
	 */
	public List listOrgSupervisors(String orgIds) throws ServiceException {
		if(orgIds==null || orgIds.isEmpty()) {
			return null;
		}
		String[] ids = orgIds.split(",");
		List supervisors = new ArrayList();
		for(int i=0; i<ids.length; i++) {
			List orgSupervisors = listOrgSupervisors(Long.parseLong(ids[i]));
			if(orgSupervisors==null || orgSupervisors.isEmpty()) {
				continue;
			}
			for(Iterator iterator = orgSupervisors.iterator(); iterator.hasNext();) {
				Person person = (Person)iterator.next();
				if(ListUtils.findObjectByProperty(supervisors, "id", new Long(person.getId()))==null) { //检查是否已经存在
					supervisors.add(person);
				}
			}
		}
		return supervisors.isEmpty() ? null : supervisors;
	}
	
	/**
	 * 获取分管领导
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgSupervisors(long orgId) throws ServiceException {
		//获取部门自己的分管领导
		String hql = "select Person " +
					 " from Person Person, OrgSupervisor OrgSupervisor" +
					 " where OrgSupervisor.orgId=" + orgId +
					 " and Person.id=OrgSupervisor.supervisorId";
		List supervisors = getDatabaseService().findRecordsByHql(hql);
		if(orgId==0 || (supervisors!=null && !supervisors.isEmpty())) {
			return supervisors;
		}
		
		//获取上级组织机构ID
		hql = "select Org.parentDirectoryId from Org Org where Org.id=" + orgId;
		Number parentOrgId = (Number)getDatabaseService().findRecordByHql(hql);
		
		//获取上级部门的领导
		return getOrgLeaders(parentOrgId.longValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#listOrgLeaders(long)
	 */
	public List listOrgLeaders(long orgId) throws ServiceException {
		return getOrgLeaders(orgId);
	}
	
	/**
	 * 递归:获取部门领导
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	private List getOrgLeaders(long orgId) throws ServiceException {
		//获取部门自己的领导
		String hql = "select Person " +
					 " from Person Person, OrgLeader OrgLeader" +
					 " where OrgLeader.orgId=" + orgId +
					 " and Person.id=OrgLeader.leaderId";
		List leaders = getDatabaseService().findRecordsByHql(hql);
		if(leaders!=null && !leaders.isEmpty()) {
			return leaders;
		}
		
		//获取上级组织机构ID
		hql = "select Org.parentDirectoryId from Org Org where Org.id=" + orgId;
		Number parentOrgId = (Number)getDatabaseService().findRecordByHql(hql);
		
		//获取分管领导
		hql = "select Person " +
			  " from Person Person, OrgSupervisor OrgSupervisor" +
			  " where OrgSupervisor.orgId=" + parentOrgId.longValue() +
			  " and Person.id=OrgSupervisor.supervisorId";
		List supervisors = getDatabaseService().findRecordsByHql(hql);
		if(parentOrgId.longValue()==0 || (supervisors!=null && !supervisors.isEmpty())) {
			return supervisors;
		}
		return getOrgLeaders(parentOrgId.longValue()); //递归获取上级部门领导
	}

	/* (non-Javadoc)
	 * @see com.kdsoft.jeaf.usermanage.service.PersonService#isNicknameUsed(java.lang.String, long)
	 */
	public boolean isNicknameUsed(String nickname, long personId) throws ServiceException {
		return getDatabaseService().findRecordByHql("select Person.id from Person Person where Person.nickname='" + JdbcUtils.resetQuot(nickname) + "' and Person.id!=" + personId)!=null;
	}

	/* (non-Javadoc)
	 * @see com.kdsoft.jeaf.usermanage.service.PersonService#getPersonByNickname(java.lang.String)
	 */
	public List listPersonsByNickname(String nickname) throws ServiceException {
		if(nickname==null || nickname.equals("")) {
			return null;
		}
		return getDatabaseService().findRecordsByHql("from Person Person where Person.nickname='" + JdbcUtils.resetQuot(nickname) + "'");
	}
	
	/**
	 * 添加用户回调
	 * @author chuan
	 *
	 */
	private interface PersonModifyCallback {
		/**
		 * 用户保存前,用来设置私有属性
		 * @param person
		 * @param isNew
		 */
		public void onBeforeSavePerson(Person person, boolean isNew);
	}
	
	/**
	 * @return Returns the userSynchClientList.
	 */
	public UserSynchClientList getUserSynchClientList() {
		return userSynchClientList;
	}
	/**
	 * @param userSynchClientList The userSynchClientList to set.
	 */
	public void setUserSynchClientList(UserSynchClientList userSynchClientList) {
		this.userSynchClientList = userSynchClientList;
	}
    /**
     * @return Returns the cryptService.
     */
    public CryptService getCryptService() {
        return cryptService;
    }
    /**
     * @param cryptService The cryptService to set.
     */
    public void setCryptService(CryptService cryptService) {
        this.cryptService = cryptService;
    }
    /**
     * @return Returns the createMailAccount.
     */
    public boolean isCreateMailAccount() {
        return createMailAccount;
    }
    /**
     * @param createMailAccount The createMailAccount to set.
     */
    public void setCreateMailAccount(boolean createMailAccount) {
        this.createMailAccount = createMailAccount;
    }

	/**
	 * @return the webApplicationUrl
	 */
	public String getWebApplicationUrl() {
		return webApplicationUrl;
	}

	/**
	 * @param webApplicationUrl the webApplicationUrl to set
	 */
	public void setWebApplicationUrl(String webApplicationUrl) {
		this.webApplicationUrl = webApplicationUrl;
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
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * @return the webApplicationSafeUrl
	 */
	public String getWebApplicationSafeUrl() {
		return webApplicationSafeUrl;
	}

	/**
	 * @param webApplicationSafeUrl the webApplicationSafeUrl to set
	 */
	public void setWebApplicationSafeUrl(String webApplicationSafeUrl) {
		this.webApplicationSafeUrl = webApplicationSafeUrl;
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
	 * @return the newUserSynchClientList
	 */
	public UserSynchClientList getNewUserSynchClientList() {
		return newUserSynchClientList;
	}

	/**
	 * @param newUserSynchClientList the newUserSynchClientList to set
	 */
	public void setNewUserSynchClientList(UserSynchClientList newUserSynchClientList) {
		this.newUserSynchClientList = newUserSynchClientList;
	}

	/**
	 * @return the sidSequence
	 */
	public String getSidSequence() {
		return sidSequence;
	}

	/**
	 * @param sidSequence the sidSequence to set
	 */
	public void setSidSequence(String sidSequence) {
		this.sidSequence = sidSequence;
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
	 * @return the selfRegistEnabled
	 */
	public boolean isSelfRegistEnabled() {
		return selfRegistEnabled;
	}

	/**
	 * @param selfRegistEnabled the selfRegistEnabled to set
	 */
	public void setSelfRegistEnabled(boolean selfRegistEnabled) {
		this.selfRegistEnabled = selfRegistEnabled;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
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
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @return the templateService
	 */
	public TemplateService getTemplateService() {
		return templateService;
	}

	/**
	 * @param templateService the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	/**
	 * @return the userReplicateServiceList
	 */
	public UserReplicateServiceList getUserReplicateServiceList() {
		return userReplicateServiceList;
	}

	/**
	 * @param userReplicateServiceList the userReplicateServiceList to set
	 */
	public void setUserReplicateServiceList(
			UserReplicateServiceList userReplicateServiceList) {
		this.userReplicateServiceList = userReplicateServiceList;
	}
}