package com.yuanluesoft.logistics.vehicle.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 车源(logistics_vehicle_supply)
 * @author linchuan
 *
 */
public class LogisticsVehicleSupply extends Record {
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
	
	/**
	 * 获取车牌号
	 * @return
	 */
	public String getPlateNumbers() {
		return ListUtils.join(freeVehicles, "plateNumber", ",", false);
	}

	/**
	 * 获取出发地点
	 * @return
	 */
	public String getDepartureAreas() {
		return ListUtils.join(departures, "departure", ",", false);
	}
	
	/**
	 * 获取目的地
	 * @return
	 */
	public String getDestinationAreas() {
		return ListUtils.join(destinations, "destination", ",", false);
	}

	/**
	 * 输出单价
	 * @return
	 */
	public String getFreightAmountTitle() {
		return freightAmount==0 ? "面议" : new DecimalFormat("#.##").format(freightAmount) + "元/" + freightUnit;
	}
	
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