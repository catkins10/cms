/*
 * Created on 2007-4-17
 *
 */
package com.yuanluesoft.jeaf.usermanage.service;

import java.util.List;

import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.FormulaSupport;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.School;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolClass;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolDepartment;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;

/**
 * 
 * @author linchuan
 * 
 */
public interface OrgService extends DirectoryService, FormulaSupport {
	
	/**
	 * 添加自定义的目录权限类型
	 * @param name
	 * @param title
	 * @param directoryTypes
	 * @param inheritFromParent
	 * @param keepMyselfPopedom
	 * @param navigatorFilter
	 * @throws ServiceException
	 */
	public void appendDirectoryPopedomType(String name, String title, String directoryTypes, int inheritFromParent, boolean keepMyselfPopedom, boolean navigatorFilter) throws ServiceException;
	
	/**
	 * 初始化根组织,如果根已经创建,不再重新初始化
	 * @param rootOrgName
	 * @param managerName
	 * @param managerLoginName
	 * @param managerPassword
	 * @throws ServiceException
	 */
	public boolean initRootOrg(String rootOrgName, String managerName, String managerLoginName, String managerPassword) throws ServiceException;
	
    /**
     * 根据id获取组织机构信息
     * @param orgId
     * @return
     * @throws ServiceException
     */
    public Org getOrg(long orgId) throws ServiceException;
	
	/**
	 * 添加班级任课教师或班主任
	 * @param classId
	 * @param teacherId
	 * @param teacherTitle
	 * @throws ServiceException
	 */
	public void addClassTeacher(long classId, long teacherId, String teacherTitle) throws ServiceException;
	
	/**
	 * 删除班级任课教师或班主任
	 * @param classTeacherIds
	 * @throws ServiceException
	 */
	public void deleteClassTeachers(String[] classTeacherIds) throws ServiceException;
	
	/**
	 * 添加学校
	 * @param name
	 * @param schoolCategory
	 * @param parentOrgId
	 * @param creatorId
	 * @param creatorName
	 * @param remark
	 * @return
	 * @throws ServiceException
	 */
	public School addSchool(String name, String schoolCategory, long parentOrgId, long creatorId, String creatorName, String remark) throws ServiceException;
	
	/**
	 * 为学校增加部门
	 * @param name
	 * @param parentOrgId
	 * @param priority
	 * @param creatorId
	 * @param creatorName
	 * @return
	 * @throws ServiceException
	 */
	public SchoolDepartment addSchoolDepartment(String name, long parentOrgId, long creatorId, String creatorName) throws ServiceException;

	/**
	 * 增加班级
	 * @param name
	 * @param classNumber
	 * @param enrollTime
	 * @param lengthOfSchooling
	 * @param parentOrgId
	 * @param creatorId
	 * @param creatorName
	 * @param remark
	 * @return
	 * @throws ServiceException
	 */
	public SchoolClass addClass(String name, int classNumber, int enrollTime, int lengthOfSchooling, long parentOrgId, long creatorId, String creatorName, String remark) throws ServiceException;
	
	/**
	 * 年级升级,毕业后第二年删除班级及其学生和家长,每年7月18日调用
	 * @throws ServiceException
	 */
	public void upgrade() throws ServiceException;
	
	/**
	 * 获取学科列表
	 * @param fullCurriculum 是否全称,如小学语文,初中英语
	 * @return
	 * @throws ServiceException
	 */
	public List listCurriculums(boolean fullCurriculum) throws ServiceException;

	/**
	 * 按学习阶段获取学科列表
	 * @return
	 * @throws ServiceException
	 */
	public List listCurriculumsByStage(String stage) throws ServiceException;
	
	/**
	 * 获取学习阶段列表,默认小学/初中/高中
	 * @return
	 * @throws ServiceException
	 */
	public List listStudyStages() throws ServiceException;
	
	/**
	 * 获取学校类型列表,默认小学/初中/高中/完全中学
	 * @return
	 * @throws ServiceException
	 */
	public List listSchoolTypes() throws ServiceException;
	
	/**
	 * 获取年级列表
	 * @return
	 * @throws ServiceException
	 */
	public List listGrades() throws ServiceException;

	/**
	 * 按学习阶段获取年级列表
	 * @return
	 * @throws ServiceException
	 */
	public List listGradesByStage(String stage) throws ServiceException;
	
	/**
	 * 判断老师是否是某个班的班主任
	 * @param teacherId
	 * @param classId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isTeacherInCharge(long teacherId, long classId) throws ServiceException;
	
	/**
	 * 列出老师任班主任的班级列表
	 * @param teacherId
	 * @return
	 * @throws ServiceException
	 */
	public List listClassesInCharge(long teacherId) throws ServiceException;
	
