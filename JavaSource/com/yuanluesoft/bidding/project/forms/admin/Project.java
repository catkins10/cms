package com.yuanluesoft.bidding.project.forms.admin;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoomSchedule;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgentDraw;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAnswer;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectArchive;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectBidopening;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCompletion;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectDeclare;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectDocument;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectKC;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectLicence;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectMaterial;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectNotice;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPay;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPitchon;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPreapproval;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectProphase;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectSupplement;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectUseFee;
import com.yuanluesoft.bidding.project.signup.model.SignUpTotal;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author yuanlue
 *
 */
public class Project extends WorkflowForm {
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
	private double area; //建筑面积
	private String workflowInstanceId; //工作流实例ID
	private Timestamp created; //创建时间
	private Timestamp registTime; //完成登记时间
	private char halt = '0'; //是否作废
	private String failed; //是否招标失败,失败时为“是”
	private String pledgeBank; //保证金开户行
	private String pledgeAccountName; //保证金帐户名
	private String pledgeAccount; //保证金帐号
	private String isRealNameSignUp; //是否实名报名,是/否
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
	private Set grades; //评分列表
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
	private Set completions; //竣工
	private Set jobholders; //人员需求
	
	//表单属性
	private List askFroms; //提问发起点列表,从项目组成部分模型获取,倒序排
	private String workBeginAm; //上午上班时间
	private String workEndAm; //上午下班时间
	private String workBeginPm; //下午上班时间
	private String workEndPm; //下午下班时间
	
	private String pageName; //当前需要显示的页面名称,如completeCreate
	
	private boolean needBiddingDocumentsStamp; //是否要求用户对招标文件盖章
	private boolean biddingDocumentsStamp; //是否对招标文件盖过章

	private boolean needPitchonStamp; //是否要求用户对中标公示盖章
	private boolean pitchonStamp; //是否对中标公示盖过章
	
	private boolean needNoticeStamp; //是否要求用户对中标通知书盖章
	private boolean noticeStamp; //是否对中标通知书盖过章
	
	private String hasAnswer; //是否有答疑纪要
	private String hasSupplement; //是否有补充通知
	
	private List prophaseFiles; //前期资料列表
	private List archiveFiles; //归档资料列表
	private boolean needFullFiles; //是否必须资料完整才能继续流转
	
	private SignUpTotal signUpTotal; //报名统计
	private String rankingEnterpriseNames; //排名靠前的企业列表
	private String rankingSignUpIds; //排名靠前的报名ID列表
	
	private String transferPassword; //转账密码
	
	//组成部分
	private BiddingProjectProphase prophase = new BiddingProjectProphase(); //前期资料备案
	private BiddingProjectAgentDraw agentDraw = new BiddingProjectAgentDraw(); //代理抽签公告
	private BiddingProjectAgent biddingAgent = new BiddingProjectAgent(); //招标代理配置
	private BiddingRoomSchedule bidopeningRoomSchedule = new BiddingRoomSchedule(); //开标室安排
	private BiddingProjectDocument document = new BiddingProjectDocument(); //招标文件
	private BiddingProjectTender tender = new BiddingProjectTender(); //招标公告
	private BiddingProjectPlan plan = new BiddingProjectPlan(); //招标安排
	private BiddingProjectMaterial material = new BiddingProjectMaterial(); //实质性内容
	private BiddingProjectPreapproval preapproval = new BiddingProjectPreapproval(); //预审公示
	private BiddingProjectBidopening bidopening = new BiddingProjectBidopening(); //开标公示
	private BiddingRoomSchedule evaluatingRoomSchedule = new BiddingRoomSchedule(); //评标室安排
	private BiddingProjectPitchon pitchon = new BiddingProjectPitchon(); //中标公示
	private BiddingProjectSupplement supplement = new BiddingProjectSupplement(); //补充通知
	private BiddingProjectAnswer answer = new BiddingProjectAnswer(); //答疑会议纪要
	private BiddingProjectNotice notice = new BiddingProjectNotice(); //中标通知书
	private BiddingProjectUseFee useFee = new BiddingProjectUseFee(); //场地费
	private BiddingProjectPay pay = new BiddingProjectPay(); //缴费
	private BiddingProjectKC kc = new BiddingProjectKC(); //KC值
	private BiddingProjectArchive archive = new BiddingProjectArchive(); //归档
	private BiddingProjectDeclare declare = new BiddingProjectDeclare(); //工程项目报建
	private BiddingProjectLicence licence = new BiddingProjectLicence(); //施工许可证
	private BiddingProjectCompletion completion = new BiddingProjectCompletion(); //竣工

