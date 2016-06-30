package com.yuanluesoft.job.talent.forms;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Intention extends ActionForm {
	private long talentId; //人才ID
	private int companyScale; //企业规模
	private int minMonthlyPay; //期望最低月薪
	private int maxMonthlyPay; //期望最高月薪
	private Date entryDate; //到岗时间
	private Set types; //求职意向工作性质
	private Set areas; //求职意向地点
	private Set industries; //求职意向行业
	private Set posts; //求职意向职能类别
	private Set companyTypes; //求职意向公司性质
	//扩展属性
	private int[] typeArray; //工作性质数组
	private String areaIds; //求职意向地点ID列表
	private String areaNames; //求职意向地点名称列表
	private String industryIds; //求职意向行业ID列表
	private String industryNames; //求职意向行业名称列表
	private String postIds; //求职意向职能类别ID列表
	private String postNames; //求职意向职能类别名称列表
	private String[] companyTypeArray; //公司性质数组
	
	/**
	 * @return the entryDate
	 */
	public Date getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	/**
	 * @return the maxMonthlyPay
	 */
	public int getMaxMonthlyPay() {
		return maxMonthlyPay;
	}
	/**
	 * @param maxMonthlyPay the maxMonthlyPay to set
	 */
	public void setMaxMonthlyPay(int maxMonthlyPay) {
		this.maxMonthlyPay = maxMonthlyPay;
	}
	/**
	 * @return the minMonthlyPay
	 */
	public int getMinMonthlyPay() {
		return minMonthlyPay;
	}
	/**
	 * @param minMonthlyPay the minMonthlyPay to set
	 */
	public void setMinMonthlyPay(int minMonthlyPay) {
		this.minMonthlyPay = minMonthlyPay;
	}
	/**
	 * @return the areas
	 */
	public Set getAreas() {
		return areas;
	}
	/**
	 * @param areas the areas to set
	 */
	public void setAreas(Set areas) {
		this.areas = areas;
	}
	/**
	 * @return the talentId
	 */
	public long getTalentId() {
		return talentId;
	}
	/**
	 * @param talentId the talentId to set
	 */
	public void setTalentId(long talentId) {
		this.talentId = talentId;
	}
	/**
	 * @return the types
	 */
	public Set getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(Set types) {
		this.types = types;
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
	 * @return the posts
	 */
	public Set getPosts() {
		return posts;
	}
	/**
	 * @param posts the posts to set
	 */
	public void setPosts(Set posts) {
		this.posts = posts;
	}
	/**
	 * @return the areaIds
	 */
	public String getAreaIds() {
		return areaIds;
	}
	/**
	 * @param areaIds the areaIds to set
	 */
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	/**
	 * @return the areaNames
	 */
	public String getAreaNames() {
		return areaNames;
	}
	/**
	 * @param areaNames the areaNames to set
	 */
	public void setAreaNames(String areaNames) {
		this.areaNames = areaNames;
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
	/**
	 * @return the postIds
	 */
	public String getPostIds() {
		return postIds;
	}
	/**
	 * @param postIds the postIds to set
	 */
	public void setPostIds(String postIds) {
		this.postIds = postIds;
	}
	/**
	 * @return the postNames
	 */
	public String getPostNames() {
		return postNames;
	}
	/**
	 * @param postNames the postNames to set
	 */
	public void setPostNames(String postNames) {
		this.postNames = postNames;
	}
	/**
	 * @return the typeArray
	 */
	public int[] getTypeArray() {
		return typeArray;
	}
	/**
	 * @param typeArray the typeArray to set
	 */
	public void setTypeArray(int[] typeArray) {
		this.typeArray = typeArray;
	}
	/**
	 * @return the companyScale
	 */
	public int getCompanyScale() {
		return companyScale;
	}
	/**
	 * @param companyScale the companyScale to set
	 */
	public void setCompanyScale(int companyScale) {
		this.companyScale = companyScale;
	}
	/**
	 * @return the companyTypeArray
	 */
	public String[] getCompanyTypeArray() {
		return companyTypeArray;
	}
	/**
	 * @param companyTypeArray the companyTypeArray to set
	 */
	public void setCompanyTypeArray(String[] companyTypeArray) {
		this.companyTypeArray = companyTypeArray;
	}
	/**
	 * @return the companyTypes
	 */
	public Set getCompanyTypes() {
		return companyTypes;
	}
	/**
	 * @param companyTypes the companyTypes to set
	 */
	public void setCompanyTypes(Set companyTypes) {
		this.companyTypes = companyTypes;
	}
}