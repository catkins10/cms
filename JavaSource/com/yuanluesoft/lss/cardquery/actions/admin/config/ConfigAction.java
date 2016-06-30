package com.yuanluesoft.lss.cardquery.actions.admin.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.lss.cardquery.forms.admin.CardQueryConfig;



/**
 * 
 * @author linchuan
 *
 */
public class ConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_importData")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(!OPEN_MODE_CREATE.equals(openMode) && acl.contains(AccessControlService.ACL_APPLICATION_VISITOR)) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
//      只有管理员有删除权限  
		if(acl.contains("application_manager")){
            return ;
		}else throw new PrivilegeException();
	}
	
	public List validateForm(ActionForm formToValidate, boolean forceValidateCode, HttpServletRequest request) throws SystemUnregistException {
		List error= super.validateForm(formToValidate, forceValidateCode, request);
		if(formToValidate.getAct().equals(FormAction.OPEN_MODE_CREATE)){
		CardQueryConfig cardQueryConfig = (CardQueryConfig)formToValidate;
        DatabaseService databaseService=(DatabaseService) getService("databaseService");
//      没有配置该制卡类型，不导入
        List oldRecord=databaseService.findRecordsByHql("select CardQueryConfig.cardType from com.yuanluesoft.lss.cardquery.pojo.CardQueryConfig CardQueryConfig " +
        		" where  CardQueryConfig.cardType='"+cardQueryConfig.getCardType()+"'");
        if(oldRecord!=null&&!oldRecord.isEmpty()){//该制卡类型已存在
        	error.add("该制卡类型已存在，不能重复新增");
        }
		}
		 return error;
	}
	
	
}