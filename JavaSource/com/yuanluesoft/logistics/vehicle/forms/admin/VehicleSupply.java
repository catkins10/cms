package com.yuanluesoft.logistics.vehicle.forms.admin;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleSupply extends ActionForm {
	private long userId; //联盟用户ID
	private int vechileCount; //汽车辆数
	private Date beginTime; //开始时间
	private Date endTime; //截止时间
	private double freightAmount; //运费金额,0表示面议
	private String freightUnit; //运费单位
	private String linkman; //联系人
	private String linkmanTel; //联系电话
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Timestamp lastModified; //最后修改时间
	private String creatorIP; //登记人IP
	private int issue; //是否发布
	private String remark; //备注
	private Set freeVehicles; //载货车辆列表
	private Set departures; //出发地点列表
	private Set destinations; //目的地点列表
	
	private String freeVehicleNumbers; //载货车辆车牌列表
	private String departureAreaIds; //出发地点ID列表
	private String departureAreas; //出发地点列表
	private String destinationAreaIds; //目的地点ID列表
	private String destinationAreas; //目的地点列表
	
	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
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
	 * @return the creatorIP
	 */
	public String getCreatorIP() {
		return creatorIP;
	}
	/**
	 * @param creatorIP the creatorIP to set
	 */
	public void setCreatorIP(String creatorIP) {
		this.creatorIP = creatorIP;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the freightAmount
	 */
	public double getFreightAmount() {
		return freightAmount;
	}
	/**
	 * @param freightAmount the freightAmount to set
	 */
	public void setFreightAmount(double freightAmount) {
		this.freightAmount = freightAmount;
	}
	/**
	 * @return the freightUnit
	 */
	public String getFreightUnit() {
		return freightUnit;
	}
	/**
	 * @param freightUnit the freightUnit to set
	 */
	public void setFreightUnit(String freightUnit) {
		this.freightUnit = freightUnit;
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
	 * @return the linkmanTel
	 */
	public String getLinkmanTel() {
		return linkmanTel;
	}
	/**
	 * @param linkmanTel the linkmanTel to set
	 */
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
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
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the vechileCount
	 */
	public int getVechileCount() {
		return vechileCount;
	}
	/**
	 * @param vechileCount the vechileCount to set
	 */
	public void setVechileCount(int vechileCount) {
		this.vechileCount = vechileCount;
	}
	/**
	 * @return the departures
	 */
	public Set getDepartures() {
		return departures;
	}
	/**
	 * @param departures the departures to set
	 */
	public void setDepartures(Set departures) {
		this.departures = departures;
	}
	/**
	 * @return the destinations
	 */
	public Set getDestinations() {
		return destinations;
	}
	/**
	 * @param destinations the destinations to set
	 */
	public void setDestinations(Set destinations) {
		this.destinations = destinations;
	}
	/**
	 * @return the freeVehicles
	 */
	public Set getFreeVehicles() {
		return freeVehicles;
	}
	/**
	 * @param freeVehicles the freeVehicles to set
	 */
	public void setFreeVehicles(Set freeVehicles) {
		this.freeVehicles = freeVehicles;
	}
	/**
	 * @return the departureAreaIds
	 */
	public String getDepartureAreaIds() {
		return departureAreaIds;
	}
	/**
	 * @param departureAreaIds the departureAreaIds to set
	 */
	public void setDepartureAreaIds(String departureAreaIds) {
		this.departureAreaIds = departureAreaIds;
	}
	/**
	 * @return the departureAreas
	 */
	public String getDepartureAreas() {
		return departureAreas;
	}
	/**
	 * @param departureAreas the departureAreas to set
	 */
	public void setDepartureAreas(String departureAreas) {
		this.departureAreas = departureAreas;
	}
	/**
	 * @return the destinationAreaIds
	 */
	public String getDestinationAreaIds() {
		return destinationAreaIds;
	}
	/**
	 * @param destinationAreaIds the destinationAreaIds to set
	 */
	public void setDestinationAreaIds(String destinationAreaIds) {
		this.destinationAreaIds = destinationAreaIds;
	}
	/**
	 * @return the destinationAreas
	 */
	public String getDestinationAreas() {
		return destinationAreas;
	}
	/**
	 * @param destinationAreas the destinationAreas to set
	 */
	public void setDestinationAreas(String destinationAreas) {
		this.destinationAreas = destinationAreas;
	}
	/**
	 * @return the freeVehicleNumbers
	 */
	public String getFreeVehicleNumbers() {
		return freeVehicleNumbers;
	}
	/**
	 * @param freeVehicleNumbers the freeVehicleNumbers to set
	 */
	public void setFreeVehicleNumbers(String freeVehicleNumbers) {
		this.freeVehicleNumbers = freeVehicleNumbers;
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
	 * @return the issue
	 */
	public int getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(int issue) {
		this.issue = issue;
	}
}