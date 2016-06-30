/*
 * Created on 2007-4-19
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Person extends Record {
	private int sid; //SID,由sequence产生的ID
	private String name; //姓名,姓名
	private String loginName; //登录用户名,登录用户名
	private char sex = 'M'; //性别,性别
	private int type; //类别,教师/学生/家长/工作人员
	private String password; //密码,密码
	private float priority; //优先级,用来做用户排序
	private String creator; //注册人,注册人
	private long creatorId; //注册人ID,注册人ID
	private Timestamp created; //注册时间,注册时间
	private Timestamp lastModified; //最后修改时间
	private String mailAddress; //邮件地址,邮件地址
	private String tel; //电话,电话
	private String telFamily; //家庭电话,家庭电话
	private String mobile; //手机,手机
	private String familyAddress; //家庭地址,家庭地址
	private String identityCard; //身份证号码
	private String identityCardName; //身份证姓名
	private char deleteDisable = '0'; //禁止删除,禁止删除
	private char preassign = '0'; //系统预置,系统预置
	private long postId; //岗位ID
	private String post; //岗位名称
	private String remark; //备注,备注
	
	private Set subjections; //用户隶属的组织机构
	private Set subjectionRoles; //用户隶属的角色
	private Set agents; //代理人列表
	private Set children; //孩子列表
	private Set supervisors; //分管领导
	private Set supervisePersons; //被分管的用户
	private Set superviseOrgs; //用户主管的组织机构
	private Set leadOrgs; //用户所领导的组织机构
	private Set userPageTemplates; //个人主页模板
	private Set logins; //登录情况;
	
	//拓展属性,用户注册和更新时有效
	private String orgIds; //用户隶属的组织ID列表,用逗号分隔
	private String supervisorIds; //分管领导ID列表
	private String supervisorNames; //分管领导姓名列表
	private String supervisePersonIds; //被分管的用户ID列表
	private String supervisePersonNames; //被分管的用户姓名列表
	private String superviseOrgIds; //分管的部门ID列表
	private String superviseOrgNames; //分管的部门列表
	private String leadOrgIds; //用户任领导的部门
	private String leadOrgNames; //用户任领导的部门
	
	/**
	 * 获取用户类型
	 * @return
	 */
	public String getPersonType() {
		return PersonService.PERSON_TYPE_NAMES[type];
	}
	
	/**
	 * 获取完整的邮件地址
	 * @return
	 */
	public String getMailFullAddress() {
		return mailAddress==null || mailAddress.equals("") ? null : "\"" + name + "\" <" + mailAddress + ">"; 
	}
	
	/**
	 * 获取用户头像URL
	 * @return
	 */
	public String getPortraitURL() {
		return Environment.getContextPath() + "/jeaf/usermanage/portrait.shtml?personId=" + getId();
	}
	
	/**
	 * @return the leadOrgIds
	 */
	public String getLeadOrgIds() {
		if(leadOrgIds==null) {
			leadOrgIds = ListUtils.join(leadOrgs, "orgId", ",", false);
		}
		return leadOrgIds;
	}

	/**
	 * 获取URL
	 * @return
	 */
	public String getUrl() {
		return "javascript:PageUtils.editrecord('jeaf/usermanage', 'admin/" + getPersonType() + "', '" + getId() + "', 'width=720,height=480')";
	}

	/**
	 * @param leadOrgIds the leadOrgIds to set
	 */
	public void setLeadOrgIds(String leadOrgIds) {
		this.leadOrgIds = leadOrgIds;
	}

	/**
	 * @return the leadOrgNames
	 */
	public String getLeadOrgNames() {
		if(leadOrgNames==null) {
			leadOrgNames = ListUtils.join(leadOrgs, "orgName", ",", false);
		}
		return leadOrgNames;
	}

	/**
	 * @param leadOrgNames the leadOrgNames to set
	 */
	public void setLeadOrgNames(String leadOrgNames) {
		this.leadOrgNames = leadOrgNames;
	}

	/**
	 * @return the orgIds
	 */
	public String getOrgIds() {
		if(orgIds==null) {
			orgIds = ListUtils.join(subjections, "orgId", ",", false);
		}
		return orgIds;
	}

	/**
	 * @param orgIds the orgIds to set
	 */
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	/**
	 * @return the superviseOrgIds
	 */
	public String getSuperviseOrgIds() {
		if(superviseOrgIds==null) {
			superviseOrgIds = ListUtils.join(superviseOrgs, "orgId", ",", false);
		}
		return superviseOrgIds;
	}

	/**
	 * @param superviseOrgIds the superviseOrgIds to set
	 */
	public void setSuperviseOrgIds(String superviseOrgIds) {
		this.superviseOrgIds = superviseOrgIds;
	}

	/**
	 * @return the superviseOrgNames
	 */
	public String getSuperviseOrgNames() {
		if(superviseOrgNames==null) {
			superviseOrgNames = ListUtils.join(superviseOrgs, "orgName", ",", false);
		}
		return superviseOrgNames;
	}

	/**
	 * @param superviseOrgNames the superviseOrgNames to set
	 */
	public void setSuperviseOrgNames(String superviseOrgNames) {
		this.superviseOrgNames = superviseOrgNames;
	}

	/**
	 * @return the supervisePersonIds
	 */
	public String getSupervisePersonIds() {
		if(supervisePersonIds==null) {
			supervisePersonIds = ListUtils.join(supervisePersons, "personId", ",", false);
		}
		return supervisePersonIds;
	}

	/**
	 * @param supervisePersonIds the supervisePersonIds to set
	 */
	public void setSupervisePersonIds(String supervisePersonIds) {
		this.supervisePersonIds = supervisePersonIds;
	}

	/**
	 * @return the supervisePersonNames
	 */
	public String getSupervisePersonNames() {
		if(supervisePersonNames==null) {
			supervisePersonNames = ListUtils.join(supervisePersons, "personName", ",", false);
		}
		return supervisePersonNames;
	}

	/**
	 * @param supervisePersonNames the supervisePersonNames to set
	 */
	public void setSupervisePersonNames(String supervisePersonNames) {
		this.supervisePersonNames = supervisePersonNames;
	}

	/**
	 * @return the supervisorIds
	 */
	public String getSupervisorIds() {
		if(supervisorIds==null) {
			supervisorIds = ListUtils.join(supervisors, "supervisorId", ",", false);
		}
		return supervisorIds;
	}

	/**
	 * @param supervisorIds the supervisorIds to set
	 */
	public void setSupervisorIds(String supervisorIds) {
		this.supervisorIds = supervisorIds;
	}

	/**
	 * @return the supervisorNames
	 */
	public String getSupervisorNames() {
		if(supervisorNames==null) {
			supervisorNames = ListUtils.join(supervisors, "supervisor", ",", false);
		}
		return supervisorNames;
	}

	/**
	 * @param supervisorNames the supervisorNames to set
	 */
	public void setSupervisorNames(String supervisorNames) {
		this.supervisorNames = supervisorNames;
	}

	/**
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return Returns the creator.
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator The creator to set.
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return Returns the creatorId.
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId The creatorId to set.
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return Returns the familyAddress.
	 */
	public String getFamilyAddress() {
		return familyAddress;
	}
	/**
	 * @param familyAddress The familyAddress to set.
	 */
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	/**
	 * @return Returns the loginName.
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName The loginName to set.
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return Returns the mailAddress.
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress The mailAddress to set.
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the preassign.
	 */
	public char getPreassign() {
		return preassign;
	}
	/**
	 * @param preassign The preassign to set.
	 */
	public void setPreassign(char preassign) {
		this.preassign = preassign;
	}
	/**
	 * @return Returns the priority.
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the subjections.
	 */
	public java.util.Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections The subjections to set.
	 */
	public void setSubjections(java.util.Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return Returns the tel.
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel The tel to set.
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return Returns the telFamily.
	 */
	public String getTelFamily() {
		return telFamily;
	}
	/**
	 * @param telFamily The telFamily to set.
	 */
	public void setTelFamily(String telFamily) {
		this.telFamily = telFamily;
	}
	/**
	 * @return Returns the deleteDisable.
	 */
	public char getDeleteDisable() {
		return deleteDisable;
	}
	/**
	 * @param deleteDisable The deleteDisable to set.
	 */
	public void setDeleteDisable(char deleteDisable) {
		this.deleteDisable = deleteDisable;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the agents
	 */
	public Set getAgents() {
		return agents;
	}
	/**
	 * @param agents the agents to set
	 */
	public void setAgents(Set agents) {
		this.agents = agents;
	}
	/**
	 * @return the children
	 */
	public Set getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(Set children) {
		this.children = children;
	}
	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}

	/**
	 * @return the identityCard
	 */
	public String getIdentityCard() {
		return identityCard;
	}

	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	/**
	 * @return the identityCardName
	 */
	public String getIdentityCardName() {
		return identityCardName;
	}

	/**
	 * @param identityCardName the identityCardName to set
	 */
	public void setIdentityCardName(String identityCardName) {
		this.identityCardName = identityCardName;
	}

	/**
	 * @return the supervisors
	 */
	public Set getSupervisors() {
		return supervisors;
	}

	/**
	 * @param supervisors the supervisors to set
	 */
	public void setSupervisors(Set supervisors) {
		this.supervisors = supervisors;
	}

	/**
	 * @return the leadOrgs
	 */
	public Set getLeadOrgs() {
		return leadOrgs;
	}

	/**
	 * @param leadOrgs the leadOrgs to set
	 */
	public void setLeadOrgs(Set leadOrgs) {
		this.leadOrgs = leadOrgs;
	}

	/**
	 * @return the superviseOrgs
	 */
	public Set getSuperviseOrgs() {
		return superviseOrgs;
	}

	/**
	 * @param superviseOrgs the superviseOrgs to set
	 */
	public void setSuperviseOrgs(Set superviseOrgs) {
		this.superviseOrgs = superviseOrgs;
	}

	/**
	 * @return the supervisePersons
	 */
	public java.util.Set getSupervisePersons() {
		return supervisePersons;
	}

	/**
	 * @param supervisePersons the supervisePersons to set
	 */
	public void setSupervisePersons(java.util.Set supervisePersons) {
		this.supervisePersons = supervisePersons;
	}

	/**
	 * @return the subjectionRoles
	 */
	public Set getSubjectionRoles() {
		return subjectionRoles;
	}

	/**
	 * @param subjectionRoles the subjectionRoles to set
	 */
	public void setSubjectionRoles(Set subjectionRoles) {
		this.subjectionRoles = subjectionRoles;
	}

	/**
	 * @return the userPageTemplates
	 */
	public Set getUserPageTemplates() {
		return userPageTemplates;
	}

	/**
	 * @param userPageTemplates the userPageTemplates to set
	 */
	public void setUserPageTemplates(Set userPageTemplates) {
		this.userPageTemplates = userPageTemplates;
	}

	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
	}

	/**
	 * @return the postId
	 */
	public long getPostId() {
		return postId;
	}

	/**
	 * @param postId the postId to set
	 */
	public void setPostId(long postId) {
		this.postId = postId;
	}

	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the logins
	 */
	public Set getLogins() {
		return logins;
	}

	/**
	 * @param logins the logins to set
	 */
	public void setLogins(Set logins) {
		this.logins = logins;
	}
}