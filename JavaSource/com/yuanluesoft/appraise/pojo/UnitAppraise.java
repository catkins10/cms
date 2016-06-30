package com.yuanluesoft.appraise.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:单位评议统计(appraise_unit_appraise)
 * @author linchuan
 *
 */
public class UnitAppraise extends Record {
	private long appraiseId; //评议ID
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String unitCategory; //单位分类,经济和社会管理部门、行政执法监督部门、公共服务行业
	private int importVoteTotal; //引用投票数,专家评议时,计入服务对象网络投票结果
	private double importScoreTotal; //引用投票得分
	private int voteTotal; //本期投票数
	private int smsVoteTotal; //本期短信投票数
	private double smsScoreTotal; //本期短信投票得分
	private int internetVoteTotal; //本期网络投票数
	private double internetScoreTotal; //本期网络投票得分
	private double scoreComprehensive; //本期综合得分
	private int yearVoteTotal; //累计投票数,截止到本期
	private int yearSmsVoteTotal; //累计短信投票数,截止到本期
	private double yearSmsScoreTotal; //累计短信投票得分,截止到本期
	private int yearInternetVoteTotal; //累计网络投票数,截止到本期
	private double yearInternetScoreTotal; //累计网络投票得分,截止到本期
	private double yearScoreComprehensive; //累计综合得分,截止到本期
	private Timestamp created; //发起时间
	private Set votes; //评议结果列表
	private Set optionVotes; //选项统计列表

	/**
	 * 生成建议输入框
	 * @return
	 */
	public String getAppraiseProposeInputBox() {
		return "<textarea class=\"appraisePropose\" name=\"appraisePropose_" + getId() + "\"></textarea>";
	}
	
