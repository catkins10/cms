package com.yuanluesoft.appraise.appraiser.forms.admin;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Appraiser extends ActionForm {
	private long importId; //导入日志ID
	private int type; //类型,0/基础,1/管理服务对象
	private long orgId; //隶属机构ID
	private String orgName; //隶属机关名称
	private String name; //姓名
	private String unit; //单位或居住地
	private String address; //通讯地址
	private String area; //区域
	private String street; //乡镇或街道
	private String areaType; //居住地类别,1、城区市区（不含县市级）；2、县城城区；3、乡镇政府所在地（不含县城所在地）；4、农村
	private String mobileNumber; //手机号码
	private String job; //身份,公务员,人大代表,政协委员,企业主,城市居民,农民
	private String npcLevel; //哪级人大代表政协委员
	private String level; //级别,地厅级,县处级,乡科级,其它
	private String education; //文化程度,暂不适用
	private String serviceUnit; //提供服务或管理单位,只对管理服务对象有效
	private String serviceContent; //服务或管理项目、内容,只对管理服务对象有效
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Date expire; //有效期
	private int status; //状态,0/待审核,1/已启用,2/已过期
	private String remark; //备注
	
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
	 * @return the areaType
	 */
	public String getAreaType() {
		return areaType;
	}
	/**
	 * @param areaType the areaType to set
	 */
	public void setAreaType(String areaType) {
		this.areaType = areaType;
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
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}
	/**
	 * @param education the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}
	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}
	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
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
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	 * @return the npcLevel
	 */
	public String getNpcLevel() {
		return npcLevel;
	}
	/**
	 * @param npcLevel the npcLevel to set
	 */
	public void setNpcLevel(String npcLevel) {
		this.npcLevel = npcLevel;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	 * @return the serviceContent
	 */
	public String getServiceContent() {
		return serviceContent;
	}
	/**
	 * @param serviceContent the serviceContent to set
	 */
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
	}
	/**
	 * @return the serviceUnit
	 */
	public String getServiceUnit() {
		return serviceUnit;
	}
	/**
	 * @param serviceUnit the serviceUnit to set
	 */
	public void setServiceUnit(String serviceUnit) {
		this.serviceUnit = serviceUnit;
	}
}