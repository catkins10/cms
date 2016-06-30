package com.yuanluesoft.cms.onlineservice.pojo;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 网上办事:办理事项(onlineservice_item)
 * @author linchuan
 *
 */
public class OnlineServiceItem extends Record {
	private String code; //编码
	private String name; //名称
	private char isPublic = '1'; //是否公开
	private float priority; //优先级
	private String itemType; //事项类型
	private String publicServiceType; //公共服务类别,管理服务|便民服务|其他事项 
	private char acceptSupport = '1'; //是否支持在线受理,网上预审，同时提供查询功能
	private String acceptWorkflowName; //在线受理流程
	private String acceptWorkflowId; //在线受理流程ID
	private char complaintSupport = '1'; //是否支持在线投诉,同时提供查询功能
	private String complaintWorkflowName; //在线投诉流程
	private String complaintWorkflowId; //在线投诉流程ID
	private char consultSupport = '1'; //是否支持在线咨询,同时提供查询功能
	private String consultWorkflowName; //在线咨询流程
	private String consultWorkflowId; //在线咨询流程ID
	private String acceptUrl; //在线受理链接,链接到行政服务中心
	private String complaintUrl; //在线投诉链接,链接到行政服务中心
	private String consultUrl; //在线咨询链接,链接到行政服务中心
	private char querySupport = '1'; //是否支持状态查询
	private String queryUrl; //状态查询链接,链接到行政服务中心
	private char resultSupport = '0'; //是否支持结果查询
	private String resultUrl; //结果查询链接
	private String remark; //备注
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	
	private Set subjections; //隶属目录列表
	private Set guide; //办理指南
	private Set materials; //材料列表
	private Set units; //办理机构列表
	private Set transactors; //办理人列表
	private Set faqs; //常见问题解答列表
	
	private List complaints; //近期投诉列表,后台录入投诉时使用
	private List consults; //近期咨询列表,后台录入咨询时使用
	private OnlineServiceItemGuide serviceItemGuide = new OnlineServiceItemGuide(); //指南,页面输出时使用
	
	/**
	 * 获取在线受理URL
	 * @return
	 */
	public String getInternetAcceptUrl() {
		if(acceptSupport!='1') { //不支持
			return null;
		}
		if(acceptUrl!=null && !acceptUrl.equals("")) { //指定了URL
			return "{FINAL}" + acceptUrl;
		}
		return Environment.getContextPath() + "/cms/onlineservice/accept/accept.shtml?itemId={PARAMETER:id}&directoryId={PARAMETER:directoryId}";
	}
	
	/**
	 * 获取在线投诉URL
	 * @return
	 */
	public String getInternetComplaintUrl() {
		if(complaintSupport!='1') { //不支持
			return null;
		}
		if(complaintUrl!=null && !complaintUrl.equals("")) { //指定了URL
			return "{FINAL}" + complaintUrl;
		}
		return Environment.getContextPath() + "/cms/onlineservice/complaint/complaint.shtml?itemId={PARAMETER:id}&directoryId={PARAMETER:directoryId}";
	}

	/**
	 * 获取在线咨询URL
	 * @return
	 */
	public String getInternetConsultUrl() {
		if(consultSupport!='1') { //不支持
			return null;
		}
		if(consultUrl!=null && !consultUrl.equals("")) { //指定了URL
			return "{FINAL}" + consultUrl;
		}
		return Environment.getContextPath() + "/cms/onlineservice/consult/consult.shtml?itemId={PARAMETER:id}&directoryId={PARAMETER:directoryId}";
	}
	
	/**
	 * 获取状态查询URL
	 * @return
	 */
	public String getInternetQueryUrl() {
		if(querySupport!='1') { //不支持
			return null;
		}
		if(queryUrl!=null && !queryUrl.equals("")) { //指定了URL
			return "{FINAL}" + queryUrl;
		}
		return Environment.getContextPath() + "/cms/onlineservice/listInteractives.shtml?applicationName=cms/onlineservice/accept&type=onlineServiceAccepts&itemId={PARAMETER:id}&directoryId={PARAMETER:directoryId}";
	}
	
	/**
	 * 获取结果查询URL
	 * @return
	 */
	public String getInternetResultUrl() {
		if(resultSupport!='1') { //不支持
			return null;
		}
		if(resultUrl!=null && !resultUrl.equals("")) { //指定了URL
			return "{FINAL}" + resultUrl;
		}
		return null;
	}
	
	/**
	 * 获取表格下载列表
	 * @return
	 */
	public Set getDownloads() {
		if(materials==null || materials.isEmpty()) {
			return null;
		}
		Set downloads = new LinkedHashSet();
		for(Iterator iterator = materials.iterator(); iterator.hasNext();) {
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
			if(material.getTableURL()!=null && !material.getTableURL().equals("")) {
				downloads.add(material);
			}
		}
		return downloads;
	}
	
	/**
	 * 获取样表表格下载列表
	 * @return
	 */
	public Set getExampleDownloads() {
		if(materials==null || materials.isEmpty()) {
			return null;
		}
		Set downloads = new LinkedHashSet();
		for(Iterator iterator = materials.iterator(); iterator.hasNext();) {
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
			if(material.getExampleURL()!=null && !material.getExampleURL().equals("")) {
				downloads.add(material);
			}
		}
		return downloads;
	}
	
