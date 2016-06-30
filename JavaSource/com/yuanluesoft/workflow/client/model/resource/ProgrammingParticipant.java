package com.yuanluesoft.workflow.client.model.resource;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 编程的办理人
 * @author linchuan
 *
 */
public class ProgrammingParticipant extends Element implements Serializable {

	public ProgrammingParticipant() {
		super();
	}

	public ProgrammingParticipant(String id, String name) {
		super(id, name);
	}
}