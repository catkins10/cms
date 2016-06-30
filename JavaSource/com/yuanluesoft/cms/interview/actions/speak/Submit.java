package com.yuanluesoft.cms.interview.actions.speak;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.interview.forms.Speak;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends SpeakAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		writeHtml(response, "<script>top.location.reload()</script>");
    		return null;
    	}
    	backgroundAction = true; //利用ifame提交,设置为后台操作
    	//保存
    	executeSaveAction(mapping, form, request, response, false, null, null, null);
    	Speak speakForm = (Speak)form;
    	if(speakForm.getErrors()!=null && !speakForm.getErrors().isEmpty()) {
    		writeHtml(response, "<script>alert('" + ListUtils.join(speakForm.getErrors(), ",", false) + "')</script>");
        	return null;
		}
    	writeHtml(response, "<script>parent.setTimeout(\"resetSpeakForm('" + speakForm.getId() + "', '" + UUIDLongGenerator.generateId() + "')\", 1, \"javascript\")</script>");
    	return null;
    }
    
    /**
     * 输出HTML
     * @param response
     * @param body
     * @throws Exception
     */
    private void writeHtml(HttpServletResponse response, String body) throws Exception {
    	response.setCharacterEncoding("utf-8");
    	response.getWriter().print("<html><body>" + body + "</body></html>");
    }
}