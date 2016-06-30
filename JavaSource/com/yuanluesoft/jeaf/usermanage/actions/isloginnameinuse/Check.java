package com.yuanluesoft.jeaf.usermanage.actions.isloginnameinuse;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;

/**
 * 
 * @author linchuan
 *
 */
public class Check extends BaseAction {
       
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
    	response.setContentType("text/javascript; charset=UTF-8");
    	Writer writer = response.getWriter();
    	boolean inUse = memberServiceList.isLoginNameInUse(request.getParameter("loginName"), 0);
    	writer.write("loginNameInUse(" + inUse + ");");
    	return null;
    }
}