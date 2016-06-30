/*
 * Created on 2006-9-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.actions.archivescode;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.archives.actions.base.ArchivesConfigAction;
import com.yuanluesoft.archives.forms.ArchivesCode;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public class ArchivesCodeAction extends ArchivesConfigAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ArchivesCode codeForm = (ArchivesCode)form;
		codeForm.setArchivesType("文书档案");
		if(codeForm.getCodeConfig()==null || codeForm.getCodeConfig().equals("")) {
			codeForm.setCodeConfig("<全宗号>-<归档年度>-<保管期限>-<机构或问题>-<序号>");
		}
	}
}
