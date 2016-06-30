package com.yuanluesoft.appraise.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.pojo.AppraiseVote;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseViewServiceImpl extends ViewServiceImpl {
	private AccessControlService accessControlService; //访问控制服务
	private OrgService orgService; //组织机构服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		List acl = accessControlService.getAcl("appraise", sessionInfo);
		if(acl==null || !acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //不是管理员
			if(view.getPojoClassName().equals(AppraiseVote.class.getName())) { //评议结果
				//检查评议单位
				String hql = "select UnitAppraise.unitId" +
							 " from UnitAppraise UnitAppraise" +
							 " where UnitAppraise.id=" + RequestUtils.getParameterLongValue(request, "unitAppraiseId");
				long unitId = ((Number)getDatabaseService().findRecordByHql(hql)).longValue();
				//检查用户的评议权限
				if(!orgService.checkParentDirectoryPopedom(unitId, "appraiseManager,appraiseTransactor", sessionInfo)) {
					view.addWhere("AppraiseVote.id=-1");
				}
			}
			else {
				List ids = orgService.listDirectoryIds("area", "appraiseManager", true, sessionInfo, 0, 1);
				String pojoClassName = view.getPojoClassName();
				pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
				if(ids==null || ids.isEmpty()) {
					view.addWhere(pojoClassName + ".id=-1");
				}
				else {
					view.addWhere(pojoClassName + ".areaId=" + ids.get(0));
				}
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

}