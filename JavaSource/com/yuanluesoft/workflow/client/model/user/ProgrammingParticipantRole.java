package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

/**
 * 编程决定的角色办理人
 * @author linchuan
 *
 */
public class ProgrammingParticipantRole extends ParticipantRole implements Serializable {
	private String programmingParticipantId; //编程决定的办理人ID

	public ProgrammingParticipantRole() {
		super();
	}

	public ProgrammingParticipantRole(String id, String name, String programmingParticipantId) {
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