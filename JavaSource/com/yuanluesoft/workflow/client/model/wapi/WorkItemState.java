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
public class WorkItemState implements Cloneable, Serializable {
    public static final int OPEN_NOTRUNNING = 0; //工作项被指派给工作流参与者，但还没有被启动
    public static final int OPEN_SUSPENDED = 1; //工作项的执行被暂停 
    public static final int OPEN_RUNNING = 2; //工作项正在执行
    public static final int CLOSED_ABORTED = 3; //工作项的执行已被异常中止，可能是由于其所属的过程实例的异常中止
    public static final int CLOSED_TERMINATED = 4; //工作项的执行已被终止，可能是由于其所属的过程实例的终止
    public static final int CLOSED_COMPLETED = 5; //工作项的执行正常地完成(也就是说，没有用户的强制干予，或由于其所属过程实例的状态改变)

    private int state;
    
	public boolean equals(int state) {
		return this.state==state; 
	}
	/**
	 * @param state
	 */
	public WorkItemState(int state) {
		this.state = state;
	}
	public WorkItemState(String stateTitle) {
		String[] stateTitles = {"OPEN_NOTRUNNING", 
				"OPEN_SUSPENDED",
				"OPEN_RUNNING",
				"CLOSED_ABORTED", 
				"CLOSED_TERMINATED",
			 	"CLOSED_COMPLETED"};
		state=0;
		for(; state<stateTitles.length && !stateTitle.equals(stateTitles[state]); state++);
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
