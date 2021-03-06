package com.yuanluesoft.bidding.project.actions.configure.fileconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.FileConfig;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AddArchiveFile extends FileConfigAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "archiveFiles", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//添加归档资料
		FileConfig category = (FileConfig)form;
		if(category.getArchiveFileName()!=null && !category.getArchiveFileName().equals("")) {
			BiddingProjectParameterService parameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			parameterService.addProjectFileItem(category.getId(), category.getArchiveFileSn(), "归档资料", category.getArchiveFileName(), category.getArchiveFileCategory(), null);
			category.setArchiveFileSn(category.getArchiveFileSn() + 1);
			category.setArchiveFileName(null);
		}
		return record;
	}
}