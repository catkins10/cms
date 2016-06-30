package com.yuanluesoft.enterprise.quality.service.spring;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocument;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocumentBody;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocumentTemplate;
import com.yuanluesoft.enterprise.quality.service.QualityDocumentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class QualityDocumentServiceImpl extends BusinessServiceImpl implements QualityDocumentService {
	private TemplateService templateService; //模板服务
	private PageBuilder pageBuilder; //页面生成器
	private HTMLParser htmlParser; //HTML解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.quality.service.QualityDocumentService#appendDocument(com.yuanluesoft.enterprise.quality.pojo.QualityDocument, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void appendDocument(QualityDocument document, long templateId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取模板记录
		QualityDocumentTemplate documentTemplate = (QualityDocumentTemplate)getDatabaseService().findRecordById(QualityDocumentTemplate.class.getName(), templateId);
		//获取模板HTML
		HTMLDocument templateDocument = templateService.getTemplateHTMLDocument(templateId, 0, false, request);
		//生成页面
		SitePage sitePage = new SitePage();
		sitePage.setAttribute("record", document);
		templateDocument = pageBuilder.buildHTMLDocument(templateDocument, 0, sitePage, request, false, false, false, false);
		//创建正文记录
		QualityDocumentBody documentBody = new QualityDocumentBody();
		documentBody.setId(UUIDLongGenerator.generateId()); //ID
		documentBody.setDocumentId(document.getId()); //文档审批ID
		documentBody.setTemplateId(templateId); //文档模板ID
		documentBody.setName(documentTemplate.getTemplateName()); //名称
		documentBody.setBody(htmlParser.getBodyHTML(templateDocument, "utf-8", false)); //文档正文
		documentBody.setCreator(sessionInfo.getUserName()); //创建人
		documentBody.setCreatorId(sessionInfo.getUserId()); //创建人ID
		documentBody.setCreated(DateTimeUtils.now()); //创建时间
		getDatabaseService().saveRecord(documentBody);
	}

	/**
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}

	/**
	 * @return the templateService
	 */
	public TemplateService getTemplateService() {
		return templateService;
	}

	/**
	 * @param templateService the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}