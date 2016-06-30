package com.yuanluesoft.jeaf.application.builder.model;

/**
 * STRUTS操作
 * @author linchuan
 *
 */
public class StrutsAction {
	private String path; //路径
	private String formBeanName; //表单BEAN名称
	private String actionClassName; //类
	private String input;
	private String[] forwardName;
	private String[] forwardPath;
	
	public StrutsAction(String path, String formBeanName, String actionClassName, String input, String forwardName, String forwardPath) {
		super();
		this.path = path;
		this.formBeanName = formBeanName;
		this.actionClassName = actionClassName;
		this.input = input;
		if(forwardName!=null) {
			this.forwardName = new String[]{forwardName};
			this.forwardPath = new String[]{forwardPath};
		}
	}

	/**
	 * @return the formBeanName
	 */
	public String getFormBeanName() {
		return formBeanName;
	}

	/**
	 * @param formBeanName the formBeanName to set
	 */
	public void setFormBeanName(String formBeanName) {
		this.formBeanName = formBeanName;
	}


	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the forwardName
	 */
	public String[] getForwardName() {
		return forwardName;
	}

	/**
	 * @param forwardName the forwardName to set
	 */
	public void setForwardName(String[] forwardName) {
		this.forwardName = forwardName;
	}

	/**
	 * @return the forwardPath
	 */
	public String[] getForwardPath() {
		return forwardPath;
	}

	/**
	 * @param forwardPath the forwardPath to set
	 */
	public void setForwardPath(String[] forwardPath) {
		this.forwardPath = forwardPath;
	}

	/**
	 * @return the actionClassName
	 */
	public String getActionClassName() {
		return actionClassName;
	}

	/**
	 * @param actionClassName the actionClassName to set
	 */
	public void setActionClassName(String actionClassName) {
		this.actionClassName = actionClassName;
	}
}