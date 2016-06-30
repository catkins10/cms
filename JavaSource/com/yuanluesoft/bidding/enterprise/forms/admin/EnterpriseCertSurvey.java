package com.yuanluesoft.bidding.enterprise.forms.admin;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCertSurvey;

/**
 * 
 * @author linchuan
 *
 */
public class EnterpriseCertSurvey extends Enterprise {
	private BiddingEnterpriseCertSurvey survey = new BiddingEnterpriseCertSurvey();

	/**
	 * @return the survey
	 */
	public BiddingEnterpriseCertSurvey getSurvey() {
		return survey;
	}

	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(BiddingEnterpriseCertSurvey survey) {
		this.survey = survey;
	}
}