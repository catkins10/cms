/*
 * Created on 2006-3-13
 *
 */
package com.yuanluesoft.jeaf.usermanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.usermanage.pojo.Employee;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonLink;
import com.yuanluesoft.jeaf.usermanage.pojo.Student;
import com.yuanluesoft.jeaf.usermanage.pojo.Teacher;

/**
 * 
 * @author linchuan
 *
 */
public interface PersonService extends BusinessService, com.yuanluesoft.jeaf.membermanage.service.MemberService {
	public static final int PERSON_TYPE_EMPLOYEE = 1; //普通用户
	public static final int PERSON_TYPE_TEACHER = 2; //教师
	public static final int PERSON_TYPE_STUDENT = 3; //学生
	public static final int PERSON_TYPE_GENEARCH = 4; //家长
	public static final int PERSON_TYPE_LINK = 5; //家长
	public static final int PERSON_TYPE_OTHER = 10; //其他用户自定义类型
	public static final String[] PERSON_TYPE_NAMES = {"", "employee", "teacher", "student", "genearch", "link"};
	public static final String[] PERSON_TYPE_TITLES = {"", "用户", "教师", "学生", "学生家长", "引用"};
	public static final String PERSON_NOT_LOGIN_PASSWORD = "NOPASSWORD"; //不需要登录的用户的密码
	
	/**
	 * 获取用户
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public Person getPerson(long personId) throws ServiceException;
	
	/**
	 * 按登录用户名获取用户
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	public Person getPersonByLoginName(String loginName) throws ServiceException;
	
	/**
	 * 获取用户名
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public String getPersonName(long personId) throws ServiceException;

	/**
     * 是否由系统创建邮件帐号
     * @return
     */
    public boolean isCreateMailAccount();
    
