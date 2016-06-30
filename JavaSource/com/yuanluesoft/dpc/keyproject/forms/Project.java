package com.yuanluesoft.dpc.keyproject.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Project extends WorkflowForm {
	private String name; //项目全称（子项目）,重点项目全称（又称为子项目）
	private String parentName; //父项目名称,项目可分父项目和子项目，子项目指具体的项目成，父项目只是项目分类，没有明细的信息。
	private String summary; //项目简介,概要描述项目的地点、规模、内容、总体安排、投资资金等信息。
	private String level; //分级管理（项目级别）,省级重点、市级重点、县级重点
	private String industry; //所属行业,包括交通、能源、农林业、工业园区、工业、社会事业、城建环保、旅游业、商贸服务业
	private String childIndustry; //所属子行业,高速公路、铁路、热电、水利、石油化工、高新技术产业、电子信息技术、机械、传统产业等
	private String status; //项目状态,前期、在建、竣工、其他
	private String classify; //项目类别,储备、在建和续建
	private String category; //项目分类,考核类、跟踪服务类
	private String leader; //项目挂点领导,对本项目关注的领导以及挂点领导名单
	private String otherLeader; //其他项目挂点领导
	private String developmentArea; //所属开发区
	private String address; //建设地点
	private String investmentSubject; //投资主体,包括国有、国有控股与外资合股、国有控股与民营合股、民营、民营控股与国有合资、外资独资、外资控股与国有合资、外资控股与民营合资、其他
	private String managementUnit; //项目管理或责任部门,市经贸委、建设局、交通局、教育局、卫生局、水利局、商贸办、延平区、武夷山市、邵武市、建瓯市、建阳市、顺昌县、浦城县、光泽县、松溪县、政和县
	private String managementUnitResponsible; //管理部门责任人
	private String managementUnitLinkman; //管理部门联络人
	private String managementUnitTel; //管理部门电话
	private String unit; //法人机构或者筹建单位,项目建设法人机构或者负责筹建的单位名称
	private String lawPerson; //法人代表或者负责人
	private String linkman; //联系人
	private String unitTel; //单位电话
	private String unitFax; //单位传真
	private String unitAddress; //单位地址
	private String unitPostcode; //邮政编码
	private String constructionType; //建设性质,新建、扩建
	private int constructionBeginYear; //建设开始年限
	private int constructionEndYear; //建设结束年限
	private Date estimateBeginDate; //预计开工时间
	private Date estimateEndDate; //预计竣工时间
	private Date beginDate; //开工时间
	private Date endDate; //竣工时间
	private double accountableInvest; //项目总投资（责任制）,项目总投资=项目总投资资金（责任制）拼盘中投资列表的总和
	private double invest; //项目总投资,项目总投资=项目总投资资金拼盘中投资列表的总和
	private String investmentRemark; //总投资不一致备注,详细说明总投资不一致的原因
	private String constructionScale; //建设规模及主要建设内容
	private String constructionEffect; //建成投产后增生生产能力,项目建设投产的成果和产能
	private String plan; //总体安排以及各年计划
	private Timestamp created; //登记时间
	private long creatorId; //登记人ID
	private String creator; //登记人
	private char fiveYearPlan = '0'; //是否五年计划项目
	private double investPlan150; //150天力争完成投资
	
	private Set areas; //所属区域,延平区、武夷山市、邵武市、建瓯市、建阳市、顺昌县、浦城县、光泽县、松溪县、政和县
	private Set declares; //申报情况
	private Set officialDocuments; //审批文件
	private Set units; //参建单位
	private Set annualObjectives; //年度目标
	private Set progresses; //形象进度
	private Set accountableInvests; //总投资(责任制)
	private Set invests; //总投资
	private Set investPaids; //资金到位情况
	private Set investCompletes; //投资完成情况
	private Set problems; //问题及措施
	private Set photos; //进展实景
	private Set plans; //参建单位安排
	private Set fiveYearPlans; //五年计划
	private Set stageProgresses; //关键节点安排
	
	//扩展属性
	private int declareYear; //申报的年度
	
	private String area; //所属区域
	
	//简化汇报相关属性
	private int debriefYear; //年度
	private int debriefMonth; //月份
	private char debriefTenDay = '1'; //旬
	private String debriefProgress; //形象进度
	private double debriefInvestPlan; //月计划投资
	private double debriefInvestComplete; //旬完成投资
	private String debriefProblem; //存在的问题
	
	/**
	 * @return the accountableInvest
	 */
	public double getAccountableInvest() {
		return accountableInvest;
	}
	/**
	 * @param accountableInvest the accountableInvest to set
	 */
	public void setAccountableInvest(double accountableInvest) {
		this.accountableInvest = accountableInvest;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the childIndustry
	 */
	public String getChildIndustry() {
		return childIndustry;
	}
	/**
	 * @param childIndustry the childIndustry to set
	 */
	public void setChildIndustry(String childIndustry) {
		this.childIndustry = childIndustry;
	}
	/**
	 * @return the classify
	 */
	public String getClassify() {
		return classify;
	}
	/**
	 * @param classify the classify to set
	 */
	public void setClassify(String classify) {
		this.classify = classify;
	}
	/**
	 * @return the constructionBeginYear
	 */
	public int getConstructionBeginYear() {
		return constructionBeginYear;
	}
	/**
	 * @param constructionBeginYear the constructionBeginYear to set
	 */
	public void setConstructionBeginYear(int constructionBeginYear) {
		this.constructionBeginYear = constructionBeginYear;
	}
	/**
	 * @return the constructionEffect
	 */
	public String getConstructionEffect() {
		return constructionEffect;
	}
	/**
	 * @param constructionEffect the constructionEffect to set
	 */
	public void setConstructionEffect(String constructionEffect) {
		this.constructionEffect = constructionEffect;
	}
	/**
	 * @return the constructionEndYear
	 */
	public int getConstructionEndYear() {
		return constructionEndYear;
	}
	/**
	 * @param constructionEndYear the constructionEndYear to set
	 */
	public void setConstructionEndYear(int constructionEndYear) {
		this.constructionEndYear = constructionEndYear;
	}
	/**
	 * @return the constructionScale
	 */
	public String getConstructionScale() {
		return constructionScale;
	}
	/**
	 * @param constructionScale the constructionScale to set
	 */
	public void setConstructionScale(String constructionScale) {
		this.constructionScale = constructionScale;
	}
	/**
	 * @return the constructionType
	 */
	public String getConstructionType() {
		return constructionType;
	}
	/**
	 * @param constructionType the constructionType to set
	 */
	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
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
	 * @return the estimateBeginDate
	 */
	public Date getEstimateBeginDate() {
		return estimateBeginDate;
	}
	/**
	 * @param estimateBeginDate the estimateBeginDate to set
	 */
	public void setEstimateBeginDate(Date estimateBeginDate) {
		this.estimateBeginDate = estimateBeginDate;
	}
	/**
	 * @return the estimateEndDate
	 */
	public Date getEstimateEndDate() {
		return estimateEndDate;
	}
	/**
	 * @param estimateEndDate the estimateEndDate to set
	 */
	public void setEstimateEndDate(Date estimateEndDate) {
		this.estimateEndDate = estimateEndDate;
	}
	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	/**
	 * @return the invest
	 */
	public double getInvest() {
		return invest;
	}
	/**
	 * @param invest the invest to set
	 */
	public void setInvest(double invest) {
		this.invest = invest;
	}
	/**
	 * @return the investmentRemark
	 */
	public String getInvestmentRemark() {
		return investmentRemark;
	}
	/**
	 * @param investmentRemark the investmentRemark to set
	 */
	public void setInvestmentRemark(String investmentRemark) {
		this.investmentRemark = investmentRemark;
	}
	/**
	 * @return the investmentSubject
	 */
	public String getInvestmentSubject() {
		return investmentSubject;
	}
	/**
	 * @param investmentSubject the investmentSubject to set
	 */
	public void setInvestmentSubject(String investmentSubject) {
		this.investmentSubject = investmentSubject;
	}
	/**
	 * @return the lawPerson
	 */
	public String getLawPerson() {
		return lawPerson;
	}
	/**
	 * @param lawPerson the lawPerson to set
	 */
	public void setLawPerson(String lawPerson) {
		this.lawPerson = lawPerson;
	}
	/**
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the managementUnit
	 */
	public String getManagementUnit() {
		return managementUnit;
	}
	/**
	 * @param managementUnit the managementUnit to set
	 */
	public void setManagementUnit(String managementUnit) {
		this.managementUnit = managementUnit;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the unitAddress
	 */
	public String getUnitAddress() {
		return unitAddress;
	}
	/**
	 * @param unitAddress the unitAddress to set
	 */
	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}
	/**
	 * @return the unitFax
	 */
	public String getUnitFax() {
		return unitFax;
	}
	/**
	 * @param unitFax the unitFax to set
	 */
	public void setUnitFax(String unitFax) {
		this.unitFax = unitFax;
	}
	/**
	 * @return the unitPostcode
	 */
	public String getUnitPostcode() {
		return unitPostcode;
	}
	/**
	 * @param unitPostcode the unitPostcode to set
	 */
	public void setUnitPostcode(String unitPostcode) {
		this.unitPostcode = unitPostcode;
	}
	/**
	 * @return the unitTel
	 */
	public String getUnitTel() {
		return unitTel;
	}
	/**
	 * @param unitTel the unitTel to set
	 */
	public void setUnitTel(String unitTel) {
		this.unitTel = unitTel;
	}
	/**
	 * @return the accountableInvests
	 */
	public Set getAccountableInvests() {
		return accountableInvests;
	}
	/**
	 * @param accountableInvests the accountableInvests to set
	 */
	public void setAccountableInvests(Set accountableInvests) {
		this.accountableInvests = accountableInvests;
	}
	/**
	 * @return the annualObjectives
	 */
	public Set getAnnualObjectives() {
		return annualObjectives;
	}
	/**
	 * @param annualObjectives the annualObjectives to set
	 */
	public void setAnnualObjectives(Set annualObjectives) {
		this.annualObjectives = annualObjectives;
	}
	/**
	 * @return the investCompletes
	 */
	public Set getInvestCompletes() {
		return investCompletes;
	}
	/**
	 * @param investCompletes the investCompletes to set
	 */
	public void setInvestCompletes(Set investCompletes) {
		this.investCompletes = investCompletes;
	}
	/**
	 * @return the investPaids
	 */
	public Set getInvestPaids() {
		return investPaids;
	}
	/**
	 * @param investPaids the investPaids to set
	 */
	public void setInvestPaids(Set investPaids) {
		this.investPaids = investPaids;
	}
	/**
	 * @return the invests
	 */
	public Set getInvests() {
		return invests;
	}
	/**
	 * @param invests the invests to set
	 */
	public void setInvests(Set invests) {
		this.invests = invests;
	}
	/**
	 * @return the officialDocuments
	 */
	public Set getOfficialDocuments() {
		return officialDocuments;
	}
	/**
	 * @param officialDocuments the officialDocuments to set
	 */
	public void setOfficialDocuments(Set officialDocuments) {
		this.officialDocuments = officialDocuments;
	}
	/**
	 * @return the photos
	 */
	public Set getPhotos() {
		return photos;
	}
	/**
	 * @param photos the photos to set
	 */
	public void setPhotos(Set photos) {
		this.photos = photos;
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
	 * @return the problems
	 */
	public Set getProblems() {
		return problems;
	}
	/**
	 * @param problems the problems to set
	 */
	public void setProblems(Set problems) {
		this.problems = problems;
	}
	/**
	 * @return the progresses
	 */
	public Set getProgresses() {
		return progresses;
	}
	/**
	 * @param progresses the progresses to set
	 */
	public void setProgresses(Set progresses) {
		this.progresses = progresses;
	}
	/**
	 * @return the units
	 */
	public Set getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(Set units) {
		this.units = units;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
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
	 * @return the declareYear
	 */
	public int getDeclareYear() {
		return declareYear;
	}
	/**
	 * @param declareYear the declareYear to set
	 */
	public void setDeclareYear(int declareYear) {
		this.declareYear = declareYear;
	}
	/**
	 * @return the areas
	 */
	public Set getAreas() {
		return areas;
	}
	/**
	 * @param areas the areas to set
	 */
	public void setAreas(Set areas) {
		this.areas = areas;
	}
	/**
	 * @return the fiveYearPlan
	 */
	public char getFiveYearPlan() {
		return fiveYearPlan;
	}
	/**
	 * @param fiveYearPlan the fiveYearPlan to set
	 */
	public void setFiveYearPlan(char fiveYearPlan) {
		this.fiveYearPlan = fiveYearPlan;
	}
	/**
	 * @return the fiveYearPlans
	 */
	public Set getFiveYearPlans() {
		return fiveYearPlans;
	}
	/**
	 * @param fiveYearPlans the fiveYearPlans to set
	 */
	public void setFiveYearPlans(Set fiveYearPlans) {
		this.fiveYearPlans = fiveYearPlans;
	}
	/**
	 * @return the investPlan150
	 */
	public double getInvestPlan150() {
		return investPlan150;
	}
	/**
	 * @param investPlan150 the investPlan150 to set
	 */
	public void setInvestPlan150(double investPlan150) {
		this.investPlan150 = investPlan150;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the managementUnitLinkman
	 */
	public String getManagementUnitLinkman() {
		return managementUnitLinkman;
	}
	/**
	 * @param managementUnitLinkman the managementUnitLinkman to set
	 */
	public void setManagementUnitLinkman(String managementUnitLinkman) {
		this.managementUnitLinkman = managementUnitLinkman;
	}
	/**
	 * @return the managementUnitResponsible
	 */
	public String getManagementUnitResponsible() {
		return managementUnitResponsible;
	}
	/**
	 * @param managementUnitResponsible the managementUnitResponsible to set
	 */
	public void setManagementUnitResponsible(String managementUnitResponsible) {
		this.managementUnitResponsible = managementUnitResponsible;
	}
	/**
	 * @return the managementUnitTel
	 */
	public String getManagementUnitTel() {
		return managementUnitTel;
	}
	/**
	 * @param managementUnitTel the managementUnitTel to set
	 */
	public void setManagementUnitTel(String managementUnitTel) {
		this.managementUnitTel = managementUnitTel;
	}
	/**
	 * @return the developmentArea
	 */
	public String getDevelopmentArea() {
		return developmentArea;
	}
	/**
	 * @param developmentArea the developmentArea to set
	 */
	public void setDevelopmentArea(String developmentArea) {
		this.developmentArea = developmentArea;
	}
	/**
	 * @return the otherLeader
	 */
	public String getOtherLeader() {
		return otherLeader;
	}
	/**
	 * @param otherLeader the otherLeader to set
	 */
	public void setOtherLeader(String otherLeader) {
		this.otherLeader = otherLeader;
	}
	/**
	 * @return the stageProgresses
	 */
	public Set getStageProgresses() {
		return stageProgresses;
	}
	/**
	 * @param stageProgresses the stageProgresses to set
	 */
	public void setStageProgresses(Set stageProgresses) {
		this.stageProgresses = stageProgresses;
	}
	/**
	 * @return the debriefInvestComplete
	 */
	public double getDebriefInvestComplete() {
		return debriefInvestComplete;
	}
	/**
	 * @param debriefInvestComplete the debriefInvestComplete to set
	 */
	public void setDebriefInvestComplete(double debriefInvestComplete) {
		this.debriefInvestComplete = debriefInvestComplete;
	}
	/**
	 * @return the debriefMonth
	 */
	public int getDebriefMonth() {
		return debriefMonth;
	}
	/**
	 * @param debriefMonth the debriefMonth to set
	 */
	public void setDebriefMonth(int debriefMonth) {
		this.debriefMonth = debriefMonth;
	}
	/**
	 * @return the debriefProblem
	 */
	public String getDebriefProblem() {
		return debriefProblem;
	}
	/**
	 * @param debriefProblem the debriefProblem to set
	 */
	public void setDebriefProblem(String debriefProblem) {
		this.debriefProblem = debriefProblem;
	}
	/**
	 * @return the debriefProgress
	 */
	public String getDebriefProgress() {
		return debriefProgress;
	}
	/**
	 * @param debriefProgress the debriefProgress to set
	 */
	public void setDebriefProgress(String debriefProgress) {
		this.debriefProgress = debriefProgress;
	}
	/**
	 * @return the debriefTenDay
	 */
	public char getDebriefTenDay() {
		return debriefTenDay;
	}
	/**
	 * @param debriefTenDay the debriefTenDay to set
	 */
	public void setDebriefTenDay(char debriefTenDay) {
		this.debriefTenDay = debriefTenDay;
	}
	/**
	 * @return the debriefYear
	 */
	public int getDebriefYear() {
		return debriefYear;
	}
	/**
	 * @param debriefYear the debriefYear to set
	 */
	public void setDebriefYear(int debriefYear) {
		this.debriefYear = debriefYear;
	}
	/**
	 * @return the debriefInvestPlan
	 */
	public double getDebriefInvestPlan() {
		return debriefInvestPlan;
	}
	/**
	 * @param debriefInvestPlan the debriefInvestPlan to set
	 */
	public void setDebriefInvestPlan(double debriefInvestPlan) {
		this.debriefInvestPlan = debriefInvestPlan;
	}
}