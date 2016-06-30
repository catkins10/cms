package com.yuanluesoft.appraise.service.spring;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.appraiser.pojo.Appraiser;
import com.yuanluesoft.appraise.appraiser.pojo.AppraiserImportTask;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.appraise.model.AppraiseVoteTotal;
import com.yuanluesoft.appraise.model.ParticipateUnit;
import com.yuanluesoft.appraise.pojo.Appraise;
import com.yuanluesoft.appraise.pojo.AppraiseOption;
import com.yuanluesoft.appraise.pojo.AppraiseOptionVote;
import com.yuanluesoft.appraise.pojo.AppraiseParameter;
import com.yuanluesoft.appraise.pojo.AppraiseParticipateUnit;
import com.yuanluesoft.appraise.pojo.AppraiseSms;
import com.yuanluesoft.appraise.pojo.AppraiseTask;
import com.yuanluesoft.appraise.pojo.AppraiseVote;
import com.yuanluesoft.appraise.pojo.UnitAppraise;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPoolException;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseServiceImpl extends BusinessServiceImpl implements AppraiseService {
	private SmsService smsService; //短信服务
	private AppraiserService appraiserService; //评议员服务
	private PageService pageService; //页面服务
	private OrgService orgService; //组织机构服务
	private RecordControlService recordControlService; //记录控制服务
	private boolean importRecipientInternetAppraise = false; //基础库评议员评议时,是否引用服务对象做的网络评议结果
	private String smsCarrierNumbers = "134,135,136,137,138,139,147,150,151,152,157,158,159,182,183,184,187,188"; //短信运营商手机号码段,默认为中国移动
	private boolean appraiseByCodeOnly = false; //网上评议时,是否只按验证码来验证,不需要手机号码
	private int appraiseCodeLength = 6; //验证码长度
	private Cache appraiseCache; //投票数据缓存
	
	//私有属性
	private ThreadPool threadPool; //线程池,发送邀请短信用
	private CountingThread countingThread; //计票线程
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		smsService.registSmsBusiness("民主评议", false, false); //注册短信业务
		threadPool = new ThreadPool(3, 1, 0); //允许同时启动3个评议
		//启动计票线程
		countingThread = new CountingThread();
		countingThread.start();
	}
	
	/**
	 * 销毁
	 *
	 */
	public void destory() {
		try {
			countingThread.interrupt(); //销毁计票线程
		}
		catch(Exception e) {
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof AppraiseParticipateUnit) { //参评单位
			rebuildStaticPageForParticipateUnit((AppraiseParticipateUnit)record);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof AppraiseParticipateUnit) { //参评单位
			rebuildStaticPageForParticipateUnit((AppraiseParticipateUnit)record);
		}
		else if(record instanceof Appraise) { //评议
			pageService.rebuildStaticPageForModifiedObject((Appraise)record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof AppraiseParticipateUnit) { //参评单位
			rebuildStaticPageForParticipateUnit((AppraiseParticipateUnit)record);
		}
	}
	
	/**
	 * 为参评单位重建静态页面
	 * @param participateUnit
	 * @throws ServiceException
	 */
	private void rebuildStaticPageForParticipateUnit(AppraiseParticipateUnit participateUnit) throws ServiceException {
		WebDirectory webDirectory = new WebDirectory();
		webDirectory.setId(participateUnit.getId());
		pageService.rebuildStaticPageForModifiedObject(webDirectory, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#startupAppraise(java.lang.String, long, int, int, java.sql.Timestamp, boolean, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Appraise startupAppraise(String name, long taskId, int year, int month, Timestamp endTime, boolean monthOnceOnly, boolean cancelNoAppraisersUnit, final SessionInfo sessionInfo) throws ServiceException {
		if(monthOnceOnly) { //一个月只允许评议一次
			//检查当月是否已经评议过
			String hql = "select Appraise.id" +
						 " from Appraise Appraise" +
						 " where Appraise.taskId=" + taskId +
						 " and Appraise.appraiseYear=" + year +
						 " and Appraise.appraiseMonth=" + month;
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				throw new ServiceException(year + "年" + month + "月评议已经发起，不允许再次发起评议。");
			}
		}
		final AppraiseTask task = (AppraiseTask)load(AppraiseTask.class, taskId); //获取评测任务
		if(Logger.isInfoEnabled()) {
			Logger.info("AppraiseService: startup a task, area is " + task.getArea() + ", name is " + task.getName() + ", id is " + taskId + ".");
		}
		if(task.getOptions()==null || task.getOptions().isEmpty()) {
			throw new ServiceException("评议选项未配置");
		}
		List participateUnits;
		if(task.getIsSpecial()!=1) { //非专题评议
			participateUnits = listParticipateUnits(task.getAreaId(), year); //获取参评单位列表
		}
		else {
			participateUnits = new ArrayList();
			String[] unitIds = task.getSpecialUnitIds().split(",");
			String[] unitNames = task.getSpecialUnitNames().split(",");
			for(int i=0; i<unitIds.length; i++) {
				participateUnits.add(new ParticipateUnit(task.getAreaId(), Long.parseLong(unitIds[i]), unitNames[i], null));
			}
		}
		if(participateUnits==null || participateUnits.isEmpty()) {
			throw new ServiceException("没有有效的参评单位");
		}

		if((task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) || task.getDelegateAttend()!=0) { //非专题评议,且评议员类型是基本库评议员,或者评议代表参与投票
			if(getAppraiseUnitId(task.getAreaId())<=0) { //获取纠风办ID
				throw new ServiceException(task.getArea() + "未注册纠风办");
			}
			cancelNoAppraisersUnit = false; //禁止放弃评议
		}
		
		//检查评议员是否为空
		List noAppraisersUnits =  validateNoAppraisersUnits(task, participateUnits, year, month);
		
		if(noAppraisersUnits!=null && !noAppraisersUnits.isEmpty()) {
			if(noAppraisersUnits.size()==participateUnits.size()) {
				throw new ServiceException("所有参评单位都没有有效的评议员。");
			}
			if(!cancelNoAppraisersUnit) { //需要评议没有评议员的单位
				throw new ServiceException(ListUtils.join(noAppraisersUnits, "unitName", "、", false) + "没有有效的评议员。");
			}
		}
		
		//创建评议
		final Appraise appraise = new Appraise();
		appraise.setId(UUIDLongGenerator.generateId()); //ID
		appraise.setName(name); //名称
		appraise.setTaskId(taskId); //评议任务ID
		appraise.setTaskName(task.getName()); //评议任务名称
		appraise.setIsSpecial(task.getIsSpecial()); //是否专题评议
		appraise.setAppraiserType(task.getAppraiserType()); //评议员类型,0/基础,1/管理服务对象
		appraise.setAreaId(task.getAreaId()); //地区ID
		appraise.setArea(task.getArea()); //地区名称
		appraise.setAppraiseYear(year); //评议年度
		appraise.setAppraiseMonth(month); //评议月份
		appraise.setCreatorId(sessionInfo==null ? 0 : sessionInfo.getUserId()); //发起人ID
		appraise.setCreator(sessionInfo==null ? "评议系统" : sessionInfo.getUserName()); //发起人
		appraise.setCreated(DateTimeUtils.now()); //发起时间
		appraise.setEndTime(endTime!=null ? endTime : DateTimeUtils.add(appraise.getCreated(), Calendar.DAY_OF_MONTH, task.getAppraiseDays())); //截止时间
		save(appraise);
		
		Timestamp lastAppraiseTime = null; //同年上一次的专家评议时间
		if(importRecipientInternetAppraise && task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) { //非专题评议,评议员类型是基本库评议员
			//获取同年上一次的专家评议时间
			String hql = "select Appraise.created" +
				  		 " from Appraise Appraise" +
				  		 " where Appraise.areaId=" + task.getAreaId() +
				  		 " and Appraise.isSpecial!=1" + //不是专题评议
				  		 " and Appraise.appraiserType=" + AppraiserService.APPRAISER_TYPE_BASIC + //评议员类型是基础库评议员
				  		 " and Appraise.appraiseYear=" + year +
				  		 " and Appraise.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(appraise.getCreated(), null) + ")" +
				  		 " order by Appraise.created DESC";
			lastAppraiseTime = (Timestamp)getDatabaseService().findRecordByHql(hql);
		}
		
		//保存单位评议列表
		appraise.setUnitAppraises(new LinkedHashSet()); //单位评议列表
		for(Iterator iterator = participateUnits.iterator(); iterator.hasNext();) {
			ParticipateUnit participateUnit = (ParticipateUnit)iterator.next();
			UnitAppraise unitAppraise = new UnitAppraise();
			unitAppraise.setId(UUIDLongGenerator.generateId()); //ID
			unitAppraise.setAppraiseId(appraise.getId()); //评议ID
			unitAppraise.setUnitId(participateUnit.getUnitId()); //单位ID
			unitAppraise.setUnitName(participateUnit.getUnitName()); //单位名称
			unitAppraise.setUnitCategory(participateUnit.getCategory()); //单位分类
			unitAppraise.setVoteTotal(0); //总投票数
			unitAppraise.setSmsVoteTotal(0); //短信投票数
			unitAppraise.setInternetVoteTotal(0); //网络投票数
			unitAppraise.setScoreComprehensive(0); //综合得分
			unitAppraise.setCreated(DateTimeUtils.now()); //发起时间
			//获取上一次评议
			String hql = "select UnitAppraise" +
		  		  		 " from UnitAppraise UnitAppraise, Appraise Appraise" +
		  		  		 " where UnitAppraise.appraiseId=Appraise.id" +
		  		  		 " and Appraise.taskId=" + taskId + //任务相同
		  		  		 " and Appraise.appraiseYear=" + year + //年度相同
		  		  		 " and UnitAppraise.unitId=" + participateUnit.getUnitId() +
		  		  		 " order by Appraise.created DESC";
			UnitAppraise lastUnitAppraise = (UnitAppraise)getDatabaseService().findRecordByHql(hql);
			if(lastUnitAppraise!=null) {
				unitAppraise.setYearVoteTotal(lastUnitAppraise.getYearVoteTotal()); //累计投票数,截止到本期
				unitAppraise.setYearSmsVoteTotal(lastUnitAppraise.getYearSmsVoteTotal()); //累计短信投票数,截止到本期
				unitAppraise.setYearSmsScoreTotal(lastUnitAppraise.getYearSmsScoreTotal()); //累计短信投票得分,截止到本期
				unitAppraise.setYearInternetVoteTotal(lastUnitAppraise.getYearInternetVoteTotal()); //累计网络投票数,截止到本期
				unitAppraise.setYearInternetScoreTotal(lastUnitAppraise.getYearInternetScoreTotal()); //累计网络投票得分,截止到本期
			}
			if(importRecipientInternetAppraise && task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) { //非专题评议,评议员类型是基本库评议员
				//引用服务对象网络评议结果
				hql = "select count(AppraiseVote.id), sum(AppraiseVote.score)" +
					  " from AppraiseVote AppraiseVote, UnitAppraise UnitAppraise, Appraise Appraise" +
					  " where AppraiseVote.unitAppraiseId=UnitAppraise.id" +
					  " and UnitAppraise.appraiseId=Appraise.id" +
					  " and Appraise.isSpecial!=1" + //不是专题评议
					  " and Appraise.appraiserType=" + AppraiserService.APPRAISER_TYPE_RECIPIENT + //评议员类型是服务对象
					  " and UnitAppraise.unitId=" + participateUnit.getUnitId() +
					  " and AppraiseVote.voteMode=1" + //网络投票
					  " and Appraise.appraiseYear=" + year +
					  (lastAppraiseTime==null ? "" : " and AppraiseVote.created>TIMESTAMP(" + DateTimeUtils.formatTimestamp(lastAppraiseTime, null) + ")");
				Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
				if(values!=null) {
					unitAppraise.setImportVoteTotal(values[0]==null ? 0 : ((Number)values[0]).intValue()); //引用投票数,专家评议时,计入服务对象网络投票结果
					unitAppraise.setImportScoreTotal(values[1]==null ? 0 : ((Number)values[1]).doubleValue()); //引用投票得分
					unitAppraise.setVoteTotal(unitAppraise.getImportVoteTotal()); //总投票数
					unitAppraise.setInternetVoteTotal(unitAppraise.getImportVoteTotal()); //网络投票数
					unitAppraise.setInternetScoreTotal(unitAppraise.getImportScoreTotal()); //网络投票得分
					unitAppraise.setYearVoteTotal(unitAppraise.getYearVoteTotal() + unitAppraise.getImportVoteTotal()); //累计投票数,截止到本期
					unitAppraise.setYearInternetVoteTotal(unitAppraise.getYearInternetVoteTotal() + unitAppraise.getImportVoteTotal()); //累计网络投票数,截止到本期
					unitAppraise.setYearInternetScoreTotal(unitAppraise.getYearInternetScoreTotal() + unitAppraise.getImportScoreTotal()); //累计网络投票得分,截止到本期
				}
			}
			//重新计算综合得分
			unitAppraise.setScoreComprehensive(computeScoreComprehensive(task, unitAppraise.getSmsVoteTotal(), unitAppraise.getSmsScoreTotal(), unitAppraise.getInternetVoteTotal(), unitAppraise.getInternetScoreTotal())); //累计综合得分,截止到本期
			unitAppraise.setYearScoreComprehensive(computeScoreComprehensive(task, unitAppraise.getYearSmsVoteTotal(), unitAppraise.getYearSmsScoreTotal(), unitAppraise.getYearInternetVoteTotal(), unitAppraise.getYearInternetScoreTotal())); //累计综合得分,截止到本期
			//保存单位评议
			getDatabaseService().saveRecord(unitAppraise);
			appraise.getUnitAppraises().add(unitAppraise); //添加到列表
			unitAppraise.setOptionVotes(new LinkedHashSet());
			
			//创建选项投票记录
			for(Iterator iteratorOption = task.getOptions().iterator(); iteratorOption.hasNext();) {
				AppraiseOption appraiseOption = (AppraiseOption)iteratorOption.next();
				if(task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC && appraiseOption.getType()==0) { //基础库,短信投票
					continue;
				}
				AppraiseOptionVote optionVote = new AppraiseOptionVote();
				optionVote.setId(UUIDLongGenerator.generateId()); //ID
				optionVote.setUnitAppraiseId(unitAppraise.getId()); //单位评议ID
				optionVote.setOptionId(appraiseOption.getId()); //选项ID
				optionVote.setOption(appraiseOption.getOption()); //选项名称
				optionVote.setOptionType(appraiseOption.getType()); //选项类型
				optionVote.setSmsOption(appraiseOption.getSmsOption()); //短信选项
				optionVote.setAbstain(appraiseOption.getAbstain()); //是否弃权
				optionVote.setOptionScore(appraiseOption.getScore()); //选项分值
				optionVote.setVoteTotal(0); //投票数
				optionVote.setSmsVoteTotal(0); //短信投票数
				optionVote.setInternetVoteTotal(0); //网络投票数
				optionVote.setScore(0); //累计分数
				getDatabaseService().saveRecord(optionVote);
				unitAppraise.getOptionVotes().add(optionVote);
			}
		}

		//给评议员发送短信
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					sendAppraiseSms(task, appraise, false, sessionInfo);
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		}, 10);
		
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(appraise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return appraise;
	}
	
	/**
	 * 计算综合得分
	 * @param task
	 * @param smsVoteTotal 短信投票数
	 * @param smsScoreTotal 短信投票得分
	 * @param internetVoteTotal 网络投票数
	 * @param internetScoreTotal 网络投票得分
	 * @return
	 */
	private double computeScoreComprehensive(AppraiseTask task, int smsVoteTotal, double smsScoreTotal, int internetVoteTotal, double internetScoreTotal) {
		double score = (smsVoteTotal<=0 ? 0 : smsScoreTotal / smsVoteTotal * (task.getSmsRatio() / 100.0)) +
					   (internetVoteTotal<=0 ? 0 : internetScoreTotal / internetVoteTotal * (task.getInternetRatio() / 100.0));
		if(score<=0) {
			return 0;
		}
		//获取最大分值
		double maxOptionScore = -1; //选项的获取最大分值
		for(Iterator iteratorOption = task.getOptions().iterator(); iteratorOption.hasNext();) {
			AppraiseOption appraiseOption = (AppraiseOption)iteratorOption.next();
			if(appraiseOption.getScore()>maxOptionScore) {
				maxOptionScore = appraiseOption.getScore();
			}
		}
		return Math.round(score / maxOptionScore * 10000) / 100.0;
	}
	
	/**
	 * 检查是否有有效的评议员
	 * @param task
	 * @param participateUnits
	 * @param year
	 * @param month
	 * @throws ServiceException
	 */
	private List validateNoAppraisersUnits(AppraiseTask task, List participateUnits, int year, int month) throws ServiceException {
		if(task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) { //非专题评议,且评议员类型是基本库评议员
			//检查是否有有效的基本库评议员
			List appraisers = appraiserService.listOrgAppraisers(task.getAreaId(), year, month, task.getAppraiserType(), task.getAppraiserJobs(), 0, 1);
			if(appraisers==null || appraisers.isEmpty()) {
				return ListUtils.generateList(new ParticipateUnit(task.getAreaId(), task.getAreaId(), task.getArea(), null));
			}
			return null;
		}
		List noAppraisersUnits = new ArrayList();
		for(Iterator iterator = participateUnits.iterator(); iterator.hasNext();) {
			ParticipateUnit participateUnit = (ParticipateUnit)iterator.next();
			List appraisers = null;
			Org org;
			if(task.getIsSpecial()!=1) { //非专题评议
				appraisers = appraiserService.listOrgAppraisers(participateUnit.getUnitId(), year, month, task.getAppraiserType(), task.getRecipientJobs(), 0, 1);
			}
			else if((org=orgService.getOrg(participateUnit.getUnitId()))==null) {
				throw new ServiceException(participateUnit.getUnitName() + "已经被删除");
			}
			else if(task.getAppraiserType()!=2) { //不是全部类型的评议员
				appraisers = appraiserService.listOrgAppraisers(org.getParentDirectoryId(), year, month, task.getAppraiserType(), AppraiserService.APPRAISER_TYPE_BASIC==task.getAppraiserType() ? task.getAppraiserJobs() : task.getRecipientJobs(), 0, 1);
			}
			else { //全部评议员类型
				appraisers = appraiserService.listOrgAppraisers(org.getParentDirectoryId(), year, month, AppraiserService.APPRAISER_TYPE_BASIC, task.getAppraiserJobs(), 0, 1);
				if(appraisers==null || appraisers.isEmpty()) {
					appraisers = appraiserService.listOrgAppraisers(org.getParentDirectoryId(), year, month, AppraiserService.APPRAISER_TYPE_RECIPIENT, task.getRecipientJobs(), 0, 1);
				}
			}
			if(appraisers==null || appraisers.isEmpty()) {
				noAppraisersUnits.add(participateUnit);
			}
		}
		return noAppraisersUnits;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#supplySendAppraiseSms(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void supplySendAppraiseSms(long appraiseId, SessionInfo sessionInfo) throws ServiceException {
		Appraise appraise = (Appraise)load(Appraise.class, appraiseId);
		AppraiseTask task = (AppraiseTask)load(AppraiseTask.class, appraise.getTaskId());
		sendAppraiseSms(task, appraise, true, sessionInfo);
	}
	
	/**
	 * 发送评议短信
	 * @param task
	 * @param appraise
	 * @param supplySend 是否补发
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void sendAppraiseSms(final AppraiseTask task, final Appraise appraise, final boolean supplySend, final SessionInfo sessionInfo) throws ServiceException {
		try {
			threadPool.execute(new Task() {
				public void process() {
					try {
						doSendAppraiseSms(task, appraise, supplySend, sessionInfo);
					}
					catch (ServiceException e) {
						Logger.exception(e);
					}						
				}
			});
		}
		catch (ThreadPoolException e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 执行发送评议短信
	 * @param task
	 * @param appraise
	 * @param supplySend
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void doSendAppraiseSms(AppraiseTask task, Appraise appraise, boolean supplySend, SessionInfo sessionInfo) throws ServiceException {
		long appraiseUnitId = 0;
		if((task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) ||  //非专题评议,且评议员类型是基本库评议员
			task.getDelegateAttend()!=0) { //评议代表参与评议
			appraiseUnitId = getAppraiseUnitId(task.getAreaId());
			if(appraiseUnitId<=0) { //获取纠风办ID
				throw new ServiceException(task.getArea() + "未注册纠风办");
			}
		}
		if(task.getIsSpecial()!=1 && task.getAppraiserType()==AppraiserService.APPRAISER_TYPE_BASIC) { //非专题评议,且评议员类型是基本库评议员
			sendAppraiseSmsToOrg(task, appraise, supplySend, task.getAreaId(), appraiseUnitId, null, task.getAppraiserType(), sessionInfo);
		}
		else {
			for(Iterator iterator = appraise.getUnitAppraises().iterator(); iterator.hasNext();) {
				UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
				long appraiserOrgId = unitAppraise.getUnitId(); //评议员所在组织ID
				if(task.getIsSpecial()==1) { //专题评议
					appraiserOrgId = orgService.getOrg(appraiserOrgId).getParentDirectoryId(); //使用上级组织的评议员
				}
				if(task.getAppraiserType()!=2) { //不是全部
					sendAppraiseSmsToOrg(task, appraise, supplySend, appraiserOrgId, unitAppraise.getUnitId(), unitAppraise.getUnitName(), task.getAppraiserType(), sessionInfo);
				}
				else { //全部
					sendAppraiseSmsToOrg(task, appraise, supplySend, appraiserOrgId, unitAppraise.getUnitId(), unitAppraise.getUnitName(), AppraiserService.APPRAISER_TYPE_BASIC, sessionInfo);
					sendAppraiseSmsToOrg(task, appraise, supplySend, appraiserOrgId, unitAppraise.getUnitId(), unitAppraise.getUnitName(), AppraiserService.APPRAISER_TYPE_RECIPIENT, sessionInfo);
				}
			}
		}
		if(task.getDelegateAttend()!=0) { //评议代表参与评议
			sendAppraiseSmsToOrg(task, appraise, supplySend, task.getAreaId(), appraiseUnitId, null, AppraiserService.APPRAISER_TYPE_DELEGATE, sessionInfo);
		}
	}
	
	/**
	 * 给指定单位发送短信
	 * @param task
	 * @param appraise
	 * @param supplySend
	 * @param appraiserOrgId 评议员所在单位ID
	 * @param participateUnitId 参评单位ID
	 * @param participateUnitName 参评单位名称
	 * @param appraiserType 评议员类型,0/基础库评议员,1/管理服务对象,2/评议代表
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void sendAppraiseSmsToOrg(AppraiseTask task, Appraise appraise, boolean supplySend, long appraiserOrgId, long participateUnitId, String participateUnitName, int appraiserType, SessionInfo sessionInfo) throws ServiceException {
		for(int j=0; ; j+=200) { //每次获取200个评议员
			List appraisers = appraiserService.listOrgAppraisers(appraiserOrgId, appraise.getAppraiseYear(), appraise.getAppraiseMonth(), appraiserType, appraiserType==AppraiserService.APPRAISER_TYPE_BASIC ? task.getAppraiserJobs() : (appraiserType==AppraiserService.APPRAISER_TYPE_RECIPIENT ? task.getRecipientJobs() : null), j, 200);
			for(Iterator iterator = appraisers==null ? null : appraisers.iterator(); iterator!=null && iterator.hasNext();) {
				Appraiser appraiser = (Appraiser)iterator.next();
				if(supplySend) { //补发
					//检查是否已经发送过
					String hql = "select AppraiseSms.id" +
								 " from AppraiseSms AppraiseSms" +
								 " where AppraiseSms.appraiseId=" + appraise.getId() +
								 " and AppraiseSms.appraiserId=" + appraiser.getId() +
								 " and AppraiseSms.appraiseUnitId=" + participateUnitId;
					if(getDatabaseService().findRecordByHql(hql)!=null) {
						continue;
					}
				}
				try {
					sendInviteAppraiseSms(task, appraise, appraiser, participateUnitId, participateUnitName, appraise.getId(), appraise.getAppraiseYear(), appraise.getAppraiseMonth(), sessionInfo); //发送评议短信
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				catch(Error e) {
					e.printStackTrace();
				}
			}
			if(appraisers==null || appraisers.size()<200) {
				break;
			}
		}
	}
	
	/**
	 * 获取纠风办ID
	 * @param areaId
	 * @return
	 */
	private long getAppraiseUnitId(long areaId) {
		String hql = "select Org.id" +
			 		 " from Org Org" +
			 		 " where Org.parentDirectoryId=" + areaId +
			 		 " and Org.directoryName like '%纠风办%'";
		Number appraiseUnitId = (Number)getDatabaseService().findRecordByHql(hql);
		return appraiseUnitId==null ? -1 : appraiseUnitId.longValue();
	}
	
	/**
	 * 发送评议短信
	 * @param task
	 * @param appraiser
	 * @param appraiseId
	 * @param smsUnitId
	 * @param year
	 * @param month
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void sendInviteAppraiseSms(AppraiseTask task, Appraise appraise, Appraiser appraiser, long participateUnitId, String participateUnitName, long appraiseId, int year, int month, SessionInfo sessionInfo) throws ServiceException {
		//生成验证码
		Timestamp now = DateTimeUtils.now();
		String appraiseCode = generateValidateCode(appraiseCodeLength, now);
		if(appraiseCode==null) {
			appraiseCode = generateValidateCode(appraiseCodeLength + 2, now);
		}
		if(appraiseCode==null) {
			throw new ServiceException("验证码生成失败");
		}
		String inviteSms;
		if(appraiser.getType()==AppraiserService.APPRAISER_TYPE_DELEGATE) {
			inviteSms = task.getDelegateInviteSms(); //评议代表邀请短信
		}
		else {
			//检查评议员手机号段,以确认是否和短信运营商一致
			boolean isSameCarrier = true;
			if(smsCarrierNumbers!=null && !smsCarrierNumbers.isEmpty()) {
				String[] carrierNumbers = smsCarrierNumbers.split(",");
				String mobileNumber = appraiser.getMobileNumber().trim();
				int i=0;
				for(; i<carrierNumbers.length && !mobileNumber.startsWith(carrierNumbers[i]); i++);
				isSameCarrier = i<carrierNumbers.length;
			}
			inviteSms = isSameCarrier || task.getOtherCarrierInviteSms()==null || task.getOtherCarrierInviteSms().isEmpty() ? task.getInviteSms() : task.getOtherCarrierInviteSms();
		}
		//构造短信内容
		AppraiseSms appraiseSms = new AppraiseSms();
		appraiseSms.setId(UUIDLongGenerator.generateId()); //ID
		appraiseSms.setAppraiseId(appraiseId); //评议ID
		appraiseSms.setAppraiserId(appraiser.getId()); //评议员ID
		appraiseSms.setAppraiserOrgId(appraiser.getOrgId()); //评议员所在组织ID
		appraiseSms.setAppraiser(appraiser.getName()); //评议员姓名
		appraiseSms.setAppraiserNumber(appraiser.getMobileNumber()); //评议员手机号码
		appraiseSms.setAppraiserType(appraiser.getType()); //评议员类型
		appraiseSms.setAppraiseUnitId(participateUnitId); //被评议单位ID
		appraiseSms.setAppraiseCode(appraiseCode); //评议验证码,网上评议时使用
		appraiseSms.setInviteSms(generateSmsContent(inviteSms, appraiser.getName(), participateUnitName, appraiseCode, task.getOptions(), appraise.getEndTime())); //邀请短信内容
		appraiseSms.setInviteSmsSent(DateTimeUtils.now()); //邀请短信发送时间
		appraiseSms.setReplySms(null); //回复内容
		appraiseSms.setReplyTime(null); //回复时间
		appraiseSms.setApplauseSms(null); //答谢短信内容
		appraiseSms.setApplauseSmsSent(null); //答谢短信发送时间
		//发送短信
		smsService.sendShortMessage(sessionInfo==null ? 0 : sessionInfo.getUserId(), sessionInfo==null ? "评议系统" : sessionInfo.getUserName(), participateUnitId, "民主评议", null, appraiser.getMobileNumber(), appraiseSms.getInviteSms(), null, appraiseId, false, null, false);
		//保存短信发送记录
		getDatabaseService().saveRecord(appraiseSms);
	}
	
	/**
	 * 生成验证码
	 * @param codeLength
	 * @param time
	 * @return
	 */
	private String generateValidateCode(int codeLength, Timestamp time) {
		Random random = new Random();
		int maxValue = 1;
		for(int i=0; i<codeLength; i++) {
			maxValue *= 10;
		}
		for(int i=0; i<1000; i++) {
			String appraiseCode = StringUtils.formatNumber(random.nextInt(maxValue), codeLength, true); //6位数字
			//检查是否已经使用过
			String hql = "select AppraiseSms.id" +
						 " from AppraiseSms AppraiseSms, Appraise Appraise" +
						 " where AppraiseSms.appraiseId=Appraise.id" +
						 " and AppraiseSms.appraiseCode='" + appraiseCode + "'" +
						 " and Appraise.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(time, null) + ")";
			if(getDatabaseService().findRecordByHql(hql)==null) {
				return appraiseCode;
			}
		}
		return null;
	}
	
	/**
	 * 生成短信内容
	 * @param smsContent
	 * @param appraiserName
	 * @param participateUnitName
	 * @param appraiseCode
	 * @param options
	 * @param endTime
	 * @return
	 */
	private String generateSmsContent(String smsContent, String appraiserName, String participateUnitName, String appraiseCode, Set options, Timestamp endTime) {
		String optionsText = null;
		for(Iterator iterator = options==null ? null : options.iterator(); iterator!=null && iterator.hasNext();) {
			AppraiseOption option = (AppraiseOption)iterator.next();
			if(option.getType()==0) { //短信选项
				optionsText = (optionsText==null ? "" : optionsText + "、") + option.getSmsOption() + ":" + option.getOption();
			}
		}
		return smsContent.replaceAll("<评议员姓名>", appraiserName==null ? "" : appraiserName)
						 .replaceAll("<参评单位名称>", participateUnitName==null ? "" : participateUnitName)
						 .replaceAll("<评议验证码>", appraiseCode==null ? "" : appraiseCode)
						 .replaceAll("<评议选项列表>", optionsText==null ? "" : optionsText)
						 .replaceAll("<WAP评议链接>", Environment.getContextPath() + "/appraise/wapAppraise.shtml?appraiseCode=" + appraiseCode + " ")
						 .replaceAll("<截止时间>", endTime==null ? "" : DateTimeUtils.formatTimestamp(endTime, "M月d日"));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#getAppraiseParameter()
	 */
	public AppraiseParameter getAppraiseParameter() throws ServiceException {
		return (AppraiseParameter)getDatabaseService().findRecordByHql("from AppraiseParameter AppraiseParameter");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#listParticipateUnits(long, int)
	 */
	public List listParticipateUnits(long areaId, int year) throws ServiceException {
		String hql = "from AppraiseParticipateUnit AppraiseParticipateUnit" +
					 " where AppraiseParticipateUnit.areaId=" + areaId +
					 " and AppraiseParticipateUnit.year=" + year +
					 " order by AppraiseParticipateUnit.id";
		List participateUnitConfigs = getDatabaseService().findRecordsByHql(hql);
		if(participateUnitConfigs==null || participateUnitConfigs.isEmpty()) {
			return null;
		}
		List participateUnits = new ArrayList();
		for(Iterator iterator = participateUnitConfigs.iterator(); iterator.hasNext();) {
			AppraiseParticipateUnit participateUnitConfig = (AppraiseParticipateUnit)iterator.next();
			if(participateUnitConfig.getUnitIds()==null || participateUnitConfig.getUnitIds().isEmpty()) {
				continue;
			}
			String[] unitIds = participateUnitConfig.getUnitIds().split(",");
			String[] unitNames = participateUnitConfig.getUnitNames().split(",");
			for(int i=0; i<unitIds.length; i++) {
				if(ListUtils.findObjectByProperty(participateUnits, "unitId", new Long(unitIds[i]))!=null) {
					continue;
				}
				participateUnits.add(new ParticipateUnit(areaId, Long.parseLong(unitIds[i]), unitNames[i], participateUnitConfig.getCategory()));
			}
		}
		return participateUnits;
	}
	
	/**
	 * 发送答谢短信
	 * @param appraise
	 * @param appraiseSms
	 * @throws ServiceException
	 */
	private void sendApplauseSms(Appraise appraise, AppraiseSms appraiseSms) throws ServiceException {
		if(appraiseSms.getApplauseSmsSent()!=null || appraiseSms.getAppraiserType()==AppraiserService.APPRAISER_TYPE_DELEGATE) { //已经发送过,或者评议代表
			return;
		}
		//获取答谢短信格式
		String hql = "from AppraiseTask AppraiseTask" +
					 " where AppraiseTask.id=(select Appraise.taskId from Appraise Appraise where Appraise.id=" + appraiseSms.getAppraiseId() + ")";
		AppraiseTask appraiseTask = (AppraiseTask)getDatabaseService().findRecordByHql(hql);
		if(appraiseTask==null || appraiseTask.getApplauseSms()==null || appraiseTask.getApplauseSms().isEmpty()) {
			return;
		}
		String applauseSms = generateSmsContent(appraiseTask.getApplauseSms(), appraiseSms.getAppraiser(), null, appraiseSms.getAppraiseCode(), null, appraise.getEndTime());
		appraiseSms.setApplauseSms(applauseSms);
		appraiseSms.setApplauseSmsSent(DateTimeUtils.now());
		getDatabaseService().updateRecord(appraiseSms);
		smsService.sendShortMessage(0, "评议系统", appraiseSms.getAppraiseUnitId(), "民主评议", null, appraiseSms.getAppraiserNumber(), applauseSms, null, appraiseSms.getAppraiseId(), false, null, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#loadAppraiseByCode(java.lang.String, java.lang.String, boolean)
	 */
	public Appraise loadAppraiseByCode(String appraiserNumber, String appraiseCode, boolean forWapAppraise) throws ServiceException {
		//获取短信发送记录
		AppraiseSms appraiseSms = loadAppraiseSms(appraiserNumber, appraiseCode, forWapAppraise);
		if(appraiseSms==null) {
			return null;
		}
		//获取评议
		Appraise appraise = (Appraise)load(Appraise.class, appraiseSms.getAppraiseId());
		if(appraiseSms.getAppraiserType()==AppraiserService.APPRAISER_TYPE_DELEGATE) { //评议代表
			//获取评测任务
			AppraiseTask task = (AppraiseTask)load(AppraiseTask.class, appraise.getTaskId());
			//删除短信投票选项或者网络投票选项
			for(Iterator iterator = appraise.getUnitAppraises().iterator(); iterator.hasNext();) {
				UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
				if(ListUtils.findObjectByProperty(unitAppraise.getOptionVotes(), "optionType", new Integer(task.getDelegateAttend()==1 ? 0 : 1))!=null) {
					ListUtils.removeObjectsByProperty(unitAppraise.getOptionVotes(), "optionType", new Integer(task.getDelegateAttend()==1 ? 1 : 0));
				}
			}
		}
		else if(forWapAppraise || //WAP评议
		   appraise.getIsSpecial()==1 || //或者专题评议
		   (appraise.getAppraiserType()==AppraiserService.APPRAISER_TYPE_RECIPIENT && appraiseSms.getReplyTime()==null)) { //或者评议的评议员类型是服务对象,且短信没有答复过
			//只保留被评议单位的评议记录
			UnitAppraise unitAppraise = (UnitAppraise)ListUtils.findObjectByProperty(appraise.getUnitAppraises(), "unitId", new Long(appraiseSms.getAppraiseUnitId()));
			appraise.setUnitAppraises(new HashSet());
			appraise.getUnitAppraises().add(unitAppraise);
			//删除网络投票选项
			ListUtils.removeObjectsByProperty(unitAppraise.getOptionVotes(), "optionType", new Integer(1));
		}
		else if(appraise.getAppraiserType()==AppraiserService.APPRAISER_TYPE_RECIPIENT) { //不是WAP评议,也不是专题评议,评议的评议员类型是服务对象
			if(appraiseSms.getApplauseSms()==null || appraiseSms.getApplauseSms().indexOf(appraiseCode)==-1) { //答谢短信中没有评议验证码,不需要网络评议
				appraise.setUnitAppraises(null); //清空评议记录
			}
			else {
				//剔除被评议单位的评议记录
				ListUtils.removeObjectByProperty(appraise.getUnitAppraises(), "unitId", new Long(appraiseSms.getAppraiseUnitId()));
				//删除短信投票选项
				for(Iterator iterator = appraise.getUnitAppraises().iterator(); iterator.hasNext();) {
					UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
					if(ListUtils.findObjectByProperty(unitAppraise.getOptionVotes(), "optionType", new Integer(1))!=null) { //有网络投票选项
						ListUtils.removeObjectsByProperty(unitAppraise.getOptionVotes(), "optionType", new Integer(0));
					}
				}
			}
		}
		return appraise;
	}

	/**
	 * 获取短信发送记录
	 * @param appraiserNumber
	 * @param appraiseCode
	 * @param wapAppraise
	 * @return
	 * @throws ServiceException
	 */
	private AppraiseSms loadAppraiseSms(String  appraiserNumber, String appraiseCode, boolean wapAppraise) throws ServiceException {
		String hql = "select AppraiseSms" +
					 " from AppraiseSms AppraiseSms, Appraise Appraise" +
					 " where AppraiseSms.appraiseId=Appraise.id" +
					 " and AppraiseSms.appraiseCode='" + appraiseCode + "'" +
					 (appraiseByCodeOnly || wapAppraise ? "" : " and AppraiseSms.appraiserNumber='" + appraiserNumber + "'") +
					 " and Appraise.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")";
		return (AppraiseSms)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#sendAppraiseUnitSms(long, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void sendAppraiseUnitSms(long areaId, String unitIds, String message, SessionInfo sessionInfo) throws ServiceException {
		//获取纠风办ID
		long smsUnitId = getAppraiseUnitId(areaId);
		String[] ids = unitIds.split(",");
		for(int i=0; i<ids.length; i++) {
			//获取各单位的评议责任人
			List appraiseTransactors = orgService.listDirectoryVisitors(Long.parseLong(ids[i]), "appraiseTransactor", true, true, 5);
			smsService.sendShortMessage(sessionInfo==null ? 0 : sessionInfo.getUserId(), sessionInfo==null ? "评议系统" : sessionInfo.getUnitName(), smsUnitId, "民主评议", ListUtils.join(appraiseTransactors, "id", ",", false), null, message, null, -1, false, null, true);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#appraiseVoteTotal(long, boolean)
	 */
	public AppraiseVoteTotal appraiseVoteTotal(long areaId, boolean currentYearOnly) throws ServiceException {
		AppraiseVoteTotal appraiseVoteTotal = new AppraiseVoteTotal();
		String hql = "select sum(UnitAppraise.voteTotal), sum(UnitAppraise.smsVoteTotal), sum(UnitAppraise.internetVoteTotal)" +
					 " from UnitAppraise UnitAppraise, Appraise Appraise" +
					 " where UnitAppraise.appraiseId=Appraise.id" +
					 " and Appraise.areaId=" + areaId +
					 (currentYearOnly ? " and Appraise.appraiseYear=" + DateTimeUtils.getYear(DateTimeUtils.date()) : "");
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		if(values!=null) {
			appraiseVoteTotal.setVoteTotal(values[0]==null ? 0 : ((Number)values[0]).intValue()); //总投票数
			appraiseVoteTotal.setSmsVoteTotal(values[1]==null ? 0 : ((Number)values[1]).intValue()); //短信投票数
			appraiseVoteTotal.setInternetVoteTotal(values[2]==null ? 0 : ((Number)values[2]).intValue()); //网络投票数
		}
		//统计投票人次
		hql = "select sum(Appraise.voteTimes)" +
			  " from Appraise Appraise" +
			  " where Appraise.areaId=" + areaId +
			  (currentYearOnly ? " and Appraise.appraiseYear=" + DateTimeUtils.getYear(DateTimeUtils.date()) : "");
		Number times = (Number)getDatabaseService().findRecordByHql(hql);
		appraiseVoteTotal.setVoteTimes(times==null ? 0 : times.intValue());
		return appraiseVoteTotal;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#schedule()
	 */
	public void schedule() throws ServiceException {
		Timestamp now = DateTimeUtils.now();
		final Date monthBegin = DateTimeUtils.getMonthBegin();
		final Date nextMonthBegin = DateTimeUtils.getNextMonthBegin();
		String checkBegin = DateTimeUtils.formatTimestamp(DateTimeUtils.add(now, Calendar.MINUTE, -10), "HH:mm"); //检查10分钟内的任务,注:时间不能设置为23:50~23:59
		String checkEnd = DateTimeUtils.formatTimestamp(now, "HH:mm");
		int day = DateTimeUtils.getDay(now);
		final int month = DateTimeUtils.getMonth(now) + 1;
		final int year = DateTimeUtils.getYear(now);
		
		//检查是否有需要执行的评议任务
		String hql = "select AppraiseTask" +
			  		 " from AppraiseTask AppraiseTask" +
			  		 " where AppraiseTask.enabled=1" + //已启用
			  		 " and AppraiseTask.appraiseMode=1" + //评议发起方式为自动
			  		 " and AppraiseTask.appraiseMonths like '%," + month + ",%'" + //评议月份匹配
			  		 " and AppraiseTask.appraiseBeginDay<=" + day + //评议开始日期 <= 当前日期
			  		 " and AppraiseTask.appraiseTime>'" + checkBegin + "'" + //评议发起时间 > 检查开始时间
			  		 " and AppraiseTask.appraiseTime<='" + checkEnd + "'" + //评议发起时间 > 检查开始时间
			  		 " and (" +
			  		 "  select min(Appraise.id)" +
			  		 "   from Appraise Appraise" +
			  		 "   where Appraise.taskId=AppraiseTask.id" +
			  		 "   and Appraise.created>=DATE(" + DateTimeUtils.formatDate(monthBegin, null) + ")" +
			  		 "   and Appraise.created<DATE(" + DateTimeUtils.formatDate(nextMonthBegin, null) + ")) is null";
		final List todoAppraiseTasks = getDatabaseService().findRecordsByHql(hql);
		if(todoAppraiseTasks!=null && !todoAppraiseTasks.isEmpty()) {
			if(Logger.isInfoEnabled()) {
				Logger.info("AppraiseService: found " + todoAppraiseTasks.size() + " tasks to startup.");
			}
			//创建线程,自动发起评议
			new Thread() {
				public void run() {
					try {
						startupAppraiseTasks(todoAppraiseTasks, year, month);
					}
					catch (ServiceException e) {
						Logger.exception(e);
					}
				}
			}.start();
		}
		
		//检查是否在服务对象上传催办时间内
		hql = "select AppraiseParameter.recipientsUploadUrgeSms" +
			  " from AppraiseParameter AppraiseParameter" +
			  " where AppraiseParameter.recipientsUploadUrgeBegin<=" + day + //服务对象上报催办开始日期 > 当前时间
			  " and AppraiseParameter.recipientsUploadUrgeBegin + AppraiseParameter.recipientsUploadUrgeDays>" + day + //服务对象上报催办开始日期+服务对象上报催办天数 < 当前时间
			  " and AppraiseParameter.recipientsUploadUrgeTime>'" + checkBegin + "'" + //催办时间 > 检查开始时间
			  " and AppraiseParameter.recipientsUploadUrgeTime<='" + checkEnd + "'"; //催办时间 <= 检查结束时间
		final String recipientsUploadUrgeSms = (String)getDatabaseService().findRecordByHql(hql); //获取服务对象上报催办短信格式
		if(recipientsUploadUrgeSms!=null) {
			if(Logger.isInfoEnabled()) {
				Logger.info("AppraiseService: startup recipients upload urge.");
			}
			//创建线程,开始服务对象上报催办
			new Thread() {
				public void run() {
					try {
						recipientsUploadUrge(recipientsUploadUrgeSms, year, month, monthBegin, nextMonthBegin);
					}
					catch (ServiceException e) {
						Logger.exception(e);
					}
				}
			}.start();
		}
	}
	
	/**
	 * 自动发起评议
	 * @param todoAppraiseTasks
	 * @param year
	 * @param month
	 * @throws ServiceException
	 */
	private void startupAppraiseTasks(List todoAppraiseTasks, int year, int month) throws ServiceException {
		for(Iterator iterator = todoAppraiseTasks.iterator(); iterator.hasNext();) {
			AppraiseTask appraiseTask = (AppraiseTask)iterator.next();
			try {
				startupAppraise(generateAppraiseName(appraiseTask, year, month), appraiseTask.getId(), year, month, null, true, false, null); //发布评议
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 服务对象上报催办
	 * @param recipientsUploadUrgeSms
	 * @param year
	 * @param month
	 * @param monthBegin
	 * @param nextMonthBegin
	 * @throws ServiceException
	 */
	private void recipientsUploadUrge(String recipientsUploadUrgeSms, int year, int month, Date monthBegin, Date nextMonthBegin) throws ServiceException {
		//获取地区列表
		String hql = "select distinct AppraiseParticipateUnit.areaId" +
					 " from AppraiseParticipateUnit AppraiseParticipateUnit" +
					 " where AppraiseParticipateUnit.year=" + year;
		List areaIds = getDatabaseService().findRecordsByHql(hql);
		if(areaIds==null || areaIds.isEmpty()) { //没有配置参评单位
			return;
		}
		for(Iterator iterator = areaIds.iterator(); iterator.hasNext();) {
			long areaId = ((Number)iterator.next()).longValue();
			//检查当月是否需要做服务对象评议
			hql = "select AppraiseTask.id" +
		  		  " from AppraiseTask AppraiseTask" +
		  		  " where AppraiseTask.enabled=1" + //已启用
		  		  " and AppraiseTask.areaId=" + areaId + //评议发起方式为自动
		  		  " and (AppraiseTask.appraiserType=1 or AppraiseTask.appraiserType=2)" + //评议员类型,0/基础,1/管理服务对象,2/全部
		  		  " and AppraiseTask.appraiseMonths like '%," + month + ",%'"; //评议月份匹配
			if(getDatabaseService().findRecordByHql(hql)==null) {
				continue;
			}
			//获取当月已上传服务对象的参评单位
			hql = "select distinct AppraiserImport.unitId" +
				  " from AppraiserImport AppraiserImport" +
				  " where AppraiserImport.areaId=" + areaId +
				  " and AppraiserImport.created>=DATE(" + DateTimeUtils.formatDate(monthBegin, null) + ")" +
			  	  " and AppraiserImport.created<DATE(" + DateTimeUtils.formatDate(nextMonthBegin, null) + ")";
			List importedUnitIds = getDatabaseService().findRecordsByHql(hql);

			//获取参评单位配置列表
			List participateUnits = listParticipateUnits(areaId, year);
			
			//剔除已经导入的单位,得到未导入的单位列表
			participateUnits = ListUtils.getNotInsideSubList(importedUnitIds, null, participateUnits, "unitId");
			if(participateUnits==null || participateUnits.isEmpty()) {
				continue;
			}
			
			List appraiseManagers = null;
			for(Iterator unitIterator = participateUnits.iterator(); unitIterator.hasNext();) {
				ParticipateUnit participateUnit = (ParticipateUnit)unitIterator.next();
				//发送短信
				String sms = recipientsUploadUrgeSms.replaceAll("<单位名称>", participateUnit.getUnitName())
							 						.replaceAll("<年度>", "" + year)
							 						.replaceAll("<月份>", "" + month);
				sendAppraiseUnitSms(areaId, "" + participateUnit.getUnitId(), sms, null);
				
				//创建服务对象上传任务,任务查询人包括参评单位责任人以及地区的评议管理员
				hql = "select AppraiserImportTask.id" +
					  " from AppraiserImportTask AppraiserImportTask" +
					  " where AppraiserImportTask.unitId=" + participateUnit.getUnitId() +
					  " and AppraiserImportTask.taskYear=" + year +
					  " and AppraiserImportTask.taskMonth=" + month;
				if(getDatabaseService().findRecordByHql(hql)!=null) {
					continue;
				}
				AppraiserImportTask appraiserImportTask = new AppraiserImportTask();
				appraiserImportTask.setId(UUIDLongGenerator.generateId()); //ID
				appraiserImportTask.setUnitId(participateUnit.getUnitId()); //单位ID
				appraiserImportTask.setUnitName(participateUnit.getUnitName()); //单位名称
				appraiserImportTask.setTaskYear(year); //年度
				appraiserImportTask.setTaskMonth(month); //月份
				appraiserImportTask.setCreated(DateTimeUtils.now()); //创建时间
				getDatabaseService().saveRecord(appraiserImportTask);
				if(appraiseManagers==null) {
					appraiseManagers = orgService.listDirectoryVisitors(areaId, "appraiseManager", true, false,0);
					if(appraiseManagers==null) {
						appraiseManagers = new ArrayList();
					}
				}
				List appraiseTransactors = orgService.listDirectoryVisitors(participateUnit.getUnitId(), "appraiseTransactor", false, false, 0);
				if(appraiseTransactors==null) {
					appraiseTransactors = new ArrayList();
				}
				appraiseTransactors.addAll(appraiseManagers);
				for(Iterator iteratorVisitor = appraiseTransactors.iterator(); iteratorVisitor.hasNext();) {
					DirectoryPopedom popedom = (DirectoryPopedom)iteratorVisitor.next();
					recordControlService.appendVisitor(appraiserImportTask.getId(), appraiserImportTask.getClass().getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#generateAppraiseName(com.yuanluesoft.appraise.pojo.AppraiseTask, int, int)
	 */
	public String generateAppraiseName(AppraiseTask task, int appraiseYear, int appraiseMonth) {
		String name = appraiseYear + "年";
		String[] months = task.getAppraiseMonths().substring(1, task.getAppraiseMonths().length()-1).split(",");
		if(months.length==2) {
			name += appraiseMonth<7 ? "上半年" : "下半年";
		}
		else if(months.length==4) {
			name += "第" + StringUtils.getChineseNumber((appraiseMonth - 1) / 3 + 1, false) + "季度";
		}
		else if(months.length!=1) {
			name += appraiseMonth + "月";
		}
		return name + task.getName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.service.AppraiseService#submitInternetAppraise(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void submitInternetAppraise(String appraiserNumber, String appraiseCode, boolean wapAppraise, HttpServletRequest request) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("AppraiseService: submit internet vote from " + appraiserNumber);
		}
		//添加到缓存
		InternetVote internetVote = new InternetVote();
		internetVote.setAppraiserNumber(appraiserNumber); //评议人手机号码 
		internetVote.setAppraiseCode(appraiseCode); //评议验证码
		internetVote.setWapAppraise(wapAppraise); //是否WAP评议
		internetVote.setVotes(new LinkedHashMap()); //投票情况,key:投票选项ID, value:意见或建议
		for(Enumeration parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();) {
			String parameterName = (String)parameterNames.nextElement();
			if(!parameterName.startsWith("appraise_")) { //不是评议选项
				continue;
			}
			String value = request.getParameter(parameterName);
			if(value!=null && !value.isEmpty()) {
				internetVote.getVotes().put(new Long(value), request.getParameter("appraisePropose_" + parameterName.substring("appraise_".length())));
			}
		}
		try {
			appraiseCache.put(new Long(UUIDLongGenerator.generateId()), internetVote);
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
		//唤醒计票线程
		countingThread.wakeup();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageArrived(com.yuanluesoft.jeaf.sms.pojo.SmsSend)
	 */
	public void onShortMessageArrived(SmsSend sentMessage) throws ServiceException {
		//不处理
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageReceived(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)
	 */
	public boolean onShortMessageReceived(String senderNumber, String message, Timestamp receiveTime, String receiveNumber, SmsUnitBusiness smsUnitBusiness) throws ServiceException {
		if(smsUnitBusiness==null || !"民主评议".equals(smsUnitBusiness.getBusinessName())) { //不是民主评议
			return false;
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("AppraiseService: submit sms vote from " + senderNumber);
		}
		//添加到缓存
		SmsVote smsVote = new SmsVote();
		smsVote.setSenderNumber(senderNumber); //发送人号码
		smsVote.setMessage(message); //短信内容
		smsVote.setReceiveTime(receiveTime); //接收时间
		smsVote.setReceiveNumber(receiveNumber); //接收人号码
		smsVote.setUnitId(smsUnitBusiness.getUnitConfig().getUnitId()); //单位ID
		try {
			appraiseCache.put(new Long(UUIDLongGenerator.generateId()), smsVote);
		}
		catch (CacheException e) {
			throw new ServiceException(e);
		}
		//唤醒计票线程
		countingThread.wakeup();
		return true;
	}
	
	/**
	 * 处理短信投票
	 * @param smsVote
	 */
	private void processSmsVote(SmsVote smsVote) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("AppraiseService: process sms vote from " + smsVote.getSenderNumber());
		}
		//获取短信发送记录
		String hql = "select AppraiseSms" +
					 " from AppraiseSms AppraiseSms, Appraise Appraise" +
					 " where AppraiseSms.appraiseId=Appraise.id" +
					 " and Appraise.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" +
					 " and AppraiseSms.appraiseUnitId=" + smsVote.getUnitId() +
					 " and AppraiseSms.appraiserNumber='" + smsVote.getSenderNumber() + "'" +
					 " and AppraiseSms.replyTime is null";
		AppraiseSms appraiseSms = (AppraiseSms)getDatabaseService().findRecordByHql(hql);
		if(appraiseSms==null) { //没有短信发送记录
			return;
		}
		appraiseSms.setReplySms(smsVote.getMessage()); //回复内容
		appraiseSms.setReplyTime(smsVote.getReceiveTime()); //回复内容
		getDatabaseService().updateRecord(appraiseSms);
		
		//获取选项投票记录
		AppraiseOptionVote optionVote = null;
		String message = smsVote.getMessage().trim();
		for(int i=0; i<2; i++) {
			hql = "select AppraiseOptionVote" +
				  " from AppraiseOptionVote AppraiseOptionVote, UnitAppraise UnitAppraise" +
				  " where AppraiseOptionVote.unitAppraiseId=UnitAppraise.id" +
				  " and UnitAppraise.appraiseId=" + appraiseSms.getAppraiseId() +
				  " and UnitAppraise.unitId=" + smsVote.getUnitId() +
				  " and AppraiseOptionVote.optionType=0" +
				  " and (AppraiseOptionVote.smsOption='" + JdbcUtils.resetQuot(StringUtils.fullWidthToHalfAngle(message)) + "'" +
				  " or AppraiseOptionVote.option='" + JdbcUtils.resetQuot(StringUtils.fullWidthToHalfAngle(message)) + "')";
			optionVote = (AppraiseOptionVote)getDatabaseService().findRecordByHql(hql);
			if(optionVote!=null) {
				break;
			}
			//删除标点符号
			String newMessage = StringUtils.removePunctuation(message);
			if(message.equals(newMessage)) {
				break;
			}
			message = newMessage;
		}
		//获取评议
		Appraise appraise = (Appraise)load(Appraise.class, appraiseSms.getAppraiseId());
		if(optionVote!=null) {
			//获取评测任务
			AppraiseTask task = (AppraiseTask)load(AppraiseTask.class, appraise.getTaskId());
			//更新投票人次
			appraise.setVoteTimes(appraise.getVoteTimes()+1);
			getDatabaseService().updateRecord(appraise);
			//记录投票结果
			vote(task, appraiseSms, optionVote, null, true);
			//重建静态页面
			pageService.rebuildStaticPageForModifiedObject(appraise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		//发送答谢短信
		sendApplauseSms(appraise, appraiseSms);
	}
	
	/**
	 * 处理网络投票
	 * @param internetVote
	 */
	private void processInternetVote(InternetVote internetVote) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("AppraiseService: process internet vote from " + internetVote.getAppraiserNumber());
		}
		AppraiseSms appraiseSms = loadAppraiseSms(internetVote.getAppraiserNumber(), internetVote.getAppraiseCode(), internetVote.isWapAppraise());
		if(appraiseSms==null) { //评议短信不存在
			return;
		}
		if(internetVote.isWapAppraise() && appraiseSms.getReplyTime()!=null) { //WAP评议,且已经评议过
			return;
		}
		//获取评议
		Appraise appraise = (Appraise)load(Appraise.class, appraiseSms.getAppraiseId());
		//获取评测任务
		AppraiseTask task = (AppraiseTask)load(AppraiseTask.class, appraise.getTaskId());
		
		//更新投票人次
		appraise.setVoteTimes(appraise.getVoteTimes()+1);
		getDatabaseService().updateRecord(appraise);
		
		//判断是否当作短信投票
		boolean smsVote = internetVote.isWapAppraise() || //WAP评议
						  task.getIsSpecial()==1  || //专题评议
						  (appraiseSms.getAppraiserType()==AppraiserService.APPRAISER_TYPE_DELEGATE && task.getDelegateAttend()==1) || //评议代表,且参与短信投票 
						  (appraiseSms.getAppraiserType()==AppraiserService.APPRAISER_TYPE_RECIPIENT && appraiseSms.getReplyTime()==null); //评议的评议员类型是服务对象且没有评议过
		if(smsVote) { //当作短信投票
			appraiseSms.setReplyTime(DateTimeUtils.now()); //设置回复时间
		}
		if(!smsVote || appraiseSms.getAppraiserType()==AppraiserService.APPRAISER_TYPE_DELEGATE) { //网络评议,或者评议代表
			appraiseSms.setAppraiseCode(null); //清空评议验证码
		}
		getDatabaseService().updateRecord(appraiseSms);
		
		//提交评议结果
		AppraiseOptionVote optionVote = null;
		for(Iterator iterator = internetVote.getVotes().keySet().iterator(); iterator.hasNext();) {
			try {
				Long optionVoteId = (Long)iterator.next();
				optionVote = (AppraiseOptionVote)getDatabaseService().findRecordById(AppraiseOptionVote.class.getName(), optionVoteId.longValue());
				vote(task, appraiseSms, optionVote, (String)internetVote.getVotes().get(optionVoteId), smsVote);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		
		//发送答谢短信
		sendApplauseSms(appraise, appraiseSms);
		
		//重建静态页面
		if(optionVote!=null) {
			pageService.rebuildStaticPageForModifiedObject(appraise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
	}

	/**
	 * 记录并统计投票结果
	 * @param task
	 * @param appraiseSms
	 * @param optionVote
	 * @param propose
	 * @param smsVote
	 * @throws ServiceException
	 */
	private void vote(AppraiseTask task, AppraiseSms appraiseSms, AppraiseOptionVote optionVote, String propose, boolean smsVote) throws ServiceException {
		AppraiseVote appraiseVote = new AppraiseVote();
		appraiseVote.setId(UUIDLongGenerator.generateId()); //ID
		appraiseVote.setUnitAppraiseId(optionVote.getUnitAppraiseId()); //单位评议ID
		appraiseVote.setAppraiserId(appraiseSms.getAppraiserId()); //评议员ID
		appraiseVote.setAppraiserOrgId(appraiseSms.getAppraiserOrgId()); //评议员所在组织ID
		appraiseVote.setAppraiser(appraiseSms.getAppraiser()); //评议员姓名
		appraiseVote.setAppraiserNumber(appraiseSms.getAppraiserNumber()); //评议员手机号码
		appraiseVote.setAppraiserType(appraiseSms.getAppraiserType()); //评议员类型
		appraiseVote.setOptionId(optionVote.getOptionId()); //选项ID
		appraiseVote.setOption(optionVote.getOption()); //选项名称
		appraiseVote.setScore(optionVote.getOptionScore()); //分数
		appraiseVote.setPropose(propose==null || propose.isEmpty() ? null : propose.substring(0, Math.min(2000, propose.length()))); //意见或建议
		appraiseVote.setCreated(DateTimeUtils.now()); //投票时间
		appraiseVote.setVoteMode(smsVote ? 0 : 1); //投票方式,0/短信,1/网络
		getDatabaseService().saveRecord(appraiseVote);
		
		//选项统计
		if(optionVote.getAbstain()==1) { //弃权,不做统计
			return;
		}
		optionVote.setVoteTotal(optionVote.getVoteTotal() + 1); //投票数
		if(smsVote) {
			optionVote.setSmsVoteTotal(optionVote.getSmsVoteTotal()); //短信投票数
		}
		else {
			optionVote.setInternetVoteTotal(optionVote.getInternetVoteTotal() + 1); //网络投票数
		}
		optionVote.setScore(optionVote.getScore() + optionVote.getOptionScore()); //累计分数
		getDatabaseService().updateRecord(optionVote);
		
		if(task.getIsSpecial()==1) { //专题评议
			//清空评议验证码
			appraiseSms.setAppraiseCode(null);
			getDatabaseService().updateRecord(appraiseSms);
		}
		
		//单位统计
		UnitAppraise unitAppraise = optionVote.getUnitAppraise();
		unitAppraise.setVoteTotal(unitAppraise.getVoteTotal() + 1); //投票数
		unitAppraise.setYearVoteTotal(unitAppraise.getYearVoteTotal() + 1); //单位累计投票数
		if(smsVote) {
			unitAppraise.setSmsVoteTotal(unitAppraise.getSmsVoteTotal() + 1); //短信投票数
			unitAppraise.setSmsScoreTotal(unitAppraise.getSmsScoreTotal() + optionVote.getOptionScore()); //短信投票得分
			unitAppraise.setYearSmsVoteTotal(unitAppraise.getYearSmsVoteTotal() + 1); //单位累计短信投票数
			unitAppraise.setYearSmsScoreTotal(unitAppraise.getYearSmsScoreTotal() + optionVote.getOptionScore()); //单位累计短信投票得分
		}
		else {
			unitAppraise.setInternetVoteTotal(unitAppraise.getInternetVoteTotal() + 1); //网络投票数
			unitAppraise.setInternetScoreTotal(unitAppraise.getInternetScoreTotal() + optionVote.getOptionScore()); //网络投票得分
			unitAppraise.setYearInternetVoteTotal(unitAppraise.getYearInternetVoteTotal() + 1); //单位累计网络投票数
			unitAppraise.setYearInternetScoreTotal(unitAppraise.getYearInternetScoreTotal() + optionVote.getOptionScore()); //单位累计网络投票得分
		}
		//计算综合得分
		unitAppraise.setScoreComprehensive(computeScoreComprehensive(task, unitAppraise.getSmsVoteTotal(), unitAppraise.getSmsScoreTotal(), unitAppraise.getInternetVoteTotal(), unitAppraise.getInternetScoreTotal())); //累计综合得分,截止到本期
		unitAppraise.setYearScoreComprehensive(computeScoreComprehensive(task, unitAppraise.getYearSmsVoteTotal(), unitAppraise.getYearSmsScoreTotal(), unitAppraise.getYearInternetVoteTotal(), unitAppraise.getYearInternetScoreTotal())); //累计综合得分,截止到本期
		getDatabaseService().updateRecord(unitAppraise);
	}

	/**
     * 计票线程
     * @author linchuan
     *
     */
    private class CountingThread extends Thread {
    	private boolean isBusy;
   
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			synchronized(this) {
				try {
					Thread.sleep(30*1000); //等待30s
				} 
				catch (InterruptedException e1) {
				
				}
				while(!isInterrupted()) {
					try {
						isBusy = true;
						Collection keys = appraiseCache.getKeys();
						if(keys==null || keys.isEmpty()) {
							isBusy = false;
							wait(600 * 1000); //没有未处理的投票,等待10分钟
							continue;
						}
						Object key = keys.iterator().next();
						try {
							Object vote = appraiseCache.get(key);
							if(vote instanceof SmsVote) { //短信投票
								processSmsVote((SmsVote)vote);
							}
							else if(vote instanceof InternetVote) { //网络投票
								processInternetVote((InternetVote)vote);
							}
						}
						catch (Error e) {
							e.printStackTrace();
							Logger.error(e.getMessage());
						}
						catch (Exception e) {
							Logger.exception(e);
						}
						finally {
							appraiseCache.remove(key);
						}
					}
					catch (Error e) {
						e.printStackTrace();
						Logger.error(e.getMessage());
					}
					catch (Exception e) {
						Logger.exception(e);
					}
				}
			}
		}
		
		/**
		 * 唤醒
		 *
		 */
		public void wakeup() {
			if(isBusy) {
				return;
			}
			synchronized(this) {
				this.notify();
			}
		}
    }
    
    /**
     * 短信投票
     * @author linchuan
     *
     */
    private class SmsVote implements Serializable {
    	private String senderNumber; //发送人号码
    	private String message; //短信内容
    	private Timestamp receiveTime; //接收时间
    	private String receiveNumber; //接收人号码
    	private long unitId; //单位ID
    	
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
		 * @return the receiveNumber
		 */
		public String getReceiveNumber() {
			return receiveNumber;
		}
		/**
		 * @param receiveNumber the receiveNumber to set
		 */
		public void setReceiveNumber(String receiveNumber) {
			this.receiveNumber = receiveNumber;
		}
		/**
		 * @return the receiveTime
		 */
		public Timestamp getReceiveTime() {
			return receiveTime;
		}
		/**
		 * @param receiveTime the receiveTime to set
		 */
		public void setReceiveTime(Timestamp receiveTime) {
			this.receiveTime = receiveTime;
		}
		/**
		 * @return the senderNumber
		 */
		public String getSenderNumber() {
			return senderNumber;
		}
		/**
		 * @param senderNumber the senderNumber to set
		 */
		public void setSenderNumber(String senderNumber) {
			this.senderNumber = senderNumber;
		}
		/**
		 * @return the unitId
		 */
		public long getUnitId() {
			return unitId;
		}
		/**
		 * @param unitId the unitId to set
		 */
		public void setUnitId(long unitId) {
			this.unitId = unitId;
		}
    }
    
    /**
     * 网络投票
     * @author linchuan
     *
     */
    private class InternetVote implements Serializable {
    	private String appraiserNumber; //评议人手机号码 
    	private String appraiseCode; //评议验证码
    	private boolean wapAppraise; //是否WAP评议
    	private Map votes; //投票情况,key:投票选项ID, value:意见或建议
    	
		/**
		 * @return the appraiseCode
		 */
		public String getAppraiseCode() {
			return appraiseCode;
		}
		/**
		 * @param appraiseCode the appraiseCode to set
		 */
		public void setAppraiseCode(String appraiseCode) {
			this.appraiseCode = appraiseCode;
		}
		/**
		 * @return the appraiserNumber
		 */
		public String getAppraiserNumber() {
			return appraiserNumber;
		}
		/**
		 * @param appraiserNumber the appraiserNumber to set
		 */
		public void setAppraiserNumber(String appraiserNumber) {
			this.appraiserNumber = appraiserNumber;
		}
		/**
		 * @return the votes
		 */
		public Map getVotes() {
			return votes;
		}
		/**
		 * @param votes the votes to set
		 */
		public void setVotes(Map votes) {
			this.votes = votes;
		}
		/**
		 * @return the wapAppraise
		 */
		public boolean isWapAppraise() {
			return wapAppraise;
		}
		/**
		 * @param wapAppraise the wapAppraise to set
		 */
		public void setWapAppraise(boolean wapAppraise) {
			this.wapAppraise = wapAppraise;
		}
    }
    
	/**
	 * @return the smsService
	 */
	public SmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the appraiserService
	 */
	public AppraiserService getAppraiserService() {
		return appraiserService;
	}

	/**
	 * @param appraiserService the appraiserService to set
	 */
	public void setAppraiserService(AppraiserService appraiserService) {
		this.appraiserService = appraiserService;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
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
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the appraiseByCodeOnly
	 */
	public boolean isAppraiseByCodeOnly() {
		return appraiseByCodeOnly;
	}

	/**
	 * @param appraiseByCodeOnly the appraiseByCodeOnly to set
	 */
	public void setAppraiseByCodeOnly(boolean appraiseByCodeOnly) {
		this.appraiseByCodeOnly = appraiseByCodeOnly;
	}

	/**
	 * @return the smsCarrierNumbers
	 */
	public String getSmsCarrierNumbers() {
		return smsCarrierNumbers;
	}

	/**
	 * @param smsCarrierNumbers the smsCarrierNumbers to set
	 */
	public void setSmsCarrierNumbers(String smsCarrierNumbers) {
		this.smsCarrierNumbers = smsCarrierNumbers;
	}

	/**
	 * @return the importRecipientInternetAppraise
	 */
	public boolean isImportRecipientInternetAppraise() {
		return importRecipientInternetAppraise;
	}

	/**
	 * @param importRecipientInternetAppraise the importRecipientInternetAppraise to set
	 */
	public void setImportRecipientInternetAppraise(
			boolean importRecipientInternetAppraise) {
		this.importRecipientInternetAppraise = importRecipientInternetAppraise;
	}

	/**
	 * @return the appraiseCache
	 */
	public Cache getAppraiseCache() {
		return appraiseCache;
	}

	/**
	 * @param appraiseCache the appraiseCache to set
	 */
	public void setAppraiseCache(Cache appraiseCache) {
		this.appraiseCache = appraiseCache;
	}
}