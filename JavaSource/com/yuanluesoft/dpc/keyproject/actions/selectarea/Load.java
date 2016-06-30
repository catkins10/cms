package com.yuanluesoft.dpc.keyproject.actions.selectarea;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.forms.SelectArea;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectParameter;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
import com.yuanluesoft.jeaf.dialog.actions.listdialog.OpenListDialog;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends OpenListDialog {
    
  	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		SelectArea selectForm = (SelectArea)dialog;
    	KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
		KeyProjectParameter parameter = keyProjectService.getKeyProjectParameter();
		selectForm.setItemsText(parameter.getArea());
	}
}