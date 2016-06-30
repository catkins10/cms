package com.yuanluesoft.j2oa.info.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;

/**
 * 信息采编:来稿(info_receive)
 * @author linchuan
 *
 */
public class InfoReceive extends Record {
	private String subject; //标题
	private String keywords; //主题词
	private String presetMagazines; //预选刊型
	private String secretLevel; //密级,普通 秘密 机密
	private String fromUnit; //来稿单位名称
	private long fromUnitId; //来稿单位ID
	private String source; //信息来源
	private String sn; //期数
	private String editor; //责任编辑
	private String editorTel; //电话
	private String signer; //签发领导
	private String signerTel; //签发领导电话
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp contributeTime; //投稿时间
	private long filterPersonId; //筛选人ID
	private String filterPerson; //筛选人
	private Timestamp filterTime; //筛选时间
	private String filterOpinion; //筛选意见
	private Set lazyBody; //正文
	private Set infoFilters; //过滤后的信息列表
	private Set revises; //退改稿记录
	
	/**
	 * @return the revises
	 */
	public Set getRevises() {
		return revises;
	}

	/**
	 * @param revises the revises to set
	 */
	public void setRevises(Set revises) {
		this.revises = revises;
	}

	/**
	 * 获取正文
	 */
	public String getBody() {
		return LazyBodyUtils.getBody(this);
	}
	
	/**
	 * 设置正文
	 * @param body
	 */
	public void setBody(String body) {
		LazyBodyUtils.setBody(this, body);
	}
	
	/**
	 * @return the contributeTime
	 */
	public Timestamp getContributeTime() {
		return contributeTime;
	}
	/**
	 * @param contributeTime the contributeTime to set
	 */
	public void setContributeTime(Timestamp contributeTime) {
		this.contributeTime = contributeTime;
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
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/**
	 * @return the editorTel
	 */
	public String getEditorTel() {
		return editorTel;
	}
	/**
	 * @param editorTel the editorTel to set
	 */
	public void setEditorTel(String editorTel) {
		this.editorTel = editorTel;
	}
	/**
	 * @return the filterOpinion
	 */
	public String getFilterOpinion() {
		return filterOpinion;
	}
	/**
	 * @param filterOpinion the filterOpinion to set
	 */
	public void setFilterOpinion(String filterOpinion) {
		this.filterOpinion = filterOpinion;
	}
	/**
	 * @return the filterPerson
	 */
	public String getFilterPerson() {
		return filterPerson;
	}
	/**
	 * @param filterPerson the filterPerson to set
	 */
	public void setFilterPerson(String filterPerson) {
		this.filterPerson = filterPerson;
	}
	/**
	 * @return the filterPersonId
	 */
	public long getFilterPersonId() {
		return filterPersonId;
	}
	/**
	 * @param filterPersonId the filterPersonId to set
	 */
	public void setFilterPersonId(long filterPersonId) {
		this.filterPersonId = filterPersonId;
	}
	/**
	 * @return the filterTime
	 */
	public Timestamp getFilterTime() {
		return filterTime;
	}
	/**
	 * @param filterTime the filterTime to set
	 */
	public void setFilterTime(Timestamp filterTime) {
		this.filterTime = filterTime;
	}
	/**
	 * @return the fromUnit
	 */
	public String getFromUnit() {
		return fromUnit;
	}
	/**
	 * @param fromUnit the fromUnit to set
	 */
	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
	}
	/**
	 * @return the fromUnitId
	 */
	public long getFromUnitId() {
		return fromUnitId;
	}
	/**
	 * @param fromUnitId the fromUnitId to set
	 */
	public void setFromUnitId(long fromUnitId) {
		this.fromUnitId = fromUnitId;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the presetMagazines
	 */
	public String getPresetMagazines() {
		return presetMagazines;
	}
	/**
	 * @param presetMagazines the presetMagazines to set
	 */
	public void setPresetMagazines(String presetMagazines) {
		this.presetMagazines = presetMagazines;
	}
	/**
	 * @return the secretLevel
	 */
	public String getSecretLevel() {
		return secretLevel;
	}
	/**
	 * @param secretLevel the secretLevel to set
	 */
	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}
	/**
	 * @return the signer
	 */
	public String getSigner() {
		return signer;
	}
	/**
	 * @param signer the signer to set
	 */
	public void setSigner(String signer) {
		this.signer = signer;
	}
	/**
	 * @return the signerTel
	 */
	public String getSignerTel() {
		return signerTel;
	}
	/**
	 * @param signerTel the signerTel to set
	 */
	public void setSignerTel(String signerTel) {
		this.signerTel = signerTel;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the lazyBody
	 */
	public Set getLazyBody() {
		return lazyBody;
	}
	/**
	 * @param lazyBody the lazyBody to set
	 */
	public void setLazyBody(Set lazyBody) {
		this.lazyBody = lazyBody;
	}

	/**
	 * @return the infoFilters
	 */
	public Set getInfoFilters() {
		return infoFilters;
	}

	/**
	 * @param infoFilters the infoFilters to set
	 */
	public void setInfoFilters(Set infoFilters) {
		this.infoFilters = infoFilters;
	}
}