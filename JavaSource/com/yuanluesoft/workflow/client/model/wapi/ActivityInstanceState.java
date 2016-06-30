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
public class ActivityInstanceState implements Cloneable, Serializable {
    public static final int OPEN_NOTRUNNING = 0; //活动实例已准备完毕，但还没有被启动
    public static final int OPEN_SUSPENDED = 1; //活动实例的执行被暂停
    public static final int OPEN_RUNNING = 2; //活动实例正在执行
    public static final int CLOSED_ABORTED = 3; //活动实例的执行已被异常中止，可能是由于其所属的过程实例的异常中止
    public static final int CLOSED_TERMINATED = 4; //活动实例的执行已被终止，可能是由于其所属的过程实例的终止
    public static final int CLOSED_COMPLETED = 5; //活动实例的执行正常地完成(也就是说，没有用户的强制干予，或由于其所属过程实例的状态改变。)

    private int state;
    
	public ActivityInstanceState() {
	}
	public ActivityInstanceState(String stateTitle) {
		String[] stateTitles = {"OPEN_NOTRUNNING", 
				"OPEN_SUSPENDED",
				"OPEN_RUNNING",
				"CLOSED_ABORTED", 
				"CLOSED_TERMINATED",
			 	"CLOSED_COMPLETED"};
		state=0;
		for(; state<stateTitles.length && !stateTitles[state].equals(stateTitle); state++);
	}
	/**
	 * @param state
	 */
	public ActivityInstanceState(int state) {
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
		String[] stateTitles = {"OPEN_NOTRUNNING", 
								"OPEN_SUSPENDED",
								"OPEN_RUNNING",
								"CLOSED_ABORTED", 
								"CLOSED_TERMINATED",
							 	"CLOSED_COMPLETED"};
		return stateTitles[state];
	}
}
