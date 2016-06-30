package com.yuanluesoft.bbs.article.actions.viewarticle;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.article.forms.ViewArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticleSubjection;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.base.actions.BbsViewFormAction;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class ViewArticleAction extends BbsViewFormAction {

	public ViewArticleAction() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "bbs/article";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "reply";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		String where = "BbsReply.articleId=" + viewForm.getId();
		view.addWhere(where);
	}

	/**
	 * 权限控制
	 * @param form
	 * @param request
	 * @param pojo
	 * @param openMode
	 * @param acl
	 * @param sessionInfo
	 * @return
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	protected void checkViewPrivilege(BbsArticle bbsArticle, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(((BbsSessionInfo)sessionInfo).getVip()=='1') { //VIP用户
			return; 
		}
		//检查论坛的权限,隶属多个版块的主题,按最低要求来控制
		ForumService forumService = (ForumService)getService("forumService");
		for(Iterator iterator = bbsArticle.getSubjections().iterator(); iterator.hasNext();) {
			BbsArticleSubjection subjection = (BbsArticleSubjection)iterator.next();
			Forum forum;
			try {
				forum = (Forum)forumService.getDirectory(subjection.getForumId());
			}
			catch (Exception e) {
				throw new PrivilegeException();
			}
			if(forum.getVipOnly()!='1' && forum.getAnonymousEnabled()=='1') { //允许匿名访问
				return;
			}
			List popedoms = forumService.listDirectoryPopedoms(forum.getId(), sessionInfo);
			if(forum.getVipOnly()!='1' && popedoms!=null && !popedoms.isEmpty()) {
				return;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.base.actions.BbsViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		ViewArticle viewArticleForm = (ViewArticle)viewForm;
		//获取文章
		BbsArticleService bbsArticleService = (BbsArticleService)getService("bbsArticleService");
		BbsArticle bbsArticle = bbsArticleService.getArticle(viewArticleForm.getId());
		//权限检查
		checkViewPrivilege(bbsArticle, sessionInfo);
		viewArticleForm.setArticle(bbsArticle);
		if("get".equals(request.getMethod().toLowerCase())) { //是打开操作
			//增加文章的访问次数
			bbsArticleService.increaseAccessCount(bbsArticle);
		}
		//获取所在的位置
		BbsArticleSubjection subjection = (BbsArticleSubjection)bbsArticle.getSubjections().iterator().next();
		ForumService forumService = (ForumService)getService("forumService");
		viewArticleForm.setParentDirectories(forumService.listParentDirectories(subjection.getForumId(), "bbs"));
		//所在论坛版块
		Forum forum = (Forum)forumService.getDirectory(subjection.getForumId());
		viewArticleForm.getParentDirectories().add(forum);
		viewArticleForm.setForum(forum);
		//获取上一主题和下一主题
		viewArticleForm.setNextArticle(bbsArticleService.getNextArticle(bbsArticle, subjection.getForumId()));
		viewArticleForm.setPreviousArticle(bbsArticleService.getPreviousArticle(bbsArticle, subjection.getForumId()));
		//判断是不是版主
		List popedoms = forumService.listDirectoryPopedoms(subjection.getForumId(), sessionInfo);
		viewArticleForm.setManager(popedoms!=null && popedoms.contains("manager"));
	}
}