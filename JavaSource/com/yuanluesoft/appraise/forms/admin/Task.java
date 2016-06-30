package com.yuanluesoft.appraise.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Task extends ActionForm {
	private long areaId; //地区ID
	private String area; //地区名称
	private String name; //任务名称
	private int appraiserType; //评议员类型,0/基础,1/管理服务对象,2/全部
	private String appraiserJobs; //评议员身份,空表示全部,具体包括:人大代表,政协委员,民评代表,特邀监察员,党风,效能监督员,党代表,企业主,其他
	private String recipientJobs; //服务对象身份,空表示全部
	private int delegateAttend; //评议代表是否参与,0/不参与,1/参与短信投票,2/参与网络投票
	private double smsRatio; //短信评分比例
	private double internetRatio; //网络投票评分比例
	private int appraiseMode; //评议发起方式,0/手动,1/自动
	private String appraiseMonths; //评议月份,如：每月|1,2,3,4,5,6,7,8,9,10,11,12,指定月份|1,7
	private int appraiseBeginDay; //评议开始日期,1-28
	private int appraiseDays; //评议有效期
	private String appraiseTime; //评议发起时间,如:8:30
	private String inviteSms; //邀请短信格式
	private String otherCarrierInviteSms; //电信联通邀请短信格式
	private String delegateInviteSms; //评议代表邀请短信格式
	private String applauseSms; //答谢短信格式
	private int isSpecial; //是否专题评议
	private String specialUnitIds; //专题评议单位ID
	private String specialUnitNames; //专题评议单位
	private long lastModifierId; //最后修改人ID
	private String lastModifier; //最后修改人
	private Timestamp lastModified; //最后修改时间
	private int enabled; //是否启用
	private Set options; //选项列表
	
	//扩展属性
	private String[] months; //评议月份
	private boolean allAppraiserJobs; //是否全部评议员
	private String[] selectedAppraiserJobs; //评议员身份
	private boolean allRecipientJobs; //是否全部服务对象
	private String[] selectedRecipientJobs; //服务对象身份
	
	/**
	 * @return the applauseSms
	 */
	public String getApplauseSms() {
		return applauseSms;
	}
	/**
	 * @param applauseSms the applauseSms to set
	 */
	public void setApplauseSms(String applauseSms) {
		this.applauseSms = applauseSms;
	}
	/**
	 * @return the appraiseBeginDay
	 */
	public int getAppraiseBeginDay() {
		return appraiseBeginDay;
	}
	/**
	 * @param appraiseBeginDay the appraiseBeginDay to set
	 */
	public void setAppraiseBeginDay(int appraiseBeginDay) {
		this.appraiseBeginDay = appraiseBeginDay;
	}
	/**
	 * @return the appraiseMode
	 */
	public int getAppraiseMode() {
		return appraiseMode;
	}
	/**
	 * @param appraiseMode the appraiseMode to set
	 */
	public void setAppraiseMode(int appraiseMode) {
		this.appraiseMode = appraiseMode;
	}
	/**
	 * @return the appraiseMonths
	 */
	public String getAppraiseMonths() {
		return appraiseMonths;
	}
	/**
	 * @param appraiseMonths the appraiseMonths to set
	 */
	public void setAppraiseMonths(String appraiseMonths) {
		this.appraiseMonths = appraiseMonths;
	}
	/**
	 * @return the appraiseTime
	 */
	public String getAppraiseTime() {
		return appraiseTime;
	}
	/**
	 * @param appraiseTime the appraiseTime to set
	 */
	public void setAppraiseTime(String appraiseTime) {
		this.appraiseTime = appraiseTime;
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
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the inviteSms
	 */
	public String getInviteSms() {
		return inviteSms;
	}
	/**
	 * @param inviteSms the inviteSms to set
	 */
	public void setInviteSms(String inviteSms) {
		this.inviteSms = inviteSms;
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
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the lastModifierId
	 */
	public long getLastModifierId() {
		return lastModifierId;
	}
	/**
	 * @param lastModifierId the lastModifierId to set
	 */
	public void setLastModifierId(long lastModifierId) {
		this.lastModifierId = lastModifierId;
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
	 * @return the options
	 */
	public Set getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(Set options) {
		this.options = options;
	}
	/**
	 * @return the appraiseDays
	 */
	public int getAppraiseDays() {
		return appraiseDays;
	}
	/**
	 * @param appraiseDays the appraiseDays to set
	 */
	public void setAppraiseDays(int appraiseDays) {
		this.appraiseDays = appraiseDays;
	}
	/**
	 * @return the months
	 */
	public String[] getMonths() {
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(String[] months) {
		this.months = months;
	}
	/**
	 * @return the otherCarrierInviteSms
	 */
	public String getOtherCarrierInviteSms() {
		return otherCarrierInviteSms;
	}
	/**
	 * @param otherCarrierInviteSms the otherCarrierInviteSms to set
	 */
	public void setOtherCarrierInviteSms(String otherCarrierInviteSms) {
		this.otherCarrierInviteSms = otherCarrierInviteSms;
	}
	/**
	 * @return the isSpecial
	 */
	public int getIsSpecial() {
		return isSpecial;
	}
	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(int isSpecial) {
		this.isSpecial = isSpecial;
	}
	/**
	 * @return the recipientJobs
	 */
	public String getRecipientJobs() {
		return recipientJobs;
	}
	/**
	 * @param recipientJobs the recipientJobs to set
	 */
	public void setRecipientJobs(String recipientJobs) {
		this.recipientJobs = recipientJobs;
	}
	/**
	 * @return the specialUnitIds
	 */
	public String getSpecialUnitIds() {
		return specialUnitIds;
	}
	/**
	 * @param specialUnitIds the specialUnitIds to set
	 */
	public void setSpecialUnitIds(String specialUnitIds) {
		this.specialUnitIds = specialUnitIds;
	}
	/**
	 * @return the specialUnitNames
	 */
	public String getSpecialUnitNames() {
		return specialUnitNames;
	}
	/**
	 * @param specialUnitNames the specialUnitNames to set
	 */
	public void setSpecialUnitNames(String specialUnitNames) {
		this.specialUnitNames = specialUnitNames;
	}
	/**
	 * @return the appraiserType
	 */
	public int getAppraiserType() {
		return appraiserType;
	}
	/**
	 * @param appraiserType the appraiserType to set
	 */
	public void setAppraiserType(int appraiserType) {
		this.appraiserType = appraiserType;
	}
	/**
	 * @return the allAppraiserJobs
	 */
	public boolean isAllAppraiserJobs() {
		return allAppraiserJobs;
	}
	/**
	 * @param allAppraiserJobs the allAppraiserJobs to set
	 */
	public void setAllAppraiserJobs(boolean allAppraiserJobs) {
		this.allAppraiserJobs = allAppraiserJobs;
	}
	/**
	 * @return the allRecipientJobs
	 */
	public boolean isAllRecipientJobs() {
		return allRecipientJobs;
	}
	/**
	 * @param allRecipientJobs the allRecipientJobs to set
	 */
	public void setAllRecipientJobs(boolean allRecipientJobs) {
		this.allRecipientJobs = allRecipientJobs;
	}
	/**
	 * @return the selectedAppraiserJobs
	 */
	public String[] getSelectedAppraiserJobs() {
		return selectedAppraiserJobs;
	}
	/**
	 * @param selectedAppraiserJobs the selectedAppraiserJobs to set
	 */
	public void setSelectedAppraiserJobs(String[] selectedAppraiserJobs) {
		this.selectedAppraiserJobs = selectedAppraiserJobs;
	}
	/**
	 * @return the selectedRecipientJobs
	 */
	public String[] getSelectedRecipientJobs() {
		return selectedRecipientJobs;
	}
	/**
	 * @param selectedRecipientJobs the selectedRecipientJobs to set
	 */
	public void setSelectedRecipientJobs(String[] selectedRecipientJobs) {
		this.selectedRecipientJobs = selectedRecipientJobs;
	}
	/**
	 * @return the appraiserJobs
	 */
	public String getAppraiserJobs() {
		return appraiserJobs;
	}
	/**
	 * @param appraiserJobs the appraiserJobs to set
	 */
	public void setAppraiserJobs(String appraiserJobs) {
		this.appraiserJobs = appraiserJobs;
	}
	/**
	 * @return the delegateAttend
	 */
	public int getDelegateAttend() {
		return delegateAttend;
	}
	/**
	 * @param delegateAttend the delegateAttend to set
	 */
	public void setDelegateAttend(int delegateAttend) {
		this.delegateAttend = delegateAttend;
	}
	/**
	 * @return the delegateInviteSms
	 */
	public String getDelegateInviteSms() {
		return delegateInviteSms;
	}
	/**
	 * @param delegateInviteSms the delegateInviteSms to set
	 */
	public void setDelegateInviteSms(String delegateInviteSms) {
		this.delegateInviteSms = delegateInviteSms;
	}
	/**
	 * @return the internetRatio
	 */
	public double getInternetRatio() {
		return internetRatio;
	}
	/**
	 * @param internetRatio the internetRatio to set
	 */
	public void setInternetRatio(double internetRatio) {
		this.internetRatio = internetRatio;
	}
	/**
	 * @return the smsRatio
	 */
	public double getSmsRatio() {
		return smsRatio;
	}
	/**
	 * @param smsRatio the smsRatio to set
	 */
	public void setSmsRatio(double smsRatio) {
		this.smsRatio = smsRatio;
	}
}