	/**
	 * 列出用户任管理员的学校列表
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public List listManagedSchools(long personId) throws ServiceException;
	
	/**
	 * 检查用户是否是组织的管理员
	 * @param ordId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isOrgManager(long orgId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
     * 获取上级组织机构ID列表,不包括自己,用逗号分隔
     * @param orgId
     * @return
     * @throws ServiceException
     */
    public String listParentOrgIds(long orgId) throws ServiceException;
    
	/**
	 * 获得一组部门(Org)
	 * @param orgIds
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgs(String orgIds) throws ServiceException;
	
    /**
     * 获取用户所在的单位或学校 
     * @return
     * @throws ServiceException
     */
    public Org getPersonalUnitOrSchool(long personId) throws ServiceException;
    
    /**
     * 获取用户所在部门
     * @param personId
     * @return
     * @throws ServiceException
     */
    public Org getPersonalDepartment(long personId) throws ServiceException;
    
    /**
     * 获取部门/班级所在的单位或学校
     * @param orgId
     * @return
     * @throws ServiceException
     */
    public Org getParentUnitOrSchool(long orgId) throws ServiceException;

    /**
     * 获取用户所在机构(Org)列表,其中第一个为直属机构
     * @param personIds
     * @param containsParentOrg 是否包括上级机构
     * @return
     * @throws ServiceException
     */
    public List listOrgsOfPerson(String personIds, boolean containsParentOrg) throws ServiceException;
    
    /**
     * 获取组织机构所在的区域列表,按从低到高顺序排列
     * @param personId
     * @return
     * @throws ServiceException
     */
    public List listParentAreasOfOrg(long orgId) throws ServiceException;
    
    /**
     * 获取用户所在机构ID列表,其中第一个为直属机构
     * @param personIds
     * @param containsParentOrg
     * @return
     * @throws ServiceException
     */
    public String listOrgIdsOfPerson(String personIds, boolean containsParentOrg) throws ServiceException;
    
    /**
     * 获取指定班级的教师,返回ClassTeacher
     * @param classId
     * @return
     * @throws ServiceException
     */
    public List listClassTeachers(long classId) throws ServiceException;
    
    /**
     * 获取指定班级的学生及其家长,返回Student
     * @param classId
     * @return
     * @throws ServiceException
     */
    public List listStudentsWithGenearchs(long classId) throws ServiceException;
    
	/**
	 * 获得部门人员(Person)列表
	 * @param orgIds
	 * @param personTypes 用户类型,teacher/student/employee/genearch,可以是多个,以逗号为分隔符,默认为teacher,student,employee
	 * @param containChildOrg 是否包扩下级部门
	 * @param readLazyLoadProperties 是否读取延迟加载的属性
	 * @param first
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgPersons(String orgIds, String personTypes, boolean containChildOrg, boolean readLazyLoadProperties, int first, int max) throws ServiceException;
	
	/**
	 * 获取班级学生列表
	 * @param classId
	 * @return
	 * @throws ServiceException
	 */
	public List listClassStudents(long classId) throws ServiceException;

	/**
	 * 检查用户是否属于指定组织
	 * @param personId
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isMemberOfOrg(long personId, long orgId) throws ServiceException;
	
	/**
	 * 获取与用户所在部门(用户所在部门可以有多个)的部门成员(Person)
	 * @param personIds
	 * @param personTypes
	 * @param containChildOrg
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonsOfPersonalOrg(String personIds, String personTypes, boolean containChildOrg, int max) throws ServiceException;
	
	/**
	 * 获取组织机构所在的地区
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public Org getAreaOfOrg(long orgId) throws ServiceException;
	
	/**
	 * 获取组织机构类型列表
	 * @return
	 * @throws ServiceException
	 */
	public String getOrgTypes() throws ServiceException;
	
	/**
	 * 导入地名
	 * @param global 是否导入全球地名
	 * @throws ServiceException
	 */
	public void importPlaceName(boolean global) throws ServiceException;
	
	/**
	 * 按编码获取单位
	 * @param unitCode
	 * @return
	 * @throws ServiceException
	 */
	public Unit getUnitByCode(String unitCode) throws ServiceException;
	
	/**
	 * 添加获取更新组织机构
	 * @param orgId
	 * @param parentOrgId
	 * @param name
	 * @param fullName
	 * @param type
	 * @param priority
	 * @param linkedOrgId
	 * @param leaderIds
	 * @param leaderNames
	 * @param supervisorIds
	 * @param supervisorNames
	 * @param remark
	 * @throws ServiceException
	 */
	public void addOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark) throws ServiceException;
	
	/**
	 * 获取第一个用户有访问权限的机构
	 * @param orgTypes
	 * @param popedomNames
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Org getFirstAccessibleOrg(String orgTypes, String popedomNames, SessionInfo sessionInfo) throws ServiceException;
}