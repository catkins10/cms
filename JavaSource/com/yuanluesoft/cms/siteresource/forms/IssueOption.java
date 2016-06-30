package com.yuanluesoft.cms.siteresource.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class IssueOption extends ActionForm {
	private int option; //发布选项,0/一直发布,1/在指定时间后撤销发布
	private Timestamp endTme; //截止时间
	private boolean reissue; //是否重新发布
	
	/**
	 * @return the endTme
	 */
	public Timestamp getEndTme() {
		return endTme;
	}
	/**
	 * @param endTme the endTme to set
	 */
	public void setEndTme(Timestamp endTme) {
		this.endTme = endTme;
	}
	/**
	 * @return the option
	 */
	public int getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(int option) {
		this.option = option;
	}
	/**
	 * @return the reissue
	 */
	public boolean isReissue() {
		return reissue;
	}
	/**
	 * @param reissue the reissue to set
	 */
	public void setReissue(boolean reissue) {
		this.reissue = reissue;
	}
}