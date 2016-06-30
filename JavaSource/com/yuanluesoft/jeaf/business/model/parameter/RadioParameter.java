package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:单选框
 * @author linchuan
 *
 */
public class RadioParameter {
	private String itemsText; //条目列表,格式:名称1|值1\0名称2|值2...
	private String itemsHql; //获取条目列表的hql,第一个值是条目名称,第二个是条目值, 如:select Site.name, Site.id from Site Site
	private String itemsSql; //获取条目列表的sql,第一个值是条目名称,第二个是条目值, 如: select name, id from cms_site
	private String itemsServiceName; //获取条目列表的服务名称,必须实现BusinessService接口
	private String itemsName; //条目列表名称,让BusinessService识别当前要获取的内容,默认等于Field.name
	
	/**
	 * @return the itemsHql
	 */
	public String getItemsHql() {
		return itemsHql;
	}
	/**
	 * @param itemsHql the itemsHql to set
	 */
	public void setItemsHql(String itemsHql) {
		this.itemsHql = itemsHql;
	}
	/**
	 * @return the itemsName
	 */
	public String getItemsName() {
		return itemsName;
	}
	/**
	 * @param itemsName the itemsName to set
	 */
	public void setItemsName(String itemsName) {
		this.itemsName = itemsName;
	}
	/**
	 * @return the itemsServiceName
	 */
	public String getItemsServiceName() {
		return itemsServiceName;
	}
	/**
	 * @param itemsServiceName the itemsServiceName to set
	 */
	public void setItemsServiceName(String itemsServiceName) {
		this.itemsServiceName = itemsServiceName;
	}
	/**
	 * @return the itemsText
	 */
	public String getItemsText() {
		return itemsText;
	}
	/**
	 * @param itemsText the itemsText to set
	 */
	public void setItemsText(String itemsText) {
		this.itemsText = itemsText;
	}
	/**
	 * @return the itemsSql
	 */
	public String getItemsSql() {
		return itemsSql;
	}
	/**
	 * @param itemsSql the itemsSql to set
	 */
	public void setItemsSql(String itemsSql) {
		this.itemsSql = itemsSql;
	}
}