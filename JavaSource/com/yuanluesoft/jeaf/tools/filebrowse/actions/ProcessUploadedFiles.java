package com.yuanluesoft.jeaf.tools.filebrowse.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.filebrowse.forms.FileBrowse;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ZipUtils;

/**
 * 
 * @author chuan
 *
 */
public class ProcessUploadedFiles extends FileBrowseAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeLoadAction(mapping, form, request, response);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.filebrowse.actions.FileBrowseAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//运行命令
		FileBrowse fileBrowseForm = (FileBrowse)form;
		if(fileBrowseForm.isUnzip()) {
			String filePath = fileBrowseForm.getPath();
			if(!filePath.endsWith("/") && !filePath.endsWith("\\")) {
				filePath += "/";
			}
			String[] fileNames = fileBrowseForm.getLastUploadFiles().split("\\*");
			for(int i = 0; i<fileNames.length; i++) {
				if(fileNames[i].toLowerCase().endsWith(".zip")) {
					ZipUtils.unZip(filePath + fileNames[i], filePath);
					FileUtils.deleteFile(filePath + fileNames[i]);
				}
			}
		}
		super.initForm(form, acl, sessionInfo, request, response);
	}
}