package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 项目管理:项目组(enterprise_project_team)
 * @author linchuan
 *
 */
public class EnterpriseProjectTeam extends Record {
	private long projectId; //项目ID
	private String stage; //项目阶段,工可、初步设计、施工图设计等
	private String workContent; //工作内容
	private Date expectingDate; //计划完成时间
	private Date completionDate; //实际完成时间,设计完成情况日期（设计人员填写）
	private String completionDescription; //实际完成情况
	private String designQuality; //设计质量,总工填写，分为优秀、良好、合格、不合格、原则性错误、技术性错误、一般性错误等（总工）
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private String remark; //备注
	
	private EnterpriseProject project; //项目
	private Set members; //项目组成员列表
	private Set plans; //项目计划列表
	private Set qualities; //设计完成情况列表
	
	//扩展属性
	private String projectTeamManagerIds; //项目经理ID列表
	private String projectTeamManagerNames; //项目经理姓名列表
	private String projectTeamMemberIds; //项目组成员ID列表
	private String projectTeamMemberNames; //项目组成员姓名列表
	
	private boolean isManager; //是否项目组负责人
	private boolean isMember; //是否项目组成员
	private String projectName; //项目名称
	
	private List workReports; //项目工作报告列表
	private List assesses; //项目绩效考核列表
	
	/**
	 * 转换项目组成员为编程的办理人列表
	 * @param programmingParticipantId
	 * @return
	 */
	public List listProgrammingParticipants(String programmingParticipantId) {
		List participants = new ArrayList();
		for(Iterator iterator = members.iterator(); iterator.hasNext();) {
			EnterpriseProjectTeamMember member = (EnterpriseProjectTeamMember)iterator.next();
			if("projectTeamManager".equals(programmingParticipantId)) { //项目组负责人
				if(member.getIsManager()!='1') {
					continue;
				}
			}
			else if("projectTeamNormalMember".equals(programmingParticipantId)) { //项目组成员（不含负责人）
				if(member.getIsManager()=='1') {
					continue;
				}
			}
			participants.add(new com.yuanluesoft.jeaf.base.model.user.Person(member.getMemberId() + "", member.getMemberName()));
		}
		return participants;
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
	 * @return the completionDescription
	 */
	public String getCompletionDescription() {
		return completionDescription;
	}
	/**
	 * @param completionDescription the completionDescription to set
	 */
	public void setCompletionDescription(String completionDescription) {
		this.completionDescription = completionDescription;
	}
	/**
	 * @return the designQuality
	 */
	public String getDesignQuality() {
		return designQuality;
	}
	/**
	 * @param designQuality the designQuality to set
	 */
	public void setDesignQuality(String designQuality) {
		this.designQuality = designQuality;
	}
	/**
	 * @return the expectingDate
	 */
	public Date getExpectingDate() {
		return expectingDate;
	}
	/**
	 * @param expectingDate the expectingDate to set
	 */
	public void setExpectingDate(Date expectingDate) {
		this.expectingDate = expectingDate;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
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
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	/**
	 * @return the workContent
	 */
	public String getWorkContent() {
		return workContent;
	}
	/**
	 * @param workContent the workContent to set
	 */
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
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
	 * @return the members
	 */
	public Set getMembers() {
		return members;
	}
	/**
	 * @param members the members to set
	 */
	public void setMembers(Set members) {
		this.members = members;
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
	 * @return the qualities
	 */
	public Set getQualities() {
		return qualities;
	}
	/**
	 * @param qualities the qualities to set
	 */
	public void setQualities(Set qualities) {
		this.qualities = qualities;
	}
	/**
	 * @return the projectTeamManagerIds
	 */
	public String getProjectTeamManagerIds() {
		if(projectTeamManagerIds==null) {
			projectTeamManagerIds = ListUtils.join(ListUtils.getSubListByProperty(members, "isManager", new Character('1')), "memberId", ",", false);
		}
		return projectTeamManagerIds;
	}
	/**
	 * @param projectTeamManagerIds the projectTeamManagerIds to set
	 */
	public void setProjectTeamManagerIds(String projectTeamManagerIds) {
		this.projectTeamManagerIds = projectTeamManagerIds;
	}
	/**
	 * @return the projectTeamManagerNames
	 */
	public String getProjectTeamManagerNames() {
		if(projectTeamManagerNames==null) {
			projectTeamManagerNames = ListUtils.join(ListUtils.getSubListByProperty(members, "isManager", new Character('1')), "memberName", ",", false);
		}
		return projectTeamManagerNames;
	}
	/**
	 * @param projectTeamManagerNames the projectTeamManagerNames to set
	 */
	public void setProjectTeamManagerNames(String projectTeamManagerNames) {
		this.projectTeamManagerNames = projectTeamManagerNames;
	}
	/**
	 * @return the projectTeamMemberIds
	 */
	public String getProjectTeamMemberIds() {
		if(projectTeamMemberIds==null) {
			projectTeamMemberIds = ListUtils.join(ListUtils.getSubListByProperty(members, "isManager", new Character('0')), "memberId", ",", false);
		}
		return projectTeamMemberIds;
	}
	/**
	 * @param projectTeamMemberIds the projectTeamMemberIds to set
	 */
	public void setProjectTeamMemberIds(String projectTeamMemberIds) {
		this.projectTeamMemberIds = projectTeamMemberIds;
	}
	/**
	 * @return the projectTeamMemberNames
	 */
	public String getProjectTeamMemberNames() {
		if(projectTeamMemberNames==null) {
			projectTeamMemberNames = ListUtils.join(ListUtils.getSubListByProperty(members, "isManager", new Character('0')), "memberName", ",", false);
		}
		return projectTeamMemberNames;
	}
	/**
	 * @param projectTeamMemberNames the projectTeamMemberNames to set
	 */
	public void setProjectTeamMemberNames(String projectTeamMemberNames) {
		this.projectTeamMemberNames = projectTeamMemberNames;
	}
	/**
	 * @return the isManager
	 */
	public boolean isManager() {
		return isManager;
	}
	/**
	 * @param isManager the isManager to set
	 */
	public void setManager(boolean isManager) {
		this.isManager = isManager;
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
	 * @return the assesses
	 */
	public List getAssesses() {
		return assesses;
	}

	/**
	 * @param assesses the assesses to set
	 */
	public void setAssesses(List assesses) {
		this.assesses = assesses;
	}

	/**
	 * @return the workReports
	 */
	public List getWorkReports() {
		return workReports;
	}

	/**
	 * @param workReports the workReports to set
	 */
	public void setWorkReports(List workReports) {
		this.workReports = workReports;
	}

	/**
	 * @return the isMember
	 */
	public boolean isMember() {
		return isMember;
	}

	/**
	 * @param isMember the isMember to set
	 */
	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	/**
	 * @return the project
	 */
	public EnterpriseProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(EnterpriseProject project) {
		this.project = project;
	}
}