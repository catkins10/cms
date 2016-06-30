package com.yuanluesoft.jeaf.application.builder.forms;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationNavigator extends Application {
	private String type = "viewLink"; //类型
	private com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator navigator = new com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator();

	/**
	 * @return the navigator
	 */
	public com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator getNavigator() {
		return navigator;
	}

	/**
	 * @param navigator the navigator to set
	 */
	public void setNavigator(
			com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator navigator) {
		this.navigator = navigator;
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
}