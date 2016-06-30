package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.archives.pojo.ArchivesFonds;
import com.yuanluesoft.archives.services.ArchivesConfigService;
import com.yuanluesoft.j2oa.dispatch.forms.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchFilingConfig;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SetFilingOption extends DispatchAction {
    
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, true, null, null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Dispatch formDispatch = (Dispatch)form;
		if(!formDispatch.isForceFiling()) {
			formDispatch.setUndoneActions(listUndoneActions(formDispatch, record, openMode, request, sessionInfo));
		}
		if(formDispatch.getUndoneActions()!=null && !formDispatch.getUndoneActions().isEmpty()) {
			setUndoneActionsPromptDialog(formDispatch, request, "FormUtils.doAction('setFilingOption', 'forceFiling=true')"); //提醒还有未执行的操作
		}
		else {
			formDispatch.setFormTitle("归档");
			formDispatch.setInnerDialog("filing.jsp");
			formDispatch.getFormActions().addFormAction(-1, "归档", "filing()", true);
			addReloadAction(formDispatch, "取消", request, -1, false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Dispatch formDispatch = (Dispatch)form;
		if(formDispatch.getUndoneActions()!=null && !formDispatch.getUndoneActions().isEmpty()) { //还有未执行的操作
			return;
		}
		//获取归档配置
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		DispatchFilingConfig filingConfig = (DispatchFilingConfig)databaseService.findRecordByHql("from DispatchFilingConfig DispatchFilingConfig");
		if(filingConfig==null) {
			filingConfig = new DispatchFilingConfig();
			filingConfig.setToArchives('1');
			filingConfig.setToDatabank('0');
		}
		formDispatch.setFilingConfig(filingConfig);
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch pojoDispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
		//归档年度
		if(formDispatch.getFilingOption().getFilingYear()==0) {
			formDispatch.getFilingOption().setFilingYear(DateTimeUtils.getYear(pojoDispatch.getSignDate()==null ? Calendar.getInstance().getTime() : pojoDispatch.getSignDate()));
		}
		if(filingConfig.getToArchives()=='1') {
			//获取文书档案服务
			ArchivesConfigService archivesConfigService = (ArchivesConfigService)getService("archivesConfigService");
			//全宗名称
			if(formDispatch.getFilingOption().getFondsName()==null) {
				List fondsList = archivesConfigService.listFonds();
				if(fondsList!=null && fondsList.size()==1) {
					formDispatch.getFilingOption().setFondsName(((ArchivesFonds)fondsList.get(0)).getFondsName());
				}
			}
			//密级
			if(formDispatch.getFilingOption().getSecureLevel()==null) {
				List secureLevels = archivesConfigService.listSecureLevels();
				if(ListUtils.findObjectByProperty(secureLevels, "secureLevel", pojoDispatch.getSecureLevel())!=null) {
					formDispatch.getFilingOption().setSecureLevel(pojoDispatch.getSecureLevel());
				}
			}
			//文件分类
			if(formDispatch.getFilingOption().getDocCategory()==null) {
				formDispatch.getFilingOption().setDocCategory(pojoDispatch.getDocType());
			}
			//机构或问题
			if(formDispatch.getFilingOption().getUnit()==null) {
				List archivesUnits = archivesConfigService.listArchivesUnits();
				if(ListUtils.findObjectByProperty(archivesUnits, "unit", pojoDispatch.getDraftDepartment())!=null) {
					formDispatch.getFilingOption().setUnit(pojoDispatch.getDraftDepartment());
				}
			}
			//责任者
			if(formDispatch.getFilingOption().getResponsibilityPerson()==null) {
				formDispatch.getFilingOption().setResponsibilityPerson(pojoDispatch.getDraftPerson());
			}
		}
	}
}