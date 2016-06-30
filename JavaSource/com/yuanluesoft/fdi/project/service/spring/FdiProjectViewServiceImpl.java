package com.yuanluesoft.fdi.project.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fdi.industry.service.FdiIndustryService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class FdiProjectViewServiceImpl extends ViewServiceImpl {
	private AccessControlService accessControlService; //访问控制服务
	private FdiIndustryService fdiIndustryService; //行业服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		List industryIds = fdiIndustryService.listIndustryIds(true, sessionInfo); //获取有编辑权限的行业ID列表
		if(industryIds==null || industryIds.isEmpty()) { //对行业没有编辑权限
			ListUtils.removeObjectByProperty(view.getActions(), "title", "添加项目"); //删除“添加项目”操作
		}
		//检查用户对应用的权限
		List acl = accessControlService.getAcl(view.getApplicationName(), sessionInfo);
		if(acl==null || !acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //不是管理员,管理员允许查看全部
			//获取用户有访问权限的行业ID列表
			industryIds = fdiIndustryService.listIndustryIds(false, sessionInfo); //获取有访问 权限的行业ID列表
			String pojoClassName = view.getPojoClassName();
			pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
			if(industryIds==null || industryIds.isEmpty()) { //对行业没有权限
				view.addWhere(pojoClassName + ".id=-1"); //不返回任何记录
			}
			else if(pojoClassName.equals("FdiProject")) { //项目视图
				view.addWhere("FdiProject.industryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(industryIds, ",", false)) + ")");
			}
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/**
	 * @return the accessControlService
	 */
	public AccessControlService getAccessControlService() {
		return accessControlService;
	}

	/**
	 * @param accessControlService the accessControlService to set
	 */
	public void setAccessControlService(AccessControlService accessControlService) {
		this.accessControlService = accessControlService;
	}

	/**
	 * @return the fdiIndustryService
	 */
	public FdiIndustryService getFdiIndustryService() {
		return fdiIndustryService;
	}

	/**
	 * @param fdiIndustryService the fdiIndustryService to set
	 */
	public void setFdiIndustryService(FdiIndustryService fdiIndustryService) {
		this.fdiIndustryService = fdiIndustryService;
	}
}