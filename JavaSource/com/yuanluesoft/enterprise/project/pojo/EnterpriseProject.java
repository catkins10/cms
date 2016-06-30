package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 项目管理:项目(enterprise_project)
 * @author linchuan
 *
 */
public class EnterpriseProject extends WorkflowData {
	private String name; //工程名称
	private String overview; //工程概况
	private String bridgeScale; //桥梁规模,特大、大、中、小
	private double bridgeWidth; //桥宽
	private double bridgeLength; //桥长
	private String tunnelScale; //隧道规模,特大、大、中、小
	private double tunnelWidth; //隧道宽
	private double tunnelLength; //隧道长
	private String highwayLevel; //公路等级,一级、二级、三级
	private double highwaySpeed; //公路时速
	private double highwayMileage; //公路里程
	private String scale; //项目规模
	private String owner; //业主单位,从客户管理中提取客户信息
	private String city; //所在地区
	private String type; //项目类别,内部、外部、转包、外包
	private Date beginDate; //工作开始时间
	private Date endDate; //工作结束时间
	private String sn; //文件编号
	private String constructionUnit; //施工单位
	private String surveillanceUnit; //监理单位
	private String projectLeader; //项目负责人
	private Date completionDate; //项目建成时间
	private char isBidding = '0'; //是否为招标项目
	private String biddingNumber; //招标编号,从招投标管理中提取招标编号
	private String projectStage; //项目阶段,工可、初步设计、施工图设计等
	private String projectProgress; //项目进展,由项目组负责人填写
	private char iso = '0'; //是否进行ISO贯标,是或者否，总工选择该项目是否进行ISO管理
	private Timestamp created; //登记时间
	private long creatorId; //登记人ID
	private String creator; //登记人
	private char isContract = '0'; //是否合同审批
	private String remark; //备注
	
	private Set contracts; //合同
	private Set collects; //收款
	private Set paies; //付款
	private Set teams; //项目组列表
	
	//流程运转参数
	private String designCompleted; //是否已完成全部设计
	
	/**
	 * 获取当前设计阶段的设计质量
	 * @return
	 */
	public String getDesignQuality() {
		if(teams==null || teams.isEmpty()) {
			return null;
		}
		for(Iterator iterator = teams.iterator(); iterator.hasNext();) {
			EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
			if(team.getDesignQuality()!=null && !team.getDesignQuality().equals("")) {
				return team.getDesignQuality();
			}
		}
		return null;
	}
	
	/**
	 * 是否进行ISO贯标
	 * @return
	 */
	public String getIsoUsed() {
		return iso=='1' ? "是" : "否";
	}
	
