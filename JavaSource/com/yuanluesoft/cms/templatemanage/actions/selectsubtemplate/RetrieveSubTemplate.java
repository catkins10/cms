package com.yuanluesoft.cms.templatemanage.actions.selectsubtemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class RetrieveSubTemplate extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.cms.templatemanage.forms.RetrieveSubTemplate retrieveSubTemplate = (com.yuanluesoft.cms.templatemanage.forms.RetrieveSubTemplate)form;
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		response.getWriter().write("<html><body onload=\"alert('会话异常,请重新选择子模板');\"></body></html>");
    		return null;
        }
    	//获取子模板
    	TemplateService templateService = (TemplateService)getService("templateService");
    	HTMLDocument htmlDcoument = templateService.getTemplateHTMLDocument(retrieveSubTemplate.getTemplateId(), -1, true, request);
    	HTMLParser htmlParser = (HTMLParser)getService("htmlParser");
    
    	//处理头部,给link、style和script添加模板ID
    	NodeList nodes = htmlDcoument.getElementsByTagName("head");
    	HTMLHeadElement head = (nodes==null || nodes.getLength()==0 ? null : (HTMLHeadElement)nodes.item(0));
		NodeList childNodes = head==null ? null : head.getChildNodes();
		for(int i=(childNodes==null ? 0 : childNodes.getLength()-1); i>=0; i--) {
			if(!(childNodes.item(i) instanceof HTMLElement)) {
				continue;
			}
			HTMLElement htmlElement = (HTMLElement)childNodes.item(i);
			String tagName = htmlElement.getTagName().toLowerCase();
			htmlElement.setId("subTemplateHeadElement_" + retrieveSubTemplate.getTemplateId());
			if(!"link".equals(tagName) && !"style".equals(tagName) && !"script".equals(tagName)) {
				htmlElement.getParentNode().removeChild(htmlElement);
			}
			else if("script".equals(tagName)) {
				String content = htmlElement.getTextContent();
				if(content!=null && content.indexOf("window.onerror=function(){return true;}")!=-1) {
					htmlElement.getParentNode().removeChild(htmlElement);
				}
			}
		}
		response.setCharacterEncoding("utf-8");
		htmlParser.writeHTMLDocument(htmlDcoument, response.getWriter(), "utf-8");
    	return null;
    }
}