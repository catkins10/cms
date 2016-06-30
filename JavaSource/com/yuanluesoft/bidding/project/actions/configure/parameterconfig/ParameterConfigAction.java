package com.yuanluesoft.bidding.project.actions.configure.parameterconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.project.forms.admin.ParameterConfig;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ParameterConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ParameterConfig configForm = (ParameterConfig)form;
		configForm.setBiddingNumberFormat("<地区简称><类别简称>招<年*4><序号*3>"); //项目招标编号规则,<类别简称><分类简称>招 <年*4><序号*3>
		configForm.setSignUpNumberFormat("<年*4><随机数*8>"); //投标报名号规则,<项目编号><随机数*5>
		configForm.setNoticeNumberFormat("<地区简称><类别简称>交<年*4><序号*3>"); //中标通知书编号规则,榕市建安交2009001
		configForm.setDeclareNumberFormat("<地区简称><类别简称>报<年*4><序号*3>"); //项目报建编号规则
		configForm.setDeclareReceiveNumberFormat("<地区简称><类别简称><年*4><序号*3>"); //项目收件编号规则
	}
}
