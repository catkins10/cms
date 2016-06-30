package com.yuanluesoft.telex.base.model;

/**
 * 
 * @author linchuan
 *
 */
public class Total {
	private String type; //电报类型,包括"合计"
	private int count; //电报数量
	private int unitCount; //接收单位数量
	private int pages; //页数
	private int unitPages; //总页数
	
	public Total(String type) {
		super();
		this.type = type;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the unitCount
	 */
	public int getUnitCount() {
		return unitCount;
	}
	/**
	 * @param unitCount the unitCount to set
	 */
	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}
	/**
	 * @return the unitPages
	 */
	public int getUnitPages() {
		return unitPages;
	}
	/**
	 * @param unitPages the unitPages to set
	 */
	public void setUnitPages(int unitPages) {
		this.unitPages = unitPages;
	}
}