	/**
	 * 获取设计阶段
	 * @return
	 */
	public String getDesignStage() {
		if(teams==null || teams.isEmpty()) {
			return null;
		}
		for(Iterator iterator = teams.iterator(); iterator.hasNext();) {
			EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
			if(!iterator.hasNext()) {
				return team.getStage();
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
	 * @return the biddingNumber
	 */
	public String getBiddingNumber() {
		return biddingNumber;
	}
	/**
	 * @param biddingNumber the biddingNumber to set
	 */
	public void setBiddingNumber(String biddingNumber) {
		this.biddingNumber = biddingNumber;
	}
	/**
	 * @return the bridgeLength
	 */
	public double getBridgeLength() {
		return bridgeLength;
	}
	/**
	 * @param bridgeLength the bridgeLength to set
	 */
	public void setBridgeLength(double bridgeLength) {
		this.bridgeLength = bridgeLength;
	}
	/**
	 * @return the bridgeScale
	 */
	public String getBridgeScale() {
		return bridgeScale;
	}
	/**
	 * @param bridgeScale the bridgeScale to set
	 */
	public void setBridgeScale(String bridgeScale) {
		this.bridgeScale = bridgeScale;
	}
	/**
	 * @return the bridgeWidth
	 */
	public double getBridgeWidth() {
		return bridgeWidth;
	}
	/**
	 * @param bridgeWidth the bridgeWidth to set
	 */
	public void setBridgeWidth(double bridgeWidth) {
		this.bridgeWidth = bridgeWidth;
	}
	/**
	 * @return the collects
	 */
	public Set getCollects() {
		return collects;
	}
	/**
	 * @param collects the collects to set
	 */
	public void setCollects(Set collects) {
		this.collects = collects;
	}
	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}
	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	/**
	 * @return the constructionUnit
	 */
	public String getConstructionUnit() {
		return constructionUnit;
	}
	/**
	 * @param constructionUnit the constructionUnit to set
	 */
	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}
	/**
	 * @return the contracts
	 */
	public Set getContracts() {
		return contracts;
	}
	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(Set contracts) {
		this.contracts = contracts;
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
	 * @return the highwayLevel
	 */
	public String getHighwayLevel() {
		return highwayLevel;
	}
	/**
	 * @param highwayLevel the highwayLevel to set
	 */
	public void setHighwayLevel(String highwayLevel) {
		this.highwayLevel = highwayLevel;
	}
	/**
	 * @return the highwayMileage
	 */
	public double getHighwayMileage() {
		return highwayMileage;
	}
	/**
	 * @param highwayMileage the highwayMileage to set
	 */
	public void setHighwayMileage(double highwayMileage) {
		this.highwayMileage = highwayMileage;
	}
	/**
	 * @return the highwaySpeed
	 */
	public double getHighwaySpeed() {
		return highwaySpeed;
	}
	/**
	 * @param highwaySpeed the highwaySpeed to set
	 */
	public void setHighwaySpeed(double highwaySpeed) {
		this.highwaySpeed = highwaySpeed;
	}
	/**
	 * @return the isBidding
	 */
	public char getIsBidding() {
		return isBidding;
	}
	/**
	 * @param isBidding the isBidding to set
	 */
	public void setIsBidding(char isBidding) {
		this.isBidding = isBidding;
	}
	/**
	 * @return the iso
	 */
	public char getIso() {
		return iso;
	}
	/**
	 * @param iso the iso to set
	 */
	public void setIso(char iso) {
		this.iso = iso;
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
	 * @return the overview
	 */
	public String getOverview() {
		return overview;
	}
	/**
	 * @param overview the overview to set
	 */
	public void setOverview(String overview) {
		this.overview = overview;
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
	 * @return the paies
	 */
	public Set getPaies() {
		return paies;
	}
	/**
	 * @param paies the paies to set
	 */
	public void setPaies(Set paies) {
		this.paies = paies;
	}
	/**
	 * @return the projectLeader
	 */
	public String getProjectLeader() {
		return projectLeader;
	}
	/**
	 * @param projectLeader the projectLeader to set
	 */
	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}
	/**
	 * @return the projectProgress
	 */
	public String getProjectProgress() {
		return projectProgress;
	}
	/**
	 * @param projectProgress the projectProgress to set
	 */
	public void setProjectProgress(String projectProgress) {
		this.projectProgress = projectProgress;
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
	 * @return the surveillanceUnit
	 */
	public String getSurveillanceUnit() {
		return surveillanceUnit;
	}
	/**
	 * @param surveillanceUnit the surveillanceUnit to set
	 */
	public void setSurveillanceUnit(String surveillanceUnit) {
		this.surveillanceUnit = surveillanceUnit;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the teams
	 */
	public Set getTeams() {
		return teams;
	}

	/**
	 * @param teams the teams to set
	 */
	public void setTeams(Set teams) {
		this.teams = teams;
	}

	/**
	 * @return the projectStage
	 */
	public String getProjectStage() {
		return projectStage;
	}

	/**
	 * @param projectStage the projectStage to set
	 */
	public void setProjectStage(String projectStage) {
		this.projectStage = projectStage;
	}

	/**
	 * @return the tunnelLength
	 */
	public double getTunnelLength() {
		return tunnelLength;
	}

	/**
	 * @param tunnelLength the tunnelLength to set
	 */
	public void setTunnelLength(double tunnelLength) {
		this.tunnelLength = tunnelLength;
	}

	/**
	 * @return the tunnelScale
	 */
	public String getTunnelScale() {
		return tunnelScale;
	}

	/**
	 * @param tunnelScale the tunnelScale to set
	 */
	public void setTunnelScale(String tunnelScale) {
		this.tunnelScale = tunnelScale;
	}

	/**
	 * @return the tunnelWidth
	 */
	public double getTunnelWidth() {
		return tunnelWidth;
	}

	/**
	 * @param tunnelWidth the tunnelWidth to set
	 */
	public void setTunnelWidth(double tunnelWidth) {
		this.tunnelWidth = tunnelWidth;
	}

	/**
	 * @return the designCompleted
	 */
	public String getDesignCompleted() {
		return designCompleted;
	}

	/**
	 * @param designCompleted the designCompleted to set
	 */
	public void setDesignCompleted(String designCompleted) {
		this.designCompleted = designCompleted;
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
	 * @return the isContract
	 */
	public char getIsContract() {
		return isContract;
	}

	/**
	 * @param isContract the isContract to set
	 */
	public void setIsContract(char isContract) {
		this.isContract = isContract;
	}

	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
}
