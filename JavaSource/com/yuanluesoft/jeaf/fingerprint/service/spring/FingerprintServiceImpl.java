package com.yuanluesoft.jeaf.fingerprint.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.fingerprint.matcher.FingerprintMatcher;
import com.yuanluesoft.jeaf.fingerprint.pojo.FingerprintMatch;
import com.yuanluesoft.jeaf.fingerprint.pojo.FingerprintTemplate;
import com.yuanluesoft.jeaf.fingerprint.service.FingerprintService;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class FingerprintServiceImpl implements FingerprintService {
	private int templateNumberPerPerson = 1; //每个用户保存的指纹模板数量
	private int matchLogNumberPerPerson = 10; //每个用户保存的指纹匹配日志数量
	private DatabaseService databaseService; //数据库访问
	private FingerprintMatcher fingerprintMatcher; //指纹比对
	private MemberServiceList memberServiceList; //成员服务
	private boolean fingerprintSupport; //是否支持指纹认证
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.fingerprint.service.FingerprintService#saveFingerprintTemplate(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveFingerprintTemplate(long personId, String personName, String fingerName, String templateData) throws ServiceException {
		//删除超出上限的指纹记录
		String hql = "from FingerprintTemplate FingerprintTemplate" +
					 " where FingerprintTemplate.personId=" + personId +
					 " order by FingerprintTemplate.created DESC";
		List fingerprintTemplates = databaseService.findRecordsByHql(hql, templateNumberPerPerson-1, 0);
		if(fingerprintTemplates!=null && !fingerprintTemplates.isEmpty()) {
			for(Iterator iterator = fingerprintTemplates.iterator(); iterator.hasNext();) {
				FingerprintTemplate fingerprintTemplate = (FingerprintTemplate)iterator.next();
				databaseService.deleteRecord(fingerprintTemplate);
			}
		}
		//添加新指纹
		FingerprintTemplate fingerprintTemplate = new FingerprintTemplate();
		fingerprintTemplate.setId(UUIDLongGenerator.generateId()); //ID
		fingerprintTemplate.setPersonId(personId); //用户ID
		fingerprintTemplate.setPersonName(personName); //用户名
		fingerprintTemplate.setTemplate(templateData); //指纹数据
		fingerprintTemplate.setFinger(fingerName); //手指名称
		fingerprintTemplate.setCreated(DateTimeUtils.now()); //创建时间
		databaseService.saveRecord(fingerprintTemplate);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.fingerprint.service.FingerprintService#verifyFingerprint(java.lang.String, java.lang.String, long)
	 */
	public long verifyFingerprint(String featureData, String ip, long perhapsPersonId) throws ServiceException {
		FingerprintTemplate matchTemplate = null;
		if(perhapsPersonId>0) {
			//先按可能的用户ID来比对指纹
			String hql = "from FingerprintTemplate FingerprintTemplate" +
						 " where FingerprintTemplate.personId=" + perhapsPersonId;
			matchTemplate = verifyFingerprintByHql(featureData, hql);
		}
		if(matchTemplate==null) {
			//按IP查找曾经比对过的指纹
			String hql = "select FingerprintTemplate" +
						 " from FingerprintTemplate FingerprintTemplate, FingerprintMatch FingerprintMatch" +
						 " where FingerprintTemplate.personId=FingerprintMatch.personId" +
						 " and FingerprintMatch.ip='" + ip + "'" +
						 " order by FingerprintMatch.matchTimes DESC"; //按比对次数降序排列
			matchTemplate = verifyFingerprintByHql(featureData, hql);
			if(matchTemplate==null) {
				//比对曾经比对过的用户
				hql = "select FingerprintTemplate" +
				 	  " from FingerprintTemplate FingerprintTemplate, FingerprintMatch FingerprintMatch" +
				 	  " where FingerprintTemplate.personId=FingerprintMatch.personId" +
				 	  " and FingerprintMatch.ip!='" + ip + "'" +
				 	  " order by FingerprintMatch.matchTimes DESC";
				matchTemplate = verifyFingerprintByHql(featureData, hql);
				if(matchTemplate==null) {
					//比对从未比对过的指纹
					hql = "select FingerprintTemplate" +
					 	  " from FingerprintTemplate FingerprintTemplate" +
					 	  " where FingerprintTemplate.personId not in (select FingerprintMatch.personId from FingerprintMatch FingerprintMatch)" +
					 	  " order by FingerprintTemplate.created DESC";
					matchTemplate = verifyFingerprintByHql(featureData, hql);
				}
			}
		}
		if(matchTemplate==null) {
			return -1;
		}
		//写入匹配日志
		String hql = "from FingerprintMatch FingerprintMatch" +
					 " where FingerprintMatch.personId=" + matchTemplate.getPersonId() +
					 " and FingerprintMatch.ip='" + ip + "'";
		FingerprintMatch fingerprintMatch = (FingerprintMatch)databaseService.findRecordByHql(hql);
		if(fingerprintMatch==null) { //没有对应的匹配日志
			//删除超出上限的匹配日志
			hql = "from FingerprintMatch FingerprintMatch" +
				  " where FingerprintMatch.personId=" + matchTemplate.getPersonId() +
				  " order by FingerprintMatch.matchTimes DESC";
			List matchLogs = databaseService.findRecordsByHql(hql, matchLogNumberPerPerson-1, 0);
			if(matchLogs!=null && !matchLogs.isEmpty()) {
				for(Iterator iterator = matchLogs.iterator(); iterator.hasNext();) {
					FingerprintMatch matchLog = (FingerprintMatch)iterator.next();
					databaseService.deleteRecord(matchLog);
				}
			}
			//添加新指纹
			fingerprintMatch = new FingerprintMatch();
			fingerprintMatch.setId(UUIDLongGenerator.generateId()); //ID
			fingerprintMatch.setPersonId(matchTemplate.getPersonId()); //用户ID
			fingerprintMatch.setIp(ip); //IP
			fingerprintMatch.setMatchTimes(1); //匹配次数
			databaseService.saveRecord(fingerprintMatch);
		}
		else {
			fingerprintMatch.setMatchTimes(fingerprintMatch.getMatchTimes() + 1); //匹配次数
			databaseService.updateRecord(fingerprintMatch);
		}
		return matchTemplate.getPersonId();
	}
	
	/**
	 * 按hql匹配指纹
	 * @param featureData
	 * @param hql
	 * @return
	 * @throws ServiceException
	 */
	private FingerprintTemplate verifyFingerprintByHql(String featureData, String hql) throws ServiceException {
		for(int i=0; ; i+=100) { //每次处理100个模板
			List templates = databaseService.findRecordsByHql(hql, i, 100);
			if(templates==null || templates.isEmpty()) {
				return null;
			}
			FingerprintTemplate matchTemplate = fingerprintMatcher.match(featureData, templates);
			if(matchTemplate!=null) {
				return matchTemplate;
			}
			if(templates.size()<100) {
				return null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.fingerprint.service.FingerprintService#removeFingerprint(long)
	 */
	public void removeFingerprint(long personId) throws ServiceException {
		//删除指纹模板
		databaseService.deleteRecordsByHql("from FingerprintTemplate FingerprintTemplate where FingerprintTemplate.personId=" + personId);
		//删除比对记录
		databaseService.deleteRecordsByHql("from FingerprintMatch FingerprintMatch where FingerprintMatch.personId=" + personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		String featureData = request.getParameter("fingerprintFeatureData");
		if(featureData==null || featureData.equals("")) {
			return null;
		}
		long perhapsPersonId = -1;
		if(loginName!=null && !loginName.equals("")) {
			try {
				SessionInfo sessionInfo = memberServiceList.createSessionInfo(loginName);
				if(sessionInfo!=null) {
					perhapsPersonId = sessionInfo.getUserId();
				}
			}
			catch(SessionException e) {
		
			}
		}
		Member member;
		long personId = verifyFingerprint(featureData, request.getRemoteHost(), perhapsPersonId);
		if(personId>0 && (member = memberServiceList.getMember(personId))!=null) {
			return new MemberLogin(member.getLoginName(), personId, member.getMemberType(), null, false);
		}
		throw new LoginException("没有找到匹配的指纹");
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
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		return null;
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
	 * @return the fingerprintMatcher
	 */
	public FingerprintMatcher getFingerprintMatcher() {
		return fingerprintMatcher;
	}
	
	/**
	 * @param fingerprintMatcher the fingerprintMatcher to set
	 */
	public void setFingerprintMatcher(FingerprintMatcher fingerprintMatcher) {
		this.fingerprintMatcher = fingerprintMatcher;
	}
	
	/**
	 * @return the templateNumberPerPerson
	 */
	public int getTemplateNumberPerPerson() {
		return templateNumberPerPerson;
	}
	
	/**
	 * @param templateNumberPerPerson the templateNumberPerPerson to set
	 */
	public void setTemplateNumberPerPerson(int templateNumberPerPerson) {
		this.templateNumberPerPerson = Math.max(1, Math.min(5, templateNumberPerPerson));
	}

	/**
	 * @return the matchLogNumberPerPerson
	 */
	public int getMatchLogNumberPerPerson() {
		return matchLogNumberPerPerson;
	}

	/**
	 * @param matchLogNumberPerPerson the matchLogNumberPerPerson to set
	 */
	public void setMatchLogNumberPerPerson(int matchLogNumberPerPerson) {
		this.matchLogNumberPerPerson = Math.max(1, Math.min(10, matchLogNumberPerPerson));
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
	 * @return the fingerprintSupport
	 */
	public boolean isFingerprintSupport() {
		return fingerprintSupport;
	}

	/**
	 * @param fingerprintSupport the fingerprintSupport to set
	 */
	public void setFingerprintSupport(boolean fingerprintSupport) {
		this.fingerprintSupport = fingerprintSupport;
	}
}
