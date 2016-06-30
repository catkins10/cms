package com.yuanluesoft.j2oa.attendance.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.attendance.pojo.AttendanceMend;
import com.yuanluesoft.j2oa.attendance.pojo.AttendanceRecord;
import com.yuanluesoft.j2oa.attendance.service.AttendanceService;
import com.yuanluesoft.j2oa.businesstrip.pojo.Businesstrip;
import com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService;
import com.yuanluesoft.j2oa.leave.pojo.Leave;
import com.yuanluesoft.j2oa.leave.service.LeaveService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.report.excel.ExcelReportCallback;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.timetable.model.LeavePost;
import com.yuanluesoft.jeaf.timetable.model.WorkAttendance;
import com.yuanluesoft.jeaf.timetable.services.TimetableService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * TODO: 自动删除前一年的打卡记录
 * @author linchuan
 *
 */
public class AttendanceServiceImpl extends BusinessServiceImpl implements AttendanceService {
	private LeaveService leaveService; //请假服务
	private BusinesstripService businesstripService; //出差服务
	private OrgService orgService; //组织机构服务
	private ExcelReportService excelReportService; //电子表格服务
	private TimetableService timetableService; //作息时间服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.attendance.service.AttendanceService#writeReport(long, java.sql.Date, java.sql.Date, javax.servlet.http.HttpServletResponse)
	 */
	public void writeReport(long orgId, Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException {
		Map timetables = new HashMap(); //时间表
		final List attendanceDataSet = new ArrayList();
		for(int i=0; ; i+=200) {
			//获取人员列表
			List persons = orgService.listOrgPersons("" + orgId, null, true, false, i, 200);
			if(persons==null || persons.isEmpty()) {
				break;
			}
			for(Iterator iterator=persons.iterator(); iterator.hasNext();) {
				Person person = (Person)iterator.next();
				ExcelReportData data = attendanceStats(person, beginDate, endDate, timetables);
				if(data!=null) { //不是空的
					attendanceDataSet.add(data); //考勤情况统计
				}
			}
			if(persons.size()<200) {
				break;
			}
		}
		ExcelReportCallback excelReportCallback = new ExcelReportCallback() {
			public ExcelReport getExcelReport(String sheetName) {
				ExcelReport report = new ExcelReport();
				report.setHeadRowNumbers("0,1"); //表头行号列表,如:1,2,3
				report.setReferenceRowNumbers("2"); //格式参考行号列表,如:4,5
				report.setDataRowNumber(3); //数据输出的行号
				report.setDataSet(attendanceDataSet); //数据列表
				return report;
			}

			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null;
			}
		};
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "j2oa/attendance/template/考勤.xls", "考勤(" + DateTimeUtils.formatDate(beginDate, null) + "~" + DateTimeUtils.formatDate(endDate, null) + ").xls", response, excelReportCallback);
	}
	
	/**
	 * 对一个用户的考勤统计
	 * @param person
	 * @param beginDate
	 * @param endDate
	 * @param timetables
	 * @return
	 * @throws ServiceException
	 */
	private ExcelReportData attendanceStats(Person person, Date beginDate, Date endDate, Map timetables) throws ServiceException {
		//获取用户的打卡记录
		String hql = "select AttendanceRecord.punchTime" +
					 " from AttendanceRecord AttendanceRecord" +
					 " where AttendanceRecord.personId=" + person.getId() +
					 " and AttendanceRecord.punchTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and AttendanceRecord.punchTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" +
					 " order by AttendanceRecord.punchTime";
		List punchTimes = getDatabaseService().findRecordsByHql(hql);
		if(punchTimes==null) {
			punchTimes = new ArrayList();
		}
	
		//获取补卡记录
		hql = "select AttendanceMend.mendTime" +
			  " from AttendanceMend AttendanceMend" +
			  " where AttendanceMend.personId=" + person.getId() +
			  " and AttendanceMend.pass='1'" +
			  " and AttendanceMend.mendTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  " and AttendanceMend.mendTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" +
			  " order by AttendanceMend.mendTime";
		List mendTimes = getDatabaseService().findRecordsByHql(hql);
		if(mendTimes!=null) {
			punchTimes.addAll(mendTimes);
			Collections.sort(punchTimes, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					Timestamp time0 = (Timestamp)arg0;
					Timestamp time1 = (Timestamp)arg1;
					return time0.equals(time1) ? 0 : (time0.before(time1) ? -1 : 1);
				}
			});
		}
		
		List leavePosts = new ArrayList(); //离岗记录
		//获取请假记录,并转换为离岗记录
		List leaves = leaveService.listPersonalLeaves(person.getId(), new Timestamp(beginDate.getTime()), new Timestamp(endDate.getTime()));
		for(Iterator iterator = leaves==null ? null : leaves.iterator(); iterator!=null && iterator.hasNext();) {
			Leave leave = (Leave)iterator.next();
			leavePosts.add(new LeavePost(leave.getType(), true, "病假".equals(leave.getType()), "事假".equals(leave.getType()), false, leave.getActualBeginTime(), leave.getActualEndTime()));
		}
		
		//获取出差记录
		List businesstrips = businesstripService.listPersonalBusinesstrips(person.getId(), new Timestamp(beginDate.getTime()), new Timestamp(endDate.getTime()));
		for(Iterator iterator = businesstrips==null ? null : businesstrips.iterator(); iterator!=null && iterator.hasNext();) {
			Businesstrip businesstrip = (Businesstrip)iterator.next();
			leavePosts.add(new LeavePost("出差", false, false, false, true, businesstrip.getBeginTime(), businesstrip.getEndTime()));
		}
		
		//考勤统计
		WorkAttendance workAttendance = timetableService.workAttendanceStat(person, beginDate, endDate, punchTimes, leavePosts);
		
		//检查是否有缺勤、请假记录
		if(workAttendance.getLateTimes()==0 && //迟到次数
		   workAttendance.getEarlyTimes()==0 && //早退次数
		   workAttendance.getAbsentDays()==0l && //缺勤天数
		   workAttendance.getOvertimeDays()==0l && //加班天数
		   workAttendance.getLeavePostDays()==0l) { //离岗天数
			return null;
		}
		
		//转换为报表数据
		ExcelReportData reportData = new ExcelReportData("2", false, null); //格式参考第二行,不固定行高
		reportData.setPropertyValue("personName", workAttendance.getPersonName()); //用户名
		reportData.setPropertyNumberValue("lateTimes", workAttendance.getLateTimes()); //迟到次数
		reportData.setPropertyNumberValue("earlyTimes", workAttendance.getEarlyTimes()); //早退次数
		reportData.setPropertyNumberValue("absentDays", workAttendance.getAbsentDays()); //缺勤天数
		reportData.setPropertyNumberValue("overtimeDays", workAttendance.getOvertimeDays()); //加班天数
		reportData.setPropertyNumberValue("legalOvertimeDays", workAttendance.getLegalOvertimeDays()); //法定假日加班天数
		reportData.setPropertyNumberValue("leavePostDays", workAttendance.getLeavePostDays()); //离岗天数
		reportData.setPropertyNumberValue("leaveDays", workAttendance.getLeaveDays()); //请假天数
		reportData.setPropertyNumberValue("compassionateLeaveDays", workAttendance.getCompassionateLeaveDays()); //事假天数
		reportData.setPropertyNumberValue("sickLeaveDays", workAttendance.getSickLeaveDays()); //病假天数
		reportData.setPropertyNumberValue("businesstripDays", workAttendance.getBusinesstripDays()); //出差天数
		//设置各离岗类型对应的天数
		Set leavePostTypes = workAttendance.getLeavePostTypes();
		for(Iterator iterator = leavePostTypes==null ? null : leavePostTypes.iterator(); iterator!=null && iterator.hasNext();) {
			String leavePostType = (String)iterator.next();
			reportData.setPropertyNumberValue(leavePostType, workAttendance.getLeavePostDaysByType(leavePostType));
		}
		return reportData;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.attendance.service.AttendanceService#punchCard(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void punchCard(SessionInfo sessionInfo) throws ServiceException {
		AttendanceRecord attendanceRecord = new AttendanceRecord();
		attendanceRecord.setId(UUIDLongGenerator.generateId()); //ID
		attendanceRecord.setPersonId(sessionInfo.getUserId()); //用户ID
		attendanceRecord.setPunchTime(DateTimeUtils.now()); //打卡时间
		getDatabaseService().saveRecord(attendanceRecord);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.attendance.service.AttendanceService#approvalMend(com.yuanluesoft.j2oa.attendance.pojo.AttendanceMend)
	 */
	public void approvalMend(AttendanceMend mend) throws ServiceException {
		mend.setPass('1');
		getDatabaseService().updateRecord(mend);
	}

	/**
	 * @return the businesstripService
	 */
	public BusinesstripService getBusinesstripService() {
		return businesstripService;
	}

	/**
	 * @param businesstripService the businesstripService to set
	 */
	public void setBusinesstripService(BusinesstripService businesstripService) {
		this.businesstripService = businesstripService;
	}

	/**
	 * @return the leaveService
	 */
	public LeaveService getLeaveService() {
		return leaveService;
	}

	/**
	 * @param leaveService the leaveService to set
	 */
	public void setLeaveService(LeaveService leaveService) {
		this.leaveService = leaveService;
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
	 * @return the excelReportService
	 */
	public ExcelReportService getExcelReportService() {
		return excelReportService;
	}

	/**
	 * @param excelReportService the excelReportService to set
	 */
	public void setExcelReportService(ExcelReportService excelReportService) {
		this.excelReportService = excelReportService;
	}

	/**
	 * @return the timetableService
	 */
	public TimetableService getTimetableService() {
		return timetableService;
	}

	/**
	 * @param timetableService the timetableService to set
	 */
	public void setTimetableService(TimetableService timetableService) {
		this.timetableService = timetableService;
	}
}