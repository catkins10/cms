package com.yuanluesoft.logistics.supply.forms.admin;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Supply extends ActionForm {
	private long userId; //联盟用户ID
	private String goodsName; //货物名称
	private double quantity; //数量
	private String unit; //单位
	private double freight; //运费,0表示面议
	private String notice; //注意事项,向上，防潮，易碎，危险品 
	private double vehicleLong; //需要的车长
	private double vehicleWidth; //需要的车宽
	private String vehicleType; //需要的车型
	private String paymentMode; //付款方式
	private Date validityBegin; //有效期开始
	private Date validityEnd; //有效期结束
	private String linkman; //联系人,自动从用户信息复制
	private String linkmanTel; //联系电话
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Timestamp lastModified; //最后修改时间
	private String creatorIP; //登记人IP
	private int issue; //是否发布
	private String remark; //备注
	private Set departures; //出发地点列表
	private Set destinations; //目的地点列表
	
	private String departureAreaIds; //出发地点ID列表
	private String departureAreas; //出发地点列表
	private String destinationAreaIds; //目的地点ID列表
	private String destinationAreas; //目的地点列表
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
	 * @return the freight
	 */
	public double getFreight() {
		return freight;
	}
	/**
	 * @param freight the freight to set
	 */
	public void setFreight(double freight) {
		this.freight = freight;
	}
	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * @param goodsName the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
	 * @return the notice
	 */
	public String getNotice() {
		return notice;
	}
	/**
	 * @param notice the notice to set
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}
	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
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
	 * @return the validityBegin
	 */
	public Date getValidityBegin() {
		return validityBegin;
	}
	/**
	 * @param validityBegin the validityBegin to set
	 */
	public void setValidityBegin(Date validityBegin) {
		this.validityBegin = validityBegin;
	}
	/**
	 * @return the validityEnd
	 */
	public Date getValidityEnd() {
		return validityEnd;
	}
	/**
	 * @param validityEnd the validityEnd to set
	 */
	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}
	/**
	 * @return the vehicleLong
	 */
	public double getVehicleLong() {
		return vehicleLong;
	}
	/**
	 * @param vehicleLong the vehicleLong to set
	 */
	public void setVehicleLong(double vehicleLong) {
		this.vehicleLong = vehicleLong;
	}
	/**
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}
	/**
	 * @param vehicleType the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	/**
	 * @return the vehicleWidth
	 */
	public double getVehicleWidth() {
		return vehicleWidth;
	}
	/**
	 * @param vehicleWidth the vehicleWidth to set
	 */
	public void setVehicleWidth(double vehicleWidth) {
		this.vehicleWidth = vehicleWidth;
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