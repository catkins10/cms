package com.yuanluesoft.portal.wsrp.producer.internal;

import java.io.Serializable;

import oasis.names.tc.wsrp.v1.types.RegistrationContext;
import oasis.names.tc.wsrp.v1.types.RegistrationData;

/**
 * 
 * @author linchuan
 *
 */
public class Registration implements Serializable, Cloneable {
	private RegistrationContext registrationContext;
	private RegistrationData registrationData;
	
	public Registration(RegistrationContext registrationContext, RegistrationData registrationData) {
		super();
		this.registrationContext = registrationContext;
		this.registrationData = registrationData;
	}
	/**
	 * @return the registrationContext
	 */
	public RegistrationContext getRegistrationContext() {
		return registrationContext;
	}
	/**
	 * @param registrationContext the registrationContext to set
	 */
	public void setRegistrationContext(RegistrationContext registrationContext) {
		this.registrationContext = registrationContext;
	}
	/**
	 * @return the registrationData
	 */
	public RegistrationData getRegistrationData() {
		return registrationData;
	}
	/**
	 * @param registrationData the registrationData to set
	 */
	public void setRegistrationData(RegistrationData registrationData) {
		this.registrationData = registrationData;
	}
}