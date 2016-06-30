package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.point.service.PointService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 登录用户处理器
 * @author linchuan
 *
 */
public class LoginUserProcessor implements PageElementProcessor {
	private PointService pointService; //积分服务
	private MemberServiceList memberServiceList; //成员服务列表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//获取用户名
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			anchorElement.getParentNode().removeChild(anchorElement);
			return;
		}
		//解析操作
		String urn = pageElement.getAttribute("urn");
		if("用户名".equals(urn)) {
			anchorElement.getParentNode().replaceChild(anchorElement.getOwnerDocument().createTextNode(sessionInfo.getUserName()), anchorElement);
		}
		else if("登录用户名".equals(urn)) {
			anchorElement.getParentNode().replaceChild(anchorElement.getOwnerDocument().createTextNode(sessionInfo.getLoginName()), anchorElement);
		}
		else if("积分".equals(urn)) {
			anchorElement.getParentNode().replaceChild(anchorElement.getOwnerDocument().createTextNode("" + pointService.getPoint(sessionInfo.getUserId())), anchorElement);
		}
		else if("头像".equals(urn)) {
			//检查A里面是否有IMG
			String imageSrc = Environment.getContextPath() + "/jeaf/usermanage/portrait.shtml?personId=" + sessionInfo.getUserId();
			NodeList imgs = anchorElement.getElementsByTagName("img");
			HTMLImageElement img;
			if(imgs==null || imgs.getLength()==0) {
				img = (HTMLImageElement)anchorElement.getOwnerDocument().createElement("img");
			}
			else {
				img = (HTMLImageElement)imgs.item(0);
			}
			img.setBorder("0");
			img.setSrc(imageSrc);
			anchorElement.getParentNode().replaceChild(img, anchorElement);
		}
		else if("用户所在部门".equals(urn)) {
			anchorElement.getParentNode().replaceChild(anchorElement.getOwnerDocument().createTextNode(sessionInfo.getDepartmentName()), anchorElement);
		}
		else if("注销".equals(urn)) {
			anchorElement.removeAttribute("urn");
			anchorElement.removeAttribute("id");
			String href = null;
			try {
				href = Environment.getWebApplicationSafeUrl() + "/jeaf/sessionmanage/logout.shtml?external=" + (sitePage.isInternal() ? "false" : "true") + "&redirect=" + URLEncoder.encode(RequestUtils.getRequestURL(request, true), "utf-8");
			}
			catch (UnsupportedEncodingException e) {
				
			}
			anchorElement.setHref(href);
			anchorElement.setTarget("_top");
		}
		else if("修改密码".equals(urn)) {
			anchorElement.removeAttribute("urn");
			anchorElement.removeAttribute("id");
			String onclick = null;
			try {
				onclick = "DialogUtils.openDialog(\"" + Environment.getWebApplicationSafeUrl() + "/jeaf/sso/changePassword.shtml?loginName=" + URLEncoder.encode(sessionInfo.getLoginName(), "utf-8") + (parentSite.getId()==0 ? "" : "&siteId=" + parentSite.getId()) + "\", 430, 260);";
			} 
			catch (UnsupportedEncodingException e) {
				
			}
			anchorElement.setHref("#");
			anchorElement.setAttribute("onclick", onclick);
		}
		else if("修改个人资料".equals(urn)) {
			Member member = memberServiceList.getMember(sessionInfo.getUserId());
			if(member==null || member.getProfileURL()==null || member.getProfileURL().isEmpty()) {
				anchorElement.getParentNode().removeChild(anchorElement);
				return;
			}
			anchorElement.removeAttribute("urn");
			anchorElement.removeAttribute("id");
			LinkUtils.writeLink(anchorElement, member.getProfileURL(), null, webDirectory.getId(), parentSite.getId(), null, false, true, sitePage, request);
		}
		else if("模板配置".equals(urn)) {
			anchorElement.removeAttribute("urn");
			anchorElement.removeAttribute("id");
			String onclick = "DialogUtils.userPageTemplateConfigure('" + sessionInfo.getUserId() + "');";
			anchorElement.setHref("#");
			anchorElement.setAttribute("onclick", onclick);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//非匿名页面,不需要创建静态页面重生成依据
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}

	/**
	 * @return the pointService
	 */
	public PointService getPointService() {
		return pointService;
	}

	/**
	 * @param pointService the pointService to set
	 */
	public void setPointService(PointService pointService) {
		this.pointService = pointService;
	}

	/**
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}
}