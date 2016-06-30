package com.yuanluesoft.bbs.article.actions.article;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bbs.article.forms.Article;
import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticleSubjection;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.base.actions.BbsFormAction;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 创建和修改帖子
 * @author yuanluesoft
 *
 */
public class ArticleAction extends BbsFormAction {

	public ArticleAction() {
		super();
		anonymousEnable = true;
		forceValidateCode = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		BbsArticle article = (BbsArticle)record;
		Article articleForm = (Article)form;
		ForumService forumService = (ForumService)getService("forumService");
		Forum forum;
		try {
			forum = (Forum)forumService.getDirectory(OPEN_MODE_CREATE.equals(openMode) ? articleForm.getForumId() : ((BbsArticleSubjection)article.getSubjections().iterator().next()).getForumId());
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new PrivilegeException();
		}
		List popedoms = forumService.listDirectoryPopedoms(forum.getId(), sessionInfo);
		if(popedoms==null || popedoms.isEmpty()) {
			throw new PrivilegeException();
		}
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名用户
				//检查是否允许匿名发帖
				if(forum.getVipOnly()!='1' && forum.getAnonymousEnabled()=='1' && forum.getAnonymousCreate()=='1') { //版块允许匿名访问和发帖
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
				throw new PrivilegeException();
			}
			try {
				//判断是否只允许管理员发帖
				if(forum.getManagerCreateOnly()=='1' && !popedoms.contains("manager")) {
					throw new PrivilegeException();
				}
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new PrivilegeException();
			}
			//检查版块是否只允许VIP用户访问
			if(((BbsSessionInfo)sessionInfo).getVip()=='1') { //VIP用户
				return RecordControlService.ACCESS_LEVEL_EDITABLE; 
			}
			if(forum.getVipOnly()!='1' && forum.getAnonymousEnabled()=='1') { //版块允许匿名访问
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else if(OPEN_MODE_EDIT.equals(openMode)) { //编辑
			try {
				if(popedoms.contains("manager") ||
				   (article.getCreatorId()>0 && article.getCreatorId()==sessionInfo.getUserId())) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new PrivilegeException();
			}
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Article articleForm = (Article)form;
		articleForm.setCreatorNickname(((BbsSessionInfo)sessionInfo).getNickname());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		BbsArticle article = (BbsArticle)record;
		Article articleForm = (Article)form;
		articleForm.setForumId(((BbsArticleSubjection)article.getSubjections().iterator().next()).getForumId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Article articleForm = (Article)form;
		BbsArticle bbsArticle = (BbsArticle)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			bbsArticle.setCreatorId(sessionInfo.getUserId()); //创建者ID
			if(!SessionService.ANONYMOUS.equals(sessionInfo.getLoginName()) || bbsArticle.getCreatorNickname()==null || bbsArticle.getCreatorNickname().equals("")) { //非匿名用户或没有输入昵称
				bbsArticle.setCreatorNickname(((BbsSessionInfo)sessionInfo).getNickname()); //创建者昵称
			}
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(request.getParameter("forumId")!=null) {
			BbsArticleService bbsArticleService = (BbsArticleService)getService("bbsArticleService");
			bbsArticleService.updateArticleSubjections(bbsArticle, (OPEN_MODE_CREATE.equals(openMode)), articleForm.getForumId() + "");
		}
		return record;
	}
}
