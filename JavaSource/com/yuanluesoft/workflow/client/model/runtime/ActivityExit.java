/*
 * Created on 2006-4-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

/**
 * 类型为"流程环节"的流程出口
 * @author LinChuan
 *
 */
public class ActivityExit extends BaseExit {
	private boolean autoSend; //是否自动发送
	private List transactModes; //办理方式列表
	private List participants; //办理人列表,如果其中有ProgrammingParticipant,必须有程序负责转换
	private List programmingVisitors; //由程序决定的查询人列表,输出时是ProgrammingParticipant,输入时必须转换为实际的用户
	private boolean anyoneParticipate; //是否谁都可以办理
	private double urgeHours; //催办周期
	private boolean urgeHoursAdjustable; //催办周期可调整
	
	/**
	 * @return Returns the autoSend.
	 */
	public boolean isAutoSend() {
		return autoSend;
	}
	/**
	 * @param autoSend The autoSend to set.
	 */
	public void setAutoSend(boolean autoSend) {
		this.autoSend = autoSend;
	}
	/**
	 * @return Returns the participants.
	 */
	public List getParticipants() {
		return participants;
	}
	/**
	 * @param participants The participants to set.
	 */
	public void setParticipants(List participants) {
		this.participants = participants;
	}
	/**
	 * @return Returns the transactModes.
	 */
	public List getTransactModes() {
		return transactModes;
	}
	/**
	 * @param transactModes The transactModes to set.
	 */
	public void setTransactModes(List transactModes) {
		this.transactModes = transactModes;
	}
	/**
	 * @return the programmingVisitors
	 */
	public List getProgrammingVisitors() {
		return programmingVisitors;
	}
	/**
	 * @param programmingVisitors the programmingVisitors to set
	 */
	public void setProgrammingVisitors(List programmingVisitors) {
		this.programmingVisitors = programmingVisitors;
	}
	/**
	 * @return the anyoneParticipate
	 */
	public boolean isAnyoneParticipate() {
		return anyoneParticipate;
	}
	/**
	 * @param anyoneParticipate the anyoneParticipate to set
	 */
	public void setAnyoneParticipate(boolean anyoneParticipate) {
		this.anyoneParticipate = anyoneParticipate;
	}
	/**
	 * @return the urgeHours
	 */
	public double getUrgeHours() {
		return urgeHours;
	}
	/**
	 * @param urgeHours the urgeHours to set
	 */
	public void setUrgeHours(double urgeHours) {
		this.urgeHours = urgeHours;
	}
	/**
	 * @return the urgeHoursAdjustable
	 */
	public boolean isUrgeHoursAdjustable() {
		return urgeHoursAdjustable;
	}
	/**
	 * @param urgeHoursAdjustable the urgeHoursAdjustable to set
	 */
	public void setUrgeHoursAdjustable(boolean urgeHoursAdjustable) {
		this.urgeHoursAdjustable = urgeHoursAdjustable;
	}
}