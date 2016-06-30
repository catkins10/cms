package com.yuanluesoft.charge.servicemanage.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.charge.servicemanage.service.ServiceManage;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 计费:服务报价(charge_service_price)
 * @author wangjianping
 *
 */
public class ServicePrice extends Record {
	private String name; //名称
	private long serviceId; //服务ID
	private double servicePrice; //价格,必须大于0
	private String orderModes; //订购方式列表,电信运营商/充值卡/网上订购
	private String paymentMode; //计费方式,按天/按周/按月/按年/一次性付款
	private int paymentPeriod; //计费周期,默认为1
	private char reiteration = '0'; //是否自动重复
	private int deductDay; //扣款周期开始时间,计费方式按周：0-6,按月：1-28,按年：(1-12)(1-28)(由月份和日期组合)，-1表示任意
	private String deductPolicy; //扣款策略,day/按实际天数扣除,custom/自定义,当deductionDay>0时有效
	private int minPeriods; //最小的订购周期数,默认为1,如果用户使用时间没有达到minPeriods,不允许退订
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private char deleted = '0'; //是否删除
	private char halt = '0'; //是否停用
	private String remark; //备注

	private Set serviceDeductPolicies; //扣款策略
	private Service service; //服务
	
	/**
	 * 报价标题
	 * @return
	 */
	public String getTitle() {
		return name!=null ? name : (service==null ? "" : service.getServiceName() + "(" + getPrice() + ")"); 
	}
	
	/**
	 * 获取服务价格
	 * @return
	 */
	public String getPrice() {
		if("一次性付款".equals(paymentMode)) {
			return servicePrice + "元";
		}
		else {
			String unit = paymentMode.substring(1);
			if(paymentPeriod>1) {
				if(paymentPeriod==6 && "月".equals(unit)) {
					return servicePrice + "元/半年";
				}
				return servicePrice + "元/" + paymentPeriod + ("月".equals(unit) ? "个" : "")  + unit;
			}
			else {
				return servicePrice + "元/" + unit;
			}
		}
	}
	
	/**
	 * 获取扣款时间
	 * @return
	 */
	public String getDeductDayText() {
		if(deductDay==-1) {
			return "订购时间";
		}
		if(paymentMode.equals("按月")) {
			return "每月的" + deductDay + "日";
		}
		if(paymentMode.equals("按周")) {
			String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
			return "每周的" + weekDays[deductDay];
		}
		if(paymentMode.equals("按年")) {
			return "每年的" + (deductDay/100) + "月" + (deductDay%100) + "日";
		}
		return "";
	}
	
	/**
	 * 获取订购方式列表
	 * @return
	 */
	public String getOrderModeTitles() {
		if(orderModes==null || "".equals(orderModes)) {
			return null;
		}
		return orderModes.replaceFirst(ServiceManage.SERVICE_ORDER_MODE_WEB, "网上订购").replaceFirst(ServiceManage.SERVICE_ORDER_MODE_CARRIER, "运营商").replaceFirst(ServiceManage.SERVICE_ORDER_MODE_CARD, "充值卡");
	}
	
	/**
	 * @return the deductDay
	 */
	public int getDeductDay() {
		return deductDay;
	}

	/**
	 * @param deductDay the deductDay to set
	 */
	public void setDeductDay(int deductDay) {
		this.deductDay = deductDay;
	}

	/**
	 * @return the deductPolicy
	 */
	public String getDeductPolicy() {
		return deductPolicy;
	}

	/**
	 * @param deductPolicy the deductPolicy to set
	 */
	public void setDeductPolicy(String deductPolicy) {
		this.deductPolicy = deductPolicy;
	}

	/**
	 * @return the orderModes
	 */
	public String getOrderModes() {
		return orderModes;
	}

	/**
	 * @param orderModes the orderModes to set
	 */
	public void setOrderModes(String orderModes) {
		this.orderModes = orderModes;
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
	 * @return the paymentPeriod
	 */
	public int getPaymentPeriod() {
		return paymentPeriod;
	}

	/**
	 * @param paymentPeriod the paymentPeriod to set
	 */
	public void setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	/**
	 * @return the serviceDeductPolicies
	 */
	public Set getServiceDeductPolicies() {
		return serviceDeductPolicies;
	}

	/**
	 * @param serviceDeductPolicies the serviceDeductPolicies to set
	 */
	public void setServiceDeductPolicies(Set serviceDeductPolicies) {
		this.serviceDeductPolicies = serviceDeductPolicies;
	}

	/**
	 * @return the serviceId
	 */
	public long getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the servicePrice
	 */
	public double getServicePrice() {
		return servicePrice;
	}

	/**
	 * @param servicePrice the servicePrice to set
	 */
	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
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
	 * @return the reiteration
	 */
	public char getReiteration() {
		return reiteration;
	}

	/**
	 * @param reiteration the reiteration to set
	 */
	public void setReiteration(char reiteration) {
		this.reiteration = reiteration;
	}

	/**
	 * @return the minPeriods
	 */
	public int getMinPeriods() {
		return minPeriods;
	}

	/**
	 * @param minPeriods the minPeriods to set
	 */
	public void setMinPeriods(int minPeriods) {
		this.minPeriods = minPeriods;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
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
	 * @return the deleted
	 */
	public char getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}

	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
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
}
