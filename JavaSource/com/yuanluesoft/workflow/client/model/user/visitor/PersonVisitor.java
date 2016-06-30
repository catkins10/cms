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
public class PersonVisitor extends Visitor implements Serializable {

	public PersonVisitor(String id, String name) {
		super(id, name);
	}	
}