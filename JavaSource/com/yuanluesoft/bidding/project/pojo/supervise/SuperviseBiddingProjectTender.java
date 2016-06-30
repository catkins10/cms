package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;

/**
 * 招标公告(bidding_project_tender)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectTender extends SuperviseBiddingProjectComponent {
	private String tenderProjectName; //项目名称
	private String approvalUnit; //项目审批机关
	private String projectSn; //批名称及编号
	private String owner; //项目业主
	private String investSource; //资金来源
	private String tenderee; //招标人
	private String agent; //招标代理
	private String biddingMode; //招标方式
	private String projectAddress; //建设地点
	private String projectScale; //建设规模
	private String biddingContent; //招标范围和内容
	private String timeLimit; //总工期,日历天
	private String keysTimeLimit; //关键节点的工期要求
	private String quality; //工程质量要求
	private String consultationUnit; //咨询单位
	private String designUnit; //设计单位
	private String fillinUnit; //代建单位
	private String supervisorUnit; //监理单位
	private String bidderLevel; //资质类别及等级
	private String managerLevel; //项目经理等级
	private String managerSubject; //项目经理专业
	private String unionBid; //接受/不接受联合体投标
	private String majorBidder; //联合体投标主办方
	private String managerAchievement; //项目经理"类似工程业绩"要求
	private String similarityProject; //类似工程业绩
	private String approvalMode; //资格审查采用的方式
	private String buyDocumentAddress; //购买招标文件地址
	private Timestamp buyDocumentBegin; //购买招标文件开始时间
	private Timestamp buyDocumentEnd; //购买招标文件结束时间
	private String documentPrice; //招标文件每份售价
	private String drawingPrice; //招标图纸每份售价
	private String evaluateMethod; //采用的评标办法,综合评标
	private String pledgeTime; //保证金提交的时间
	private String pledgeMode; //保证金提交的方式
	private String pledgeMoney; //保证金提交的金额
	private Timestamp submitTime; //投标文件的递交截止时间
	private String submitAddress; //投标文件的递交地点,福州市建设工程交易管理中心
	private String media; //发布公告的媒体名称
	private String tendereeAddress; //招标人地址
	private String tendereePostalCode; //招标人邮编
	private String tendereeTel; //招标人电话
	private String tendereeFax; //招标人传真
	private String tendereeLinkman; //招标人联系人
	private String agentAddress; //招标代理机构地址
	private String agentPostalCode; //招标代理机构邮编
	private String agentTel; //招标代理机构电话
	private String agentFax; //招标代理机构传真
	private String agentLinkman; //招标代理机构联系人
	private String bank; //开户银行
	private String accountName; //帐户名称
	private String accounts; //帐号
	private String tradingName; //交易中心名称
	private String tradingAddress; //地址
	private String controlPrice; //附加：招标控制价
	private String agentGenerate; //附加：代理单位产生方式,委托
	private String body; //正文,没有固定模板时使用
	private String remark1; //备注1
	private String remark2; //备注2
	private String remark3; //备注3
	private String remark4; //备注4
	
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the accounts
	 */
	public String getAccounts() {
		return accounts;
	}
	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
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
	 * @return the approvalMode
	 */
	public String getApprovalMode() {
		return approvalMode;
	}
	/**
	 * @param approvalMode the approvalMode to set
	 */
	public void setApprovalMode(String approvalMode) {
		this.approvalMode = approvalMode;
	}
	/**
	 * @return the approvalUnit
	 */
	public String getApprovalUnit() {
		return approvalUnit;
	}
	/**
	 * @param approvalUnit the approvalUnit to set
	 */
	public void setApprovalUnit(String approvalUnit) {
		this.approvalUnit = approvalUnit;
	}
	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}
	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}
	/**
	 * @return the bidderLevel
	 */
	public String getBidderLevel() {
		return bidderLevel;
	}
	/**
	 * @param bidderLevel the bidderLevel to set
	 */
	public void setBidderLevel(String bidderLevel) {
		this.bidderLevel = bidderLevel;
	}
	/**
	 * @return the biddingContent
	 */
	public String getBiddingContent() {
		return biddingContent;
	}
	/**
	 * @param biddingContent the biddingContent to set
	 */
	public void setBiddingContent(String biddingContent) {
		this.biddingContent = biddingContent;
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
	 * @return the buyDocumentAddress
	 */
	public String getBuyDocumentAddress() {
		return buyDocumentAddress;
	}
	/**
	 * @param buyDocumentAddress the buyDocumentAddress to set
	 */
	public void setBuyDocumentAddress(String buyDocumentAddress) {
		this.buyDocumentAddress = buyDocumentAddress;
	}
	/**
	 * @return the buyDocumentBegin
	 */
	public Timestamp getBuyDocumentBegin() {
		return buyDocumentBegin;
	}
	/**
	 * @param buyDocumentBegin the buyDocumentBegin to set
	 */
	public void setBuyDocumentBegin(Timestamp buyDocumentBegin) {
		this.buyDocumentBegin = buyDocumentBegin;
	}
	/**
	 * @return the buyDocumentEnd
	 */
	public Timestamp getBuyDocumentEnd() {
		return buyDocumentEnd;
	}
	/**
	 * @param buyDocumentEnd the buyDocumentEnd to set
	 */
	public void setBuyDocumentEnd(Timestamp buyDocumentEnd) {
		this.buyDocumentEnd = buyDocumentEnd;
	}
	/**
	 * @return the consultationUnit
	 */
	public String getConsultationUnit() {
		return consultationUnit;
	}
	/**
	 * @param consultationUnit the consultationUnit to set
	 */
	public void setConsultationUnit(String consultationUnit) {
		this.consultationUnit = consultationUnit;
	}
	/**
	 * @return the designUnit
	 */
	public String getDesignUnit() {
		return designUnit;
	}
	/**
	 * @param designUnit the designUnit to set
	 */
	public void setDesignUnit(String designUnit) {
		this.designUnit = designUnit;
	}
	/**
	 * @return the documentPrice
	 */
	public String getDocumentPrice() {
		return documentPrice;
	}
	/**
	 * @param documentPrice the documentPrice to set
	 */
	public void setDocumentPrice(String documentPrice) {
		this.documentPrice = documentPrice;
	}
	/**
	 * @return the drawingPrice
	 */
	public String getDrawingPrice() {
		return drawingPrice;
	}
	/**
	 * @param drawingPrice the drawingPrice to set
	 */
	public void setDrawingPrice(String drawingPrice) {
		this.drawingPrice = drawingPrice;
	}
	/**
	 * @return the evaluateMethod
	 */
	public String getEvaluateMethod() {
		return evaluateMethod;
	}
	/**
	 * @param evaluateMethod the evaluateMethod to set
	 */
	public void setEvaluateMethod(String evaluateMethod) {
		this.evaluateMethod = evaluateMethod;
	}
	/**
	 * @return the fillinUnit
	 */
	public String getFillinUnit() {
		return fillinUnit;
	}
	/**
	 * @param fillinUnit the fillinUnit to set
	 */
	public void setFillinUnit(String fillinUnit) {
		this.fillinUnit = fillinUnit;
	}
	/**
	 * @return the investSource
	 */
	public String getInvestSource() {
		return investSource;
	}
	/**
	 * @param investSource the investSource to set
	 */
	public void setInvestSource(String investSource) {
		this.investSource = investSource;
	}
	/**
	 * @return the keysTimeLimit
	 */
	public String getKeysTimeLimit() {
		return keysTimeLimit;
	}
	/**
	 * @param keysTimeLimit the keysTimeLimit to set
	 */
	public void setKeysTimeLimit(String keysTimeLimit) {
		this.keysTimeLimit = keysTimeLimit;
	}
	/**
	 * @return the majorBidder
	 */
	public String getMajorBidder() {
		return majorBidder;
	}
	/**
	 * @param majorBidder the majorBidder to set
	 */
	public void setMajorBidder(String majorBidder) {
		this.majorBidder = majorBidder;
	}
	/**
	 * @return the managerAchievement
	 */
	public String getManagerAchievement() {
		return managerAchievement;
	}
	/**
	 * @param managerAchievement the managerAchievement to set
	 */
	public void setManagerAchievement(String managerAchievement) {
		this.managerAchievement = managerAchievement;
	}
	/**
	 * @return the managerLevel
	 */
	public String getManagerLevel() {
		return managerLevel;
	}
	/**
	 * @param managerLevel the managerLevel to set
	 */
	public void setManagerLevel(String managerLevel) {
		this.managerLevel = managerLevel;
	}
	/**
	 * @return the managerSubject
	 */
	public String getManagerSubject() {
		return managerSubject;
	}
	/**
	 * @param managerSubject the managerSubject to set
	 */
	public void setManagerSubject(String managerSubject) {
		this.managerSubject = managerSubject;
	}
	/**
	 * @return the media
	 */
	public String getMedia() {
		return media;
	}
	/**
	 * @param media the media to set
	 */
	public void setMedia(String media) {
		this.media = media;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the pledgeMode
	 */
	public String getPledgeMode() {
		return pledgeMode;
	}
	/**
	 * @param pledgeMode the pledgeMode to set
	 */
	public void setPledgeMode(String pledgeMode) {
		this.pledgeMode = pledgeMode;
	}
	/**
	 * @return the pledgeMoney
	 */
	public String getPledgeMoney() {
		return pledgeMoney;
	}
	/**
	 * @param pledgeMoney the pledgeMoney to set
	 */
	public void setPledgeMoney(String pledgeMoney) {
		this.pledgeMoney = pledgeMoney;
	}
	/**
	 * @return the pledgeTime
	 */
	public String getPledgeTime() {
		return pledgeTime;
	}
	/**
	 * @param pledgeTime the pledgeTime to set
	 */
	public void setPledgeTime(String pledgeTime) {
		this.pledgeTime = pledgeTime;
	}
	/**
	 * @return the projectAddress
	 */
	public String getProjectAddress() {
		return projectAddress;
	}
	/**
	 * @param projectAddress the projectAddress to set
	 */
	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}
	/**
	 * @return the projectScale
	 */
	public String getProjectScale() {
		return projectScale;
	}
	/**
	 * @param projectScale the projectScale to set
	 */
	public void setProjectScale(String projectScale) {
		this.projectScale = projectScale;
	}
	/**
	 * @return the projectSn
	 */
	public String getProjectSn() {
		return projectSn;
	}
	/**
	 * @param projectSn the projectSn to set
	 */
	public void setProjectSn(String projectSn) {
		this.projectSn = projectSn;
	}
	/**
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}
	/**
	 * @param quality the quality to set
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}
	/**
	 * @return the similarityProject
	 */
	public String getSimilarityProject() {
		return similarityProject;
	}
	/**
	 * @param similarityProject the similarityProject to set
	 */
	public void setSimilarityProject(String similarityProject) {
		this.similarityProject = similarityProject;
	}
	/**
	 * @return the submitAddress
	 */
	public String getSubmitAddress() {
		return submitAddress;
	}
	/**
	 * @param submitAddress the submitAddress to set
	 */
	public void setSubmitAddress(String submitAddress) {
		this.submitAddress = submitAddress;
	}
	/**
	 * @return the submitTime
	 */
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	/**
	 * @param submitTime the submitTime to set
	 */
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	/**
	 * @return the supervisorUnit
	 */
	public String getSupervisorUnit() {
		return supervisorUnit;
	}
	/**
	 * @param supervisorUnit the supervisorUnit to set
	 */
	public void setSupervisorUnit(String supervisorUnit) {
		this.supervisorUnit = supervisorUnit;
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
	 * @return the tenderProjectName
	 */
	public String getTenderProjectName() {
		return tenderProjectName;
	}
	/**
	 * @param tenderProjectName the tenderProjectName to set
	 */
	public void setTenderProjectName(String tenderProjectName) {
		this.tenderProjectName = tenderProjectName;
	}
	/**
	 * @return the timeLimit
	 */
	public String getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * @return the tradingAddress
	 */
	public String getTradingAddress() {
		return tradingAddress;
	}
	/**
	 * @param tradingAddress the tradingAddress to set
	 */
	public void setTradingAddress(String tradingAddress) {
		this.tradingAddress = tradingAddress;
	}
	/**
	 * @return the tradingName
	 */
	public String getTradingName() {
		return tradingName;
	}
	/**
	 * @param tradingName the tradingName to set
	 */
	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}
	/**
	 * @return the unionBid
	 */
	public String getUnionBid() {
		return unionBid;
	}
	/**
	 * @param unionBid the unionBid to set
	 */
	public void setUnionBid(String unionBid) {
		this.unionBid = unionBid;
	}
	/**
	 * @return the agentGenerate
	 */
	public String getAgentGenerate() {
		return agentGenerate;
	}
	/**
	 * @param agentGenerate the agentGenerate to set
	 */
	public void setAgentGenerate(String agentGenerate) {
		this.agentGenerate = agentGenerate;
	}
	/**
	 * @return the controlPrice
	 */
	public String getControlPrice() {
		return controlPrice;
	}
	/**
	 * @param controlPrice the controlPrice to set
	 */
	public void setControlPrice(String controlPrice) {
		this.controlPrice = controlPrice;
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
	/**
	 * @return the remark1
	 */
	public String getRemark1() {
		return remark1;
	}
	/**
	 * @param remark1 the remark1 to set
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	/**
	 * @return the remark2
	 */
	public String getRemark2() {
		return remark2;
	}
	/**
	 * @param remark2 the remark2 to set
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	/**
	 * @return the remark3
	 */
	public String getRemark3() {
		return remark3;
	}
	/**
	 * @param remark3 the remark3 to set
	 */
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	/**
	 * @return the remark4
	 */
	public String getRemark4() {
		return remark4;
	}
	/**
	 * @param remark4 the remark4 to set
	 */
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
}
