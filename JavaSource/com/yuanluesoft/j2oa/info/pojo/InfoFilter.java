package com.yuanluesoft.j2oa.info.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.util.LazyBodyUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 信息采编:拟采用稿件(info_filter)
 * @author linchuan
 *
 */
public class InfoFilter extends WorkflowData {
	private long infoReceiveId; //来稿ID
	private long magazineDefineId; //刊物配置ID
	private String magazineName; //刊物名称
	private String subject; //标题
	private String level; //采用级别,普通 优先
	private int isBrief; //是否简讯
	private int isVerified; //是否核实
	private int isCircular; //是否通报
	private int isCombined; //是否多条合一
	private int isBeCombined; //是否被合并
	private String combineInfoIds; //合并的稿件ID
	private int status; //状态,0/审核中,1/未录用,2/待排版,3/已排版,4/已定版
	private long magazineId; //刊物ID
	private String magazineColumn; //栏目
	private double priority; //序号
	private Timestamp issueTime; //定版时间
	private int magazineSN; //刊物期数
	private long supplementPersonId; //补录人ID
	private String supplementPerson; //补录人
	private Timestamp supplementTime; //补录时间
	private InfoReceive infoReceive; //来稿
	private Set lazyBody; //正文
	private Set sendHighers; //报送情况
	private Set instructs; //领导批示
	private Set revises; //退改稿记录
	
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
	 * @return the combineInfoIds
	 */
	public String getCombineInfoIds() {
		return combineInfoIds;
	}
	/**
	 * @param combineInfoIds the combineInfoIds to set
	 */
	public void setCombineInfoIds(String combineInfoIds) {
		this.combineInfoIds = combineInfoIds;
	}
	/**
	 * @return the isBeCombined
	 */
	public int getIsBeCombined() {
		return isBeCombined;
	}
	/**
	 * @param isBeCombined the isBeCombined to set
	 */
	public void setIsBeCombined(int isBeCombined) {
		this.isBeCombined = isBeCombined;
	}
	/**
	 * @return the isBrief
	 */
	public int getIsBrief() {
		return isBrief;
	}
	/**
	 * @param isBrief the isBrief to set
	 */
	public void setIsBrief(int isBrief) {
		this.isBrief = isBrief;
	}
	/**
	 * @return the isCircular
	 */
	public int getIsCircular() {
		return isCircular;
	}
	/**
	 * @param isCircular the isCircular to set
	 */
	public void setIsCircular(int isCircular) {
		this.isCircular = isCircular;
	}
	/**
	 * @return the isCombined
	 */
	public int getIsCombined() {
		return isCombined;
	}
	/**
	 * @param isCombined the isCombined to set
	 */
	public void setIsCombined(int isCombined) {
		this.isCombined = isCombined;
	}
	/**
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	/**
	 * @return the isVerified
	 */
	public int getIsVerified() {
		return isVerified;
	}
	/**
	 * @param isVerified the isVerified to set
	 */
	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
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
	 * @return the magazineDefineId
	 */
	public long getMagazineDefineId() {
		return magazineDefineId;
	}
	/**
	 * @param magazineDefineId the magazineDefineId to set
	 */
	public void setMagazineDefineId(long magazineDefineId) {
		this.magazineDefineId = magazineDefineId;
	}
	/**
	 * @return the magazineName
	 */
	public String getMagazineName() {
		return magazineName;
	}
	/**
	 * @param magazineName the magazineName to set
	 */
	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}
	/**
	 * @return the magazineSN
	 */
	public int getMagazineSN() {
		return magazineSN;
	}
	/**
	 * @param magazineSN the magazineSN to set
	 */
	public void setMagazineSN(int magazineSN) {
		this.magazineSN = magazineSN;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the supplementPerson
	 */
	public String getSupplementPerson() {
		return supplementPerson;
	}
	/**
	 * @param supplementPerson the supplementPerson to set
	 */
	public void setSupplementPerson(String supplementPerson) {
		this.supplementPerson = supplementPerson;
	}
	/**
	 * @return the supplementPersonId
	 */
	public long getSupplementPersonId() {
		return supplementPersonId;
	}
	/**
	 * @param supplementPersonId the supplementPersonId to set
	 */
	public void setSupplementPersonId(long supplementPersonId) {
		this.supplementPersonId = supplementPersonId;
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
	 * @return the infoReceive
	 */
	public InfoReceive getInfoReceive() {
		return infoReceive;
	}
	/**
	 * @param infoReceive the infoReceive to set
	 */
	public void setInfoReceive(InfoReceive infoReceive) {
		this.infoReceive = infoReceive;
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
	 * @return the sendHighers
	 */
	public Set getSendHighers() {
		return sendHighers;
	}
	/**
	 * @param sendHighers the sendHighers to set
	 */
	public void setSendHighers(Set sendHighers) {
		this.sendHighers = sendHighers;
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
	 * @return the infoReceiveId
	 */
	public long getInfoReceiveId() {
		return infoReceiveId;
	}

	/**
	 * @param infoReceiveId the infoReceiveId to set
	 */
	public void setInfoReceiveId(long infoReceiveId) {
		this.infoReceiveId = infoReceiveId;
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
	 * @return the magazineColumn
	 */
	public String getMagazineColumn() {
		return magazineColumn;
	}

	/**
	 * @param magazineColumn the magazineColumn to set
	 */
	public void setMagazineColumn(String magazineColumn) {
		this.magazineColumn = magazineColumn;
	}

	/**
	 * @return the magazineId
	 */
	public long getMagazineId() {
		return magazineId;
	}

	/**
	 * @param magazineId the magazineId to set
	 */
	public void setMagazineId(long magazineId) {
		this.magazineId = magazineId;
	}

	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
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
}