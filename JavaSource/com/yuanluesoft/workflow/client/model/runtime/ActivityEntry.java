package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class ActivityEntry extends Element {
	private List programmingParticipants; //需要由使用者自动检查的编程决定的办理人
	private List programmingVisitors; //需要由使用者自动检查的编程决定的查询人

	public ActivityEntry(String id, String name) {
		super(id, name);
	}

	/**
	 * @return the programmingParticipants
	 */
	public List getProgrammingParticipants() {
		return programmingParticipants;
	}

	/**
	 * @param programmingParticipants the programmingParticipants to set
	 */
	public void setProgrammingParticipants(List programmingParticipants) {
		this.programmingParticipants = programmingParticipants;
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
}