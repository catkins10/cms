package com.yuanluesoft.msa.seafarer.forms.admin;

import com.yuanluesoft.msa.seafarer.pojo.MsaCertificationExaminee;

/**
 * 
 * @author linchuan
 *
 */
public class CertificationExaminee extends CertificationExam {
	private MsaCertificationExaminee examinee = new MsaCertificationExaminee(); //考生

	/**
	 * @return the examinee
	 */
	public MsaCertificationExaminee getExaminee() {
		return examinee;
	}

	/**
	 * @param examinee the examinee to set
	 */
	public void setExaminee(MsaCertificationExaminee examinee) {
		this.examinee = examinee;
	}
}