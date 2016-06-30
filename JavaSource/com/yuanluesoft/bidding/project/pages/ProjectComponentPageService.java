package com.yuanluesoft.bidding.project.pages;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoomSchedule;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgentDraw;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectMaterial;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.service.BiddingProjectTemplateService;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 项目组成部份页面服务
 * @author linchuan
 *
 */
public class ProjectComponentPageService extends com.yuanluesoft.cms.pagebuilder.PageService {
	private DatabaseService databaseService;
	private BiddingProjectService biddingProjectService; //项目管理服务
	private BiddingService biddingService; //项目报名服务
	private AttachmentService attachmentService; //附件服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//按页面名称获取pojo名称
		String pojoName;
		if("开标室安排".equals(sitePage.getTitle())) {
			pojoName = "BiddingRoomSchedule";
		}
		else if("中标通知书打印".equals(sitePage.getTitle())) {
			pojoName = "BiddingProjectNotice";
		}
		else {
			pojoName = sitePage.getUrl();
			pojoName = pojoName.substring(pojoName.lastIndexOf('/') + 1, pojoName.lastIndexOf(".shtml")); 
			pojoName = "BiddingProject" + pojoName.substring(0, 1).toUpperCase() + pojoName.substring(1);
		}
		//获取项目组成部份
		long id = RequestUtils.getParameterLongValue(request, "id");
		BiddingProjectComponent component;
		String hql = "from " + pojoName + " " + pojoName;
		if(id>0) { //按ID获取中标结果
			hql += " where " + pojoName + ".id=" + id;
		}
		else { //按项目ID获取中标结果
			hql += " where " + pojoName + ".projectId=" + RequestUtils.getParameterLongValue(request, "projectId");
		}
		if(!"中标通知书打印".equals(sitePage.getTitle())) {
			hql += " and not " + pojoName + ".publicBeginTime is null";
		}
		hql += " order by " + pojoName + ".publicBeginTime DESC";
		component = (BiddingProjectComponent)getDatabaseService().findRecordByHql(hql);
		if(component==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		component.setProject(biddingProjectService.getProject(component.getProjectId()));
		sitePage.setAttribute("record", component);
		if(pojoName.equals("BiddingProjectAgentDraw")) { //代理抽签公告
			//获取中选代理
			((BiddingProjectAgentDraw)component).setAgent((BiddingProjectAgent)getDatabaseService().findRecordByHql("from BiddingProjectAgent BiddingProjectAgent where BiddingProjectAgent.projectId=" + component.getProjectId()));
		}
		else if(pojoName.equals("BiddingProjectAgent")) { //代理抽签结果
			//获取代理抽签公告
			((BiddingProjectAgent)component).setAgentDraw((BiddingProjectAgentDraw)getDatabaseService().findRecordByHql("from BiddingProjectAgentDraw BiddingProjectAgentDraw where BiddingProjectAgentDraw.projectId=" + component.getProjectId()));
		}
		else if(pojoName.equals("BiddingProjectMaterial")) { //实质性内容
			//获取招标公告
			((BiddingProjectMaterial)component).setTender((BiddingProjectTender)getDatabaseService().findRecordByHql("from BiddingProjectTender BiddingProjectTender where BiddingProjectTender.projectId=" + component.getProjectId()));
		}
		else if(pojoName.equals("BiddingProjectPlan")) { //时间安排
			//获取招标公告
			((BiddingProjectPlan)component).setTender((BiddingProjectTender)getDatabaseService().findRecordByHql("from BiddingProjectTender BiddingProjectTender where BiddingProjectTender.projectId=" + component.getProjectId()));
		}
		else if(pojoName.equals("BiddingRoomSchedule")) { //开标室安排
			//获取招标公告
			((BiddingRoomSchedule)component).setAgent((BiddingProjectAgent)getDatabaseService().findRecordByHql("from BiddingProjectAgent BiddingProjectAgent where BiddingProjectAgent.projectId=" + component.getProjectId()));
		}
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		BiddingProjectTemplateService biddingProjectTemplateService = (BiddingProjectTemplateService)getTemplateService();
		//获取主记录
		BiddingProject project = ((BiddingProjectComponent)sitePage.getAttribute("record")).getProject();
		if(project!=null && applicationName.equals("bidding/project")) {
			return biddingProjectTemplateService.getTemplateHTMLDocument(pageName, project, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		}
		else {
			return biddingProjectTemplateService.getTemplateHTMLDocument(applicationName, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//获取项目组成部份
		BiddingProjectComponent component = (BiddingProjectComponent)sitePage.getAttribute("record");
		//获取主记录
		BiddingProject project = component.getProject();
		//设置标题
		String title = template.getTitle();
		template.setTitle((title==null || title.equals("") ? sitePage.getTitle() : title) + " - " + project.getProjectName());
		
		//判断是否超出提问时间,超出则删除"提问"链接
		Timestamp now = DateTimeUtils.now();
		
		//判断是否超出提问时间,超出则删除"提问"链接
		boolean hideAsk = component.getPublicEndTime()!=null && DateTimeUtils.now().after(component.getPublicEndTime()); //是否删除"提问"链接
		boolean hideSignUp = true; //是否删除"投标报名"链接
		boolean hideSubmit = true; //是否删除"标书提交"链接
		//设置页面有效时间
		if(!hideAsk) {
			PageUtils.setPageExpiresTime(component.getPublicEndTime(), request);
		}
		if(component instanceof BiddingProjectTender) { //招标公告
			BiddingProjectTender tender = (BiddingProjectTender)component;
			BiddingProjectPlan plan = (BiddingProjectPlan)getDatabaseService().findRecordByHql("from BiddingProjectPlan BiddingProjectPlan where BiddingProjectPlan.projectId=" + component.getProjectId());
			hideAsk = (plan!=null && plan.getAskEnd()!=null && now.after(plan.getAskEnd()));
			//判断是否在报名时间内,超出则删除"投标报名"链接
			hideSignUp =  biddingService.isSignUpTimeout(project);
			if(!hideSignUp) {
				//检查是否有电子版的招标文件
				List biddingDocuments = attachmentService.list("bidding/project", "biddingDocuments", tender.getProjectId(), false, 0, request);
				hideSignUp = (biddingDocuments==null || biddingDocuments.isEmpty());
			}
			//判断是否超出提交时间,超出则删除"标书提交"链接
			BiddingRoomSchedule schedule = (BiddingRoomSchedule)getDatabaseService().findRecordByHql("from BiddingRoomSchedule BiddingRoomSchedule where BiddingRoomSchedule.projectId=" + component.getProjectId());
			hideSubmit = (schedule==null || schedule.getBeginTime()==null || DateTimeUtils.now().after(DateTimeUtils.add(schedule.getBeginTime(), Calendar.MINUTE, -biddingService.getBidUploadPaddingMinutes())));
			//设置页面有效时间
			if(!hideSignUp) {
				PageUtils.setPageExpiresTime(tender.getBuyDocumentEnd(), request);
			}
			if(!hideSubmit) {
				PageUtils.setPageExpiresTime(schedule==null ? null : schedule.getBeginTime(), request);
			}
			if(!hideAsk) {
				PageUtils.setPageExpiresTime(plan.getAskEnd(), request);
			}
		}
		String[] linkMapping = {"前期资料备案", "prophase", "代理抽签公示", "agentDraw", "缴纳场地费", "useFee",
								"开标室安排", "bidopeningRoomSchedule", "预审公示", "preapproval", "招标文件", "tender",
								"招标公告", "tender",	"时间安排", "plan", "答疑纪要", "answer",
								"补充通知", "supplement", "开标公示", "bidopening", "中标公示", "pitchon", 
								"中标通知书", "notice", "缴费", "pay", "书面报告备案", "archive", 
								"报建", "declare", "施工许可证", "licence"};
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
			if("提问".equals(linkName)) {
				if(hideAsk) {
					a.getParentNode().removeChild(a);
				}
			}
			else if("投标报名".equals(linkName)) {
				if(hideSignUp) {
					a.getParentNode().removeChild(a);
				}
			}
			else if("提交标书".equals(linkName)) {
				if(hideSubmit) {
					a.getParentNode().removeChild(a);
				}
			}
			else {
				for(int j=0; j<linkMapping.length; j+=2) {
					if(linkMapping[j].equals(linkName)) {
						if(!project.isComponentPublic(linkMapping[j + 1])) {
							a.getParentNode().removeChild(a);
						}
						break;
					}
				}
			}
			
		}
		if("中标通知书打印".equals(sitePage.getTitle())) {
			//插入打印脚本
			template.getBody().setAttribute("onload", "window.print()");
		}
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the biddingService
	 */
	public BiddingService getBiddingService() {
		return biddingService;
	}

	/**
	 * @param biddingService the biddingService to set
	 */
	public void setBiddingService(BiddingService biddingService) {
		this.biddingService = biddingService;
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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