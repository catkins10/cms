package com.yuanluesoft.telex.receive.cryptic.forms;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Sign extends ActionForm {
	private long telegramId; //电报ID,单个签收时有效
	private List signTelegrams; //待签收或已签收的电报列表
	private List returnTelegrams; //待清退或已清退的电报列表
	private String selectedTelegramSignIds; //选中的电报ID列表
	private String signPersonName; //当前签收人姓名
	private String signPersonOrgFullName; //签收人单位
	private boolean isAgent; //是否代理人
	
	//打印办理单属性
	private Timestamp printTime; //打印时间
	
	//以下属性在录入代理人信息时有效
	private String agentName; //姓名
	private String agentOrgName; //所属组织机构
	private long agentOrgId; //所属组织机构ID
	private String agentCertificateName; //证件名称
	private String agentCertificateCode; //证件号码
	private char agentSex = 'M'; //性别,F/M
	private String template; //指纹模板
	
	/**
	 * @return the agentCertificateCode
	 */
	public String getAgentCertificateCode() {
		return agentCertificateCode;
	}
	/**
	 * @param agentCertificateCode the agentCertificateCode to set
	 */
	public void setAgentCertificateCode(String agentCertificateCode) {
		this.agentCertificateCode = agentCertificateCode;
	}
	/**
	 * @return the agentCertificateName
	 */
	public String getAgentCertificateName() {
		return agentCertificateName;
	}
	/**
	 * @param agentCertificateName the agentCertificateName to set
	 */
	public void setAgentCertificateName(String agentCertificateName) {
		this.agentCertificateName = agentCertificateName;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the agentOrgId
	 */
	public long getAgentOrgId() {
		return agentOrgId;
	}
	/**
	 * @param agentOrgId the agentOrgId to set
	 */
	public void setAgentOrgId(long agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	/**
	 * @return the agentSex
	 */
	public char getAgentSex() {
		return agentSex;
	}
	/**
	 * @param agentSex the agentSex to set
	 */
	public void setAgentSex(char agentSex) {
		this.agentSex = agentSex;
	}
	/**
	 * @return the returnTelegrams
	 */
	public List getReturnTelegrams() {
		return returnTelegrams;
	}
	/**
	 * @param returnTelegrams the returnTelegrams to set
	 */
	public void setReturnTelegrams(List returnTelegrams) {
		this.returnTelegrams = returnTelegrams;
	}
	/**
	 * @return the signPersonName
	 */
	public String getSignPersonName() {
		return signPersonName;
	}
	/**
	 * @param signPersonName the signPersonName to set
	 */
	public void setSignPersonName(String signPersonName) {
		this.signPersonName = signPersonName;
	}
	/**
	 * @return the signPersonOrgFullName
	 */
	public String getSignPersonOrgFullName() {
		return signPersonOrgFullName;
	}
	/**
	 * @param signPersonOrgFullName the signPersonOrgFullName to set
	 */
	public void setSignPersonOrgFullName(String signPersonOrgFullName) {
		this.signPersonOrgFullName = signPersonOrgFullName;
	}
	/**
	 * @return the signTelegrams
	 */
	public List getSignTelegrams() {
		return signTelegrams;
	}
	/**
	 * @param signTelegrams the signTelegrams to set
	 */
	public void setSignTelegrams(List signTelegrams) {
		this.signTelegrams = signTelegrams;
	}
	/**
	 * @return the telegramId
	 */
	public long getTelegramId() {
		return telegramId;
	}
	/**
	 * @param telegramId the telegramId to set
	 */
	public void setTelegramId(long telegramId) {
		this.telegramId = telegramId;
	}
	/**
	 * @return the isAgent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	/**
	 * @param isAgent the isAgent to set
	 */
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}
	/**
	 * @return the agentOrgName
	 */
	public String getAgentOrgName() {
		return agentOrgName;
	}
	/**
	 * @param agentOrgName the agentOrgName to set
	 */
	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}
	/**
	 * @return the selectedTelegramSignIds
	 */
	public String getSelectedTelegramSignIds() {
		return selectedTelegramSignIds;
	}
	/**
	 * @param selectedTelegramSignIds the selectedTelegramSignIds to set
	 */
	public void setSelectedTelegramSignIds(String selectedTelegramSignIds) {
		this.selectedTelegramSignIds = selectedTelegramSignIds;
	}
	/**
	 * @return the printTime
	 */
	public Timestamp getPrintTime() {
		return printTime;
	}
	/**
	 * @param printTime the printTime to set
	 */
	public void setPrintTime(Timestamp printTime) {
		this.printTime = printTime;
	}
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}	
}