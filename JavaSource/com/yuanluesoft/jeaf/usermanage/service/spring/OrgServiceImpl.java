/*
 * Created on 2007-4-17
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Element;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.model.StudyStage;
import com.yuanluesoft.jeaf.usermanage.pojo.Area;
import com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgCategory;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgLeader;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgLink;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgPopedom;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgRoot;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgSubjection;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgSupervisor;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.School;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolClass;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolDepartment;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.pojo.UnitDepartment;
import com.yuanluesoft.jeaf.usermanage.replicator.spring.UserReplicateServiceList;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 * TODO 1、机构同步 2、组织机构名称或上级机构变更后,用户信息同步
 * 
 */
public class OrgServiceImpl extends DirectoryServiceImpl implements OrgService {
	private DistributionService distributionService;
	private String sidSequence = null;
	private PersonService personService; //用户服务
	private UserSynchClientList userSynchClientList; //用户同步客户端列表
	private UserSynchClientList newUserSynchClientList; //新添加的用户同步客户端列表
	private SessionService sessionService;
	private GpsService gpsService; //GPS定位服务,用来按网站获取地区名称
	private TemplateService templateService; //模板服务,清除模板缓存用
	private boolean schoolSupport; //允是否支持学校的管理
	private Cache recordCache; //记录缓存,需要缓存时配置
	private UserReplicateServiceList userReplicateServiceList; //用户复制服务列表
	
	//选项配置
	private List schoolTypes; //学校类型列表
	private List studyStages; //学习阶段列表
	private List studyStageNames;
	private List curriculums; //科目列表
	private List fullCurriculums; //科目列表,显示全部,如小学语文,初中英语
	private List grades; //年级列表
	
