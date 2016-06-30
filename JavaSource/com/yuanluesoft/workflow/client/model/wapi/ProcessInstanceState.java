/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class ProcessInstanceState implements Cloneable, Serializable {
    public static final int OPEN_NOTRUNNING_NOTSTARTED = 0; //过程实例已被创建，但是还没有启动
    public static final int OPEN_NOTRUNNING_SUSPENDED = 1; //过程实例的执行被暂停
    public static final int OPEN_RUNNING = 2; //过程实例正在执行
    public static final int CLOSED_ABORTED = 3; //过程实例的执行已被用户异常中止
    public static final int CLOSED_TERMINATED = 4; //过程实例的执行已被用户终止
    public static final int CLOSED_COMPLETED = 5; //过程实例的执行正常地完成(也就是说，没有用户的强制干予)
    
    private int state;

	public ProcessInstanceState() {
	}
	public ProcessInstanceState(String stateTitle) {
		String[] stateTitles = {"OPEN_NOTRUNNING_NOTSTARTED", 
				"OPEN_NOTRUNNING_SUSPENDED",
				"OPEN_RUNNING",
				"CLOSED_ABORTED", 
				"CLOSED_TERMINATED",
			 	"CLOSED_COMPLETED"};
		state=0;
		for(; state<stateTitles.length && !stateTitle.equals(stateTitles[state]); state++);
	}
	/**
	 * @param state
	 */
	public ProcessInstanceState(int state) {
		this.state = state;
	}
    //是否打开
    public boolean isOpen() {
    	return state<=OPEN_RUNNING;
    }
    //是否关闭
    public boolean isClosed() {
    	return state>=CLOSED_ABORTED;
    }
    //是否未运行
    public boolean isNotRunning() {
    	return state<=OPEN_NOTRUNNING_SUSPENDED;
    }
	/**
	 * @return Returns the state.
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * 获得状态标题
	 * @return
	 */
	public String getStateTitle() {
		String[] stateTitles = {"OPEN_NOTRUNNING_NOTSTARTED", 
								"OPEN_NOTRUNNING_SUSPENDED",
								"OPEN_RUNNING",
								"CLOSED_ABORTED", 
								"CLOSED_TERMINATED",
							 	"CLOSED_COMPLETED"};
		return stateTitles[state];
	}
}