	/**
	 * @return the appraiseId
	 */
	public long getAppraiseId() {
		return appraiseId;
	}
	/**
	 * @param appraiseId the appraiseId to set
	 */
	public void setAppraiseId(long appraiseId) {
		this.appraiseId = appraiseId;
	}
	/**
	 * @return the internetVoteTotal
	 */
	public int getInternetVoteTotal() {
		return internetVoteTotal;
	}
	/**
	 * @param internetVoteTotal the internetVoteTotal to set
	 */
	public void setInternetVoteTotal(int internetVoteTotal) {
		this.internetVoteTotal = internetVoteTotal;
	}
	/**
	 * @return the scoreComprehensive
	 */
	public double getScoreComprehensive() {
		return scoreComprehensive;
	}
	/**
	 * @param scoreComprehensive the scoreComprehensive to set
	 */
	public void setScoreComprehensive(double scoreComprehensive) {
		this.scoreComprehensive = scoreComprehensive;
	}
	/**
	 * @return the smsVoteTotal
	 */
	public int getSmsVoteTotal() {
		return smsVoteTotal;
	}
	/**
	 * @param smsVoteTotal the smsVoteTotal to set
	 */
	public void setSmsVoteTotal(int smsVoteTotal) {
		this.smsVoteTotal = smsVoteTotal;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the voteTotal
	 */
	public int getVoteTotal() {
		return voteTotal;
	}
	/**
	 * @param voteTotal the voteTotal to set
	 */
	public void setVoteTotal(int voteTotal) {
		this.voteTotal = voteTotal;
	}
	/**
	 * @return the optionVotes
	 */
	public Set getOptionVotes() {
		return optionVotes;
	}
	/**
	 * @param optionVotes the optionVotes to set
	 */
	public void setOptionVotes(Set optionVotes) {
		this.optionVotes = optionVotes;
	}
	/**
	 * @return the unitCategory
	 */
	public String getUnitCategory() {
		return unitCategory;
	}
	/**
	 * @param unitCategory the unitCategory to set
	 */
	public void setUnitCategory(String unitCategory) {
		this.unitCategory = unitCategory;
	}

	/**
	 * @return the votes
	 */
	public Set getVotes() {
		return votes;
	}

	/**
	 * @param votes the votes to set
	 */
	public void setVotes(Set votes) {
		this.votes = votes;
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
	 * @return the importScoreTotal
	 */
	public double getImportScoreTotal() {
		return importScoreTotal;
	}

	/**
	 * @param importScoreTotal the importScoreTotal to set
	 */
	public void setImportScoreTotal(double importScoreTotal) {
		this.importScoreTotal = importScoreTotal;
	}

	/**
	 * @return the importVoteTotal
	 */
	public int getImportVoteTotal() {
		return importVoteTotal;
	}

	/**
	 * @param importVoteTotal the importVoteTotal to set
	 */
	public void setImportVoteTotal(int importVoteTotal) {
		this.importVoteTotal = importVoteTotal;
	}

	/**
	 * @return the yearInternetVoteTotal
	 */
	public int getYearInternetVoteTotal() {
		return yearInternetVoteTotal;
	}

	/**
	 * @param yearInternetVoteTotal the yearInternetVoteTotal to set
	 */
	public void setYearInternetVoteTotal(int yearInternetVoteTotal) {
		this.yearInternetVoteTotal = yearInternetVoteTotal;
	}

	/**
	 * @return the yearScoreComprehensive
	 */
	public double getYearScoreComprehensive() {
		return yearScoreComprehensive;
	}

	/**
	 * @param yearScoreComprehensive the yearScoreComprehensive to set
	 */
	public void setYearScoreComprehensive(double yearScoreComprehensive) {
		this.yearScoreComprehensive = yearScoreComprehensive;
	}

	/**
	 * @return the yearSmsVoteTotal
	 */
	public int getYearSmsVoteTotal() {
		return yearSmsVoteTotal;
	}

	/**
	 * @param yearSmsVoteTotal the yearSmsVoteTotal to set
	 */
	public void setYearSmsVoteTotal(int yearSmsVoteTotal) {
		this.yearSmsVoteTotal = yearSmsVoteTotal;
	}

	/**
	 * @return the yearVoteTotal
	 */
	public int getYearVoteTotal() {
		return yearVoteTotal;
	}

	/**
	 * @param yearVoteTotal the yearVoteTotal to set
	 */
	public void setYearVoteTotal(int yearVoteTotal) {
		this.yearVoteTotal = yearVoteTotal;
	}

	/**
	 * @return the internetScoreTotal
	 */
	public double getInternetScoreTotal() {
		return internetScoreTotal;
	}

	/**
	 * @param internetScoreTotal the internetScoreTotal to set
	 */
	public void setInternetScoreTotal(double internetScoreTotal) {
		this.internetScoreTotal = internetScoreTotal;
	}

	/**
	 * @return the smsScoreTotal
	 */
	public double getSmsScoreTotal() {
		return smsScoreTotal;
	}

	/**
	 * @param smsScoreTotal the smsScoreTotal to set
	 */
	public void setSmsScoreTotal(double smsScoreTotal) {
		this.smsScoreTotal = smsScoreTotal;
	}

	/**
	 * @return the yearInternetScoreTotal
	 */
	public double getYearInternetScoreTotal() {
		return yearInternetScoreTotal;
	}

	/**
	 * @param yearInternetScoreTotal the yearInternetScoreTotal to set
	 */
	public void setYearInternetScoreTotal(double yearInternetScoreTotal) {
		this.yearInternetScoreTotal = yearInternetScoreTotal;
	}

	/**
	 * @return the yearSmsScoreTotal
	 */
	public double getYearSmsScoreTotal() {
		return yearSmsScoreTotal;
	}

	/**
	 * @param yearSmsScoreTotal the yearSmsScoreTotal to set
	 */
	public void setYearSmsScoreTotal(double yearSmsScoreTotal) {
		this.yearSmsScoreTotal = yearSmsScoreTotal;
	}
}