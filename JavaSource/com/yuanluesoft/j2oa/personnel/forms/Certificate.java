package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelCertificate;


/**
 * 
 * @author linchuan
 *
 */
public class Certificate extends Employee {
	private PersonnelCertificate certificate = new PersonnelCertificate();

	/**
	 * @return the certificate
	 */
	public PersonnelCertificate getCertificate() {
		return certificate;
	}

	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(PersonnelCertificate certificate) {
		this.certificate = certificate;
	}
}