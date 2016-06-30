package com.yuanluesoft.cms.infopublic.actions.admin.publicinfoview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.service.PublicInfoExportService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * 导出XML,福州市政府TRS的格式
 * @author linchuan
 *
 */
public class ExportXML extends PublicInfoViewAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
			sessionInfo = getSessionInfo(request, response); //会话检查
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, true);
    	}
    	ViewForm viewForm = (ViewForm)form;
    	//初始化表单
		initForm(viewForm, request, sessionInfo);
		//获取视图
    	ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
    	View view = viewDefineService.getView(getViewApplicationName(viewForm, request), getViewName(viewForm, request), sessionInfo);
		viewForm.getViewPackage().setView(view);
    	//重置视图
    	resetView(viewForm, view, sessionInfo, request);
    	//输出报表
    	PublicInfoExportService publicInfoExportService = (PublicInfoExportService)getService("publicInfoTrsExportService");
    	publicInfoExportService.exportXML(view, viewForm.getViewPackage().getCategories(), viewForm.getViewPackage().getSearchConditions(), response, request, sessionInfo);
    	return null;
    }
}