package com.yuanluesoft.webmail.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 下载在邮件服务器上的附件
 * @author linchuan
 *
 */
public class DownloadAttachment extends ActionForm {
	private boolean attachment; //是否按附件下载方式下载

	/**
	 * @return the attachment
	 */
	public boolean isAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}
}