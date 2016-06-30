package com.yuanluesoft.jeaf.dialog.actions.viewselectdialog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.dialog.forms.ViewSelectDialog;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JsonUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 加载选择视图数据
 * @author linchuan
 *
 */
public class LoadSelectViewData extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo;
		anonymousEnable = true;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		ViewSelectDialog dataForm = (ViewSelectDialog)form;
		View view = null;
		try {
			//获取选择对话框定义
			view = ((ViewDefineService)getService("viewDefineService")).getView(dataForm.getApplicationName(), dataForm.getViewName(), sessionInfo);
			//调用视图包服务填充视图包
			dataForm.getViewPackage().setViewMode(dataForm.isMultiSelect() ? View.VIEW_DISPLAY_MODE_MULTI_SELECT : View.VIEW_DISPLAY_MODE_SELECT);
			if(!dataForm.isPaging()) { //不需要分页
				view.setPageRows(1000); //设置记录数为1000
			}
			view.setLinks(null); //清理链接列表
			String key = dataForm.getKey();
			if(key!=null && !key.equals("")) { //搜索条件不为空
				dataForm.getViewPackage().setSearchMode(true);
				dataForm.getViewPackage().setSearchConditions(generateSearchConditons(key));
			}
			ViewService viewService = ((ViewService)getService(view.getViewServiceName()==null ? "viewService" : view.getViewServiceName()));
			viewService.retrieveViewPackage(dataForm.getViewPackage(), view, 0, false, false, false, request, sessionInfo);
		}
		catch(Exception e) {
			Logger.exception(e);
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
 		return mapping.findForward("load");
    }
    
    /**
	 * 生成搜索条件
	 * @param key
	 * @return
	 */
	protected String generateSearchConditons(String key) {
		Condition condition = new Condition(null, null, null, Condition.CONDITION_EXPRESSION_KEY, key, null);
    	List conditions = new ArrayList();
    	conditions.add(condition);
    	return JsonUtils.generateJSONObject(conditions).toJSONString();
	}
}