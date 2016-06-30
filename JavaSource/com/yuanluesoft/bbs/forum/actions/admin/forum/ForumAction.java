package com.yuanluesoft.bbs.forum.actions.admin.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bbs.forum.actions.admin.directory.DirectoryAction;
import com.yuanluesoft.bbs.forum.forms.admin.Forum;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ForumAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.forum.actions.admin.directory.DirectoryAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Forum forumForm = (Forum)form;
		forumForm.setPageArticles(30); //论坛每页显示主题数
		forumForm.setPageReplies(30); //每贴显示的回复数
		forumForm.setAnonymousEnabled('1'); //是否允许匿名访问
		forumForm.setAnonymousReply('0'); //是否允许匿名回复
		forumForm.setAnonymousCreate('0'); //是否允许匿名发帖
		forumForm.setVipOnly('0'); //是否只允许VIP访问
		forumForm.setAnonymousDownload('1'); //是否允许匿名用户下载附件
		forumForm.setManagerCreateOnly('0'); //仅允许版主发帖
	}
}
