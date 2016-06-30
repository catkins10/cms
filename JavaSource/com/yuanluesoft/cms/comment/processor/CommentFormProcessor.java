package com.yuanluesoft.cms.comment.processor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLFormElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class CommentFormProcessor extends FormProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFieldValue(org.w3c.dom.html.HTMLFormElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		Object record;
		if(!"cms/comment".equals(sitePage.getApplicationName()) && !"comment".equals(sitePage.getName()) && (record=sitePage.getAttribute("record"))!=null) { //不是独立的评论页面
			if("id".equals(field.getName())) {
				Long id = (Long)sitePage.getAttribute("commentId");
				if(id==null) {
					id = new Long(UUIDLongGenerator.generateId());
					sitePage.setAttribute("commentId", id);
				}
				return "" + id;
			}
			else if("act".equals(field.getName())) {
				return "create";
			}
			else if("applicationName".equals(field.getName())) { //所在应用
				return sitePage.getApplicationName();
			}
			else if("pageName".equals(field.getName())) { //页面名称
				return sitePage.getName();
			}
			else if("recordId".equals(field.getName())) { //记录ID
				return "" + PropertyUtils.getProperty(record, "id");
			}
			else if("pageTitle".equals(field.getName())) { //页面标题
				String pageTitle = ((HTMLDocument)formElement.getOwnerDocument()).getTitle();
				return pageTitle!=null && !pageTitle.isEmpty() ? pageTitle : StringUtils.getBeanTitle(record);
			}
			else if("pageUrl".equals(field.getName())) { //页面URL
				return StringUtils.removeQueryParameters(RequestUtils.getRequestURL(request, true), "writePageElementAsJs,elementId");
			}
		}
		return super.getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
	}
}