	/**
	 * 增加教师
	 * @param name
	 * @param loginName
	 * @param password
	 * @param sex
	 * @param tel
	 * @param telFamily
	 * @param mobile
	 * @param familyAddress
	 * @param mailAddress
	 * @param departmentId
	 * @param creatorId
	 * @param creatorName
	 * @return
	 * @throws ServiceException
	 */
	public Teacher addTeacher(String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException;
	
	/**
	 * 增加学生
	 * @param name
	 * @param loginName
	 * @param seatNumber
	 * @param password
	 * @param sex
	 * @param tel
	 * @param telFamily
	 * @param mobile
	 * @param familyAddress
	 * @param mailAddress
	 * @param classId
	 * @param creatorId
	 * @param creatorName
	 * @throws ServiceException
	 */
	public Student addStudent(String name, String loginName, int seatNumber, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String identityCard, String identityCardName, String remark, long classId, long creatorId, String creatorName) throws ServiceException;

	/**
	 * 增加家长
	 * @param name
	 * @param loginName 空时,自动生成
	 * @param password 空时,自动生成
	 * @param passwordEncryptMode
	 * @param title 
	 * @param sex 空时,根据title获取
	 * @param mobile
	 * @param mailAddress
	 * @param studentId
	 * @param creatorId
	 * @param creatorName
	 * @throws ServiceException
	 */
	public Genearch addGenearch(String name, String loginName, String password, String passwordEncryptMode, String title, char sex, String mobile, String mailAddress, long studentId, long creatorId, String creatorName) throws ServiceException;
	
	/**
	 * 注册普通用户
	 * @param id
	 * @param name
	 * @param loginName
	 * @param password
	 * @param sex
	 * @param tel
	 * @param telFamily
	 * @param mobile
	 * @param familyAddress
	 * @param mailAddress
	 * @param departmentId
	 * @param creatorId
	 * @param creatorName
	 * @return
	 * @throws ServiceException
	 */
	public Employee addEmployee(long id, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException;

	/**
	 * 添加用户引用
	 * @param id
	 * @param userClassName
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
	 * @return
	 * @throws ServiceException
	 */
	public PersonLink addPersonLink(long id, String userClassName, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException;

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
	 * @param departmentId
	 * @param creatorId
	 * @param creatorName
	 * @return
	 * @throws ServiceException
	 */
	public Person addPerson(long id, String type, String name, String loginName, String password, char sex, String tel, String telFamily, String mobile, String familyAddress, String mailAddress, String orgIds, long creatorId, String creatorName) throws ServiceException;
	
	/**
	 * 批量注册用户
	 * @param dataFilePath
	 * @param password
	 * @param departmentId
	 * @param creatorId
	 * @param creatorName
	 * @throws ServiceException
	 */
	public void batchRegistEmployees(String dataFilePath, String password, long departmentId, long creatorId, String creatorName) throws ServiceException;
	
	/**
	 * 添加学生家长
	 * @param studentId
	 * @param genearchId
	 * @param teacherTitle
	 * @throws ServiceException
	 */
	public void addStudentGenearch(long studentId, long genearchId, String genearchTitle) throws ServiceException;
	
	/**
	 * 删除家长
	 * @param genearchIds
	 * @throws ServiceException
	 */
	public void deleteStudentGenearches(String[] genearchIds) throws ServiceException;
	
	/**
	 * 获取用户头像
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public Image getPersonPortrait(long personId, boolean useHttps) throws ServiceException;
	
	/**
	 * 按登录用户名获取头像
	 * @param loginName
	 * @param useHttps
	 * @return
	 * @throws ServiceException
	 */
	public Image getPersonPortraitByLoginName(String loginName, boolean useHttps) throws ServiceException;
	
	/**
	 * 删除用户头像
	 * @param personId
	 * @throws ServiceException
	 */
	public void downloadPersonPortrait(long personId, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 按用户名生成登录用户名
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	public String generateLoginName(String name) throws ServiceException;
	
	/**
	 * 口令解密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public String decryptPassword(long userId, String password) throws ServiceException;
	
	/**
	 * 把学生从班级里删除
	 * @param studentId
	 * @throws ServiceException
	 */
	public void deleteStudentFromClass(long studentId) throws ServiceException;
	
	/**
	 * 同步所有的用户
	 * @throws ServiceException
	 */
	public void synchAllPerson() throws ServiceException;
	
	/**
	 * 获取登录用户名
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public String getPersonLoginName(long personId) throws ServiceException;
	
	/**
	 * 获得用户(Person)列表
	 * @param personIds
	 * @param nameOnly 仅用户名
	 * @return
	 * @throws ServiceException
	 */
	public List listPersons(String personIds) throws ServiceException;
	
	/**
	 * 按用户登录名获取用户(Person)列表
	 * @param personLoginNames
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonsByLoginNames(String personLoginNames) throws ServiceException;
	
	/**
	 * 按用户姓名获取用户列表
	 * @param personName
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonsByName(String personName) throws ServiceException;
	
	/**
	 * 获取学生家长列表
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	public List listStudentGenearchs(long studentId) throws ServiceException;
	
	/**
	 * 获取用户分管领导
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonSupervisors(long personId) throws ServiceException;
	
	/**
	 * 获取用户分管的用户
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public List listSupervisePersons(long personId) throws ServiceException;
	
	/**
	 * 获取部门主管领导(Person)
	 * @param orgIds
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgSupervisors(String orgIds) throws ServiceException;
	
	/**
	 * 获取部门领导
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgLeaders(long orgId) throws ServiceException;
	
	/**
	 * 检查昵称是否被使用
	 * @param nickname
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isNicknameUsed(String nickname, long personId) throws ServiceException;
	
	/**
	 * 按昵称查找用户
	 * @param nickname
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonsByNickname(String nickname) throws ServiceException;
	
	/**
	 * 更新用户隶属的组织机构ID
	 * @param personId
	 * @param subjectionOrgIds
	 * @throws ServiceException
	 */
	public void updatePersonSubjection(long personId, String subjectionOrgIds) throws ServiceException;
}