/*
 * Created on 2006-9-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.actions;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.archives.administrative.pojo.AdministrativeArchives;
import com.yuanluesoft.archives.pojo.ArchivesFonds;
import com.yuanluesoft.archives.services.ArchivesCodeService;
import com.yuanluesoft.archives.services.ArchivesConfigService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 *
 * @author linchuan
 *
 */
public class AdministrativeArchivesAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.archives.administrative.forms.AdministrativeArchives archivesForm = (com.yuanluesoft.archives.administrative.forms.AdministrativeArchives)form;
		//设置归档日期
		if(archivesForm.getFilingDate()==null) {
			archivesForm.setFilingDate(DateTimeUtils.date());
		}
		//设置归档年度
		if(archivesForm.getFilingYear()==0) {
			archivesForm.setFilingYear(Calendar.getInstance().get(Calendar.YEAR));
		}
		//设置全宗号
		ArchivesConfigService archivesConfigService = (ArchivesConfigService)getService("archivesConfigService");
		List fonds = archivesConfigService.listFonds();
		if(fonds!=null && fonds.size()==1) {
			ArchivesFonds fondsConfig = (ArchivesFonds)fonds.get(0);
			archivesForm.setFondsCode(fondsConfig.getFondsCode());
			archivesForm.setFondsName(fondsConfig.getFondsName());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		com.yuanluesoft.archives.administrative.forms.AdministrativeArchives archivesForm = (com.yuanluesoft.archives.administrative.forms.AdministrativeArchives)form;
		//设置访问者
		RecordVisitorList visitors = getRecordControlService().getVisitors(archivesForm.getId(), AdministrativeArchives.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY);
		if(visitors!=null) {
			archivesForm.setReaders(visitors);
		}

	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.platform.form.actions.FormAction#saveRecord(com.yuanluesoft.platform.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.platform.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		//设置或更新档号
		AdministrativeArchives pojoArchives = (AdministrativeArchives)record;
		int[] serialNumber = new int[1];
		serialNumber[0] = pojoArchives.getSerialNumber();
		pojoArchives.setArchivesCode(((ArchivesCodeService)getService("archivesCodeService")).generateArchivesCode("文书档案", pojoArchives.getFondsCode(), pojoArchives.getFilingYear(), pojoArchives.getRotentionPeriodCode(), pojoArchives.getUnitCode(), serialNumber));
		pojoArchives.setSerialNumber(serialNumber[0]);
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存密级对应的访问者
		RecordControlService recordControlService = getRecordControlService();
		com.yuanluesoft.archives.administrative.forms.AdministrativeArchives archivesForm = (com.yuanluesoft.archives.administrative.forms.AdministrativeArchives)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			if(archivesForm.getReaders().getVisitorIds()==null || archivesForm.getReaders().getVisitorIds().equals("")) {
				//未指定访问者,则根据密级自动设置访问者列表
				List visitors = ((ArchivesConfigService)getService("archivesConfigService")).listSecureLevelVisitors(pojoArchives.getSecureLevel());
				if(visitors!=null) {
					for(Iterator iterator = visitors.iterator(); iterator.hasNext();) {
						RecordPrivilege recordPrivilege = (RecordPrivilege)iterator.next();
						recordControlService.appendVisitor(pojoArchives.getId(), AdministrativeArchives.class.getName(), recordPrivilege.getVisitorId(), RecordControlService.ACCESS_LEVEL_READONLY);
					}
				}
			}
			else {
				recordControlService.updateVisitors(pojoArchives.getId(), AdministrativeArchives.class.getName(), archivesForm.getReaders(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
			//将文书档案管理员设为编辑者
			List managers = getAccessControlService().listVisitors(archivesForm.getFormDefine().getApplicationName(), "manageUnit_administrativeArchivesManage");
			for(Iterator iterator = managers.iterator(); iterator.hasNext();) {
				Element manager = (Element)iterator.next();
				recordControlService.appendVisitor(pojoArchives.getId(), AdministrativeArchives.class.getName(), Long.parseLong(manager.getId()), RecordControlService.ACCESS_LEVEL_EDITABLE);
			}
		}
		else if(archivesForm.getReaders().getVisitorIds()!=null) { //提交的内容中必须包含分发范围
			recordControlService.updateVisitors(pojoArchives.getId(), AdministrativeArchives.class.getName(), archivesForm.getReaders(), RecordControlService.ACCESS_LEVEL_READONLY);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl!=null && acl.contains("manageUnit_administrativeArchivesManage")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		AdministrativeArchives pojoAdministrativeArchives = (AdministrativeArchives)record;
		try {
			RecordControlService recordControlService = (RecordControlService)getService("recordControlService");
			return recordControlService.getAccessLevel(pojoAdministrativeArchives.getId(), record.getClass().getName(), sessionInfo);
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
	}
}