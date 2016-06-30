package com.yuanluesoft.bbs.templatemanage.service.spring;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bbs.article.forms.Article;
import com.yuanluesoft.bbs.article.forms.Reply;
import com.yuanluesoft.bbs.article.forms.ViewArticle;
import com.yuanluesoft.bbs.forum.forms.Bbs;
import com.yuanluesoft.bbs.forum.forms.Category;
import com.yuanluesoft.bbs.forum.forms.Forum;
import com.yuanluesoft.bbs.templatemanage.service.BbsTemplateService;
import com.yuanluesoft.cms.pagebuilder.spring.SiteFormServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class BbsFormServiceImpl extends SiteFormServiceImpl {
	private BbsTemplateService bbsTemplateService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.spring.ApplicationFormServiceImpl#getTemplateDocument(java.lang.String, java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	protected HTMLDocument getTemplateDocument(String applicationName, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, ActionForm actionForm, HttpServletRequest request) throws ServiceException {
		long directoryId = 0;
		if(actionForm instanceof Bbs) {
			directoryId = ((Bbs)actionForm).getId();
		}
		else if(actionForm instanceof Category) {
			directoryId = ((Category)actionForm).getId();
		}
		else if(actionForm instanceof Forum) {
			directoryId = ((Forum)actionForm).getId();
		}
		else if(actionForm instanceof Article) {
			directoryId = ((Article)actionForm).getForumId();
		}
		else if(actionForm instanceof Reply) {
			directoryId = ((Reply)actionForm).getForum().getId();
		}
		else if(actionForm instanceof ViewArticle) {
			directoryId = ((ViewArticle)actionForm).getForum().getId();
		}
		return bbsTemplateService.getTemplateHTMLDocument(pageName, directoryId, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/**
	 * @return the bbsTemplateService
	 */
	public BbsTemplateService getBbsTemplateService() {
		return bbsTemplateService;
	}

	/**
	 * @param bbsTemplateService the bbsTemplateService to set
	 */
	public void setBbsTemplateService(BbsTemplateService bbsTemplateService) {
		this.bbsTemplateService = bbsTemplateService;
	}
}