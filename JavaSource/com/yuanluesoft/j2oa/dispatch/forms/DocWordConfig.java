/*
 * Created on 2005-8-17
 *
 */
package com.yuanluesoft.j2oa.dispatch.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *
 * @author linchuan
 *
 */
public class DocWordConfig extends ActionForm {
	private String docWord; //文件字名称
	private String format; //文件字格式
	private long groupId; //联合编号组ID
	
	private String unionDocWords;
	
	/**
	 * @return Returns the docWord.
	 */
	public java.lang.String getDocWord() {
		return docWord;
	}
	/**
	 * @param docWord The docWord to set.
	 */
	public void setDocWord(java.lang.String docWord) {
		this.docWord = docWord;
	}
	/**
	 * @return Returns the format.
	 */
	public java.lang.String getFormat() {
		return format;
	}
	/**
	 * @param format The format to set.
	 */
	public void setFormat(java.lang.String format) {
		this.format = format;
	}
	/**
	 * @return Returns the unionDocWords.
	 */
	public java.lang.String getUnionDocWords() {
		return unionDocWords;
	}
	/**
	 * @param unionDocWords The unionDocWords to set.
	 */
	public void setUnionDocWords(java.lang.String unionDocWords) {
		this.unionDocWords = unionDocWords;
	}
	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
}