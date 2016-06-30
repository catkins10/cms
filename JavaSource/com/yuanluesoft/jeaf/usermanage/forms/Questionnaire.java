package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Questionnaire extends ActionForm {
	private char hasComputer = '0';
	private char connectInternet = '0';
	private String whoUseInternet;
	private String internetPurpose;
	private String connectMode;
	private double bandwidth; 
	private String connectLimit;
	private String carrier;
	private int connectBegin;
	private int connectEnd;
	private int timesPerWeek;
	private String useRate;
	private char connectInternetForSite = '0';
	private String eduContent;
	private String internetProblem;
	private String whatEduSite;
	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}
	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	/**
	 * @return the connectBegin
	 */
	public int getConnectBegin() {
		return connectBegin;
	}
	/**
	 * @param connectBegin the connectBegin to set
	 */
	public void setConnectBegin(int connectBegin) {
		this.connectBegin = connectBegin;
	}
	/**
	 * @return the connectEnd
	 */
	public int getConnectEnd() {
		return connectEnd;
	}
	/**
	 * @param connectEnd the connectEnd to set
	 */
	public void setConnectEnd(int connectEnd) {
		this.connectEnd = connectEnd;
	}
	/**
	 * @return the connectInternet
	 */
	public char getConnectInternet() {
		return connectInternet;
	}
	/**
	 * @param connectInternet the connectInternet to set
	 */
	public void setConnectInternet(char connectInternet) {
		this.connectInternet = connectInternet;
	}
	/**
	 * @return the connectInternetForSite
	 */
	public char getConnectInternetForSite() {
		return connectInternetForSite;
	}
	/**
	 * @param connectInternetForSite the connectInternetForSite to set
	 */
	public void setConnectInternetForSite(char connectInternetForSite) {
		this.connectInternetForSite = connectInternetForSite;
	}
	/**
	 * @return the connectLimit
	 */
	public String getConnectLimit() {
		return connectLimit;
	}
	/**
	 * @param connectLimit the connectLimit to set
	 */
	public void setConnectLimit(String connectLimit) {
		this.connectLimit = connectLimit;
	}
	/**
	 * @return the connectMode
	 */
	public String getConnectMode() {
		return connectMode;
	}
	/**
	 * @param connectMode the connectMode to set
	 */
	public void setConnectMode(String connectMode) {
		this.connectMode = connectMode;
	}
	/**
	 * @return the eduContent
	 */
	public String getEduContent() {
		return eduContent;
	}
	/**
	 * @param eduContent the eduContent to set
	 */
	public void setEduContent(String eduContent) {
		this.eduContent = eduContent;
	}
	/**
	 * @return the hasComputer
	 */
	public char getHasComputer() {
		return hasComputer;
	}
	/**
	 * @param hasComputer the hasComputer to set
	 */
	public void setHasComputer(char hasComputer) {
		this.hasComputer = hasComputer;
	}
	/**
	 * @return the internetProblem
	 */
	public String getInternetProblem() {
		return internetProblem;
	}
	/**
	 * @param internetProblem the internetProblem to set
	 */
	public void setInternetProblem(String internetProblem) {
		this.internetProblem = internetProblem;
	}
	/**
	 * @return the internetPurpose
	 */
	public String getInternetPurpose() {
		return internetPurpose;
	}
	/**
	 * @param internetPurpose the internetPurpose to set
	 */
	public void setInternetPurpose(String internetPurpose) {
		this.internetPurpose = internetPurpose;
	}
	/**
	 * @return the timesPerWeek
	 */
	public int getTimesPerWeek() {
		return timesPerWeek;
	}
	/**
	 * @param timesPerWeek the timesPerWeek to set
	 */
	public void setTimesPerWeek(int timesPerWeek) {
		this.timesPerWeek = timesPerWeek;
	}
	/**
	 * @return the useRate
	 */
	public String getUseRate() {
		return useRate;
	}
	/**
	 * @param useRate the useRate to set
	 */
	public void setUseRate(String useRate) {
		this.useRate = useRate;
	}
	/**
	 * @return the whatEduSite
	 */
	public String getWhatEduSite() {
		return whatEduSite;
	}
	/**
	 * @param whatEduSite the whatEduSite to set
	 */
	public void setWhatEduSite(String whatEduSite) {
		this.whatEduSite = whatEduSite;
	}
	/**
	 * @return the whoUseInternet
	 */
	public String getWhoUseInternet() {
		return whoUseInternet;
	}
	/**
	 * @param whoUseInternet the whoUseInternet to set
	 */
	public void setWhoUseInternet(String whoUseInternet) {
		this.whoUseInternet = whoUseInternet;
	}
	/**
	 * @return the bandwidth
	 */
	public double getBandwidth() {
		return bandwidth;
	}
	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	
}
