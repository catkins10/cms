package com.yuanluesoft.aic.barcode.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.aic.barcode.model.BarcodeCompanySessionInfo;
import com.yuanluesoft.aic.barcode.pojo.BarcodeCompany;
import com.yuanluesoft.aic.barcode.service.BarcodeCompanyService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.security.service.exception.SecurityException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BarcodeCompanyServiceImpl extends BusinessServiceImpl implements BarcodeCompanyService {
	private MemberServiceList memberServiceList;
	private CryptService cryptService; //加/解密服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		BarcodeCompany company = (BarcodeCompany)super.load(recordClass, id);
		if(company!=null) {
			if(company.getPassword()==null || company.getPassword().equals("")) {
				company.setPassword(encryptPassword(company.getId(), company.getLoginName()));
	    	}
			company.setPassword("{" + company.getPassword() + "}");
		}
		return company;
	}


	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		BarcodeCompany company = (BarcodeCompany)record;
		//用户名不区分大小写
		company.setLoginName(company.getLoginName().toLowerCase()); //转换为小写
		company.setPassword(encryptPassword(company.getId(), company.getPassword()));
		super.save(record);
		company.setPassword("{" + company.getPassword() + "}");
		return company;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		BarcodeCompany company = (BarcodeCompany)record;
		//用户名不区分大小写
		company.setLoginName(company.getLoginName().toLowerCase()); //转换为小写
		company.setPassword(encryptPassword(company.getId(), company.getPassword()));
		super.update(record);
		company.setPassword("{" + company.getPassword() + "}");
		return company;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		List errors = super.validateBusiness(record, isNew);
		BarcodeCompany company = (BarcodeCompany)record;
		if(memberServiceList.isLoginNameInUse(company.getLoginName(), company.getId())) {
			if(errors==null) {
				errors = new ArrayList();
			}
			errors.add("登录用户名已经被使用");
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		List errors = super.validateDataIntegrity(record, isNew);
		//检查名称是否重复
		BarcodeCompany company = (BarcodeCompany)record;
		String hql = "select BarcodeCompany.id from BarcodeCompany BarcodeCompany where BarcodeCompany.name='" + JdbcUtils.resetQuot(company.getName()) + "' and BarcodeCompany.id!=" + company.getId();
		if(getDatabaseService().findRecordByHql(hql)!=null) {
			if(errors==null) {
				errors = new ArrayList();
			}
			errors.add("“" + company.getName() + "”已经被注册过");
		}
		return errors;
	}

	/**
	 * 口令加密
	 * @param companyId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long companyId, String password) throws ServiceException {
		if(password.startsWith("{") && password.endsWith("}")) {
			return password.substring(1, password.length() - 1);
		}
	    try {
	        return cryptService.encrypt(password, "" + companyId, false);
	    } 
	    catch (SecurityException e) {
	        throw new ServiceException();
	    }
	}

	/**
	 * 口令解密
	 * @param companyId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String decryptPassword(long companyId, String password) throws ServiceException {
		try {
			return cryptService.decrypt(password, "" + companyId, false);
		}
		catch (SecurityException e) {
			throw new ServiceException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		String hql = "from BarcodeCompany BarcodeCompany where BarcodeCompany.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'";
		BarcodeCompany company = (BarcodeCompany)getDatabaseService().findRecordByHql(hql); //按登录用户名查找用户
		if(company==null) {
			return false;
		}
		if(validateOldPassword) {
			if(company.getPassword()==null || company.getPassword().equals("")) {
			    if(!oldPassword.equals(company.getLoginName())) {
			        throw(new WrongPasswordException()); //密码错误
			    }
			}
			else if(!encryptPassword(company.getId(), oldPassword).equals(company.getPassword())) {
				throw(new WrongPasswordException()); //密码错误
			}
		}
		//设置新密码
		company.setPassword(encryptPassword(company.getId(), newPassword));
		getDatabaseService().updateRecord(company);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			BarcodeCompany company = (BarcodeCompany)getDatabaseService().findRecordByHql("from BarcodeCompany BarcodeCompany where BarcodeCompany.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'"); //按登录用户名查找用户
			if(company==null) { //不是企业用户
				return null;
			}
			BarcodeCompanySessionInfo sessionInfo = new BarcodeCompanySessionInfo();
			//设置用户信息
			sessionInfo.setCode(company.getCode());
			sessionInfo.setLoginName(company.getLoginName());
			sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER);
			String password = company.getPassword();
			if(password==null || password.equals("")) {
				password = company.getLoginName();
			}
			else {//口令解密
				password = decryptPassword(company.getId(), password);
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(company.getName());
			sessionInfo.setUserId(company.getId());
			String orgIds = "0," + company.getId();
			sessionInfo.setDepartmentId(company.getId());
			sessionInfo.setDepartmentName(company.getName());
			//设置部门信息
			sessionInfo.setDepartmentIds(orgIds);
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select BarcodeCompany.id from BarcodeCompany BarcodeCompany where BarcodeCompany.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
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
		loginName = loginName.toLowerCase();
    	//用户校验
		String hql = "from BarcodeCompany BarcodeCompany" +
					 " where BarcodeCompany.loginName='" + JdbcUtils.resetQuot(loginName) + "'";
    	BarcodeCompany company = (BarcodeCompany)getDatabaseService().findRecordByHql(hql); //按登录用户名查找用户
    	if(company==null) {
    		return null;
    	}
    	if(company.getIsHalt()=='1') {
    		throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT); //帐号停用
    	}
    	//密码解密
    	String correctPassword = loginName; //正确的密码
	    if(company.getPassword()!=null && !company.getPassword().equals("")) { //注:密码为空时,要求用户输入的密码必须和用户名相同
	    	correctPassword =  cryptService.decrypt(company.getPassword(), "" + company.getId(), false);
	    }
		return new MemberLogin(company.getLoginName(), company.getId(), PersonService.PERSON_TYPE_EMPLOYEE, correctPassword, !passwordMatcher.matching(correctPassword, password));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		BarcodeCompany company = (BarcodeCompany)load(BarcodeCompany.class, memberId);
		if(company==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, company);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PersonService.PERSON_TYPE_EMPLOYEE);
		member.setOriginalRecord(company);
		return member;
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
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}


	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}
}