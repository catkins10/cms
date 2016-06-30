package com.yuanluesoft.cms.onlineservice.actions.selectitemtype;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.dialog.actions.listdialog.OpenListDialog;
import com.yuanluesoft.jeaf.dialog.forms.ListDialog;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author chuan
 *
 */
public class Load extends OpenListDialog {
    
  	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		ListDialog selectForm = (ListDialog)dialog;
    	selectForm.setItemsText(FieldUtils.getSelectItemsText(FieldUtils.getRecordField(OnlineServiceItem.class.getName(), "itemType", request), null, request).replaceAll("\\\\0", ","));
	}
}