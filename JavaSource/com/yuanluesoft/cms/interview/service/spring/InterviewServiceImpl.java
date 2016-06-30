package com.yuanluesoft.cms.interview.service.spring;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.cms.interview.pojo.InterviewImage;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeak;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeakFlow;
import com.yuanluesoft.cms.interview.pojo.InterviewSubject;
import com.yuanluesoft.cms.interview.pojo.InterviewSubjectRole;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewServiceImpl extends BusinessServiceImpl implements InterviewService {
	private String sitePagePath; //站点页面路径
	private String sitePageUrl; //站点页面URL
	private String webApplicationLocalUrl; //在服务器上访问网站时的应用URL
	private PersonService personService; //系统用户服务
	private final int NEWEST_SPEAKS_FILE_COUNT = 30; //生成最新发言静态文件的数量,避免文件被web服务器缓存
	private final String NEWEST_SPEAKS_FILE_NAME = "newSpeaks.html"; //最新发言静态文件名称
	private final String IMAGES_FILE_NAME = "images.html"; //最新图片静态文件
	private ExchangeClient exchangeClient; //数据交换服务
	private PageService pageService; //页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof InterviewSubject) { //访谈主题
			updateSubjectRoles((InterviewSubject)record, true); //保存角色配置
		}
		else if(record instanceof InterviewImage) { //访谈图片
			generateImagesHtmlFile(((InterviewImage)record).getInterviewSubjectId(), 2000); //2秒后生成最新发言页面,以保证数据被更新
		}
		else if(record instanceof InterviewSpeak) { //访谈发言
			InterviewSpeak speak = (InterviewSpeak)record;
			if(speak.getPublishTime()!=null) { //直接发布
				generateNewSpeaksHtmlFile(speak.getSubjectId(), speak.getPublishTime()); //生成最新发言页面
			}
		}
		//同步更新
		exchangeClient.synchUpdate(record, null, 2000);
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof InterviewSubject) { //访谈主题
			updateSubjectRoles((InterviewSubject)record, false); //更新角色配置
		}
		else if(record instanceof InterviewImage) { //访谈图片
			generateImagesHtmlFile(((InterviewImage)record).getInterviewSubjectId(), 2000); //2秒后生成最新发言页面,以保证数据被更新
		}
		else if(record instanceof InterviewSpeak) { //访谈发言
			InterviewSpeak speak = (InterviewSpeak)record;
			if(speak.getPublishTime()!=null) { //已发布
				generateNewSpeaksHtmlFile(speak.getSubjectId(), speak.getPublishTime()); //生成最新发言页面
			}
		}
		//同步更新
		exchangeClient.synchUpdate(record, null, 2000);
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof InterviewSubject) {
			//删除发言
			InterviewSubject interviewSubject = (InterviewSubject)record;
			String hql = "from InterviewSpeak InterviewSpeak" +
						 " where InterviewSpeak.subjectId=" + interviewSubject.getId();
			getDatabaseService().deleteRecordsByHql(hql);
		}
		else if(record instanceof InterviewImage) { //访谈图片
			InterviewImage interviewImage = (InterviewImage)record;
			generateImagesHtmlFile(interviewImage.getInterviewSubjectId(), 2000); //2秒后生成最新发言页面,以保证数据被更新
		}
		//同步删除
		exchangeClient.synchDelete(record, null, 2000);
		//重建静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#submitSpeak(com.yuanluesoft.cms.interview.pojo.InterviewSpeak, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void submitSpeak(InterviewSpeak speak, HttpServletRequest request, InterviewSessionInfo sessionInfo) throws ServiceException {
		if(speak.getContent().equals("")) {
			return;
		}
		//获取访谈主题
		InterviewSubject interviewSubject = getInterviewSubject(speak.getSubjectId());
		if(interviewSubject.getIsEnding()=='1') { //访谈已经结束
			return;
		}
		//获取发言人类型
		String role = getInterviewRole(interviewSubject, sessionInfo);
		if(role==null) { //不属于任何访谈角色
			return;
		}
		String flow;
		if("嘉宾".equals(role)) {
			speak.setSpeaker(sessionInfo.getUserName());
			speak.setSpeakerType(SPEAKER_TYPE_GUESTS);
			flow = interviewSubject.getGuestsSpeakFlow();
		}
		else if("主持人".equals(role)) {
			speak.setSpeaker("主持人");
			speak.setSpeakerType(SPEAKER_TYPE_COMPERE);
			flow = interviewSubject.getCompereSpeakFlow();
		}
		else { //网友
			if(speak.getSpeaker()==null || speak.getSpeaker().trim().equals("")) { //用户没有填写姓名
				speak.setSpeaker("网友");
			}
			else if((",主持人,嘉宾," + interviewSubject.getGuests() + ",").indexOf("," + speak.getSpeaker().trim() + ",")!=-1) { //过滤掉关键字
				speak.setSpeaker("网友");
			}
			speak.setSpeakerType(SPEAKER_TYPE_NETUSER);
			flow = interviewSubject.getSpeakFlow();
		}
		if(flow==null || flow.equals("")) { //没有配置流程
			//直接发布
			speak.setPublishTime(DateTimeUtils.now());
		}
		else { //把流程中第一个角色设置为审核角色
			speak.setApprovalRole(flow.split(",")[0]);
		}
		speak.setSpeakerIP(request.getRemoteAddr()); //发言人IP
		speak.setSpeakTime(DateTimeUtils.now()); //发言时间
		speak.setIsLeave(speak.getSpeakTime().before(interviewSubject.getBeginTime()) ? '1' : '0'); //早于访谈开始时间,设置为留言
		save(speak); //保存发言
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#approvalSpeak(long, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalSpeak(long speakId, boolean pass, InterviewSessionInfo sessionInfo) throws ServiceException {
		String hql = "from InterviewSpeak InterviewSpeak" +
					 " where InterviewSpeak.id=" + speakId;
		InterviewSpeak interviewSpeak = (InterviewSpeak)getDatabaseService().findRecordByHql(hql);
		if(interviewSpeak==null || interviewSpeak.getApprovalRole()==null) { //已经被删除,或者已经发布
			return;
		}
		if(!pass) { //审核不通过
			delete(interviewSpeak);
			return;
		}
		//获取访谈主题
		InterviewSubject interviewSubject = getInterviewSubject(interviewSpeak.getSubjectId());
		if(interviewSubject.getIsEnding()=='1') { //访谈已经结束
			return;
		}
		String role = getInterviewRole(interviewSubject, sessionInfo);
		if(!interviewSpeak.getApprovalRole().equals(role)) { //当前审核角色不匹配,说明已经被其他人审核过
			return;
		}
		String flow;
		if(interviewSpeak.getSpeakerType()==SPEAKER_TYPE_GUESTS) {
			flow = interviewSubject.getGuestsSpeakFlow();
		}
		else if(interviewSpeak.getSpeakerType()==SPEAKER_TYPE_COMPERE) {
			flow = interviewSubject.getCompereSpeakFlow();
		}
		else { //网友
			flow = interviewSubject.getSpeakFlow();
		}
		//获取下一个审核人角色
		String[] roles = flow.split(",");
		int i=0;
		for(; i<roles.length && !roles[i].equals(interviewSpeak.getApprovalRole()); i++);
		if(i==roles.length-1) { //全部审批完成
			interviewSpeak.setApprovalRole(null);
			interviewSpeak.setPublishTime(DateTimeUtils.now()); //设置发布时间
		}
		else {
			interviewSpeak.setApprovalRole(roles[++i]); //设置下一个审核角色
		}
		update(interviewSpeak);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#endInterview(long, com.yuanluesoft.cms.interview.model.InterviewSessionInfo)
	 */
	public void endInterview(long subjectId, InterviewSessionInfo sessionInfo) throws ServiceException {
		//获取访谈主题
		InterviewSubject interviewSubject = getInterviewSubject(subjectId);
		if(interviewSubject.getIsEnding()=='1') { //访谈已经结束
			return;
		}
		if(!"主持人".equals(getInterviewRole(interviewSubject, sessionInfo))) { //不是主持人
			throw new ServiceException();
		}
		interviewSubject.setIsEnding('1'); //设置为结束
		interviewSubject.setEndTime(DateTimeUtils.now()); //结束时间
		update(interviewSubject);
		//删除未审核的发言
		String hql = "from InterviewSpeak InterviewSpeak" +
					 " where InterviewSpeak.subjectId=" + subjectId +
					 " and InterviewSpeak.publishTime is null";
		for(int i=0; ; i+=100) {
			List speaks = getDatabaseService().findRecordsByHql(hql, i, 100);
			if(speaks==null || speaks.isEmpty()) {
				break;
			}
			for(Iterator iterator = speaks.iterator(); iterator.hasNext();) {
				InterviewSpeak speak = (InterviewSpeak)iterator.next();
				delete(speak);
			}
		}
		//同步更新
		exchangeClient.synchUpdate(interviewSubject, null, 2000);
	}
	
	/**
	 * 保存或更新角色配置
	 * @param interviewSubject
	 * @param isNew
	 * @throws ServiceException
	 */
	private void updateSubjectRoles(InterviewSubject interviewSubject, boolean isNew) throws ServiceException {
		if(!isNew) {
			String hql = "from InterviewSubjectRole InterviewSubjectRole" +
						 " where InterviewSubjectRole.subjectId=" + interviewSubject.getId();
			getDatabaseService().deleteRecordsByHql(hql);
		}
		if(interviewSubject.getRoles()!=null) {
			for(Iterator iterator = interviewSubject.getRoles().iterator(); iterator.hasNext();) {
				InterviewSubjectRole role = (InterviewSubjectRole)iterator.next();
				getDatabaseService().saveRecord(role);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#getInterviewSubject(long)
	 */
	public InterviewSubject getInterviewSubject(long id) throws ServiceException {
		return (InterviewSubject)load(InterviewSubject.class, id);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#loadSpeakFlow(long)
	 */
	public InterviewSpeakFlow loadSpeakFlow(long siteId) throws ServiceException {
		String hql = "from InterviewSpeakFlow InterviewSpeakFlow" +
					 " where InterviewSpeakFlow.siteId=" + siteId;
		return (InterviewSpeakFlow)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#listInterviewRoles(long)
	 */
	public List listInterviewRoles(long siteId) throws ServiceException {
		String hql = "from InterviewRole InterviewRole" +
					 " where InterviewRole.siteId=" + siteId +
					 " order by InterviewRole.role";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#getLiveImagesUpdateUrl(long)
	 */
	public String getLiveImagesUpdateUrl(long subjectId) throws ServiceException {
		return sitePageUrl + subjectId + "/" + IMAGES_FILE_NAME;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#getLiveSpeaksUpdateUrl(long)
	 */
	public String getLiveSpeaksUpdateUrl(long subjectId) throws ServiceException {
		return sitePageUrl + subjectId + "/" + NEWEST_SPEAKS_FILE_NAME;
	}
	
	/**
	 * 生成最新发言的静态页面
	 * @param subjectId
	 * @param lastPublishTime
	 * @throws ServiceException
	 */
	private void generateNewSpeaksHtmlFile(final long subjectId, Timestamp lastPublishTime) throws ServiceException {
		//获取过去20秒内的发言,客户机是6秒更新一次
		String url = webApplicationLocalUrl + "/cms/interview/liveUpdate.shtml" +
		 			 "?target=interviewLiveSpeaks" +
					 "&id=" + subjectId + 
					 "&siteId=" + getInterviewSubject(subjectId).getSiteId() + 
					 "&pageName=interviewLive" +
					 "&lastSpeakTime=" + DateTimeUtils.add(lastPublishTime, Calendar.SECOND, -20).getTime();
		String path = FileUtils.createDirectory(sitePagePath + subjectId) + "/" + NEWEST_SPEAKS_FILE_NAME ;
		generateHtmlFile(url, path + "0");
		for(int i=1; i<NEWEST_SPEAKS_FILE_COUNT; i++) {
			FileUtils.copyFile(path + "0", path + i, true, false);
		}
	}
	
	/**
	 * 生成更新图片列表的静态页面
	 * @param subjectId
	 * @param delay 延时时间
	 * @throws ServiceException
	 */
	private void generateImagesHtmlFile(final long subjectId, int delay) throws ServiceException {
		//生成最新发言页面
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					String url = webApplicationLocalUrl + "/cms/interview/liveUpdate.shtml" +
								 "?target=interviewImages" +
								 "&id=" + subjectId + 
								 "&pageName=interviewLive";
					generateHtmlFile(url, FileUtils.createDirectory(sitePagePath + subjectId) + "/" + IMAGES_FILE_NAME);
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
				timer.cancel(); 
			}
		}, delay);
	}
	
	/**
	 * 生成最新发言页面
	 * @param subjectId
	 * @throws ServiceException
	 */
	private void generateHtmlFile(String url, String filePath) throws ServiceException {
		HttpURLConnection connection = null;
		InputStream in = null;
		InputStreamReader reader = null;
		FileOutputStream out = null;
		OutputStreamWriter writer = null;
		try {
			connection = (HttpURLConnection)new java.net.URL(url).openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(30000);
			connection.connect();
			in = connection.getInputStream();
			reader = new InputStreamReader(in , "utf-8");
			for(int i=0; i<10; i++) {
				try {
					out = new FileOutputStream(filePath);
					writer = new OutputStreamWriter(out, "utf-8");
					break;
				}
				catch(Exception e) {
					if(i==9) {
						return;
					}
					Thread.sleep(100);
				}
			}
			char[] buffer = new char[4096];
			for(int readLen = reader.read(buffer); readLen>0; readLen = reader.read(buffer)) {
				writer.write(buffer, 0, readLen);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try { reader.close(); } catch(Exception e) { }
			try { in.close(); } catch(Exception e) { }
			try { connection.disconnect(); } catch(Exception e) { }
			try { writer.close(); } catch(Exception e) { }
			try { out.close(); } catch(Exception e) { }
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		if(loginName.startsWith("interviewGuests_")) {
			String[] values = loginName.split("_");
			InterviewSessionInfo sessionInfo = new InterviewSessionInfo();
			sessionInfo.setGuests(true);
			sessionInfo.setUserName(values[2]);
			sessionInfo.setLoginName(values[2]);
			sessionInfo.setUnitId(Long.parseLong(values[1]));
			return sessionInfo;
		}
		//调用网上用户注册服务获取会话
		SessionInfo sessionInfo = personService.createSessionInfo(loginName); //调用系统用户服务获取会话
		if(sessionInfo==null) { //不是系统用户
			return null;
		}
		InterviewSessionInfo interviewSessionInfo = new InterviewSessionInfo();
		try {
			PropertyUtils.copyProperties(interviewSessionInfo, sessionInfo);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
		interviewSessionInfo.setGuests(false);
		return interviewSessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		if(!loginName.startsWith("interviewGuests_")) { //不是嘉宾
			return null;
		}
		//获取访谈主题
		long subjectId = RequestUtils.getParameterLongValue(request, "id");
		InterviewSubject interviewSubject;
		try {
			interviewSubject = getInterviewSubject(subjectId);
		} 
		catch (ServiceException e) {
			return null;
		}
		if(interviewSubject==null) {
			return null;
		}
		//检查登录密码
		if(!passwordMatcher.matching(interviewSubject.getGuestsPassword(), password)) {
			return null;
		}
		//检查用户是否嘉宾
		if(("," + interviewSubject.getGuests() + ",").indexOf("," + loginName.split("_")[2] + ",")==-1) {
			return null;
		}
		return new MemberLogin(loginName, 0, PersonService.PERSON_TYPE_OTHER, null, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.service.InterviewService#getInterviewRole(com.yuanluesoft.cms.interview.pojo.InterviewSubject, com.yuanluesoft.cms.interview.model.InterviewSessionInfo)
	 */
	public String getInterviewRole(InterviewSubject interviewSubject, SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名用户
    		return "网友";
    	}
    	if(sessionInfo instanceof InterviewSessionInfo && ((InterviewSessionInfo)sessionInfo).isGuests()) { //嘉宾
			return (sessionInfo.getUnitId()==interviewSubject.getId() ? "嘉宾" : null); //检查用户是否本主题的嘉宾
		}
		if(("," + interviewSubject.getCompereIds() + ",").indexOf("," + sessionInfo.getUserId() + ",")!=-1) { //检查是否主持人
			return "主持人";
		}
		if(interviewSubject.getRoles()!=null) { //检查是否审核人
			for(Iterator iterator = interviewSubject.getRoles().iterator(); iterator.hasNext();) {
				InterviewSubjectRole interviewSubjectRole = (InterviewSubjectRole)iterator.next();
				if(("," + interviewSubjectRole.getRoleMemberIds() + ",").indexOf("," + sessionInfo.getUserId() + ",")!=-1) {
					return interviewSubjectRole.getRole();
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("guests".equals(itemsName)) { //嘉宾列表
			long id;
			try {
				id = ((Number)PropertyUtils.getProperty(bean, "id")).longValue();
			}
			catch(Exception e) {
				return null;
			}
			InterviewSubject interviewSubject = getInterviewSubject(id);
			return ListUtils.generateList(interviewSubject.getGuests(), ",");
		}
		else if("speakers".equals(itemsName)) {
			InterviewSubject subject = null;
			if(bean instanceof InterviewSubject) {
				subject = (InterviewSubject)bean;
			}
			else {
				try {
					subject = getInterviewSubject(((Number)PropertyUtils.getProperty(bean, "id")).longValue());
				}
				catch(Exception e) {
					
				}
			}
			if(subject==null) {
				return null;
			}
			List speakers = new ArrayList();
			speakers.add(new String[]{"主持人", InterviewService.SPEAKER_TYPE_COMPERE + ""});
			if(subject.getGuests()!=null && !subject.getGuests().equals("")) {
				String guests[] = subject.getGuests().split(",");
				for(int i=0; i<guests.length; i++) {
					speakers.add(new String[]{guests[i], InterviewService.SPEAKER_TYPE_GUESTS + ""});
				}
			}
			speakers.add(new String[]{"网友", InterviewService.SPEAKER_TYPE_NETUSER + ""});
			return speakers;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the sitePagePath
	 */
	public String getSitePagePath() {
		return sitePagePath;
	}

	/**
	 * @param sitePagePath the sitePagePath to set
	 */
	public void setSitePagePath(String sitePagePath) {
		this.sitePagePath = sitePagePath;
	}

	/**
	 * @return the sitePageUrl
	 */
	public String getSitePageUrl() {
		return sitePageUrl;
	}

	/**
	 * @param sitePageUrl the sitePageUrl to set
	 */
	public void setSitePageUrl(String sitePageUrl) {
		this.sitePageUrl = sitePageUrl;
	}

	/**
	 * @return the webApplicationLocalUrl
	 */
	public String getWebApplicationLocalUrl() {
		return webApplicationLocalUrl;
	}

	/**
	 * @param webApplicationLocalUrl the webApplicationLocalUrl to set
	 */
	public void setWebApplicationLocalUrl(String webApplicationLocalUrl) {
		this.webApplicationLocalUrl = webApplicationLocalUrl;
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
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
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
}