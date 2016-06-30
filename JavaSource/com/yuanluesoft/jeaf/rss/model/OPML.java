package com.yuanluesoft.jeaf.rss.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class OPML {
	private String title;
	private List outlines; //大纲列表

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the outlines
	 */
	public List getOutlines() {
		return outlines;
	}

	/**
	 * @param outlines the outlines to set
	 */
	public void setOutlines(List outlines) {
		this.outlines = outlines;
	}
}
