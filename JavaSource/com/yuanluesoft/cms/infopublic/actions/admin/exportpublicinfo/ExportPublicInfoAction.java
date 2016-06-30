package com.yuanluesoft.cms.infopublic.actions.admin.exportpublicinfo;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.forms.admin.ExportPublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class ExportPublicInfoAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/infopublic";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/exportPublicInfo";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		ExportPublicInfo exportForm = (ExportPublicInfo)viewForm;
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		if(!publicDirectoryService.checkPopedom(exportForm.getDirectoryId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		super.initForm(viewForm, request, sessionInfo);
		viewForm.setFormTitle("信息导出");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		ExportPublicInfo exportForm = (ExportPublicInfo)viewForm;
		if(exportForm.getBeginDate()!=null && exportForm.getEndDate()!=null) {
			String where = "PublicInfo.issueTime>=DATE(" + DateTimeUtils.formatDate(exportForm.getBeginDate(), null) + ")" +
						   " and PublicInfo.issueTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(exportForm.getEndDate(), Calendar.DAY_OF_MONTH, 1), null) + ")";
			view.addWhere(where);
		}
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		view.addJoin(", PublicDirectorySubjection PublicDirectorySubjection");
		String where = "subjections.directoryId=PublicDirectorySubjection.directoryId" +
					   " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(publicDirectoryService.getChildDirectoryIds(exportForm.getDirectoryId() + "", "category,info")) + ")";
		view.addWhere(where);
	}
}