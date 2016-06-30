package com.yuanluesoft.cms.interview.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLIFrameElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewSpeakFormProcessor extends FormProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage,	requestInfo, request);
		//添加隐藏的IFrame,用来做发言提交
		HTMLIFrameElement iFrameElement = (HTMLIFrameElement)pageElement.getOwnerDocument().createElement("iframe");
		iFrameElement.setName("speakFrame");
		iFrameElement.setAttribute("style", "display:none");
		pageElement.appendChild(iFrameElement);
	
		//修改表单的target
		HTMLFormElement form = (HTMLFormElement)pageElement;
		form.setAttribute("target", "speakFrame");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#writeFormField(org.w3c.dom.html.HTMLFormElement, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.util.model.RequestInfo)
	 */
	protected void writeFormField(HTMLFormElement formElement, HTMLElement fieldElement, SitePage sitePage, Form form, Field formField, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, HTMLParser htmlParser, RequestInfo requestInfo) throws ServiceException {
		if("htmleditor".equals(formField.getInputMode())) {
			formField.setParameter("attachmentSelector", "/" + sitePage.getApplicationName() + "/selectAttachment.shtml?subjectId=" + request.getParameter("id") + "&id=" + getId(sitePage));
		}
		if("speaker".equals(formField.getName())) { //发言人
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			if(sessionInfo!=null && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
				fieldElement.getParentNode().replaceChild(fieldElement.getOwnerDocument().createTextNode(sessionInfo.getUserName()), fieldElement);
				return;
			}
		}
		super.writeFormField(formElement, fieldElement, sitePage, form, formField, webDirectory, parentSite, request, htmlParser, requestInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFieldValue(org.w3c.dom.html.HTMLFormElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		if("id".equals(field.getName())) {
			return new Long(getId(sitePage));
		}
		Object value = super.getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
		if((value==null || value.equals("")) && "speaker".equals(field.getName())) {
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
				return "网友";
			}
		}
		return value;
	}

	/**
	 * 获取ID
	 * @param sitePage
	 * @return
	 */
	private long getId(SitePage sitePage) {
		Long id = (Long)sitePage.getAttribute("id");
		if(id==null) {
			id = new Long(UUIDLongGenerator.generateId());
			sitePage.setAttribute("id", id);
		}
		return id.longValue();
	}
}