package com.yuanluesoft.jeaf.stat.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.stat.model.Statistics;
import com.yuanluesoft.jeaf.stat.pojo.AccessStat;
import com.yuanluesoft.jeaf.stat.pojo.DayAccessStat;
import com.yuanluesoft.jeaf.stat.pojo.HourAccessStat;
import com.yuanluesoft.jeaf.stat.pojo.LoginStat;
import com.yuanluesoft.jeaf.stat.pojo.SchoolLoginStat;
import com.yuanluesoft.jeaf.stat.pojo.StudentLoginStat;
import com.yuanluesoft.jeaf.stat.pojo.TodayLoginStat;
import com.yuanluesoft.jeaf.stat.pojo.UserAccessStat;
import com.yuanluesoft.jeaf.stat.service.StatService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;


/**
 * 
 * @author linchuan
 *
 */
public class StatServiceImpl implements StatService {
	private boolean enabled = true;
	private DatabaseService databaseService;
	private OrgService orgService;
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#access(java.lang.String, java.lang.String, long)
	 */
	public long access(String applicationName, String pageName, long recordId) throws ServiceException {
		if(!enabled) {
			return 0;
		}
		//按日统计
		Timestamp now = DateTimeUtils.now();
		//查找日统计记录
		String hql = "from DayAccessStat DayAccessStat" + 
					 " where DayAccessStat.accessDate=DATE(" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ")" +
					 " and DayAccessStat.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
					 " and DayAccessStat.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
					 " and DayAccessStat.recordId=" + recordId;
		DayAccessStat dayAccessStat = (DayAccessStat)databaseService.findRecordByHql(hql);
		if(dayAccessStat==null) {
			dayAccessStat = new DayAccessStat();
			dayAccessStat.setId(UUIDLongGenerator.generateId());
			dayAccessStat.setAccessDate(DateTimeUtils.date()); //时间
			dayAccessStat.setApplicationName(applicationName); //应用名称
			dayAccessStat.setPageName(pageName); //页面名称
			dayAccessStat.setRecordId(recordId); //记录ID
			dayAccessStat.setTimes(1);
			databaseService.saveRecord(dayAccessStat);
		}
		else {
			dayAccessStat.setTimes(dayAccessStat.getTimes() + 1);
			databaseService.updateRecord(dayAccessStat);
		}
		
		//总访问次数
		hql = "from AccessStat AccessStat" + 
			  " where AccessStat.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
			  " and AccessStat.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
			  " and AccessStat.recordId=" + recordId;
		AccessStat accessStat = (AccessStat)databaseService.findRecordByHql(hql);
		if(accessStat==null) {
			accessStat = new AccessStat();
			accessStat.setId(UUIDLongGenerator.generateId());
			accessStat.setApplicationName(applicationName); //应用名称
			accessStat.setPageName(pageName); //页面名称
			accessStat.setRecordId(recordId); //记录ID
			accessStat.setTimes(1);
			databaseService.saveRecord(accessStat);
		}
		else {
			accessStat.setTimes(accessStat.getTimes() + 1);
			databaseService.updateRecord(accessStat);
		}
		//按时段统计
		hql = "from HourAccessStat HourAccessStat" + 
			  " where HourAccessStat.accessDate=DATE(" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ")" +
			  " and HourAccessStat.accessHour=" + DateTimeUtils.getHour(now);
		HourAccessStat hourAccessStat = (HourAccessStat)databaseService.findRecordByHql(hql);
		if(hourAccessStat==null) {
			hourAccessStat = new HourAccessStat();
			hourAccessStat.setId(UUIDLongGenerator.generateId());
			hourAccessStat.setAccessDate(new Date(now.getTime())); //时间
			hourAccessStat.setWeek(DateTimeUtils.getWeek(now)); //星期
			hourAccessStat.setAccessHour(DateTimeUtils.getHour(now)); //小时
			hourAccessStat.setTimes(1);
			databaseService.saveRecord(hourAccessStat);
		}
		else {
			hourAccessStat.setTimes(hourAccessStat.getTimes() + 1);
			databaseService.updateRecord(hourAccessStat);
		}
		return getAccessTimes(applicationName, pageName, recordId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#getAccessTimes(java.lang.String, java.lang.String, long)
	 */
	public long getAccessTimes(String applicationName, String pageName, long recordId) throws ServiceException {
		//总访问次数
		String hql = "select sum(AccessStat.times)" +
					 " from AccessStat AccessStat" + 
					 " where AccessStat.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
					 " and AccessStat.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
					 (recordId==-1 ? "" : " and AccessStat.recordId=" + recordId);
		Number times = (Number)databaseService.findRecordByHql(hql);
		return times==null ? 0 : times.longValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#access(java.lang.String, java.lang.String, long, long, java.lang.String)
	 */
	public void access(String applicationName, String pageName, long recordId, long userId, String userName) throws ServiceException {
		UserAccessStat userAccessStat = new UserAccessStat();
		userAccessStat.setId(UUIDLongGenerator.generateId()); //ID
		userAccessStat.setApplicationName(applicationName); //应用名称
		userAccessStat.setRecordId(recordId); //记录ID
		userAccessStat.setPageName(pageName); //页面名称
		userAccessStat.setUserId(userId); //用户ID
		userAccessStat.setUserName(userName); //用户名
		userAccessStat.setAccessTime(DateTimeUtils.now()); //访问时间
		try {
			databaseService.saveRecord(userAccessStat);
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#listAccessUsers(java.lang.String, java.lang.String, long)
	 */
	public List listAccessUsers(String applicationName, String pageName, long recordId) throws ServiceException {
		String hql = "from UserAccessStat UserAccessStat" +
					 " where UserAccessStat.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
					 " and UserAccessStat.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
					 " and UserAccessStat.recordId=" + recordId +
					 " order by UserAccessStat.accessTime DESC";
		return databaseService.findRecordsByHql(hql, 0, 1000); //最多返回1000条记录
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#login(long)
	 */
	public void login(long personId, String personName, int personType) throws ServiceException {
		if(!enabled) {
			return;
		}
		Date day = DateTimeUtils.date();
		//查找总登录次数记录
		String hql = "from LoginStat LoginStat" + 
					 " where LoginStat.personId=" + personId +
					 " and LoginStat.loginDate=DATE(" + DateTimeUtils.formatDate(day, null) + ")";
		LoginStat loginStat = (LoginStat)databaseService.findRecordByHql(hql);
		if(loginStat!=null) {
			loginStat.setTimes(loginStat.getTimes() + 1);
			loginStat.setLastLogin(DateTimeUtils.now());
			databaseService.updateRecord(loginStat);
		}
		else {
			loginStat = new LoginStat();
			loginStat.setId(UUIDLongGenerator.generateId());
			loginStat.setPersonId(personId); //用户ID
			loginStat.setPersonName(personName); //用户名
			loginStat.setLoginDate(day);
			loginStat.setTimes(1);
			loginStat.setLastLogin(DateTimeUtils.now());
			databaseService.saveRecord(loginStat);
		}
		//判断是否学生或老师
		if(personType!=PersonService.PERSON_TYPE_STUDENT && personType!=PersonService.PERSON_TYPE_TEACHER) {
			return;
		}
		if(personType==PersonService.PERSON_TYPE_STUDENT) { //学生,记录到学生登录统计
			Date today = DateTimeUtils.date();
			hql = "from StudentLoginStat StudentLoginStat" +
				  " where StudentLoginStat.personId=" + personId +
				  " and StudentLoginStat.loginDate=DATE(" + DateTimeUtils.formatDate(today, null) + ")";
			StudentLoginStat studentLoginStat = (StudentLoginStat)databaseService.findRecordByHql(hql);
			if(studentLoginStat==null) {
				studentLoginStat = new StudentLoginStat();
				studentLoginStat.setId(UUIDLongGenerator.generateId()); //ID
				studentLoginStat.setPersonId(personId); //用户ID
				studentLoginStat.setPersonName(personName); //姓名
				studentLoginStat.setTimes(1); //登录次数
				studentLoginStat.setLoginDate(today); //日期
				databaseService.saveRecord(studentLoginStat);
			}
			else {
				studentLoginStat.setTimes(studentLoginStat.getTimes()+1); //登录次数
				databaseService.updateRecord(studentLoginStat);
			}
		}
		//获取所在学校
		Org personalSchool = orgService.getPersonalUnitOrSchool(personId);
		if(personalSchool.getDirectoryType().equals("school")) { //只统计学校
			return;
		}
		//查找当天登录记录
		hql = "select TodayLoginStat.id from TodayLoginStat TodayLoginStat" + 
			  " where TodayLoginStat.personId=" + personId;
		boolean loginOnce = databaseService.findRecordByHql(hql)!=null;
		if(!loginOnce) { //今天未登录过
			//保存登录记录
			TodayLoginStat todayLoginStat = new TodayLoginStat();
			todayLoginStat.setId(UUIDLongGenerator.generateId());
			todayLoginStat.setPersonId(personId);
			databaseService.saveRecord(todayLoginStat);
		}
		//获取学校当天的登录情况记录
		Date date = DateTimeUtils.date();
		hql = "from SchoolLoginStat SchoolLoginStat" +
			  " where SchoolLoginStat.schoolId=" + personalSchool.getId() +
			  " and SchoolLoginStat.loginDay=DATE(" + new SimpleDateFormat("yyyy-MM-dd").format(date) + ")";
		SchoolLoginStat schoolLoginStat = (SchoolLoginStat)databaseService.findRecordByHql(hql);
		if(schoolLoginStat==null) { //没有学校记录
			schoolLoginStat = new SchoolLoginStat();
			schoolLoginStat.setId(UUIDLongGenerator.generateId());
			schoolLoginStat.setSchoolId(personalSchool.getId());
			schoolLoginStat.setSchoolName(personalSchool.getDirectoryName());
			schoolLoginStat.setLoginDay(date);
			if(personType==PersonService.PERSON_TYPE_STUDENT) { //学生
				schoolLoginStat.setStudentNumber(1);
				schoolLoginStat.setStudentTimes(1);
			}
			else { //老师
				schoolLoginStat.setTeacherNumber(1);
				schoolLoginStat.setTeacherTimes(1);
			}
			databaseService.saveRecord(schoolLoginStat);
		}
		else {
			if(personType==PersonService.PERSON_TYPE_STUDENT) { //学生
				if(!loginOnce) { //未登录过,登录人数加一
					schoolLoginStat.setStudentNumber(schoolLoginStat.getStudentNumber() + 1);
				}
				schoolLoginStat.setStudentTimes(schoolLoginStat.getStudentTimes() + 1);
			}
			else { //老师
				if(!loginOnce) { //未登录过,登录人数加一
					schoolLoginStat.setTeacherNumber(schoolLoginStat.getTeacherNumber() + 1);
				}
				schoolLoginStat.setTeacherTimes(schoolLoginStat.getTeacherTimes() + 1);
			}
			databaseService.updateRecord(schoolLoginStat);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#getLoginStat(long)
	 */
	public LoginStat getLoginStat(long personId) throws ServiceException {
		return (LoginStat)databaseService.findRecordByHql("from LoginStat LoginStat where LoginStat.personId=" + personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#getStatistics()
	 */
	public Statistics getStatistics() throws ServiceException {
		Statistics statistics = new Statistics();
		//总访问次数
		String hql = "select sum(AccessStat.times) from AccessStat AccessStat";
		statistics.setTotalAccess(((Number)databaseService.findRecordByHql(hql)).intValue());
		//本月访问次数
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		hql = "select sum(DayAccessStat.times)" +
			  " from DayAccessStat DayAccessStat" +
			  " where DayAccessStat.accessDate>=DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		calendar.add(Calendar.MONTH, 1);
		hql += " and DayAccessStat.accessDate<DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		Number total = (Number)databaseService.findRecordByHql(hql);
		statistics.setCurrentMonthAccess(total==null ? 0 : total.intValue());
		//上月访问量
		calendar.add(Calendar.MONTH, -2);
		hql = "select sum(DayAccessStat.times)" +
			  " from DayAccessStat DayAccessStat" +
			  " where DayAccessStat.accessDate>=DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		calendar.add(Calendar.MONTH, 1);
		hql += " and DayAccessStat.accessDate<DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		total = (Number)databaseService.findRecordByHql(hql);
		statistics.setPreviousMonthAccess(total==null ? 0 : total.intValue());
		//今天访问量
		calendar = Calendar.getInstance();
		hql = "select sum(DayAccessStat.times)" +
			  " from DayAccessStat DayAccessStat" +
			  " where DayAccessStat.accessDate=DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		total = (Number)databaseService.findRecordByHql(hql);
		statistics.setTodayAccess(total==null ? 0 : total.intValue());
		//昨天访问量
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		hql = "select sum(DayAccessStat.times)" +
			  " from DayAccessStat DayAccessStat" +
			  " where DayAccessStat.accessDate=DATE(" + formatter.format(new Date(calendar.getTimeInMillis())) + ")";
		total = (Number)databaseService.findRecordByHql(hql);
		statistics.setYesterdayAccess(total==null ? 0 : total.intValue());
		//平均每天访问量
		hql = "select min(DayAccessStat.accessDate) from DayAccessStat DayAccessStat";
		Date date = (Date)databaseService.findRecordByHql(hql);
		calendar = Calendar.getInstance();
		int totalDays = (int)((calendar.getTimeInMillis() - date.getTime()) / (1000 * 3600 * 24));
		statistics.setAverageAccess(statistics.getTotalAccess()/totalDays);
		//信息公开访问统计
		hql = "select sum(AccessStat.times) from AccessStat AccessStat where AccessStat.applicationName='cms/infopublic'";
		total = (Number)databaseService.findRecordByHql(hql);
		statistics.setInfoPublicAccess(total==null ? 0 : total.intValue());
		return statistics;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.stat.service.StatService#clearTodayLoginStat()
	 */
	public void clearTodayLoginStat() throws ServiceException {
		databaseService.deleteRecordsByHql("from TodayLoginStat TodayLoginStat");		
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
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
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
