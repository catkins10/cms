package com.yuanluesoft.fet.tradestat.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.pojo.FetCompany;
import com.yuanluesoft.fet.tradestat.pojo.FetCounty;
import com.yuanluesoft.fet.tradestat.pojo.FetDevelopmentArea;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class FetCompanyServiceImpl extends BusinessServiceImpl implements FetCompanyService, MemberService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.FetCompanyService#listPermitCounties(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listPermitCounties(SessionInfo sessionInfo) throws ServiceException {
		String hql = "select distinct FetCounty from FetCounty FetCounty, FetCountyPrivilege FetCountyPrivilege" +
					 "   where FetCounty.id=FetCountyPrivilege.recordId" +
					 "   and FetCountyPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.FetCompanyService#listPermitDevelopmentAreas(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listPermitDevelopmentAreas(SessionInfo sessionInfo) throws ServiceException {
		String hql = "select distinct FetDevelopmentArea from FetDevelopmentArea FetDevelopmentArea, FetDevelopmentAreaPrivilege FetDevelopmentAreaPrivilege" +
					 "   where FetDevelopmentArea.id=FetDevelopmentAreaPrivilege.recordId" +
					 "   and FetDevelopmentAreaPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.FetLoginService#cryptPassword(java.lang.String)
	 */
	public String cryptPassword(String password) throws ServiceException {
		try {
			password = Encoder.getInstance().md5Encode(password);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		return password.toLowerCase().substring(8, 24);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		Record org = getOrg(loginName);
		if(org==null) {
			return null;
		}
		FetSessionInfo sessionInfo = new FetSessionInfo();
		sessionInfo.setUserId(org.getId());
		sessionInfo.setDepartmentId(-1);
		try {
			sessionInfo.setUserName((String)PropertyUtils.getProperty(org, "name"));
			sessionInfo.setCode((String)PropertyUtils.getProperty(org, "code"));
		}
		catch(Exception e) {
			
		}
		sessionInfo.setLoginName(loginName);
		sessionInfo.setUserType(org instanceof FetCompany ? USER_TYPE_COMPANY : (org instanceof FetCounty ? USER_TYPE_COUNTY : USER_TYPE_DEVELOPMENT_AREA));
		return sessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		//获取公司
		FetCompany fetCompany = (FetCompany)getDatabaseService().findRecordById(FetCompany.class.getName(), memberId);
		Member member = new Member();
		if(fetCompany!=null) {
			try {
				PropertyUtils.copyProperties(member, fetCompany);
			}
			catch(Exception e) {
				
			}
			member.setMemberType(USER_TYPE_COMPANY);
			member.setOriginalRecord(fetCompany);
		}
		else {
			//获取县区
			FetCounty fetCounty = (FetCounty)getDatabaseService().findRecordById(FetCounty.class.getName(), memberId);
			if(fetCounty!=null) {
				try {
					PropertyUtils.copyProperties(member, fetCounty);
				}
				catch(Exception e) {
					
				}
				member.setMemberType(USER_TYPE_COUNTY);
				member.setOriginalRecord(fetCounty);
			}
			else {
				//获取开发区
				FetDevelopmentArea fetDevelopmentArea = (FetDevelopmentArea)getDatabaseService().findRecordById(FetDevelopmentArea.class.getName(), memberId);
				if(fetDevelopmentArea==null) {
					return null;
				}
				try {
					PropertyUtils.copyProperties(member, fetDevelopmentArea);
				}
				catch(Exception e) {
					
				}
				member.setMemberType(USER_TYPE_DEVELOPMENT_AREA);
				member.setOriginalRecord(fetDevelopmentArea);
			}
		}
		return member;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查企业表
		String hql = "from FetCompany FetCompany" + 
					 " where FetCompany.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
					 " or FetCompany.code='" + JdbcUtils.resetQuot(loginName) + "'" +
					 " or FetCompany.name='" + JdbcUtils.resetQuot(loginName) + "'";
		FetCompany company = (FetCompany)getDatabaseService().findRecordByHql(hql);
		if(company!=null) {
			return true;
		}
		//检查区县表
		hql = "from FetCounty FetCounty" + 
			  " where FetCounty.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
			  " or FetCounty.name='" + JdbcUtils.resetQuot(loginName) + "'";
		FetCounty county = (FetCounty)getDatabaseService().findRecordByHql(hql);
		if(county!=null) {
			return true;
		}
		//检查开发区表
		hql = "from FetDevelopmentArea FetDevelopmentArea" + 
			  " where FetDevelopmentArea.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
			  " or FetDevelopmentArea.name='" + JdbcUtils.resetQuot(loginName) + "'";
		FetDevelopmentArea developmentArea = (FetDevelopmentArea)getDatabaseService().findRecordByHql(hql);
		if(developmentArea!=null) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		password = cryptPassword(password);
		Record org = getOrg(loginName);
		if(org==null) {
			return null;
		}
		String orgPassword = null;
		try {
			orgPassword = (String)PropertyUtils.getProperty(org, "password");
		} 
		catch(Exception e) {
			
		}
		return new MemberLogin(loginName, org.getId(), (org instanceof FetCompany ? USER_TYPE_COMPANY : (org instanceof FetCounty ? USER_TYPE_COUNTY : USER_TYPE_DEVELOPMENT_AREA)), null, !orgPassword.equals(password));
	}
	
	/**
	 * 按用户名获取机构
	 * @param loginName
	 * @return
	 */
	private Record getOrg(String loginName) {
		//检查企业表
		String hql = "from FetCompany FetCompany" + 
					 " where (FetCompany.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
					 " or FetCompany.code='" + JdbcUtils.resetQuot(loginName) + "'" +
					 " or FetCompany.name='" + JdbcUtils.resetQuot(loginName) + "')" +
					 " and FetCompany.approvalPass='1'";
		FetCompany company = (FetCompany)getDatabaseService().findRecordByHql(hql);
		if(company!=null) {
			return company;
		}
		//检查区县表
		hql = "from FetCounty FetCounty" + 
			  " where FetCounty.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
			  " or FetCounty.name='" + JdbcUtils.resetQuot(loginName) + "'";
		FetCounty county = (FetCounty)getDatabaseService().findRecordByHql(hql);
		if(county!=null) {
			return county;
		}
		//检查开发区表
		hql = "from FetDevelopmentArea FetDevelopmentArea" + 
			  " where FetDevelopmentArea.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
			  " or FetDevelopmentArea.name='" + JdbcUtils.resetQuot(loginName) + "'";
		FetDevelopmentArea developmentArea = (FetDevelopmentArea)getDatabaseService().findRecordByHql(hql);
		if(developmentArea!=null) {
			return developmentArea;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		Record org = getOrg(loginName);
		if(org==null) {
			return false;
		}
		try {
			if(validateOldPassword && !cryptPassword(oldPassword).equals(PropertyUtils.getProperty(org, "password"))) {
				throw(new WrongPasswordException()); //密码错误
			}
			PropertyUtils.setProperty(org, "password", cryptPassword(newPassword));
			update(org);
		}
		catch(WrongPasswordException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("counties".equals(itemsName)) { //区县列表
			return getDatabaseService().findRecordsByHql("select FetCounty.name, FetCounty.id from FetCounty FetCounty order by FetCounty.name");
		}
		else if("developmentAreas".equals(itemsName)) { //开发区列表
			List items = getDatabaseService().findRecordsByHql("select FetDevelopmentArea.name, FetDevelopmentArea.id from FetDevelopmentArea FetDevelopmentArea order by FetDevelopmentArea.name");
			if(items==null) {
				items = ListUtils.generateList(new String[] {"无", "0"});
			}
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
}