	/**
	 * @return the acceptSupport
	 */
	public char getAcceptSupport() {
		return acceptSupport;
	}
	/**
	 * @param acceptSupport the acceptSupport to set
	 */
	public void setAcceptSupport(char acceptSupport) {
		this.acceptSupport = acceptSupport;
	}
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
	 * @return the complaintSupport
	 */
	public char getComplaintSupport() {
		return complaintSupport;
	}
	/**
	 * @param complaintSupport the complaintSupport to set
	 */
	public void setComplaintSupport(char complaintSupport) {
		this.complaintSupport = complaintSupport;
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
	 * @return the consultSupport
	 */
	public char getConsultSupport() {
		return consultSupport;
	}
	/**
	 * @param consultSupport the consultSupport to set
	 */
	public void setConsultSupport(char consultSupport) {
		this.consultSupport = consultSupport;
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
	 * @return the isPublic
	 */
	public char getIsPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(char isPublic) {
		this.isPublic = isPublic;
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
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the guide
	 */
	public Set getGuide() {
		return guide;
	}
	/**
	 * @param guide the guide to set
	 */
	public void setGuide(Set guide) {
		this.guide = guide;
		if(guide!=null && !guide.isEmpty()) {
			setServiceItemGuide((OnlineServiceItemGuide)guide.iterator().next());
		}
	}
	/**
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return the units
	 */
	public Set getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(Set units) {
		this.units = units;
	}
	/**
	 * @return the transactors
	 */
	public Set getTransactors() {
		return transactors;
	}
	/**
	 * @param transactors the transactors to set
	 */
	public void setTransactors(Set transactors) {
		this.transactors = transactors;
	}
	/**
	 * @return the materials
	 */
	public Set getMaterials() {
		return materials;
	}
	/**
	 * @param materials the materials to set
	 */
	public void setMaterials(Set materials) {
		this.materials = materials;
	}
	/**
	 * @return the serviceItemGuide
	 */
	public OnlineServiceItemGuide getServiceItemGuide() {
		return serviceItemGuide;
	}
	/**
	 * @param serviceItemGuide the serviceItemGuide to set
	 */
	public void setServiceItemGuide(OnlineServiceItemGuide serviceItemGuide) {
		this.serviceItemGuide = serviceItemGuide;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the acceptUrl
	 */
	public String getAcceptUrl() {
		return acceptUrl;
	}
	/**
	 * @param acceptUrl the acceptUrl to set
	 */
	public void setAcceptUrl(String acceptUrl) {
		this.acceptUrl = acceptUrl;
	}
	/**
	 * @return the complaintUrl
	 */
	public String getComplaintUrl() {
		return complaintUrl;
	}
	/**
	 * @param complaintUrl the complaintUrl to set
	 */
	public void setComplaintUrl(String complaintUrl) {
		this.complaintUrl = complaintUrl;
	}
	/**
	 * @return the consultUrl
	 */
	public String getConsultUrl() {
		return consultUrl;
	}
	/**
	 * @param consultUrl the consultUrl to set
	 */
	public void setConsultUrl(String consultUrl) {
		this.consultUrl = consultUrl;
	}

	/**
	 * @return the consults
	 */
	public List getConsults() {
		return consults;
	}

	/**
	 * @param consults the consults to set
	 */
	public void setConsults(List consults) {
		this.consults = consults;
	}

	/**
	 * @return the complaints
	 */
	public List getComplaints() {
		return complaints;
	}

	/**
	 * @param complaints the complaints to set
	 */
	public void setComplaints(List complaints) {
		this.complaints = complaints;
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
	 * @return the querySupport
	 */
	public char getQuerySupport() {
		return querySupport;
	}

	/**
	 * @param querySupport the querySupport to set
	 */
	public void setQuerySupport(char querySupport) {
		this.querySupport = querySupport;
	}

	/**
	 * @return the queryUrl
	 */
	public String getQueryUrl() {
		return queryUrl;
	}

	/**
	 * @param queryUrl the queryUrl to set
	 */
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}

	/**
	 * @return the faqs
	 */
	public Set getFaqs() {
		return faqs;
	}

	/**
	 * @param faqs the faqs to set
	 */
	public void setFaqs(Set faqs) {
		this.faqs = faqs;
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
	 * @return the resultSupport
	 */
	public char getResultSupport() {
		return resultSupport;
	}

	/**
	 * @param resultSupport the resultSupport to set
	 */
	public void setResultSupport(char resultSupport) {
		this.resultSupport = resultSupport;
	}

	/**
	 * @return the resultUrl
	 */
	public String getResultUrl() {
		return resultUrl;
	}

	/**
	 * @param resultUrl the resultUrl to set
	 */
	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}

	/**
	 * @return the publicServiceType
	 */
	public String getPublicServiceType() {
		return publicServiceType;
	}

	/**
	 * @param publicServiceType the publicServiceType to set
	 */
	public void setPublicServiceType(String publicServiceType) {
		this.publicServiceType = publicServiceType;
	}
}