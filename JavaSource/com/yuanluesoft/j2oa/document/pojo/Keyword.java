/*
 * Created on 2005-9-10
 *
 */
package com.yuanluesoft.j2oa.document.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公文配置:主题词(document_keyword)
 * @author linchuan
 *
 */
public class Keyword extends Record {
	private long categoryId; //分类ID
	private String keyword; //主题词
	private int keywordIndex; //序号

	/**
	 * @return Returns the categotyId.
	 */
	public long getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categotyId The categotyId to set.
	 */
	public void setCategoryId(long categotyId) {
		this.categoryId = categotyId;
	}
	/**
	 * @return Returns the keyword.
	 */
	public java.lang.String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword The keyword to set.
	 */
	public void setKeyword(java.lang.String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return Returns the keywordIndex.
	 */
	public int getKeywordIndex() {
		return keywordIndex;
	}
	/**
	 * @param keywordIndex The keywordIndex to set.
	 */
	public void setKeywordIndex(int keywordIndex) {
		this.keywordIndex = keywordIndex;
	}
}
