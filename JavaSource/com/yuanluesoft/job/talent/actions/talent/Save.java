package com.yuanluesoft.job.talent.actions.talent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.talent.forms.Talent;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends TalentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
        if(forward==null || !"result".equals(forward.getName())) {
        	return forward;
        }
    	Talent talentForm = (Talent)form;
    	long siteId = RequestUtils.getParameterLongValue(request, "siteId");
    	if("talentRegistted".equals(talentForm.getSubForm())) { //完成注册
    		if(talentForm.getStatus()==1) { //通过审核
	        	response.sendRedirect(request.getContextPath() + "/job/talent/talentIndex.shtml" + (siteId>0 ? "?siteId=" + siteId : "")); //跳转到个人主页
	        	return null;
    		}
    	}
    	else if(!"talentSubmitted".equals(talentForm.getSubForm())) { //不是完成提交
    		response.sendRedirect(request.getContextPath() + "/job/talent/talent.shtml?id=" + ((Talent)form).getId() + "&act=edit" + (siteId>0 ? "&siteId=" + siteId : ""));
        	return null;
    	}
        return forward;
    }
}