package com.yuanluesoft.j2oa.document.actions.keyword;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.document.service.KeywordService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.Encoder;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class ParseKeyword extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("text/javascript; charset=UTF-8");
    	PrintWriter writer = response.getWriter();
    	String text = ((KeywordService)getService("keywordService")).parseKeyword(request.getParameter("text"));
   		writer.write("var keywords = '" + (text==null ? "" : Encoder.getInstance().utf8JsEncode(text)) + "';");
    	writer.write("var fields = document.getElementsByName('" + request.getParameter("fieldName") + "');");
    	writer.write("for(var i=0; i<fields.length; i++) { " +
    				 "	if(fields[i].tagName=='INPUT' || fields[i].tagName=='TEXTAREA') {" +
    				 "	 	fields[i].value = keywords;" +
    				 "		break;" +
    				 "	}" +
    				 "}");
    	return null;
    }
}