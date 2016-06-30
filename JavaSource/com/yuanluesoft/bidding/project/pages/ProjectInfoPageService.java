package com.yuanluesoft.bidding.project.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.service.BiddingProjectTemplateService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 项目登记页面服务
 * @author linchuan
 *
 */
public class ProjectInfoPageService extends PageService {
	private BiddingProjectService biddingProjectService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取项目
		BiddingProject project = biddingProjectService.getProject(RequestUtils.getParameterLongValue(request, "id"));
		if(project==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", project);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		BiddingProjectTemplateService biddingProjectTemplateService = (BiddingProjectTemplateService)getTemplateService();
		//获取主记录
		BiddingProject project = (BiddingProject)sitePage.getAttribute("record");
		return biddingProjectTemplateService.getTemplateHTMLDocument(pageName, project, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		BiddingProject project = (BiddingProject)sitePage.getAttribute("record");
		//设置标题
		String title = template.getTitle();
		template.setTitle((title==null || title.equals("") ? "" : title + " - ") +  project.getProjectName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetPageLink(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetPageLink(link, linkName, template, applicationName, pageName, sitePage, siteId, request);
		BiddingProject project = (BiddingProject)sitePage.getAttribute("record");
		String[] linkMapping = {"前期资料备案", "prophase", "代理抽签公示", "agentDraw", "缴纳场地费", "useFee",
				"开标室安排", "bidopeningRoomSchedule", "预审公示", "preapproval", "招标文件", "tender",
				"招标公告", "tender",	"时间安排", "plan", "答疑纪要", "answer",
				"补充通知", "supplement", "开标公示", "bidopening", "中标公示", "pitchon", 
				"中标通知书", "notice", "缴费", "pay", "书面报告备案", "archive", 
				"报建", "declare", "施工许可证", "licence"};
		for(int j=0; j<linkMapping.length; j+=2) {
			if(linkMapping[j].equals(linkName)) {
				if(!project.isComponentPublic(linkMapping[j + 1])) {
					link.getParentNode().removeChild(link);
				}
				break;
			}
		}
	}

	/**
	 * @return the biddingProjectService
	 */
	public BiddingProjectService getBiddingProjectService() {
		return biddingProjectService;
	}

	/**
	 * @param biddingProjectService the biddingProjectService to set
	 */
	public void setBiddingProjectService(BiddingProjectService biddingProjectService) {
		this.biddingProjectService = biddingProjectService;
	}
}
