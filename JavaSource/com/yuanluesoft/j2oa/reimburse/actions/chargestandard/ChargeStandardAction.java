/*
 * Created on 2006-6-21
 *
 */
package com.yuanluesoft.j2oa.reimburse.actions.chargestandard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.reimburse.forms.ChargeStandardForm;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseChargeStandard;
import com.yuanluesoft.j2oa.reimburse.service.ChargeCategoryService;
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
public class ChargeStandardAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, boolean)
     */
    public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        ChargeStandardForm standardForm = (ChargeStandardForm)form;
        ReimburseChargeStandard standard = (ReimburseChargeStandard)record;
        //设置费用类别
        standardForm.setSelectedCategory(standard.getChargeCategory().getCategory());
        //设置单位
        standardForm.setUnit(standard.getChargeCategory().getUnit());
    }
    
    /*
     * (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
        //获取费用类别列表
        ChargeStandardForm standardForm = (ChargeStandardForm)form;
        ChargeCategoryService chargeCategoryService = (ChargeCategoryService)getService("chargeCategoryService");
        standardForm.setChargeCategories(chargeCategoryService.listChargeCategories());
    }
}
