package com.yuanluesoft.cms.infopublic.actions.printinfoview;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class Print extends ViewFormAction {
	
	public Print() {
		super();
		anonymousAlways = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/infopublic";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		String viewName = request.getParameter("viewName");
		return viewName==null ? "infos" : viewName;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
	    if(view.getActions()==null) {
	        view.setActions(new ArrayList());
	    }
	    String directoryId = request.getParameter("directoryId");
	    if(directoryId==null || directoryId.isEmpty()) { //没有指定目录
			//按站点获取主目录ID
	    	PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
	    	directoryId = publicDirectoryService.getMainDirectoryBySite(RequestUtils.getParameterLongValue(request, "siteId")).getId() + "";
	    }
	    PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
	    String directoryIds = publicDirectoryService.getChildDirectoryIds("" + directoryId, "category,info");
	    view.addJoin(", PublicDirectorySubjection PublicDirectorySubjection");
		String where = "subjections.directoryId=PublicDirectorySubjection.directoryId" +
		  			   " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		
		//处理查询条件
		String value;
		if((value=request.getParameter("subject"))!=null && !value.isEmpty()) { //名称
    		where += "and PublicInfo.subject like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//处理索引号
    	if((value=request.getParameter("infoIndex"))!=null && !value.isEmpty()) {
    		where += "and PublicInfo.infoIndex like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//处理发布机构
    	if((value=request.getParameter("infoFrom"))!=null && !value.isEmpty()) {
    		where += "and PublicInfo.infoFrom like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//处理文号
    	if((value=request.getParameter("mark"))!=null && !value.isEmpty()) {
    		where += "and PublicInfo.mark like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//处理起始生成时间
    	if(RequestUtils.getParameterDateValue(request, "generateDateBegin")!=null) {
    		where += "and PublicInfo.generateDate>=DATE(" + DateTimeUtils.formatDate(RequestUtils.getParameterDateValue(request, "generateDateBegin"), null) + ")";
    	}
    	//处理结束生成时间
    	if(RequestUtils.getParameterDateValue(request, "generateDateEnd")!=null) {
    		where += "and PublicInfo.generateDate<DATE(" + DateTimeUtils.formatDate(RequestUtils.getParameterDateValue(request, "generateDateEnd"), null) + ")";
    	}
    	//处理内容概述
    	if((value=request.getParameter("summarize"))!=null && !value.isEmpty()) {
    		where += "and PublicInfo.summarize like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//处理内容
    	if((value=request.getParameter("lazyBody.body"))!=null && !value.isEmpty()) {
    		where += "and lazyBody.body like '%" + JdbcUtils.resetQuot(value) + "%'";
    	}
    	//快速搜索
    	String searchKey = request.getParameter("searchKey");
		if(searchKey!=null && !searchKey.isEmpty()) {
			String searchMode = request.getParameter("searchMode"); //标题\0正文\0文号\0发布机构\0索引号
			String fieldName = "PublicInfo.subject";
			if("正文".equals(searchMode)) {
				fieldName = "lazyBody.body";
			}
			else if("文号".equals(searchMode)) {
				fieldName = "PublicInfo.mark";
			}
			else if("发布机构".equals(searchMode)) {
				fieldName = "PublicInfo.infoFrom";
			}
			else if("索引号".equals(searchMode)) {
				fieldName = "PublicInfo.infoIndex";
			}
			where += " and " + fieldName + " like '%" + JdbcUtils.resetQuot(searchKey) + "%'";
		}
		int year = RequestUtils.getParameterIntValue(request, "year");
		if(year>0) {
			where += " and " + "year(PublicInfo.generateDate)=" + year;
		}
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		viewForm.getViewPackage().setCurrentViewAction("true".equals(request.getParameter("printAsPdf")) ? "printAsPdf" : "printAsExcel"); //设置为打印
		super.initForm(viewForm, request, sessionInfo);
	}
}