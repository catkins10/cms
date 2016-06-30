package com.yuanluesoft.bbs.forum.forms;

import com.yuanluesoft.bbs.base.forms.BbsViewForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Forum extends BbsViewForm {
	private com.yuanluesoft.bbs.forum.model.Forum forum;

	/**
	 * @return the forum
	 */
	public com.yuanluesoft.bbs.forum.model.Forum getForum() {
		return forum;
	}

	/**
	 * @param forum the forum to set
	 */
	public void setForum(com.yuanluesoft.bbs.forum.model.Forum forum) {
		this.forum = forum;
	}

}