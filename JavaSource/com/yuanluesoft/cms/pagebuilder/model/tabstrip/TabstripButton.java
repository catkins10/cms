package com.yuanluesoft.cms.pagebuilder.model.tabstrip;

/**
 * TAB选项
 * @author linchuan
 *
 */
public class TabstripButton {
	private String tabstripBody;
	private String moreLink;
	private String mouseOverStyle;
	private String selectedStyle;
	private String unselectedStyle;
	
	/**
	 * @return the bodyFormat
	 */
	public String getTabstripBody() {
		return tabstripBody;
	}
	/**
	 * @param bodyFormat the bodyFormat to set
	 */
	public void setTabstripBody(String bodyFormat) {
		this.tabstripBody = bodyFormat;
	}
	/**
	 * @return the moreLinkFormat
	 */
	public String getMoreLink() {
		return moreLink;
	}
	/**
	 * @param moreLinkFormat the moreLinkFormat to set
	 */
	public void setMoreLink(String moreLinkFormat) {
		this.moreLink = moreLinkFormat;
	}
	/**
	 * @return the mouseOverStyle
	 */
	public String getMouseOverStyle() {
		return mouseOverStyle;
	}
	/**
	 * @param mouseOverStyle the mouseOverStyle to set
	 */
	public void setMouseOverStyle(String mouseOverStyle) {
		this.mouseOverStyle = mouseOverStyle;
	}
	/**
	 * @return the selectedStyle
	 */
	public String getSelectedStyle() {
		return selectedStyle;
	}
	/**
	 * @param selectedStyle the selectedStyle to set
	 */
	public void setSelectedStyle(String selectedStyle) {
		this.selectedStyle = selectedStyle;
	}
	/**
	 * @return the unselectedStyle
	 */
	public String getUnselectedStyle() {
		return unselectedStyle;
	}
	/**
	 * @param unselectedStyle the unselectedStyle to set
	 */
	public void setUnselectedStyle(String unselectedStyle) {
		this.unselectedStyle = unselectedStyle;
	}
}