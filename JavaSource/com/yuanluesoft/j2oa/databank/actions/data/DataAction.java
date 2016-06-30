/*
 * Created on 2006-5-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.j2oa.databank.actions.data;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.databank.forms.Data;
import com.yuanluesoft.j2oa.databank.pojo.DatabankData;
import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 *
 * @author linchuan
 *
 */
public class DataAction extends FormAction {
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        Data dataForm = (Data)form;
        DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
        if(OPEN_MODE_CREATE.equals(openMode)) {
        	if(!databankDirectoryService.checkPopedom(dataForm.getDirectoryId(), "manager", sessionInfo)) {
        		throw new PrivilegeException();
        	}
        	return RecordControlService.ACCESS_LEVEL_EDITABLE;
        }
        DatabankDataService databankDataService = (DatabankDataService)getService("databankDataService");
        try {
			char accessLevel = databankDataService.getDataAccessLevel(dataForm.getId(), sessionInfo);
			if(accessLevel>RecordControlService.ACCESS_LEVEL_NONE) {
				return accessLevel;
	        }
		}
		catch (ServiceException e) {
			
		}
        throw new PrivilegeException();
        
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
	    Data dataForm = (Data)form;
	    DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
	    //设置所在目录全称
        dataForm.setDirectoryName(databankDirectoryService.getDirectoryFullName(dataForm.getDirectoryId(), "/", null));
        //创建人和创建时间
        dataForm.setCreated(DateTimeUtils.now());
        dataForm.setCreator(sessionInfo.getUserName());
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Data dataForm = (Data)form;
		DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
		//设置所在目录全称
        dataForm.setDirectoryName(databankDirectoryService.getDirectoryFullName(dataForm.getDirectoryId(), "/", null));
        //设置资料访问者
        dataForm.setDataVisitors(getRecordControlService().getVisitors(dataForm.getId(), DatabankData.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    if(OPEN_MODE_CREATE.equals(openMode)) {
	    	DatabankData data = (DatabankData)record;
            data.setCreatorId(sessionInfo.getUserId());
            data.setCreator(sessionInfo.getUserName());
            data.setCreated(DateTimeUtils.now());
        }
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存资料访问者
		Data dataForm = (Data)form;
		getRecordControlService().updateVisitors(dataForm.getId(), DatabankData.class.getName(), dataForm.getDataVisitors(), RecordControlService.ACCESS_LEVEL_READONLY);
		return record;
    }
}