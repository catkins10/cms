package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

/**
 * 编程决定的部门办理人
 * @author linchuan
 *
 */
public class ProgrammingParticipantDepartment extends ParticipantDepartment implements Serializable {
	private String programmingParticipantId; //编程决定的办理人ID

	public ProgrammingParticipantDepartment() {
		super();
	}

	public ProgrammingParticipantDepartment(String id, String name, String programmingParticipantId) {
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