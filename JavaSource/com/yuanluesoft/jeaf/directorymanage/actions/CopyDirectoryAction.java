package com.yuanluesoft.jeaf.directorymanage.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.forms.CopyDirectory;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 目录拷贝
 * @author linchuan
 *
 */
public abstract class CopyDirectoryAction extends FormAction {
	
	/**
	 * 获取目录服务
	 * @return
	 * @throws SystemUnregistException
	 */
	protected abstract DirectoryService getDirectoryService() throws SystemUnregistException;
	
	/**
	 * 获取目标目录类型
	 * @param fromDirectoryType
	 * @return
	 * @throws SystemUnregistException
	 */
	protected abstract String getTargetDirectoryTypes(String fromDirectoryType) throws SystemUnregistException;
	
	/**
	 * 获取有拷贝权限的权限名称列表
	 * @return
	 */
	protected String getCopyablePopedomNames() {
		return "manager"; //默认"管理"权限
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		CopyDirectory copyForm = (CopyDirectory)form;
		if(!getDirectoryService().checkPopedom(copyForm.getFromDirectoryId(), getCopyablePopedomNames(), sessionInfo) ||
		   !getDirectoryService().checkPopedom(copyForm.getToDirectoryId(), getCopyablePopedomNames(), sessionInfo)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	/**
	 * 目录拷贝
	 * @param copyForm
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected Directory copyDirectory(long fromDirectoryId, long toDirectoryId, String newDirectoryName, ActionForm form, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        return getDirectoryService().copyDirectory(fromDirectoryId, toDirectoryId, newDirectoryName, sessionInfo.getUserName(), sessionInfo.getUserId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		CopyDirectory copyForm = (CopyDirectory)form;
		//获取源目录名称
        Directory directory = getDirectoryService().getDirectory(copyForm.getFromDirectoryId());
        copyForm.setFromDirectoryName(getDirectoryService().getDirectoryFullName(copyForm.getFromDirectoryId(), "/", null)); //源目录名称
        copyForm.setToDirectoryTypes(getTargetDirectoryTypes(directory.getDirectoryType())); //目标目录类型
        if(copyForm.getNewDirectoryName()==null || copyForm.getNewDirectoryName().isEmpty()) {
        	copyForm.setNewDirectoryName(directory.getDirectoryName()); //新目录名称
        }
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		CopyDirectory copyForm = (CopyDirectory)form;
        String[] names = copyForm.getNewDirectoryName().replaceAll("，", ",").split(",");
        for(int i=0; i<names.length; i++) {
        	copyDirectory(copyForm.getFromDirectoryId(), copyForm.getToDirectoryId(), names[i].trim(), form, response, sessionInfo);
        }
        return null;
    }
}