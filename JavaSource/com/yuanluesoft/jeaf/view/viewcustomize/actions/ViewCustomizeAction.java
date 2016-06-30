/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.jeaf.view.viewcustomize.forms.ViewCustomize;
import com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustomizeAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(com.yuanluesoft.jeaf.form.ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ViewCustomize viewCustomizeForm = (ViewCustomize)form;
		ViewCustomizeService viewCustomizeService = (ViewCustomizeService)getService("viewCustomizeService");
		ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
		//获取视图
		View view = viewDefineService.getView(viewCustomizeForm.getApplicationName(), viewCustomizeForm.getViewName(), sessionInfo);
		List columns = view.getColumns();
		view.setColumns(new ArrayList(columns));
		ViewUtils.getViewService(view).resetViewColumns(view, View.VIEW_DISPLAY_MODE_CUSTOMIZE, request, sessionInfo);
		//设置源列表
		viewCustomizeForm.setSourceColumns(view.getColumns());
		view.setColumns(columns);
		List fields = FieldUtils.listRecordFields(view.getPojoClassName(), null, "html,attachment,image,video,components", null, "hidden,none", false, false, false, false, 1);
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(ListUtils.findObjectByProperty(viewCustomizeForm.getSourceColumns(), "name", field.getName())==null) {
				viewCustomizeForm.getSourceColumns().add(new Column(field.getName(), field.getTitle(), Column.COLUMN_TYPE_FIELD));
			}
		}
		for(Iterator iterator = viewCustomizeForm.getSourceColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			Field field = (Field)ListUtils.findObjectByProperty(fields, "name", column.getName());
			if(field!=null && field.isPersistence()) { //是数据库字段
				column.setDisplay("sort"); //设置为允许排序
			}
		}
		//获取视图定制
		viewCustomizeForm.setViewCustom(viewCustomizeService.loadViewCustom(view, sessionInfo.getUserId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ViewCustomize viewCustomizeForm = (ViewCustomize)form;
		ViewCustomizeService viewCustomizeService = (ViewCustomizeService)getService("viewCustomizeService");
		viewCustomizeService.saveViewCustom(viewCustomizeForm.getApplicationName(), viewCustomizeForm.getViewName(), sessionInfo.getUserId(), viewCustomizeForm.getViewCustom().getPageRows(), request.getParameterValues("columnName"), request.getParameterValues("columnTitle"), request.getParameterValues("columnWidth"), request.getParameterValues("columnAlign"), request.getParameterValues("sortColumnName"), request.getParameterValues("sortColumnTitle"), request.getParameterValues("sortColumnDirection"));
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ViewCustomize viewCustomizeForm = (ViewCustomize)form;
		ViewCustomizeService viewCustomizeService = (ViewCustomizeService)getService("viewCustomizeService");
		viewCustomizeService.deleteViewCustom(viewCustomizeForm.getApplicationName(), viewCustomizeForm.getViewName(), sessionInfo.getUserId());
	}
}