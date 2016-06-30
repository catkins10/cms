package com.yuanluesoft.jeaf.dialog.actions.inputdialog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.dialog.forms.InputDialog;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.callback.GenerateBeanCallback;

/**
 * 
 * @author linchuan
 *
 */
public class OpenInputDialog extends DialogFormAction {
    
    public OpenInputDialog() {
		super();
		anonymousAlways = true;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		InputDialog inputDialog = (InputDialog)form;
		inputDialog.setInputs(new ArrayList());
		for(int i=0; ; i++) {
			String fieldDefine = request.getParameter("field" + i);
			if(fieldDefine==null || fieldDefine.isEmpty()) {
				break;
			}
			Field field = (Field)BeanUtils.generateBeanByProperties(Field.class, fieldDefine, new GenerateBeanCallback() {
				public void setPropertyValue(Object bean, String propertyName, String propertyValue) {
					Field field = (Field)bean;
					field.setParameter(propertyName, propertyValue);
				}
			});
			inputDialog.getFormDefine().getFields().add(field);
			inputDialog.getInputs().add(field);
		}
	}
}