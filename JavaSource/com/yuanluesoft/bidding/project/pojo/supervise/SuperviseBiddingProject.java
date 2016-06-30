package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 工程项目(bidding_project)
 * @author linchuan
 *
 */
public class SuperviseBiddingProject extends WorkflowData {
	private String projectName; //工程名称
	private String projectCategory; //工程类别,房建工程/装修工程/监理工程/市政工程/专业工程/设计招标
	private String projectProcedure; //招标内容,施工/设计/监理等
	private String city; //所属地区,福州/福清/长乐
	private String projectNumber; //项目编号,榕市建安招2009001
	private String agentEnable; //是否委托招标代理,是/否
	private String biddingMode; //招标方式,公开招标/邀请招标
	private String approvalMode; //资格审查方式,资格后审/资格预审
	private String agentMode; //代理产生方式,随机抽签/直接指定
	private long ownerId; //建设单位ID,自行招标时必须是系统注册企业
	private String owner; //建设单位
	private String ownerType; //建设单位性质,全民
	private String ownerRepresentative; //建设单位法人代表
	private String ownerLinkman; //建设单位联系人
	private String ownerLinkmanIdCard; //建设单位联系人身份证
	private String ownerTel; //建设单位联系电话
	private String ownerFax; //建设单位传真
	private String ownerMail; //建设单位电子邮件
	private String projectAddress; //建设地点
	private String scale; //建设规模
	private String workflowInstanceId; //工作流实例ID
	private Timestamp created; //创建时间
	private Timestamp registTime; //完成登记时间
	private String remark; //备注

	private Set prophases; //前期资料备案
	private Set askQuestions; //提问列表
	private Set documents; //招标文件
	private Set kcs; //KC值抽签公示
	private Set agentDraws; //代理抽签公示
	private Set biddingAgents; //中选代理
	private Set materials; //实质性内容
	private Set tenders; //招标公告
	private Set plans; //时间安排
	private Set preapprovals; //预审公示
	private Set bidopenings; //开标公示
	private Set grades; //开标评分列表
	private Set pitchons; //中标公示
	private Set pitchouts; //不合格投标人
	private Set notices; //中标通知书
	private Set invites; //被邀请企业
	private Set useFees; //场地费
	private Set pays; //缴费
	private Set supplements; //补充通知
	private Set answers; //答疑会议纪要
	private Set signUps; //报名
	private Set bidopeningRoomSchedules; //开标室安排
	private Set evaluatingRoomSchedules; //评标室安排
	private Set archives; //归档
	private Set declares; //项目报建
	private Set licences; //施工许可证
	
	//流转时使用的属性
	private String failed; //是否招标失败,失败时为“是”
	private String hasAnswer; //是否有答疑
	private String hasSupplement; //是否有补充通知

	/**
	 * @return the agentDraws
	 */
	public Set getAgentDraws() {
		return agentDraws;
	}

	/**
	 * @param agentDraws the agentDraws to set
	 */
	public void setAgentDraws(Set agentDraws) {
		this.agentDraws = agentDraws;
	}

	/**
	 * @return the agentEnable
	 */
	public String getAgentEnable() {
		return agentEnable;
	}

	/**
	 * @param agentEnable the agentEnable to set
	 */
	public void setAgentEnable(String agentEnable) {
		this.agentEnable = agentEnable;
	}

	/**
	 * @return the agentMode
	 */
	public String getAgentMode() {
		return agentMode;
	}

	/**
	 * @param agentMode the agentMode to set
	 */
	public void setAgentMode(String agentMode) {
		this.agentMode = agentMode;
	}

