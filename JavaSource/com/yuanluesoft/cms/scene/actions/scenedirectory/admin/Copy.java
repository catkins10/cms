package com.yuanluesoft.cms.scene.actions.scenedirectory.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 目录拷贝
 * @author linchuan
 *
 */
public class Copy extends SceneDirectoryAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.actions.scenedirectory.admin.SceneDirectoryAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SceneDirectory sceneDirectory = (SceneDirectory)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		SceneService sceneService = (SceneService)getService("sceneService");
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		sceneDirectory = sceneService.copySceneDirectory(sceneDirectoryForm.getId(), sceneDirectoryForm.getCopyToDirectoryId());
		return sceneDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.actions.scenedirectory.admin.SceneDirectoryAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		return "window.onerror=function(){return false;};top.frames(\"treeFrame\").setTimeout(\"window.tree.reloadChildNodes('" + sceneDirectoryForm.getCopyToDirectoryId() + "')\", 10);";
	}
}