package com.yuanluesoft.j2oa.receival.actions.receival;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.archives.pojo.ArchivesFonds;
import com.yuanluesoft.archives.services.ArchivesConfigService;
import com.yuanluesoft.j2oa.receival.forms.Receival;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig;
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
public class SetFilingOption extends ReceivalAction {
    
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.actions.receival.ReceivalAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Receival formReceival = (Receival)form;
		formReceival.setFormTitle("归档");
		formReceival.setInnerDialog("filing.jsp");
		formReceival.getFormActions().addFormAction(-1, "归档", "filing()", true);
		addReloadAction(formReceival, "取消", request, -1, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.actions.receival.ReceivalAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Receival formReceival = (Receival)form;
		if(formReceival.getUndoneActions()!=null) { //还有未执行的操作
			return;
		}
		com.yuanluesoft.j2oa.receival.pojo.Receival pojoReceival = (com.yuanluesoft.j2oa.receival.pojo.Receival)record;
		//获取归档配置
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		ReceivalFilingConfig filingConfig = (ReceivalFilingConfig)databaseService.findRecordByHql("from ReceivalFilingConfig ReceivalFilingConfig");
		if(filingConfig==null) {
			filingConfig = new ReceivalFilingConfig();
			filingConfig.setToArchives('1');
			filingConfig.setToDatabank('0');
		}
		formReceival.setFilingConfig(filingConfig);
		//归档年度
		if(formReceival.getFilingOption().getFilingYear()==0) {
			formReceival.getFilingOption().setFilingYear(DateTimeUtils.getYear(pojoReceival.getSignDate()==null ? Calendar.getInstance().getTime() : pojoReceival.getSignDate()));
		}
		if(filingConfig.getToArchives()=='1') {
			//获取文书档案服务
			ArchivesConfigService archivesConfigService = (ArchivesConfigService)getService("archivesConfigService");
			//全宗名称
			if(formReceival.getFilingOption().getFondsName()==null) {
				List fondsList = archivesConfigService.listFonds();
				if(fondsList!=null && fondsList.size()==1) {
					formReceival.getFilingOption().setFondsName(((ArchivesFonds)fondsList.get(0)).getFondsName());
				}
			}
			//密级
			if(formReceival.getFilingOption().getSecureLevel()==null) {
				List secureLevels = archivesConfigService.listSecureLevels();
				if(ListUtils.findObjectByProperty(secureLevels, "secureLevel", pojoReceival.getSecureLevel())!=null) {
					formReceival.getFilingOption().setSecureLevel(pojoReceival.getSecureLevel());
				}
			}
			//文件分类
			if(formReceival.getFilingOption().getDocCategory()==null) {
				formReceival.getFilingOption().setDocCategory(pojoReceival.getDocType());
			}
			//机构或问题
			if(formReceival.getFilingOption().getUnit()==null) {
				List archivesUnits = archivesConfigService.listArchivesUnits();
				if(ListUtils.findObjectByProperty(archivesUnits, "unit", pojoReceival.getRegistDepartment())!=null) {
					formReceival.getFilingOption().setUnit(pojoReceival.getRegistDepartment());
				}
			}
			//责任者
			if(formReceival.getFilingOption().getResponsibilityPerson()==null) {
				formReceival.getFilingOption().setResponsibilityPerson(pojoReceival.getRegistPerson());
			}
		}
	}
}