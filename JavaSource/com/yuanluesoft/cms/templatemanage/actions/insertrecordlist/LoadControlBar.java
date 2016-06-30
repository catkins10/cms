package com.yuanluesoft.cms.templatemanage.actions.insertrecordlist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.templatemanage.forms.RecordListControlBar;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author chuan
 *
 */
public class LoadControlBar extends com.yuanluesoft.jeaf.htmleditor.actions.editordialog.Load {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		RecordListControlBar controlBarForm = (RecordListControlBar)form;
		controlBarForm.setControlBarStyles(new ArrayList());
		String[] styleNames = {"number", "roundDot", "squareDot", "rightSmallImage", "bottomSmallImage"};
		for(int i=0; i<styleNames.length; i++) {
			controlBarForm.getControlBarStyles().add(new Element(styleNames[i], RecordListUtils.getDefaultControlBarFormat(styleNames[i])));
		}
	}
}