	/**
	 * 判断是否已经完成保证金的返还
	 * @return
	 */
	public boolean isPledgeReturned() {
		try {
			if(signUps==null || signUps.isEmpty()) {
				return true;
			}
			for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
				BiddingSignUp signUp = (BiddingSignUp)iterator.next();
				if(signUp.getPledgeReturnEnabled()=='1' && signUp.getPledgePaymentTime()!=null && signUp.getPledgeReturnTime()==null) {
					return false;
				}
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
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
	 * @return the archiveFiles
	 */
	public List getArchiveFiles() {
		return archiveFiles;
	}

	/**
	 * @param archiveFiles the archiveFiles to set
	 */
	public void setArchiveFiles(List archiveFiles) {
		this.archiveFiles = archiveFiles;
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
	 * @return the askFroms
	 */
	public List getAskFroms() {
		return askFroms;
	}

	/**
	 * @param askFroms the askFroms to set
	 */
	public void setAskFroms(List askFroms) {
		this.askFroms = askFroms;
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
	 * @return the documents
	 */
	public Set getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(Set documents) {
		this.documents = documents;
	}

	/**
	 * @return the biddingDocumentsStamp
	 */
	public boolean isBiddingDocumentsStamp() {
		return biddingDocumentsStamp;
	}

	/**
	 * @param biddingDocumentsStamp the biddingDocumentsStamp to set
	 */
	public void setBiddingDocumentsStamp(boolean biddingDocumentsStamp) {
		this.biddingDocumentsStamp = biddingDocumentsStamp;
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
	 * @return the needBiddingDocumentsStamp
	 */
	public boolean isNeedBiddingDocumentsStamp() {
		return needBiddingDocumentsStamp;
	}

	/**
	 * @param needBiddingDocumentsStamp the needBiddingDocumentsStamp to set
	 */
	public void setNeedBiddingDocumentsStamp(boolean needBiddingDocumentsStamp) {
		this.needBiddingDocumentsStamp = needBiddingDocumentsStamp;
	}

	/**
	 * @return the needNoticeStamp
	 */
	public boolean isNeedNoticeStamp() {
		return needNoticeStamp;
	}

	/**
	 * @param needNoticeStamp the needNoticeStamp to set
	 */
	public void setNeedNoticeStamp(boolean needNoticeStamp) {
		this.needNoticeStamp = needNoticeStamp;
	}

	/**
	 * @return the needPitchonStamp
	 */
	public boolean isNeedPitchonStamp() {
		return needPitchonStamp;
	}

	/**
	 * @param needPitchonStamp the needPitchonStamp to set
	 */
	public void setNeedPitchonStamp(boolean needPitchonStamp) {
		this.needPitchonStamp = needPitchonStamp;
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
	 * @return the noticeStamp
	 */
	public boolean isNoticeStamp() {
		return noticeStamp;
	}

	/**
	 * @param noticeStamp the noticeStamp to set
	 */
	public void setNoticeStamp(boolean noticeStamp) {
		this.noticeStamp = noticeStamp;
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
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
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
	 * @return the pitchonStamp
	 */
	public boolean isPitchonStamp() {
		return pitchonStamp;
	}

	/**
	 * @param pitchonStamp the pitchonStamp to set
	 */
	public void setPitchonStamp(boolean pitchonStamp) {
		this.pitchonStamp = pitchonStamp;
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
	 * @return the prophaseFiles
	 */
	public List getProphaseFiles() {
		return prophaseFiles;
	}

	/**
	 * @param prophaseFiles the prophaseFiles to set
	 */
	public void setProphaseFiles(List prophaseFiles) {
		this.prophaseFiles = prophaseFiles;
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
	 * @return the signUpTotal
	 */
	public SignUpTotal getSignUpTotal() {
		return signUpTotal;
	}

	/**
	 * @param signUpTotal the signUpTotal to set
	 */
	public void setSignUpTotal(SignUpTotal signUpTotal) {
		this.signUpTotal = signUpTotal;
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
	 * @return the workBeginAm
	 */
	public String getWorkBeginAm() {
		return workBeginAm;
	}

	/**
	 * @param workBeginAm the workBeginAm to set
	 */
	public void setWorkBeginAm(String workBeginAm) {
		this.workBeginAm = workBeginAm;
	}

	/**
	 * @return the workBeginPm
	 */
	public String getWorkBeginPm() {
		return workBeginPm;
	}

	/**
	 * @param workBeginPm the workBeginPm to set
	 */
	public void setWorkBeginPm(String workBeginPm) {
		this.workBeginPm = workBeginPm;
	}

	/**
	 * @return the workEndAm
	 */
	public String getWorkEndAm() {
		return workEndAm;
	}

	/**
	 * @param workEndAm the workEndAm to set
	 */
	public void setWorkEndAm(String workEndAm) {
		this.workEndAm = workEndAm;
	}

	/**
	 * @return the workEndPm
	 */
	public String getWorkEndPm() {
		return workEndPm;
	}

	/**
	 * @param workEndPm the workEndPm to set
	 */
	public void setWorkEndPm(String workEndPm) {
		this.workEndPm = workEndPm;
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
	 * @return the needFullFiles
	 */
	public boolean isNeedFullFiles() {
		return needFullFiles;
	}

	/**
	 * @param needFullFiles the needFullFiles to set
	 */
	public void setNeedFullFiles(boolean needFullFiles) {
		this.needFullFiles = needFullFiles;
	}

	/**
	 * @return the hasAnswer
	 */
	public String getHasAnswer() {
		return hasAnswer;
	}

	/**
	 * @param hasAnswer the hasAnswer to set
	 */
	public void setHasAnswer(String hasAnswer) {
		this.hasAnswer = hasAnswer;
	}

	/**
	 * @return the hasSupplement
	 */
	public String getHasSupplement() {
		return hasSupplement;
	}

	/**
	 * @param hasSupplement the hasSupplement to set
	 */
	public void setHasSupplement(String hasSupplement) {
		this.hasSupplement = hasSupplement;
	}

	/**
	 * @return the area
	 */
	public double getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}

	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
	}

	/**
	 * @return the agentDraw
	 */
	public BiddingProjectAgentDraw getAgentDraw() {
		return agentDraw;
	}

	/**
	 * @param agentDraw the agentDraw to set
	 */
	public void setAgentDraw(BiddingProjectAgentDraw agentDraw) {
		this.agentDraw = agentDraw==null ? new BiddingProjectAgentDraw() : agentDraw;
	}

	/**
	 * @return the answer
	 */
	public BiddingProjectAnswer getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(BiddingProjectAnswer answer) {
		this.answer = answer==null ? new BiddingProjectAnswer() : answer;
	}

	/**
	 * @return the archive
	 */
	public BiddingProjectArchive getArchive() {
		return archive;
	}

	/**
	 * @param archive the archive to set
	 */
	public void setArchive(BiddingProjectArchive archive) {
		this.archive = archive==null ? new BiddingProjectArchive() : archive;
	}

	/**
	 * @return the biddingAgent
	 */
	public BiddingProjectAgent getBiddingAgent() {
		return biddingAgent;
	}

	/**
	 * @param biddingAgent the biddingAgent to set
	 */
	public void setBiddingAgent(BiddingProjectAgent biddingAgent) {
		this.biddingAgent = biddingAgent==null ? new BiddingProjectAgent() : biddingAgent;
	}

	/**
	 * @return the bidopening
	 */
	public BiddingProjectBidopening getBidopening() {
		return bidopening;
	}

	/**
	 * @param bidopening the bidopening to set
	 */
	public void setBidopening(BiddingProjectBidopening bidopening) {
		this.bidopening = bidopening==null ? new BiddingProjectBidopening() : bidopening;
	}

	/**
	 * @return the bidopeningRoomSchedule
	 */
	public BiddingRoomSchedule getBidopeningRoomSchedule() {
		return bidopeningRoomSchedule;
	}

	/**
	 * @param bidopeningRoomSchedule the bidopeningRoomSchedule to set
	 */
	public void setBidopeningRoomSchedule(BiddingRoomSchedule bidopeningRoomSchedule) {
		this.bidopeningRoomSchedule = bidopeningRoomSchedule==null ? new BiddingRoomSchedule() : bidopeningRoomSchedule;
	}

	/**
	 * @return the declare
	 */
	public BiddingProjectDeclare getDeclare() {
		return declare;
	}

	/**
	 * @param declare the declare to set
	 */
	public void setDeclare(BiddingProjectDeclare declare) {
		this.declare = declare==null ? new BiddingProjectDeclare() : declare;
	}

	/**
	 * @return the document
	 */
	public BiddingProjectDocument getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(BiddingProjectDocument document) {
		this.document = document==null ? new BiddingProjectDocument() : document;
	}

	/**
	 * @return the evaluatingRoomSchedule
	 */
	public BiddingRoomSchedule getEvaluatingRoomSchedule() {
		return evaluatingRoomSchedule;
	}

	/**
	 * @param evaluatingRoomSchedule the evaluatingRoomSchedule to set
	 */
	public void setEvaluatingRoomSchedule(BiddingRoomSchedule evaluatingRoomSchedule) {
		this.evaluatingRoomSchedule = evaluatingRoomSchedule==null ? new BiddingRoomSchedule() : evaluatingRoomSchedule;
	}

	/**
	 * @return the kc
	 */
	public BiddingProjectKC getKc() {
		return kc;
	}

	/**
	 * @param kc the kc to set
	 */
	public void setKc(BiddingProjectKC kc) {
		this.kc = kc==null ? new BiddingProjectKC() : kc;
	}

	/**
	 * @return the licence
	 */
	public BiddingProjectLicence getLicence() {
		return licence;
	}

	/**
	 * @param licence the licence to set
	 */
	public void setLicence(BiddingProjectLicence licence) {
		this.licence = licence==null ? new BiddingProjectLicence() : licence;
	}

	/**
	 * @return the material
	 */
	public BiddingProjectMaterial getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(BiddingProjectMaterial material) {
		this.material = material==null ? new BiddingProjectMaterial() : material;
	}

	/**
	 * @return the notice
	 */
	public BiddingProjectNotice getNotice() {
		return notice;
	}

	/**
	 * @param notice the notice to set
	 */
	public void setNotice(BiddingProjectNotice notice) {
		this.notice = notice==null ? new BiddingProjectNotice() : notice;
	}

	/**
	 * @return the pay
	 */
	public BiddingProjectPay getPay() {
		return pay;
	}

	/**
	 * @param pay the pay to set
	 */
	public void setPay(BiddingProjectPay pay) {
		this.pay = pay==null ? new BiddingProjectPay() : pay;
	}

	/**
	 * @return the pitchon
	 */
	public BiddingProjectPitchon getPitchon() {
		return pitchon;
	}

	/**
	 * @param pitchon the pitchon to set
	 */
	public void setPitchon(BiddingProjectPitchon pitchon) {
		this.pitchon = pitchon==null ? new BiddingProjectPitchon() : pitchon;
	}

	/**
	 * @return the plan
	 */
	public BiddingProjectPlan getPlan() {
		return plan;
	}

	/**
	 * @param plan the plan to set
	 */
	public void setPlan(BiddingProjectPlan plan) {
		this.plan = plan==null ? new BiddingProjectPlan() : plan;
	}

	/**
	 * @return the preapproval
	 */
	public BiddingProjectPreapproval getPreapproval() {
		return preapproval;
	}

	/**
	 * @param preapproval the preapproval to set
	 */
	public void setPreapproval(BiddingProjectPreapproval preapproval) {
		this.preapproval = preapproval==null ? new BiddingProjectPreapproval() : preapproval;
	}

	/**
	 * @return the prophase
	 */
	public BiddingProjectProphase getProphase() {
		return prophase;
	}

	/**
	 * @param prophase the prophase to set
	 */
	public void setProphase(BiddingProjectProphase prophase) {
		this.prophase = prophase==null ? new BiddingProjectProphase() : prophase;
	}

	/**
	 * @return the supplement
	 */
	public BiddingProjectSupplement getSupplement() {
		return supplement;
	}

	/**
	 * @param supplement the supplement to set
	 */
	public void setSupplement(BiddingProjectSupplement supplement) {
		this.supplement = supplement==null ? new BiddingProjectSupplement() : supplement;
	}

	/**
	 * @return the tender
	 */
	public BiddingProjectTender getTender() {
		return tender;
	}

	/**
	 * @param tender the tender to set
	 */
	public void setTender(BiddingProjectTender tender) {
		this.tender = tender==null ? new BiddingProjectTender() : tender;
	}

	/**
	 * @return the useFee
	 */
	public BiddingProjectUseFee getUseFee() {
		return useFee;
	}

	/**
	 * @param useFee the useFee to set
	 */
	public void setUseFee(BiddingProjectUseFee useFee) {
		this.useFee = useFee==null ? new BiddingProjectUseFee() : useFee;
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
	 * @return the rankingEnterpriseNames
	 */
	public String getRankingEnterpriseNames() {
		return rankingEnterpriseNames;
	}

	/**
	 * @param rankingEnterpriseNames the rankingEnterpriseNames to set
	 */
	public void setRankingEnterpriseNames(String rankingEnterpriseNames) {
		this.rankingEnterpriseNames = rankingEnterpriseNames;
	}

	/**
	 * @return the rankingSignUpIds
	 */
	public String getRankingSignUpIds() {
		return rankingSignUpIds;
	}

	/**
	 * @param rankingSignUpIds the rankingSignUpIds to set
	 */
	public void setRankingSignUpIds(String rankingSignUpIds) {
		this.rankingSignUpIds = rankingSignUpIds;
	}

	/**
	 * @return the transferPassword
	 */
	public String getTransferPassword() {
		return transferPassword;
	}

	/**
	 * @param transferPassword the transferPassword to set
	 */
	public void setTransferPassword(String transferPassword) {
		this.transferPassword = transferPassword;
	}

	/**
	 * @return the pledgeAccount
	 */
	public String getPledgeAccount() {
		return pledgeAccount;
	}

	/**
	 * @param pledgeAccount the pledgeAccount to set
	 */
	public void setPledgeAccount(String pledgeAccount) {
		this.pledgeAccount = pledgeAccount;
	}

	/**
	 * @return the pledgeAccountName
	 */
	public String getPledgeAccountName() {
		return pledgeAccountName;
	}

	/**
	 * @param pledgeAccountName the pledgeAccountName to set
	 */
	public void setPledgeAccountName(String pledgeAccountName) {
		this.pledgeAccountName = pledgeAccountName;
	}

	/**
	 * @return the pledgeBank
	 */
	public String getPledgeBank() {
		return pledgeBank;
	}

	/**
	 * @param pledgeBank the pledgeBank to set
	 */
	public void setPledgeBank(String pledgeBank) {
		this.pledgeBank = pledgeBank;
	}

	/**
	 * @return the jobholders
	 */
	public Set getJobholders() {
		return jobholders;
	}

	/**
	 * @param jobholders the jobholders to set
	 */
	public void setJobholders(Set jobholders) {
		this.jobholders = jobholders;
	}

	/**
	 * @return the completion
	 */
	public BiddingProjectCompletion getCompletion() {
		return completion;
	}

	/**
	 * @param completion the completion to set
	 */
	public void setCompletion(BiddingProjectCompletion completion) {
		this.completion = completion;
	}

	/**
	 * @return the completions
	 */
	public Set getCompletions() {
		return completions;
	}

	/**
	 * @param completions the completions to set
	 */
	public void setCompletions(Set completions) {
		this.completions = completions;
	}

	/**
	 * @return the isRealNameSignUp
	 */
	public String getIsRealNameSignUp() {
		return isRealNameSignUp;
	}

	/**
	 * @param isRealNameSignUp the isRealNameSignUp to set
	 */
	public void setIsRealNameSignUp(String isRealNameSignUp) {
		this.isRealNameSignUp = isRealNameSignUp;
	}
}