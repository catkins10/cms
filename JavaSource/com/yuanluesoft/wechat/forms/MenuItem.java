package com.yuanluesoft.wechat.forms;

import com.yuanluesoft.wechat.pojo.WechatMenuItem;

/**
 * 
 * @author linchuan
 *
 */
public class MenuItem extends Account {
	private WechatMenuItem menuItem = new WechatMenuItem();

	/**
	 * @return the menuItem
	 */
	public WechatMenuItem getMenuItem() {
		return menuItem;
	}

	/**
	 * @param menuItem the menuItem to set
	 */
	public void setMenuItem(WechatMenuItem menuItem) {
		this.menuItem = menuItem;
	}
}