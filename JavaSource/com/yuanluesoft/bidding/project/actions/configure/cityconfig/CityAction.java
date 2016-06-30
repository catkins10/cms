package com.yuanluesoft.bidding.project.actions.configure.cityconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.project.forms.admin.CityConfig;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
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
public class CityAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		CityConfig configForm = (CityConfig)form;
		if(configForm.getAgentChargeStandard()==null || configForm.getAgentChargeStandard().equals("")) {
			configForm.setAgentChargeStandard("招标代理费按照《福建省物价局转发国家计委关于印发招标代理服务收费管理暂行办法的通知》（闽价［2002］服610号）、造价咨询费按照《福建省物价局关于规范建设工程造价咨询服务收费有关问题的通知》（闽价[2002]房457号）规定的标准及允许的浮动率收取，本招标项目允许浮动率为20%。");
		}
		if(configForm.getAgentDrawRemark()==null || configForm.getAgentDrawRemark().equals("")) {
			configForm.setAgentDrawRemark("（1）中选单位与招标人按照建设部《建设工程招标代理合同》(GF-2005-0215)范本商谈签订委托招标代理合同。\r\n（2）招标代理中选单位不接受委托的，或不按招标代理合同约定提供服务的，招标人将有权向有关行政主管部门报告，建议按有关规定予以处理。");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		CityConfig configForm = (CityConfig)form;
		configForm.setManagers(getRecordControlService().getVisitors(configForm.getId(), BiddingProjectCity.class.getName(), BiddingProjectParameterService.BIDDING_CITY_MANAGER)); //管理员
		configForm.setReportVisitors(getRecordControlService().getVisitors(configForm.getId(), BiddingProjectCity.class.getName(), BiddingProjectParameterService.BIDDING_CITY_REPORT_VISITOR)); //报表查询人员
		configForm.setProjectCreators(getRecordControlService().getVisitors(configForm.getId(), BiddingProjectCity.class.getName(), BiddingProjectParameterService.BIDDING_CITY_PROJECT_CREATOR)); //项目登记人员
		configForm.setProjectApprovers(getRecordControlService().getVisitors(configForm.getId(), BiddingProjectCity.class.getName(), BiddingProjectParameterService.BIDDING_CITY_PROJECT_APPROVER)); //项目审核人员
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BiddingProjectCity city = (BiddingProjectCity)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		CityConfig configForm = (CityConfig)form;
		getRecordControlService().updateVisitors(city.getId(), BiddingProjectCity.class.getName(), configForm.getManagers(), BiddingProjectParameterService.BIDDING_CITY_MANAGER); //管理员
		getRecordControlService().updateVisitors(city.getId(), BiddingProjectCity.class.getName(), configForm.getReportVisitors(), BiddingProjectParameterService.BIDDING_CITY_REPORT_VISITOR); //报表查询人员
		getRecordControlService().updateVisitors(city.getId(), BiddingProjectCity.class.getName(), configForm.getProjectCreators(), BiddingProjectParameterService.BIDDING_CITY_PROJECT_CREATOR); //项目登记人员
		getRecordControlService().updateVisitors(city.getId(), BiddingProjectCity.class.getName(), configForm.getProjectApprovers(), BiddingProjectParameterService.BIDDING_CITY_PROJECT_APPROVER); //项目审核人员
		return city;
	}
}