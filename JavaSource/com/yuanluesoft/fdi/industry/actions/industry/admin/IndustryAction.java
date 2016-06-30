package com.yuanluesoft.fdi.industry.actions.industry.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.fdi.industry.forms.admin.Industry;
import com.yuanluesoft.fdi.industry.pojo.FdiIndustry;
import com.yuanluesoft.fdi.industry.service.FdiIndustryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class IndustryAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //只允许管理员编辑和修改
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		RecordControlService recordControlService = getRecordControlService();
		Industry industryForm = (Industry)form;
		industryForm.setEditors(recordControlService.getVisitors(industryForm.getId(), FdiIndustry.class.getName(), RecordControlService.ACCESS_LEVEL_EDITABLE)); //编辑权限
		industryForm.setReaders(recordControlService.getVisitors(industryForm.getId(), FdiIndustry.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY)); //查询权限
	}
	
	 

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Industry industryForm = (Industry)form;
		FdiIndustry industry = (FdiIndustry)record;
		//设置父分类名称
		long parentCategoryId = industry==null ? industryForm.getParentCategoryId() : industry.getParentCategoryId();
		if(parentCategoryId>0) {
			FdiIndustryService fdiIndustryService = (FdiIndustryService)getService("fdiIndustryService");
			industryForm.setParentCategory(fdiIndustryService.getIndustry(parentCategoryId).getCategory());
			//删除“添加下级分类”按钮
			industryForm.getFormActions().removeFormAction("添加下级分类");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		((Industry)newForm).setParentCategoryId(((Industry)currentForm).getParentCategoryId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		FdiIndustry industry = (FdiIndustry)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		RecordControlService recordControlService = getRecordControlService();
		Industry industryForm = (Industry)form;
		recordControlService.updateVisitors(industry.getId(), industry.getClass().getName(), industryForm.getEditors(), RecordControlService.ACCESS_LEVEL_EDITABLE); //编辑权限
		recordControlService.updateVisitors(industry.getId(), industry.getClass().getName(), industryForm.getReaders(), RecordControlService.ACCESS_LEVEL_READONLY); //查询权限
		return industry;
	}
}