	//私有属性
	private List customDirectoryPopedomTypes;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return Org.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return OrgLink.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		DirectoryType[] directoryTypes = new DirectoryType[schoolSupport ? 8 : 5];
		directoryTypes[0] = new DirectoryType("root", null, "根目录", OrgRoot.class, "/jeaf/usermanage/icons/root.gif", null, true);
		directoryTypes[1] = new DirectoryType("category", "root,area", "分类", OrgCategory.class, "/jeaf/usermanage/icons/category.gif", null, true);
		directoryTypes[2] = new DirectoryType("area", "root,area,category", "区域", Area.class, "/jeaf/usermanage/icons/area.gif", null, true);
		directoryTypes[3] = new DirectoryType("unit", "root,area,category,unit", "单位", Unit.class, "/jeaf/usermanage/icons/unit.gif", null, true);
		directoryTypes[4] = new DirectoryType("unitDepartment", "unit", "部门", UnitDepartment.class, "/jeaf/usermanage/icons/unitDepartment.gif", null, false);
		if(schoolSupport) {
			directoryTypes[5] = new DirectoryType("school", "root,area,category", "学校", School.class, "/jeaf/usermanage/icons/school.gif", null, true);
			directoryTypes[6] = new DirectoryType("schoolDepartment", "school", "部门", SchoolDepartment.class, "/jeaf/usermanage/icons/schoolDepartment.gif", null, false);
			directoryTypes[7] = new DirectoryType("schoolClass", "school", "班级", SchoolClass.class, "/jeaf/usermanage/icons/schoolClass.gif", null, false);
		}
		return directoryTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		DirectoryPopedomType[] popedomTypes = new DirectoryPopedomType[(schoolSupport ? 5 : 3) + (customDirectoryPopedomTypes==null ? 0 : customDirectoryPopedomTypes.size())];
		popedomTypes[0] = new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true);
		popedomTypes[1] = new DirectoryPopedomType("registEmployee", "root,category,area,unit,unitDepartment,school,schoolDepartment", "用户注册权限", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true);
		popedomTypes[2] = new DirectoryPopedomType("browse", "浏览", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, false);
		if(schoolSupport) {
			popedomTypes[3] = new DirectoryPopedomType("registTeacher", "教师注册权限", "root,category,area,school,schoolDepartment", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true);
			popedomTypes[4] = new DirectoryPopedomType("registStudent", "学生注册权限", "root,category,area,school,schoolClass", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true);
		}
		for(int i=0; i<(customDirectoryPopedomTypes==null ? 0 : customDirectoryPopedomTypes.size()); i++) {
			DirectoryPopedomType directoryPopedomType = (DirectoryPopedomType)customDirectoryPopedomTypes.get(i);
			popedomTypes[(schoolSupport ? 5 : 3) + i] = directoryPopedomType;
		}
		return popedomTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#appendDirectoryPopedomType(java.lang.String, java.lang.String, java.lang.String, boolean, boolean, boolean)
	 */
	public void appendDirectoryPopedomType(String name, String title, String directoryTypes, int inheritFromParent, boolean keepMyselfPopedom, boolean navigatorFilter) throws ServiceException {
		if(ListUtils.findObjectByProperty(customDirectoryPopedomTypes, "name", name)!=null) {
			return;
		}
		if(customDirectoryPopedomTypes==null) {
			customDirectoryPopedomTypes = new ArrayList();
		}
		customDirectoryPopedomTypes.add(new DirectoryPopedomType(name, title, directoryTypes, inheritFromParent, keepMyselfPopedom, navigatorFilter));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/jeaf/usermanage/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/jeaf/usermanage/admin/personView.shtml?orgId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		//删除用户
		String hql = "select Person" +
					 " from Person Person left join Person.subjections PersonSubjection" +
					 " where PersonSubjection.orgId=" + directory.getId();
		List persons = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections,genearches", ","));
		if(persons!=null) {
			for(Iterator iterator = persons.iterator(); iterator.hasNext(); ) {
				Person person = (Person)iterator.next();
				if(!(person instanceof Genearch)) { //非家长用户,家长在删除学生时自动被删除
					if(person.getSubjections().size()<2) { //用户只属于一个组织
						personService.delete(person); //删除单个用户
					}
					else { //用户属于多个组织
						//删除用户隶属记录
						getDatabaseService().deleteRecord((Record)ListUtils.findObjectByProperty(person.getSubjections(), "orgId", new Long(directory.getId())));
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#childDirectoriesFilter(long, java.util.List, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void childDirectoriesFilter(long parentDierctoryId, List directoryIds, String directoryTypeFilters, String popedomFilters, SessionInfo sessionInfo) throws ServiceException {
		if(parentDierctoryId==sessionInfo.getUnitId()) { //上级目录是用户所在单位
			return;
		}
		if(isChildDirectory(parentDierctoryId, sessionInfo.getUnitId())) { //上级机构是否是所在单位的下级
			return;
		}
		//获取用户所在的组织
		List myOrgs = new ArrayList();
		for(Iterator iterator = directoryIds.iterator(); iterator.hasNext();) {
			Object id = iterator.next();
			if(("," + sessionInfo.getDepartmentIds() + ",").indexOf("," + id + ",")!=-1) {
				myOrgs.add(id);
				iterator.remove();
			}
		}
		super.childDirectoriesFilter(parentDierctoryId, directoryIds, directoryTypeFilters, popedomFilters, sessionInfo);
		directoryIds.addAll(myOrgs);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#initRootOrg(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean initRootOrg(String rootOrgName, String managerName, String managerLoginName, String managerPassword) throws ServiceException {
		if(getDirectory(0)!=null) { //检查是否已经创建过根组织
			return false;
		}
		//创建根组织
		OrgRoot root = new OrgRoot();
		root.setDirectoryType("root");
		root.setDirectoryName(rootOrgName);
		save(root);
		
		//更新隶属关系
		OrgSubjection subjection = new OrgSubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//创建管理员用户
		long managerId = UUIDLongGenerator.generateId();
		personService.addEmployee(managerId, managerName, managerLoginName, managerPassword, 'M', null, null, null, null, null, "0", managerId, managerName);

		//授权
		OrgPopedom popedom = new OrgPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		Org org = (Org)record;
		if(sidSequence!=null) {
			//org.setSid((int)getDatabaseService().getSequenceValue(sidSequence));
		}
		//重新设置班级优先级和班级名称
		if(org instanceof SchoolClass) {
			resetSchoolClass((SchoolClass)org);
		}
		org.setLastModified(DateTimeUtils.now());
		super.save(record);
		//更新组成部分
		updateOrgComponents(org);
		//复制组织机构
		userReplicateServiceList.registOrg(org);
		//同步组织机构
		userSynchClientList.saveOrg(org, true);
		return org;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		Org org = (Org)record;
		org.setLastModified(DateTimeUtils.now());
		//重新设置班级优先级和班级名称
		if(org instanceof SchoolClass) {
			resetSchoolClass((SchoolClass)org);
		}
		//更新组成部分
		updateOrgComponents(org);
		org = (Org)super.update(record);
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
		//复制组织机构
		userReplicateServiceList.modifyOrg(org);
		//同步步组织机构
		userSynchClientList.saveOrg(org, false);
		//清除缓存的模板
		templateService.clearCachedTemplate();

		//清除记录缓存
		try {
			recordCache.clear();
		}
		catch(Exception e) {

		}
		return org;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		Org org = (Org)record;
		//复制组织机构
		userReplicateServiceList.deleteOrg(org.getId(), org.getParentDirectoryId());
		//同步删除
		userSynchClientList.deleteOrg(org);
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
		try {
			recordCache.clear();
		}
		catch(Exception e) {
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectory(long)
	 */
	public Directory getDirectory(long directoryId) throws ServiceException {
		try {
			Directory directory = (Directory)recordCache.get("org:" + directoryId);
			if(directory!=null) {
				return directory;
			}
			directory = (Directory)super.getDirectory(directoryId);
			if(directory!=null) {
				recordCache.put("org:" + directoryId, directory);
			}
			return directory;
		}
		catch(CacheException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getOrg(long)
	 */
	public Org getOrg(long orgId) throws ServiceException {
		return (Org)getDirectory(orgId);
	}
	
	/**
	 * 更新组成部分
	 * @param org
	 * @throws ServiceException
	 */
	private void updateOrgComponents(Org org) throws ServiceException {
		//保存部门分管领导
		updateOrgSupervisors(org.getId(), org.getDirectoryName(), org.getSupervisorIds(), org.getSupervisorNames());
		//保存部门领导
		updateOrgLeaders(org.getId(), org.getDirectoryName(), org.getLeaderIds(), org.getLeaderNames());
	}

	/**
	 * 更新部门主管领导
	 * @param orgId
	 * @param orgName
	 * @param supervisorIds
	 * @param supervisorNames
	 * @throws ServiceException
	 */
	private void updateOrgSupervisors(long orgId, String orgName, String supervisorIds, String supervisorNames) throws ServiceException {
		if(supervisorIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from OrgSupervisor OrgSupervisor where OrgSupervisor.orgId=" + orgId);
		if(supervisorIds.equals("")) {
			return;
		}
		String[] ids = supervisorIds.split(",");
		String[] names = supervisorNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OrgSupervisor orgSupervisor = new OrgSupervisor();
			orgSupervisor.setId(UUIDLongGenerator.generateId()); //ID
			orgSupervisor.setOrgId(orgId); //组织机构ID
			orgSupervisor.setOrgName(orgName); //组织机构名称
			orgSupervisor.setSupervisorId(Long.parseLong(ids[i])); //分管领导ID
			orgSupervisor.setSupervisor(names[i]); //分管领导姓名
			getDatabaseService().saveRecord(orgSupervisor);
		}
	}
	
	/**
	 * 更新部门领导
	 * @param orgId
	 * @param orgName
	 * @param leaderIds
	 * @param leaderNames
	 * @throws ServiceException
	 */
	private void updateOrgLeaders(long orgId, String orgName, String leaderIds, String leaderNames) throws ServiceException {
		if(leaderIds==null) {
			return;
		}
		getDatabaseService().deleteRecordsByHql("from OrgLeader OrgLeader where OrgLeader.orgId=" + orgId);
		if(leaderIds.equals("")) {
			return;
		}
		String[] ids = leaderIds.split(",");
		String[] names = leaderNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OrgLeader orgLeader = new OrgLeader();
			orgLeader.setId(UUIDLongGenerator.generateId()); //ID
			orgLeader.setOrgId(orgId); //组织机构ID
			orgLeader.setOrgName(orgName); //组织机构名称
			orgLeader.setLeaderId(Long.parseLong(ids[i])); //领导ID
			orgLeader.setLeader(names[i]); //领导姓名
			getDatabaseService().saveRecord(orgLeader);
		}
	}
	
	/**
	 * 重新设置班级优先级和班级名称
	 * @param schoolClass
	 * @throws ServiceException
	 */
	private void resetSchoolClass(SchoolClass schoolClass) throws ServiceException {
		String className = schoolClass.getDirectoryName().replaceAll("\\x28", "（").replaceAll("\\x29", "）"); 
		schoolClass.setDirectoryName(className);
		int classType = 0;
		if(className.startsWith("初") || className.startsWith("七年") || className.startsWith("八年") || className.startsWith("九年")) {
			classType = 1;
		}
		else if(className.startsWith("高")) {
			classType = 2;
		}
		String hql = "select SchoolClass.priority" +
					" from SchoolClass SchoolClass" +
					" where SchoolClass.parentDirectoryId=" + schoolClass.getParentDirectoryId();
		//获取上一个班级的优先级
		String hqlPrev = hql + " and ((SchoolClass.enrollTime=" + schoolClass.getEnrollTime() + //条件1:入学年度相同,同样是初中或高中,班级编号小
						 (classType==0 ? "" : " and " + (classType==1 ? "not " : "") + "SchoolClass.directoryName like '高%'") + 
						 " and SchoolClass.classNumber<" + schoolClass.getClassNumber() + ")" +
						 " or (SchoolClass.enrollTime>" + schoolClass.getEnrollTime() + //条件2:入学年度大(年级低),同样是初中或高中
						 (classType==0 ? "" : " and " + (classType==1 ? "not " : "") + "SchoolClass.directoryName like '高%'") + ")" + 
						 (classType!=2 ? "" : " or (not SchoolClass.directoryName like '高%')") + ")" + //条件3:如果是高中,只要是初中就可以
						 " order by SchoolClass.priority";
		Number priorityPrev = (Number)getDatabaseService().findRecordByHql(hqlPrev);
		//获取后一个班级的优先级
		String hqlNext = hql + " and ((SchoolClass.enrollTime=" + schoolClass.getEnrollTime() + //条件1:入学年度相同,同样是初中或高中,班级编号大
						 (classType==0 ? "" : " and " + (classType==1 ? "not " : "") + "SchoolClass.directoryName like '高%'") + 
						 " and SchoolClass.classNumber>" + schoolClass.getClassNumber() + ")" +
						 " or (SchoolClass.enrollTime<" + schoolClass.getEnrollTime() + //条件2:入学年度小(年级高),同样是初中或高中
						 (classType==0 ? "" : " and " + (classType==1 ? "not " : "") + "SchoolClass.directoryName like '高%'") + ")" + 
						 (classType!=1 ? "" : " or SchoolClass.directoryName like '高%'") + ")" + //条件3:如果是初中,只要是高中就可以
						 " order by SchoolClass.priority DESC";
		Number priorityNext = (Number)getDatabaseService().findRecordByHql(hqlNext);
		if(priorityPrev!=null) { 
			if(priorityNext!=null) {
				schoolClass.setPriority(priorityPrev.floatValue() + (priorityNext.floatValue()-priorityPrev.floatValue())/2);
			}
			else {
				schoolClass.setPriority(priorityPrev.floatValue() - 1);
			}
		}
		else if(priorityNext!=null) {
			schoolClass.setPriority(priorityNext.floatValue() + 1);
		}
		else {
			schoolClass.setPriority(100);
		}
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#addClassTeacher(long, long, java.lang.String)
	 */
	public void addClassTeacher(long classId, long teacherId, String teacherTitle) throws ServiceException {
		//查找是否添加过
		if(getDatabaseService().findRecordByHql("from ClassTeacher ClassTeacher where ClassTeacher.classId=" + classId + " and ClassTeacher.teacherId=" + teacherId + " and ClassTeacher.title='" + JdbcUtils.resetQuot(teacherTitle) + "'")!=null) {
			return;
		}
		ClassTeacher classTeacher = new ClassTeacher();
		classTeacher.setId(UUIDLongGenerator.generateId());
		classTeacher.setClassId(classId); //班级ID
		classTeacher.setTeacherId(teacherId); //教师ID
		classTeacher.setTitle(teacherTitle); //教师称谓
		getDatabaseService().saveRecord(classTeacher);
		//同步班级老师
		userSynchClientList.saveClassTeacher(classTeacher);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#deleteClassTeachers(java.lang.String[])
	 */
	public void deleteClassTeachers(String[] classTeacherIds) throws ServiceException {
		if(classTeacherIds==null || classTeacherIds.length==0) {
			return;
		}
		String ids = classTeacherIds[0];
		for(int i=1; i<classTeacherIds.length; i++) {
			ids += "," + classTeacherIds[i];
		}
		List classTeachers = getDatabaseService().findRecordsByHql("from ClassTeacher ClassTeacher where ClassTeacher.id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")");
		if(classTeachers==null || classTeachers.isEmpty()) {
			return;
		}
		for(Iterator iterator=classTeachers.iterator();iterator.hasNext();) {
			ClassTeacher classTeacher = (ClassTeacher)iterator.next();
			getDatabaseService().deleteRecord(classTeacher);
			userSynchClientList.deleteClassTeacher(classTeacher);  //同步删除
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#addSchool(java.lang.String, java.lang.String, long, long, java.lang.String, java.lang.String)
	 */
	public School addSchool(String name, String schoolCategory, long parentOrgId, long creatorId, String creatorName, String remark) throws ServiceException {
		School school = (School)createDirectory(-1, parentOrgId, name, "school", remark, creatorId, creatorName);
		school.setCategory(schoolCategory);
		getDatabaseService().updateRecord(school);
		return school;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#addClass(java.lang.String, int, int, int, long, long, java.lang.String, java.lang.String)
	 */
	public SchoolClass addClass(String name, int classNumber, int enrollTime, int lengthOfSchooling, long parentOrgId, long creatorId, String creatorName, String remark) throws ServiceException {
		SchoolClass schoolClass = (SchoolClass)createDirectory(-1, parentOrgId, name, "schoolClass", remark, creatorId, creatorName);
		schoolClass.setClassNumber(classNumber); //班级编号
		schoolClass.setEnrollTime(enrollTime); //入学年度
		schoolClass.setLengthOfSchooling(lengthOfSchooling); //学制
		getDatabaseService().updateRecord(schoolClass);
		return schoolClass;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#addSchoolDepartment(java.lang.String, long, long, java.lang.String)
	 */
	public SchoolDepartment addSchoolDepartment(String name, long parentOrgId, long creatorId, String creatorName) throws ServiceException {
		SchoolDepartment department = (SchoolDepartment)createDirectory(-1, parentOrgId, name, "schoolDepartment", null, creatorId, creatorName);
		return department;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		List errors = new ArrayList();
		//检查在父机构下是否有相同的部门名称
		if(isOrgExist((Org)record)) {
			errors.add(((Org)record).getDirectoryName() + "已经被注册过");
		}
		if(record instanceof Unit) { //单位
			Unit unit = (Unit)record;
			if(unit.getUnitCode()!=null && !unit.getUnitCode().isEmpty()) {
				//检查单位编码是否被使用过
				String hql = "select Unit.id" +
							 " from Unit Unit" +
							 " where Unit.unitCode='" + JdbcUtils.resetQuot(unit.getUnitCode()) + "'" +
							 " and Unit.id!=" + unit.getId();
				if(getDatabaseService().findRecordByHql(hql)!=null) {
					errors.add("单位编码" + unit.getUnitCode() + "已经被登记");
				}
			}
		}
		return errors.isEmpty() ? null : errors;
	}
	
	/**
	 * 检查部门是否已经存在
	 * @param org
	 * @return
	 * @throws ServiceException
	 */
	private boolean isOrgExist(Org org) throws ServiceException {
		String hql = "select Org.id from Org Org" +
		 			 " where Org.parentDirectoryId=" + org.getParentDirectoryId() +
		 			 " and Org.directoryName='" + JdbcUtils.resetQuot(org.getDirectoryName()) + "'";
		Long id = (Long)getDatabaseService().findRecordByHql(hql);
		return (id!=null && org.getId()!=id.longValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#upgrade()
	 */
	public void upgrade() throws ServiceException {
		if(!distributionService.isMasterServer(true)) { //不是主服务器,不需要执行,避免冲突
			return;
		}
		Logger.info("*********************** school class upgrade ***********************");
		//获取所有的班级列表
		List schoolClasses = getDatabaseService().findRecordsByHql("from SchoolClass SchoolClass");
		if(schoolClasses==null) {
			return;
		}
		int currentYear = DateTimeUtils.getYear(DateTimeUtils.date());
		String[][] gradeNames = {{"一年", "二年", "三年", "四年", "五年", "六年"}, {"七年", "八年", "九年"}, {"初一", "初二", "初三"}, {"高一", "高二", "高三"}};
		for(Iterator iterator = schoolClasses.iterator(); iterator.hasNext();) {
			SchoolClass schoolClass = (SchoolClass)iterator.next();
			String className = schoolClass.getDirectoryName();
			boolean found = false;
			for(int i=0; i<gradeNames.length && !found; i++) {
				found = false;
				for(int j=0; j<gradeNames[i].length; j++) {
					if(className.startsWith(gradeNames[i][j])) {
						//检查入学年度
						if(j==schoolClass.getLengthOfSchooling()-1) { //最后一个年级
							if(currentYear-schoolClass.getEnrollTime()>schoolClass.getLengthOfSchooling()) { //已经毕业超过1年
								//TODO: 删除没有学生的班级
								
							}
							else if(currentYear-schoolClass.getEnrollTime()-j==1) { //今年毕业的班级
								//修改班级名称为六年（1）班（2007届）
								if(className.indexOf("届")==-1) {
									schoolClass.setDirectoryName(className + "（" + currentYear + "届）");
								}
								schoolClass.setIsGraduated('1'); //设置为以毕业
								update(schoolClass);
							}
						}
						else if(currentYear-schoolClass.getEnrollTime()-j==1) { //当前年级与实际年级差1年的班级
							//升级
							schoolClass.setDirectoryName(gradeNames[i][j+1] + "（" + schoolClass.getClassNumber() + "）班");
							update(schoolClass);
						}
						found = true;
						break;
					}
				}
			}
		}
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#synchAllOrgs()
	 */
	public void synchAllOrgs() throws ServiceException {
		final int BATCH_SIZE = 100; //每次处理100个用户
		for(int i=0; ; i+=BATCH_SIZE) {
			List personList = getDatabaseService().findRecordsByHql("from UserDirectory UserDirectory order by UserDirectory.type, UserDirectory.id", i, BATCH_SIZE);
			if(personList==null || personList.isEmpty()) {
				return;
			}
			for(Iterator iterator = personList.iterator(); iterator.hasNext(); ) {
				Org org = (Org)iterator.next();
				try {
					newUserSynchClientList.saveOrg(org, true);
				}
				catch(Exception e) {
					Logger.error("UserDirectory " + org.getDirectoryName() + " synch failed.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#synchAllClassTeachers()
	 */
	public void synchAllClassTeachers() throws ServiceException {
		final int BATCH_SIZE = 100; //每次处理100个用户
		for(int i=0; ; i+=BATCH_SIZE) {
			String hql = "from ClassTeacher ClassTeacher order by ClassTeacher.id"; 
			List classTeacherList = getDatabaseService().findRecordsByHql(hql, i, BATCH_SIZE);
			if(classTeacherList==null || classTeacherList.isEmpty()) {
				return;
			}
			for(Iterator iterator = classTeacherList.iterator(); iterator.hasNext(); ) {
				ClassTeacher classTeacher = (ClassTeacher)iterator.next();
				try {
					newUserSynchClientList.saveClassTeacher(classTeacher);
				}
				catch(Exception e) {
					Logger.error("ClassTeacher " + classTeacher.getId() + " synch failed.");
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#synchAllOrgManagers()
	 */
	public void synchAllOrgManagers() throws ServiceException {
		final int BATCH_SIZE = 100; //每次处理100个用户
		for(int i=0; ; i+=BATCH_SIZE) {
			List orgs = getDatabaseService().findRecordsByHql("from Org Org order by Org.id", ListUtils.generateList("directoryPopedoms", ","), i, BATCH_SIZE);
			if(orgs==null || orgs.isEmpty()) {
				return;
			}
			for(Iterator iterator = orgs.iterator(); iterator.hasNext(); ) {
				Org org = (Org)iterator.next();
				//获取组织机构管理员
				List popedomUsers = org.getMyVisitors("manager");
				List managers = personService.listPersons(ListUtils.join(popedomUsers, "userId", ",", false));
				if(managers==null || managers.isEmpty()) {
					continue;
				}
				for(Iterator iteratorManager = managers.iterator(); iteratorManager.hasNext();) {
					Person manager = (Person)iteratorManager.next();
					if(manager==null) {
						continue;
					}
					try { //同步组织机构管理员
						newUserSynchClientList.saveOrgManager(org, manager);
					}
					catch(Exception e) {
						Logger.error("synchAllOrgManagers: org " + org.getDirectoryName() + " manager: " + manager.getLoginName() + " synch failed.");
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listCurriculums()
	 */
	public List listCurriculums(boolean fullCurriculum) throws ServiceException {
		List list = new ArrayList();
		list.addAll(fullCurriculum ? fullCurriculums : curriculums);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listCurriculumsByStage(java.lang.String)
	 */
	public List listCurriculumsByStage(String stage) throws ServiceException {
		List list = new ArrayList();
		list.addAll(getStudyStage(stage).getCurriculums());
		return list;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listGrades()
	 */
	public List listGrades() throws ServiceException {
		List list = new ArrayList();
		list.addAll(grades);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listGradesByStage(java.lang.String)
	 */
	public List listGradesByStage(String stage) throws ServiceException {
		List list = new ArrayList();
		list.addAll(getStudyStage(stage).getGrages());
		return list;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listSchoolTypes()
	 */
	public List listSchoolTypes() throws ServiceException {
		List list = new ArrayList();
		list.addAll(schoolTypes);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.base.service.OptionService#listStudyStages()
	 */
	public List listStudyStages() throws ServiceException {
		List list = new ArrayList();
		list.addAll(studyStages);
		return list;
	}

	/**
	 * 获取学习阶段 
	 * @param stage
	 * @return
	 */
	private StudyStage getStudyStage(String stage) {
		return (StudyStage)ListUtils.findObjectByProperty(studyStages, "stageName", stage);
	}
	
	/**
	 * @param curriculums the curriculums to set
	 */
	public void setCurriculums(String curriculums) {
		this.curriculums = new ArrayList();
		this.fullCurriculums = new ArrayList();
		String[] values = curriculums.split(";");
		for(int i=0; i<values.length; i++) {
			String[] curriculumValues = values[i].split(",");
			//获取学习阶段
			String stageName = curriculumValues[0].trim();
			StudyStage studyStage = getStudyStage(stageName);
			studyStage.setCurriculums(new ArrayList());
			for(int j=1; j<curriculumValues.length; j++) {
				String value = curriculumValues[j].trim();
				studyStage.getCurriculums().add(value); //添加到学习阶段的课程列表
				if(this.curriculums.indexOf(value)==-1) {
					this.curriculums.add(value);
				}
				this.fullCurriculums.add(stageName + value);
			}
		}
	}

	/**
	 * @param grades the grades to set
	 */
	public void setGrades(String grades) {
		this.grades = new ArrayList();
		String[] values = grades.split(";");
		for(int i=0; i<values.length; i++) {
			String[] gradeValues = values[i].split(",");
			//获取学习阶段
			String stageName = gradeValues[0].trim();
			StudyStage studyStage = getStudyStage(stageName);
			studyStage.setGrages(new ArrayList());
			for(int j=1; j<gradeValues.length; j++) {
				String value = gradeValues[j].trim();
				studyStage.getGrages().add(value); //添加到学习阶段的年级列表
				this.grades.add(value);
			}
		}
	}

	/**
	 * @param schoolTypes the schoolTypes to set
	 */
	public void setSchoolTypes(String schoolTypes) {
		this.schoolTypes = ListUtils.generateList(schoolTypes, ",");
	}

	/**
	 * @param studyStages the studyStages to set
	 */
	public void setStudyStages(String studyStages) {
		this.studyStageNames = ListUtils.generateList(studyStages, ",");
		this.studyStages = new ArrayList();
		for(Iterator iterator = this.studyStageNames.iterator(); iterator.hasNext();) {
			StudyStage studyStage = new StudyStage();
			studyStage.setStageName((String)iterator.next());
			this.studyStages.add(studyStage);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getAreaOfOrg(long)
	 */
	public Org getAreaOfOrg(long orgId) throws ServiceException {
		String hql = "select Org " +
					 " from Org Org, OrgSubjection OrgSubjection" +
					 " where Org.directoryType='area'" +
					 " and Org.id=OrgSubjection.parentDirectoryId" +
					 " and OrgSubjection.directoryId=" + orgId + 
					 " order by OrgSubjection.id";
		return (Org)getDatabaseService().findRecordByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getParentUnitOrSchool(long)
	 */
	public Org getParentUnitOrSchool(long orgId) throws ServiceException {
		//从用户所在机构的上级机构中查询
		String hql = "select Org" +
					 " from Org Org, OrgSubjection OrgSubjection" +
					 " where OrgSubjection.directoryId=" + orgId +
					 " and Org.id=OrgSubjection.parentDirectoryId" +
					 " and (Org.directoryType='school' or Org.directoryType='unit')" +
					 " order by OrgSubjection.id";
		Org unit = (Org)getDatabaseService().findRecordByHql(hql);
		return unit==null ? getOrg(0) : unit;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getPersonalUnitOrSchool(long)
	 */
	public Org getPersonalUnitOrSchool(long personId) throws ServiceException {
		//获取用户所在机构
		Org org = getPersonalDepartment(personId);
		if(org==null || org.getDirectoryType().equals("school") || org.getDirectoryType().equals("unit") || org.getDirectoryType().equals("root")) { //用户被注册在单位或学校
			return org;
		}
		//从用户所在机构的上级机构中查询
		String hql = "select Org" +
			  		 " from Org Org, OrgSubjection OrgSubjection" +
					 " where OrgSubjection.directoryId=" + org.getId() +
					 " and Org.id=OrgSubjection.parentDirectoryId" +
					 " and (Org.directoryType='school' or Org.directoryType='unit')" +
					 " order by OrgSubjection.id";
		org = (Org)getDatabaseService().findRecordByHql(hql);
		return org==null ? getOrg(0) : org;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getPersonalDepartment(long)
	 */
	public Org getPersonalDepartment(long personId) throws ServiceException {
		String hql = "select Org" +
					" from Org Org, PersonSubjection PersonSubjection" +
					" where PersonSubjection.personId=" + personId +
					" and Org.id=PersonSubjection.orgId" +
					" order by PersonSubjection.id";
		return (Org)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#isMemberOfOrg(long, long)
	 */
	public boolean isMemberOfOrg(long personId, long orgId) throws ServiceException {
		String hql = "select Person.id" +
					 " from Person Person left join Person.subjections PersonSubjection, OrgSubjection OrgSubjection" +
					 " where PersonSubjection.orgId=OrgSubjection.directoryId" +
					 " and OrgSubjection.parentDirectoryId=" + orgId +
					 " and Person.id=" + personId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#isOrgManager(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isOrgManager(long orgId, SessionInfo sessionInfo) throws ServiceException {
		return checkPopedom(orgId, "manager", sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#isTeacherInCharge(long, long)
	 */
	public boolean isTeacherInCharge(long teacherId, long classId) throws ServiceException {
		String hql = "select ClassTeacher.id" +
					 " from ClassTeacher ClassTeacher" +
					 " where ClassTeacher.teacherId=" + teacherId +
					 " and ClassTeacher.classId=" + classId +
					 " and ClassTeacher.title='班主任'";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listClassesInCharge(long)
	 */
	public List listClassesInCharge(long teacherId) throws ServiceException {
		String hql = "select distinct SchoolClass" +
					 " from SchoolClass SchoolClass, ClassTeacher ClassTeacher" +
					 " where SchoolClass.id=ClassTeacher.classId" +
					 " and ClassTeacher.teacherId=" + teacherId +
					 " and ClassTeacher.title='班主任'";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listClassStudents(long)
	 */
	public List listClassStudents(long classId) throws ServiceException {
		//获取组织机构中的人员列表
		String hql = "select Student" +
					 " from Student Student left join Student.subjections PersonSubjection" +
					 " where PersonSubjection.orgId=" +  classId +
					 " order by Student.seatNumber";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listClassTeachers(long)
	 */
	public List listClassTeachers(long classId) throws ServiceException {
		return getDatabaseService().findRecordsByHql("from ClassTeacher ClassTeacher where ClassTeacher.classId=" + classId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listManagedSchools(long)
	 */
	public List listManagedSchools(long personId) throws ServiceException {
		if(personId<=0) {
			return null;
		}
		String hql = "select distinct School" +
					 " from School School left join School.directoryPopedoms OrgPopedom" +
					 " and OrgPopedom.userId=" + personId +
					 " and OrgPopedom.popedomName='manager'";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listOrgIdsOfPerson(long, boolean)
	 */
	public String listOrgIdsOfPerson(String personIds, boolean containsParentOrg) throws ServiceException {
		//获取用户所直属的机构ID列表
		String hql = "select PersonSubjection.orgId" +
					 " from PersonSubjection PersonSubjection" +
					 " where PersonSubjection.personId in (" + JdbcUtils.validateInClauseNumbers(personIds) + ")" +
					 " order by PersonSubjection.id";
		List orgIds = getDatabaseService().findRecordsByHql(hql);
		if(orgIds==null || orgIds.equals("")) {
			return null;
		}
		if(containsParentOrg) {
			//从用户所在机构的上级机构中查询
			String ids = ListUtils.join(orgIds, ",", false);
			hql = "select OrgSubjection.parentDirectoryId" +
				  " from OrgSubjection OrgSubjection" +
				  " where OrgSubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
				  " and OrgSubjection.parentDirectoryId not in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
				  " order by OrgSubjection.id";
			List parentOrgIds = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = parentOrgIds==null ? null : parentOrgIds.iterator(); iterator!=null && iterator.hasNext();) {
				Object parentOrgId = iterator.next();
				if(orgIds.indexOf(parentOrgId)==-1) { //检查之前是否已经存在
					orgIds.add(parentOrgId);
				}
			}
		}
		return ListUtils.join(orgIds, ",", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listOrgPersons(java.lang.String, java.lang.String, boolean, int, int)
	 */
	public List listOrgPersons(String orgIds, String personTypes, boolean containChildOrg, boolean readLazyLoadProperties, int first, int max) throws ServiceException {
		//获取组织机构中的人员列表
		String hql;
		if(("," + orgIds + ",").indexOf(",0,")!=-1 && containChildOrg) {
			hql = "from Person Person" +
				  " where Person.preassign!='1'";
		}
		else { 
			hql = "select distinct Person " +
				  " from Person Person, PersonSubjection PersonSubjection" + (containChildOrg ? ", OrgSubjection OrgSubjection" : "") +
				  " where Person.id=PersonSubjection.personId" +
				  " and Person.preassign!='1'" +
				  " and " + (!containChildOrg ? "PersonSubjection.orgId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + ")" : "PersonSubjection.orgId=OrgSubjection.directoryId and OrgSubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + ")");
		}
		//用户类型,teacher/student/employee/genearch,可以是多个,以逗号为分隔符,默认为teacher,student,employee
		if(personTypes==null) {
			hql += " and Person.type!=" + PersonService.PERSON_TYPE_GENEARCH;
		}
		else if(!personTypes.equals("all")) {
			hql += " and Person.type in (" + JdbcUtils.validateInClauseNumbers(personTypes.replaceFirst("teacher", "" + PersonService.PERSON_TYPE_TEACHER).replaceFirst("student", "" + PersonService.PERSON_TYPE_STUDENT).replaceFirst("employee", "" + PersonService.PERSON_TYPE_EMPLOYEE).replaceFirst("genearch", "" + PersonService.PERSON_TYPE_GENEARCH)) + ")";
		}
		hql += " order by Person.priority DESC, Person.name";
		return getDatabaseService().findRecordsByHql(hql, readLazyLoadProperties ? listLazyLoadProperties(Person.class) : null, first, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listOrgs(java.lang.String)
	 */
	public List listOrgs(String orgIds) throws ServiceException {
		if(orgIds==null || orgIds.isEmpty()) {
			return null;
		}
		List orgs = getDatabaseService().findRecordsByHql("from Org Org where Org.id in (" + JdbcUtils.validateInClauseNumbers(orgIds) + ")", 0, 0);
		return ListUtils.sortByProperty(orgs, "id", orgIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listOrgsOfPerson(long, boolean)
	 */
	public List listOrgsOfPerson(String personIds, boolean containsParentOrg) throws ServiceException {
		//获取用户所属的机构ID列表
		String ids = listOrgIdsOfPerson(personIds, containsParentOrg);
		List orgs = getDatabaseService().findRecordsByHql("from Org Org where Org.id in(" + ids + ")");
		return ListUtils.sortByProperty(orgs, "id", ids);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listParentAreasOfOrg(long)
	 */
	public List listParentAreasOfOrg(long orgId) throws ServiceException {
		String hql = "select Area.directoryName" + 
					 " from Area Area, OrgSubjection OrgSubjection" +
					 " where Area.id=OrgSubjection.parentDirectoryId" +
					 " and OrgSubjection.directoryId=" + orgId +
					 " order by OrgSubjection.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listParentOrgIds(long)
	 */
	public String listParentOrgIds(long orgId) throws ServiceException {
		String hql = "select OrgSubjection.parentDirectoryId" +
					 " from OrgSubjection OrgSubjection" +
					 " where OrgSubjection.directoryId=" + orgId +
					 " and OrgSubjection.parentDirectoryId!=" + orgId +
					 " order by OrgSubjection.id";
		return ListUtils.join(getDatabaseService().findRecordsByHql(hql), ",", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listPersonsOfPersonalOrg(long, java.lang.String, boolean, int)
	 */
	public List listPersonsOfPersonalOrg(String personIds, String personTypes, boolean containChildOrg, int max) throws ServiceException {
		String hql = "select distinct PersonSubjection.orgId" +
					 " from PersonSubjection PersonSubjection" +
					 " where PersonSubjection.personId in (" + JdbcUtils.validateInClauseNumbers(personIds) + ")";
		String orgIds = ListUtils.join(getDatabaseService().findRecordsByHql(hql), ",", false);
		//获取组织机构中的人员列表
		hql = "select Person " +
			  " from Person Person, PersonSubjection PersonSubjection" + (containChildOrg ? ", OrgSubjection OrgSubjection" : "") +
			  " where Person.id=PersonSubjection.personId" +
			  " and Person.preassign!='1'" +
			  " and " + (!containChildOrg ? "PersonSubjection.orgId in (" +  JdbcUtils.validateInClauseNumbers(orgIds) + ")" : "PersonSubjection.orgId=OrgSubjection.directoryId and OrgSubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + ")");
		//用户类型,teacher/student/employee/genearch,可以是多个,以逗号为分隔符,默认为teacher,student,employee
		if(personTypes==null) {
			hql += " and Person.type!=" + PersonService.PERSON_TYPE_GENEARCH;
		}
		else if(!personTypes.equals("all")) {
			hql += " and Person.type in (" + JdbcUtils.validateInClauseNumbers(personTypes.replaceFirst("teacher", "" + PersonService.PERSON_TYPE_TEACHER).replaceFirst("student", "" + PersonService.PERSON_TYPE_STUDENT).replaceFirst("employee", "" + PersonService.PERSON_TYPE_EMPLOYEE).replaceFirst("genearch", "" + PersonService.PERSON_TYPE_GENEARCH)) + ")";
		}
		hql += " order by Person.priority DESC, Person.name";
		return getDatabaseService().findRecordsByHql(hql, 0, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#listStudentsWithGenearchs(long)
	 */
	public List listStudentsWithGenearchs(long classId) throws ServiceException {
		String hql = "select Student " +
					 " from Student Student left join Student.subjections PersonSubjection" +
					 " where PersonSubjection.orgId=" +  classId;
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("genearches"));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getOrgTypes()
	 */
	public String getOrgTypes() throws ServiceException {
		DirectoryType[] directoryTypes = getDirectoryTypes();
		String orgTypes = null;
		for(int i=0; i<directoryTypes.length; i++) {
			orgTypes = (orgTypes==null ? "" : orgTypes + ",") + directoryTypes[i].getType();
		}
		return orgTypes;
	}
	
	/**
	 * 创建一个地区
	 * @param name
	 * @param parentOrgId
	 * @param creatorId
	 * @param creatorName
	 * @param remark
	 * @return
	 * @throws ServiceException
	 */
	public Area addArea(String name, long parentOrgId, long creatorId, String creatorName, String remark) throws ServiceException {
		Area area = (Area)createDirectory(-1, parentOrgId, name, "area", remark, creatorId, creatorName);
		return area;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getAreaByNetAddress(java.lang.String, boolean)
	 */
	public Area getAreaByNetAddress(String netAddress, boolean createWhenNotExists) throws ServiceException {
		Location location = gpsService.getLocationByIP(netAddress, true, false);
		if(location==null) {
			return null;
		}
		String placeName = location.getPlaceName();
		int index = placeName.indexOf("省");
		if(index!=-1) {
			placeName = placeName.substring(index + 1);
		}
		else if((index=placeName.indexOf("自治区"))!=-1) {
			placeName = placeName.substring(index + 3);
		}
		placeName = placeName.replaceAll("市", "").replaceAll("县", "");
		String hql = "from Area Area where Area.directoryName='" + JdbcUtils.resetQuot(placeName) + "'";
		Area area = (Area)getDatabaseService().findRecordByHql(hql);
		if(area!=null || !createWhenNotExists) {
			return area;
		}
		return addArea(placeName, 0, 0, "系统", "系统自动创建");
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#importPlaceName(boolean)
	 */
	public void importPlaceName(boolean global) throws ServiceException {
		OrgService orgService = (OrgService)Environment.getService("orgService"); 
		FileInputStream fileInputStream = null;
		try {
			//解析LocList.xml
			Element qqLocation = new XmlParser().parseXmlFile(Environment.getWebinfPath() + "jeaf/usermanage/placename/LocList.xml"); //QQ LocList.xml
			if(!global) { //不是全球
				//在QQ地名中查找国家
				for(Iterator iterator = qqLocation.elementIterator(); iterator.hasNext();) {
					Element countryElement = (Element)iterator.next();
					if("中国".equals(countryElement.attributeValue("Name"))) {
						importPlaceNameFromXML(0, countryElement, orgService);
						break;
					}
				}
				return;
			}
			//解析电子表格,获取国家名和所在洲名
			fileInputStream = new FileInputStream(Environment.getWebinfPath() + "jeaf/usermanage/placename/世界国家名，世界区号，国家所属洲，世界国家快递分区表.xls");
			Workbook book = new HSSFWorkbook(fileInputStream);
			Sheet sheet = book.getSheetAt(0);
			Map continents = new HashMap(); //洲列表 
			for(int i=1; i<sheet.getPhysicalNumberOfRows(); i++) {
				Row row = sheet.getRow(i);
				try {
					String countryEnglishName = ExcelUtils.getCellString(row.getCell(1)); //国家的英文名称
					String countryName = ExcelUtils.getCellString(row.getCell(2)); //国家名称
					String continentEnglishName = ExcelUtils.getCellString(row.getCell(4)); //洲英文名称
					String continentName = ExcelUtils.getCellString(row.getCell(4)); //洲名称
					if(countryName==null || countryName.isEmpty() || continentName==null || continentName.isEmpty()) { //国家名称、洲名称不能为空
						continue;
					}
					Org continent = (Org)continents.get(continentName);
					if(continent==null) {
						continent = (Org)orgService.createDirectory(-1, 0, continentName, "area", continentEnglishName, 0, "系统"); //添加洲
					}
					//创建国家
					Org country = (Org)orgService.createDirectory(-1, continent.getId(), countryName, "area", countryEnglishName, 0, "系统"); //添加国家
					//在QQ地名中查找国家
					for(Iterator iterator = qqLocation.elementIterator(); iterator.hasNext();) {
						Element countryElement = (Element)iterator.next();
						if(countryName.equals(countryElement.attributeValue("Name"))) {
							importPlaceNameFromXML(country.getId(), countryElement, orgService);
							break;
						}
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		catch (Exception e) {
			throw new ServiceException(e);
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
	 * 递归:从XML导入地名
	 * @param parentOrgId
	 * @param parentElement
	 * @param orgService
	 * @throws ServiceException
	 */
	private void importPlaceNameFromXML(long parentOrgId, Element parentElement, OrgService orgService) throws ServiceException {
		for(Iterator iterator = parentElement.elementIterator(); iterator!=null && iterator.hasNext();) {
			Element element = (Element)iterator.next();
			String name = element.attributeValue("Name");
			if(name==null || name.isEmpty()) {
				importPlaceNameFromXML(parentOrgId, element, orgService); //递归导入下级
			}
			else {
				Org org = (Org)orgService.createDirectory(-1, parentOrgId, element.attributeValue("Name"), "area", null, 0, "系统");
				importPlaceNameFromXML(org.getId(), element, orgService); //递归导入下级
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getUnitByCode(java.lang.String)
	 */
	public Unit getUnitByCode(String unitCode) throws ServiceException {
		if(unitCode==null || unitCode.isEmpty()) {
			return null;
		}
		String hql = "from Unit Unit where Unit.unitCode='" + unitCode + "'"; 
		return (Unit)getDatabaseService().findRecordByHql(hql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#addOrg(long, long, java.lang.String, java.lang.String, java.lang.String, float, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark) throws ServiceException {
		//获取组织机构
		Org org = getOrg(orgId);
		boolean newOrg = org==null;
		if(newOrg) { //组织机构不存在
			if(linkedOrgId<=0) { //不是目录引用
				org = (Org)createDirectory(orgId, parentOrgId, name, type, null, 0, null);
			}
			else { //目录引用
				OrgLink orgLink = new OrgLink();
				orgLink.setId(orgId); //ID
				orgLink.setDirectoryName(name); //目录名称
				orgLink.setParentDirectoryId(parentOrgId); //上级目录ID
				orgLink.setDirectoryType(type); //目录类型
				orgLink.setCreated(DateTimeUtils.now()); //创建时间
				org = (Org)createDirectory(orgLink);
			}
		}
		if(org instanceof Unit) {
			((Unit)org).setFullName(fullName);
		}
		else if(org instanceof OrgLink) {
			((OrgLink)org).setLinkedDirectoryId(linkedOrgId);
		}
		org.setDirectoryName(name); //目录名称
		org.setParentDirectoryId(parentOrgId); //上级目录ID
		org.setPriority(priority); //优先级
		if(remark!=null) {
			org.setRemark(remark); //备注
		}
		//设置部门领导
		org.setLeaderIds(leaderIds==null ? "" : leaderIds);
		org.setLeaderNames(leaderNames==null ? "" : leaderNames);
		//设置主管领导
		org.setSupervisorIds(supervisorIds==null ? "" : supervisorIds);
		org.setSupervisorNames(supervisorNames==null ? "" : supervisorNames);
		update(org);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.OrgService#getFirstAccessibleOrg(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Org getFirstAccessibleOrg(String orgTypes, String popedomNames, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select Org" +
					 " from OrgPopedom OrgPopedom, Org Org" +
					 " where OrgPopedom.directoryId=Org.id" +
					 " and Org.directoryType in ('" + JdbcUtils.resetQuot(orgTypes).replaceAll(",", "','") + "')" +
					 " and OrgPopedom.popedomName in ('" + JdbcUtils.resetQuot(popedomNames).replace(",", "','") + "')" +
					 " and OrgPopedom.userId in (" + sessionInfo.getUserIds() + ")" +
					 " order by Org.id";
		return (Org)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.formula.service.FormulaSupport#executeFormula(java.lang.String, java.lang.String[], java.lang.String, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Object executeFormula(String formulaName, String[] parameters, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException {
		if(formulaName.equals("isNotOrgManager")) { //是否某类组织机构的管理员
			List directoryIds = listDirectoryIds(ListUtils.join(parameters, ",", false), "manager", false, sessionInfo, 0, 1);
			return new Boolean(directoryIds==null || directoryIds.isEmpty());
		}
		else if(formulaName.equals("noOrgPopedom")) { //是否对组织机构有指定的权限
			List directoryIds = listDirectoryIds(null, ListUtils.join(parameters, ",", false), false, sessionInfo, 0, 1);
			return new Boolean(directoryIds==null || directoryIds.isEmpty());
		}
		else if(formulaName.equals("isNotMyUnitManager")) { //是否本单位管理员
			return new Boolean(!checkPopedom(sessionInfo.getUnitId(), "manager", sessionInfo));
		}
		else if(formulaName.equals("noMyUnitPopedom")) { //是否对本单位有指定的权限
			return new Boolean(!checkPopedom(sessionInfo.getUnitId(),  ListUtils.join(parameters, ",", false), sessionInfo));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("unitLeaders".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select OrgLeader.leader, OrgLeader.leader from OrgLeader OrgLeader where OrgLeader.orgId=" + sessionInfo.getUnitId());
		}
		else if("units".equals(itemsName)) { //单位列表
			List units = listParentDirectories(sessionInfo.getUnitId(), null);
			if(units==null) {
				units = new ArrayList();
			}
			for(int i=units.size()-1; i>=0; i--) {
				Org org = (Org)units.get(i);
				if(org.getId()==0) { //根目录
					units.set(i, new String[]{org.getDirectoryName(), "0"});
				}
				else if(org instanceof Unit) { //单位
					Unit unit = (Unit)org;
					units.set(i, new String[]{unit.getFullName()==null || unit.getFullName().isEmpty() ? unit.getDirectoryName() : unit.getFullName(), "" + + unit.getId()});
				}
				else {
					units.remove(i);
				}
			}
			//添加用户所在单位
			Org myUnit = getOrg(sessionInfo.getUnitId());
			if(myUnit instanceof Unit) {
				Unit unit = (Unit)myUnit;
				units.add(new String[]{unit.getFullName()==null || unit.getFullName().isEmpty() ? unit.getDirectoryName() : unit.getFullName(), "" + + unit.getId()});
			}
			return units;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
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
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
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
	 * @return the schoolSupport
	 */
	public boolean isSchoolSupport() {
		return schoolSupport;
	}

	/**
	 * @param schoolSupport the schoolSupport to set
	 */
	public void setSchoolSupport(boolean schoolSupport) {
		this.schoolSupport = schoolSupport;
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
	 * @return the gpsService
	 */
	public GpsService getGpsService() {
		return gpsService;
	}

	/**
	 * @param gpsService the gpsService to set
	 */
	public void setGpsService(GpsService gpsService) {
		this.gpsService = gpsService;
	}

	/**
	 * @return the recordCache
	 */
	public Cache getRecordCache() {
		return recordCache;
	}

	/**
	 * @param recordCache the recordCache to set
	 */
	public void setRecordCache(Cache recordCache) {
		this.recordCache = recordCache;
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