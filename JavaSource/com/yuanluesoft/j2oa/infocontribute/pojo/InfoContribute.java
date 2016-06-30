package com.yuanluesoft.j2oa.infocontribute.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;

/**
 * 投稿(info_contribute)
 * @author linchuan
 *
 */
public class InfoContribute extends Record {
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
	private Timestamp created; //创建时间
	private Timestamp contributeTime; //投稿时间
	private Timestamp supplementTime; //补录时间
	private Set lazyBody; //正文
	private Set receiveUnits; //接收单位
	private Set revises; //退改稿
	private Set uses; //采用情况
	private Set instructs; //领导批示
	
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
	 * @return the instructs
	 */
	public Set getInstructs() {
		return instructs;
	}
	/**
	 * @param instructs the instructs to set
	 */
	public void setInstructs(Set instructs) {
		this.instructs = instructs;
	}
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
	 * @return the uses
	 */
	public Set getUses() {
		return uses;
	}
	/**
	 * @param uses the uses to set
	 */
	public void setUses(Set uses) {
		this.uses = uses;
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
	 * @return the supplementTime
	 */
	public Timestamp getSupplementTime() {
		return supplementTime;
	}
	/**
	 * @param supplementTime the supplementTime to set
	 */
	public void setSupplementTime(Timestamp supplementTime) {
		this.supplementTime = supplementTime;
	}
	/**
	 * @return the receiveUnits
	 */
	public Set getReceiveUnits() {
		return receiveUnits;
	}
	/**
	 * @param receiveUnits the receiveUnits to set
	 */
	public void setReceiveUnits(Set receiveUnits) {
		this.receiveUnits = receiveUnits;
	}
}