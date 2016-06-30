package com.yuanluesoft.cms.templatemanage.actions.insertrecordlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.templatemanage.forms.InsertRecordList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends com.yuanluesoft.jeaf.htmleditor.actions.editordialog.Load {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
    	InsertRecordList insertForm = (InsertRecordList)form;
    	PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
    	//获取视图定义
    	View view = pageDefineService.getRecordList(insertForm.getRedirectApplicationName()==null ? insertForm.getApplicationName() : insertForm.getRedirectApplicationName(),
    												insertForm.getRedirectRecordListName()==null ? insertForm.getRecordListName() : insertForm.getRedirectRecordListName(),
    												insertForm.isPrivateRecordList(),
    												insertForm.getRecordClassName(),
    												sessionInfo);
    	insertForm.setView(view);
    	insertForm.setViewTitle(view.getTitle());
    	insertForm.setTemplateExtendURL((String)view.getExtendParameter("templateExtendURL"));
    	insertForm.setEmptyPrompt((String)view.getExtendParameter("emptyPrompt"));
    	insertForm.setFormTitle(view.getTitle() + " - 记录列表");
	}
}