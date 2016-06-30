package com.yuanluesoft.j2oa.infocontribute.soap.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @author linchuan
 *
 */
public class InfoContribute implements Serializable {
	private long id; //ID
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
	private Calendar contributed; //投稿时间
	private String body; //正文
	private String[] attachmentFileNames; //附件文件名称列表
	
	/**
	 * @return the attachmentFileNames
	 */
	public String[] getAttachmentFileNames() {
		return attachmentFileNames;
	}
	/**
	 * @param attachmentFileNames the attachmentFileNames to set
	 */
	public void setAttachmentFileNames(String[] attachmentFileNames) {
		this.attachmentFileNames = attachmentFileNames;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the contributed
	 */
	public Calendar getContributed() {
		return contributed;
	}
	/**
	 * @param contributed the contributed to set
	 */
	public void setContributed(Calendar contributed) {
		this.contributed = contributed;
	}
}