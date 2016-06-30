package com.yuanluesoft.bidding.project.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FileConfig extends ActionForm {
	private String[] categoryArray; //适用的工程分类列表,系统预设，如建安、市政
	private String[] procedureArray; //适用的招标内容列表,系统预设，如施工、监理、设计
	private String[] cityArray; //适用的地区列表
	
	private Set items; //资料条目列表
	
	//添加前期资料相关
	private long prophaseFileId; //前期资料ID
	private double prophaseFileSn; //前期资料序号
	private String prophaseFileName; //前期资料名称
	private char needFull = '0'; //是否要求全部齐全才能继续
	
	//添加前期资料相关
	private long archiveFileId; //归档资料Id
	private double archiveFileSn; //归档资料序号
	private String archiveFileName; //归档资料名称
	private String archiveFileCategory; //归档资料分类
	
	/**
	 * @return the archiveFileCategory
	 */
	public String getArchiveFileCategory() {
		return archiveFileCategory;
	}
	/**
	 * @param archiveFileCategory the archiveFileCategory to set
	 */
	public void setArchiveFileCategory(String archiveFileCategory) {
		this.archiveFileCategory = archiveFileCategory;
	}
	/**
	 * @return the archiveFileId
	 */
	public long getArchiveFileId() {
		return archiveFileId;
	}
	/**
	 * @param archiveFileId the archiveFileId to set
	 */
	public void setArchiveFileId(long archiveFileId) {
		this.archiveFileId = archiveFileId;
	}
	/**
	 * @return the archiveFileName
	 */
	public String getArchiveFileName() {
		return archiveFileName;
	}
	/**
	 * @param archiveFileName the archiveFileName to set
	 */
	public void setArchiveFileName(String archiveFileName) {
		this.archiveFileName = archiveFileName;
	}
	/**
	 * @return the archiveFileSn
	 */
	public double getArchiveFileSn() {
		return archiveFileSn;
	}
	/**
	 * @param archiveFileSn the archiveFileSn to set
	 */
	public void setArchiveFileSn(double archiveFileSn) {
		this.archiveFileSn = archiveFileSn;
	}
	/**
	 * @return the prophaseFileId
	 */
	public long getProphaseFileId() {
		return prophaseFileId;
	}
	/**
	 * @param prophaseFileId the prophaseFileId to set
	 */
	public void setProphaseFileId(long prophaseFileId) {
		this.prophaseFileId = prophaseFileId;
	}
	/**
	 * @return the prophaseFileName
	 */
	public String getProphaseFileName() {
		return prophaseFileName;
	}
	/**
	 * @param prophaseFileName the prophaseFileName to set
	 */
	public void setProphaseFileName(String prophaseFileName) {
		this.prophaseFileName = prophaseFileName;
	}
	/**
	 * @return the prophaseFileSn
	 */
	public double getProphaseFileSn() {
		return prophaseFileSn;
	}
	/**
	 * @param prophaseFileSn the prophaseFileSn to set
	 */
	public void setProphaseFileSn(double prophaseFileSn) {
		this.prophaseFileSn = prophaseFileSn;
	}
	/**
	 * @return the items
	 */
	public Set getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(Set items) {
		this.items = items;
	}
	/**
	 * @return the categoryArray
	 */
	public String[] getCategoryArray() {
		return categoryArray;
	}
	/**
	 * @param categoryArray the categoryArray to set
	 */
	public void setCategoryArray(String[] categoryArray) {
		this.categoryArray = categoryArray;
	}
	/**
	 * @return the cityArray
	 */
	public String[] getCityArray() {
		return cityArray;
	}
	/**
	 * @param cityArray the cityArray to set
	 */
	public void setCityArray(String[] cityArray) {
		this.cityArray = cityArray;
	}
	/**
	 * @return the procedureArray
	 */
	public String[] getProcedureArray() {
		return procedureArray;
	}
	/**
	 * @param procedureArray the procedureArray to set
	 */
	public void setProcedureArray(String[] procedureArray) {
		this.procedureArray = procedureArray;
	}
	/**
	 * @return the needFull
	 */
	public char getNeedFull() {
		return needFull;
	}
	/**
	 * @param needFull the needFull to set
	 */
	public void setNeedFull(char needFull) {
		this.needFull = needFull;
	}
}
