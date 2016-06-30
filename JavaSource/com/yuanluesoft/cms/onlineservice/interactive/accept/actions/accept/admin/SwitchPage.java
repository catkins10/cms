package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin.Accept;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 切换页面
 * @author yuanlue
 *
 */
public class SwitchPage extends AcceptAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public Record save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult) throws Exception {
		Accept acceptForm = (Accept)form;
		Record record = super.save(mapping, form, request, response, reload, tabSelected, actionResult);
		setTransactPage(acceptForm, request);
		return record;
	}
	
	/**
	 * 设置办理页面
	 * @param acceptForm
	 * @param request
	 */
	private void setTransactPage(Accept acceptForm, HttpServletRequest request) {
		acceptForm.setInnerDialog(acceptForm.getPageName() + ".jsp");
		if("answer".equals(acceptForm.getPageName())) {
			acceptForm.setFormTitle("受理申报");
			acceptForm.getFormActions().addFormAction(-1, "确定", "answer()", true);
		}
		else if("caseDeclined".equals(acceptForm.getPageName())) {
			acceptForm.setFormTitle("受理申报");
			acceptForm.getFormActions().addFormAction(-1, "确定", "caseDeclined()", true);
		}
		else if("missing".equals(acceptForm.getPageName())) {
			acceptForm.setFormTitle("发送缺件通知");
			acceptForm.getFormActions().addFormAction(-1, "发送", "sendMissingNotify()", true);
		}
		addReloadAction(acceptForm, "取消", request, -1, false);
	}
}