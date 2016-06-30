package com.yuanluesoft.fdi.customer.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Company extends ActionForm {
	private String name; //中文名称
	private String englishName; //英文名称
	private String address; //地址,中英文，邮编
	private String webSite; //网站地址
	private String country; //母公司所在国家或地区
	private String introduction; //单位简介,包括战略布局、核心行业与产品优势、竞争力分析等
	private String chinaHeadOffice; //中国区总部名称
	private String contact; //联络方式
	private int worldTop500; //是否世界500强企业
	private String chosenYear; //入选年份
	private String ranking; //排名
	private String remark; //备注
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Set contacts; //联系人列表
	private Set industries; //行业列表

	//扩展属性
	private String industryIds; //行业分类ID
	private String industryNames; //行业分类名称

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
	 * @return the chinaHeadOffice
	 */
	public String getChinaHeadOffice() {
		return chinaHeadOffice;
	}
	/**
	 * @param chinaHeadOffice the chinaHeadOffice to set
	 */
	public void setChinaHeadOffice(String chinaHeadOffice) {
		this.chinaHeadOffice = chinaHeadOffice;
	}
	/**
	 * @return the chosenYear
	 */
	public String getChosenYear() {
		return chosenYear;
	}
	/**
	 * @param chosenYear the chosenYear to set
	 */
	public void setChosenYear(String chosenYear) {
		this.chosenYear = chosenYear;
	}
	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}
	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}
	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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
	 * @return the ranking
	 */
	public String getRanking() {
		return ranking;
	}
	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(String ranking) {
		this.ranking = ranking;
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
	 * @return the worldTop500
	 */
	public int getWorldTop500() {
		return worldTop500;
	}
	/**
	 * @param worldTop500 the worldTop500 to set
	 */
	public void setWorldTop500(int worldTop500) {
		this.worldTop500 = worldTop500;
	}
	/**
	 * @return the contacts
	 */
	public Set getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(Set contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}
	/**
	 * @param webSite the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	/**
	 * @return the industries
	 */
	public Set getIndustries() {
		return industries;
	}
	/**
	 * @param industries the industries to set
	 */
	public void setIndustries(Set industries) {
		this.industries = industries;
	}
	/**
	 * @return the industryIds
	 */
	public String getIndustryIds() {
		return industryIds;
	}
	/**
	 * @param industryIds the industryIds to set
	 */
	public void setIndustryIds(String industryIds) {
		this.industryIds = industryIds;
	}
	/**
	 * @return the industryNames
	 */
	public String getIndustryNames() {
		return industryNames;
	}
	/**
	 * @param industryNames the industryNames to set
	 */
	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}
}