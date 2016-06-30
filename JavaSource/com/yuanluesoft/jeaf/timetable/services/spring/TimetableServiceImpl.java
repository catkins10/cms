package com.yuanluesoft.jeaf.timetable.services.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.timetable.exception.NoTimetableException;
import com.yuanluesoft.jeaf.timetable.model.LeavePost;
import com.yuanluesoft.jeaf.timetable.model.WorkAttendance;
import com.yuanluesoft.jeaf.timetable.pojo.Timetable;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableCommuteTime;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableFestival;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableFestivalAdjust;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableOffDay;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableOvertime;
import com.yuanluesoft.jeaf.timetable.pojo.TimetableWorkDayTask;
import com.yuanluesoft.jeaf.timetable.services.TimetableService;
import com.yuanluesoft.jeaf.timetable.services.TimetableServiceListener;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class TimetableServiceImpl extends BusinessServiceImpl implements TimetableService {
	private SessionService sessionService; //会话服务
	private OrgService orgService; //组织机构服务
	private String timetableServiceListenerNames; //侦听器名称列表
	private DistributionService distributionService; //分布式服务
	
	//私有属性
	private List timetableServiceListeners = new ArrayList(); //侦听器列表
	
	/**
	 * 启动
	 *
	 */
	public void startup() {
		startupProcessWorkDayTasks(30000); //30秒后启动工作日变动任务处理
	}
	
	/**
	 * 处理工作日变动任务
	 *
	 */
	public void processWorkDayChangeTasks() throws ServiceException {
		if(!distributionService.isMasterServer(true)) { //不是主服务器
			distributionService.invokeMethodOnMasterServer("timetableService", "processWorkDayChangeTasks", null);
			return;
		}
		synchronized (timetableServiceListeners) {
			//初始化侦听器列表
			initTimetableServiceListeners();
			//获取任务
			String hql = "from TimetableWorkDayTask TimetableWorkDayTask order by TimetableWorkDayTask.created";
			for(;;) { 
				List tasks = getDatabaseService().findRecordsByHql(hql, 0, 200); //每次处理200个任务
				for(Iterator iterator = tasks==null ? null : tasks.iterator(); iterator!=null && iterator.hasNext();) {
					TimetableWorkDayTask workDayTask = (TimetableWorkDayTask)iterator.next();
					for(Iterator iteratorListener = timetableServiceListeners.iterator(); iteratorListener.hasNext();) {
						TimetableServiceListener timetableServiceListener = (TimetableServiceListener)iteratorListener.next();
						try {
							timetableServiceListener.onWorkDayChanged(workDayTask.getBeginDate(), workDayTask.getEndDate());
						}
						catch(Exception e) {
							Logger.exception(e);
						}
					}
					getDatabaseService().deleteRecord(workDayTask); //从数据库中删除任务
				}
				if(tasks==null || tasks.size()<200) {
					break;
				}
			}
		}
	}
	
	/**
	 * 初始化侦听器列表
	 *
	 */
	private void initTimetableServiceListeners() {
		if(timetableServiceListeners.isEmpty() && timetableServiceListenerNames!=null && !timetableServiceListenerNames.isEmpty()) {
			String[] names = timetableServiceListenerNames.split(",");
			for(int i=0; i<names.length; i++) {
				try {
					timetableServiceListeners.add((TimetableServiceListener)Environment.getService(names[i]));
				}
				catch(Exception e) {
					
				}
			}
		}
	}
	
	/**
	 * 启动工作日变动任务处理
	 * @param delay
	 */
	private void startupProcessWorkDayTasks(long delay) {
		new Timer().schedule(new TimerTask() {
			public void run() {
				try {
					processWorkDayChangeTasks();
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
			}
		}, delay); //等待30s后启动
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		createWorkDayChangeTask(record, false);
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		createWorkDayChangeTask(record, true);
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		createWorkDayChangeTask(record, false);
	}
	
	/**
	 * 生成工作日变动任务
	 * @param record
	 * @param isUpdate
	 * @throws ServiceException
	 */
	private void createWorkDayChangeTask(Record record, boolean isUpdate) throws ServiceException {
		java.util.Date beginDate = null;
		java.util.Date endDate = null;
		java.util.Date oldBeginDate = null;
		java.util.Date oldEndDate = null;
		if(record instanceof Timetable) { //时间表
			beginDate = ((Timetable)record).getEffectiveDate();
			if(isUpdate) {
				String hql = "select Timetable.effectiveDate from Timetable Timetable where Timetable.id=" + record.getId();
				Date effectiveDate = (Date)getDatabaseService().findRecordByHql(hql);
				if(effectiveDate!=null) {
					if(effectiveDate.equals(beginDate)) {
						return;
					}
					if(effectiveDate.before(beginDate)) {
						beginDate = effectiveDate;
					}
				}
			}
		}
		else if(record instanceof TimetableCommuteTime) { //上下班时间
			TimetableCommuteTime commuteTime = (TimetableCommuteTime)record;
			Timetable timetable = getTimetable(commuteTime.getTimetableId());
			if(timetable==null) {
				return;
			}
			beginDate = timetable.getEffectiveDate();
		}
		else if(record instanceof TimetableOffDay) { //日常休息时间
			TimetableOffDay offDay = (TimetableOffDay)record;
			Timetable timetable = getTimetable(offDay.getTimetableId());
			if(timetable==null) {
				return;
			}
			beginDate = timetable.getEffectiveDate();
		}
		else if(record instanceof TimetableFestival) { //节假日
			TimetableFestival festival = (TimetableFestival)record;
			beginDate = festival.getBeginTime();
			endDate = festival.getEndTime();
			if(isUpdate) {
				String hql = "select TimetableFestival.beginTime, TimetableFestival.endTime" +
							 " from TimetableFestival TimetableFestival" +
							 " where TimetableFestival.id=" + record.getId();
				Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
				oldBeginDate = (Timestamp)values[0];
				oldEndDate = (Timestamp)values[1];
				if(beginDate.equals(oldBeginDate) && endDate.equals(oldEndDate)) { //没有变化
					return;
				}
			}
		}
		else if(record instanceof TimetableFestivalAdjust) { //调休
			TimetableFestivalAdjust adjust = (TimetableFestivalAdjust)record;
			beginDate = adjust.getBeginTime();
			endDate = adjust.getEndTime();
			if(isUpdate) {
				String hql = "select TimetableFestivalAdjust.beginTime, TimetableFestivalAdjust.endTime" +
							 " from TimetableFestivalAdjust TimetableFestivalAdjust" +
							 " where TimetableFestivalAdjust.id=" + record.getId();
				Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
				oldBeginDate = (Timestamp)values[0];
				oldEndDate = (Timestamp)values[1];
				if(beginDate.equals(oldBeginDate) && endDate.equals(oldEndDate)) { //没有变化
					return;
				}
			}
		}
		TimetableWorkDayTask workDayTask = new TimetableWorkDayTask();
		workDayTask.setId(UUIDLongGenerator.generateId()); //ID
		workDayTask.setBeginDate(beginDate==null ? null : DateTimeUtils.date(beginDate.getTime())); //开始时间
		workDayTask.setEndDate(endDate==null ? null : DateTimeUtils.date(endDate.getTime())); //结束时间
		workDayTask.setCreated(DateTimeUtils.now()); //登记时间
		getDatabaseService().saveRecord(workDayTask);
		if(oldBeginDate!=null) {
			workDayTask = new TimetableWorkDayTask();
			workDayTask.setId(UUIDLongGenerator.generateId()); //ID
			workDayTask.setBeginDate(DateTimeUtils.date(oldBeginDate.getTime())); //开始时间
			workDayTask.setEndDate(oldEndDate==null ? null : DateTimeUtils.date(oldEndDate.getTime())); //结束时间
			workDayTask.setCreated(DateTimeUtils.now()); //登记时间
			getDatabaseService().saveRecord(workDayTask);
		}
		startupProcessWorkDayTasks(5000); //5秒后启动工作日变动任务处理
	}
	
	/**
	 * 获取时间表
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	private Timetable getTimetable(long id) throws ServiceException {
		return (Timetable)getDatabaseService().findRecordById(Timetable.class.getName(), id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableService#countWorkDays(java.sql.Timestamp, java.sql.Timestamp, long)
	 */
	public double countWorkDays(Timestamp beginTime, Timestamp endTime, long orgId) throws ServiceException {
		String userIds = orgId==0 ? "0" : orgId + "," + orgService.listParentOrgIds(orgId);
		return countWorkDays(beginTime, endTime, userIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableService#countWorkDays(java.sql.Timestamp, java.sql.Timestamp, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public double countWorkDays(Timestamp beginTime, Timestamp endTime, SessionInfo sessionInfo) throws ServiceException {
		return countWorkDays(beginTime, endTime, sessionInfo.getUserIds());
	}

	/**
	 * 计算工作日
	 * @param beginDate
	 * @param endDate
	 * @param userIds
	 * @return
	 * @throws ServiceException
	 */
	private double countWorkDays(Timestamp beginTime, Timestamp endTime, String userIds) throws ServiceException {
		Date beginDate = DateTimeUtils.date(beginTime);
		Date endDate = DateTimeUtils.date(endTime);
		double days = 0;
		List personalTimetables = listPersonalTimetables(userIds, beginDate, endDate);
		if(personalTimetables==null || personalTimetables.isEmpty()) {
			throw new NoTimetableException();
		}
		for(Date date = beginDate; !date.after(endDate); date=DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1)) {
			WorkDay workDay = computeWorkDay(date, personalTimetables, date.equals(beginDate) ? beginTime : null, date.equals(endDate) ? endTime : null);
			days += workDay.getWorkDay();
		}
		return days;
	}
	
	/**
	 * 计算工作日
	 * @param date
	 * @param personalTimetables
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private WorkDay computeWorkDay(Date date, List personalTimetables, Timestamp beginTime, Timestamp endTime) {
		//获取时间表
		Timetable timetable = getTimetableByDate(date, personalTimetables);
		
		//获取用户当天的工作时间
		Timestamp[] workTime = getWorkTime(timetable, date);
		double workBeginHour = DateTimeUtils.getHour(workTime[0]) + (DateTimeUtils.getMinute(workTime[0]) + 0.0)/60.0;
		double workEndHour = DateTimeUtils.getHour(workTime[1]) + (DateTimeUtils.getMinute(workTime[1]) + 0.0)/60.0;
		double workFullHours = workEndHour - workBeginHour; //一天的工作小时数
		//计算工作开始的小时和结束的小时
		if(beginTime==null || beginTime.before(workTime[0])) {
			beginTime = workTime[0];
		}
		if(endTime==null || endTime.after(workTime[1])) {
			endTime = workTime[1];
		}
		workBeginHour = DateTimeUtils.getHour(beginTime) + (DateTimeUtils.getMinute(beginTime) + 0.0)/60.0;
		workEndHour = DateTimeUtils.getHour(endTime) + (DateTimeUtils.getMinute(endTime) + 0.0)/60.0;
		
		//获取用户当天的休息时间
		Timestamp[] offTime = getOffTime(timetable, date);
		//计算休息开始的小时和结束的小时
		double offBeginHour = offTime[0]==null ? 0 : DateTimeUtils.getHour(offTime[0]) + (DateTimeUtils.getMinute(offTime[0]) + 0.0)/60.0;
		double offEndHour = offTime[0]==null ? 0 : DateTimeUtils.getHour(offTime[1]) + (DateTimeUtils.getMinute(offTime[1]) + 0.0)/60.0;

		//计算工作时长
		double workHours = Math.min(workEndHour - workBeginHour, Math.max(0, offBeginHour-workBeginHour) + Math.max(0, workEndHour-offEndHour));
		double workDays = 0; 
		if(workHours>=workFullHours*0.8) { //工作时间超过80%,计为1个工作日
			workDays = 1;
		}
		else if(workHours>workFullHours*0.4) { //工作时间超过40%,计为0.5个工作日
			workDays = 0.5;
		}
		//System.out.println("***********workDays:" + workDays + " workHours:" + workHours + " workFullHours:" + workFullHours + " workBeginHour:" + workBeginHour + " workEndHour:" + workEndHour + " offBeginHour:" + offBeginHour + " offEndHour:" + offEndHour + " workTime: " + workTime[0] + "," + workTime[1]);
		return new WorkDay(workDays, workHours, workFullHours, workBeginHour, workEndHour, offBeginHour, offEndHour);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableService#addWorkDays(java.sql.Timestamp, double, long)
	 */
	public Timestamp addWorkDays(Timestamp time, double days, long orgId) throws ServiceException {
		String userIds = orgId==0 ? "0" : orgId + "," + orgService.listParentOrgIds(orgId);
		return addWorkDays(time, days, userIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableService#addWorkDays(java.sql.Timestamp, double, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Timestamp addWorkDays(Timestamp time, double days, SessionInfo sessionInfo) throws ServiceException {
		return addWorkDays(time, days, sessionInfo.getUserIds());
	}
	
	/**
	 * 增加工作日
	 * @param date
	 * @param days
	 * @param userIds
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp addWorkDays(Timestamp time, double days, String userIds) throws ServiceException {
		if(days<=0) {
			return time;
		}
		Date beginDate = DateTimeUtils.date(time);
		Date endDate = null;
		boolean firstDay = true;
		List personalTimetables = null;
		double addDays = days;
		for(double leftDays = days; leftDays>0; beginDate=DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, 1)) {
			if(endDate==null || beginDate.after(endDate)) {
				endDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, Math.min(30, (int)Math.round(leftDays * 2))); //结束时间按2倍工作日计算
				personalTimetables = listPersonalTimetables(userIds, beginDate, endDate); //获取时间表
				if(personalTimetables==null || personalTimetables.isEmpty()) {
					throw new NoTimetableException();
				}
			}
			//计算工作日
			WorkDay wordDay = computeWorkDay(beginDate, personalTimetables, firstDay ? time : null, null);
			if(wordDay.getWorkDay()==1) { //1个工作日
				if(leftDays<1) {
					return DateTimeUtils.set(new Timestamp(beginDate.getTime()), Calendar.HOUR_OF_DAY, leftDays<=0.5 && wordDay.getWorkHours()>=wordDay.getWorkFullHours()*0.9 ? 12 : 23); //如果剩余时间小等于于0.5,把截止时间设为当天的12:00,否则设为23:00
				}
				leftDays -= 1;
			}
			else if(wordDay.getWorkDay()==0.5) { //0.5个工作日
				if(leftDays<=0.5) {
					return DateTimeUtils.set(new Timestamp(beginDate.getTime()), Calendar.HOUR_OF_DAY, 23); //把截止时间设为当天的23:00
				}
				leftDays -= 0.5;
				if(firstDay) { //第一天
					addDays -= 0.5; //减0.5天
					time = DateTimeUtils.set(time, Calendar.HOUR_OF_DAY, 23); //把开始时间设置为当天的23点
				}
				else {
					addDays += 0.5; //向后补0.5天
				}
			}
			else { //0个工作日
				if(!firstDay){ //不是第一天
					addDays += 1; //向后补1天
				}
				else if(wordDay.getWorkHours()<=0 || wordDay.getWorkHours()!=wordDay.getWorkEndHour()-wordDay.getWorkBeginHour()) { //是第一天
					time = DateTimeUtils.set(time, Calendar.HOUR_OF_DAY, 23); //把开始时间设置为当天的23点
				}
			}
			firstDay = false;
		}
		return DateTimeUtils.add(time, Calendar.HOUR_OF_DAY, (int)(addDays * 24));
	}
	
	/**
	 * 获取时间表
	 * @param date
	 * @param personalTimetables
	 * @return
	 */
	private Timetable getTimetableByDate(Date date, List personalTimetables) {
		//获取对应的时间表
		Timetable timetable = null;
		for(Iterator iterator = personalTimetables.iterator(); iterator.hasNext();) {
			timetable = (Timetable)iterator.next();
			if(!timetable.getEffectiveDate().after(date)) {
				break;
			}
		}
		return timetable;
	}
	
	/**
	 * 获取一天工作时间,返回Timestamp[]{beginTime, endTime}
	 * @param timetable
	 * @param date
	 * @return
	 */
	private Timestamp[] getWorkTime(Timetable timetable, Date date) {
		String beginTime = null;
		String endTime = null;
		List commuteTimes = getCommuteTimes(timetable, date);
		for(Iterator iterator = commuteTimes.iterator(); iterator.hasNext();) {
			TimetableCommuteTime commuteTime = (TimetableCommuteTime)iterator.next();
			if(commuteTime.getIsOvertime()=='1') {
				continue;
			}
			if(beginTime==null || beginTime.compareTo(commuteTime.getOnDutyTime())>0) {
				beginTime = commuteTime.getOnDutyTime();
			}
			if(endTime==null || endTime.compareTo(commuteTime.getOffDutyTime())<0) {
				endTime = commuteTime.getOffDutyTime();
			}
		}
		if(beginTime==null) {
			beginTime = "08:00";
		}
		if(endTime==null) {
			endTime = "18:00";
		}		
		String dateText = DateTimeUtils.formatDate(date, null);
		try {
			return new Timestamp[]{DateTimeUtils.parseTimestamp(dateText + " " + beginTime, "yyyy-MM-dd HH:mm"), DateTimeUtils.parseTimestamp(dateText + " " + endTime, "yyyy-MM-dd HH:mm")};
		}
		catch(ParseException e) {
			return null;
		}
	}
	
	/**
	 * 获取上下班时间
	 * @param timetable
	 * @param date
	 * @return
	 */
	private List getCommuteTimes(Timetable timetable, Date date) {
		String day = DateTimeUtils.formatDate(date, "MM-dd");
		List normalCommuteTimes = new ArrayList(); //默认上班时间
		List rangedCommuteTimes = new ArrayList(); //时段内的上班时间,如:夏时制
		for(Iterator iterator = timetable.getCommuteTimes().iterator(); iterator.hasNext();) {
			TimetableCommuteTime commuteTime = (TimetableCommuteTime)iterator.next();
			if(commuteTime.getEffectiveBegin()==null || commuteTime.getEffectiveBegin().isEmpty() || commuteTime.getEffectiveEnd()==null || commuteTime.getEffectiveEnd().isEmpty()) {
				normalCommuteTimes.add(commuteTime);
			}
			else if(day.compareTo(commuteTime.getEffectiveBegin())>=0 && day.compareTo(commuteTime.getEffectiveEnd())<=0) {
				rangedCommuteTimes.add(commuteTime);
			}
		}
		return rangedCommuteTimes.isEmpty() ? normalCommuteTimes : rangedCommuteTimes;
	}
	
	/**
	 * 获取一天中的休息/放假时段Timestamp[]{beginTime, endTime}
	 * @param timetable
	 * @param date
	 * @return
	 */
	private Timestamp[] getOffTime(Timetable timetable, Date date) {
		//检查是否节日放假
		if(timetable.getFestivals()!=null && !timetable.getFestivals().isEmpty()) {
			Timestamp beginTime = new Timestamp(date.getTime());
			Timestamp endTime = DateTimeUtils.add(DateTimeUtils.add(beginTime, Calendar.DAY_OF_MONTH, 1), Calendar.SECOND, -1);
			for(Iterator iterator = timetable.getFestivals().iterator(); iterator.hasNext();) {
				TimetableFestival festival = (TimetableFestival)iterator.next();
				if(!endTime.after(festival.getBeginTime()) || !beginTime.before(festival.getEndTime())) { //当天不在节日放假时间内
					continue;
				}
				if(!festival.getBeginTime().before(beginTime) && festival.getBeginTime().before(endTime)) { //放假开始时间就在当天
					beginTime = festival.getBeginTime();
				}
				if(festival.getEndTime().after(beginTime) && !festival.getEndTime().after(endTime)) { //放假结束时间就在当天
					endTime = festival.getEndTime();
				}
				return new Timestamp[]{beginTime, endTime};
			}
		}
		//检查是否日常休息时间
		int week = DateTimeUtils.getWeek(date);
		for(Iterator iterator = timetable.getOffDays()==null ? null : timetable.getOffDays().iterator(); iterator!=null && iterator.hasNext();) {
			TimetableOffDay offDay = (TimetableOffDay)iterator.next();
			if(offDay.getOffDayOfWeek()!=week) { //星期不匹配
				continue;
			}
			Timestamp offBeginTime = compositeTimestamp(date, offDay.getBeginTime());
			Timestamp offEndTime = compositeTimestamp(date, offDay.getEndTime());
			//检查调休时间
			boolean adjusted = false;
			for(Iterator iteratorFestival = timetable.getFestivals().iterator(); !adjusted && iteratorFestival.hasNext();) {
				TimetableFestival festival = (TimetableFestival)iteratorFestival.next();
				for(Iterator iteratorAdjust = festival.getAdjusts()==null ? null : festival.getAdjusts().iterator(); iteratorAdjust!=null && iteratorAdjust.hasNext();) {
					TimetableFestivalAdjust adjust = (TimetableFestivalAdjust)iteratorAdjust.next();
					if(!adjust.getBeginTime().before(offEndTime) || !adjust.getEndTime().after(offBeginTime)) { //调休开始时间在结束时间之后,或者调休结束时间在开始时间前
						continue;
					}
					if(!adjust.getBeginTime().after(offBeginTime) && !adjust.getEndTime().before(offEndTime)) { //调休时间包含了日常休息时间
						offBeginTime = null;
						offEndTime = null;
					}
					else if(!adjust.getBeginTime().after(offEndTime) && adjust.getBeginTime().after(offBeginTime)) { //调休开始时间在休息时间内
						offEndTime = adjust.getBeginTime(); //休息结束时间设置为调休开始时间
					}
					else { //调休结束时间在休息时间内
						offBeginTime = adjust.getEndTime(); //休息开始时间设置为调休结束时间
					}
					adjusted = true;
					break;
				}
			}
			return new Timestamp[]{offBeginTime, offEndTime};
		}
		return new Timestamp[]{null, null}; //正常上班
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.timetable.services.TimetableService#workAttendanceStat(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.sql.Date, java.sql.Date, java.util.List, java.util.List)
	 */
	public WorkAttendance workAttendanceStat(Person person, Date beginDate, Date endDate, List clockInTimes, List leavePosts) throws ServiceException {
		//获取用户使用的时间表
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(person.getLoginName());
		}
		catch (SessionException e) {
			throw new ServiceException(e.getMessage());
		}
		List personalTimetables = listPersonalTimetables(sessionInfo.getUserIds(), beginDate, endDate);
		//考勤统计
		WorkAttendance workAttendance = new WorkAttendance();
		workAttendance.setPersonName(person.getName());
		for(; !beginDate.after(endDate); beginDate=DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, 1)) {
			workAttendanceOneDayStat(workAttendance, beginDate, personalTimetables, clockInTimes, leavePosts);
		}
		return workAttendance;
	}
	
	/**
	 * 统计一个用户一天的考勤情况
	 * @param attendanceData
	 * @param date
	 * @param personalTimetables
	 * @param punchTimes
	 * @param leaves
	 * @param businesstrips
	 */
	private void workAttendanceOneDayStat(WorkAttendance workAttendance, Date date, List personalTimetables, List clockInTimes, List leavePosts) {
		//获取作息时间表
		Timetable timetable = getTimetableByDate(date, personalTimetables);
		//获取休息时段
		Timestamp[] offTime = getOffTime(timetable, date);
		//获取详细的工作时间
		List workTimes = retrieveWorkTimes(timetable, date, offTime[0], offTime[1], leavePosts);
		
		long overtimeTotal = 0; //加班时长
		long legalOvertimeTotal = 0; //法定假期加班时长
		for(Iterator iterator = workTimes.iterator(); iterator.hasNext();) {
			WorkTime workTime = (WorkTime)iterator.next(); //工作时间
			//检查上、下班打卡时间
			boolean clockInOnTime = false; //是否按时上班
			boolean clockOutOnTime = false; //是否按时下班
			Timestamp clockIn = null; //上班打卡时间
			Timestamp clockOut = null; //下班打卡时间
			for(Iterator iteratorClockIn=clockInTimes.iterator(); iteratorClockIn.hasNext();) {
				Timestamp time = (Timestamp)iteratorClockIn.next();
				if(time.before(workTime.getClockInBegin())) { //早于上班打卡开始时间
					continue;
				}
				if(time.after(workTime.getClockOutEnd())) { //晚于下班打卡结束时间
					break;
				}
				if(!time.after(workTime.getClockInEnd())) { //早于上班打卡结束时间
					clockInOnTime = true; //按时上班
					clockIn = time; //设置为上班时间
				}
				else if(!time.before(workTime.getClockOutBegin())) {
					clockOutOnTime = true; //按时下班
					clockOut =  time;
					break;
				}
				else if(clockIn==null) {
					clockIn = time; //设置为上班时间
				}
				else {
					clockOut = time; //设置为下班时间
				}
			}
			if(workTime.getBeginTime()!=null) { //正常上班
				if(clockInOnTime && clockOutOnTime) { //按时上下班
					continue;
				}
				boolean absent = false; //缺勤
				boolean late = false; //迟到
				boolean early = false; //早退
				if(!clockInOnTime) { //没有按时上班
					if(clockIn==null || //上班没有打卡
					   (clockIn.getTime()-workTime.getBeginTime().getTime())/60000>workTime.getCommuteTime().getLateMiniutes()) { //或者超过迟到时长
						absent = true;
					}
					else { //迟到
						late = true;
					}
				}
				if(!absent && !clockOutOnTime) { //没有按时下班
					if(clockOut==null || //下班没有打卡
					   (workTime.getEndTime().getTime()-clockOut.getTime())/60000>workTime.getCommuteTime().getEarlyMiniutes()) { //或者超过早退时长
						absent = true;
					}
					else { //早退
						early = true;
					}
				}
				if(absent) { //缺勤
					workAttendance.setAbsentDays(workAttendance.getAbsentDays() + workTime.getCommuteTime().getAbsentDay());
					continue;
				}
				if(late) { //迟到
					workAttendance.setLateTimes(workAttendance.getLateTimes()+1);
				}
				if(early) { //早退
					workAttendance.setEarlyTimes(workAttendance.getEarlyTimes()+1);
				}
			}
			else if(workTime.getLeavePostDays()<=0) { //加班,且不是离岗(如:请假、出差、培训)
				if(clockInOnTime && clockOutOnTime) { //按时上下班
					workAttendance.setOvertimeDays(workAttendance.getOvertimeDays() + workTime.getCommuteTime().getAbsentDay());
					if(workTime.isLegalFestival()) { //法定假期
						workAttendance.setLegalOvertimeDays(workAttendance.getLegalOvertimeDays() + workTime.getCommuteTime().getAbsentDay());
					}
					continue;
				}
				if(!clockInOnTime && clockIn==null) { //没有按时上班,且上班没有打卡
					continue;
				}
				if(!clockOutOnTime && clockOut==null) { //没有按时下班,且下班没有打卡
					continue;
				}
				//累积加班时间
				long time = (clockOutOnTime ? workTime.getCommuteEndTime() : clockOut).getTime() - (clockInOnTime ? workTime.getCommuteBeginTime() : clockIn).getTime();
				if(time>0) {
					overtimeTotal += time;
					if(workTime.isLegalFestival()) { //法定假期
						legalOvertimeTotal += time;
					}
				}
			}
			if(workTime.getLeavePostDays()>0) { //离岗
				workAttendance.setLeavePostDays(workAttendance.getLeavePostDays() + workTime.getLeavePostDays()); //累加离岗时间
				workAttendance.addLeavePostDaysByType(workTime.getLeavePostType(), workTime.getLeavePostDays()); //累加指定类型的离岗时间
				if(workTime.isLeave()) { //请假
					workAttendance.setLeaveDays(workAttendance.getLeaveDays() + workTime.getLeavePostDays()); //累加请假时间
				}
				if(workTime.isSickLeave()) { //病假
					workAttendance.setSickLeaveDays(workAttendance.getSickLeaveDays() + workTime.getLeavePostDays());
				}
				if(workTime.isCompassionateLeave()) { //事假
					workAttendance.setCompassionateLeaveDays(workAttendance.getCompassionateLeaveDays() + workTime.getLeavePostDays());
				}
				if(workTime.isBusinesstrip()) { //出差
					workAttendance.setBusinesstripDays(workAttendance.getBusinesstripDays() + workTime.getLeavePostDays());
				}
			}
		}
		if(overtimeTotal>0) { //有加班
			//设置加班天数
			WorkTime workTime = (WorkTime)workTimes.iterator().next();
			workAttendance.setOvertimeDays(workAttendance.getOvertimeDays() + convertDays(overtimeTotal, workTime.getTimetable()));
			workAttendance.setLegalOvertimeDays(workAttendance.getLegalOvertimeDays() + convertDays(legalOvertimeTotal, workTime.getTimetable())); //法定假期加班天数
		}
	}
	
	/**
	 * 获取工作时间
	 * @param timetable
	 * @param date
	 * @param offBeginTime
	 * @param offEndTime
	 * @param leavePosts
	 * @return
	 */
	private List retrieveWorkTimes(Timetable timetable, Date date, Timestamp offBeginTime, Timestamp offEndTime, List leavePosts) {
		List commuteTimes = getCommuteTimes(timetable, date);
		List workTimes = new ArrayList();
		for(Iterator iterator = commuteTimes.iterator(); iterator.hasNext();) {
			TimetableCommuteTime commuteTime = (TimetableCommuteTime)iterator.next();
			WorkTime workTime = new WorkTime();
			workTime.setTimetable(timetable); //使用的时间表
			workTime.setCommuteTime(commuteTime); //对应的上下班时间安排
			workTime.setCommuteBeginTime(compositeTimestamp(date, commuteTime.getOnDutyTime())); //默认的上班开始时间
			workTime.setCommuteEndTime(compositeTimestamp(date, commuteTime.getOffDutyTime())); //默认的上班结束时间
			if(commuteTime.getIsOvertime()!='1') { //不是加班
				//设置实际的开始和结束时间
				if(offBeginTime==null || //没有指定放假时间 
				   (!workTime.getCommuteEndTime().after(offBeginTime) || !workTime.getCommuteBeginTime().before(offEndTime))) { //不在放假时间范围内
					workTime.setBeginTime(workTime.getCommuteBeginTime()); //实际的开始时间,null表示不需要上班
					workTime.setEndTime(workTime.getCommuteEndTime()); //实际的结束时间,null表示不需要上班
				}
				else if(workTime.getCommuteBeginTime().before(offEndTime) && offEndTime.before(workTime.getCommuteEndTime())) { //开始时间早于放假结束时间,则开始时间以放假时间为准
					workTime.setBeginTime(offEndTime); //实际的开始时间,null表示不需要上班
					workTime.setEndTime(workTime.getCommuteEndTime()); //实际的结束时间,null表示不需要上班
				}
				else if(workTime.getCommuteEndTime().after(offBeginTime) && offBeginTime.after(workTime.getCommuteBeginTime())) { //结束时间晚于放假结束时间,则开结束时间以放假时间为准
					workTime.setBeginTime(workTime.getCommuteBeginTime()); //实际的开始时间,null表示不需要上班
					workTime.setEndTime(offBeginTime); //实际的结束时间,null表示不需要上班
				}
				else { //整个都是休息时间
					//检查是否属于法定假期
					for(Iterator iteratorFestival=timetable.getFestivals().iterator(); iteratorFestival.hasNext();) {
						TimetableFestival festival = (TimetableFestival)iteratorFestival.next();
						if(festival.getLegalBeginTime()==null || festival.getLegalEndTime()==null) {
							continue;
						}
						if(!workTime.getCommuteBeginTime().after(festival.getLegalBeginTime()) && !workTime.getCommuteEndTime().before(festival.getLegalEndTime())) {
							workTime.setLegalFestival(true);
							break;
						}
					}
				}
				
				//检查离岗情况
				if(workTime.getBeginTime()!=null) { //正常上班
					for(Iterator iteratorLeave=leavePosts.iterator(); iteratorLeave.hasNext();) {
						LeavePost leavePost = (LeavePost)iteratorLeave.next();
						//检查工作时间在不在离岗时间内
						if(!leavePost.getLeaveBegin().before(workTime.getEndTime()) || !leavePost.getLeaveEnd().after(workTime.getBeginTime())) {
							continue;
						}
						//离岗时间包含了整个上下班时间
						if(!leavePost.getLeaveBegin().after(workTime.getBeginTime()) && !leavePost.getLeaveEnd().before(workTime.getEndTime())) {
							if(workTime.getBeginTime().equals(workTime.getCommuteBeginTime()) && workTime.getEndTime().equals(workTime.getCommuteEndTime())) {
								workTime.setLeavePostDays(commuteTime.getAbsentDay()); //设置为离岗天数
							}
							else {
								workTime.setLeavePostDays(convertDays(workTime.getEndTime().getTime() - workTime.getBeginTime().getTime(), timetable)); //设置为离岗天数
							}
							workTime.setBeginTime(null); //实际的开始时间,null表示不需要上班
							workTime.setEndTime(null); //实际的结束时间,null表示不需要上班
						}
						else if(leavePost.getLeaveBegin().after(workTime.getBeginTime()) && leavePost.getLeaveBegin().before(workTime.getEndTime())) { //离岗开始时间在上班时间内
							if(leavePost.getLeaveEnd().after(workTime.getBeginTime()) && leavePost.getLeaveEnd().before(workTime.getEndTime())) {
								//离岗结束时间也在上班时间内
								workTime.setLeavePostDays(convertDays(leavePost.getLeaveEnd().getTime() - leavePost.getLeaveBegin().getTime(), timetable)); //计算离岗天数
							}
							else {
								workTime.setLeavePostDays(convertDays(workTime.getEndTime().getTime() - leavePost.getLeaveBegin().getTime(), timetable)); //计算离岗天数
								//调整上班结束时间
								if(workTime.getEndTime().after(leavePost.getLeaveBegin())) {
									workTime.setEndTime(leavePost.getLeaveBegin());
								}
							}
						}
						else if(leavePost.getLeaveEnd().after(workTime.getBeginTime()) && leavePost.getLeaveEnd().before(workTime.getEndTime())) {
							workTime.setLeavePostDays(convertDays(leavePost.getLeaveEnd().getTime() - workTime.getBeginTime().getTime(), timetable)); //计算离岗天数
							//调整上班开始时间
							if(workTime.getBeginTime().before(leavePost.getLeaveEnd())) {
								workTime.setBeginTime(leavePost.getLeaveEnd());
							}
						}
						workTime.setLeavePostType(leavePost.getType()); //设置离岗类型
						workTime.setBusinesstrip(leavePost.isBusinesstrip()); //出差
						workTime.setLeave(leavePost.isLeave()); //请假
						workTime.setSickLeave(leavePost.isSickLeave()); //病假
						workTime.setCompassionateLeave(leavePost.isCompassionateLeave()); //事假
						break;
					}
				}
			}
			//设置上下班打卡时间
			workTime.setClockInBegin(compositeTimestamp(date, commuteTime.getClockInBegin())); //上班打卡开始时间
			workTime.setClockInEnd(compositeTimestamp(date, commuteTime.getClockInEnd())); //上班打卡结束时间
			workTime.setClockOutBegin(compositeTimestamp(date, commuteTime.getClockOutBegin())); //下班打卡开始时间
			workTime.setClockOutEnd(compositeTimestamp(date, commuteTime.getClockOutEnd())); //下班打卡结束时间
			if(workTime.getBeginTime()!=null && workTime.getCommuteBeginTime().before(workTime.getBeginTime())) { //上班时间被推迟
				//推迟上班打卡结束时间
				workTime.setClockInEnd(new Timestamp(workTime.getClockInEnd().getTime() + workTime.getBeginTime().getTime() - workTime.getCommuteBeginTime().getTime()));
			}
			else if(workTime.getEndTime()!=null && workTime.getCommuteEndTime().after(workTime.getEndTime())) { //下班时间被提前
				//提前下班打卡开始时间
				workTime.setClockOutBegin(new Timestamp(workTime.getClockOutBegin().getTime() + workTime.getEndTime().getTime() - workTime.getCommuteEndTime().getTime()));
			}
			workTimes.add(workTime);
		}
		return workTimes;
	}
	
	/**
	 * 合成时间
	 * @param date
	 * @param time 格式必须是 00:00
	 * @return
	 */
	private Timestamp compositeTimestamp(Date date, String time) {
		String values[] = time.split(":");
		return new Timestamp(date.getTime() + (Integer.parseInt(values[0]) * 60 + Integer.parseInt(values[1])) * 60000);
	}
	
	/**
	 * 把时间转换成天数
	 * @param time
	 * @param timetable
	 * @return
	 */
	private double convertDays(long time, Timetable timetable) {
		if(time<=0 || timetable.getOvertimes()==null || timetable.getOvertimes().isEmpty()) {
			return 0;
		}
		//换算成小时数
		double overtimeHours = (time + 0.0) / (3600 * 1000.0);
		//检查所在的区间,转换为天数
		double absentDay = 0;
		for(Iterator iterator = timetable.getOvertimes().iterator(); iterator.hasNext();) {
			TimetableOvertime overtime = (TimetableOvertime)iterator.next();
			if(overtime.getOnDutyTime()>overtimeHours) {
				break;
			}
			absentDay = overtime.getAbsentDay();
		}
		return absentDay;
	}
	
	/**
	 * 获取用户在指定时间段内使用的时间表,按生效时间倒序排列
	 * @param personLoginName
	 * @param beginDate
	 * @param endDate
	 * @param timetables
	 * @return
	 * @throws ServiceException
	 */
	private List listPersonalTimetables(String userIds, Date beginDate, Date endDate) throws ServiceException {
		String hql = "select Timetable" +
					 " from Timetable Timetable" +
					 " left join Timetable.visitors TimetablePrivilege" +
					 " where Timetable.effectiveDate<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
					 " and TimetablePrivilege.visitorId in (" + userIds + ")" +
					 " order by Timetable.priority DESC, Timetable.effectiveDate DESC";
		List timetables = getDatabaseService().findRecordsByHql(hql, listLazyLoadProperties(Timetable.class));
		if(timetables==null || timetables.isEmpty()) {
			throw new NoTimetableException();
		}
		List personalTimetables = new ArrayList();
		for(Iterator iterator = timetables.iterator(); iterator.hasNext();) {
			Timetable timetable = (Timetable)iterator.next();
			personalTimetables.add(timetable);
			if(!timetable.getEffectiveDate().after(beginDate)) { //检查时间表,只保留一个生效时间早于beginDate的记录
				break;
			}
		}
		return personalTimetables;
	}
	
	/**
	 * 工作日
	 * @author linchuan
	 *
	 */
	private class WorkDay {
		private double workDay; //工作日
		private double workHours; //工作小时数
		private double workFullHours; //没有休息时一天的工作小时数
		private double workBeginHour; //工作开始的小时
		private double workEndHour; //工作开始结束的小时
		private double offBeginHour; //休息开始的小时
		private double offEndHour; //休息结束的小时
		
		public WorkDay(double workDay, double workHours, double workFullHours, double workBeginHour, double workEndHour, double offBeginHour, double offEndHour) {
			super();
			this.workDay = workDay;
			this.workHours = workHours;
			this.workFullHours = workFullHours;
			this.workBeginHour = workBeginHour;
			this.workEndHour = workEndHour;
			this.offBeginHour = offBeginHour;
			this.offEndHour = offEndHour;
		}
		/**
		 * @return the workDay
		 */
		public double getWorkDay() {
			return workDay;
		}
		/**
		 * @param workDay the workDay to set
		 */
		public void setWorkDay(double workDay) {
			this.workDay = workDay;
		}
		/**
		 * @return the offBeginHour
		 */
		public double getOffBeginHour() {
			return offBeginHour;
		}
		/**
		 * @param offBeginHour the offBeginHour to set
		 */
		public void setOffBeginHour(double offBeginHour) {
			this.offBeginHour = offBeginHour;
		}
		/**
		 * @return the offEndHour
		 */
		public double getOffEndHour() {
			return offEndHour;
		}
		/**
		 * @param offEndHour the offEndHour to set
		 */
		public void setOffEndHour(double offEndHour) {
			this.offEndHour = offEndHour;
		}
		/**
		 * @return the workBeginHour
		 */
		public double getWorkBeginHour() {
			return workBeginHour;
		}
		/**
		 * @param workBeginHour the workBeginHour to set
		 */
		public void setWorkBeginHour(double workBeginHour) {
			this.workBeginHour = workBeginHour;
		}
		/**
		 * @return the workEndHour
		 */
		public double getWorkEndHour() {
			return workEndHour;
		}
		/**
		 * @param workEndHour the workEndHour to set
		 */
		public void setWorkEndHour(double workEndHour) {
			this.workEndHour = workEndHour;
		}
		/**
		 * @return the workHours
		 */
		public double getWorkHours() {
			return workHours;
		}
		/**
		 * @param workHours the workHours to set
		 */
		public void setWorkHours(double workHours) {
			this.workHours = workHours;
		}
		/**
		 * @return the workFullHours
		 */
		public double getWorkFullHours() {
			return workFullHours;
		}
		/**
		 * @param workFullHours the workFullHours to set
		 */
		public void setWorkFullHours(double workFullHours) {
			this.workFullHours = workFullHours;
		}
	}
	
	/**
	 * 一天内的工作时间
	 * @author linchuan
	 *
	 */
	protected class WorkTime {
		private Timetable timetable; //使用的时间表
		private TimetableCommuteTime commuteTime; //对应的上下班时间安排
		
		private Timestamp commuteBeginTime; //默认的上班开始时间
		private Timestamp commuteEndTime; //默认的上班结束时间
		
		private Timestamp beginTime; //实际的开始时间,null表示不需要上班
		private Timestamp endTime; //实际的结束时间,null表示不需要上班

		private Timestamp clockInBegin; //上班打卡开始时间
		private Timestamp clockInEnd; //上班打卡结束时间
		private Timestamp clockOutBegin; //下班打卡开始时间
		private Timestamp clockOutEnd; //下班打卡结束时间
		
		private boolean isLegalFestival; //是否法定假期
		private double leavePostDays; //离岗天数
		private String leavePostType; //离岗类型
		private boolean isLeave; //是否请假
		private boolean isCompassionateLeave; //是否事假
		private boolean isSickLeave; //是否病假
		private boolean isBusinesstrip; //是否出差
		
		/**
		 * @return the beginTime
		 */
		public Timestamp getBeginTime() {
			return beginTime;
		}
		/**
		 * @param beginTime the beginTime to set
		 */
		public void setBeginTime(Timestamp beginTime) {
			this.beginTime = beginTime;
		}
		/**
		 * @return the clockInBegin
		 */
		public Timestamp getClockInBegin() {
			return clockInBegin;
		}
		/**
		 * @param clockInBegin the clockInBegin to set
		 */
		public void setClockInBegin(Timestamp clockInBegin) {
			this.clockInBegin = clockInBegin;
		}
		/**
		 * @return the clockInEnd
		 */
		public Timestamp getClockInEnd() {
			return clockInEnd;
		}
		/**
		 * @param clockInEnd the clockInEnd to set
		 */
		public void setClockInEnd(Timestamp clockInEnd) {
			this.clockInEnd = clockInEnd;
		}
		/**
		 * @return the clockOutBegin
		 */
		public Timestamp getClockOutBegin() {
			return clockOutBegin;
		}
		/**
		 * @param clockOutBegin the clockOutBegin to set
		 */
		public void setClockOutBegin(Timestamp clockOutBegin) {
			this.clockOutBegin = clockOutBegin;
		}
		/**
		 * @return the clockOutEnd
		 */
		public Timestamp getClockOutEnd() {
			return clockOutEnd;
		}
		/**
		 * @param clockOutEnd the clockOutEnd to set
		 */
		public void setClockOutEnd(Timestamp clockOutEnd) {
			this.clockOutEnd = clockOutEnd;
		}
		/**
		 * @return the commuteBeginTime
		 */
		public Timestamp getCommuteBeginTime() {
			return commuteBeginTime;
		}
		/**
		 * @param commuteBeginTime the commuteBeginTime to set
		 */
		public void setCommuteBeginTime(Timestamp commuteBeginTime) {
			this.commuteBeginTime = commuteBeginTime;
		}
		/**
		 * @return the commuteEndTime
		 */
		public Timestamp getCommuteEndTime() {
			return commuteEndTime;
		}
		/**
		 * @param commuteEndTime the commuteEndTime to set
		 */
		public void setCommuteEndTime(Timestamp commuteEndTime) {
			this.commuteEndTime = commuteEndTime;
		}
		/**
		 * @return the commuteTime
		 */
		public TimetableCommuteTime getCommuteTime() {
			return commuteTime;
		}
		/**
		 * @param commuteTime the commuteTime to set
		 */
		public void setCommuteTime(TimetableCommuteTime commuteTime) {
			this.commuteTime = commuteTime;
		}
		/**
		 * @return the endTime
		 */
		public Timestamp getEndTime() {
			return endTime;
		}
		/**
		 * @param endTime the endTime to set
		 */
		public void setEndTime(Timestamp endTime) {
			this.endTime = endTime;
		}
		/**
		 * @return the isBusinesstrip
		 */
		public boolean isBusinesstrip() {
			return isBusinesstrip;
		}
		/**
		 * @param isBusinesstrip the isBusinesstrip to set
		 */
		public void setBusinesstrip(boolean isBusinesstrip) {
			this.isBusinesstrip = isBusinesstrip;
		}
		/**
		 * @return the isCompassionateLeave
		 */
		public boolean isCompassionateLeave() {
			return isCompassionateLeave;
		}
		/**
		 * @param isCompassionateLeave the isCompassionateLeave to set
		 */
		public void setCompassionateLeave(boolean isCompassionateLeave) {
			this.isCompassionateLeave = isCompassionateLeave;
		}
		/**
		 * @return the isLeave
		 */
		public boolean isLeave() {
			return isLeave;
		}
		/**
		 * @param isLeave the isLeave to set
		 */
		public void setLeave(boolean isLeave) {
			this.isLeave = isLeave;
		}
		/**
		 * @return the isLegalFestival
		 */
		public boolean isLegalFestival() {
			return isLegalFestival;
		}
		/**
		 * @param isLegalFestival the isLegalFestival to set
		 */
		public void setLegalFestival(boolean isLegalFestival) {
			this.isLegalFestival = isLegalFestival;
		}
		/**
		 * @return the isSickLeave
		 */
		public boolean isSickLeave() {
			return isSickLeave;
		}
		/**
		 * @param isSickLeave the isSickLeave to set
		 */
		public void setSickLeave(boolean isSickLeave) {
			this.isSickLeave = isSickLeave;
		}
		/**
		 * @return the leavePostDays
		 */
		public double getLeavePostDays() {
			return leavePostDays;
		}
		/**
		 * @param leavePostDays the leavePostDays to set
		 */
		public void setLeavePostDays(double leavePostDays) {
			this.leavePostDays = leavePostDays;
		}
		/**
		 * @return the leavePostType
		 */
		public String getLeavePostType() {
			return leavePostType;
		}
		/**
		 * @param leavePostType the leavePostType to set
		 */
		public void setLeavePostType(String leavePostType) {
			this.leavePostType = leavePostType;
		}
		/**
		 * @return the timetable
		 */
		public Timetable getTimetable() {
			return timetable;
		}
		/**
		 * @param timetable the timetable to set
		 */
		public void setTimetable(Timetable timetable) {
			this.timetable = timetable;
		}
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
	 * @return the timetableServiceListenerNames
	 */
	public String getTimetableServiceListenerNames() {
		return timetableServiceListenerNames;
	}

	/**
	 * @param timetableServiceListenerNames the timetableServiceListenerNames to set
	 */
	public void setTimetableServiceListenerNames(
			String timetableServiceListenerNames) {
		this.timetableServiceListenerNames = timetableServiceListenerNames;
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
}