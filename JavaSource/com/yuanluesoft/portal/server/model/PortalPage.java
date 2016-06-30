package com.yuanluesoft.portal.server.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class PortalPage implements Serializable {
	private long id; //ID
	private String style; //样式名称
	private String layout; //布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
	private boolean alwaysDisplayPortletButtons; //是否总是显示PORTLET按钮
	private String title; //标题
	private boolean selected; //是否选中
	private List portletInstances; //PORTLET实例列表

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
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyle(String styleClass) {
		this.style = styleClass;
	}

	/**
	 * @return the portletInstances
	 */
	public List getPortletInstances() {
		return portletInstances;
	}

	/**
	 * @param portletInstances the portletInstances to set
	 */
	public void setPortletInstances(List portletInstances) {
		this.portletInstances = portletInstances;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the alwaysDisplayPortletButtons
	 */
	public boolean isAlwaysDisplayPortletButtons() {
		return alwaysDisplayPortletButtons;
	}

	/**
	 * @param alwaysDisplayPortletButtons the alwaysDisplayPortletButtons to set
	 */
	public void setAlwaysDisplayPortletButtons(boolean alwaysDisplayPortletButtons) {
		this.alwaysDisplayPortletButtons = alwaysDisplayPortletButtons;
	}
}