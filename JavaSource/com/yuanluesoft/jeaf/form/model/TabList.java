package com.yuanluesoft.jeaf.form.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 页签列表
 * @author linchuan
 *
 */
public class TabList extends ArrayList {
	
	public TabList() {
		super();
	}

	/**
	 * 添加页签
	 * @param index -1添加到最后
	 * @param tabId
	 * @param tabName
	 * @param jspFile
	 * @param selected
	 */
	public Tab addTab(int index, String tabId, String tabName, String jspFile, boolean selected) {
		//检查是否已经存在
		Tab tab = (Tab)ListUtils.findObjectByProperty(this, "id", tabId);
		if(tab==null) {
			tab = new Tab(tabId, tabName, jspFile, selected);
			if(index==-1) {
				add(tab);
			}
			else {
				add(index, tab);
			}
		}
		if(size()==1) {
			tab.setSelected(true);
		}	
		else if(selected) { //选中,清空其他页签的选中状态
			setTabSelected(tab);
		}
		return tab;
	}
	
	/**
	 * 插入一个TAB,如果找不到指定的页签,则追加到最后
	 * @param targetTabId
	 * @param tabId
	 * @param tabName
	 * @param jspFile
	 * @param selected
	 * @return
	 */
	public Tab insertTab(String targetTabId, String tabId, String tabName, String jspFile, boolean selected) {
		int i = size() - 1;
		for(; i>=0 && !((Tab)get(i)).getId().equals(targetTabId); i--);
		return addTab(i, tabId, tabName, jspFile, selected);
	}
	
	/**
	 * 删除一个TAB
	 * @param tabId
	 */
	public void removeTab(String tabId) {
		ListUtils.removeObjectByProperty(this, "id", tabId);
	}
	
	/**
	 * 获取TAB
	 * @param tabId
	 * @return
	 */
	public Tab getTab(String tabId) {
		return (Tab)ListUtils.findObjectByProperty(this, "id", tabId);
	}
	
	/**
	 * 把指定页签设置为选中
	 * @param tabId
	 */
	public void setTabSelected(String tabId) {
		setTabSelected((Tab)ListUtils.findObjectByProperty(this, "id", tabId));
	}
	
	/**
	 * 把指定页签设置为选中
	 * @param tab
	 */
	private void setTabSelected(Tab tab) {
		if(tab==null) {
			return;
		}
		for(Iterator iterator = iterator(); iterator.hasNext();) {
			Tab otherTab = (Tab)iterator.next();
			otherTab.setSelected(false);
		}
		tab.setSelected(true);
	}
}