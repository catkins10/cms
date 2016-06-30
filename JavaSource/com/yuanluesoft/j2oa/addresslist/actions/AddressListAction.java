/*
 * Created on 2006-5-25
 *
 */
package com.yuanluesoft.j2oa.addresslist.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.addresslist.forms.AddressListForm;
import com.yuanluesoft.j2oa.addresslist.pojo.AddressList;
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
public class AddressListAction extends FormAction {
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        AddressList addressList = (AddressList)record;
        if(OPEN_MODE_CREATE.equals(openMode)) {
            addressList.setCreatorId(sessionInfo.getUserId());
            AddressListForm addressListForm = (AddressListForm)form;
            addressList.setIsPersonal(addressListForm.isCommon() ? '0' : '1');
        }
        return super.saveRecord(form, record, openMode, request, response, sessionInfo);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
            AddressListForm addressListForm = (AddressListForm)form;
            if(addressListForm.isCommon() && !acl.contains("manageUnit_commonaddresslist")) {
                //创建公共通讯录,但没有管理权限
                throw new PrivilegeException();
            }
            return RecordControlService.ACCESS_LEVEL_EDITABLE;
        }
        else {
            AddressList addressList = (AddressList)record;
            if(addressList.getIsPersonal()=='1') { //个人通讯录
                if(sessionInfo.getUserId()==addressList.getCreatorId()) {
                    return RecordControlService.ACCESS_LEVEL_EDITABLE;
                }
                else if(acl.contains("application_manager")) { //系统管理员
                    return RecordControlService.ACCESS_LEVEL_READONLY;
                }
                throw new PrivilegeException();
            }
            else { //公共通讯录
                return acl.contains("manageUnit_commonaddresslist") ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
            }
        }
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		form.getTabs().addTab(-1, "company", "单位信息", null, false);
		form.getTabs().addTab(-1, "home", "家庭信息", null, false);
		form.getTabs().addTab(-1, "log", "来往记录", null, false);
	}
}