package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.user.Person;

/**
 * 编程决定的个人办理人
 * @author linchuan
 *
 */
public class ProgrammingPerson extends Person implements Serializable {
	private String programmingParticipantId; //编程决定的办理人ID

	public ProgrammingPerson() {
		super();
	}

	public ProgrammingPerson(String id, String name, String programmingParticipantId) {
		super(id, name);
		this.programmingParticipantId = programmingParticipantId;
	}

	/**
	 * @return the programmingParticipantId
	 */
	public String getProgrammingParticipantId() {
		return programmingParticipantId;
	}

	/**
	 * @param programmingParticipantId the programmingParticipantId to set
	 */
	public void setProgrammingParticipantId(String programmingParticipantId) {
		this.programmingParticipantId = programmingParticipantId;
	}
}