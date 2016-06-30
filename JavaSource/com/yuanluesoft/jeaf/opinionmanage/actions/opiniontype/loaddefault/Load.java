package com.yuanluesoft.jeaf.opinionmanage.actions.opiniontype.loaddefault;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.opinionmanage.forms.LoadDefault;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoadDefault loadDefaultForm = (LoadDefault)form;
    	loadDefaultForm.getFormActions().addFormAction(-1, "确定", "FormUtils.submitForm();", true);
    	loadDefaultForm.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog();", false);
    	loadDefaultForm.setFormTitle("加载预设意见类型");
    	return mapping.findForward("load");
    }
}