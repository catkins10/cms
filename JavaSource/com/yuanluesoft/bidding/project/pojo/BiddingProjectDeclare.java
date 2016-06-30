package com.yuanluesoft.bidding.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 工程项目报建(bidding_project_declare)
 * @author linchuan
 *
 */
public class BiddingProjectDeclare extends BiddingProjectComponent {
	private String declareNumber; //报建编号
	private String owner; //建设单位
	private String ownerType; //建设单位性质,全民
	private String ownerRepresentative; //建设单位法人代表
	private String ownerLinkman; //建设单位联系人
	private String ownerTel; //建设单位联系电话
	private String declaringProjectName; //报建名称,房建工程/装修工程/监理工程/市政工程/专业工程/设计招标
	private String projectAddress; //建设地点
	private String projectProperty; //项目性质,生产/非生产
	private String proposalNumber; //立项文号
	private Date proposalDate; //批准时间
	private String invest; //投资规模
	private String scale; //工程规模
	private String govInvest; //拨款
	private String personalInvest; //自筹
	private String foriegnInvest; //外资
	private String otherInvest; //其他投资
	private String investYear; //当年投资
	private String govInvestYear; //当年拨款
	private String personalInvestYear; //当年自筹
	private String foriegnInvestYear; //当年外资
	private String otherInvestYear; //当年其他投资
	private String prepare; //筹建情况
	private String award; //申请发包形式
	private Date beginDate; //计划开工时间
	private Date endDate; //计划竣工时间
	private Timestamp receiveTime; //收理时间
	private String receiveNumber; //收件编号
	private long declarePersonId; //受理经办人ID
	private String declarePerson; //受理经办人
	private String verify; //核实情况
	private String remark; //备注
	/**
	 * @return the award
	 */
	public String getAward() {
		return award;
	}
	/**
	 * @param award the award to set
	 */
	public void setAward(String award) {
		this.award = award;
	}
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the declareNumber
	 */
	public String getDeclareNumber() {
		return declareNumber;
	}
	/**
	 * @param declareNumber the declareNumber to set
	 */
	public void setDeclareNumber(String declareNumber) {
		this.declareNumber = declareNumber;
	}
	/**
	 * @return the declarePerson
	 */
	public String getDeclarePerson() {
		return declarePerson;
	}
	/**
	 * @param declarePerson the declarePerson to set
	 */
	public void setDeclarePerson(String declarePerson) {
		this.declarePerson = declarePerson;
	}
	/**
	 * @return the declarePersonId
	 */
	public long getDeclarePersonId() {
		return declarePersonId;
	}
	/**
	 * @param declarePersonId the declarePersonId to set
	 */
	public void setDeclarePersonId(long declarePersonId) {
		this.declarePersonId = declarePersonId;
	}
	/**
	 * @return the declaringProjectName
	 */
	public String getDeclaringProjectName() {
		return declaringProjectName;
	}
	/**
	 * @param declaringProjectName the declaringProjectName to set
	 */
	public void setDeclaringProjectName(String declaringProjectName) {
		this.declaringProjectName = declaringProjectName;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the foriegnInvest
	 */
	public String getForiegnInvest() {
		return foriegnInvest;
	}
	/**
	 * @param foriegnInvest the foriegnInvest to set
	 */
	public void setForiegnInvest(String foriegnInvest) {
		this.foriegnInvest = foriegnInvest;
	}
	/**
	 * @return the foriegnInvestYear
	 */
	public String getForiegnInvestYear() {
		return foriegnInvestYear;
	}
	/**
	 * @param foriegnInvestYear the foriegnInvestYear to set
	 */
	public void setForiegnInvestYear(String foriegnInvestYear) {
		this.foriegnInvestYear = foriegnInvestYear;
	}
	/**
	 * @return the govInvest
	 */
	public String getGovInvest() {
		return govInvest;
	}
	/**
	 * @param govInvest the govInvest to set
	 */
	public void setGovInvest(String govInvest) {
		this.govInvest = govInvest;
	}
	/**
	 * @return the govInvestYear
	 */
	public String getGovInvestYear() {
		return govInvestYear;
	}
	/**
	 * @param govInvestYear the govInvestYear to set
	 */
	public void setGovInvestYear(String govInvestYear) {
		this.govInvestYear = govInvestYear;
	}
	/**
	 * @return the invest
	 */
	public String getInvest() {
		return invest;
	}
	/**
	 * @param invest the invest to set
	 */
	public void setInvest(String invest) {
		this.invest = invest;
	}
	/**
	 * @return the investYear
	 */
	public String getInvestYear() {
		return investYear;
	}
	/**
	 * @param investYear the investYear to set
	 */
	public void setInvestYear(String investYear) {
		this.investYear = investYear;
	}
	/**
	 * @return the otherInvest
	 */
	public String getOtherInvest() {
		return otherInvest;
	}
	/**
	 * @param otherInvest the otherInvest to set
	 */
	public void setOtherInvest(String otherInvest) {
		this.otherInvest = otherInvest;
	}
	/**
	 * @return the otherInvestYear
	 */
	public String getOtherInvestYear() {
		return otherInvestYear;
	}
	/**
	 * @param otherInvestYear the otherInvestYear to set
	 */
	public void setOtherInvestYear(String otherInvestYear) {
		this.otherInvestYear = otherInvestYear;
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
	 * @return the ownerLinkman
	 */
	public String getOwnerLinkman() {
		return ownerLinkman;
	}
	/**
	 * @param ownerLinkman the ownerLinkman to set
	 */
	public void setOwnerLinkman(String ownerLinkman) {
		this.ownerLinkman = ownerLinkman;
	}
	/**
	 * @return the ownerRepresentative
	 */
	public String getOwnerRepresentative() {
		return ownerRepresentative;
	}
	/**
	 * @param ownerRepresentative the ownerRepresentative to set
	 */
	public void setOwnerRepresentative(String ownerRepresentative) {
		this.ownerRepresentative = ownerRepresentative;
	}
	/**
	 * @return the ownerTel
	 */
	public String getOwnerTel() {
		return ownerTel;
	}
	/**
	 * @param ownerTel the ownerTel to set
	 */
	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}
	/**
	 * @return the ownerType
	 */
	public String getOwnerType() {
		return ownerType;
	}
	/**
	 * @param ownerType the ownerType to set
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	/**
	 * @return the personalInvest
	 */
	public String getPersonalInvest() {
		return personalInvest;
	}
	/**
	 * @param personalInvest the personalInvest to set
	 */
	public void setPersonalInvest(String personalInvest) {
		this.personalInvest = personalInvest;
	}
	/**
	 * @return the personalInvestYear
	 */
	public String getPersonalInvestYear() {
		return personalInvestYear;
	}
	/**
	 * @param personalInvestYear the personalInvestYear to set
	 */
	public void setPersonalInvestYear(String personalInvestYear) {
		this.personalInvestYear = personalInvestYear;
	}
	/**
	 * @return the prepare
	 */
	public String getPrepare() {
		return prepare;
	}
	/**
	 * @param prepare the prepare to set
	 */
	public void setPrepare(String prepare) {
		this.prepare = prepare;
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
	 * @return the projectProperty
	 */
	public String getProjectProperty() {
		return projectProperty;
	}
	/**
	 * @param projectProperty the projectProperty to set
	 */
	public void setProjectProperty(String projectProperty) {
		this.projectProperty = projectProperty;
	}
	/**
	 * @return the proposalDate
	 */
	public Date getProposalDate() {
		return proposalDate;
	}
	/**
	 * @param proposalDate the proposalDate to set
	 */
	public void setProposalDate(Date proposalDate) {
		this.proposalDate = proposalDate;
	}
	/**
	 * @return the proposalNumber
	 */
	public String getProposalNumber() {
		return proposalNumber;
	}
	/**
	 * @param proposalNumber the proposalNumber to set
	 */
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	/**
	 * @return the receiveNumber
	 */
	public String getReceiveNumber() {
		return receiveNumber;
	}
	/**
	 * @param receiveNumber the receiveNumber to set
	 */
	public void setReceiveNumber(String receiveNumber) {
		this.receiveNumber = receiveNumber;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}
	/**
	 * @return the verify
	 */
	public String getVerify() {
		return verify;
	}
	/**
	 * @param verify the verify to set
	 */
	public void setVerify(String verify) {
		this.verify = verify;
	}
	/**
	 * @return the receiveTime
	 */
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
}
