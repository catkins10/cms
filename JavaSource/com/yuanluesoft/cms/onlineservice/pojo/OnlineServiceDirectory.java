package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;

/**
 * 网上办事:目录(onlineservice_directory)
 * @author linchuan
 *
 */
public class OnlineServiceDirectory extends Directory {
	private String code; //编号,用来给事项编号
	private String acceptWorkflowName; //在线受理流程
	private String acceptWorkflowId; //在线受理流程ID
	private String complaintWorkflowName; //在线投诉流程
	private String complaintWorkflowId; //在线投诉流程ID
	private String consultWorkflowName; //在线咨询流程
	private String consultWorkflowId; //在线咨询流程ID
	private String faqWorkflowName; //常见问题发布流程
	private String faqWorkflowId; //常见问题发布流程ID
	private String itemSynchSiteIds; //办理事项同步的网站栏目
	private String complaintSynchSiteIds; //投诉同步的网站栏目
	private String consultSynchSiteIds; //咨询同步的网站栏目
	private long siteId; //隶属站点ID,仅对根目录有效
	private String siteName; //隶属站点名称
	private char halt = '0'; //是否停用
	
	/**
	 * @return the acceptWorkflowId
	 */
	public String getAcceptWorkflowId() {
		return acceptWorkflowId;
	}
	/**
	 * @param acceptWorkflowId the acceptWorkflowId to set
	 */
	public void setAcceptWorkflowId(String acceptWorkflowId) {
		this.acceptWorkflowId = acceptWorkflowId;
	}
	/**
	 * @return the acceptWorkflowName
	 */
	public String getAcceptWorkflowName() {
		return acceptWorkflowName;
	}
	/**
	 * @param acceptWorkflowName the acceptWorkflowName to set
	 */
	public void setAcceptWorkflowName(String acceptWorkflowName) {
		this.acceptWorkflowName = acceptWorkflowName;
	}
	/**
	 * @return the complaintWorkflowId
	 */
	public String getComplaintWorkflowId() {
		return complaintWorkflowId;
	}
	/**
	 * @param complaintWorkflowId the complaintWorkflowId to set
	 */
	public void setComplaintWorkflowId(String complaintWorkflowId) {
		this.complaintWorkflowId = complaintWorkflowId;
	}
	/**
	 * @return the complaintWorkflowName
	 */
	public String getComplaintWorkflowName() {
		return complaintWorkflowName;
	}
	/**
	 * @param complaintWorkflowName the complaintWorkflowName to set
	 */
	public void setComplaintWorkflowName(String complaintWorkflowName) {
		this.complaintWorkflowName = complaintWorkflowName;
	}
	/**
	 * @return the consultWorkflowId
	 */
	public String getConsultWorkflowId() {
		return consultWorkflowId;
	}
	/**
	 * @param consultWorkflowId the consultWorkflowId to set
	 */
	public void setConsultWorkflowId(String consultWorkflowId) {
		this.consultWorkflowId = consultWorkflowId;
	}
	/**
	 * @return the consultWorkflowName
	 */
	public String getConsultWorkflowName() {
		return consultWorkflowName;
	}
	/**
	 * @param consultWorkflowName the consultWorkflowName to set
	 */
	public void setConsultWorkflowName(String consultWorkflowName) {
		this.consultWorkflowName = consultWorkflowName;
	}
	/**
	 * @return the complaintSynchSiteIds
	 */
	public String getComplaintSynchSiteIds() {
		return complaintSynchSiteIds;
	}
	/**
	 * @param complaintSynchSiteIds the complaintSynchSiteIds to set
	 */
	public void setComplaintSynchSiteIds(String complaintSynchSiteIds) {
		this.complaintSynchSiteIds = complaintSynchSiteIds;
	}
	/**
	 * @return the consultSynchSiteIds
	 */
	public String getConsultSynchSiteIds() {
		return consultSynchSiteIds;
	}
	/**
	 * @param consultSynchSiteIds the consultSynchSiteIds to set
	 */
	public void setConsultSynchSiteIds(String consultSynchSiteIds) {
		this.consultSynchSiteIds = consultSynchSiteIds;
	}
	/**
	 * @return the itemSynchSiteIds
	 */
	public String getItemSynchSiteIds() {
		return itemSynchSiteIds;
	}
	/**
	 * @param itemSynchSiteIds the itemSynchSiteIds to set
	 */
	public void setItemSynchSiteIds(String itemSynchSiteIds) {
		this.itemSynchSiteIds = itemSynchSiteIds;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the faqWorkflowId
	 */
	public String getFaqWorkflowId() {
		return faqWorkflowId;
	}
	/**
	 * @param faqWorkflowId the faqWorkflowId to set
	 */
	public void setFaqWorkflowId(String faqWorkflowId) {
		this.faqWorkflowId = faqWorkflowId;
	}
	/**
	 * @return the faqWorkflowName
	 */
	public String getFaqWorkflowName() {
		return faqWorkflowName;
	}
	/**
	 * @param faqWorkflowName the faqWorkflowName to set
	 */
	public void setFaqWorkflowName(String faqWorkflowName) {
		this.faqWorkflowName = faqWorkflowName;
	}
}