package com.yuanluesoft.jeaf.view.model.viewaction;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class ViewActionGroup extends CloneableObject {
	private String title; //标题
	private String image; //操作按钮图标
	private List actions; //操作列表
	
	private String execute; //执行的操作

	public ViewActionGroup() {
		super();
	}

	public ViewActionGroup(String title) {
		super();
		this.title = title;
		actions = new ArrayList();
	}
    
    /**
     * 添加视图操作
     * @param action
     */
    public void addViewAction(ViewAction action) {
    	if(actions==null) {
    		actions = new ArrayList();
    	}
    	actions.add(action);
    }
    
	/**
	 * @return the actions
	 */
	public List getActions() {
		return actions;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(List actions) {
		this.actions = actions;
	}
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
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
	 * @return the execute
	 */
	public String getExecute() {
		return execute;
	}

	/**
	 * @param execute the execute to set
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}
}