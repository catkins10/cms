/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.workflow.client.model.resource;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Action extends Element implements Serializable {
	private String prompt; //未执行时的提示
	private boolean necessary; //是否必须执行
	private boolean promptOnCreate; //创建时是否需要在未执行时的提示
	private boolean deleteAction; //是否删除操作
	private boolean send; //操作中是否包含流程发送操作
	private boolean reverse; //操作中是否包含流程回退操作
	private boolean transmit; //操作中是否包含转办操作
	
	public Action() {
		super();
	}
	
	public Action(String id, String name) {
		super(id, name);
	}
	
	public Action(String name, String prompt, boolean necessary, boolean promptOnCreate, boolean deleteAction, boolean send, boolean reverse) {
		super(null, name);
		this.prompt = prompt;
		this.necessary = necessary;
		this.promptOnCreate = promptOnCreate;
		this.deleteAction = deleteAction;
		this.send = send;
		this.reverse = reverse;
	}

	/**
	 * @return Returns the necessary.
	 */
	public boolean isNecessary() {
		return necessary;
	}
	/**
	 * @param necessary The necessary to set.
	 */
	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}
	/**
	 * @return Returns the prompt.
	 */
	public String getPrompt() {
		return prompt;
	}
	/**
	 * @param prompt The prompt to set.
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	/**
	 * @return Returns the promptOnCreate.
	 */
	public boolean isPromptOnCreate() {
		return promptOnCreate;
	}
	/**
	 * @param promptOnCreate The promptOnCreate to set.
	 */
	public void setPromptOnCreate(boolean promptOnCreate) {
		this.promptOnCreate = promptOnCreate;
	}
	/**
	 * @return Returns the send.
	 */
	public boolean isSend() {
		return send;
	}
	/**
	 * @param send The send to set.
	 */
	public void setSend(boolean send) {
		this.send = send;
	}
	/**
	 * @return Returns the deleteAction.
	 */
	public boolean isDeleteAction() {
		return deleteAction;
	}
	/**
	 * @param deleteAction The deleteAction to set.
	 */
	public void setDeleteAction(boolean deleteAction) {
		this.deleteAction = deleteAction;
	}
    /**
     * @return Returns the reverse.
     */
    public boolean isReverse() {
        return reverse;
    }
    /**
     * @param reverse The reverse to set.
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

	/**
	 * @return the transmit
	 */
	public boolean isTransmit() {
		return transmit;
	}

	/**
	 * @param transmit the transmit to set
	 */
	public void setTransmit(boolean transmit) {
		this.transmit = transmit;
	}
}
