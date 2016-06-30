/*
 * Created on 2006-4-21
 *
 */
package com.yuanluesoft.workflow.client.model.user.visitor;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class RoleVisitor extends Visitor implements Serializable {

	public RoleVisitor(String id, String name) {
		super(id, name);
	}
}