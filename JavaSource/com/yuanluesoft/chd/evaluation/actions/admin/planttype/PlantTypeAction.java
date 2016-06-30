package com.yuanluesoft.chd.evaluation.actions.admin.planttype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.chd.evaluation.actions.admin.DirectoryAction;
import com.yuanluesoft.chd.evaluation.forms.admin.PlantType;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationGenerator;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantType;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class PlantTypeAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.actions.admin.DirectoryAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//如果有发电企业属于该类型时,禁止删除
		ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)record;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		try {
			if(evaluationDirectoryService.isPlantTypeUsed(plantType.getDirectoryName())) {
				throw new PrivilegeException();
			}
		} 
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().addTab(-1, "prerequisites", "必备条件", "plantTypePrerequisites.jsp", false);
			//form.getTabs().addTab(-1, "indicators", "主要指标", "plantTypeIndicators.jsp", false);
			//form.getTabs().addTab(-1, "generators", "机组综合数据", "plantTypeGenerators.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		PlantType plantTypeForm = (PlantType)form;
		ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)record;
		if(plantType.getGenerators()!=null && !plantType.getGenerators().isEmpty()) {
			plantTypeForm.setGeneratorTemplate(((ChdEvaluationGenerator)plantType.getGenerators().iterator().next()).getBody());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		PlantType plantTypeForm = (PlantType)form;
		ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存机组数据模板
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		evaluationDirectoryService.saveGeneratorTemplate(plantTypeForm.getGeneratorTemplate(), plantType);
		return plantType;
	}
}