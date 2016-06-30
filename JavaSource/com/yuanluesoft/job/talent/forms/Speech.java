package com.yuanluesoft.job.talent.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Speech extends ActionForm {
	private long talentId; //人才ID
	private String language; //语言类别,英语,日语,俄语,阿拉伯语,法语,德语,西班牙语,葡萄牙语,意大利语,韩语/朝鲜语,普通话,粤语,闽南语,上海话,其它
	private String level; //掌握程度,不限,一般,良好,熟练,精通
	private String literacy; //读写能力,不限,一般,良好,熟练,精通
	private String spoken; //听说能力,不限,一般,良好,熟练,精通
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the literacy
	 */
	public String getLiteracy() {
		return literacy;
	}
	/**
	 * @param literacy the literacy to set
	 */
	public void setLiteracy(String literacy) {
		this.literacy = literacy;
	}
	/**
	 * @return the spoken
	 */
	public String getSpoken() {
		return spoken;
	}
	/**
	 * @param spoken the spoken to set
	 */
	public void setSpoken(String spoken) {
		this.spoken = spoken;
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
}