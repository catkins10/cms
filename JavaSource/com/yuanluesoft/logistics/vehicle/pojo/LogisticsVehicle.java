package com.yuanluesoft.logistics.vehicle.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.pojo.GpsLocation;

/**
 * 车辆(logistics_vehicle)
 * @author linchuan
 *
 */
public class LogisticsVehicle extends Record {
	private long userId; //联盟用户ID
	private String plateNumber; //车牌号码
	private String owner; //车主姓名
	private String ownerTel; //车主电话
	private String ownerBirthplace; //籍贯
	private String ownerAddress; //家庭住址
	private String ownerIdNumber; //身份证号
	private String licenceNumber; //行车证号
	private String linkman; //随车联系人
	private String linkmanTel; //随车联系人电话
	private String brand; //汽车品牌
	private String type; //车型
	private Date buyDate; //购买时间
	private String vehicleBody; //车体状况,厢式,敞蓬,半挂,全挂,高栏,中栏,低栏,平板车,不锈钢大槽罐车,油罐车,中型罐车,铁笼车,冷藏车,加长挂,进口气囊避震车,保温车,起重车,自卸车,后八轮,前四后八
	private double vehicleLoad; //载重,吨
	private long areaId; //车辆归属地ID
	private String area; //车辆归属地
	private double vehicleLong; //车长,米
	private double vehicleWidth; //车宽,米
	private String transportLines; //主要运输路线
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Timestamp lastModified; //最后修改时间
	private String creatorIP; //登记人IP
	private char isEmpty = '1'; //空车/重车
	private String remark; //备注
	private Set gpsLocation; //车辆位置(定时更新)

	//扩展属性
	private Location location; //所在位置

	/**
	 * 车辆位置,定时更新
	 * @return the placeName
	 */
	public String getPlaceName() {
		try {
			return gpsLocation==null || gpsLocation.isEmpty() ? null : ((GpsLocation)gpsLocation.iterator().next()).getPlaceName();
		}
		catch(Exception e) {
			return null;
		}
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
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return the buyDate
	 */
	public Date getBuyDate() {
		return buyDate;
	}
	/**
	 * @param buyDate the buyDate to set
	 */
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
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
	 * @return the licenceNumber
	 */
	public String getLicenceNumber() {
		return licenceNumber;
	}
	/**
	 * @param licenceNumber the licenceNumber to set
	 */
	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
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
	 * @return the ownerAddress
	 */
	public String getOwnerAddress() {
		return ownerAddress;
	}
	/**
	 * @param ownerAddress the ownerAddress to set
	 */
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	/**
	 * @return the ownerBirthplace
	 */
	public String getOwnerBirthplace() {
		return ownerBirthplace;
	}
	/**
	 * @param ownerBirthplace the ownerBirthplace to set
	 */
	public void setOwnerBirthplace(String ownerBirthplace) {
		this.ownerBirthplace = ownerBirthplace;
	}
	/**
	 * @return the ownerIdNumber
	 */
	public String getOwnerIdNumber() {
		return ownerIdNumber;
	}
	/**
	 * @param ownerIdNumber the ownerIdNumber to set
	 */
	public void setOwnerIdNumber(String ownerIdNumber) {
		this.ownerIdNumber = ownerIdNumber;
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
	 * @return the plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}
	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
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
	 * @return the vehicleBody
	 */
	public String getVehicleBody() {
		return vehicleBody;
	}
	/**
	 * @param vehicleBody the vehicleBody to set
	 */
	public void setVehicleBody(String vehicleBody) {
		this.vehicleBody = vehicleBody;
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
	 * @return the transportLines
	 */
	public String getTransportLines() {
		return transportLines;
	}
	/**
	 * @param transportLines the transportLines to set
	 */
	public void setTransportLines(String transportLines) {
		this.transportLines = transportLines;
	}
	/**
	 * @return the vehicleLoad
	 */
	public double getVehicleLoad() {
		return vehicleLoad;
	}
	/**
	 * @param vehicleLoad the vehicleLoad to set
	 */
	public void setVehicleLoad(double vehicleLoad) {
		this.vehicleLoad = vehicleLoad;
	}
	/**
	 * @return the isEmpty
	 */
	public char getIsEmpty() {
		return isEmpty;
	}
	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setIsEmpty(char isEmpty) {
		this.isEmpty = isEmpty;
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
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
	 * @return the gpsLocation
	 */
	public Set getGpsLocation() {
		return gpsLocation;
	}
	/**
	 * @param gpsLocation the gpsLocation to set
	 */
	public void setGpsLocation(Set gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

}