package com.yuanluesoft.bbs.article.actions.reply;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bbs.article.forms.Reply;
import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticleSubjection;
import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.base.actions.BbsFormAction;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ReplyAction extends BbsFormAction {

	public ReplyAction() {
		super();
		anonymousEnable = true;
		forceValidateCode = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Reply replyForm = (Reply)form;
		replyForm.setCreatorNickname(((BbsSessionInfo)sessionInfo).getNickname());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		BbsReply bbsReply = (BbsReply)record;
		Reply replyForm = (Reply)form;
		ForumService forumService = (ForumService)getService("forumService");
		BbsArticleService articleService = (BbsArticleService)getService("bbsArticleService");
		BbsArticle article;
		Forum forum;
		try {
			article = articleService.getArticle(bbsReply==null ? replyForm.getArticleId() : bbsReply.getArticleId());
			forum = (Forum)forumService.getDirectory(((BbsArticleSubjection)article.getSubjections().iterator().next()).getForumId());
		}
		catch(ServiceException e) {
			throw new PrivilegeException();
		}
		replyForm.setArticle(article);
		replyForm.setForum(forum);
		List popedoms = forumService.listDirectoryPopedoms(forum.getId(), sessionInfo);
		if(popedoms==null || popedoms.isEmpty()) {
			throw new PrivilegeException();
		}
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			if(((BbsSessionInfo)sessionInfo).getVip()=='1') { //VIP用户
				return RecordControlService.ACCESS_LEVEL_EDITABLE; 
			}
			if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名用户
				//检查是否允许匿名回复
				if(forum.getVipOnly()!='1' && forum.getAnonymousEnabled()=='1' && forum.getAnonymousReply()=='1') { //版块允许匿名访问和回复
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			else { //非匿名用户
				//检查版块是否只允许VIP用户访问
				if(forum.getVipOnly()!='1' && forum.getAnonymousEnabled()=='1') { //版块允许匿名访问
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
		}
		else if(OPEN_MODE_EDIT.equals(openMode)) { //编辑
			if(popedoms.contains("manager") || (bbsReply.getCreatorId()>0 && bbsReply.getCreatorId()==sessionInfo.getUserId())) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BbsReply bbsReply = (BbsReply)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			bbsReply.setCreatorId(sessionInfo.getUserId()); //创建者ID
			if(!SessionService.ANONYMOUS.equals(sessionInfo.getLoginName()) || bbsReply.getCreatorNickname()==null || bbsReply.getCreatorNickname().equals("")) { //非匿名用户或没有输入昵称
				bbsReply.setCreatorNickname(((BbsSessionInfo)sessionInfo).getNickname()); //创建者昵称
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
