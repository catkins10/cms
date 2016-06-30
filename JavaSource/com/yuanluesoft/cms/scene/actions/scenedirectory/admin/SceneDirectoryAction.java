package com.yuanluesoft.cms.scene.actions.scenedirectory.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class SceneDirectoryAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		SceneDirectory sceneDirectory = (SceneDirectory)record;
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		//获取场景服务
		SceneService sceneService = (SceneService)getService("sceneService");
		com.yuanluesoft.cms.scene.pojo.SceneService service;
		try {
			service = sceneService.getSceneServiceByDirectoryId(sceneDirectory==null ? sceneDirectoryForm.getParentDirectoryId() : sceneDirectory.getParentDirectoryId());
			sceneDirectoryForm.setSceneServiceId(service.getId());
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
		//检查用户对绑定站点的管理权限
		SiteService siteService = (SiteService)getService("siteService");
		if(siteService.checkPopedom(service.getSiteId(), "manager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(!OPEN_MODE_CREATE.equals(openMode) && acl.contains("application_visitor")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		//获取上级目录名称
		sceneDirectoryForm.setParentDirectoryName(getParentDirectoryName(sceneDirectoryForm.getParentDirectoryId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		SceneDirectory sceneDirectory = (SceneDirectory)record;
		//获取上级目录名称
		sceneDirectoryForm.setParentDirectoryName(getParentDirectoryName(sceneDirectory.getParentDirectoryId()));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.setDisplayMode(null);
	}

	/**
	 * 获取上级目录名称
	 * @param parentDirectoryId
	 * @return
	 * @throws Exception
	 */
	private String getParentDirectoryName(long parentDirectoryId) throws Exception {
		SceneService sceneService = (SceneService)getService("sceneService");
		com.yuanluesoft.cms.scene.pojo.SceneService service = sceneService.getSceneService(parentDirectoryId);
		if(service!=null) {
			return service.getName();
		}
		SceneDirectory sceneDirectory = sceneService.getSceneDirectory(parentDirectoryId);
		return sceneDirectory.getDirectoryName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory sceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)form;
		if("delete".equals(currentAction)) {
			return "window.onerror=function(){return false;};top.frames[\"treeFrame\"].setTimeout(\"window.tree.deleteNode('" + sceneDirectoryForm .getId() + "')\", 10);";
		}
		else if("save".equals(currentAction)) {
			if(OPEN_MODE_CREATE.equals(openMode)) { //新目录
				return "window.onerror=function(){return false;};top.frames[\"treeFrame\"].setTimeout(\"window.tree.reloadChildNodes('" + sceneDirectoryForm.getParentDirectoryId() + "')\", 10);";
			}
			else { //更新目录
				return "window.onerror=function(){return false;};top.frames[\"treeFrame\"].setTimeout(\"window.tree.renameNode('" + sceneDirectoryForm.getId() + "', '" + sceneDirectoryForm.getDirectoryName() + "')\", 10);";
			}
		}
		return super.generateRefreshOpenerScript(form, record, openMode, currentAction, actionResult, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory newSceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)newForm;
		com.yuanluesoft.cms.scene.forms.admin.SceneDirectory currentSceneDirectoryForm = (com.yuanluesoft.cms.scene.forms.admin.SceneDirectory)currentForm;
		newSceneDirectoryForm.setParentDirectoryId(currentSceneDirectoryForm.getParentDirectoryId());
	}
}