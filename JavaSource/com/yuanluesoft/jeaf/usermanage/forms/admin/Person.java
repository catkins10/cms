/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * @author Administrator
 *
 *
 */
public class Person extends ActionForm {
	private String name;
	private String loginName;
	private int type;
	private String password;
	private char sex = 'M';
	private float priority;
	private String creator;
	private long creatorId;
	private Timestamp created;
	private Timestamp lastModified; //最后修改时间
	private String mailAddress;
	private String familyAddress;
	private String mobile;
	private String tel;
	private String telFamily;
	private String identityCard; //身份证号码
	private String identityCardName; //身份证姓名
	private char preassign = '0';
	private char deleteDisable = '0';
	private long postId; //岗位ID
	private String post; //岗位名称
	private String remark;
	private java.util.Set subjections;
	private java.util.Set supervisors; //分管领导
	private java.util.Set supervisePersons; //被分管的用户
	private Set superviseOrgs; //用户主管的组织机构
	private Set leadOrgs; //用户所领导的组织机构
	
	private long orgId; //用户所在机构,机构列表中的第一个
	private String orgFullName; //用户所在组织全称
	private String otherOrgIds; //用户兼职的其他组织ID列表,用逗号分隔
	private String otherOrgNames; //用户兼职的其他组织名称列表,用逗号分隔
	
	private String supervisorIds; //分管领导ID列表
	private String supervisorNames; //分管领导姓名列表
	private String supervisePersonIds; //被分管的用户ID列表
	private String supervisePersonNames; //被分管的用户姓名列表
	
	private String superviseOrgIds; //分管的部门ID列表
	private String superviseOrgNames; //分管的部门列表
	private String leadOrgIds; //用户任领导的部门
	private String leadOrgNames; //用户任领导的部门
	
	private char halt = '0';
	
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
	 * @return Returns the orgFullName.
	 */
	public String getOrgFullName() {
		return orgFullName;
	}
	/**
	 * @param orgFullName The orgFullName to set.
	 */
	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}
	/**
	 * @return Returns the orgId.
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
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
	 * @return Returns the halt.
	 */
	public char getHalt() {
		return halt;
	}
	/**
	 * @param halt The halt to set.
	 */
	public void setHalt(char halt) {
		this.halt = halt;
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
	 * @return Returns the otherOrgIds.
	 */
	public String getOtherOrgIds() {
		return otherOrgIds;
	}
	/**
	 * @param otherOrgIds The otherOrgIds to set.
	 */
	public void setOtherOrgIds(String otherOrgIds) {
		this.otherOrgIds = otherOrgIds;
	}
	/**
	 * @return Returns the otherOrgNames.
	 */
	public String getOtherOrgNames() {
		return otherOrgNames;
	}
	/**
	 * @param otherOrgNames The otherOrgNames to set.
	 */
	public void setOtherOrgNames(String otherOrgNames) {
		this.otherOrgNames = otherOrgNames;
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
	 * @return the supervisorIds
	 */
	public String getSupervisorIds() {
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
		return supervisorNames;
	}
	/**
	 * @param supervisorNames the supervisorNames to set
	 */
	public void setSupervisorNames(String supervisorNames) {
		this.supervisorNames = supervisorNames;
	}
	/**
	 * @return the supervisors
	 */
	public java.util.Set getSupervisors() {
		return supervisors;
	}
	/**
	 * @param supervisors the supervisors to set
	 */
	public void setSupervisors(java.util.Set supervisors) {
		this.supervisors = supervisors;
	}
	/**
	 * @return the superviseOrgIds
	 */
	public String getSuperviseOrgIds() {
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
		return superviseOrgNames;
	}
	/**
	 * @param superviseOrgNames the superviseOrgNames to set
	 */
	public void setSuperviseOrgNames(String superviseOrgNames) {
		this.superviseOrgNames = superviseOrgNames;
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
	 * @return the leadOrgIds
	 */
	public String getLeadOrgIds() {
		return leadOrgIds;
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
		return leadOrgNames;
	}
	/**
	 * @param leadOrgNames the leadOrgNames to set
	 */
	public void setLeadOrgNames(String leadOrgNames) {
		this.leadOrgNames = leadOrgNames;
	}
	/**
	 * @return the supervisePersonIds
	 */
	public String getSupervisePersonIds() {
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
		return supervisePersonNames;
	}
	/**
	 * @param supervisePersonNames the supervisePersonNames to set
	 */
	public void setSupervisePersonNames(String supervisePersonNames) {
		this.supervisePersonNames = supervisePersonNames;
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
}