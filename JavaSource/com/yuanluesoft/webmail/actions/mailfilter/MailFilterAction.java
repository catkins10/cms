/*
 * Created on 2006-5-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.actions.mailfilter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.webmail.pojo.MailFilter;
import com.yuanluesoft.webmail.service.MailFilterService;
import com.yuanluesoft.webmail.service.MailboxService;

/**
 *
 * @author linchuan
 *
 */
public class MailFilterAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
        ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
        View view = viewDefineService.getView("webmail", "inbox", sessionInfo);
        com.yuanluesoft.webmail.forms.MailFilter mailFilterForm = (com.yuanluesoft.webmail.forms.MailFilter)form;
        mailFilterForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_CONDITION);
        ((ViewService)getService("viewService")).retrieveViewPackage(mailFilterForm.getViewPackage(), view, 0, false, false, false, request, sessionInfo);
        ListUtils.removeObjectByProperty(view.getColumns(), "name", "date");
        //获取收件箱列表
        MailboxService mailboxService = (MailboxService)getService("mailboxService");
        mailFilterForm.setReceiveMailboxes(mailboxService.listMailboxes(true, false, sessionInfo));
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
	    com.yuanluesoft.webmail.forms.MailFilter mailFilterForm = (com.yuanluesoft.webmail.forms.MailFilter)form;
	    if(mailFilterForm.getEnable()==0) {
	    	mailFilterForm.setEnable('1');
	    }
        mailFilterForm.setActionType(MailFilterService.ACTION_TYPE_DELETE);
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        com.yuanluesoft.webmail.forms.MailFilter mailFilterForm = (com.yuanluesoft.webmail.forms.MailFilter)form;
        MailFilter mailFilter = (MailFilter)record;
        String conditions = mailFilter.getConditions();
        if(conditions!=null && !conditions.equals("")) {
            int index = conditions.indexOf("\r\n\r\n");
            mailFilterForm.getViewPackage().setSearchConditionsDescribe(conditions.substring(0, index));
            mailFilterForm.getViewPackage().setSearchConditions(conditions.substring(index + 4));
        }
        //设置操作
        if(mailFilter.getAction()==null || mailFilter.getAction().equals("")) {
            mailFilterForm.setActionType(MailFilterService.ACTION_TYPE_DELETE);
        }
        else {
            String[] values = mailFilter.getAction().split("\\x7c");
            mailFilterForm.setActionType(values[1]);
            if(values.length>2) {
                mailFilterForm.setSelectedMailboxId(Long.parseLong(values[2]));
            }
        }
    }
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        com.yuanluesoft.webmail.forms.MailFilter mailFilterForm = (com.yuanluesoft.webmail.forms.MailFilter)form;
        MailFilter mailFilter = (MailFilter)record;
        mailFilter.setUserId(sessionInfo.getUserId()); //设置用户ID
        //设置过滤条件
        if(mailFilterForm.getViewPackage().getSearchConditions()==null || mailFilterForm.getViewPackage().getSearchConditions().equals("")) {
            mailFilter.setConditions(null);
        }
        else {
            //保存过滤条件,用两个空行分隔条件描述和条件列表
            mailFilter.setConditions(mailFilterForm.getViewPackage().getSearchConditionsDescribe() + "\r\n\r\n" + mailFilterForm.getViewPackage().getSearchConditions());
        }
        //设置执行的操作
        if(MailFilterService.ACTION_TYPE_DELETE.equals(mailFilterForm.getActionType())) {
            mailFilter.setAction("直接删除|delete");
        }
        else {
            //获取邮箱名称
            MailboxService mailboxService = (MailboxService)getService("mailboxService");
            mailFilter.setAction("发送到邮箱" + mailboxService.loadMailbox(mailFilterForm.getSelectedMailboxId(), sessionInfo).getMailboxName() + "|move|" + mailFilterForm.getSelectedMailboxId());
        }
        return super.saveRecord(form, record, openMode, request, response, sessionInfo);
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        if(!OPEN_MODE_CREATE.equals(openMode)) {
            MailFilter mailFilter = (MailFilter)record;
            if(mailFilter.getUserId()!=sessionInfo.getUserId()) {
                throw new PrivilegeException();
            }
        }
        return RecordControlService.ACCESS_LEVEL_EDITABLE;
    }
}