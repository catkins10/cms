package com.yuanluesoft.cms.advice.pages;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.cms.advice.service.AdviceService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AdviceTopicPageService extends PageService {
	private AdviceService adviceService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		AdviceTopic adviceTopic = (AdviceTopic)sitePage.getAttribute("record");
		//设置标题
		String title = template.getTitle();
		template.setTitle(adviceTopic.getSubject() + (title==null || title.equals("") ? "" : " - " + title));
		//如果征集结束,删除提交表单
		if(adviceTopic.getEndDate()!=null && adviceTopic.getEndDate().before(DateTimeUtils.date())) {
			HTMLElement formElement = (HTMLElement)template.getElementById("adviceForm");
			if(formElement!=null) {
				//formElement.getParentNode().removeChild(formElement);
			}
		}
		else if(adviceTopic.getEndDate()!=null) {
			//设置页面有效时间
			PageUtils.setPageExpiresTime(DateTimeUtils.add(adviceTopic.getEndDate(), Calendar.DAY_OF_MONTH, 1), request);
		}
		//查找链接
		NodeList links = template.getElementsByTagName("a");
		for(int i=links.getLength()-1; i>=0; i--) {
			HTMLAnchorElement a = (HTMLAnchorElement)links.item(i);
			if(!"pageLink".equals(a.getId())) {
				continue;
			}
			String urn  = a.getAttribute("urn");
			String linkName = StringUtils.getPropertyValue(urn, "linkTitle");
			if(linkName==null || linkName.isEmpty()) {
				linkName = urn;
			}
			if("结果反馈".equals(linkName)) {
				if(adviceTopic.getFeedbacks()==null || adviceTopic.getFeedbacks().isEmpty()) {
					a.getParentNode().removeChild(a);
				}
			}
			else if("建议列表".equals(linkName)) {
				if(ListUtils.findObjectByProperty(adviceTopic.getAdvices(), "publicPass", new Character('1'))==null) {
					a.getParentNode().removeChild(a);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取征集主题
		AdviceTopic adviceTopic = adviceService.getAdviceTopic(RequestUtils.getParameterLongValue(request, "topicId"));
		if(adviceTopic==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", adviceTopic);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/**
	 * @return the adviceService
	 */
	public AdviceService getAdviceService() {
		return adviceService;
	}

	/**
	 * @param adviceService the adviceService to set
	 */
	public void setAdviceService(AdviceService adviceService) {
		this.adviceService = adviceService;
	}
}