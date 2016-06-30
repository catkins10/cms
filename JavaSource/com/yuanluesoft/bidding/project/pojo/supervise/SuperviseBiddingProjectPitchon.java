package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;


/**
 * 中标公示(bidding_project_pitchon)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectPitchon extends SuperviseBiddingProjectComponent {
	private String path; //中标结果文件路径,金润标书服务器上的存放目录
	private String projectNumber; //项目编号
	private Timestamp bidopeningTime; //开标时间
	private String biddingMode; //招标方式
	private long pitchonEnterpriseId; //中标人ID
	private String pitchonEnterprise; //中标人
	private String manager; //项目经理
	private double pitchonMoney; //中标金额
	private String ownerRater; //业主评委
	private String raters; //评标专家
	private String tenderee; //招标人
	private String tendereeAddress; //招标人地址
	private String tendereePostalCode; //招标人邮政编码
	private String tendereeMail; //招标人电子邮件
	private String tendereeLinkman; //招标人联系人
	private String tendereeTel; //招标人联系电话
	private String tendereeFax; //招标人传真
	private String agent; //招标代理机构
	private String agentAddress; //代理办公地址
	private String agentPostalCode; //代理邮政编码
	private String agentFax; //代理传真
	private String agentTel; //代理电话
	private String agentLinkman; //代理联系人
	private String agentMail; //代理电子邮件
	private String supervision; //招投标监督机构
	private String supervisionAddress; //监督机构地址
	private String supervisionTel; //监督电话
	private char synch = '0'; //是否同步过
	private String body; //正文,没有固定模板时使用
	
	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	/**
	 * @return the agentAddress
	 */
	public String getAgentAddress() {
		return agentAddress;
	}
	/**
	 * @param agentAddress the agentAddress to set
	 */
	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}
	/**
	 * @return the agentFax
	 */
	public String getAgentFax() {
		return agentFax;
	}
	/**
	 * @param agentFax the agentFax to set
	 */
	public void setAgentFax(String agentFax) {
		this.agentFax = agentFax;
	}
	/**
	 * @return the agentMail
	 */
	public String getAgentMail() {
		return agentMail;
	}
	/**
	 * @param agentMail the agentMail to set
	 */
	public void setAgentMail(String agentMail) {
		this.agentMail = agentMail;
	}
	/**
	 * @return the agentPostalCode
	 */
	public String getAgentPostalCode() {
		return agentPostalCode;
	}
	/**
	 * @param agentPostalCode the agentPostalCode to set
	 */
	public void setAgentPostalCode(String agentPostalCode) {
		this.agentPostalCode = agentPostalCode;
	}
	/**
	 * @return the agentTel
	 */
	public String getAgentTel() {
		return agentTel;
	}
	/**
	 * @param agentTel the agentTel to set
	 */
	public void setAgentTel(String agentTel) {
		this.agentTel = agentTel;
	}
	/**
	 * @return the biddingMode
	 */
	public String getBiddingMode() {
		return biddingMode;
	}
	/**
	 * @param biddingMode the biddingMode to set
	 */
	public void setBiddingMode(String biddingMode) {
		this.biddingMode = biddingMode;
	}
	/**
	 * @return the bidopeningTime
	 */
	public Timestamp getBidopeningTime() {
		return bidopeningTime;
	}
	/**
	 * @param bidopeningTime the bidopeningTime to set
	 */
	public void setBidopeningTime(Timestamp bidopeningTime) {
		this.bidopeningTime = bidopeningTime;
	}
	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}
	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}
	/**
	 * @return the ownerRater
	 */
	public String getOwnerRater() {
		return ownerRater;
	}
	/**
	 * @param ownerRater the ownerRater to set
	 */
	public void setOwnerRater(String ownerRater) {
		this.ownerRater = ownerRater;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the pitchonEnterprise
	 */
	public String getPitchonEnterprise() {
		return pitchonEnterprise;
	}
	/**
	 * @param pitchonEnterprise the pitchonEnterprise to set
	 */
	public void setPitchonEnterprise(String pitchonEnterprise) {
		this.pitchonEnterprise = pitchonEnterprise;
	}
	/**
	 * @return the pitchonEnterpriseId
	 */
	public long getPitchonEnterpriseId() {
		return pitchonEnterpriseId;
	}
	/**
	 * @param pitchonEnterpriseId the pitchonEnterpriseId to set
	 */
	public void setPitchonEnterpriseId(long pitchonEnterpriseId) {
		this.pitchonEnterpriseId = pitchonEnterpriseId;
	}
	/**
	 * @return the pitchonMoney
	 */
	public double getPitchonMoney() {
		return pitchonMoney;
	}
	/**
	 * @param pitchonMoney the pitchonMoney to set
	 */
	public void setPitchonMoney(double pitchonMoney) {
		this.pitchonMoney = pitchonMoney;
	}
	/**
	 * @return the projectNumber
	 */
	public String getProjectNumber() {
		return projectNumber;
	}
	/**
	 * @param projectNumber the projectNumber to set
	 */
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	/**
	 * @return the raters
	 */
	public String getRaters() {
		return raters;
	}
	/**
	 * @param raters the raters to set
	 */
	public void setRaters(String raters) {
		this.raters = raters;
	}
	/**
	 * @return the supervision
	 */
	public String getSupervision() {
		return supervision;
	}
	/**
	 * @param supervision the supervision to set
	 */
	public void setSupervision(String supervision) {
		this.supervision = supervision;
	}
	/**
	 * @return the supervisionAddress
	 */
	public String getSupervisionAddress() {
		return supervisionAddress;
	}
	/**
	 * @param supervisionAddress the supervisionAddress to set
	 */
	public void setSupervisionAddress(String supervisionAddress) {
		this.supervisionAddress = supervisionAddress;
	}
	/**
	 * @return the supervisionTel
	 */
	public String getSupervisionTel() {
		return supervisionTel;
	}
	/**
	 * @param supervisionTel the supervisionTel to set
	 */
	public void setSupervisionTel(String supervisionTel) {
		this.supervisionTel = supervisionTel;
	}
	/**
	 * @return the tenderee
	 */
	public String getTenderee() {
		return tenderee;
	}
	/**
	 * @param tenderee the tenderee to set
	 */
	public void setTenderee(String tenderee) {
		this.tenderee = tenderee;
	}
	/**
	 * @return the tendereeAddress
	 */
	public String getTendereeAddress() {
		return tendereeAddress;
	}
	/**
	 * @param tendereeAddress the tendereeAddress to set
	 */
	public void setTendereeAddress(String tendereeAddress) {
		this.tendereeAddress = tendereeAddress;
	}
	/**
	 * @return the tendereeFax
	 */
	public String getTendereeFax() {
		return tendereeFax;
	}
	/**
	 * @param tendereeFax the tendereeFax to set
	 */
	public void setTendereeFax(String tendereeFax) {
		this.tendereeFax = tendereeFax;
	}
	/**
	 * @return the tendereeLinkman
	 */
	public String getTendereeLinkman() {
		return tendereeLinkman;
	}
	/**
	 * @param tendereeLinkman the tendereeLinkman to set
	 */
	public void setTendereeLinkman(String tendereeLinkman) {
		this.tendereeLinkman = tendereeLinkman;
	}
	/**
	 * @return the tendereeMail
	 */
	public String getTendereeMail() {
		return tendereeMail;
	}
	/**
	 * @param tendereeMail the tendereeMail to set
	 */
	public void setTendereeMail(String tendereeMail) {
		this.tendereeMail = tendereeMail;
	}
	/**
	 * @return the tendereePostalCode
	 */
	public String getTendereePostalCode() {
		return tendereePostalCode;
	}
	/**
	 * @param tendereePostalCode the tendereePostalCode to set
	 */
	public void setTendereePostalCode(String tendereePostalCode) {
		this.tendereePostalCode = tendereePostalCode;
	}
	/**
	 * @return the tendereeTel
	 */
	public String getTendereeTel() {
		return tendereeTel;
	}
	/**
	 * @param tendereeTel the tendereeTel to set
	 */
	public void setTendereeTel(String tendereeTel) {
		this.tendereeTel = tendereeTel;
	}
	/**
	 * @return the agentLinkman
	 */
	public String getAgentLinkman() {
		return agentLinkman;
	}
	/**
	 * @param agentLinkman the agentLinkman to set
	 */
	public void setAgentLinkman(String agentLinkman) {
		this.agentLinkman = agentLinkman;
	}
	/**
	 * @return the synch
	 */
	public char getSynch() {
		return synch;
	}
	/**
	 * @param synch the synch to set
	 */
	public void setSynch(char synch) {
		this.synch = synch;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
