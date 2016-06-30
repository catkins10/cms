package com.yuanluesoft.jeaf.usermanage.service;

import com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 
 * @author linchuan
 *
 */
public interface RegistPersonService extends BusinessService {
	public final static String ERROR_LOGINNAME_IN_USE = "LOGINNAME_IN_USE";
	public final static String ERROR_PASSWORD_INVALID = "PASSWORD_INVALID";
	
	/**
	 * 审批注册学生
	 * @param registStudentId
	 * @param classId 检查被注册学生是否在该班级里
	 * @param pass
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalRegistStudent(long registStudentId, long classId, boolean pass, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 审批教师注册
	 * @param registTeacherId
	 * @param schoolId 检查被注册教师是否属于该校
	 * @param pass
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalRegistTeacher(long registTeacherId, long schoolId, boolean pass, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 开通家长帐号
	 * @param classId
	 * @param classFullName
	 * @param childName
	 * @param name
	 * @param mobile
	 * @param validateCode
	 * @return
	 * @throws ServiceException
	 */
	public Person retrieveGenearchAccount(long classId, String classFullName, String childName, String name, String mobile, String validateCode) throws ServiceException;

	/**
	 * 修改家长账户
	 * @param loginName
	 * @param newLoginName
	 * @param newPassword
	 * @return
	 * @throws ServiceException
	 */
	public void modifyGenearchAccount(String loginName, String password, String newLoginName, String newPassword) throws ServiceException;
	
	/**
	 * 创建学校注册码
	 * @param schoolId
	 * @param code
	 * @return
	 * @throws ServiceException
	 */
	public SchoolRegistCode createSchoolRegistCode(long schoolId, String code) throws ServiceException;
	
	/**
	 * 获取学校注册码
	 * @param schoolId
	 * @return
	 * @throws ServiceException
	 */
	public SchoolRegistCode retireveSchoolRegistCode(long schoolId) throws ServiceException;
	
	/**
	 * 发送学校注册邮件
	 * @param schoolName
	 * @param managerName
	 * @param managerPasseord
	 * @param managerLoginName
	 * @param schoolRegistCode
	 * @return
	 * @throws ServiceException
	 */
	public void sendSchoolRegistMail(String mailAddress, String schoolName, String managerName, String managerPassword, String managerLoginName, String schoolRegistCode) throws ServiceException;
	
	/**
	 * 重发学校注册邮件
	 * @param schoolId
	 * @throws ServiceException
	 */
	public void resendSchoolRegistMail(long schoolId) throws ServiceException;
	
	/**
	 * 获取学校注册信息
	 * @param schoolId
	 * @return
	 * @throws ServiceException
	 */
	public String retrieveRegistInfo(long schoolId) throws ServiceException;
	
	/**
	 * 重发短信给学生家长
	 * @param schoolClassId 班级ID
	 * @throws ServiceException
	 */
	public void resendSmsToGenearch(long schoolClassId) throws ServiceException;
}
