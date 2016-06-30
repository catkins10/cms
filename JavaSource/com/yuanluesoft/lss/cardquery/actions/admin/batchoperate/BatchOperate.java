/**
 * 
 */
package com.yuanluesoft.lss.cardquery.actions.admin.batchoperate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.lss.cardquery.pojo.CardQuery;

/**
 * 批量操作，根据传递来的operateMode参数执行相应操作，如删除
 * @author kangshiwei
 */
public class BatchOperate extends ApplicationViewAction {
	
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()!=null && !viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			String mode=RequestUtils.getParameterStringValue(request, "operateMode");
			if(mode==null){//没有操作模式，无效请求
				return mapping.getInputForward();
			}
			else if(mode.equals("delete")){
				batchDelete(mapping, viewForm, request, response, sessionInfo);
			}			
			viewForm.getViewPackage().setSelectedIds(null); //清空列表
		}
		return mapping.getInputForward();
	}
	/**
	 * 批量删除
	 * @param mapping
	 * @param viewForm
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws SystemUnregistException 
	 */
	private void batchDelete(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws SystemUnregistException{
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		DatabaseService databaseService=(DatabaseService) getService("databaseService");
		for(int i=0; i<ids.length; i++) {
			databaseService.deleteRecordById(CardQuery.class.getName(), Long.parseLong(ids[i]));
		}
	}
}