	/**
	 * @return the answers
	 */
	public Set getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(Set answers) {
		this.answers = answers;
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
	 * @return the archives
	 */
	public Set getArchives() {
		return archives;
	}

	/**
	 * @param archives the archives to set
	 */
	public void setArchives(Set archives) {
		this.archives = archives;
	}

	/**
	 * @return the askQuestions
	 */
	public Set getAskQuestions() {
		return askQuestions;
	}

	/**
	 * @param askQuestions the askQuestions to set
	 */
	public void setAskQuestions(Set askQuestions) {
		this.askQuestions = askQuestions;
	}

	/**
	 * @return the biddingAgents
	 */
	public Set getBiddingAgents() {
		return biddingAgents;
	}

	/**
	 * @param biddingAgents the biddingAgents to set
	 */
	public void setBiddingAgents(Set biddingAgents) {
		this.biddingAgents = biddingAgents;
	}

	/**
	 * @return the biddingDocuments
	 */
	public Set getDocuments() {
		return documents;
	}

	/**
	 * @param biddingDocuments the biddingDocuments to set
	 */
	public void setDocuments(Set biddingDocuments) {
		this.documents = biddingDocuments;
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
	 * @return the bidopeningRoomSchedules
	 */
	public Set getBidopeningRoomSchedules() {
		return bidopeningRoomSchedules;
	}

	/**
	 * @param bidopeningRoomSchedules the bidopeningRoomSchedules to set
	 */
	public void setBidopeningRoomSchedules(Set bidopeningRoomSchedules) {
		this.bidopeningRoomSchedules = bidopeningRoomSchedules;
	}

	/**
	 * @return the bidopenings
	 */
	public Set getBidopenings() {
		return bidopenings;
	}

	/**
	 * @param bidopenings the bidopenings to set
	 */
	public void setBidopenings(Set bidopenings) {
		this.bidopenings = bidopenings;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * @return the declares
	 */
	public Set getDeclares() {
		return declares;
	}

	/**
	 * @param declares the declares to set
	 */
	public void setDeclares(Set declares) {
		this.declares = declares;
	}

	/**
	 * @return the evaluatingRoomSchedules
	 */
	public Set getEvaluatingRoomSchedules() {
		return evaluatingRoomSchedules;
	}

	/**
	 * @param evaluatingRoomSchedules the evaluatingRoomSchedules to set
	 */
	public void setEvaluatingRoomSchedules(Set evaluatingRoomSchedules) {
		this.evaluatingRoomSchedules = evaluatingRoomSchedules;
	}

	/**
	 * @return the grades
	 */
	public Set getGrades() {
		return grades;
	}

	/**
	 * @param grades the grades to set
	 */
	public void setGrades(Set grades) {
		this.grades = grades;
	}

	/**
	 * @return the invites
	 */
	public Set getInvites() {
		return invites;
	}

	/**
	 * @param invites the invites to set
	 */
	public void setInvites(Set invites) {
		this.invites = invites;
	}

	/**
	 * @return the kcs
	 */
	public Set getKcs() {
		return kcs;
	}

	/**
	 * @param kcs the kcs to set
	 */
	public void setKcs(Set kcs) {
		this.kcs = kcs;
	}

	/**
	 * @return the licences
	 */
	public Set getLicences() {
		return licences;
	}

	/**
	 * @param licences the licences to set
	 */
	public void setLicences(Set licences) {
		this.licences = licences;
	}

	/**
	 * @return the materials
	 */
	public Set getMaterials() {
		return materials;
	}

	/**
	 * @param materials the materials to set
	 */
	public void setMaterials(Set materials) {
		this.materials = materials;
	}

	/**
	 * @return the notices
	 */
	public Set getNotices() {
		return notices;
	}

	/**
	 * @param notices the notices to set
	 */
	public void setNotices(Set notices) {
		this.notices = notices;
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
	 * @return the ownerFax
	 */
	public String getOwnerFax() {
		return ownerFax;
	}

	/**
	 * @param ownerFax the ownerFax to set
	 */
	public void setOwnerFax(String ownerFax) {
		this.ownerFax = ownerFax;
	}

	/**
	 * @return the ownerId
	 */
	public long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
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
	 * @return the ownerLinkmanIdCard
	 */
	public String getOwnerLinkmanIdCard() {
		return ownerLinkmanIdCard;
	}

	/**
	 * @param ownerLinkmanIdCard the ownerLinkmanIdCard to set
	 */
	public void setOwnerLinkmanIdCard(String ownerLinkmanIdCard) {
		this.ownerLinkmanIdCard = ownerLinkmanIdCard;
	}

	/**
	 * @return the ownerMail
	 */
	public String getOwnerMail() {
		return ownerMail;
	}

	/**
	 * @param ownerMail the ownerMail to set
	 */
	public void setOwnerMail(String ownerMail) {
		this.ownerMail = ownerMail;
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
	 * @return the pays
	 */
	public Set getPays() {
		return pays;
	}

	/**
	 * @param pays the pays to set
	 */
	public void setPays(Set pays) {
		this.pays = pays;
	}

	/**
	 * @return the pitchons
	 */
	public Set getPitchons() {
		return pitchons;
	}

	/**
	 * @param pitchons the pitchons to set
	 */
	public void setPitchons(Set pitchons) {
		this.pitchons = pitchons;
	}

	/**
	 * @return the pitchouts
	 */
	public Set getPitchouts() {
		return pitchouts;
	}

	/**
	 * @param pitchouts the pitchouts to set
	 */
	public void setPitchouts(Set pitchouts) {
		this.pitchouts = pitchouts;
	}

	/**
	 * @return the plans
	 */
	public Set getPlans() {
		return plans;
	}

	/**
	 * @param plans the plans to set
	 */
	public void setPlans(Set plans) {
		this.plans = plans;
	}

	/**
	 * @return the preapprovals
	 */
	public Set getPreapprovals() {
		return preapprovals;
	}

	/**
	 * @param preapprovals the preapprovals to set
	 */
	public void setPreapprovals(Set preapprovals) {
		this.preapprovals = preapprovals;
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
	 * @return the projectCategory
	 */
	public String getProjectCategory() {
		return projectCategory;
	}

	/**
	 * @param projectCategory the projectCategory to set
	 */
	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	 * @return the prophases
	 */
	public Set getProphases() {
		return prophases;
	}

	/**
	 * @param prophases the prophases to set
	 */
	public void setProphases(Set prophases) {
		this.prophases = prophases;
	}

	/**
	 * @return the registTime
	 */
	public Timestamp getRegistTime() {
		return registTime;
	}

	/**
	 * @param registTime the registTime to set
	 */
	public void setRegistTime(Timestamp registTime) {
		this.registTime = registTime;
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
	 * @return the signUps
	 */
	public Set getSignUps() {
		return signUps;
	}

	/**
	 * @param signUps the signUps to set
	 */
	public void setSignUps(Set signUps) {
		this.signUps = signUps;
	}

	/**
	 * @return the supplements
	 */
	public Set getSupplements() {
		return supplements;
	}

	/**
	 * @param supplements the supplements to set
	 */
	public void setSupplements(Set supplements) {
		this.supplements = supplements;
	}

	/**
	 * @return the tenders
	 */
	public Set getTenders() {
		return tenders;
	}

	/**
	 * @param tenders the tenders to set
	 */
	public void setTenders(Set tenders) {
		this.tenders = tenders;
	}

	/**
	 * @return the workflowInstanceId
	 */
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	/**
	 * @param workflowInstanceId the workflowInstanceId to set
	 */
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
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
	 * @return the projectProcedure
	 */
	public String getProjectProcedure() {
		return projectProcedure;
	}

	/**
	 * @param projectProcedure the projectProcedure to set
	 */
	public void setProjectProcedure(String projectProcedure) {
		this.projectProcedure = projectProcedure;
	}

	/**
	 * @return the useFees
	 */
	public Set getUseFees() {
		return useFees;
	}

	/**
	 * @param useFees the useFees to set
	 */
	public void setUseFees(Set useFees) {
		this.useFees = useFees;
	}

	/**
	 * @return the failed
	 */
	public String getFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(String failed) {
		this.failed = failed;
	}

	/**
	 * @param hasAnswer the hasAnswer to set
	 */
	public void setHasAnswer(String hasAnswer) {
		this.hasAnswer = hasAnswer;
	}

	/**
	 * @param hasSupplement the hasSupplement to set
	 */
	public void setHasSupplement(String hasSupplement) {
		this.hasSupplement = hasSupplement;
	}

	/**
	 * @return the hasAnswer
	 */
	public String getHasAnswer() {
		return hasAnswer;
	}

	/**
	 * @return the hasSupplement
	 */
	public String getHasSupplement() {
		return hasSupplement;
	}
	
}