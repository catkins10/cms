package com.yuanluesoft.cms.photocollect.actions.photocollect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.photocollect.pojo.PhotoCollect;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class PhotoCollectAction extends PublicServiceAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.ApplicationAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PhotoCollect photoCollect = (PhotoCollect)record;
		com.yuanluesoft.cms.photocollect.forms.PhotoCollect photoCollectForm = (com.yuanluesoft.cms.photocollect.forms.PhotoCollect)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			photoCollectForm.setSubForm("photoCollect"); //提交页面
		}
		else if(photoCollect.getPublicPass()=='1') { //已公开
			photoCollectForm.setSubForm("publishPhotoCollect");
		}
	}
}